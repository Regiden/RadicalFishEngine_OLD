package de.radicalfish.debug;

import de.radicalfish.context.GameContext;
import de.radicalfish.state.GameState;
import de.radicalfish.world.World;

public abstract class TWLGameState extends GameState {
	
	public TWLGameState(GameContext context, World world, int id) {
		super(context, world, id);
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * This method is called when keyboard focus is transfered to a UI widget or to another application.
	 */
	public void keyboardFocusLost() {}
	/**
	 * This method is called when the layout of the root pane needs to be updated.
	 */
	public void layoutRootPane() {}
	
}
