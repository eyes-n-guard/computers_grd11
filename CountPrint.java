import java.io.*;

public class CountPrint
{
    public static void main(String[]args)
    {
    	int[][] array = new int[12][12];
    	for(int i=0;i<12;i++)
    		for(int j=0;j<12;j++)
    			array[i][j] = (i+1)*(j+1);
    		
    	for(int i=0;i<12;i++)
    	{
    		for(int j=0;j<12;j++)
    			System.out.print(array[i][j] + " ");
    		System.out.println();
    	}
    		
    }
}