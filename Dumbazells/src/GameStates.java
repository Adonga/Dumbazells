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
	
	private Controller[] controllers;
	
	private int registeredPlayers =0;
	private int i =0;
	private Image start;
	private Image bg;
	private Image win;
	public GameStates() {
		

//		try {
//			start = new Image("images/Start.png");
//			bg = new Image("images/Background.png");
//			win = new Image("images/Win.png");
//		} catch (SlickException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public int init(Input input) {
		
		int connectedControllers = 0;
		registeredPlayers = 0;

			// Find controllerIndex'th xbox controller
			for (int i = 0; i < Controllers.getControllerCount();i++) {
				if (Controllers.getController(i).getName().toLowerCase().contains("xbox")) {
					++connectedControllers;
				}
			}
			controllers = new Controller[connectedControllers];	
			int gamePadIndex = 0;
			for (int i = 0; i < Controllers.getControllerCount();i++) {
				if (Controllers.getController(i).getName().toLowerCase().contains("xbox")) {
					controllers[gamePadIndex] = Controllers.getController(i);
					gamePadIndex++;
				}
			}
			
			for(int i =0; i<connectedControllers;i++){
				if(controllers[i].isButtonPressed(0))
					registeredPlayers++;
			}
			if(controllers[0].isButtonPressed(7) && registeredPlayers>0){
				registering = false;
				}
		return registeredPlayers;
	}
	
	public void restart(Input input)
	{
		if(controllers[0].isButtonPressed(10)){
			gameend = false;
			gamestart = false;
			registering = true;
		}
	}
	
	public void render(Graphics g){
		if(registering)
		{
			try {
			if(i ==0){
				start = new Image("images/Start.png");
				i++;
			}
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			g.drawString("Press Start" , 0, 0);
			start.draw(0, 0, 0.12f);
			for (int i =0; i < registeredPlayers; i++)
			{
				g.drawLine( 1 + i, 5 + i, 1 + i, 7 + i);
			}
		}
		if(gameend){
				try {
					if(i!=0){
					win = new Image("images/Win.png");
					i=0;	
					}
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		}	
	
	}
