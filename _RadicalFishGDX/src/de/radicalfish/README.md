Radical Fish - Main Package
=================

This package contains all sub-packages and the most basic classes used by the engine including the ```Game``` interface and the ```GameContainer``` class. The later is the starting point of all games written with the **RadicalFishEngine**.
In the following you will learn how to build a simple game using the **Game** interface and the **GameContainer** class.

[The GameContainer][1]
-----------------

The heart and soul of the engine is this little class. It extends the [ApplicationListener][2] from libgdx. The main use of the class is to handle the calls for the [Game][3] class. It provides you with a single [Graphics][4] object which gives you a Slick2D/Java2D like way of handling the matrix context of the game. You can scale and offset the matrix for all upcoming drawing and also push and pop these changes. 
The **GameContainer** has some other neat features too:

* clipping the container. 
* (currently) 2 view types: streched and fixed
	* fixed is useful for debugging where the container sticks to the top left of the screen not matter the size if the window. The debug panels can move freely through the window tho! 
* handling debug mechanism for desktop projects
* setting a default [Font][5] when not setting one 

The **GameContainer** is the class you add as ApplicationListener to the platform specific project starter classes. Let's take a look at a desktop project:

```Java
public static void main(String[] args) {
	LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
	cfg.title = "The Title of our game";
	cfg.width = 1000;
	cfg.height = 800;
		
	GameContainer app = new GameContainer(cfg.title, GameInstance, 800, 600, config.useGL20);
	new LwjglApplication(app, cfg);
}
```

As normal you create your configuration and set all the values you need. Note that we don not take the width and height of the configuration. the configuration width/height is used for the size of the window while the width/height given in the C'Tor of the **GameContainer** is used for the size of your game (in pixels). This gives you the advantage of handling the already mentioned debug. 
Now let us get to the actual **Game** interface.

A Simple Game
----------------




[1]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/GameContainer.java
[2]: https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/ApplicationListener.java
[3]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/Game.java
[4]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/graphics/Graphics.java
[5]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/font/Font.java
