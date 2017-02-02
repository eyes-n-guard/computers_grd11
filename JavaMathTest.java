import java.io.*;

public class JavaMathTest
{

    public static void main(String[]args)
    {
    	System.out.println(h(0,1));
    }
    
    public static double h(int n,int m)
    {
    	return a(m/2) + n;
    }
    
    public static double d(int n)
    {
    	System.out.println("d");
    	return n;
    }
    
    public static double a(int n)
    {
    	System.out.println("a");
    	return n;
    }
}