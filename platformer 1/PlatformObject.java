import java.awt.*;
import javax.swing.*;

public class PlatformObject implements Collidable
{
	public int x, y;
	protected ImageIcon sprite = new ImageIcon("yee2.jpg");
	
    public PlatformObject()
    {
    	x = 0;
    	y = 0;
    }
    
    public PlatformObject(int a, int b)
    {
    	x = a;
    	y = b;
    }
    
    public Rectangle getBounds()
    {
    	return new Rectangle(x,y,sprite.getIconWidth(), sprite.getIconHeight());
    }
    
    public void draw(Graphics g)
    {
    	g.drawImage(sprite.getImage(),x,y,null);
    }
    
    
}