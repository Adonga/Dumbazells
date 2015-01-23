import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;


public class Player {
	static private final float cursorSpeed = 0.02f;
	
	private int controllerIndex = -1;
	private Circle drawCircle = new Circle(8.0f, 4.5f, 0.2f);
	
	
	Player(int controllerIndex) {
		this.controllerIndex = controllerIndex;
	}
	
	public void update(Input input, int passedTimeMS) {
		
		float movementY = 0.0f;
		float movementX = 0.0f;
		
		switch(controllerIndex) {
		case 0:
			movementY += input.isKeyDown(Input.KEY_DOWN) ? cursorSpeed : 0.0f;
			movementY -= input.isKeyDown(Input.KEY_UP) ? cursorSpeed : 0.0f;
			movementX += input.isKeyDown(Input.KEY_RIGHT) ? cursorSpeed : 0.0f;
			movementX -= input.isKeyDown(Input.KEY_LEFT) ? cursorSpeed : 0.0f;
			break;
		}
		
		drawCircle.setCenterX(drawCircle.getCenterX() + movementX);
		drawCircle.setCenterY(drawCircle.getCenterY() + movementY);
	}
	
	public void render(Graphics g) {
		g.setColor(Color.pink);
		g.draw(drawCircle);
	}

	public int getControlerIndex() {
		return controllerIndex;
	}
}
