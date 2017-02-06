import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class WebCrawler
{
	LinkedList <URL>websites;
	int branchLimit, sizeLimit;
	
    public WebCrawler(int SL, int BL, String URLString)
    {
    	sizeLimit = SL;
    	branchLimit = BL;
    	websites = new LinkedList();
    	try
    	{
    		//System.out.println("pls work");
    		crawl(branchLimit,new URL(URLString));
    		//System.out.println("pls work");
    	}
    	catch(Exception e)
    	{
    		System.out.println("error");
    	}
    }
    
    public LinkedList getList()
    {
    	return websites;
    }
    
    public URL[] getArray()
    {
    	return websites.toArray(new URL[0]);
    }
    
    private void crawl(int n,URL url)
    {
    	//System.out.println("n: " + n + " url: " + url.toString());
    	
    	
    	if(n > 0 && websites.size() < sizeLimit)
    	{
    		//System.out.println("pls workweewesdf");
    		InputStream in = null;
    		DataInputStream dataIn;
    		String line;
    		
    		try
    		{
    			in = url.openStream();
    			dataIn = new DataInputStream(new BufferedInputStream(in));
    			
    			for(int i;(line = dataIn.readLine()) != null;)
    			{
    				try
    				{
	    				if((i = line.indexOf("href")) != -1)
	    				{
	    					i += 6;
	    					URL subURL = new URL(line.substring(i,line.indexOf(line.charAt(i-1),i)));
	    					//System.out.println(subURL.toString());
	    					boolean allow = true;
	    					try
	    					{
	    						int j;
		    					String robotLine;
		    					DataInputStream robot = new DataInputStream(new BufferedInputStream(new URL(subURL.toString() + "/robots.txt").openStream()));
		    					while((robotLine = robot.readLine()) != null)
		    						if((j = robotLine.indexOf("Disallow:")) != -1 && robotLine.substring(9).equals(" "))
		    							allow = false;
	    					}
	    					catch(Exception e){}
	    					
	    					
	    					if(!websites.contains(subURL) && allow)
	    					{
	    						websites.add(subURL);
	    						crawl(n-1,subURL);
	    					}
	    				}
    				}
    				catch(MalformedURLException mue)
    				{
    					//System.out.println("mue error");
    				}
    				
    			}
    				
    		}
    		catch(IOException ioe)
    		{
    			//ioe.printStackTrace();
    		}
    		finally
    		{
    			try
    			{
    				in.close();
    			}
    			catch(IOException ioe){}
    		}
    	}
    }
    
    public static void main(String[]args)
    {
    	WebCrawler crawler = new WebCrawler(100,5,"https://amirqualitymeats.com");
    	//System.out.println("pls wor4k");
    	URL[] siteArray = crawler.getArray();
    	//System.out.println("pls work" + siteArray.length);
    	for(int i=0;i < siteArray.length;i++)
    		System.out.println(i + " " + siteArray[i].toString());
    }
    
    
}