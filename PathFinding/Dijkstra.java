import java.io.*;
import java.util.LinkedList;

public class Dijkstra
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
    	Node g = new Node();
    	Node h = new Node();
    	
    	start.addPath(a,4);
    	start.addPath(g,8);
    	a.addPath(b,8);
    	a.addPath(g,11);
    	g.addPath(h,7);
    	g.addPath(f,1);
    	b.addPath(h,2);
    	h.addPath(f,6);
    	b.addPath(c,7);
    	f.addPath(e,2);
    	c.addPath(d,9);
    	e.addPath(d,10);
    	
//    	start.addNext(a,8);
//    	start.addNext(b,4);
//    	start.addNext(c,3);
//    	a.addNext(d,6);
//    	a.addNext(start,8);
//    	b.addNext(g,2);
//    	b.addNext(start,4);
//    	c.addNext(g,7);
//    	c.addNext(start,3);
//    	d.addNext(e,3);
//    	d.addNext(a,6);
//    	e.addNext(f,7);
//    	e.addNext(end,1);
//    	e.addNext(d,3);
//    	f.addNext(end,10);
//    	f.addNext(e,7);
//    	f.addNext(g,4);
//    	g.addNext(f,4);
//    	g.addNext(c,7);
//    	g.addNext(b,2);
    	
    	findPath(c, g);
    	Node[] path = listPath(c, g);
    	
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
    		else if(path[i] == g)
    			System.out.println("g");
    		else if(path[i] == h)
    			System.out.println("h");
    			
    	System.out.println(g.getDist() + "");
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
    	
    	Node shortest = head;
    	while(shortest != end)
    	{
    		//find shortest
    		if(!unvisited.contains(head))
    		{
				Node[] nodeArray = unvisited.toArray(new Node[0]);
	    		int sDist = nodeArray[0].getDist();
	    		shortest = nodeArray[0];
	    		for(int i=1;i < nodeArray.length;i++)
	    			if(nodeArray[i].getDist() != 0 && (nodeArray[i].getDist() < sDist || sDist == 0))
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
    				if((nextList[i].getDist() == 0 || shortest.getDist() + nextDist[i].intValue() < nextList[i].getDist()) && unvisited.contains(nextList[i]))
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