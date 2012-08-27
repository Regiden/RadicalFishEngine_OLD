Debugging
=================

This package will mainly be used to link the RadicalFishDebug project with this project. Of course you can use the Interfaces and Classes to fit your use.


Logger
----------------

The Logger is used to... well log ;) The engine uses this, and only this, to log. It has some platform specific code with the ability to add [LogListener][1].
The listener will be informed when a new log entry has been added. You may use this to implement a LogConsole (which is also supported by the debug project)


[1]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/debug/LogListener.java