import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;

public class Basis {

    public static final int MAX_NUMBER_BAZELLS = 15;
    public static final float BASE_SIZE = 0.6f;
    public static final float BASE_SIDE_DEADZONE = 0.3f;
    public static final int MAX_BAZELLS_IN_BASE = 5;

    public static final float IMAGE_SCALE = 0.005f;

    private Player ownedBy;
    private ArrayList<Bazell> ownBazells;
    private Vector2f basePosition;

    BaseTypes baseType;

    private float changingSpawnRate = 0.0f;

    public Basis(Player owner, Vector2f position, BaseTypes baseType) {
        this.ownedBy = owner;
        this.ownBazells = new ArrayList<Bazell>();
        this.basePosition = position;
        this.baseType = baseType;
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

    public void update(GameContainer gc, CommandMap commandMap, Flag[] flags) {

        changingSpawnRate += 0.003;
        if (changingSpawnRate >= 0.5f) {
            changingSpawnRate = 0.0f;
            this.spawnBazels();
        }

        for (Bazell bazell : ownBazells) {
            bazell.update(commandMap, flags);
        }
    }

    public void render(Graphics graphics) {
        try {
            switch (baseType) {
                case Square:
                    Image baseSquare =  new Image("images/base_square.png");
                    baseSquare.draw(getBasePosition().getX() - baseSquare.getWidth() * 0.5f * IMAGE_SCALE, getBasePosition().getY() - baseSquare.getHeight() * 0.5f * IMAGE_SCALE, IMAGE_SCALE);
                    break;
                case Triange:
                    Image baseTriang =  new Image("images/base_triangle.png");
                    baseTriang.draw(getBasePosition().getX() - baseTriang.getWidth() * 0.5f * IMAGE_SCALE, getBasePosition().getY() - baseTriang.getHeight() * 0.5f * IMAGE_SCALE, IMAGE_SCALE);
                    break;
                case Circle:
                    Image baseCircle =  new Image("images/base_circle.png");
                    baseCircle.draw(getBasePosition().getX() - baseCircle.getWidth() * 0.5f * IMAGE_SCALE, getBasePosition().getY() - baseCircle.getHeight() * 0.5f * IMAGE_SCALE, IMAGE_SCALE);
                    break;
            }
        } catch (SlickException e) {
            e.printStackTrace();
        }

        for (Bazell bazell : ownBazells) {
            bazell.render(graphics);
        }
    }

    public Vector2f getBasePosition() {
        return basePosition;
    }

    public Player getOwnedBy() {
        return ownedBy;
    }

    public ArrayList<Bazell> getOwnBazells() {
        return ownBazells;
    }
}
