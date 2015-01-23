import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;


public class CommandMap {
	static final int RESOLUTION_X = 16 * 50;
	static final int RESOLUTION_Y = 9 * 50;
	
	static final private Color[] COMMANDCOLORS = {
		Color.black, //NOTHING
		Color.yellow, //RUN
		Color.blue, //CATCH
		Color.red, //ATTACK
	};
	
	private Image commandImage;
	private Graphics commandImageG;
	
	public CommandMap() throws SlickException {
		commandImage = new Image(RESOLUTION_X, RESOLUTION_Y);

		commandImageG = commandImage.getGraphics();
		commandImageG.setBackground(COMMANDCOLORS[CommandType.NOTHING.ordinal()]);
		commandImageG.clear();
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
		commandImageG.flush();
		g.drawImage(commandImage, 0,0, 16.0f, 9.0f, 0,0, RESOLUTION_X, RESOLUTION_Y);
	}
	
	public CommandType getCommandAt(Vector2f gamePosition) {
		Color c = commandImageG.getPixel((int)(gameCoordToCommandCoordX(gamePosition.x)), 
										 (int)(gameCoordToCommandCoordY(gamePosition.y)));
		
		if(c == COMMANDCOLORS[CommandType.NOTHING.ordinal()])
			return CommandType.NOTHING;
		else if(c == COMMANDCOLORS[CommandType.RUN.ordinal()])
			return CommandType.RUN;
		else if(c == COMMANDCOLORS[CommandType.ATTACK.ordinal()])
			return CommandType.ATTACK;
		else
			return CommandType.CATCH;
	}
	
	private float gameCoordToCommandCoordX(float x) {
		return x / 16.0f * RESOLUTION_X;
	}
	private float gameCoordToCommandCoordY(float y) {
		return y / 9.0f * RESOLUTION_Y;
	}
	private float gameCoordToCommandCoordScale(float scale) {
		return gameCoordToCommandCoordY(scale);
	}
}
