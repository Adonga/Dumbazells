import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;

public class Basis {

    public static final int MAX_NUMBER_BAZELLS = 15;
    public static final float BASE_SIZE = 1.0f;
    public static final float BASE_SIDE_DEADZONE = 0.3f;
    public static final int MAX_BAZELLS_IN_BASE = 5;

    public static final float IMAGE_SCALE = 0.005f;

    private Player ownedBy;
    private ArrayList<Bazell> ownBazells;
    private Vector2f basePosition;

    BaseTypes baseType;

    Image baseSquare;
    Image baseTriang;
    Image baseCircle;
    Image baseRaute;

    private float changingSpawnRate = 0.0f;
    
    private int numFlags = 0;
    
    int GetNumFlags() { return numFlags; }

    public Basis(Player owner, Vector2f position, BaseTypes baseType) {
        this.ownedBy = owner;
        this.ownBazells = new ArrayList<Bazell>();
        this.basePosition = position;
        this.baseType = baseType;

        try {
            baseSquare =  new Image("images/base_square.png");
            baseTriang =  new Image("images/base_triangle.png");
            baseCircle =  new Image("images/base_circle.png");
            baseRaute =  new Image("images/base_raute.png");
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }

    private void spawnBazels() {

        float angle = (float)(Math.random() * (2*Math.PI));
        float radius = (float)(Math.sqrt(Math.random()) * BASE_SIZE);

        Vector2f spawnPos = new Vector2f((float)(this.basePosition.getX() + radius * Math.cos(angle)),
                (float)(this.basePosition.getY() + radius * Math.sin(angle)));
        
        if (ownBazells.size() < MAX_NUMBER_BAZELLS) {
            Bazell spawnedBazell = new Bazell(ownedBy.getControlerIndex(), new Vector2f(spawnPos));
            ownBazells.add(spawnedBazell);
        }
    }

    public void update(GameContainer gc, CommandMap commandMap, Flag[] flags, ArrayList<Bazell> allBazells) {

        changingSpawnRate += 0.003;
        if (changingSpawnRate >= 0.5f) {
            changingSpawnRate = 0.0f;
            this.spawnBazels();
        }
        

        for(int i=0; i<ownBazells.size(); ++i) {
        	if(ownBazells.get(i).NeedsDelete()) {
        		ownBazells.remove(i);
        		--i;
        		if(i+1 >= ownBazells.size()) {
        			break;
        		}
        		continue;
        	}
        	ownBazells.get(i).update(commandMap, flags, this, allBazells);
        }
        
        // count flags
        numFlags = 0;
        for(Flag flag : flags) {
        	if(flag.getPosition().distance(getPosition()) < BASE_SIZE)
        		++numFlags;
        }
    }

    public void render(Graphics graphics) {

        switch (baseType) {
            case Square:
                baseSquare.draw(getPosition().getX() - baseSquare.getWidth() * 0.5f * IMAGE_SCALE,
                        getPosition().getY() - baseSquare.getHeight() * 0.5f * IMAGE_SCALE, IMAGE_SCALE);
                break;

            case Triangle:
                baseTriang.draw(getPosition().getX() - baseTriang.getWidth() * 0.5f * IMAGE_SCALE,
                        getPosition().getY() - baseTriang.getHeight() * 0.5f * IMAGE_SCALE, IMAGE_SCALE);
                break;

            case Circle:
                baseCircle.draw(getPosition().getX() - baseCircle.getWidth() * 0.5f * IMAGE_SCALE,
                        getPosition().getY() - baseCircle.getHeight() * 0.5f * IMAGE_SCALE, IMAGE_SCALE);
                break;

            case Raute:
                baseRaute.draw(getPosition().getX() - baseRaute.getWidth() * 0.5f * IMAGE_SCALE,
                        getPosition().getY() - baseRaute.getHeight() * 0.5f * IMAGE_SCALE, IMAGE_SCALE);
                break;
        }

        for (Bazell bazell : ownBazells) {
            bazell.render(graphics);
        }
    }

    public Vector2f getPosition() {
        return basePosition;
    }

    public Player getOwnedBy() {
        return ownedBy;
    }

    public ArrayList<Bazell> getOwnBazells() {
        return ownBazells;
    }
}
