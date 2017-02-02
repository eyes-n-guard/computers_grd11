import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Minesweeper extends JFrame
{
	Tile[][] tiles = new Tile[16][16]; //[x][y]
	TileClicked click;
	
    public Minesweeper()
    {
    	click = new TileClicked();
    	setLayout(new GridLayout(tiles[0].length,tiles.length));
    	
    	for(int y=0;y < tiles[0].length;y++)
    		for(int x=0;x < tiles.length;x++)
    		{
    			tiles[x][y] = new Tile(click);
    			add(tiles[x][y]);
    		}
    			
    	
    	setSize(500,500);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(3);
    	setTitle("Minesweeper");
    	setVisible(true);
    }
    
    private class TileClicked implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		JButton button = (JButton)e.getSource();
    		int x = tiles.length,y = 0;
    		
    		for(y=0;x >= tiles.length; y++)
    		{
    			for(x=0;x < tiles.length && tiles[x][y].getButton() != button; x++)
    				System.out.println(x + " " + y);
    		}
    		y--;
    			
    		
    		tiles[x][y].showString(x + " " + y);
    	}
    }
    
    public static void main(String[]args)
    {
    	new Minesweeper();
    }
}