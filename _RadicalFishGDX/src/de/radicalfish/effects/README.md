Effects
=================

In this little package you find neat little effects to use in your game.

[Rumble][1]
----------------

The Rumble class can be used to well... add shaking to your game. It supports multiple handles you can add to stack 
rumble effect (add a continues shake effect and add smaller heavier shakes from time to time). The Rumble class should be updated every frame to compute the 
offset you can add to your camera. To add a rumble effect to the Rumble class you can simply do:
```Java
	// create your Rumble object somewhere
	Rumble rumble = new Rumble();
		
	// a handle which can be added to the rumble class
	// the first boolean tells us if the rumble effect should never stop (only handles with a name can be continues!)
	// the second tells us we the effect should fade out (useful for long and strong shakes)
	RumbleHandle handle1 = new RumbleHandle(RUMBLE_AXIS.BOTH, RUMBLE_POWER.STRONG, RUMBLE_SPEED.FAST, 2.0f, false, false);
	handle1.setName("test");
		
	// create another one which will fade and let this one be nameless
	RumbleHandle handle2 = new RumbleHandle(RUMBLE_AXIS.X_ONLY, RUMBLE_POWER.WEAK, RUMBLE_SPEED.SLOW, 5.0f, false, true);
		
	// add the rumble effect
	rumble.addRumble(handle1);
	rumble.addRumble(handle2);
```
Of course now you need to update the rumble effect somewhere in your update code:
```Java
	// delta should be in seconds
	rumble.update(delta);
```
After a rumble effect is done it will be disposed (maybe later there will be a version with a pool to avoid memory allocation).
As you can see you can rumble on both axis or just one axis(**BOTH**, **X_ONLY**, **Y_ONLY**).
The power of the effect is also controlled by an Enum (**WEAKEST**, **WEAKER**, **WEAK**, **MEDIUM**, **STRONG**, **STRONGER**, **STRONGEST**, **MEGA**, **ULTRA**, **EXTREME**). 
The same goes for the speed (**SUPERSLOW**, **SLOWEST**, **SLOWER**, **SLOW**, **NORMAL**, **FAST**, **FASTER**, **FASTEST**).
This is done control the effect. If you want further customization you are free to copy the code of course.

After you update the Rumble object with all it handles, you can get the computed offset with:
```Java
	Vector2 vec = rumble.getOffset();
	
	// or go with
	vec.x = rumble.getOffsetX();
	vec.y = rumble.getOffsetY();
```

[1]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/effects/Rumble.java