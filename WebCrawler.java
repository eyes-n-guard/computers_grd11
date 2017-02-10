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
    		crawl(branchLimit,new URL(URLString));
    	}
    	catch(Exception e){}
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
    	boolean allow = true;
		try
		{
			int j;
			String robotLine;
			DataInputStream robot = new DataInputStream(new BufferedInputStream(new URL(url.toString() + "/robots.txt").openStream()));
			
			while((robotLine = robot.readLine()) != null && allow)
				if((j = robotLine.indexOf("Disallow:")) != -1 && robotLine.substring(10).length() > 4)
					allow = false;
		}
		catch(Exception e){}
    	
    	if(n > 0 && websites.size() < sizeLimit && allow)
    	{
    		//System.out.println("n: " + n + " size:" + websites.size() + " url: " + url.toString());
    		
    		websites.add(url);
    		InputStream in = null;
    		DataInputStream dataIn;
    		String line;
    		
    		try
    		{
    			in = url.openStream();
    			dataIn = new DataInputStream(new BufferedInputStream(in));
    			int i;
    			while((line = dataIn.readLine()) != null && websites.size() < sizeLimit) //stop reading website once limit reached
    			{
    				try
    				{
	    				if((i = line.indexOf("href")) != -1)
	    				{
	    					i += 6;
	    					URL subURL = new URL(line.substring(i,line.indexOf(line.charAt(i-1),i)));
	    					
	    					if(!websites.contains(subURL))
	    						crawl(n-1,subURL);
	    				}
    				}
    				catch(Exception ex){}
    				
    			}
    				
    		}
    		catch(Exception e){}
    		finally
    		{
    			try
    			{
    				in.close();
    			}
    			catch(Exception ioe){}
    		}
    	}
    }
    
    public static void main(String[]args)
    {
    	WebCrawler crawler = new WebCrawler(30,5,"https://en.wikipedia.org/wiki/Main_Page");
    	URL[] siteArray = crawler.getArray();
    	//System.out.println("\n\n\n");
    	for(int i=0;i < siteArray.length;i++)
    		System.out.println(i + " " + siteArray[i].toString());
    }
    
    
}