import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	private Basis[] basen;
	
	private Bazell bazelle;
	public Game() {
		super("Dumbazells");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		players = new Player[] { new Player(0), new Player(1) };
		basen = new Basis[] { new Basis(players[0], new Vector2f(8.0f, 5.0f)) };
		bazelle = new Bazell(0, new Vector2f(1,1));
	}

	@Override
	public void update(GameContainer gc, int passedTimeMS) throws SlickException {
		for(Player player : players) {
			player.update(gc.getInput(), passedTimeMS);
		}

		for (Basis base : basen) {
			base.update(passedTimeMS);
		}

		bazelle.update(passedTimeMS);
	}

	@Override

	public void render(GameContainer gc, Graphics g) throws SlickException 	{
		for(Player player : players) {
			player.render(g);
		}

		bazelle.render(g);

		for (Basis base : basen) {
			base.render(g);
		}
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
			
			// Fixed timestep, 60 fps.
			appgc.setTargetFrameRate(60);
			appgc.setMinimumLogicUpdateInterval(16);
			appgc.setMaximumLogicUpdateInterval(16);
			appgc.setVSync(true);
			
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
