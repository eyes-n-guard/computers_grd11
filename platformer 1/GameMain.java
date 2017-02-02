import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JFrame
{
	Canvas drawArea;
	PlayerObject player;
	PlatformObject[] platforms = new PlatformObject[10];
	Timer interval;
	GameLoop loop;
	int level;
	
    public GameMain()
    {
    	//load levels & stuff (loop to run all levels
    	drawArea = new Canvas();
    	player = new PlayerObject(50,50,drawArea);
    	platforms[0] = new PlatformObject(300,300);
    	loop = new GameLoop();
		interval = new Timer(60,loop);
		
		
    	add(drawArea, BorderLayout.CENTER);
    	interval.start();
    	
    	setSize(600,600);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(3);
    	setVisible(true);
    }
    
    private class GameLoop implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		player.runLoop();
    		for(int i=0;i < platforms.length;i++)
    			if(platforms[i] != null && player.isColliding(platforms[i]))
    				player.onCollision(platforms[i]);
    		repaint();
    	}
    }
    
    public void paint(Graphics g)
    {
    	g = drawArea.getGraphics();
    	g.setColor(new Color(255,255,255));
    	g.fillRect(0,0,drawArea.getWidth(),drawArea.getHeight());
    	player.draw(g);
    	platforms[0].draw(g);
    }
    
    private void loadLevel(String str)
    {
  		 //change from this (name, put in file constructer without string
    	//load player coords and platform array
    }
    
    public static void main(String[]args)
    {
    	new GameMain();
    }
    
}