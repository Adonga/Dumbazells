import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public class Game extends BasicGame
{
	private Player[] players;
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
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Game());
			appgc.setDisplayMode(1024, 768, false);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
