import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PlayerObject implements KeyListener, Collidable
{
	public int x, y;
	public int vSpeed, hSpeed;
    protected int moveSpeed = 5;
	protected int gravity = 3;
	protected int jumpSpeed = -20;
	protected boolean up, down, left, right;
	private boolean upLast;
	protected ImageIcon sprite = new ImageIcon("yee.jpg");
	
    public PlayerObject()
    {
    	x = 0;
    	y = 0;
    	vSpeed = hSpeed = 0;
    }
    
    public PlayerObject(int a, int b)
    {
    	x = a;
    	y = b;
    	vSpeed = hSpeed = 0;
    }
    
    public PlayerObject(int a, int b, Canvas c)
    {
    	x = a;
    	y = b;
    	c.addKeyListener(this);
    	vSpeed = hSpeed = 0;
    }
    
    public void runLoop()
    {
    	hSpeed = ((right ? 1 : 0) - (left ? 1 : 0)) * moveSpeed;
    	vSpeed = (up && !upLast ? jumpSpeed : vSpeed + gravity);
    	x += hSpeed;
    	y += vSpeed;
    	upLast = up;
    }
    
    public void onCollision(Collidable c)
    {
    	Rectangle bounds = c.getBounds();
    }
    
    public Rectangle getBounds()
    {
    	return new Rectangle(x,y,sprite.getIconWidth(), sprite.getIconHeight());
    }
    
    public void draw(Graphics g)
    {
    	g.drawImage(sprite.getImage(),x,y,null);
    }
    
    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e)
    {
    	int key = e.getKeyCode();
    	
    	if(key == KeyEvent.VK_UP)
    		up = true;
    	else if(key == KeyEvent.VK_DOWN)
    		down = true;
    	else if(key == KeyEvent.VK_LEFT)
    		left = true;
    	else if(key == KeyEvent.VK_RIGHT)
    		right = true;
    }
    public void keyReleased(KeyEvent e)
    {
    	int key = e.getKeyCode();
    	
    	if(key == KeyEvent.VK_UP)
    		up = false;
    	else if(key == KeyEvent.VK_DOWN)
    		down = false;
    	else if(key == KeyEvent.VK_LEFT)
    		left = false;
    	else if(key == KeyEvent.VK_RIGHT)
    		right = false;
    }
}