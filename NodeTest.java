import java.io.*;

public class NodeTest
{
	public static void main(String []args)
	{
		Node node1 = new Node(13);
		Node node2 = new Node();
		node1.setNext(node2);
		node2.setData(42);
		
		System.out.println("" + node1.getNext().getData());
	}    
}