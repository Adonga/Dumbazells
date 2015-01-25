import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.newdawn.slick.*;


public class GameStates {

	
	public boolean registering = true;
	public boolean gamestart;
	public boolean gameend;
	public boolean gameWon;
	
	final float BAZELL_SCALE = 0.025f;

	private Controller[] controllers;
	private boolean[] registerdController;
	
	private int registeredPlayers =0;
	private int i =0;
	
	private Image start;
	private Image gameOver;
	private Image won;
	private Image winner;
	private Image[] bazell = new Image[4];

	Sound mainTheme;
	Sound menuTrack;
	Sound win;
	boolean firstStartMainGame = true;
	boolean firstStartMenu = true;
	boolean winOnce = true;

	public GameStates() {

		try {
			mainTheme = new Sound("sounds/track01.aif");
			menuTrack = new Sound("sounds/menu.aif");
			win = new Sound("sounds/win.aif");

		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	public int init(Input input) {
		try {
			bazell[0]= new Image("images/Winner/Circle.png");
			bazell[1]= new Image("images/Winner/Raute.png");
			bazell[2]= new Image("images/Winner/Square.png");
			bazell[3]= new Image("images/Winner/Triangle.png");

		} catch (SlickException e) {
			e.printStackTrace();
		}

		int connectedControllers = 0;
//		registeredPlayers = 0;

		// Find controllerIndex'th xbox controller
		for (int i = 0; i < Controllers.getControllerCount();i++) {
			if (Controllers.getController(i).getName().toLowerCase().contains("xbox")) {
				++connectedControllers;
			}
		}
		controllers = new Controller[connectedControllers];
		registerdController = new boolean[connectedControllers];
		int gamePadIndex = 0;
		for (int i = 0; i < Controllers.getControllerCount();i++) {
			if (Controllers.getController(i).getName().toLowerCase().contains("xbox")) {
				controllers[gamePadIndex] = Controllers.getController(i);
				gamePadIndex++;
			}
		}

		for(int i =0; i<connectedControllers;i++){
			if(controllers[i].isButtonPressed(0) && registeredPlayers<connectedControllers && registerdController[i]==false){
				registerdController[i] = true;
				registeredPlayers++;
				}
			else if (controllers[i].isButtonPressed(1) && registeredPlayers>0){
				registerdController[i] = false;
				registeredPlayers--;
				}
			}
		if(controllers.length>0)
		{
			for (Controller controller : controllers) {
				if (controller.isButtonPressed(7) && registeredPlayers > 0) {
					registering = false;
//					gameend = true; // for insta gameover
					gamestart = true;
				}
			}
		}

		if(input.isKeyDown(Input.KEY_0) ) {
			registering = false;
			if(registeredPlayers<4)
				++registeredPlayers;
			gamestart = true;
		}

		if ((gameend || gameWon || registering) && firstStartMenu) {
			mainTheme.stop();
			menuTrack.loop();
			firstStartMenu = false;
		}

		if (gamestart) {
			if (firstStartMainGame) {
				menuTrack.stop();
				mainTheme.loop();
			}
		}
		return registeredPlayers;
	}
	

	public void restart(Input input){
		for(int i =0; i<controllers.length; i++){
			if(controllers[i].isButtonPressed(6)){
				if(gameend){}
				
				gameend = false;
				gamestart = false;
				gameWon =false;
				registering = true;
				firstStartMenu = true;
				winOnce = true;
			}
		}
		if(input.isKeyDown(Input.KEY_SPACE)){
			
			gameend = false;
			gamestart = false;
			gameWon =false;
			registering = true;
			firstStartMenu = true;
			winOnce = true;
		}
	}
	
	public void playerHasWon(Basis playersBase){
		gameWon = true;
		gameend = true;
		try {
			System.out.println(playersBase.baseType);
			winner = new Image("images/Winner/"+playersBase.baseType+".png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g){
		if(registering)
		{
			if(i ==0){
				try {
					start = new Image("images/Start.png");
					i++;
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
			start.draw(0, 0, 0.0158f);
			for (int i =0; i < registeredPlayers; i++)
			{
//				g.fillRect(4+2*i, 7, 1, 1);
				bazell[i].draw(4+2*i, 6.5f, BAZELL_SCALE * 0.4f);
				
			}
		}
		else if (gameend && !gameWon)
		{
			firstStartMainGame = true;
			mainTheme.stop();
			firstStartMenu = true;
			if(i!=0){
				try {
					gameOver = new Image("images/GameOver.png");
					i=0;
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
			gameOver.draw(0, 0, 0.0156f);
		}
		else if(gameend && gameWon){
			if (winOnce) {
				winOnce = false;
				win.play();
			}
			firstStartMainGame = true;
			mainTheme.stop();
			firstStartMenu = true;
			if(i!=0){
				try {
					won = new Image("images/Win.png");
					i=0;
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
			won.draw( 0, 0, 0.016f);
			winner.draw(8-winner.getWidth()*0.5f*BAZELL_SCALE,0,BAZELL_SCALE);
		}	
	}
	
	}
