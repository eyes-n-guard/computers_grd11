import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class QuadTreeTest extends JFrame
{
	boolean[][] grid;
	QuadTree tree;
	
	public QuadTreeTest()
	{
		grid = new boolean[8][8];
		tree = new QuadTree();
		
		for(int y=0;y<grid.length;y++)
			for(int x=0;x<grid[0].length;x++)
				grid[x][y] = Math.random() * 2 < 1.0 ? false : true;
		
		
		//printGrid();
		tree.buildTree("grid.txt");
		//printGrid(tree.getArray(8));
		//tree.loadTree("save.txt");
		tree.saveTree("save.txt");
		//tree.buildTree(grid);
		
		setLayout(new BorderLayout());
		add(tree.drawTree(),BorderLayout.CENTER);
		
		setTitle("Quad Tree");
		setSize(600,600);
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	public void printGrid(boolean[][] g)
	{
		for(int y=0;y<g.length;y++)
		{
			for(int x=0;x<g[0].length;x++)
				System.out.print((g[x][y] == true ? 1 : 0) + " ");
			System.out.println();
		}
			
	}
	
	public static void main(String[]args)
	{
		new QuadTreeTest();
	}
}