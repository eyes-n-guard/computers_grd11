import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BinaryTreeGrid extends JFrame
{
	BinaryTree tree;
    public BinaryTreeGrid()
    {
    	tree = new BinaryTree();
    	tree.insert(42);
    	tree.insert(60);
//		tree.insert(47);
//		tree.insert(83);
//		tree.insert(50);
//		tree.insert(35);
//		tree.insert(102);
//		tree.insert(104);
//		tree.insert(74);
		
		setLayout(new BorderLayout());
		add(drawTree(tree.root),BorderLayout.CENTER);
		
		setSize(600,800);
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);
		setVisible(true);
    }
    
    public JPanel drawTree(TreeNode t)
    {
    	JPanel panel = new JPanel();
    	if(!t.isLeaf())
    	{
    		panel.setLayout(new GridLayout(2,1));
    		if(t.getRight() == null)
    			panel.add(drawTree(t.getLeft()));
    		else if(t.getLeft() == null)
    			panel.add(drawTree(t.getRight()));
    		else
    		{
    			JPanel branches = new JPanel();
    			branches.setLayout(new GridLayout(1,2));
    			branches.add(drawTree(t.getLeft()));
    			branches.add(drawTree(t.getRight()));
    			panel.add(branches);
    		}
    	}
    	panel.add(new JLabel(t.getData() + ""));
    	return panel;
    }
    
    public static void main(String[]args)
    {
    	new BinaryTreeGrid();
    }
}