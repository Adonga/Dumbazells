import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;


public class Player {
	private int controlerIndex = -1;
	
	Player(int controlerIndex) {
		this.controlerIndex = controlerIndex;
	}
	
	public void update(Input input, int passedTimeMS) {
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.pink);
		g.draw(new Circle(100, 100, 10));
	}

	public int getControlerIndex() {
		return controlerIndex;
	}
}
