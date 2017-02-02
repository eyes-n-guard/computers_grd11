import java.io.*;

public class MergeSort
{
	int[] array;
	
    public MergeSort()
    {
    	array = new int[10000];
    	for(int i=0;i<array.length;i++)
    		array[i] = (int)(Math.random() * 100);
    		
    		
    	printArray(array);
    	long startTime = System.nanoTime();
    	mergeSort(array);
    	long time = System.nanoTime() - startTime;
    	
    	printArray(array);
    	System.out.println("Execution Time: " + time + " ns");
		//System.out.println("Execution Time: " + Math.round((double)time/1000000000) + "s (" + (double)time/1000000.0 + " ms)");
		
    	
    	
    }
    
    public void printArray(int[] a)
    {
    	for(int i=0;i<a.length;i++)
    		System.out.print(a[i] + " ");
    	System.out.println();
    }
    
    public void mergeSort(int[] a)
    {
    	mergeSort(0,a.length-1);
    }
    
    public void mergeSort(int l,int r)
    {
    	int left = l + (r-l)/2;
    	int right = left+1;
    	
    	if(left-l > 0)
    		mergeSort(l,left);
    	if(r-right > 0)
    		mergeSort(right,r);
    	
    	int[] tempArray = new int[r-l+1];
    	int li = l,ri = right;
    	for(int i=0;i < tempArray.length;i++)
    	{
    		if(ri > r || (li <= left && array[li] < array[ri]))
    		{
    			tempArray[i] = array[li];
    			li++;
    		}
    		else
    		{
    			tempArray[i] = array[ri];
    			ri++;
    		}
    			
    	}
    	
    	for(int i=0;i < tempArray.length;i++)
    		array[i+l] = tempArray[i];
    }
    
    public static void main(String[]args)
    {
    	new MergeSort();
    }
}