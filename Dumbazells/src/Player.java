import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;


public class Player {
	static private final float cursorSpeed = 0.02f;
	
	private int controllerIndex = -1;
	private Circle paintCircle = new Circle(8.0f, 4.5f, 0.2f);
	private CommandType nextCommandType;
	
	Player(int controllerIndex) {
		this.controllerIndex = controllerIndex;
	}
	
	public void update(Input input) {
		
		float movementY = 0.0f;
		float movementX = 0.0f;
		nextCommandType = null;
		
		switch(controllerIndex) {
		case 0:
			movementY += input.isKeyDown(Input.KEY_DOWN) ? cursorSpeed : 0.0f;
			movementY -= input.isKeyDown(Input.KEY_UP) ? cursorSpeed : 0.0f;
			movementX += input.isKeyDown(Input.KEY_RIGHT) ? cursorSpeed : 0.0f;
			movementX -= input.isKeyDown(Input.KEY_LEFT) ? cursorSpeed : 0.0f;
			if(input.isKeyDown(Input.KEY_1)) {
				nextCommandType = CommandType.NOTHING;
			} else if(input.isKeyDown(Input.KEY_2)) {
				nextCommandType = CommandType.RUN;
			} else if(input.isKeyDown(Input.KEY_3)) {
				nextCommandType = CommandType.CATCH;
			} else if(input.isKeyDown(Input.KEY_4)) {
				nextCommandType = CommandType.ATTACK;
			}
			break;
		}
		
		paintCircle.setCenterX(paintCircle.getCenterX() + movementX);
		paintCircle.setCenterY(paintCircle.getCenterY() + movementY);
		
		
	}
	
	public void render(CommandMap commandMap, Graphics g) {
		if(nextCommandType != null) {
			commandMap.paint(paintCircle, nextCommandType);
		}
		
		g.setColor(Color.pink);
		g.draw(paintCircle);
	}

	public int getControlerIndex() {
		return controllerIndex;
	}
}
