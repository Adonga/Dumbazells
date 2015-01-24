import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.renderer.*;


public class Game extends BasicGame
{
	public static final Vector2f GAME_COORD_SIZE = new Vector2f(16.0f, 9.0f);
	public static final int NUM_FLAGS = 3;
	public static final int MAX_NUM_PLAYERS = 4;
	private static ScalableGame scalableGame;
	
	private GameStates gameState = new GameStates();
	
	private CommandMap commandMap;
	private MapRenderer mapRenderer;

	private int playerRegisterd =0;
	
	private Player[] players;
	private Basis[] basen;
	private Flag[] flags;
	int i=0;
	int j = 0;
	public Game() {
		super("Dumbazells");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {

		commandMap = new CommandMap();

		mapRenderer = new MapRenderer();
	}

	@Override
	public void update(GameContainer gc, int passedTimeMS) throws SlickException {
	
		mapRenderer.updateLogic(commandMap);
		
		if(gameState.registering){

			commandMap.clear();
			if(j %10 ==0)
				playerRegisterd = gameState.init(gc.getInput());
			j++;
			
			if (playerRegisterd > 4)
				playerRegisterd = MAX_NUM_PLAYERS;

			players = new Player[playerRegisterd];
			for (int i = 0; i < playerRegisterd; i++) {
				players[i] = new Player(i);
			}	
			basen = new Basis[playerRegisterd];
			BaseTypes[] baseTypes = {BaseTypes.Circle,BaseTypes.Raute,BaseTypes.Square,BaseTypes.Triangle};
			for (int i = 0; i < playerRegisterd; i++) {
				basen[i] = new Basis( players[i], new Vector2f(
								Basis.BASE_SIZE
										+ Basis.BASE_SIDE_DEADZONE
										+ (float) Math.random()
										* (GAME_COORD_SIZE.getX() - 2 * (Basis.BASE_SIZE + Basis.BASE_SIDE_DEADZONE)),
								Basis.BASE_SIZE + Basis.BASE_SIDE_DEADZONE
										+ (float) Math.random()
										* (GAME_COORD_SIZE.getY() - 2 * (Basis.BASE_SIZE + Basis.BASE_SIDE_DEADZONE))),baseTypes[i]);
			}

			flags = new Flag[] { new Flag(Flag.randFlagPosition(basen), basen),
					new Flag(Flag.randFlagPosition(basen), basen),
					new Flag(Flag.randFlagPosition(basen), basen)};
		}else if(!gameState.gameend){
		
			for (Player player : players) {
				player.update(gc.getInput());
			}

			for (Basis base : basen) {
				base.update(gc, commandMap, flags,basen);
			}

			for (Flag flag : flags) {
				flag.update(gc, passedTimeMS);
			}
			gameState.restart(gc.getInput());
			
		}
		else {
			gameState.restart(gc.getInput());
		}
	}

	@Override

	public void render(GameContainer gc, Graphics g) throws SlickException 	{
		
		commandMap.draw(g);

		mapRenderer.drawOverlays(g);

		gameState.render(g);
		if(!gameState.registering)
		{
			for (Basis base : basen) {
				base.render(g);
			}


			for (Flag flag : flags) {
				flag.render(g);
			}

			for (Player player : players) {
				player.render(commandMap, g);
			}

		}

	}

	public static void main(String[] args)
	{
		try
		{
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int height = gd.getDisplayMode().getHeight() - 100;

			// Maybe this switch has no effect.
			//Renderer.setRenderer(new VAOGLRenderer());
			
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
