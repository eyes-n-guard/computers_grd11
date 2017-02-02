import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CharacterSort extends JFrame
{
	JTextArea input;
	JButton sort;
	ButtonClicked onClick;
	
    public CharacterSort()
    {
    	
    	onClick = new ButtonClicked();
    	input = new JTextArea();
    	input.setLineWrap(true);
    	
    	sort = new JButton("Sort");
    	sort.addActionListener(onClick);
    	
    	add(input, BorderLayout.CENTER);
    	add(sort, BorderLayout.SOUTH);
    	
    	
    	setSize(400,300);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(3);
    	setTitle("Character Sorter");
    	setVisible(true);
    }
    
    private class ButtonClicked implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		String inputString = input.getText();
    		for(int i=0;i < inputString.length();i++)
    		{
    			char c = inputString.charAt(i);
    			if(!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z'))
    			{
    				inputString = inputString.replace("" + c,"");
    				i = 0;
    			}
    		}
    			
    		
    		for(int i=0;i < inputString.length() - 1;i++)
	    		for(int n=i;n >=0 && (inputString.charAt(n) > inputString.charAt(n+1));n--)
	    		{
	    			String start = inputString.substring(0,n);
	    			String end = inputString.substring(n+2,inputString.length());
	    			inputString = start + inputString.charAt(n+1) + inputString.charAt(n) + end;
	    		}
	    	input.setText(inputString);
    	}
    }
    
    
    public static void main(String[]args)
    {
    	new CharacterSort();
    }
    
       
}