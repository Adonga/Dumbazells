import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class Flag {

    public static final float FLAG_SIZE = 0.15f;
    public static final float FLAG_BASE_DEADZONE = 0.5f;

    private Basis[] basen;
    private Vector2f position;
    private Bazell carriedBy;

    public Flag(Vector2f position, Basis[] basen) {
        this.basen = basen;
        this.position = position;
    }

    private boolean positionValid(Vector2f position) {

        // TODO: Implement
        return true;

    }

    public void update(GameContainer gc, int passedTimeMS) {
        if (carriedBy != null) {
            this.position = carriedBy.getPosition();
        }
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.fillOval(position.x, position.y, FLAG_SIZE, FLAG_SIZE);
    }

    public void setCarriedBy(Bazell carriedBy) {
        this.carriedBy = carriedBy;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Bazell getCarriedBy() {
        return carriedBy;
    }
}
