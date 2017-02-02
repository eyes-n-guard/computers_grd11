

public class BinaryTree
{
	protected TreeNode root;
	
    public BinaryTree()
    {
    	root = null;
    }
    
    public void insert(int n)
    {
    	if(root != null)
    	{
	    	TreeNode node = root;
	    	TreeNode prev = null;
	    	while(node != null)
	    	{
	    		prev = node;
	    		if(n < node.getData())
	    			node = node.getLeft();
	    		else
	    			node = node.getRight();
	    	}
	    	if(n < prev.getData())
	    		prev.setLeft(new TreeNode(n));
	    	else
	    		prev.setRight(new TreeNode(n));
    	}
    	else
    		root = new TreeNode(n);
    	
    }
    
    public void print()
    {
    	print(root);
    }
    private void print(TreeNode t)
    {
    	if(t.getLeft() != null)
    		print(t.getLeft());
    	System.out.println(t.getData() + "");
    	if(t.getRight() != null)
    		print(t.getRight());
    }
    
    public void delete(int n)
    {
    	TreeNode node = root;
	    TreeNode prev = null;
	    while(node != null && node.getData() != n)
	    {
	    	prev = node;
	    	if(n < node.getData())
	    		node = node.getLeft();
	    	else
	    		node = node.getRight();
	    }
	    if(node != null)
	    {
	    	if(prev != null)
	    	{
		    	if(prev.getLeft() == node)
			    	prev.setLeft(null);
			    else
			    	prev.setRight(null);
	    	}
	    	else
	    		root = null;
		    
		    
		    if(node.getLeft() != null)
		    	reinsert(node.getLeft());
		    if(node.getRight() != null)
		    	reinsert(node.getRight());
	    }
	    node = null;
	    
    }
    
    private void reinsert(TreeNode t)
    {
    	if(t.getLeft() != null)
    		reinsert(t.getLeft());
    	insert(t.getData());
    	if(t.getRight() != null)
    		reinsert(t.getRight());
    }
    
}