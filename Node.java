public class Node
{
	private int data;
	private Node next;
	
    public Node()
    {
    	data = 0;
    }
    
    public Node(int d)
    {
    	data = d;
    }
    
    public void setData(int d)
    {
    	data = d;
    }
    
    public int getData()
    {
    	return data;
    }
    
    public void setNext(Node n)
    {
    	next = n;
    }
    
    public Node getNext()
    {
    	return next;
    }
}