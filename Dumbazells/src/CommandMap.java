import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;


public class CommandMap {
	static final int RESOLUTION_X = (int)(Game.GAME_COORD_SIZE.x * 50);
	static final int RESOLUTION_Y = (int)(Game.GAME_COORD_SIZE.y * 50);
	static final int GRADIENT_FILTER_RADIUS = 8;
	
	static final private Color[] COMMANDCOLORS = { // please change also colorToCommandType if you change sth. here!
		Color.black, //NOTHING
		Color.blue, //RUN
		Color.green, //CATCH
		Color.red, //ATTACK
	};
	
	private Image commandImage;
	private Graphics commandImageG;
	private Shader mapShader;
	
	private static final int FADE_INTERVAL = 40; 
	private int drawsSinceLastFade = 0;
	
	public CommandMap() throws SlickException {
		commandImage = new Image(RESOLUTION_X, RESOLUTION_Y);

		commandImageG = commandImage.getGraphics();
		commandImageG.setBackground(COMMANDCOLORS[CommandType.NOTHING.ordinal()]);
		commandImageG.clear();

		mapShader = new Shader("shader/simple.vert", "shader/map.frag");
		mapShader.start();
		mapShader.setUniform("tex", 0);
		mapShader.end();
	}
	
	// Needs to be called during a Game.Draw !!
	public void paint(Circle area, CommandType command) {
		Circle commandMapCircle = new Circle(gameCoordToCommandCoordX(area.getCenterX()),
											 gameCoordToCommandCoordY(area.getCenterY()),
											 gameCoordToCommandCoordScale(area.getRadius()));
		
		commandImageG.setColor(COMMANDCOLORS[command.ordinal()]);
		commandImageG.fill(commandMapCircle);
	}
	
	public void draw(Graphics g) {
	/*	++drawsSinceLastFade;
		if(drawsSinceLastFade > FADE_INTERVAL) { 
			commandImageG.setColor(new Color(0.0f, 0.0f, 0.0f, 0.05f));
			commandImageG.fillRect(0, 0, RESOLUTION_X, RESOLUTION_Y);
			drawsSinceLastFade = 0;
		}*/
		
		commandImageG.flush();
		mapShader.start();
		g.drawImage(commandImage, 0,0, Game.GAME_COORD_SIZE.x, Game.GAME_COORD_SIZE.y, 0,0, RESOLUTION_X, RESOLUTION_Y);
		mapShader.end();
		
		commandImage.flushPixelData();
	}
	
	public CommandType getCommandAt(Vector2f gamePosition) {
		Color c = commandImage.getColor((int)(gameCoordToCommandCoordX(gamePosition.x)), 
										(int)(gameCoordToCommandCoordY(gamePosition.y)));
		return colorToCommandType(c);
	}
	
	private CommandType colorToCommandType(Color color) {
		if(color.b > 0.01f) {
			return CommandType.RUN;
		} else if(color.g > 0.01f) {
			return CommandType.CATCH;
		} else if(color.r > 0.01f) {
			return CommandType.ATTACK;
		} else {
			return CommandType.NOTHING;
		}
	}
	
	private float gameCoordToCommandCoordX(float x) {
		return x / Game.GAME_COORD_SIZE.x * RESOLUTION_X;
	}
	private float gameCoordToCommandCoordY(float y) {
		return y / Game.GAME_COORD_SIZE.y * RESOLUTION_Y;
	}
	private float gameCoordToCommandCoordScale(float scale) {
		return gameCoordToCommandCoordY(scale);
	}
	
	private boolean onMap(int x, int y) {
		return x >= 0 && y >= 0 && x < RESOLUTION_X && y < RESOLUTION_Y;
	}
	
	// Get an approximated normal between command and boring areas.
	// Returns normalized direction vector pointing away from nothing.
	public Vector2f getBoundaryGradient(Vector2f gamePosition) {
		// The target vector
		Vector2f direction = new Vector2f(0,0);
		// Center coord in map coordinates
		int cx = (int)(gameCoordToCommandCoordX(gamePosition.x));
		int cy = (int)(gameCoordToCommandCoordY(gamePosition.y));
		// Iterate over a circle
		for(int y = -GRADIENT_FILTER_RADIUS; y <= GRADIENT_FILTER_RADIUS; ++y) {
			int r = (int)Math.cos(y * 1.570796327f / GRADIENT_FILTER_RADIUS);
			for(int x = -r; x <= r; ++x) {
				Vector2f pdir = new Vector2f(x,y);
				if(pdir.lengthSquared() == 0.0f) continue;
				pdir.scale(1.0f / pdir.lengthSquared());
				if(!onMap(x+cx,y+cy) || colorToCommandType(commandImage.getColor(x+cx, y+cy)) == CommandType.NOTHING)
					direction.sub(pdir);
				else direction.add(pdir);
			}
		}
		return direction.getNormal();
	}

	public Image getImage() {
		return commandImage;
	}
	
	public void clear() {
		commandImageG.clear();
		commandImageG.flush();
		commandImage.flushPixelData();
	}
}
