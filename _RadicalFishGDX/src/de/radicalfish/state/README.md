[StateBasedGames][3]
=================

`StateBasedGame` allows you too split the logic of your game into different [GameState][1]s (like main menu, gameplay and so on).
The package uses the [GameContext][2] class to give you more control over your game but you don't need to implement a GameContext if you don't want.
StateBasedGame will create one for you, which can be used as default context.

StateBasedGame is basically a [Game][4] but expects you to do some work before it can run. The following code is a simple StateBasedGame with some doc to show what you need be aware of.
```Java

// extends our class from StateBasedGame
public class StatesTest extends StateBasedGame {
	
	// this method will be called first. It should return an instance of a GameContext.
	// you can return null in case you want to use the default context.
	protected GameContext initGameContext(GameContainer container) {
		return null;
	}
	
	// this gets called after the previous method. it should return a [GameWorld][5] object. 
	// You can return null if you don't need a world for your game.
	// This method is not abstract. You can override if you need.
	protected GameWorld initWorld(GameContainer container) {
		return null;
	}
	
	// this is the last init call. Here you should add all states
	protected void initStates(GameContext context) {
		// fancy init code
	}
	
}
```
Of course you can initiate other resources in one of the init methods. Much like the [BasicGame][6] the StateBasedGame also implements the [InputProcessor][7].
So you can override the methods if you need. But note that you must call the super implementation of the method. Otherwise the input methods on the GameState class
will not be called (They get only called if the StateBasedGame allows it, eg. if no transitions are active)!

You also have the possibility to inject update and render code before/after the game update/render methods. Just override one of these methods:
```Java
	// called before the update call to the current state
	protected void preUpdate(GameContext context, GameWorld world, GameDelta delta) {

	}

	// called after the update call to the current state
	protected void postUpdate(GameContext context, GameWorld world, GameDelta delta) {

	}

	// called before the render call to the current state
	protected void preRender(GameContext context, GameWorld world, Graphics g) {
	
	}
	
	// called after the render call to the current state
	protected void postRender(GameContext context, GameWorld world, Graphics g) {
	
	}
```
Of course the StateBasedGame also has the methods pause/resume and dispose. Much like the pre/post methods you can override them if you need them.

A GameState is basically a Game too. You have the normal init/update/render/pause/resume and dispose functions. All of them get called from the StateBasedGame.
Additionally you have access to methods which control what to do when you switch from one state to another.
```Java
	// gets called before the enter transition starts
	public void entering(GameContext context, GameWorld world, GameState form) {
	
	}
	// gets called after the enter transition is finished
	public void entered(GameContext context, GameWorld world, GameState form) {
	
	}
	
	// gets called before the leave transition starts
	public void leaving(GameContext context, GameWorld world, GameState to)  {
	
	}
	
	// gets called after the leave transition is finished
	public void left(GameContext context, GameWorld world, GameState to) {
	
	}
```
With this you can unload content in 'left' while the the screen is black (thanks to the transition). 
As you noticed there are [Transition][9]s you can use to switch from one state to another. Transitions will be simply updated until there 'isFinished()' method returns true.
A simple example can be seen in the already implemented [FadeTransition][10]. The Transition will fade until the screen is fully occupied or fade until the 
new state is visible. To switch from one state to another you can simply use the following methods:
```Java
	// the update method of a game state
	public void update(GameContext context, GameWorld world, GameDelta delta) {
		// lets move to another state if we press a state.
		if (input.isKeyPressed(Keys.ENTER)) {
			StateBasedGame game = context.getGame();
			
			// leave this state with a fade out transition an enter the new state with an fade in transition
			// we fade to black in 2 seconds :D
			Transition out = new FadeTransition(Color.BLACK, FADETYPE.FADE_OUT, 2.0f);
			// we fade in from black in 2 seconds D:
			Transition in = new FadeTransition(Color.BLACK, FADETYPE.FADE_IN, 2.0f);
			game.enterState(newID, out, in);
		}
	}
```
As you can see we enter a state by 'ID'. Every state should have unique ID which identifies the state. You can use the [BasicGameState][8] to simplify the process
of creating a state. The class gets initiated with an ID and makes only the most necessary methods (init, update and render) abstract and the other can be overridden if needed.


[1]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/state/GameState.java
[2]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/context/GameContext.java
[3]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/state/StateBasedGame.java
[4]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/Game.java
[5]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/world/World.java
[6]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/BasicGame.java
[7]: https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/InputProcessor.java
[8]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/state/BasicGameState.java
[9]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/state/transitions/Transition.java
[10]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/state/transitions/FadeTransition.java

