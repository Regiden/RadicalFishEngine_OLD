![1][]

=================

An 2D engine build on top of [libgdx][2]. The goal of this engine is to provide and easy to use framework which abstracts
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

[1]: https://raw.github.com/Regiden/RadicalFishEngine/master/TestProjects/_RadicalFishTests-android/assets/data/logo.png
[2]: https://github.com/libgdx/libgdx

Boring Legal Stuff
-------------
Not much to tell you here. Every class has a short legal text. It's a BSD license so go and use or modify the engine. I even want you to contribute :) 

Here is a copy of the license:
```
Copyright (c) 2012, Stefan Lange

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

     * Redistributions of source code must retain the above copyright notice,
       this list of conditions and the following disclaimer.
     * Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.
     * Neither the name of Stefan Lange nor the names of its contributors may
       be used to endorse or promote products derived from this software
       without specific prior written permission.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
```