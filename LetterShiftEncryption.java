import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LetterShiftEncryption extends JFrame
{
	JTextArea inputArea;
	JButton convert;
	JScrollPane sPane;
	ButtonClicked onClick;
	
    public LetterShiftEncryption()
    {
    	inputArea = new JTextArea();
    	convert = new JButton("Encrypt/Decrypt");
    	sPane = new JScrollPane(inputArea);
    	onClick = new ButtonClicked();
    	
    	setLayout(new BorderLayout());
    	add(sPane, BorderLayout.CENTER);
    	add(convert, BorderLayout.SOUTH);
    	
    	convert.addActionListener(onClick);
    	inputArea.setLineWrap(true);
    	
    	setSize(600,400);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(3);
    	setTitle("Shift Encrypter");
    	setVisible(true);
    }
    
    private class ButtonClicked implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		String inputString = inputArea.getText();
    		for(int i=0;i < inputString.length();i++)
    		{
    			char c = inputString.charAt(i);
				if(c >= 'A' && c <= 'Z')
				{
					inputString = inputString.replace("" + c, "" + (char)(c + 'a' - 'A'));
					i = 0;
				}
					
    		}
    		
			String shiftString = inputString.substring(0, inputString.indexOf(';'));
			if(shiftString.equals("decrypt"))
			{
				String solutions = inputArea.getText();
				String code = inputString.substring(inputString.indexOf(';') + 1);
				
				for(int shift=1;shift < 26;shift++)
					solutions += "\n" + shift + ";" + letterShift(code, 26 - shift);
				
				inputArea.setText(solutions);
			}
			else
			{
				try
				{
					int shift = Integer.parseInt(shiftString);
		    		
		    		inputArea.setText(letterShift(inputString, shift));
				}
				catch(NumberFormatException nfe)
	    		{
	    			inputArea.setText(inputArea.getText() + "\n" + "Shift Number Error");
	    		}
    			
    		}
	    		
    		
    		
    		
    	}
    }
    
    public static void main(String[]args)
    {
    	new LetterShiftEncryption();
    }
    
    private String letterShift(String s, int shift)
    {
    	StringBuilder stringOut = new StringBuilder(s);
		    		
		for(int i=0;i < stringOut.length();i++)
		{
			char c = stringOut.charAt(i);
			if(c >= 'a' && c <= 'z')
				stringOut.setCharAt(i, (char)((c - 'a' + shift) % 26 + 'a'));
		}
		
		return stringOut.toString();
    }
    
}