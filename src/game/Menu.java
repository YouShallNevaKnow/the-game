package game;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.state.*;
import org.lwjgl.input.Mouse;

/**
 * This is the main Menu class.
 * 
 * @author  Hampus Liljekvist
 * @version 2012-07-04
 */
public class Menu extends GameState {
	private TextField text;
	private boolean textHasFocus;
	private String warningType, warningPositive, warningWidth,
	highscoreString;
	private Image titleScreen;
	
	/**
	 * Constructor.
	 * 
	 * @param state The state
	 */
	public Menu(int state) {
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
		// Create the images
		buttons[0] = new Image("res/startButton.png");
		buttons[1] = new Image("res/aboutButton.png");
		buttons[2] = new Image("res/exitButton.png");
		
		titleScreen = new Image("res/titleScreen.png");
		
		// Set the button coordinates
		Image sb = buttons[0];
		buttonAreas[0] = new MouseOverArea(gc, sb, gc.getWidth()/2 -
				sb.getWidth()/2, gc.getHeight()/3);
		Image ab = buttons[1];
		buttonAreas[1] = new MouseOverArea(gc, ab, gc.getWidth()/2 -
				ab.getWidth()/2, gc.getHeight()/3 + ab.getHeight() + 20);
		Image eb = buttons[2];
		buttonAreas[2] = new MouseOverArea(gc, eb, gc.getWidth()/2 -
				eb.getWidth()/2, gc.getHeight()/3 + eb.getHeight()*2 + 40);
		
		// Container, font, x, y, width, height
		text = new TextField(gc, gc.getDefaultFont(),
				gc.getWidth()/2 - 50, 50, 100, 50);
		textFieldValue = 75;
		text.setText("75");
		
		// These are not visible initially
		warningType = "";
		warningPositive = "";
		warningWidth = "";
		highscoreString = "";
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
		// Scream at people with ridiculous display resolutions
		// Hardcoded required resolution
		if(gc.getScreenWidth() < 1024 || gc.getScreenHeight() < 768)
			g.drawString("YOUR DISPLAY RESOLUTION IS TOO LOW FOR THIS GAME",
					gc.getWidth()/2 - 220, 190);
		
		buttonAreas[0].render(gc, g);
		buttonAreas[1].render(gc, g);
		buttonAreas[2].render(gc, g);
		
		g.drawString("Enter target width (positive integer <= screen height): ",
				gc.getWidth()/2 - 230, 15);
		g.drawString(warningType, gc.getWidth()/2 - 190, 115);
		g.drawString(warningPositive, gc.getWidth()/2 - 153, 115);
		g.drawString(warningWidth, gc.getWidth()/2 + 7, 115);
		
		if(!firstRound)
			g.drawString(highscoreString, gc.getWidth()/2 - 90, 150);
		
		text.render(gc, g); // Render text field
		
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
		// Origin at bottom left, not as in Graphics, etc.
		int xPos = Mouse.getX() + 1; // Compensate for index 0
		int yPos = gc.getHeight() - Mouse.getY(); // Compensate for coordinate system
		
		highscoreString = "Highscore: " + highscore + " (" + highscoreHit + ")";
		
		// To check if the 'button' or corresponding key is pressed
		if(buttonAreas[0].isMouseOver())
			if(input.isMousePressed(0)) { // 0 => left click
				textHasFocus = false; // Remove text field focus
				sbg.enterState(1); // Enter state 1
				}
		if(input.isKeyPressed(Input.KEY_S) && !textHasFocus) {
			textHasFocus = false;
			sbg.enterState(1);
			}
		
		if(buttonAreas[1].isMouseOver())
			if(input.isMousePressed(0)) {
				textHasFocus = false;				
				sbg.enterState(2);
				}
		if(input.isKeyPressed(Input.KEY_A) && !textHasFocus &&
				(sbg.getCurrentStateID()==0)) {
			textHasFocus = false;
			sbg.enterState(2);
			}
		
		if(buttonAreas[2].isMouseOver())
			if(input.isMousePressed(0)) {
				textHasFocus = false;
				gc.exit(); // Exit game
				}
		
		// Esc will remove focus from text field if focused
		if(input.isKeyPressed(Input.KEY_ESCAPE)) {
			if(!textHasFocus) {
				textHasFocus = false;
				gc.exit();
				} else {
					text.setFocus(false);
					textHasFocus = false;
					}
			}
		
		// Clicking the text field will focus it
		if((xPos >= text.getX() && xPos <= text.getX()+text.getWidth()) &&
				(yPos >= text.getY() && yPos <= text.getY()+text.getHeight()))
			if(input.isMousePressed(0))
				textHasFocus = true;
		
		// Remove focus if the black area is clicked
		if(input.isMousePressed(0))
			textHasFocus = false;
		
		// To focus the text field
		if(textHasFocus)
			text.setFocus(true);
		else
			text.setFocus(false);
		
		/*
		 * Note that the first spawned target after the game has started will
		 * be at the default width value, since init() is called before the
		 * update method. If you edit the value in the text field, you need to
		 * lose a round in order for init() to be called again, so that the new
		 * width value will be assigned to the first target. Thus the value of
		 * the text field will always take a full round to be properly set.
		 */
		try {
			int newWidth = Integer.parseInt(text.getText());
			if(newWidth > gc.getScreenHeight())
				warningWidth = "<= SCREEN HEIGHT!";
			else
				if(newWidth > 0)
					textFieldValue = newWidth;
				else
					warningPositive = "POSITIVE";
		} catch(Exception e) {
			System.out.println("Only integers allowed");
			warningType = "USE          INTEGERS";
		}
	}
	
	/**
	 * Returns the ID.
	 * 
	 * @return The ID
	 */
	@Override
	public int getID() {
		return 0;
	}
}