import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;


public class Game extends BasicGame
{
	public static final Vector2f GAME_COORD_SIZE = new Vector2f(16.0f, 9.0f);
	public static final int NUM_FLAGS = 3;

	private static ScalableGame scalableGame;

	private CommandMap commandMap;

	private Player[] players;
	private Basis[] basen;
	private Flag[] flags;

	public Game() {
		super("Dumbazells");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {

		players = new Player[] { new Player(0), new Player(1) };

		basen = new Basis[] {
				new Basis(players[0], new Vector2f(Basis.BASE_SIZE + Basis.BASE_SIDE_DEADZONE + (float)Math.random() * (GAME_COORD_SIZE.getX() - 2*(Basis.BASE_SIZE + Basis.BASE_SIDE_DEADZONE)),
						Basis.BASE_SIZE + Basis.BASE_SIDE_DEADZONE + (float)Math.random() * (GAME_COORD_SIZE.getY() - 2*(Basis.BASE_SIZE + Basis.BASE_SIDE_DEADZONE)))),
				new Basis(players[1], new Vector2f(Basis.BASE_SIZE + Basis.BASE_SIDE_DEADZONE + (float)Math.random() * (GAME_COORD_SIZE.getX() - 2*(Basis.BASE_SIZE + Basis.BASE_SIDE_DEADZONE)),
						Basis.BASE_SIZE + Basis.BASE_SIDE_DEADZONE + (float)Math.random() * (GAME_COORD_SIZE.getY() - 2*(Basis.BASE_SIZE + Basis.BASE_SIDE_DEADZONE))))
		};

		flags = new Flag[] {
				new Flag(new Vector2f(Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE + (float)Math.random() * (GAME_COORD_SIZE.getX() - 2*(Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE)),
						Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE + (float)Math.random() * (GAME_COORD_SIZE.getY() - 2*(Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE))), basen),
				new Flag(new Vector2f(Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE + (float)Math.random() * (GAME_COORD_SIZE.getX() - 2*(Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE)),
						Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE + (float)Math.random() * (GAME_COORD_SIZE.getY() - 2*(Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE))), basen),
				new Flag(new Vector2f(Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE + (float)Math.random() * (GAME_COORD_SIZE.getX() - 2*(Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE)),
						Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE + (float)Math.random() * (GAME_COORD_SIZE.getY() - 2*(Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE))), basen)
		};

		commandMap = new CommandMap();
	}

	@Override
	public void update(GameContainer gc, int passedTimeMS) throws SlickException {
		for(Player player : players) {
			player.update(gc.getInput());
		}

		for (Basis base : basen) {
			base.update(gc, passedTimeMS,commandMap);
		}

		for (Flag flag : flags) {
			flag.update(gc, passedTimeMS);
		}
	}

	@Override

	public void render(GameContainer gc, Graphics g) throws SlickException 	{
		commandMap.draw(g);
		
		for(Player player : players) {
			player.render(commandMap, g);
		}

		for (Basis base : basen) {
			base.render(g);
		}

		for (Flag flag : flags) {
			flag.render(g);
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
