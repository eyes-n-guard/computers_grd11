import java.io.*;
import java.util.LinkedList;

public class Dijkstra //currently broken pls fix me
{
	
    public Dijkstra()
    {
    	Node start = new Node();
    	Node a = new Node();
    	Node b = new Node();
    	Node c = new Node();
    	Node d = new Node();
    	Node e = new Node();
    	Node f = new Node();
    	Node end = new Node();
    	
    	start.addNext(a,3);
    	start.addNext(b,2);
    	start.addNext(d,4);
    	a.addNext(e,7);
    	b.addNext(c,2);
    	b.addNext(f,1);
    	d.addNext(f,5);
    	d.addNext(b,1);
    	e.addNext(end,6);
    	f.addNext(end,3);
    	
    	findPath(start, end);
    	Node[] path = listPath(start, end);
    	
    	for(int i=0;i < path.length;i++)
    		if(path[i] == start)
    			System.out.println("start"); //kill me pls
    		else if(path[i] == a)
    			System.out.println("a");
    		else if(path[i] == b)
    			System.out.println("b");
    		else if(path[i] == c)
    			System.out.println("c");
    		else if(path[i] == d)
    			System.out.println("d");
    		else if(path[i] == e)
    			System.out.println("e");
    		else if(path[i] == f)
    			System.out.println("f");
    		else if(path[i] == end)
    			System.out.println("end");
    }
    
    public Node[] listPath(Node head, Node end)
    {
    	LinkedList<Node> nodeList = new <Node>LinkedList();
    	Node current = end;
    	while(current != head)
    	{
    		nodeList.addFirst(current);
    		current = current.getPrev();
    	}
    	nodeList.addFirst(head);
    	return nodeList.toArray(new Node[0]);
    }
    
    public void findPath(Node head, Node end)
    {
    	LinkedList<Node> unvisited = new <Node>LinkedList();
    	listNodes(unvisited, head);
    	
    	Node shortest = null;
    	while(shortest != end)
    	{
    		//find shortest
    		if(unvisited.contains(head))
    			shortest = head;
    		else
    		{
				Node[] nodeArray = unvisited.toArray(new Node[0]);
	    		int sDist = nodeArray[0].getDist();
	    		shortest = nodeArray[0];
	    		for(int i=1;i < nodeArray.length;i++)
	    			if(nodeArray[i].getDist() != 0 && nodeArray[i].getDist() > sDist)
	    			{
	    				sDist = nodeArray[i].getDist();
	    				shortest = nodeArray[i];
	    			}
    		}
    		
    		if(shortest != end)
    		{
    			Node[] nextList = shortest.getNextArray();
    			Integer[] nextDist = shortest.getNextDistArray();
    			for(int i=0;i < nextList.length;i++)
    			{
    				if(nextList[i].getDist() == 0 || shortest.getDist() + nextDist[i].intValue() < nextList[i].getDist())
    				{
    					nextList[i].setDist(shortest.getDist() + nextDist[i].intValue());
    					nextList[i].setPrev(shortest);
    				}
    			}
    		}
    		unvisited.remove(shortest);
    	}
    }
    
    public void listNodes(LinkedList <Node>list, Node n)
    {
    	if(!list.contains(n))
    	{
    		list.add(n);
    		Node[] nextList = n.getNextArray();
    		for(int i=0;i < nextList.length;i++)
    			listNodes(list, nextList[i]);
    	}
    }
    
    public static void main(String[]args)
    {
    	new Dijkstra();
    }
    
}