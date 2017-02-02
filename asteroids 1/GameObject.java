import javax.swing.*;
import java.awt.*;

public class GameObject
{
	public int x, y;
	public float hSpeed, vSpeed;
	private ImageIcon sprite;
	boolean visible;
	
	public GameObject()
	{
		x = y = 0;
		hSpeed = vSpeed = 0;
		sprite = null;
		visible = true;
	}
	
	public void draw(Graphics g)
	{
		if(visible)
			g.drawImage(sprite.getImage(),x,y,null);
	}
	public void setVisible(boolean b)
	{
		visible = b;
	}
	public boolean isVisible()
	{
		return visible;
	}
	
	public void setSprite(ImageIcon i)
	{
		sprite = i;
	}
	public ImageIcon getSprite()
	{
		return sprite;
	}
}