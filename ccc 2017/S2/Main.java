import java.io.*;
import java.util.Scanner;

public class Main //S2
{
	public static void main(String[]args)
	{
		Scanner scan = new Scanner(System.in);
		int[] array = new int[scan.nextInt()];
		for(int i=0;i < array.length;i++)
			array[i] = scan.nextInt();
		shellSort(array,1,0);
		
		int high = (array.length / 2) + (array.length & 1);
		int low = high - 1;
		
		while(low >= 0 || high < array.length)
		{
			if(low >= 0)
				System.out.print(array[low] + " ");
			if(high < array.length)
				System.out.print(array[high] + " ");
			low--;
			high++;
		}
	}
	
	public static void shellSort(int[] a, int interval, int offset)
    {
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

}