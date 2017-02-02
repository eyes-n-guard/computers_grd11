import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GridGUI extends JFrame
{
	JButton[][] buttonGrid;
	JButton convert;
	JPanel gridPanel;
	JPanel treePanel;
	JPanel buttonPanel;
	JTextField fileInput;
	JButton save;
	QuadTree tree;
	ButtonClicked onClick;
	EnterPressed onEnter;
	
    public GridGUI()
    {
    	buttonGrid = new JButton[8][8];
    	convert = new JButton("Convert To Tree");
    	onClick = new ButtonClicked();
    	gridPanel = new JPanel();
    	tree = new QuadTree();
    	treePanel = new JPanel();
    	buttonPanel = new JPanel();
    	fileInput = new JTextField();
    	save = new JButton("Save");
    	onEnter = new EnterPressed();
    	
    	buttonPanel.setLayout(new GridLayout(1,3));
    	buttonPanel.add(convert);
    	buttonPanel.add(save);
    	buttonPanel.add(fileInput);
    	
    	setLayout(new BorderLayout());
    	add(gridPanel,BorderLayout.CENTER);
    	add(buttonPanel,BorderLayout.SOUTH);
    	
    	convert.addActionListener(onClick);
    	save.addActionListener(onClick);
    	fileInput.addActionListener(onEnter);
    	
    	gridPanel.setLayout(new GridLayout(8,8));
    	
    	for(int y=0;y<buttonGrid.length;y++)
    		for(int x=0;x<buttonGrid.length;x++)
    		{
    			buttonGrid[x][y] = new JButton("0");
    			buttonGrid[x][y].addActionListener(onClick);
    			gridPanel.add(buttonGrid[x][y]);
    		}
    	
    	setSize(600,600);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(3);
    	setTitle("Grid GUI");
    	setVisible(true);
    	
    	
    }
    
    private class ButtonClicked implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		JButton button = (JButton)e.getSource();
    		if(button.getText().equals("Convert To Tree"))
    		{
    			boolean[][] grid = new boolean[8][8];
    			
    			for(int y=0;y<grid.length;y++)
		    		for(int x=0;x<grid.length;x++)
		    			grid[x][y] = buttonGrid[x][y].getText().equals("1");
    			
	    		getContentPane().remove(gridPanel);
				getContentPane().invalidate();
				
				tree.buildTree(grid);
				treePanel = tree.drawTree();
				add(treePanel,BorderLayout.CENTER);
				button.setText("Convert To Grid");
				
				getContentPane().validate();
				getContentPane().repaint();
    		}
    		else if(button.getText().equals("Convert To Grid"))
    		{
    			boolean[][] grid = tree.getArray(8);
    			
    			
    			getContentPane().remove(treePanel);
				getContentPane().invalidate();
				
				for(int y=0;y<grid.length;y++)
		    		for(int x=0;x<grid.length;x++)
		    			buttonGrid[x][y].setText((grid[x][y] ? "1" : "0"));
		    	
		    	add(gridPanel,BorderLayout.CENTER);
				button.setText("Convert To Tree");
		    	
				
				getContentPane().validate();
				getContentPane().repaint();
    		}
    		else if(button.getText().equals("Save"))
    			save.setText("Load");
    		else if(button.getText().equals("Load"))
    			save.setText("Save");
    		else
    			button.setText((button.getText().equals("0") ? "1" : "0"));
    	}
    }
    
    private class EnterPressed implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		if(save.getText().equals("Save") && convert.getText().equals("Convert To Grid"))
    			tree.saveTree(fileInput.getText() + ".txt");
    		else if(save.getText().equals("Load"))
    		{
	    		getContentPane().remove(treePanel);
	    		getContentPane().remove(gridPanel);
				getContentPane().invalidate();
				
				tree.loadTree(fileInput.getText() + ".txt");
				treePanel = tree.drawTree();
				add(treePanel,BorderLayout.CENTER);
				convert.setText("Convert To Grid");
				
				getContentPane().validate();
				getContentPane().repaint();
    		}
    	}
    }
    
    public static void main(String[]args)
    {
    	new GridGUI();
    }
    
    
}