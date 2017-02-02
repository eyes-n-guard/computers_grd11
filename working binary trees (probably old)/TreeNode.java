

public class TreeNode
{
	int data;
	TreeNode left,right;
	
	
	public TreeNode()
	{
		data = 0;
	}
	
    public TreeNode(int n)
    {
    	data = n;
    }
    
    public void setData(int n)
    {
    	data = n;
    }
    
    public int getData()
    {
    	return data;
    }
    
    public void setLeft(TreeNode t)
    {
    	left = t;
    }
    
    public void setRight(TreeNode t)
    {
    	right = t;
    }
    
    public TreeNode getLeft()
    {
    	return left;
    }
    
    public TreeNode getRight()
    {
    	return right;
    }
    
    public boolean isLeaf()
    {
    	boolean leaf = false;
    	if(left == null && right == null)
    		leaf = true;
    	return leaf;
    }
    
}