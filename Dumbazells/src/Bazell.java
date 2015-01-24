
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;


public class Bazell {
	
	int playerIndex;
	
	final float TIMER = 5; 				//Time until Bazell runs Amok 
	final float SCALE = 0.01f; 			// scaling the image
	final float ACCELERATION = 0.01f;	// the added movementspeed that it gets by each bounce
	float speed = 0.01f; 				// initial speed when in command area for moveing
	float timerForAmok; 				//timer for the Amok

	boolean idle; 			// What do I do now?
	boolean Amok; 			// has it been to long idle that it runs on a amok?
	boolean inCommandArea; // is it on any command area
	boolean carriesFlag;
	
	private Vector2f oldPosition; 	//
	private Vector2f position; 		//
	private Vector2f direction;		//
	
	Image sprite;
	
	public Bazell(int PlayerIndex,Vector2f position)
	{
		playerIndex = PlayerIndex;
		this.position = position;
		this.direction = new Vector2f(-1,-1);
		timerForAmok = TIMER;
		String ref = "images/Bazell.png";
		try {
			sprite = new Image(ref);
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void update(int passedTimeMS, CommandMap commandMap){
		
		oldPosition = position;
			
		if(oldPosition.x > 0 && oldPosition.x+sprite.getWidth()*SCALE < 16 &&
				oldPosition.y+sprite.getHeight()*SCALE < 9 && oldPosition.y>0 )
		{
		direction.normalise();
		position.x = position.x + direction.x * speed;
		position.y = position.y + direction.y * speed;
		}
		if(position.x <= 0)
		{
			position.x = 0.001f;
			direction.x = -direction.x;
			speed += ACCELERATION;
		}
		if(position.x + sprite.getWidth()*SCALE >= 16)
		{
			position.x =15.8f-sprite.getHeight()*SCALE;
			direction.x = -direction.x;
			speed += ACCELERATION;
		}
		if(position.y <= 0 )
		{
			position.y = 0.001f;
			direction.y = -direction.y;
			speed += ACCELERATION;
		}
		if(position.y + sprite.getHeight()*SCALE >= 9){
			direction.y = -direction.y;
			position.y = 8.999999f-sprite.getWidth()*SCALE;
			speed += ACCELERATION;
		}
		
		
//		if(commandMap.getCommandAt(position)== CommandType.NOTHING)
//		{
//			idle= true;
//			speed = 0;
//		}
//		else 
//			commandMap.getCommandAt(position);
			
		if(inCommandArea)
	
		if(idle) {timerForAmok--; speed = 0;}
		else {timerForAmok = TIMER; speed = 0.1f;}
		
		if(timerForAmok ==0){Amok = true;}// rage
		oldPosition = position;
	}
	
	public void render(Graphics g){
		sprite.draw(position.x,position.y, SCALE);
	}

	public Vector2f getPosition() {
		return position;
	}
}
