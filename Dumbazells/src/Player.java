import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;


public class Player {
	static private final float cursorSpeed = 0.03f;
	static private final float controllerDeadZone = 0.2f;
	
	private Circle paintCircle = new Circle(8.0f, 4.5f, 1.2f);
	private CommandType nextCommandType;
	
	private int controllerIndex;
	private Controller controller = null;
	
	int i=0;
	
	Player(int controllerIndex) {
		this.controllerIndex = controllerIndex;
		
		// Find controllerIndex'th xbox controller
		int foundXboxControllerCount = 0;
		for(int i = 0; i < Controllers.getControllerCount(); ++i) {
			if(Controllers.getController(i).getName().toLowerCase().contains("xbox")) {
				if(foundXboxControllerCount == controllerIndex) {
					controller = Controllers.getController(i);
					controller.setXAxisDeadZone(controllerDeadZone);
					controller.setYAxisDeadZone(controllerDeadZone);
				}
				++foundXboxControllerCount;
			}
		}
	}
	
	public void update(Input input) {
		
		float movementY = 0.0f;
		float movementX = 0.0f;
		nextCommandType = null;
		
		// Extra controls
		switch(controllerIndex) {
		case 0:
			if(input.isKeyDown(Input.KEY_1)) {
				nextCommandType = CommandType.NOTHING;
			} else if(input.isKeyDown(Input.KEY_2)) {
				nextCommandType = CommandType.RUN;
			} else if(input.isKeyDown(Input.KEY_3)) {
				nextCommandType = CommandType.CATCH;
			} else if(input.isKeyDown(Input.KEY_4)) {
				nextCommandType = CommandType.ATTACK;
			}
			
			movementY += input.isKeyDown(Input.KEY_DOWN) ? cursorSpeed : 0.0f;
			movementY -= input.isKeyDown(Input.KEY_UP) ? cursorSpeed : 0.0f;
			movementX += input.isKeyDown(Input.KEY_RIGHT) ? cursorSpeed : 0.0f;
			movementX -= input.isKeyDown(Input.KEY_LEFT) ? cursorSpeed : 0.0f;
			
			break;
		}
		
	
		if(controller != null && controller.getAxisCount() >= 2) {
			if(controller.isButtonPressed(3)) {
				nextCommandType = CommandType.NOTHING;
			} else if(controller.isButtonPressed(2)) {
				nextCommandType = CommandType.RUN;
			} else if(controller.isButtonPressed(0)) {
				nextCommandType = CommandType.CATCH;
			} else if(controller.isButtonPressed(1)) {
				nextCommandType = CommandType.ATTACK;
			}
			
			movementX += controller.getXAxisValue() * cursorSpeed;
			movementY += controller.getYAxisValue() * cursorSpeed;
		}
		
		// Double speed if player does not draw.
		if(nextCommandType == null) {
			movementX *= 2.0f;
			movementY *= 2.0f;
		}
		
		if(paintCircle.getCenterX() + movementX>=0 &&paintCircle.getCenterX() + movementX<=16  )
			paintCircle.setCenterX(paintCircle.getCenterX() + movementX);
		
		if(paintCircle.getCenterY() + movementY>=0 && paintCircle.getCenterY() + movementY<=9 )
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
