package game;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 * This is the main Game class, which manages all the game states
 * and initialises them.
 * 
 * @author  Hampus Liljekvist
 * @version 2012-06-26
 */
public class Game extends StateBasedGame {
	public static final String gameName = "The Game";
	public static final int menu = 0;
	public static final int play = 1;
	public static final int about = 2;
	
	/**
	 * Main method.
	 * 
	 * @param  args			  Arguments
	 * @throws SlickException
	 */
	public static void main(String[] args) throws SlickException {
		AppGameContainer appGC;
		try {
			// Create the window and put it in the container
			appGC = new AppGameContainer(new Game(gameName));
			// Fullscreen
			appGC.setDisplayMode(appGC.getScreenWidth(),
					appGC.getScreenHeight(), true);
			appGC.setVSync(true); // Enable vsync
			appGC.setSmoothDeltas(true); // Good with vsync
			/*
			 * Limit the logic update interval to make sure that
			 * players with faster computers have the same amount
			 * of time to react before the target is decreased in
			 * size. It's not certain how this would affect
			 * extremely slow computers, though.
			 */
//			appGC.setMinimumLogicUpdateInterval(16);
//			appGC.setMaximumLogicUpdateInterval(16);
			appGC.start(); // Start the game
		} catch(SlickException se) {
			se.printStackTrace();
		}
	}
	
	/**
	 * Constructor for Game.
	 * 
	 * @param gameName The game name
	 */
	public Game(String gameName) {
		super(gameName); // Window title
		this.addState(new Menu(menu));
		this.addState(new Play(play));
		this.addState(new About(about));
	}
	
	/**
	 * Initialises the game.
	 * 
	 * @param  gc			  The GameContainer
	 * @throws SlickException
	 */
	@Override
	public void initStatesList(GameContainer gc)
			throws SlickException {
		// Build the screens
		this.getState(menu).init(gc, this);
		this.getState(play).init(gc, this);
		this.getState(about).init(gc, this);
		this.enterState(menu); // The start screen
	}
}