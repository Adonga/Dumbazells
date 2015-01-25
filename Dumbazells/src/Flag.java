import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class Flag {

    public static final float FLAG_SIZE = 0.15f;
    public static final float FLAG_BASE_DEADZONE = 0.8f;

    static final float IMG_SCALE = 0.009f;

    private Basis[] basen;
    private Vector2f position;
    private Bazell carriedBy;

    Image flagImg;

    public Flag(Vector2f position, Basis[] basen) {
        this.basen = basen;
        this.position = position;

        try {
            flagImg = new Image("images/flag.png");
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void update(GameContainer gc, int passedTimeMS) {
        if (carriedBy != null) {
            this.position = carriedBy.getPosition();
        }
    }

    public void render(Graphics graphics) {

        flagImg.draw(getPosition().getX(), getPosition().getY(), IMG_SCALE);
    }

    public void setCarriedBy(Bazell carriedBy) {
        this.carriedBy = carriedBy;
    }

    public Vector2f getPosition() {
        return new Vector2f(position.getX() - flagImg.getWidth() * 0.5f * IMG_SCALE, position.getY() - flagImg.getHeight() * 0.5f * IMG_SCALE);
    }

    public Bazell getCarriedBy() {
        return carriedBy;
    }

    /**
     * Static Stuff
     */

    public static Vector2f randFlagPosition(Basis[] basen) {

        Vector2f pos = new Vector2f((float)Math.random() * (Game.GAME_COORD_SIZE.getX() - (Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE)),
               (float)Math.random() * (Game.GAME_COORD_SIZE.getY() - (Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE)));

        for (Basis basis : basen) {
            Vector2f posCopy = new Vector2f(pos);
            if (posCopy.sub(basis.getPosition()).length() < (Flag.FLAG_BASE_DEADZONE + Basis.BASE_SIZE/2)) {
                pos = new Vector2f((float)Math.random() * (Game.GAME_COORD_SIZE.getX() - (Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE)),
                        (float)Math.random() * (Game.GAME_COORD_SIZE.getY() - (Flag.FLAG_SIZE + Basis.BASE_SIDE_DEADZONE)));
            }
        }

        return pos;
    }
}
