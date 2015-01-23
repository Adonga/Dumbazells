
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;


public class Bazell {
	
	
	float timer;
	int playerIndex;
	Vector2f position;
	Vector2f direction;
	float speed = 0.1f;
	boolean attacking;
	boolean idle;
	float timerForAmock;
	Image picture;
	public Bazell(float livingtime,int PlayerIndex,Vector2f position)
	{
		Vector2f a = new Vector2f();
		playerIndex = PlayerIndex;
		this.position = position;
		this.direction = new Vector2f(1,1);
	}
	
	
	public void update(Input input, int passedTimeMS){
		direction.normalise();
		position.x = position.x + direction.x * speed;
		position.y = position.y + direction.y * speed;
		timer--;
	}
	
	public void render(Graphics g){
		g.setColor(Color.cyan);
		g.draw(new Circle(position.x, position.y, 5));
//		picture.draw(position.x,position.y);
	}
}
