

public class WaterBottle
{
	private int capacity;
	private String contents;
	private boolean open;
	private int amount;
	
    public WaterBottle()
    {
    	capacity = 500;
    	contents = "water";
    	open = false;
    	amount = 0;
    }
    
    public WaterBottle(int a,int m)
    {
    	capacity = m;
    	amount = Math.min(Math.abs(a),capacity);
    	contents = "water";
    	open = false;
    }
    
    public WaterBottle(int a,int m,String c,boolean o)
    {
    	capacity = m;
    	amount = Math.min(Math.abs(a),capacity);
    	contents = c;
    	open = o;
    }
    
    public void fill(int a)
    {
    	if(open)
    		amount = Math.min(amount+Math.abs(a),capacity);
    }
    
    public void fill(int a,String c)
    {
    	if(open)
    		amount = Math.min(amount+Math.abs(a),capacity);
    	contents = c;
    }
    
    public void pour(int a)
    {
    	if(open)
    		amount = Math.max(amount-Math.abs(a),0);
    }
    
    public void dump()
    {
    	if(open)
    		amount = 0;
    }
    
    public boolean isEmpty()
    {
    	boolean empty = false;
    	if(amount == 0)
    		empty = true;
    	return empty;
    }
    
    public boolean isFull()
    {
    	boolean full = false;
    	if(amount == capacity)
    		full = true;
    	return full;
    }
    
    public boolean isOpen()
    {
    	return open;
    }
    
    public int getAmount()
    {
    	return amount;
    }
    
    public int getCapacity()
    {
    	return capacity;
    }
    
    public String getContents()
    {
    	return contents;
    }
    
    public void openLid()
    {
    	open = true;
    }
    
    public void closeLid()
    {
    	open = false;
    }
    
    public void toggleLid()
    {
    	open = !open;
    }
    
}