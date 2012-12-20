package game;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.state.*;
import org.lwjgl.input.*;
import java.util.Random;

/**
 * The main Play class for the game, this is where all the
 * functionality of the gameplay is implemented.
 * 
 * @author  Hampus Liljekvist
 * @version 2012-07-04
 */
public class Play extends GameState {
	private int x, y, w , h; // x, y, width, height
	private int score, quantum, hit, missed, allowedMissed;
	private int minDelta;
	private String mouse;
	private boolean doDraw, resetGame;
	private Rectangle rect;
	private Random rnd;
	
	/**
	 * Constructor.
	 * 
	 * @param state The state
	 */
	public Play(int state) {
		super(state);
		rnd = new Random();
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
		w = textFieldValue;
		h = w; // Only allow symmetrical targets
		x = rnd.nextInt(gc.getWidth() - w + 1); // Random x coordinate
		y = rnd.nextInt(gc.getHeight() - h + 1); // Random y coordinate
		
		rect = new Rectangle(x, y, w, h); // The rectangle, initially invisible
		
		doDraw = true; // True if the target should be drawn (rendered)
		
		hit = 0; // The number of hit targets
		missed = 0; // The number of missed targets
		// The number of misses allowed before the game is over
		allowedMissed = 3;
		
		score = 0; // The current score
		/*
		 * The amount to gain from a clicked target, and the
		 * (amount to lose)*5 if you miss one
		 */
		quantum = 100;
		
		/*
		 * Default mouse String, never visible since init() is called when
		 * the menu screen is focused.
		 */
		mouse = "";
		/*
		 * The smallest allowed delta value before the render method sleeps to
		 * compensate for fast computers' rendering time.
		 */
		minDelta = 16;
		
		System.out.println("INIT"); // DEBUG
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
		g.drawString(mouse, 10, 35);
		g.drawString("Press ESC to Pause", 10, 60);
		g.drawString("Missed Targets: " + missed + "/" + allowedMissed,
				gc.getWidth()/2 - 100, 10);
		g.drawString("Hit Targets: " + hit,
				gc.getWidth() - 150, 10);
		g.drawString("Score: " + score, gc.getWidth() - 150, 35);
		
		/*
		 * A rectangle with width/height 0 will be one pixel wide, a
		 * rectangle with width/height 1 will be 2 pixels wide, etc.
		 */
		rect = new Rectangle(x, y, w, h); // x, y width, height
		
		 /*
		  * Won't draw the target at full width initially, rather, width - 1,
		  * due to the draw function 'lagging' behind the size change function
		  * in update. It will draw the target at width 0, though, and every
		  * other new target that spawns at full width.
		  * It seems as if the target is drawn with pixels missing in the
		  * upper left corner, and when the width reaches 0 the target will be
		  * drawn as invisible, followed by approx one pixel. This seems to
		  * have to do with the implementation of the Slick2D/LWJGL functions.
		  */
		if(doDraw) {
			g.draw(rect);
			g.fill(rect);
			System.out.println("DRAW"); // DEBUG
			} else {
				w = textFieldValue;
				h = w;
				x = rnd.nextInt(gc.getWidth() - w + 1);
				y = rnd.nextInt(gc.getHeight() - h + 1);
				/*
				 * This will make sure that the target is drawn at full width when
				 * a new one spawns, this will not however help the fact that the
				 * init target will never be drawn at full width (due to update()
				 * being called before render()).
				 */
				g.draw(rect);
				g.fill(rect);
				doDraw = true; // We want the new target to be drawn
				}
		
		if(resetGame) {
			g.clear();
			resetGame = false;
			}
		System.out.println("RENDER"); // DEBUG
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
		// IT DOESN'T WORK THIS WAY
		if(delta < minDelta) {
			System.out.println("SLEEP: " + (minDelta - delta) + " ms"); // DEBUG
			gc.sleep(minDelta - delta);
		}
		
		// Game over
		if(missed > allowedMissed) {
			if(firstRound) {
				highscore = score;
				highscoreHit = hit;
				firstRound = false;
				} else
					if(score > highscore) {
						highscore = score;
						highscoreHit = hit;
						}
			sbg.enterState(0);
			init(gc, sbg); // Horrible hack to reset the game when losing
			// To avoid cheating by seeing the init spawn position of target #1
			resetGame = true;
			}
		
		input = gc.getInput();
		int xPos = Mouse.getX() + 1; // Compensate for index 0
		int yPos = gc.getHeight() - Mouse.getY(); // Compensate for coordinate system
		mouse = "Mouse Position: " + xPos + ", " + yPos;
		
		// Exit game by pressing ESC
		if(input.isKeyPressed(Input.KEY_ESCAPE)) {
			sbg.enterState(0);
			}
		
		/*
		 * Check if the target is clicked. You will get score if you manage to
		 * click the first spawned target, which will be invisible, due to
		 * update being called first. Since (0,0) doesn't exist, it's not
		 * possible to get score by clicking at that position when the target
		 * is drawn as 'invisible'.
		 */
		if(input.isMousePressed(0))
			if(((xPos >= x) && (xPos <= (x+w))) &&
					((yPos >= y) && (yPos <= (y+h)))) {
				score += quantum;
				hit++;
				// Don't bother checking for width, create a new target
				doDraw = false;
				} else {
					score -= quantum;
					}
		
		// Decrease the size of the target if it hasn't been clicked
		if(doDraw) {
			if(w != 0) {
				w--;
				h--;
			} else { // If width = 0 you missed it
				missed++;
				score -= quantum*5;
				doDraw = false; // Create new target
			}
		}
		
		System.out.println("UPDATE: " + delta + " ms"); // DEBUG
	} 	
	
	/**
	 * Returns the ID.
	 * 
	 * @return The ID
	 */
	@Override
	public int getID() {
		return 1;
	}
}