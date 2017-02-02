import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Tile extends JPanel 
{
	private boolean bomb;
	private JButton button;
	private JLabel label;
	
    public Tile()
    {
    	bomb = false;
    	button = new JButton();
    	setLayout(new BorderLayout());
    	add(button);
    }
    
    public Tile(ActionListener a)
    {
    	bomb = false;
    	button = new JButton();
    	button.addActionListener(a);
    	setLayout(new BorderLayout());
    	add(button);
    }
    
    public void setBomb(boolean b)
    {
    	bomb = b;
    }
    
    public boolean isBomb()
    {
    	return bomb;
    }
    
    public JButton getButton()
    {
    	return button;
    }
    
    public void addActionListener(ActionListener a)
    {
    	button.addActionListener(a);
    }
    
    public void showString(String s)
    {
    	remove(button);
    	invalidate();
    	
    	label = new JLabel(s);
    	add(label);
    	
    	validate();
    	repaint();
    }    
}