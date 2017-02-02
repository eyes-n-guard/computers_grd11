import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Minesweeper extends JFrame implements ActionListener
{
	JPanel[][] panelGrid = new JPanel[9][9];
	
    public Minesweeper()
    {
    	setLayout(new GridLayout(panelGrid[0].length, panelGrid.length));
    	for(int y=0;y < panelGrid[0].length;y++)
    		for(int x=0;x < panelGrid.length;x++)
    		{
    			panelGrid[x][y] = new JPanel();
    			panelGrid[x][y].setLayout(new BorderLayout());
    			add(panelGrid[x][y]);
    			
    			JButton button = new JButton(x + " " + y);
    			button.addActionListener(this);
    			panelGrid[x][y].add(button, BorderLayout.CENTER);
    		}
    	
    	setSize(600,600);
    	setTitle("Minesweeper");
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(3);
    	setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e)
    {
    	
    }
    
    public static void main(String[]args)
    {
    	new Minesweeper();
    }
    
    
}