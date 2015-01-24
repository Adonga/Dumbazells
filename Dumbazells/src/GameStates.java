import javax.sound.sampled.Line;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


public class GameStates {

	
	public boolean registering = true;
	public boolean gamestart;
	public boolean gameend;
	public boolean gameWon;
	
	private Controller[] controllers;
	private boolean[] registerdController;
	
	private int registeredPlayers =0;
	private int i =0;
	
	private Image start;
	private Image bg;
	private Image gameOver;
	private Image won;
	private Image winner;
	
	
	public GameStates() {
		
	}

	public int init(Input input) {
		
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
				for(int i =0; i<controllers.length; i++){
					if(controllers[i].isButtonPressed(7) && registeredPlayers>0){
						registering = false;
					}
				}
			}
			
			if(input.isKeyDown(Input.KEY_0)) {
				registering = false;
				return ++registeredPlayers;
			}
		return registeredPlayers;
	}
	

	public void restart(){
		for(int i =0; i<controllers.length; i++){
			if(controllers[i].isButtonPressed(6)){
				if(gameend){}
				
				gameend = false;
				gamestart = false;
				gameWon =false;
				registering = true;
			}
		}
	}
	
	public void playerHasWon(Basis playersBase){
		gameWon = true;
		gameend = true;
		try {
			System.out.println(playersBase.baseType);
			winner = new Image("images/Winner/"+playersBase.baseType+".png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
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
				g.fillRect(4+2*i, 7, 1, 1);
			}
		}
		else if (gameend && !gameWon)
		{
			if(i!=0){
				try {
					gameOver = new Image("images/GameOver.png");
					i=0;
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
			gameOver.draw(0, 0, 0.016f);
		}
		else if(gameend && gameWon){
			if(i!=0){
				try {
					won = new Image("images/Win.png");
					i=0;
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
			float SCALE = 0.025f;
			won.draw( 0, 0, 0.016f);
			winner.draw(8-winner.getWidth()*0.5f*SCALE,0,SCALE);
		}	
	}
	
	}
