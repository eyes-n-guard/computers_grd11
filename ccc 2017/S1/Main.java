import java.io.*;
import java.util.Scanner;

public class Main //S1
{
	public static void main(String[]args)
	{
		Scanner scan = new Scanner(System.in);
		int len = scan.nextInt();
		int[] a = new int[len];
		int[] b = new int[len];
		
		for(int i=0;i < a.length;i++)
			a[i] = scan.nextInt();
		for(int i=0;i < b.length;i++)
			b[i] = scan.nextInt();
		
		int aSum = 0;
		int bSum = 0;
		for(int i=0;i < a.length;i++)
		{
			aSum += a[i];
			bSum += b[i];
		}
		
		int j;
		for(j = a.length -1;j >= 0 && aSum != bSum;j--)
		{
			aSum -= a[j];
			bSum -= b[j];
		}
		
		j++;
		System.out.println("" + j);
	}
    
}