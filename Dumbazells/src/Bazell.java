import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Bazell {
	
	private int playerIndex;

	static final float ACTION_RADIUS = 0.35f; 
	
	static final float FRICTION = 0.99f;
	static final float ACCELERATION = 0.00003f;	// the added movementspeed that it gets by each bounce
	static final float NORMAL_SPEED = 0.01f;
	static final float MAX_SPEED = 0.1f;
	static final float FLAG_SPEED = NORMAL_SPEED * 0.5f;
	
	static final float FLAG_GATHER_RADIUS = ACTION_RADIUS;
	static final float FLAG_GO_RADIUS = FLAG_GATHER_RADIUS * 4.0f;

	static final int STEPS_TO_AGGRO = 60 * 20;
	static final int AMOK_DURATION = 60 * 5;
	private int passedAgroSteps;
	private boolean amok = false;

	float speed = 0.0f; 				// initial speed when in command area for moveing

	private CommandType commandArea; 	// is on command area XY
	
	private Vector2f oldPosition; 	//
	private Vector2f position; 		//
	private Vector2f direction = new Vector2f();		//
	private boolean bouncedLastFrame = false;
	
	private Flag owningFlag = null;

	private Image[] circleImages;
	private Image[] rauteImages;
	private Image[] squareImages;
	private Image[] triangleImages;

	static final float IMAGE_SCALE = 0.01f; // 0.003f
	
	private boolean dead = false;
	private int deadTimer = 0; 
	static final int DEAD_DURATION = 400;
	
	public boolean NeedsDelete() { return dead && deadTimer > DEAD_DURATION; }
	
	public Bazell(int PlayerIndex,Vector2f position)
	{
		playerIndex = PlayerIndex;
		this.position = position;
		randomizeDirection();
		
		commandArea = CommandType.NOTHING;

		try {
			if(circleImages == null)
				circleImages = new Image[] {
						new Image("images/circle/happy.png"),
						new Image("images/circle/smile.png"),
						new Image("images/circle/normal.png"),
						new Image("images/circle/sad.png"),
						new Image("images/circle/angry.png"),
						new Image("images/circle/dead.png")
				};

			if(rauteImages == null)
				rauteImages = new Image[] {
						new Image("images/raute/happy.png"),
						new Image("images/raute/smile.png"),
						new Image("images/raute/normal.png"),
						new Image("images/raute/sad.png"),
						new Image("images/raute/angry.png"),
						new Image("images/raute/dead.png")
				};

			if(squareImages == null)
				squareImages = new Image[] {
						new Image("images/square/happy.png"),
						new Image("images/square/smile.png"),
						new Image("images/square/normal.png"),
						new Image("images/square/sad.png"),
						new Image("images/square/angry.png"),
						new Image("images/square/dead.png")
				};

			if(triangleImages == null)
				triangleImages = new Image[] {
						new Image("images/triangle/happy.png"),
						new Image("images/triangle/smile.png"),
						new Image("images/triangle/normal.png"),
						new Image("images/triangle/sad.png"),
						new Image("images/triangle/angry.png"),
						new Image("images/triangle/dead.png")
				};

		} catch (SlickException e) {
			e.printStackTrace();
		}
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
		if(position.getX() - circleImages[0].getWidth() * IMAGE_SCALE * 0.5f <= 0)
		{
			position.x = 0.01f + circleImages[0].getWidth() * IMAGE_SCALE * 0.5f;
			direction.x = -direction.x;
		}
		else if(position.getX() + circleImages[0].getWidth() * IMAGE_SCALE * 0.5f >= Game.GAME_COORD_SIZE.x)
		{
			position.x = 15.99f - circleImages[0].getWidth() * IMAGE_SCALE  * 0.5f;
			direction.x = -direction.x;
		}
		if(position.getY()  - circleImages[0].getHeight() * IMAGE_SCALE * 0.5f <= 0 )
		{
			position.y = 0.01f + circleImages[0].getHeight() * IMAGE_SCALE * 0.5f;
			direction.y = -direction.y;
		}
		else if(position.getY() + circleImages[0].getHeight() * IMAGE_SCALE  * 0.5f >= Game.GAME_COORD_SIZE.y){
			direction.y = -direction.y;
			position.y = 8.999999f - circleImages[0].getWidth() * IMAGE_SCALE  * 0.5f;
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
	private void attacking(ArrayList<Bazell> allBazells) {
		if(speed < NORMAL_SPEED)
			speed = NORMAL_SPEED;
		
		// binary search enemy bazells - search first index which is smaller than position.x-ACTION_RADIUS
		float smallerThanX = position.x- ACTION_RADIUS;
		int min = 0;
		int max = allBazells.size();
		while(max - min >= 1) {
			int mid = (max + min) / 2;
			
			Bazell other = allBazells.get(mid); 		
			if(other.getPosition().x+ ACTION_RADIUS < smallerThanX) {
				min = mid + 1;
			} else {
				max = mid;
			}
		}
		
		for(int i=min; i<allBazells.size(); ++i) {
			Bazell other = allBazells.get(i);
			if(other.dead || other.amok ||
			  (!amok && other.playerIndex == playerIndex) || other == this) continue;
			
			if(other.getPosition().x - ACTION_RADIUS > position.x + ACTION_RADIUS) // won't find anything anymore
				break;
			
			// kill?
			if(other.getPosition().distanceSquared(position) < ACTION_RADIUS * ACTION_RADIUS) {
				die();
				other.die();
				break;
			}
		}
	}
	
	private void die() {
		if(amok) return; // cannot die!
		
		dead = true;
		if(owningFlag != null) {
			owningFlag.setCarriedBy(null);
			owningFlag = null;
		}
	}

	private void carries(Flag[] flags, Basis ownBase) {
		if(speed < FLAG_SPEED)
			speed = FLAG_SPEED;
		
		if(owningFlag == null) {
			float minDistSq = 10000.0f;
			Flag bestFlag = null;
			for(Flag flag : flags) {
				if (flag.getCarriedBy() != null) continue; // ignore carried flags
				if (flag.getPosition().distance(ownBase.getPosition()) < Basis.BASE_SIZE) continue;
				
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
	private void runAmok(ArrayList<Bazell> allbazells){
		if(speed < NORMAL_SPEED*2)
			speed = NORMAL_SPEED*2;
		
		speed += ACCELERATION * 20;
		++passedAgroSteps;
		if(passedAgroSteps > AMOK_DURATION) {
			amok = false;
			die();
		}
		//commandMap.paint(new Circle(position.x, position.y, this.ACTION_RADIUS), CommandType.NOTHING);
		attacking(allbazells);
	}
	
	public void update(CommandMap commandMap, Flag[] flags, Basis ownBasis, ArrayList<Bazell> allbazells){
		if(dead) {
			++deadTimer;
			return;
		}
		
		oldPosition = new Vector2f(position);

		direction.normalise();
		position.x = position.x + direction.x * speed;
		position.y = position.y + direction.y * speed;
		bounceOnMap();
				
		CommandType nextCommand = commandMap.getCommandAt(position);
		
		if(speed > MAX_SPEED)
			speed = MAX_SPEED;
		
		if(!amok) {
		
			// staying the same
			if(nextCommand == commandArea) {
				// staying nothing
				/*if(commandArea == CommandType.NOTHING) {
					idle = true;
				}
				// staying the same but not nothing
				else */if(commandArea == CommandType.CATCH) { 
					carries(flags, ownBasis);
				} 
				else if(commandArea == CommandType.RUN) { 
					this.running();
				} 
				else if(commandArea == CommandType.ATTACK) {
					this.attacking(allbazells);
				}
				
				bouncedLastFrame = false;
			}
			// changed
			else {
				// from something to nothing -> reflect!
				if(nextCommand == CommandType.NOTHING) {
					position = new Vector2f(oldPosition); // reset
					
					if(bouncedLastFrame) { // give up - random dir
						randomizeDirection();
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
			
			
			if(commandArea == CommandType.NOTHING ) {
				++passedAgroSteps;
				if(passedAgroSteps > STEPS_TO_AGGRO) {
					passedAgroSteps = 0;
					amok = true;
					randomizeDirection();
				}
			}
			else {
				--passedAgroSteps;
				if(passedAgroSteps < 0)
					passedAgroSteps = 0;
			}
		} else {
			runAmok(allbazells);
		}
		
		
		// important - map may have changed!
		commandArea = commandMap.getCommandAt(position);
		
		// always apply friction
		if(commandArea != CommandType.RUN)
			speed *= FRICTION;
		
		// loose flag at own base
		if(owningFlag != null && ownBasis.getPosition().distance(owningFlag.getPosition()) < Basis.BASE_SIZE) {
			owningFlag.setCarriedBy(null);
			owningFlag = null;
			position = new Vector2f(oldPosition);
		}
	}
	
	private void randomizeDirection() {
		double randomAngle = (float)(Math.random() * Math.PI * 2.0);
		direction.x = (float)Math.sin(randomAngle);
		direction.y = (float)Math.cos(randomAngle);
	}
	
	public void render(Graphics g) {

		float imageSizeScaled = circleImages[0].getWidth() * IMAGE_SCALE;

		int imageIndex = -1;
		if(amok) {
			imageIndex = 4;
		} else if (dead) {
			imageIndex = 5;
		} else {
			int aggro = passedAgroSteps;
			do {
				aggro -= STEPS_TO_AGGRO / 4;
				++imageIndex;
			} while(aggro > 0 && imageIndex < circleImages.length - 2);
		}
		
		switch (playerIndex) {
			case 0:
				circleImages[imageIndex].draw(position.getX() - imageSizeScaled / 2, position.getY() - imageSizeScaled / 2, IMAGE_SCALE);
				break;

			case 1:
				rauteImages[imageIndex].draw(position.getX() - imageSizeScaled/2, position.getY() - imageSizeScaled/2, IMAGE_SCALE);
				break;

			case 2:
				squareImages[imageIndex].draw(position.getX() - imageSizeScaled/2, position.getY() - imageSizeScaled/2, IMAGE_SCALE);
				break;

			case 3:
				triangleImages[imageIndex].draw(position.getX() - imageSizeScaled/2, position.getY() - imageSizeScaled/2, IMAGE_SCALE);
				break;
		}
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
