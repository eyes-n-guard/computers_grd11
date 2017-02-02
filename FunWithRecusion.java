import java.io.*;
import java.math.*;

public class FunWithRecusion
{
	public static void main(String[]args)
	{
		long startTime = System.nanoTime();
		int num = 2000;
		//printNum(5);
		//System.out.println(factorial(5));
		//fibonacci(20);
		//for(int i=1;i<=60;i++)
		//	System.out.println(fibonacci2(i) + "");
		fibonacci4(num);
		//System.out.println(num + " " + fibonacci5(num));
		
		long time = System.nanoTime() - startTime;
		System.out.println("Execution Time: " + Math.round((double)time/1000000000) + "s (" + (double)time/1000000.0 + " ms)");
	}
	
	static void printNum(int n)
	{
		//System.out.println(n);
		if(n<10)
			printNum(n+1);
		System.out.println(n);
	}
	
	static int factorial(int n)
	{
		if(n<1)
			return(1);
		if(n>1)
			n *= factorial(n-1);
		return(n);
	}
	
	static int fibonacci2(int n)
	{
		if(n>2)
			return fibonacci2(n-1) + fibonacci2(n-2);
		return 1;
	}
	
	static long fibonacci(long n)
	{
		
		if(n == -1 || n == -2)
			return -1;
		if(n < -2)
			return fibonacci(n+1) + fibonacci(n+2);
		if(n>0)
		{
			fibonacci(n-1);
			System.out.println(n + " " + -fibonacci(-n));
		}
		return 0;
	}
    
    static void fibonacci3(int n)
    {
    	double s5 = Math.sqrt(5);
    	for(int i=1;i<=n;i++)
    		System.out.println(i + " " + Math.round(Math.pow((1+s5)/2,i)/s5));
    }
    
    static void fibonacci4(int n)
    {
    	double s5 = Math.sqrt(5);
    	for(int i=1;i<=n;i++)
    		System.out.println(i + " " + BigDecimal.valueOf((1+s5)/2).pow(i).divide(BigDecimal.valueOf(s5),10,RoundingMode.HALF_UP).setScale(0,RoundingMode.HALF_UP).toBigInteger().toString());
    }
    
    static BigInteger fibonacci5(int n)
    {
    	double s5 = Math.sqrt(5);
    	return BigDecimal.valueOf((1+s5)/2).pow(n).divide(BigDecimal.valueOf(s5),10,RoundingMode.HALF_UP).setScale(0,RoundingMode.HALF_UP).toBigInteger();
    }
    	
}