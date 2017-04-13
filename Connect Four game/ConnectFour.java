import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ConnectFour extends JFrame implements ActionListener
{
	int turn;
	JPanel gamePanel;
	JLabel turnLabel;
	JPanel [][]guiGrid;
	JButton []buttons;
	int [][]grid;
	
    public ConnectFour()
    {
    	turn = 1; //p1 1, p2 2
    	buttons = new JButton[7]; //[x]
    	gamePanel = new JPanel();
    	turnLabel = new JLabel("P1's turn");
    	guiGrid = new JPanel[7][7];
		grid = new int[7][7]; //[x][y]
		
		add(gamePanel, BorderLayout.CENTER);
		add(turnLabel, BorderLayout.NORTH);
		
		gamePanel.setLayout(new GridLayout(grid[0].length + 1,grid.length));
		
		for(int i=0;i < grid.length;i++)
			for(int j=0;j < grid[0].length;j++)
				grid[i][j] = 0;
		
		for(int y=0;y < grid[0].length;y++)
		{
			buttons[y] = new JButton();
			gamePanel.add(buttons[y]);
			buttons[y].addActionListener(this);
		}
		
		for(int y=0;y < grid[0].length;y++)
			for(int x=0;x < grid.length;x++)
			{
				guiGrid[x][y] = new JPanel();
				guiGrid[x][y].add(new JLabel(x + " " + y), BorderLayout.CENTER);
				gamePanel.add(guiGrid[x][y]);
			}
		
		
		setDefaultCloseOperation(3);
		setSize(600,800);
		setLocationRelativeTo(null);
		setTitle("Connect Four");
		setVisible(true);
		
    }
    
    public int getDist(int dir, int x, int y)
    {
    	if(x < 0 || x >= grid.length || y < 0 || y >= grid[0].length || grid[x][y] != turn)
    		return 0;
    	return getDist(dir,x + (dir >> 2) - 1, y + (dir & 0x03) - 1) + 1; //glhf
    }
    
    public void actionPerformed(ActionEvent e)
    {
    	JButton button = (JButton)e.getSource();
    	int x,y;
    	for(x=0;buttons[x] != button;x++); //find button
    	if(grid[x][0] == 0) //do nothing if full
    	{
    		for(y=grid[0].length-1;grid[x][y] != 0;y--);
    		grid[x][y] = turn;
    		guiGrid[x][y].setBackground(turn == 1 ? Color.red : Color.yellow);
    		if(((getDist(0x09,x,y) + getDist(0x01,x,y) - 1) | (getDist(0x04,x,y) + getDist(0x06,x,y) - 1) | (getDist(0x00,x,y) + getDist(0x0a,x,y) - 1) | (getDist(0x08,x,y) + getDist(0x02,x,y)) - 1) >= 4) //game won
    			turnLabel.setText("P" + turn + " Wins!");
    		else
    		{
    			turn ^= 0x03; //swap turn
    			turnLabel.setText("P" + turn + "'s turn");
    		}
    			
    	}
    }
    
    public static void main(String[]args)
    {
    	new ConnectFour();
    }
    
}