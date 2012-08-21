RadicalFishEngine
=================

An 2D engine build on top of libgdx (see https://github.com/libgdx/libgdx). The goal of this engine is to provide and easy to use framework which abstracts
handling the low-level functions (Already mostly done by libgdx). 

The engine is currently under heavy development, so there will be bugs :) The following features are not compelety implemented. We also plan to implement more features as we go.


Current Features
----------------
* Slick2D like GameContainer system
    * backed by the Game Interface 
    * easy to extend, all callback methods can be overriden and changed 
    * provides a Graphics class which can be used in a Java2D/Slick2D like way
* World System
    * with a base Entity class
    * simple collision system (not done yet)
* Map System
    * with writing/reading maps via our own MapIO class
    * all based on Interfaces to give you to maximum flexibilty
    * (a maker is currently under development)
* Font Features
    * featrues manipulating letters for animations (jumping letters etc.)
    * (currently under changes to fit the system into libgdx)
* State Based Gaming
    * with extended handling states via GameContext
    * implements transitions to move from one state to another.
    * (currently under development)
* Game Context 
    * gives you acces to Interfaces to handle the context in which a game runs
    * there are a lot of default implementations (With some missing... :/)
* Awesome Debugging
    * for Desktop the engine has a debug mechanism
    * backed by TWL http://twl.l33tlabs.org/
    * a lot of Widgets already done