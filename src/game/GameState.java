package game;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.gui.*;

/**
 * This is the game state superclass, which all game states
 * inherits from. This class does in turn extend
 * BasicGameState.
 * 
 * @author  Hampus Liljekvist
 * @version 2012-06-25
 */
public class GameState extends BasicGameState {
	protected static int textFieldValue, highscore, highscoreHit;
	protected static Image[] buttons; // The button images
	// The areas the buttons cover
	protected static MouseOverArea[] buttonAreas;
	protected static Input input;
	// To keep track of the first round when applying score
	protected static boolean firstRound;
	
	/**
	 * Constructor.
	 * 
	 * @param state The state
	 */
	public GameState(int state) {
		buttons = new Image[4];
		buttonAreas = new MouseOverArea[4];
		firstRound = true;
	}
	
	/**
	 * Initialises.
	 * 
	 * @param  gc			  GameContainer
	 * @param  sbg			  StateBasedGame
	 * @throws SlickException
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
	}
	
	/**
	 * Renders the game on the screen, this method is initially
	 * called AFTER update() is called.
	 * 
	 * @param  gc			  GameContainer
	 * @param  sbg			  StateBasedGame
	 * @param  g			  Graphics
	 * @throws SlickException
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
	}
	
	/**
	 * Updates the game logic, this method is initially called
	 * BEFORE render() is called.
	 * 
	 * @param  gc			  GameContainer
	 * @param  sbg			  StateBasedGame
	 * @param  delta
	 * @throws SlickException
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
	}
		
	/**
	 * Returns the ID (default 0).
	 * 
	 * @return The ID
	 */
	@Override
	public int getID() {
		return 0;
	}
}