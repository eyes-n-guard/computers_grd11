import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.io.Reader.*;


public class QuadTree
{
	private QuadNode root;
	private String writeS;
	private int readI;

    public QuadTree()
    {
    	root = null;
    }
    
    public void loadTree(String file)
    {
    	try
    	{
    		readI = 0;
    		BufferedReader reader = new BufferedReader(new FileReader(file));
    		writeS = reader.readLine();
    		reader.close();
    		root = readFile();
    	}
    	catch(Exception ex){}
    }
    
    private QuadNode readFile()
    {
    	QuadNode node = new QuadNode(Integer.parseInt("" + writeS.charAt(readI)));
    	readI++;
    	if(node.getData() == 2)
    	{
	    	node.setNW(readFile());
	    	node.setNE(readFile());
	    	node.setSW(readFile());
	    	node.setSE(readFile());
    	}
    	return node;
    }
    
    public void saveTree(String file)
    {
    	try
    	{
    		if(root != null)
    		{
    			writeS = "";
    			buildFile(root);
    			
    			FileWriter writer = new FileWriter(file);
    			writer.write(writeS);
    			writer.close();
    		}
    	}
    	catch(Exception ex){}
    	
    }
    
    private void buildFile(QuadNode node)
    {
    	writeS += node.getData();
    	if(node.getData() == 2)
    	{
    		buildFile(node.getNW());
    		buildFile(node.getNE());
    		buildFile(node.getSW());
    		buildFile(node.getSE());
    	}
    }
    
    public boolean[][] getArray(int l)
    {
    	boolean[][] grid = new boolean[l][l];
    	if(root != null)
    		buildArray(0,0,l,root,grid);
    	return grid;
    }
    
    private void buildArray(int x, int y, int l, QuadNode node, boolean[][] grid)
    {
    	if(node.isLeaf())
    		for(int i=y;i<y+l;i++)
    			for(int j=x;j<x+l;j++)
    				grid[j][i] = (node.getData() == 1 ? true : false);
    		
    	else
    	{
	    	buildArray(x, y, l/2, node.getNW(), grid);
			buildArray(x + l/2, y, l/2, node.getNE(), grid);
			buildArray(x, y + l/2 , l/2, node.getSW(), grid);
			buildArray(x + l/2, y + l/2, l/2, node.getSE(), grid);
    	}
    }
    
    public void buildTree(String file)
    {
    	try
    	{
	    	BufferedReader reader = new BufferedReader(new FileReader(file));
	    	
	    	String inputLine = reader.readLine();
	    	boolean[][] grid = new boolean[inputLine.length()][inputLine.length()];
	    	
	    	for(int y=0;inputLine != null;y++)
	    	{
	    		for(int x=0;x < inputLine.length();x++)
	    			grid[x][y] = (inputLine.charAt(x) == '1' ? true : false);
	    		inputLine = reader.readLine();
	    		
	    	}
	    	buildTree(grid);
	    	reader.close();
    	}
    	catch(Exception e)
    	{
    		System.out.println("error");
    	}
    	
    }
    
    public void buildTree(boolean[][] grid)
    {
    	root = buildTree(0,0,grid.length,grid);
    }
    
    private QuadNode buildTree(int x, int y, int l, boolean[][] grid)
    {
    	if(l <= 1)
    		return new QuadNode(grid[x][y] ? 1 : 0);
    		

		QuadNode node = new QuadNode(2);
		
		node.setNW(buildTree(x, y, l/2, grid));
		node.setNE(buildTree(x + l/2, y, l/2, grid));
		node.setSW(buildTree(x, y + l/2 , l/2, grid));
		node.setSE(buildTree(x + l/2, y + l/2, l/2, grid));
		
		if(node.getNW().getData() == node.getNE().getData() && node.getSW().getData() == node.getSE().getData() && node.getNW().getData() == node.getSW().getData() && node.getNW().getData() != 2)
			return new QuadNode(node.getNW().getData());
		
		return node;
    }
    
    public JPanel drawTree()
    {
    	if(root != null)
    		return drawTree(root);
    	return new JPanel();
    }
    
    private JPanel drawTree(QuadNode n)
    {
    	JPanel panel = new JPanel();
    	panel.setBorder(BorderFactory.createLineBorder(new Color((int)(Math.random()*255),(int)(Math.random() * 255),(int)(Math.random() * 255))));
    	
    	if(n.isLeaf())
    	{
    		panel.setLayout(new BorderLayout());
    		panel.add(new JLabel("" + n.getData(),JLabel.CENTER));
    	}
    	else
    	{
    		panel.setLayout(new GridLayout(2,2));
    		panel.add(drawTree(n.getNW()));
    		panel.add(drawTree(n.getNE()));
    		panel.add(drawTree(n.getSW()));
    		panel.add(drawTree(n.getSE()));
    	}
    		
    	return panel;
    }
    
    
    public QuadNode getRoot()
    {
    	return root;
    }
}