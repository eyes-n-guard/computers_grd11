import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Collection;

public class GameMain extends JFrame implements ActionListener
{
	Canvas drawArea;
	Timer interval;
	
	Collection<GameObject> objects;
	Collection<Script> scripts;
	Collection<Moveable> moveables;
	Collection<Visible> visibles;
	
    public GameMain()
    {
    	drawArea = new Canvas();
    	interval = new Timer(60,this);
    	
    	add(drawArea, BorderLayout.CENTER);
    	interval.start();
    	
    	setSize(600,600);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(3);
    	setVisible(true);
    }
    
    public void paint(Graphics g)
    {
    	g = drawArea.getGraphics();
    	g.setColor(new Color(255,255,255));
    	g.fillRect(0,0,drawArea.getWidth(),drawArea.getHeight());
    	
    }
    
	public void actionPerformed(ActionEvent e)
	{
		moveables.forEach(<moveable>movement);
		
		
		repaint();
		
	}
	
	public void loadLevel()
	{
		JumpyDude temp = new JumpyDude();
		objects.add(temp);
		scripts.add(temp);
		moveables.add(temp);
		visibles.add(temp);
		
	}
    
    public static void main(String[]args)
    {
    	new GameMain();
    }
    
}