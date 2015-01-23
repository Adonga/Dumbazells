import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;

public class Basis {

    public static final int MAX_NUMBER_BAZELLS = 15;


    private Player ownedBy;
    private ArrayList<Bazell> ownBazells;
    private Vector2f basePosition;

    private float spawnRate = 0.5f;

    public Basis(Player owner, Vector2f position) {
        this.ownedBy = owner;
        this.ownBazells = new ArrayList<Bazell>();
        this.basePosition = position;
    }

    public void spawnBazels() {

        // TODO: remove first argument (livingtime) !
        Bazell spawnedBazell = new Bazell(0.5f, ownedBy.getControlerIndex(), basePosition);

        if (ownBazells.size() <= MAX_NUMBER_BAZELLS) {
            ownBazells.add(spawnedBazell);
        } else {
            System.err.println("Spawned more Bazells than allowed, this should not happen!");
        }

    }

    public void draw() {

    }

    public Player getOwnedBy() {
        return ownedBy;
    }

    public ArrayList<Bazell> getOwnBazells() {
        return ownBazells;
    }
}
