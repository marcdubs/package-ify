package com.marcusman.packageify;

/**
* Struct that represents a packaged file.
* @author Marcus Dubreuil
* @version 1.0
*/
public class PackagedFile {
	public String className;
	public String path;

	/**
	* Create a PackagedFile with given path and className.
	* @param path Full package path of class.
	* @param className Name of class.
	*/
	public PackagedFile(String path, String className) {
		this.path = path;
		this.className = className;
	}

	/**
	* Retreive the full package definition for the PackagedFile's path
	* @return "package com.marcusman.utils;" if path is "com.marcusman.utils"
	*/
	public String getPackageDefinition() {
		return "package " + path + ";";
	}

	/**
	* Returns the package path in directory form
	* @return com/marcusman/packageify if the package is com.marcusman.packageify
	* or null if the package is empty.
	*/
	public String packageToDirectory() {
		if(path.length() == 0)
			return null;

		String outputPath = "";

		if(!path.contains("."))
			return path;

		String[] splitPath = path.split("\\.");

		outputPath = splitPath[0];

		for(int i = 1; i < splitPath.length; i++)
			outputPath += '/' + splitPath[i];

		return outputPath;
	}

	public String toString() {
		return "Class Name: " + className + " Package: " + path;
	}
}