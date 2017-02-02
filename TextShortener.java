import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TextShortener extends JFrame
{
	JTextArea inputArea;
	JButton format;
	ButtonClicked onClick;
	
    public TextShortener()
    {
    	inputArea = new JTextArea();
    	format = new JButton("Shorten");
    	onClick = new ButtonClicked();
    	
    	inputArea.setLineWrap(true);
    	format.addActionListener(onClick);
    	
    	setLayout(new BorderLayout());
    	add(inputArea, BorderLayout.CENTER);
    	add(format, BorderLayout.SOUTH);
    	
    	setSize(600,400);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(3);
    	setTitle("Text Shortener");
    	setVisible(true);
    }
    
    private class ButtonClicked implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		String excluded = "AaEeIiOoUuYy";
    		//String excluded = "BbCcDdFfGgHhJjKkLlMmNnPpQqRrSsTtVvWwXxZz";
    		
    		String inputString = inputArea.getText();
    		
    		for(int i=0;i < inputString.length();i++)
    		{
    			char c = inputString.charAt(i);
    			if(excluded.indexOf(c) != -1)
    			{
    				inputString = inputString.replace("" + c, "");
    				i = 0;
    			}
    		}
    		
    		inputArea.setText(inputString);
    	}
    }
    
    public static void main(String[]args)
    {
    	new TextShortener();
    }
    
}