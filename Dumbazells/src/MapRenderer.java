import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.lwjgl.opengl.*;
import org.newdawn.slick.geom.Vector2f;

class MapRenderer {
    static final int NUM_PARTICLES = 40;
    private Image battleGroundParticles[];
    private Shader shader;

    class Particle {
        public float lifeTime;  // Number in [0,1]
        public int spriteID;    // ID in own of the arrays
        public int type;        // 0 == battleGround
        public Vector2f position;
        public float scale;
    }

    private Particle particles[];

    public MapRenderer() {
        battleGroundParticles = new Image[1];
        try {
            battleGroundParticles[0] = new Image("images/battleground0.png");
        } catch (SlickException ex) {
            System.out.println(ex.toString());
        }

        battleGroundParticles[0].bind();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        //ARBFramebufferObject.glGenerateMipmap(0);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        shader = new Shader("shader/symbol.vert", "shader/symbol.frag");

        // Allocate a static number of particles
        particles = new Particle[NUM_PARTICLES];
        for(int i=0; i<NUM_PARTICLES; ++i) {
            particles[i] = new Particle();
            particles[i].lifeTime = (float)Math.random();
            particles[i].spriteID = 0;
            particles[i].type = 0;
            particles[i].position = new Vector2f();
            randomizePosScale(i);
        }
    }

    // Spawn and move particles
    public void updateLogic(CommandMap commandMap) {
        for(int i=0; i<NUM_PARTICLES; ++i) {
            particles[i].lifeTime -= 0.001f;
            if(particles[i].lifeTime < 0.0f) {
                particles[i].lifeTime = 1.0f;
                randomizePosScale(i);
            }
        }
    }

    // Draw the overlays on the background
    public void drawOverlays(Graphics g) {
        shader.start();
        for(int i=0; i<NUM_PARTICLES; ++i) {
            double fade = Math.sin(particles[i].lifeTime * Math.PI);
            fade = Math.pow(fade, 0.4f);
            shader.setUniform("blur", (1-(float)fade));

            battleGroundParticles[0].draw(particles[i].position.x, particles[i].position.y, particles[i].scale / battleGroundParticles[0].getWidth());
        }
        shader.end();
        //battleGroundParticles[0].draw(1.0f, 1.0f);

        //battleGroundParticles[0].draw(5.0f, 5.0f, 3.0f / battleGroundParticles[0].getWidth());
    }

    private void randomizePosScale(int particleID) {
        particles[particleID].position.x = (float)Math.random() * (Game.GAME_COORD_SIZE.x - 1);
        particles[particleID].position.y = (float)Math.random() * (Game.GAME_COORD_SIZE.y - 1);
        particles[particleID].scale = (float)Math.random() + 1.0f;
    }
}