package com.marcusman.packageify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.util.ArrayList;
import java.util.Scanner;

/**
* Packageify puts files that were originally all in the same folder.
* in organized packages based on input information.
*
* <pre>
* Program arguments:
* (path to folder with all java files) (package definition txt file) (output folder)
* 
* Example of package definition file:
* AnimatedSprite;com.marcusman.graphics
* Game;com.marcusman
* GameObject;com.marcusman.utils
* GUI;com.marcusman.graphics.gui
* GUIButton;com.marcusman.graphics.gui
* KeyBoardListener;com.marcusman.input
* Map;com.marcusman.logic
* MouseEventListener;com.marcusman.input
* Player;com.marcusman.logic
* Rectangle;com.marcusman.utils
* RenderHandler;com.marcusman.graphics
* SDKButton;com.marcusman.graphics.gui
* Sprite;com.marcusman.graphics
* SpriteSheet;com.marcusman.graphics
* Tiles;com.marcusman.logic
* </pre>
* @author Marcus Dubreuil
* @version 1.0
*/
public class Packageify {

	private File inputFolder;
	private File definitionFile;
	private File outputFolder;

	private ArrayList<PackagedFile> packagedFiles;

	/**
	* Packageify the given directory with the description given above.
	* @param args args[0] = inputFolder args[1] = definitionFile args[2] = outputFolder.
	*/
	public Packageify(String[] args) throws Exception {
		if(args.length < 3)
			throw new IllegalArgumentException("Expected at least 3 arguments and Received " + args.length + "!");

		inputFolder = new File(args[0]);
		definitionFile = new File(args[1]);
		outputFolder = new File(args[2]);

		//Validate arguments and create output folder
		validateFiles();

		createPackagedFilesList();
		putFilesIntoPackageFolders();
		copyNonJavaFiles();

	}

	/**
	* Copy all non-java files from the inputDir to the outputDir
	*/
	private void copyNonJavaFiles() throws IOException {
		File[] files = inputFolder.listFiles();

		for(int i = 0; i < files.length; i++) {
			File file = files[i];
			if(!file.getName().endsWith(".java"))
				Files.copy(file.toPath(),
						   new File(outputFolder + "/" + file.getName()).toPath(),
						   StandardCopyOption.REPLACE_EXISTING);
		}
	}

	/**
	* Copy all of the java files in the input directory into the output directory under
	* their package directories and put the package definition at the top of the file.
	* Also try to add imports to other classes that have been packageified if they
	* are found to be used in this class.
	*/
	private void putFilesIntoPackageFolders() throws IOException {
		for(int i = 0; i < packagedFiles.size(); i++) {
			PackagedFile packagedFile = packagedFiles.get(i);

			File containingFolder = new File(outputFolder.getAbsolutePath() + '/' + packagedFile.packageToDirectory());
			containingFolder.mkdirs();

			File inputClassFile = new File(inputFolder.getAbsolutePath() + '/' + packagedFile.className + ".java");
			if(!inputClassFile.exists())
				throw new IOException("File " + packagedFile.className + " does not exist at " + inputClassFile.getAbsolutePath());

			File outputClassFile = new File(containingFolder.getAbsolutePath() + '/' + packagedFile.className + ".java");

			//Copy input java class to sting
			FileInputStream fis = new FileInputStream(inputClassFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			StringBuilder sb = new StringBuilder();
			String line = "";

			while(line != null) {
				sb.append(line).append("\n");
				line = br.readLine();
			}

			String inserts = packagedFile.getPackageDefinition() + "\n\n";

			inserts += getPackagedImports(packagedFile, sb.toString());

			//Add package definition to beginning of file string
			sb.insert(0, inserts);
			fis.close();

			//Output file string to new file
			FileOutputStream fos = new FileOutputStream(outputClassFile);
			fos.write(sb.toString().getBytes());
			fos.flush();
		}
	}

	/**
	* Find the usage of other packaged files in this one and return a string of imports
	* if any given packaged file is in use.
	* @param consideredFile The packaged file we are considering.
	* @param fileString The packaged file we are considering in string form.
	* @return a string with \n separated import statements.
	*/
	private String getPackagedImports(PackagedFile consideredFile, String fileString) {
		String imports = "";

		//Get the file without quotes.
		String withoutQuotes = fileString.replaceAll("\".*?\"|\'.*?\'|`.*`", "");

		for(int i = 0; i < packagedFiles.size(); i++) {
			PackagedFile packagedFile = packagedFiles.get(i);

			if(packagedFile == consideredFile)
				continue;

			if(withoutQuotes.contains(packagedFile.className)) {
				imports += "import " + packagedFile.path + "." + packagedFile.className + ";\n";
			}
		}

		return imports;
	}

	/**
	* Initialize and fill in our packagedFiles ArrayList with the information
	* provided in the individual lines of the definition file.
	*/
	private void createPackagedFilesList() throws IOException {
		packagedFiles = new ArrayList<PackagedFile>();

		Scanner scanner = new Scanner(definitionFile);

		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if(line.length() == 0)
				continue;

			if(!line.contains(";"))
				throw new IOException("Expected semicolon in definition file for line: \n" + line);

			String[] splitLine = line.split(";");

			if(splitLine.length < 2)
				throw new IOException("Expected two strings around semicolon for line: \n" + line);

			if(splitLine[0].length() == 0 || splitLine[1].length() == 0)
				throw new IOException("Expected two strings around semicolon for line: \n" + line);

			packagedFiles.add(new PackagedFile(splitLine[1], splitLine[0]));
		}
	}

	/**
	* Validate that all 3 files and directories (inputFolder, definitionFile, and outputFile) exist
	* or are accessible.
	*/
	private void validateFiles() {
		if(!inputFolder.exists())
			throw new IllegalArgumentException("Input folder does not exist!");

		if(!inputFolder.isDirectory())
			throw new IllegalArgumentException("Input folder is not a directory!");

		File[] files = inputFolder.listFiles();

		if(files.length == 0)
			throw new IllegalArgumentException("Input folder contains no files!");

		boolean foundJava = false;

		for(int i = 0; i < files.length; i++)
			if(files[i].getName().endsWith(".java"))
				foundJava = true;

		if(!foundJava)
			throw new IllegalArgumentException("Input folder does not contain any Java files!");

		if(!definitionFile.exists())
			throw new IllegalArgumentException("Package Definition File does not exist!");

		outputFolder.mkdirs();

	}

    public static void main(String[] args) throws Exception {
        new Packageify(args);
    }
}
