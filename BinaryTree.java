

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
    	insert(t.getData());
    	if(t.getLeft() != null)
    		reinsert(t.getLeft());
    	if(t.getRight() != null)
    		reinsert(t.getRight());
    }
    
    public void balance()
    {
    	if(root != null)
    	{
	    	LinkedList list = new LinkedList();
	    	loadList(root,list);
	    	root = null;
	    	rebalance(list);
    	}
    	
    }
    
    private void rebalance(LinkedList list)
    {
    	if(list.length() <= 2)
    	{
    		insert(list.getNode(0).getData());
    		if(list.length() == 2)
    			insert(list.getNode(1).getData());
    	}
    	else
    	{
	    	int mid = list.length()/2;
	    	insert(list.getNode(mid).getData());
	    	rebalance(new LinkedList(list.getNode(mid+1)));
	    	list.getNode(mid-1).setNext(null);
	    	rebalance(list);
    	}
    }
    
    private void loadList(TreeNode t,LinkedList l)
    {
    	if(t.getLeft() != null)
    		loadList(t.getLeft(),l);
    	l.insert(t.getData());
    	if(t.getRight() != null)
    		loadList(t.getRight(),l);
    }
    
    
}