import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import java.util.ArrayList;

public class Basis {

    public static final int MAX_NUMBER_BAZELLS = 15;


    private Player ownedBy;
    private ArrayList<Bazell> ownBazells;
    private Vector2f basePosition;

    private float spawnRate = 0.5f;
    private float changingSpawnRate = 0.0f;

    public Basis(Player owner, Vector2f position) {
        this.ownedBy = owner;
        this.ownBazells = new ArrayList<Bazell>();
        this.basePosition = position;
    }

    public void spawnBazels() {

        if (ownBazells.size() <= MAX_NUMBER_BAZELLS) {

            if (changingSpawnRate >= spawnRate) {
                changingSpawnRate = 0.0f;

                Bazell spawnedBazell = new Bazell(ownedBy.getControlerIndex(), basePosition);
                ownBazells.add(spawnedBazell);
            }

        } else {
            System.err.println("Spawned more Bazells than allowed, this should not happen!");
        }

    }

    public void update(int passedTimeMS) {
        for (Bazell bazell : ownBazells) {
            bazell.update(passedTimeMS);
        }
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.orange);
        graphics.draw(new Circle(basePosition.x, basePosition.y, 1.5f));

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
