import java.io.*;

public class RecursionExample2
{
	public static void main(String[] args)
	{
		//System.out.println("" + (1.0/0.0 + 1.0/0.0));
		H(6);
	}
	
	public static double H(int n)
	{
		System.out.println(n);
		if(n == 0)
			return 0.0;
		return H(n/2) + 1.0/H(n-1);
	}
}