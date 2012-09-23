Radical Fish - Main Package
=================

This package contains all sub-packages and the most basic classes used by the engine including the `Game` Interface and the `GameContainer` class. The later is the starting point of all games written with the **RadicalFishEngine**.

In the following you will learn how to build a simple game using the `Game` Interface and the `GameContainer` class.

[The GameContainer][1]
-----------------

The heart and soul of the engine is this little class. It extends the [ApplicationListener][2] from libgdx. The main use of the class is to handle the calls for the [Game][3] class. It provides you with a single [Graphics][4] object which gives you a Slick2D/Java2D like way of handling the matrix context of the game. You can scale and offset the matrix for all upcoming drawings and also push and pop these changes. 
The `GameContainer` has some other neat features too:

* y-down viewport be default (or y-up if you prefer it)
* clipping the container. 
* (currently) 2 view types: streched and fixed
	* fixed is useful for debugging where the container sticks to the top left of the screen ( or bottem left if the context is y-up) no matter the size of the window. The debug panels can move freely through the window tho! 
* handling debug mechanism for desktop projects
* setting a default [Font][5] when not setting one in the init method of your game
* providing an input handle via [GameInput][9]
* automatically adding the game instance to the input handler if it implements the [InputProcessor][7] Interface


The `GameContainer` is the class you add as ApplicationListener to the platform specific project starter classes. Let's take a look at a desktop project (examples on how to start on other platforms will follow):

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

As normal you create your configuration and set all the values you need. Note that we do not take the width and height of the configuration. The configuration width/height is used for the size of the window while the width/height given in the C'Tor of the `GameContainer` is used for the size of your game (in pixels). This gives you the advantage of handling the already mentioned debug. 

Now let's get to the actual `Game` Interface.

[The Game][3]
----------------

As mentioned before a game with this engine runs always with and implementation of the `Game` Interface. The Interface discribes all the methods we need.

* **init**: gets called only once at game start. This is the point where you normally load all resources and init your game objects. The `GameContainer` will be passed in as parameter here
* **update**: gets called once per frame and should be used to update the logic of your game. Next to the `GameContainer` the **delta** time will be passed in (in seconds)
* **render**: gets called once per frame and should be used to render all your objects, background, etc. A shared instance of the `Graphics` class will be passed in. Note that you can get the `Graphic` instance by using the getter `container.getGraphics()`
* **pause**: gets called if you press the **HOME**-Button on Android and can be used to free memory as long as the player is not in the game
* **resume**: gets called if you enter the game again. Use it the reload data that you set free in the pause method
* **dispose**: gets called on shutdown of the game. In this method you should unload all content of your game to guaranty a clean shutdown

As you can see these are a lot of methods and you mostly only want to use `init`, `update` and `render`. Also the Interface does not include an [InputProcessor][7] which you might want to use to proceed input events. 

To avoid implementing the `InputProcessor` every time your create a game and to give you the possibility to only use the input callbacks you need the **RadicaFisheEngine** offers you [BasicGame][6]. The abstract class implements all the methods from the `InputProcessor` and the `Game` Interface and only forces the `init`, `update` and `render` to be abstract. Every other method can be overriden on demand! What a nice feature to have!

Here is a bit code which shows how this could look like:

```Java
public class SimpleGame extends BasicGame {
	
	private Texture texture;
	private Sprite sprite;
	
	private Vector2 position;
	
	public void init(GameContainer container) {
		position = new Vector2(100, 100);
		
		texture = new Texture("data/block.png");
		sprite = new Sprite(texture);
		sprite.setPosition(position.x, position.y);
	}
	public void update(GameContainer container, float delta) {
		
	}
	public void render(GameContainer container, Graphics g) {
		SpriteBatch batch = g.getSpriteBatch();
		
		batch.begin();
		{
			sprite.draw(batch);
		}
		batch.end();
	}
	
	// lets override some of the methods BasicGame helpfully hides for us
	// override the dispose method and dispose our stuff
	public void dispose() {
		texture.dispose();
	}
	// maybe the keyDown from the InputProcessor?
	public boolean keyDown(int keycode) {
		System.out.println("Pressed: " + GameInput.getKeyName(keycode) + " | code: " + keycode);
		return false; 
	}
	
}
```

Which looks like this:<p>
![10][]

In this code snippet we create a simple game which renders the test block used by the engine at the postion `100, 100`. We also override 2 methods. The `dispose` method were we gently dispose the texture we used and the `keyDown` method which displays the name of the key and the key code (I don't use the `@Override` annotation here). 

As you might already saw the `Graphics` object has a default [SpriteBatch][8] instance you can use to render your graphics. You can set your own batch to or modify the existing one (like setting a custom shader). 

[Input][9]
----------

Of course you don't simply always want to implement the `InputProcessor` Interface. Sometimes you might want to add your own input handler to receive input events. For this the engine gives you the `GameInput` class. It wraps up the Input class from **libgdx** and offers methods to add new `InputProcessors`, get the name of a key (as seen in the example above) or map a string to a key. `GameInput` also has a feature to check if a key was pressed. This way you don't need to use the callback methods from the `InputProcessor` class. 

Lets extend the above example with some input handling to move the sprite around:

```Java
public void update(GameContainer container, float delta) {
		GameInput input = container.getInput();
		
		// we check if a key is down and move our sprite by a friction of 100 pixels per frame
		// thus we multiply with delta to get constant movement no matter the frame rate
		if(input.isKeyDown(Keys.LEFT)) {
			position.x -= 100f * delta;
		} else if(input.isKeyDown(Keys.RIGHT)) {
			position.x += 100f * delta;
		}
		if(input.isKeyDown(Keys.UP)) {
			position.y -= 100f * delta;
		} else if(input.isKeyDown(Keys.DOWN)) {
			position.y += 100f * delta;
		}
		
		sprite.setPosition(position.x, position.y);
	}
```

We now have conditions to check if a specific key was pressed (in this case `left`, `right`, `up` and `down`). If any of the key is down we move the sprite by a given factor in the appropriate direction. 

To check if a key was just pressed you can use the method `input.isKeyPressed(int keycode)`. After one call to this method the pressed flag will be set to false! For any other input commands please read the guide from libgdx here: [Input Handling][11]


# Reduce the Stutter! #
To reduce the stutter you might see when testing the code we can use a feature called **smooth deltas**. With this the delta time will be calculated based on the average framerate and gives are more stable delta time. To enable this just call the setter on the `GameContainer` instance:

```Java
// we can set it in the main body of your application
public static void main(String[] args) {
	LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
	cfg.title = "The Title of our game";
	cfg.width = 1000;
	cfg.height = 800;
		
	GameContainer app = new GameContainer(cfg.title, GameInstance, 800, 600, config.useGL20);
	app.setSmoothDelta(true);
	new LwjglApplication(app, cfg);
}

// or we set it in the init method of our game
public void init(GameContainer container) {
	position = new Vector2(100, 100);
		
	texture = new Texture("data/block.png");
	sprite = new Sprite(texture);
	sprite.setPosition(position.x, position.y);

	// you can enable/disable the feature at any time!
	container.setSmoothDelta(true);
}

```


This conludes the introduction to the engine. Let's rehash the things we learned:

* the `GameContainer` does all the hard work
* we pass our game instance and start coding
* single input instance we use to check input events
* single graphics instance we use 
* potatoes

Yepp that sums it up very well :) Enjoy your stay and have fun with the engine. For any ideas and features you might want to see here I'm will listen to them, consider them and might add them! :D


[1]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/GameContainer.java
[2]: https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/ApplicationListener.java
[3]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/Game.java
[4]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/graphics/Graphics.java
[5]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/font/Font.java
[6]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/BasicGame.java
[7]: https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/InputProcessor.java
[8]: https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/graphics/g2d/SpriteBatch.java
[9]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/GameInput.java
[10]: http://i.imgur.com/4JLjh.png
[11]: http://code.google.com/p/libgdx/wiki/InputHandling
