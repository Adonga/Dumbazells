
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


public class Bazell {
	
	int playerIndex;
	
	final float timer = 5;
	final float scale = 0.01f;
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
	
	
	public void update(int passedTimeMS){
		if(position.x > 0 && position.x+picture.getWidth()*scale < 16 &&
				position.y+picture.getHeight()*scale < 9 && position.y>0 )
		{
		direction.normalise();
		position.x = position.x + direction.x * speed;
		position.y = position.y + direction.y * speed;
		}
		
		if(attacking)
	
		if(idle) {timerForAmock--; speed = 0;}
		else {timerForAmock = timer; speed = 0.001f;}
		
		if(timerForAmock ==0){}// rage

	}
	
	public void render(Graphics g){
		picture.draw(position.x,position.y, scale);
	}
}
