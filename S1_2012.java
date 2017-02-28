import java.io.*;
import java.util.Scanner;

public class S1_2012
{
	public static void main(String[]args)
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("" + ncr(6,4));
	}
	
	public static int ncr(int n, int k)
	{
		return (int)(factorial(n) / (factorial(n-k) * factorial(k)));
	}
	
	public static long factorial(int n)
	{
		if(n == 0)
			return 1;
		long f = (int)n;
		for(long i=f;i > 2;i--)
			f *= i-1;
		return f;
	}
}