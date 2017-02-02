import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BinaryTreeGrid2 extends JFrame
{
	BinaryTree tree;
	ButtonClicked onClick;
	JPanel treePanel;
	JTextField input;
	
    public BinaryTreeGrid2()
    {
    	tree = new BinaryTree();
    	onClick = new ButtonClicked();
    	treePanel = new JPanel();
    	input = new JTextField();
    	input.addActionListener(onClick);
    	
    	for(int i = 0;i < 10;i++)
    		tree.insert((int)(Math.random() * 100));
		
		setLayout(new BorderLayout());
		treePanel = drawTree(tree.root);
		add(treePanel,BorderLayout.CENTER);
		add(input,BorderLayout.SOUTH);
		
		setTitle("Binary Tree");
		setSize(600,800);
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);
		setVisible(true);
    }
    
    private class ButtonClicked implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		if(e.getSource() == input)
    		{
    			tree.insert(Integer.parseInt(((JTextField)e.getSource()).getText()));
    			input.setText("");
    		}
    		else
    			tree.delete(Integer.parseInt(((JButton)e.getSource()).getText()));
    		
    		
	    	getContentPane().remove(treePanel);
			getContentPane().invalidate();
			
			treePanel = drawTree(tree.root);
			add(treePanel,BorderLayout.CENTER);
			
			getContentPane().validate();
			getContentPane().repaint();
			
    	}
    }
    
    private JPanel drawTree(TreeNode t)
    {
    	JPanel panel = new JPanel();
    	panel.setBorder(BorderFactory.createLineBorder(Color.black));
    	panel.setLayout(new BorderLayout());
    	
    	if(t != null)
    	{
	    	JButton button = new JButton(t.getData() + "");
	    	button.addActionListener(onClick);
	    	panel.add(button);
	    	
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
    	}
    	
    	return panel;
    }
    
    public static void main(String[]args)
    {
    	new BinaryTreeGrid2();
    }
}