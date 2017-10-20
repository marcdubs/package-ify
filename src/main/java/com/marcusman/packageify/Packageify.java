package com.marcusman.packageify;

import java.io.File;

/**
* Packageify puts files that were originally all in the same folder
* in organized packages based on input information.
*
* <pre>
* Program arguments:
* (path to folder with all java files) (package definition txt file) (output folder)
* 
* Example of package definition file:
* AnimatedSprite;com.marcusman.graphics.AnimatedSprite
* Game;com.marcusman.Game
* GameObject;com.marcusman.utils.GameObject
* GUI;com.marcusman.graphics.gui.GUI
* GUIButton;com.marcusman.graphics.gui.GUIButton
* KeyBoardListener;com.marcusman.input.KeyBoardListener
* Map;com.marcusman.logic.Map
* MouseEventListener;com.marcusman.input.MouseEventListener
* Player;com.marcusman.logic.Player
* Rectangle;com.marcusman.utils.Rectangle
* RenderHandler;com.marcusman.graphics.RenderHandler
* SDKButton;com.marcusman.graphics.gui.SDKButton
* Sprite;com.marcusman.graphics.Sprite
* SpriteSheet;com.marcusman.graphics.SpriteSheet
* Tiles;com.marcusman.logic.Tiles
* </pre>
* @author Marcus Dubreuil
* @version 1.0
*/
public class Packageify {

	private File inputFolder;
	private File definitionFile;
	private File outputFolder;

	/**
	* Packageify the given directory with the description given above
	* @param args args[0] = inputFolder args[1] = definitionFile args[2] = outputFolder
	*/
	public Packageify(String[] args) throws IllegalArgumentException {
		if(args.length < 3)
			throw new IllegalArgumentException("Expected at least 3 arguments and Received " + args.length + "!");

		inputFolder = new File(args[0]);
		definitionFile = new File(args[1]);
		outputFolder = new File(args[2]);
		validateFiles();

	}

	/**
	* Validate that all 3 files and directories (inputFolder, definitionFile, and outputFile) exist
	* or are accessible
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

    public static void main(String[] args) {
        new Packageify(args);
    }
}
