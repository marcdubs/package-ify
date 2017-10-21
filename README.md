# Package-ify

Inserts files into Java package directories and adds imports for package-ified files that were previously all in the same package (in the same folder)<br><br>

Usage:<br>
Packageify puts files that were originally all in the same folder in organized packages based on input information.

Program arguments:
(path to folder with all java files) (package definition txt file) (output folder)

Example of package definition file:<br>
AnimatedSprite;com.marcusman.graphics<br>
Game;com.marcusman<br>
GameObject;com.marcusman.utils<br>
GUI;com.marcusman.graphics.gui<br>
GUIButton;com.marcusman.graphics.gui<br>
KeyBoardListener;com.marcusman.input<br>
Map;com.marcusman.logic<br>
MouseEventListener;com.marcusman.input<br>
Player;com.marcusman.logic<br>
Rectangle;com.marcusman.utils<br>
RenderHandler;com.marcusman.graphics<br>
SDKButton;com.marcusman.graphics.gui<br>
Sprite;com.marcusman.graphics<br>
SpriteSheet;com.marcusman.graphics<br>
Tiles;com.marcusman.logic<br>
