package game;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.state.*;

/**
 * This is the About class, which contains information about
 * the game.
 * 
 * @author  Hampus Liljekvist
 * @version 2012-06-25
 */
public class About extends GameState {
	private Image titleScreen;
	
	/**
	 * Constructor.
	 * 
	 * @param state The state
	 */
	public About(int state) {
		super(state);
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
		buttons[3] = new Image("res/backButton.png");
		Image ab = buttons[3];
		buttonAreas[3] = new MouseOverArea(gc, ab, gc.getWidth()/2 -
				ab.getWidth()/2, gc.getHeight()/3 + ab.getHeight()*2 + 40);
		
		titleScreen = new Image("res/titleScreen.png");
	}
	
	/**
	 * Renders.
	 * 
	 * @param  gc			  GameContainer
	 * @param  sbg			  StateBasedGame
	 * @param  g			  Graphics
	 * @throws SlickException
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		// Draw info and back button, hardcoded String coordinates
		g.drawString("This game was created by Hampus Liljekvist using\n" +
		"Java and Slick2D. The goal is to click the\n" +
				"targets before they disappear.\n\n" +
		"Version 0.1\n\n" +
				"FREE AS IN BEER 2012", gc.getWidth()/2 - 230, gc.getHeight()/4 + 50);
		
		buttonAreas[3].render(gc, g);
		
		// Draw title screen
		g.drawImage(titleScreen, gc.getWidth()/2 - titleScreen.getWidth()/2,
				gc.getHeight() - gc.getHeight()/4 - titleScreen.getHeight());
	}
	
	/**
	 * Updates.
	 * 
	 * @param  gc			  GameContainer
	 * @param  sbg			  StateBasedGame
	 * @param  delta
	 * @throws SlickException
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		input = gc.getInput();
		
		if(buttonAreas[3].isMouseOver())
			if(input.isMousePressed(0))
				sbg.enterState(0); // Enter menu state
		if(input.isKeyPressed(Input.KEY_ESCAPE))
			sbg.enterState(0);
	}
	
	/**
	 * Returns the ID.
	 * 
	 * @return The ID
	 */
	@Override
	public int getID() {
		return 2;
	}
}