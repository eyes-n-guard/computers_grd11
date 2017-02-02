
public class QuadNode
{
	private int data;
	private QuadNode NW, NE, SW, SE;
	
    public QuadNode()
    {
    	data = 0;
    	NW = NE = SW = SE = null;
    }
    
    public QuadNode(int d)
    {
    	data = d;
    	NW = NE = SW = SE = null;
    }
    
    public boolean isLeaf()
    {
    	return (NW == null && NE == null && SW == null && SE == null);
    }
    
    public void setData(int d)
    {
    	data = d;
    }
    
    public int getData()
    {
    	return data;
    }
    
    
    public void setNW(QuadNode p)
    {
    	NW = p;
    }
    
    public QuadNode getNW()
    {
    	return NW;
    }
    
    public void setNE(QuadNode p)
    {
    	NE = p;
    }
    
    public QuadNode getNE()
    {
    	return NE;
    }
    
    public void setSW(QuadNode p)
    {
    	SW = p;
    }
    
    public QuadNode getSW()
    {
    	return SW;
    }
    
    public void setSE(QuadNode p)
    {
    	SE = p;
    }
    
    public QuadNode getSE()
    {
    	return SE;
    }
    
}