import java.io.*;

public class QuickSort
{
	int[] array;

    public QuickSort()
    {
    	array = new int[10000];
    	
    	for(int i = 0;i < array.length;i++)
    		array[i] = (int)(Math.random()*101 + 1);
    		
    	printArray(array);
    	long startTime = System.nanoTime();
    	quickSort(array);
    	long time = System.nanoTime() - startTime;
    	
    	printArray(array);
    	System.out.println("Execution Time: " + time + " ns");
		//System.out.println("Execution Time: " + Math.round((double)time/1000000000) + "s (" + (double)time/1000000.0 + " ms)");
    	
    }
    
    public void quickSort(int[] a)
    {
    	quickSort(0,a.length-1);
    }
    
    public void quickSort(int l,int r)
    {

		int a=l,b=r;
    	int p = l;
    	while(p != a || p != b)
    	{
    		while(array[p] <= array[b] && p != b)
    			b--;
    		swap(p,b,array);
    		p = b;
    		while(array[p] >= array[a] && p != a)
    			a++;
    		swap(p,a,array);
    		p = a;
    	}
    	if(p > l)
    		quickSort(l,p-1);
    	if(p < r)
    		quickSort(p+1,r);
    	
    }
    
    public void swap(int a,int b,int[] c)
    {
    	int hold = c[a];
    	c[a] = c[b];
    	c[b] = hold;
    }
    
    public void printArray(int[] c)
    {
    	for(int i = 0;i < c.length;i++)
    		System.out.print(c[i] + " ");
    	System.out.println();
    }
    
    public static void main(String[]args)
    {
    	new QuickSort();
    }
    
    
}