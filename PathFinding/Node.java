import java.util.LinkedList;

public class Node
{
	private int distance;
	private Node prev;
	private LinkedList<Node> next;
	
    public Node()
    {
    	distance = -1;
    	prev = null;
    	next = new <Node>LinkedList();
    }
    
    public Node(int d)
    {
    	distance = d;
    	prev = null;
    	next = new <Node>LinkedList();
    }
    
    public void clearNext()
    {
    	next = new <Node>LinkedList();
    }
    
    public void addNext(Node n)
    {
    	next.add(n);
    }
    
    public Node[] getNext()
    {
    	return next.toArray(new Node[0]);
    }
    
    public void setDist(int d)
    {
    	distance = d;
    }
    
    public int getDist()
    {
    	return distance;
    }
    
    public void setPrev(Node p)
    {
    	prev = p;
    }
    
    public Node getPrev()
    {
    	return prev;
    }
    
}