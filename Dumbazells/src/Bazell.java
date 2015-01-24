import java.awt.HeadlessException;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Bazell {
	
	private int playerIndex;
	
	final float TIMER = 5; 				//Time until Bazell runs Amok 
	final float SCALE = 0.01f; 			// scaling the image
	final float ACCELERATION = 0.01f;	// the added movementspeed that it gets by each bounce
	final float ATK = 0.1f;
	final float FULL_HEALTH = 1f;
	float health;
	float speed = 0.01f; 				// initial speed when in command area for moveing
	float timerForAmok; 				//timer for the Amok
	float amokTime = 3;

	boolean idle; 			// What do I do now?
	boolean inCommandArea; 	// is it on any command area
	boolean carriesFlag;	// carries a flag y/n
	
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
		health = FULL_HEALTH;
		String ref = "images/Bazell.png";
		try {
			sprite = new Image(ref);
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//simple method bounces on map edges
	private void bounceOnMap()
	{
		
		if(position.x <= 0)
		{
			position.x = 0.001f;
			direction.x = -direction.x;
			running();
		}
		else if(position.x + sprite.getWidth()*SCALE >= 16)
		{
			position.x =15.8f-sprite.getHeight()*SCALE;
			direction.x = -direction.x;
			running();
		}
		if(position.y <= 0 )
		{
			position.y = 0.001f;
			direction.y = -direction.y;
			running();
		}
		else if(position.y + sprite.getHeight()*SCALE >= 9){
			direction.y = -direction.y;
			position.y = 8.999999f-sprite.getWidth()*SCALE;
			running();
		}
	}
	
	/*get und setters*/
	public int getPlayer(){
		return playerIndex;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	//makes the bazell run in runCommand and accelrerate until certain point
	private void running()
	{
		if(speed<0.25f)
			speed = (float) (1 - 1* Math.exp(-1.5 * speed));
	}
	
	//makes that Bazell attacks
	private void attacking(Bazell otherBazell)
	{
		if(playerIndex != otherBazell.getPlayer()){
			otherBazell.health--;
		}
		position = position.sub(otherBazell.getPosition().normalise());
	}

	private void carries()
	{
		// go to flag 
		// if flag is not on sb else take it and go to base?
	}
	
	//killing other Bazells and erases the command
	private void runAmok(CommandMap commandMap, Bazell otherBazelle){
		amokTime -= 0.3f;
		if(amokTime ==0){}
			//die
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

		bounceOnMap();
		
		CommandType here = commandMap.getCommandAt(position);
		if(here== CommandType.NOTHING)
		{
			idle= true;
			speed = 0;
		}
		else if(here == CommandType.RUN){
			idle =false;
			speed = ACCELERATION;
		}
			
		if(inCommandArea)
	
		if(idle) {timerForAmok--; speed = 0;}
		else {timerForAmok = TIMER; speed = 0.1f;}
		
		if(timerForAmok ==0){
//			runAmok(commandMap, otherBazell);
			}
		if(health ==0 )
			//die
		oldPosition = position;
		
	}
	
	
	public void render(Graphics g){
		sprite.draw(position.x,position.y, SCALE);
	}

}
