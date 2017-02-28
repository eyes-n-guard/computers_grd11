import java.io.*;
import java.util.Scanner;

public class Main //S5
{
	public static void main(String[]args)
	{
		Scanner scan = new Scanner(System.in);
		Train[] stations = new Train[scan.nextInt()];
		int[] starts = new int[scan.nextInt()];
		Train[] index = new Train[starts.length];
		int tests = scan.nextInt();
		
		for(int i=0;i < stations.length;i++)
		{
			startIn = scan.nextInt();
			stations[i] = new Train(0,startIn);
			if(starts[startIn] == null)
			{
				starts[startIn] = i;
				index[startIn] = stations[i];
			}
			else
			{
				index[startIn].setNext(stations[i]);
				index = index.getNext();
			}
		}
		
		for(int i=0;i < index.length;i++)
			index[i].setNext(starts[i]);
		
		for(int i=0;i < stations.length;i++)
			stations[i].setSize(scan.nextInt());
		
		for(int i=0;i < tests;i++)
		{
			if(scan.nextInt() == 1)
			{
				int start = scan.nextInt();
				int end = scan.nextInt();
				int sum = 0;
				for(int j=start-1;j < end;j++)
					sum += stations[j];
				System.out.println("" + sum);
			}
			else
			{
				starts[i] = starts[i].getNext();
			}
		}
		
		
	}
	
	private class Train
	{
		int size;
		Train next;
		int start;
		public Train(int n, int s)
		{
			size = n;
			start = s;
		}
		
		public Train()
		{
			size = 0;
			next = null;
		}
		
		public Train(int n)
		{
			size = n;
			next = null;
		}
		
		public int getStart()
		{
			return start;
		}
		
		public int getSize()
		{
			return size;
		}
		
		public void setSize(int n)
		{
			size = n;
		}
		
		public Train getNext()
		{
			return next;
		}
		
		public void setNext(Train t)
		{
			next = t;
		}
	}
}