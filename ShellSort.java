import java.io.*;

public class ShellSort
{
	int[] array;
	
    public ShellSort()
    {
    	array = new int[100];
    	for(int i=0;i < array.length;i++)
    		array[i] = (int)(Math.random() * 10000);
    	
    	printArray(array);
    	long startTime = System.nanoTime();
    	shellSort(array,1,0);
    	//insertionSort(array);
    	//gnomeSort(array);
    	long time = System.nanoTime() - startTime;
    	
    	printArray(array);
		System.out.println("Execution Time: " + time + " ns");
    	
    	
    }
    
    public void shellSort(int[] a, int interval, int offset)
    {
    	//call as shellSort(array,1,0);
    	if(interval*2 < a.length && offset == 0)
    		shellSort(a,interval*2,0);
    	if(offset < interval - 1 && interval + offset < a.length - 1)
    		 shellSort(a,interval,offset+1);
    	
    	for(int i=offset;i + interval < a.length;i += interval)
    	{
    		if(a[i] > a[i+interval])
    		{
    			int hold = a[i+interval],n;
    			for(n = i;n >= offset && a[n] > hold;n -= interval)
    				a[n+interval] = a[n];
    			a[n+interval] = hold;
    		}
    	}
    }
    
    public void insertionSort(int[] list) //dank
    {
    	for(int i = 0;i < list.length - 1;i++)
    		if(list[i] > list[i+1])
    		{
    			int hold = list[i+1],n;
    			for(n = i;n >= 0 && list[n] > hold;n--)
    				list[n+1] = list[n];
    			list[n+1] = hold;
    		}
    		
    }
    
    public void gnomeSort(int[] list)
    {
//    	for(int i=0;i < list.length - 1;i++)
//    		for(int n=i;n >=0 && list[n] > list[n+1];n--)
//    		{
//    			list[n] += list[n+1];
//    			list[n+1] = list[n] - list[n+1];
//    			list[n] -= list[n+1];
//    		}


		for(int i=0;i < list.length;)
			if(i==0 || list[i] >= list[i-1])
				i++;
			else
			{
				int hold = list[i];
				list[i] = list[i-1];
				list[--i] = hold;
			}
    }
    
    public void printArray(int[] a)
    {
    	for(int i=0;i<a.length;i++)
    		System.out.print(a[i] + " ");
    	System.out.println();
    }
    
    public void swap(int a,int b,int[] c)
    {
    	int hold = c[a];
    	c[a] = c[b];
    	c[b] = hold;
    }
    
    public static void main(String[]args)
    {
    	new ShellSort();
    }
    
}