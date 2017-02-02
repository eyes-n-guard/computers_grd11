import java.io.*;

public class LinkedListTest
{
	public static void main(String []args)
	{
		LinkedList list = new LinkedList();
		
		list.insert(5);
		list.insert(4);
		list.insert(42);
		list.insert(1);
		list.print();
		System.out.println();
		//if(list.delete(4) == true)
		//	System.out.println("it worked yeeeeeee");
		//else
		//	System.out.println("it not didnt not tont woerk");
		System.out.println("" + list.getNode(2).getData());
		list.print();
	}
}