import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class WaterBottleGUI extends JFrame
{
	Canvas screen;
	JPanel buttonPanel;
	JPanel bottlePanel;
	JTextArea info;
	JTextField input;
	JButton open;
	JButton fill;
	JButton pour;
	JButton dump;
	JButton contents;
	JButton reset;
	WaterBottle bottle;
	ButtonClicked onClick;
	
    public WaterBottleGUI()
    {
    	screen = new Canvas();
    	buttonPanel = new JPanel();
    	bottlePanel = new JPanel();
    	info = new JTextArea("info");
    	input = new JTextField();
    	open = new JButton("Open");
    	fill = new JButton("Fill");
    	contents = new JButton("Set Contents");
    	reset = new JButton("Reset");
    	dump = new JButton("Dump");
    	pour = new JButton("Pour");
    	bottle = new WaterBottle();
    	onClick = new ButtonClicked();
    	
    	setLayout(new GridLayout(1,2));
    	add(bottlePanel);
    	add(buttonPanel);
    	
    	bottlePanel.setLayout(new BorderLayout());
    	bottlePanel.add(screen,BorderLayout.CENTER);
    	screen.setBackground(new Color(235,245,255));
    	bottlePanel.add(info,BorderLayout.SOUTH);
    	info.setEditable(false);
    	
    	buttonPanel.setLayout(new GridLayout(7,1));
    	buttonPanel.add(open);
    	buttonPanel.add(fill);
    	buttonPanel.add(pour);
    	buttonPanel.add(dump);
    	buttonPanel.add(contents);
    	buttonPanel.add(reset);
    	buttonPanel.add(input);
    	
    	open.addActionListener(onClick);
    	fill.addActionListener(onClick);
    	pour.addActionListener(onClick);
    	dump.addActionListener(onClick);
    	contents.addActionListener(onClick);
    	reset.addActionListener(onClick);
    	
    	updateInfo();
    	
    	
    	setSize(600,800);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(3);
    	setVisible(true);
    	
    	//repaint();
    }
    
    private class ButtonClicked implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		JButton button = (JButton)e.getSource();
    		try
    		{
	    		if(button == open)
	    		{
	    			bottle.toggleLid();
	    			if(bottle.isOpen())
	    				open.setText("Close");
	    			else
	    				open.setText("Open");
	    		}
	    		else if(button == fill)
	    		{
	    			bottle.fill(Integer.parseInt(input.getText()));
	    		}
	    		else if(button == pour)
	    		{
	    			bottle.pour(Integer.parseInt(input.getText()));
	    		}
	    		else if(button == dump)
	    		{
	    			bottle.dump();
	    		}
	    		else if(button == contents)
	    		{
	    			bottle.fill(0,input.getText());
	    		}
	    		else if(button == reset)
	    		{
	    			try
	    			{
	    				bottle = new WaterBottle(0,Integer.parseInt(input.getText()));
	    			}
	    			catch(NumberFormatException nfe)
	    			{
	    				bottle = new WaterBottle();
	    			}
	    			
	    		}
    		}
    		catch(Exception ex)
    		{
    			input.setText("error");
    		}
    		
    		updateInfo();
    		repaint();
    	}
    }
    
    @Override
    public void paint(Graphics g)
    {
    	super.paint(g);
    	g = screen.getGraphics();
    	g.setColor(new Color(0,255,255));
    	g.fillRect(0,0,50,50);
    }
    
    public void updateInfo()
    {
    	String outputString;
    	outputString = "Lid is ";
    	if(bottle.isOpen())
    		outputString += "open\n";
    	else
    		outputString += "closed\n";
    	outputString += "Amount: " + bottle.getAmount() + "\n";
    	outputString += "Capacity: " + bottle.getCapacity() + "\n";
    	outputString += "Contents: " + bottle.getContents();
    	info.setText(outputString);
    }
    
    public static void main(String[]args)
    {
    	new WaterBottleGUI();
    }
}