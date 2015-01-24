import java.awt.HeadlessException;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Bazell {
	
	private int playerIndex;
	
	final float TIMER = 5; 				//Time until Bazell runs Amok 
	final float SCALE = 0.01f; 			// scaling the image
	
	final float FRICTION = 0.98f;
	final float ACCELERATION = 0.00008f;	// the added movementspeed that it gets by each bounce
	final float NORMAL_SPEED = 0.01f;
	final float FLAG_SPEED = NORMAL_SPEED * 0.1f;
	
	final float FLAG_GATHER_RADIUS = SCALE * 3.0f;
	final float FLAG_GO_RADIUS = FLAG_GATHER_RADIUS * 6.0f;
	
	final float ATK = 0.1f;
	final float FULL_HEALTH = 1f;
	
	
	float health;
	float speed = 0.0f; 				// initial speed when in command area for moveing
	float timerForAmok; 				//timer for the Amok
	float amokTime = 3;

	boolean idle; 			// What do I do now?
	boolean carriesFlag;	// carries a flag y/n
	
	private CommandType commandArea; 	// is on command area XY
	
	private Vector2f oldPosition; 	//
	private Vector2f position; 		//
	private Vector2f direction;		//
	private boolean bouncedLastFrame = false;
	
	private Image sprite;
	
	private Flag owningFlag = null;
	
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
		if(speed < NORMAL_SPEED)
			speed = NORMAL_SPEED;
		
		// accellerate a bit
		speed += ACCELERATION;
	}
	
	//makes that Bazell attacks
	private void attacking(Bazell otherBazell)
	{
		if(speed < NORMAL_SPEED)
			speed = NORMAL_SPEED;
		
		if(playerIndex != otherBazell.getPlayer()){
			otherBazell.health--;
		}
		position = position.sub(otherBazell.getPosition().normalise());
	}

	private void carries(Flag[] flags)
	{
		if(speed < FLAG_SPEED)
			speed = FLAG_SPEED;
		
		if(owningFlag == null) {
			float minDistSq = 10000.0f;
			Flag bestFlag = null;
			for(Flag flag : flags) {
				float newDistSq = flag.getPosition().distanceSquared(position);
				if(newDistSq < minDistSq) {
					minDistSq = newDistSq;
					bestFlag = flag;
				}
			}
			
			float dist = (float)Math.sqrt(minDistSq);
			if(dist < FLAG_GATHER_RADIUS) {
				bestFlag.setCarriedBy(this);
				owningFlag = bestFlag;
			} else if(dist < FLAG_GO_RADIUS) {
				direction = new Vector2f(bestFlag.getPosition());
				direction.sub(position);
				direction.normalise();
			}
		}
	}
	
	//killing other Bazells and erases the command
	private void runAmok(CommandMap commandMap, Bazell otherBazelle){
		amokTime -= 0.3f;
		if(amokTime ==0){}
			//die
	}
	
	public void update(CommandMap commandMap, Flag[] flags, Basis ownBasis){
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
			}
			// staying the same but not nothing
			else if(commandArea == CommandType.CATCH) { 
				carries(flags);
			} 
			else if(commandArea == CommandType.RUN) { 
				this.running();
			} 
			
			bouncedLastFrame = false;
		}
		// changed
		else {
			// from something to nothing -> reflect!
			if(nextCommand == CommandType.NOTHING) {
				position = new Vector2f(oldPosition); // reset
				
				if(bouncedLastFrame) { // give up - random dir
					double randomAngle = (float)(Math.random() * Math.PI * 2.0);
					direction.x = (float)Math.sin(randomAngle);
					direction.y = (float)Math.cos(randomAngle);
				} else {
					
					reflectAtMap(commandMap);
					bouncedLastFrame = true;
				}
			}
			// was nothing, now something
			else {
				commandArea = nextCommand;
				bouncedLastFrame = false;
			}
		}
		
		
		// important - map may have changed!
		commandArea = commandMap.getCommandAt(position);
		
		// always apply friction
		if(commandArea != CommandType.RUN)
			speed *= FRICTION;
		
//		idle ? timerForAmok-- : timerForAmok = TIMER ;
//		if(idle) {timerForAmok--;}
//		else {timerForAmok = TIMER;}
		
//		if(timerForAmok ==0){
//			runAmok(commandMap, otherBazell);
//			}

		// lose flag at own base
		if(owningFlag != null && ownBasis.getPosition().distance(position) < Basis.BASE_SIZE) {
			owningFlag.setCarriedBy(null);
			owningFlag = null;
		}
	}
	
	
	public void render(Graphics g){
		sprite.draw(position.x - sprite.getWidth() * SCALE * 0.5f, position.y - sprite.getHeight() * SCALE * 0.5f ,SCALE );
	}


	// Reflects the internal direction at the map gradient
	private void reflectAtMap(CommandMap commandMap) {
		Vector2f gradient = commandMap.getBoundaryGradient(position);
		Vector2f oldDirection = new Vector2f(direction);
		direction.sub(gradient.scale(2.0f * gradient.dot(direction)));
		
		//System.out.println("bounce");
		
		if(direction.lengthSquared() < 0.1) {
			direction = oldDirection;
			direction.scale(-1.0f);
			System.out.println("blub!!");
		}
	}
}
