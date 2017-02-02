public class LinkedList
{
	Node head;
    public LinkedList()
    {
    	head = null;
    }
    
    public LinkedList(Node n)
    {
    	head = n;
    }
    
    public void insert(int d)
    {
    	Node temp = new Node(d);
    	Node current;
    	Node previous = null;
    	current = head;
    	while(current != null && d > current.getData())
    	{
    		previous = current;
    		current = current.getNext();
    	}
    	
    	temp.setNext(current);
    	if(current == head)
    		head = temp;
    	else
	    	previous.setNext(temp);
    }
    
    public int length()
    {
    	Node current = head;
    	int i = 0;
    	while(current != null)
    	{
    		current = current.getNext();
    		i++;
    	}
    	return i;
    }
    
    public Node getNode(int d)
    {
    	Node current = head;
    	for(int i = 0;i < d && current.getNext() != null;i++)
    		current = current.getNext();
    	return current;
    }
    
    public void print()
    {
    	Node current = head;
    	while(current != null)
    	{
    		System.out.print(current.getData() + " ");
    		current = current.getNext();
    	}
    		
    }
    
    public boolean delete(int d)
    {
    	Node current = head;
    	Node previous = null;
    	boolean deleted = false;
    	while(current != null && d != current.getData())
    	{
    		previous = current;
    		current = current.getNext();
    	}
    	if(current != null)
    	{
    		if(current == head)
    			head = current.getNext();
    		else
    			previous.setNext(current.getNext());
    		deleted = true;
    	}
    	return deleted;
    }
    
}