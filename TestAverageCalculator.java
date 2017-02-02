import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;

public class TestAverageCalculator extends JFrame implements ActionListener
{
	JTextArea nameArea;
	JPanel ioPanel;
	LinkedList[] markList = new LinkedList[4];
	JTextField[] inputFields = new JTextField[4];
	JTextField[] outputFields = new JTextField[4];
	JTextField nameField;
	JButton addMark;
	JButton avgButton;
	
    public TestAverageCalculator()
    {
    	for(int i=0; i < markList.length; i++)
    	{
    		markList[i] = new LinkedList<Float>();
    		inputFields[i] = new JTextField();
    		outputFields[i] = new JTextField();
    		outputFields[i].setEditable(false);
    	}
    	addMark = new JButton("Input Mark");
    	avgButton = new JButton("Average");
    	nameArea = new JTextArea();
    	nameField = new JTextField();
    	nameArea.setEditable(false);
    	
    	addMark.addActionListener(this);
    	avgButton.addActionListener(this);
    	
    	ioPanel = new JPanel();
    	ioPanel.setLayout(new GridLayout(4,5));
    	
    	ioPanel.add(new JLabel("a avg"));
    	ioPanel.add(new JLabel("b avg"));
    	ioPanel.add(new JLabel("c avg"));
    	ioPanel.add(new JLabel("d avg"));
    	ioPanel.add(avgButton);
    	
    	for(int i=0; i < outputFields.length; i++)
    		ioPanel.add(outputFields[i]);
    	ioPanel.add(addMark);
    	
    	ioPanel.add(new JLabel("a mark"));
    	ioPanel.add(new JLabel("b mark"));
    	ioPanel.add(new JLabel("c mark"));
    	ioPanel.add(new JLabel("d mark"));
    	ioPanel.add(new JLabel("Name"));
    	
    	for(int i=0; i < inputFields.length; i++)
    		ioPanel.add(inputFields[i]);
    	ioPanel.add(nameField);
    	
    	setLayout(new BorderLayout());
    	add(nameArea, BorderLayout.CENTER);
    	add(ioPanel, BorderLayout.SOUTH);
    	
    	setSize(800,600);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(3);
    	setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e)
    {
    	JButton button = (JButton)e.getSource();
    	if(button == addMark)
    	{
    		try
    		{
	    		for(int i=0; i < markList.length; i++)
	    		{
	    			float mark = Float.parseFloat(inputFields[i].getText());
	    			markList[i].add(mark);
	    			inputFields[i].setText("");
	    			outputFields[i].setText("");
	    		}
	    		nameArea.setText(nameArea.getText() + nameField.getText() + "\n");
	    		nameField.setText("");
    		}
    		catch(Exception ex){}
    	}
    	else
    	{
    		for(int i=0; i < markList.length; i++)
    		{
    			Float[] markArray = (Float[])markList[i].toArray(new Float[0]);
    			float sum = 0;
    			for(int j=0; j < markArray.length; i++)
    				sum += markArray[j];
    			sum /= markArray.length;
    			outputFields[i].setText("" + sum);
    		}
    	}
    }
    
    public static void main(String[]args)
    {
    	new TestAverageCalculator();
    }
    
    
}