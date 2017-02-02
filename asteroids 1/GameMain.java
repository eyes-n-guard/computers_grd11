import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Collection;

public class GameMain extends JFrame implements ActionListener
{
	Canvas drawArea;
	Timer interval;
	
    public GameMain()
    {
    	drawArea = new drawArea();
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
    	g.fillRect(0,0,drawArea.getWidth(), drawArea.getHeight());
    	
    	
    }
    
    public void actionPerformed(ActionEvent e)
    {
    	
    }
    
    public static void main(String[]args)
    {
    	new GameMain();
    }
    
}