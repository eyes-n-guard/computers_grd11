import java.util.LinkedList;

public class Node
{
	private int distance;
	private Node prev;
	private LinkedList<Node> next;
	private LinkedList<Integer> nextDist;
	
    public Node()
    {
    	distance = 0;
    	prev = null;
    	next = new <Node>LinkedList();
    	nextDist = new <Integer>LinkedList();
    }
    
    public LinkedList getNextList()
    {
    	return next;
    }
    
    public LinkedList getNextDistList()
    {
    	return nextDist;
    }
    
    public void clearNext()
    {
    	next = new <Node>LinkedList();
    	nextDist = new <Integer>LinkedList();
    }
    
    public void addPath(Node n, int d)
    {
    	addNext(n,d);
    	n.addNext(this,d);
    }
    
    public void addNext(Node n, int d)
    {
    	next.add(n);
    	nextDist.add(new Integer(d));
    }
    
    public Integer[] getNextDistArray()
    {
    	return nextDist.toArray(new Integer[0]);
    }
    
    public Node[] getNextArray()
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