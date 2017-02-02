import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class BFLanguageIDE extends JFrame
{
	JTextArea code;
	JPanel outputPanel;
	JTextField outputField;
	JButton run;
	ButtonClicked onClick;
	
    public BFLanguageIDE()
    {
    	code = new JTextArea();
    	outputPanel = new JPanel();
    	run = new JButton("Run");
    	outputField = new JTextField();
    	onClick = new ButtonClicked();
    	
    	setLayout(new BorderLayout());
    	add(code, BorderLayout.CENTER);
    	add(outputPanel, BorderLayout.SOUTH);
    	
    	outputPanel.setLayout(new GridLayout(2,1));
    	outputPanel.add(run);
    	outputPanel.add(outputField);
    	
    	run.addActionListener(onClick);
    	code.setLineWrap(true);
    	
    	setSize(600,400);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(3);
    	setVisible(true);
    }
    
    private class ButtonClicked implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		//format input
    		String validChars = "<>,.+-[]";
    		String codeString = code.getText();
    			
    		for(int i=0;i < codeString.length();i++)
    		{
    			char c = codeString.charAt(i);
    			if(validChars.indexOf(c) == -1)
    			{
    				codeString = codeString.replace("" + c,"");
    				i = 0;
    			}
    		}
    		
    		//run formated code
    		int[] mem = new int[20];
    		Stack<Integer> loops = new Stack<Integer>();
    		outputField.setText("");
    		for(int i=0;i < mem.length;i++)
    			mem[i] = 0;
    		int memI = 0;
    		
    		for(int index=0; index < codeString.length();index++)
    		{
    			char c = codeString.charAt(index);
    			
    			if(c == '<')
    				memI--;
    			else if(c == '>')
    				memI++;
    			else if(c == '-')
    				mem[memI]--;
    			else if(c == '+')
    				mem[memI]++;
    			else if(c == '[')
    				loops.push(new Integer(index));
    			else if(c == ']')
    			{
    				if(mem[memI] == 0)
    					loops.pop();
    				else
    					index = (Integer)loops.peek();
    			}
    			else if(c == '.')
    				outputField.setText(outputField.getText() + (char)mem[memI]);
    				
    		}
    	}
    }
    
    public static void main(String[]args)
    {
    	new BFLanguageIDE();
    }
    
}