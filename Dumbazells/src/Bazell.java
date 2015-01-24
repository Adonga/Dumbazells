import java.awt.HeadlessException;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Bazell {
	
	private int playerIndex;
	
	final float TIMER = 5; 				//Time until Bazell runs Amok 
	final float SCALE = 0.01f; 			// scaling the image
	
	final float FRICTION = 0.8f;
	final float ACCELERATION = 1.001f;	// the added movementspeed that it gets by each bounce
	final float START_SPEED = 0.01f;
	
	
	final float ATK = 0.1f;
	final float FULL_HEALTH = 1f;
	
	
	float health;
	float speed = 0.0f; 				// initial speed when in command area for moveing
	float timerForAmok; 				//timer for the Amok
	float amokTime = 3;

	boolean idle; 			// What do I do now?
	boolean carriesFlag;	// carries a flag y/n
	
	CommandType commandArea; 	// is on command area XY
	
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
		commandArea = CommandType.NOTHING;
	}
	
	/*get und setters*/
	public int getPlayer(){
		return playerIndex;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	//simple method bounces on map edges
	private void bounceOnMap()
	{
		if(position.x <= 0)
		{
			position.x = 0.001f;
			direction.x = -direction.x;
		}
		else if(position.x + sprite.getWidth()*SCALE*0.5f >= Game.GAME_COORD_SIZE.x)
		{
			position.x =15.8f-sprite.getHeight()*SCALE*0.5f;
			direction.x = -direction.x;
		}
		if(position.y <= 0 )
		{
			position.y = 0.001f;
			direction.y = -direction.y;
		}
		else if(position.y + sprite.getHeight()*SCALE*0.5f >= Game.GAME_COORD_SIZE.y){
			direction.y = -direction.y;
			position.y = 8.999999f - sprite.getWidth()*SCALE*0.5f;
		}
	}
	
	//makes the bazell run in runCommand and accelrerate until certain point
	private void running()
	{
		// accellerate a bit
		speed *= ACCELERATION;
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
		oldPosition = new Vector2f(position);

		direction.normalise();
		position.x = position.x + direction.x * speed;
		position.y = position.y + direction.y * speed;
		bounceOnMap();
				
		CommandType nextCommand = commandMap.getCommandAt(position);
		
		// staying the same
		if(nextCommand == commandArea) {
			// staying nothing
			if(commandArea == CommandType.NOTHING) {
				idle = true;
				speed *= FRICTION;
			}
			// staying the same but not nothing
			else {
				running();
			} 
		}
		// changed
		else {
			// from something to nothing -> reflect!
			if(nextCommand == CommandType.NOTHING) {
				position = oldPosition; // reset
				reflectAtMap(commandMap);
				
				// important - map may be changed!
				commandArea = commandMap.getCommandAt(position);
			}
			// was nothing, now something -> speed up
			else {
				speed = START_SPEED;
				commandArea = nextCommand;
			}
		}
		
		
//		idle ? timerForAmok-- : timerForAmok = TIMER ;
//		if(idle) {timerForAmok--;}
//		else {timerForAmok = TIMER;}
		
//		if(timerForAmok ==0){
//			runAmok(commandMap, otherBazell);
//			}

	}
	
	
	public void render(Graphics g){
		sprite.draw(position.x - sprite.getWidth() * SCALE * 0.5f, position.y - sprite.getHeight() * SCALE * 0.5f ,SCALE );
	}


	// Reflects the internal direction at the map gradient
	private void reflectAtMap(CommandMap commandMap) {
		Vector2f gradient = commandMap.getBoundaryGradient(position);
		direction = direction.sub(gradient.scale(2 * gradient.dot(direction)));
	}
}
