import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Snake extends JFrame
{
	Canvas drawArea;
	int direction;
	GameLoop loop;
	PlayerAdapter keys;
	Timer interval;
	SnakeNode head;
	int addCount, addInterval;
	int score;
	int height, width;
	int prevDir;
	int cx, cy;
	
    public Snake()
    {
    	drawArea = new Canvas();
    	loop = new GameLoop();
    	keys = new PlayerAdapter();
    	interval = new Timer(200, loop);
    	direction = 0;
    	score = 0;
    	height = 300;
    	width = 300;
    	prevDir = 0;
    	cx = 50; 
    	cy = 50;
    	
    	head = new SnakeNode(width/2, height/2);
    	SnakeNode temp = head;
    	for(int i=0;i < 50;i += 10)
    	{
    		temp.setNext(new SnakeNode(head.x, head.y + i + 10));
    		temp = temp.getNext();
    	}
    	
    	add(drawArea, BorderLayout.CENTER);
    	drawArea.addKeyListener(keys);
    	interval.start();
    	
    	setSize(330,350);
    	
    	setResizable(false);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(3);
    	setTitle("Score: 0");
    	setVisible(true);
    }
    
    private class GameLoop implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		if(Math.abs(prevDir - direction) == 2)
    			direction = prevDir;
    		
    		prevDir = direction;
    		if(direction == 0)
    			head = new SnakeNode(head.x, head.y - 10, head);
    		else if(direction == 1)
    			head = new SnakeNode(head.x + 10, head.y, head);
    		else if(direction == 2)
    			head = new SnakeNode(head.x, head.y + 10, head);
    		else
    			head = new SnakeNode(head.x - 10, head.y, head);
    		
    		SnakeNode node = head;
    		SnakeNode last = null;
    		boolean colision = (head.x < 10 || head.y < 10 || head.x > width || head.y > height);
    		while(head != null && node.getNext() != null)
    		{
    			last = node;
    			node = node.getNext();
    			if(node != head && node.x == head.x && node.y == head.y)
    				colision = true;
    		}
    		
    		if(head.x == cx && head.y == cy)
    		{
    			node = head;
    			while(head != null && node.getNext() != null)
    			{
    				node = node.getNext();
    				if((node.x == cx && node.y == cy) || (head.x == cx && head.y == cy))
    				{
	    				cx = ((int)(Math.random() * (float)(width/10))) * 10 + 10;
	    				cy = ((int)(Math.random() * (float)(height/10))) * 10 + 10;
    					node = head;
    				}	
    			}
    			addCount = 0;
    			score++;
    			setTitle("Score: " + score);
    		}
    		else
    			last.setNext(null);
    		
    		addCount++;
    		
    		if(colision)
    		{
    			JOptionPane.showMessageDialog(null,"Game Over" + "\n" + "Score: " + score);
    			interval.stop();
    		}
    		
    		repaint();
    	}
    }
    
    public void paint(Graphics g)
    {
    	g = drawArea.getGraphics();
    	g.setColor(new Color(255,10,10));
    	g.fillRect(0,0, drawArea.getWidth(), drawArea.getHeight());
    	
    	g.setColor(new Color(255,255,255));
    	g.fillRect(10,10,width,height);
    	
    	g.setColor(new Color(60,100,255));
    	g.fillRect(cx,cy,10,10);
    	
    	g.setColor(new Color(0,0,0));
    	SnakeNode node = head;
    	while(head != null && node != null)
    	{
    		g.fillRect(node.x, node.y, 10, 10);
    		node = node.getNext();
    	}
    }
    
    private class PlayerAdapter implements KeyListener
	{
		public void keyTyped(KeyEvent e){}
		public void keyPressed(KeyEvent e)
		{
		    int key = e.getKeyCode();
		    
		    if(key == KeyEvent.VK_UP)
				direction = 0;
		    else if(key == KeyEvent.VK_RIGHT)
				direction = 1;
		    else if(key == KeyEvent.VK_DOWN)
				direction = 2;
		    else if(key == KeyEvent.VK_LEFT)
				direction = 3;
		}
		public void keyReleased(KeyEvent e){}
    }
    
    public class SnakeNode
    {
    	int x, y;
    	private SnakeNode next;
    	
    	public SnakeNode()
    	{
    		x = 0;
    		y = 0;
    		next = null;
    	}
    	
    	public SnakeNode(int a, int b)
    	{
    		x = a;
    		y = b;
    		next = null;
    	}
    	
    	public SnakeNode(int a, int b, SnakeNode n)
    	{
    		x = a;
    		y = b;
    		next = n;
    	}
    	
    	public void setNext(SnakeNode n)
    	{
    		next = n;
    	}
    	
    	public SnakeNode getNext()
    	{
    		return next;
    	}
    }
    
    public static void main(String[]args)
    {
    	new Snake();
    }
}