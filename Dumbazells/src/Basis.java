import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public class Basis {

    public static final int MAX_NUMBER_BAZELLS = 15;
    public static final float BASE_SIZE = 0.9f;
    public static final float BASE_SIDE_DEADZONE = 0.2f;

    private Player ownedBy;
    private ArrayList<Bazell> ownBazells;
    private Vector2f basePosition;

    private float changingSpawnRate = 0.0f;

    public Basis(Player owner, Vector2f position) {
        this.ownedBy = owner;
        this.ownBazells = new ArrayList<Bazell>();
        this.basePosition = position;
    }

    public void spawnBazels() {

        if (ownBazells.size() < MAX_NUMBER_BAZELLS) {
            Bazell spawnedBazell = new Bazell(ownedBy.getControlerIndex(), new Vector2f(basePosition));
            ownBazells.add(spawnedBazell);
        }

    }

    public void update(GameContainer gc, int passedTimeMS, CommandMap commandMap) {

        changingSpawnRate += 0.003;
        if (changingSpawnRate >= 0.5f) {
            changingSpawnRate = 0.0f;
            this.spawnBazels();
        }

        for (Bazell bazell : ownBazells) {
            bazell.update(passedTimeMS, commandMap);
        }
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.orange);
        graphics.fillOval(basePosition.x - BASE_SIZE/2, basePosition.y - BASE_SIZE/2, BASE_SIZE, BASE_SIZE);

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
