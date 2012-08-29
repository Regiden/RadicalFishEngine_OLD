Debugging
=================

This package will mainly be used to link the [RadicalFishDebug][6] project with this project. Of course you can use the 
Interfaces and Classes to fit your use. Let's explain some of the classes in more detail


[Logger][1]
----------------

The Logger is used to... well log ;) The engine uses this, and only this, to log. It has some platform specific code with the ability to add [LogListener][2].
The listener will be informed when a new log entry has been added. You may use this to implement a LogConsole (which is also supported by the debug project).
All methods are static (You don't need an instance of the class)so the general use of this class may look like this:
```Java
// this will log a simple String which look like this: INFO: Just logged and Info!
Logger.info("Just logged and Info!");
// will add an LogListener to the Logger.
Logger.addLogListener(FanzyClass);
// will return an string with the last '20' logs. 
String logText = Logger.getLogAsString(20)
```

[DebugCallback][3]
----------------

This little class is used in the debug project. You can set a DebugCallBack in the [GameContainer][4] class via the method:
```Java
GameContainer app = new GameContainer("Fancy Title", GameClass, 800, 600, yesWeWantGL20);
app.setDebugCallBack(MyDebugClass);
```
The Interface forces the implementation to use the methods provided by the [Game][5] Interface. All methods will be called after the game methods are called by the game container. 
This way you always get a clean state from the game to debug. Note that all methods are only called if the type of the application is Desktop. Otherwise setting a callback will be ignored.

The Implementation of the DebugCallback should run in the Desktop version of the game to access the Debug project. With this you have a powerful debugging tool (especially is you use a [GameContext ][7]).


[1]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/debug/Logger.java
[2]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/debug/LogListener.java
[3]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/debug/DebugCallback.java
[4]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/GameContainer.java
[5]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/Game.java
[6]: https://github.com/Regiden/RadicalFishEngine/tree/master/_RadicalFishDebug
[7]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/context/GameContext.java