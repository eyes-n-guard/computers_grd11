import java.io.*;
import java.util.Scanner;

public class Main //S3
{
	public static void main(String[]args)
	{
		Scanner scan = new Scanner(System.in);
		int[] boards = new int[scan.nextInt()];
		for(int i=0;i < boards.length;i++)
			boards[i] = scan.nextInt();
		shellSort(boards);
		
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