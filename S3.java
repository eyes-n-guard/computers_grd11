import java.util.Stack;
import java.util.Scanner;
import java.io.*;

public class S3
{    
    public static void main(String[]args)
    {
    	Scanner scan = new Scanner(System.in);
    	String[] results = new String[scan.nextInt()];
    	
    	for(int t=0;t < results.length;t++)
    	{
    		int[] array = new int[scan.nextInt()];
    		Stack<Integer> trains = new Stack<Integer>();
    		for(int i=array.length-1;i >= 0;i--) //load array backwards
    			array[i] = scan.nextInt();
    		
    		int out = 1;
    		boolean valid = true;
    		int i=0;
    		while(valid && (i < array.length || !trains.empty()))
    		{
				if(!trains.empty() && trains.peek().intValue() == out)
				{
					trains.pop();
					out++;
				}
				else if(i < array.length && array[i] == out)
				{
					i++;
					out++;
				}
				else if(i < array.length && (trains.empty() || array[i] < trains.peek().intValue()))
				{
					trains.push(new Integer(array[i]));
					i++;
				}
				else
					valid = false;
    		}
    		results[t] = (valid ? "Y" : "N");
    	}
    	
    	for(int i=0;i < results.length;i++)
    		System.out.println(results[i]);
    }
}