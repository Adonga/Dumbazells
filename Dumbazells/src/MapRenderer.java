import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.lwjgl.opengl.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

class MapRenderer {
    static final int NUM_PARTICLES = 100;
    static final int TEXTURE_SIZE = 256;
    private Image battleGroundParticles[];
    private Image runGroundParticles[];
    private Image takeGroundParticles[];
    private Image voidGroundParticles[];
    private Shader shader;

    class Particle {
        public float lifeTime;  // Number in [0,1]
        public Image sprite;    // ID in own of the arrays
        public Vector2f position;
        public float scale;
        public int type;
       // boolean show;
    }

    private Particle particles[];

    public MapRenderer() {
        battleGroundParticles = new Image[1];
        runGroundParticles = new Image[1];
        takeGroundParticles = new Image[1];
        voidGroundParticles = new Image[1];
        try {
            battleGroundParticles[0] = new Image("images/battleground0.png");
            runGroundParticles[0] = new Image("images/runground0.png");
            takeGroundParticles[0] = new Image("images/takeground0.png");
            voidGroundParticles[0] = new Image("images/voidground0.png");
        } catch (SlickException ex) {
            System.out.println(ex.toString());
        }

        computeMipMaps(battleGroundParticles[0]);
        computeMipMaps(runGroundParticles[0]);
        computeMipMaps(takeGroundParticles[0]);
        computeMipMaps(voidGroundParticles[0]);

        shader = new Shader("shader/symbol.vert", "shader/symbol.frag");
        shader.start();
        shader.setUniform("tex", 1);
        shader.setUniform("backgroundTex", 0);
        shader.end();

        // Allocate a static number of particles
        particles = new Particle[NUM_PARTICLES];
        for(int i=0; i<NUM_PARTICLES; ++i) {
            particles[i] = new Particle();
            particles[i].lifeTime = (float)Math.random();
            particles[i].sprite = voidGroundParticles[0];
            particles[i].type = 3;
            particles[i].position = new Vector2f();
     //       particles[i].show = false;
            randomizePosScale(i);
            assignSprite(i, (CommandType)(0));
        }
    }

    // Spawn and move particles
    public void updateLogic(CommandMap commandMap) {
        for(int i=0; i<NUM_PARTICLES; ++i) {
            // Get commando at the position to choose the right type
            Vector2f offset = new Vector2f(particles[i].scale * 0.5f, particles[i].scale * 0.5f);
            CommandType commandType = commandMap.getCommandAt(offset.add(particles[i].position));
            particles[i].lifeTime -= 0.001f;
            if(particles[i].lifeTime < 0.0f) {
             //   particles[i].show = false;  // Force assignSprite to rebuild
                randomizePosScale(i);
                particles[i].lifeTime = 1.0f;
                assignSprite(i, commandType);
            }
            //assignSprite(i, commandType);
        }
    }

    // Draw the overlays on the background
    public void drawOverlays(Graphics g, Image commandImage) {
        //GL13.glActiveTexture(GL13.GL_TEXTURE1);
        //runGroundParticles[0].bind();
        //TextureImpl.bindNone();
        //commandImage.bind();
        //commandImage.draw(0.0f, 0.0f);
        //g.drawImage(commandImage, 0,0, Game.GAME_COORD_SIZE.x, Game.GAME_COORD_SIZE.y, 0,0, RESOLUTION_X, RESOLUTION_Y);
        //runGroundParticles[0].bind();
        //GL13.glActiveTexture(GL13.GL_TEXTURE0);
        shader.start();
        for(int i=0; i<NUM_PARTICLES; ++i) {
            double fade = Math.sin(particles[i].lifeTime * Math.PI);
            fade = Math.pow(fade, 0.4f);
            shader.setUniform("blur", (1-(float)fade));
            float magicScaleX = 1.0f / commandImage.getTextureWidth();
            float magicScaleY = 1.0f / commandImage.getTextureHeight();
            shader.setUniform("mapScaleOff", particles[i].scale / Game.GAME_COORD_SIZE.x / magicScaleX, particles[i].scale / Game.GAME_COORD_SIZE.y / magicScaleY,
                    particles[i].position.x / Game.GAME_COORD_SIZE.x / magicScaleX, particles[i].position.y / Game.GAME_COORD_SIZE.y / magicScaleY);
            shader.setUniform("type", particles[i].type);

            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            particles[i].sprite.bind();
            GL13.glActiveTexture(GL13.GL_TEXTURE0);

            //commandImage.draw(particles[i].position.x, particles[i].position.y, particles[i].scale/ TEXTURE_SIZE);
            g.drawImage(commandImage, particles[i].position.x, particles[i].position.y,
                    particles[i].position.x + particles[i].scale, particles[i].position.y + particles[i].scale,
                    0, 0, commandImage.getWidth() * magicScaleX, commandImage.getHeight() * magicScaleY);
            //particles[i].sprite.draw(particles[i].position.x, particles[i].position.y, particles[i].scale/ TEXTURE_SIZE);
            //particles[i].sprite.drawEmbedded(particles[i].position.x, particles[i].position.y, particles[i].sprite.getTextureWidth() * particles[i].scale/ TEXTURE_SIZE, particles[i].sprite.getTextureHeight() * particles[i].scale/ TEXTURE_SIZE);
        }
        shader.end();
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        TextureImpl.bindNone();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }

    private void randomizePosScale(int particleID) {
        particles[particleID].position.x = (float)Math.random() * (Game.GAME_COORD_SIZE.x - 1);
        particles[particleID].position.y = (float)Math.random() * (Game.GAME_COORD_SIZE.y - 1);
        particles[particleID].scale = ((float)Math.random() + 1.0f);
    }

    private void computeMipMaps(Image img) {
        img.bind();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        //ARBFramebufferObject.glGenerateMipmap(0);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
    }

    private void assignSprite(int particleID, CommandType commandType) {
        //if(!particles[particleID].show) {
        //    particles[particleID].show = true;
         //   particles[particleID].lifeTime = 1.0f;
        ///}
        if(commandType == CommandType.ATTACK)     { particles[particleID].sprite = battleGroundParticles[0]; particles[particleID].type = 0; }
        else if(commandType == CommandType.RUN)   { particles[particleID].sprite = runGroundParticles[0]; particles[particleID].type = 2; }
        else if(commandType == CommandType.CATCH) { particles[particleID].sprite = takeGroundParticles[0]; particles[particleID].type = 1; }
        else { particles[particleID].sprite = voidGroundParticles[0]; particles[particleID].type = 3; }
    }
}