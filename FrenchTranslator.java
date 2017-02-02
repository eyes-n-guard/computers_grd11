import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FrenchTranslator extends JFrame implements ActionListener
{
	JTextArea textArea;
	JButton convert;
	
    public FrenchTranslator()
    {
    	textArea = new JTextArea();
    	convert = new JButton("Convert");
    	
    	add(convert, BorderLayout.SOUTH);
    	add(textArea, BorderLayout.CENTER);
    	convert.addActionListener(this);
    	
    	setSize(600,600);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(3);
    	setTitle("French Translator");
    	setVisible(true);
    	
    }
    
    public void actionPerformed(ActionEvent e)
    {
    	StringBuilder input = new StringBuilder(textArea.getText());
    	for(int i=0;i < input.length();i++)
    	{
    		char c = input.charAt(i);
    		if(c >= 'a' && c <= 'z')
    			input.setCharAt(i,(char)('z' - c + 'a'));
    		else if(c >= 'A' && c <= 'Z')
    			input.setCharAt(i,(char)('Z' - c + 'A'));
    	}
    	textArea.setText(input.toString());
    }
    
    public static void main(String[]args)
    {
    	new FrenchTranslator();
    }
}