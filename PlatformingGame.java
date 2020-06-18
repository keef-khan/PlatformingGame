//Created by Aakef Khan © 2020

import javax.swing.*;   // Import everything from swing package
import java.awt.*;		// Import everything from awt package
import java.awt.geom.Ellipse2D;
import java.awt.event.*; // for key listener
import java.io.*;
import java.awt.Image;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class PlatformingGame extends JPanel implements KeyListener, Runnable
{
	//window elements
	private JFrame frame;
	private Font f;
	private Color color;
	private static final int frameWidth = 1000;  // Set Dimension of full frame
	private static final int frameHeight = 800;
	private Thread t;

	//key stuff
	private boolean left, right, up, down, jump, win, lose, gameOn;

	//items
	private String msg1, msg2;
	private Shape ground;
	private BufferedImage character = null;
	private int charX, charY;
	
	private BufferedImage enemy1 = null;
	private BufferedImage enemy2 = null;
	private int enemy1x, enemy1y, enemy2x, enemy2y;
	private boolean enemy1Dead, enemy2Dead;
	
	private BufferedImage goal = null;
	private int goalX, goalY;
	
	//jumping stuff
	private int jumpHeight, jumpProgress;
	private boolean jumping;
	private double vel;
	private double gravity;
	
	//other
	private boolean stop;
	
	



	public PlatformingGame()
	{
		//window elements
		frame=new JFrame("PlatformingGame");
		f=new Font("MONACO",Font.PLAIN,25);  // Set Font  https://docs.oracle.com/javase/7/docs/api/java/awt/Font.html
		frame.add(this);  //Add Object to Frame, this will invoke the paintComponent method
		frame.addKeyListener(this);
		frame.setSize(frameWidth,frameHeight);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		t=new Thread(this);  // The thread
		t.start();

		//key stuff
		right = false;
		left = false;
		up = false;
		down = false;
		jump = false;
		win = false;
		lose = false;
		gameOn = true;

		//gravity stuff
		vel = 0.0;
		gravity = 0.5;

		//items
		msg1 = "Use left and right keys to move. Use space to jump."; 
		msg2 = "Avoid the enemies and press the button.";
		charX = 40;
		charY = 562;
		enemy1x = 300;
		enemy1y = 568;
		enemy2x = 600;
		enemy2y = 568;
		goalX = 900;
		goalY = 568;

		
		try
		{
    		character = ImageIO.read( new File("mario.png" ));
    		enemy1 = ImageIO.read(new File("goomba.png"));
    		enemy2 = ImageIO.read(new File("goomba.png"));
    		goal = ImageIO.read(new File("pButton.png"));
		}
		catch ( IOException exc )
		{
		    //TODO: Handle exception.
		}
		
		stop = false;
		enemy1Dead = false;
		enemy2Dead = false;
	}

	public void paintComponent(Graphics g)  // Need to have this method to complete the painting
	{
		super.paintComponent(g);  // Need to call this first to initialize in parent class, do not change
		Graphics2D g2d = (Graphics2D)g;  // Cast to Graphics2D which is a subclass of Graphics with additional properties


		//background
		g2d.setPaint(new Color(0,191,255));//sky blue//
		g2d.fillRect(0,0,frameWidth,frameHeight);

		//title
		g2d.setColor(Color.RED);
		g2d.setFont(f);
		g2d.drawString(msg1,50,50);
		g2d.drawString(msg2,50,75);
 

		g2d.setColor(new Color(135,61,29));//brown//
		g2d.fillRect(0, 600, 1000, 200);//ground

		g2d.drawImage(character.getScaledInstance(30,45,Image.SCALE_DEFAULT), charX, charY, this);
		if (enemy1Dead==false)
			g2d.drawImage(enemy1.getScaledInstance(42,40,Image.SCALE_DEFAULT), enemy1x, enemy1y, this);
		if (enemy2Dead==false)
			g2d.drawImage(enemy2.getScaledInstance(42,40,Image.SCALE_DEFAULT), enemy2x, enemy2y, this);
		if (win==false)
			g2d.drawImage(goal.getScaledInstance(42,40,Image.SCALE_DEFAULT), goalX, goalY, this);


	}


	public void run()
	{
		
		while(gameOn){
		
			//key stuff
			if (right)
			{
				charX+=2;
			}
			if(left)
			{
				charX-=2;
			}
			if(jump)
			{

			}
			updateGravity();
			//enemy movement
			
			if (stop==false)
			{
				if (enemy1x>charX)
					enemy1x--;
				if (enemy1x<charX)
					enemy1x++;
				if (enemy2x>charX)
					enemy2x--;
				if (enemy2x<charX)
					enemy2x++;
				stop = true;
			}
			else
				stop = false;
				
				
			
			//win/lose stuff
			if (win || lose)
			{
				gameOn = false;
				if (win)
				{
					f=new Font("MONACO",Font.PLAIN,50);
					msg1 = "YOU WIN";
				}
				if (lose)
				{
					f=new Font("MONACO",Font.PLAIN,50);
					msg1 = "YOU LOSE";
				}
				msg2 = "";
			}
			
				
			//collision
			
			//enemy 1
			if (enemy1Dead==false)
				if( (charX + 30 >= enemy1x && charX <= enemy1x + 42) && // check horizontal collision
				(charY + 45 >= enemy1y && charY <= enemy1y + 40)) // and check vertical collision
				{
  					if (vel > 0) // player is falling
  					{
   						 enemy1Dead = true;
   						 vel = -10;
   						 charY = 562;
  					}
  					else
  					{
   	 					lose = true;
  					}
				}
			//enemy 2
			if (enemy2Dead==false)
				if( (charX + 30 >= enemy2x && charX <= enemy2x + 42) && // check horizontal collision
				(charY + 45 >= enemy2y && charY <= enemy2y + 40)) // and check vertical collision
				{
  					if (vel > 0) // player is falling
  					{
   						enemy2Dead = true;
   						vel = -10;
   						charY = 562;
  					}
  					else
  					{
   	 					lose = true;
  					}
				}
			
			if( (charX + 30 >= goalX && charX <= goalX + 42) && // check horizontal collision
			(charY + 45 >= goalY && charY <= goalY + 40)) // and check vertical collision
			{
  				if (vel > 0) // player is falling
  				{
   					win = true;
  				}
  				else
  				{
    				
  				}
			}
					
							
			//refresh stuff
			try
			{
				t.sleep(5);  //5 milliseconds
			}catch(InterruptedException e)
			{
			}
			repaint();
		}

	}
	
	
	public void updateGravity() // to be called in main loop; id est, every frame
	{
	    if (jumping)
    	{
        	vel+=gravity;
        	charY+=vel;
    	}
    	if ((charY+45)>=600)
    	{
        	jumping = false;
        	vel=0.0;
    	}

	}

	public void keyPressed(KeyEvent ke)
	{

		if(ke.getKeyCode()==39)
            right=true;
        if(ke.getKeyCode()==37)
        left=true;
	    if (ke.getKeyCode()==32 && jumping == false)
	    {
	    	jump = true;
	        jumping = true;
    	    vel=-15.0;
    	}
	}
	public void keyReleased(KeyEvent ke)
	{
		if(ke.getKeyCode()==32)
			jump=false;
		if(ke.getKeyCode()==39)
			right=false;
		if(ke.getKeyCode()==37)
			left=false;
	}


	public void keyTyped(KeyEvent ke)
	{
		// This method is included because it is required by the KeyListener Interface
		// it is not used for basic movement but can be used to get actual input
		// show the keyboard such as typing name.

	}

	public static void main(String[]args)
	{
		PlatformingGame game = new PlatformingGame();
	}
}