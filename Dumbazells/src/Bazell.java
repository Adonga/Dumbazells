
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


public class Bazell {
	
	int playerIndex;
	
	final float timer = 5;
	float speed = 0.001f;
	float timerForAmock;
	
	boolean attacking;
	boolean idle;
	boolean amock;
	
	Vector2f position;
	Vector2f direction;
	
	Image picture;
	
	public Bazell(int PlayerIndex,Vector2f position)
	{
		playerIndex = PlayerIndex;
		this.position = position;
		this.direction = new Vector2f(1,1);
		timerForAmock = timer;
		String ref = "images/Bazell.png";
		try {
			picture = new Image(ref);
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void update(Input input, int passedTimeMS){
		direction.normalise();
		position.x = position.x + direction.x * speed;
		position.y = position.y + direction.y * speed;
		
		
		
		if(attacking)
	
		if(idle) timerForAmock--;
		else timerForAmock = timer;
		
		if(timer ==0){}// rage

	}
	
	public void render(Graphics g){
		picture.draw(position.x,position.y, 0.01f);
	}
}
