import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Math;

public class JumpyDude extends GameObject implements Visible, Moveable, Script, KeyListener
{
	boolean left, right;
	int moveSpeed;
	
	public JumpyDude()
	{
		moveSpeed = 5;
		left = right = false;
		setSprite(new ImageIcon("yee.jpg"));
		setGravity(1,0);
	}
	
	public void runScripts()
	{
		System.out.println("script");
	}
	
	public void movement()
	{
		//vSpeed += Math.cos(Math.toRadians(gravityDir)) * gravity;
		//hSpeed += Math.sin(Math.toRadians(gravityDir)) * gravity;
		vSpeed += gravity;
		hSpeed = (right ? moveSpeed : 0) - (left ? moveSpeed : 0);
		
		x += hSpeed;
		y += vSpeed;
		System.out.println("movement");
	}
	
	public void keyTyped(KeyEvent k){}
	public void keyPressed(KeyEvent k)
	{
		int key = k.getKeyCode();
		if(key == KeyEvent.VK_LEFT)
			left = true;
		else if(key == KeyEvent.VK_RIGHT)
			right = true;
		else if(key == KeyEvent.VK_UP)
			vSpeed = -10;
	}
	public void keyReleased(KeyEvent k)
	{
		int key = k.getKeyCode();
		if(key == KeyEvent.VK_LEFT)
			left = false;
		else if(key == KeyEvent.VK_RIGHT)
			right = false;
	}
	
}