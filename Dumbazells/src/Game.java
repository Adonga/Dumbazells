import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;


public class Game extends BasicGame
{
	private Player[] players;
	private static ScalableGame scalableGame;
	
	private Bazell bazelle;
	public Game() {
		super("Dumbazells");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		players = new Player[] { new Player(0), new Player(1) };
		bazelle = new Bazell(10, 0, new Vector2f(100,100));
	}

	@Override
	public void update(GameContainer gc, int passedTimeMS) throws SlickException {
		for(Player player : players) {
			player.update(gc.getInput(), passedTimeMS);
		}
		bazelle.update(gc.getInput(), passedTimeMS);
	}

	@Override

	public void render(GameContainer gc, Graphics g) throws SlickException 	{
		for(Player player : players) {
			player.render(g);
		}
		bazelle.render(g);
	}

	public static void main(String[] args)
	{
		try
		{
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int height = gd.getDisplayMode().getHeight() - 100;
			
			// Enforce a width that is compatible to our fixed ratio
			int width = (int)(height * (16.0f / 9.0f));
			
			
			AppGameContainer appgc;
			Game.scalableGame = new ScalableGame(new Game(), 16, 9, true);
			
			appgc = new AppGameContainer(Game.scalableGame);
			appgc.setDisplayMode(width, height, false);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
