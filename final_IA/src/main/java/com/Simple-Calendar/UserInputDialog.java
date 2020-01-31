package com.Simple;

import java.awt.BorderLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserInputDialog extends JDialog {
	private JLabel dayLabel = new JLabel("Day field here");
	private JTextField startField = new JTextField("00:00am");
	private JTextField endField = new JTextField("00:00am");
	private JTextField eventField = new JTextField("Untitled event");
	private EventDateModel model;
	private LocalDate date;
	
	public UserInputDialog(JFrame owner){
		super(owner, "Create Event", true);
		this.add(eventField, BorderLayout.NORTH);
		JPanel panel = new JPanel();
		panel.add(dayLabel);
		panel.add(startField);
		panel.add(endField);
		
		JButton save = new JButton("Save");
		save.addActionListener(event->{
			Event.Time start = extractTime(startField.getText());
			Event.Time end = extractTime(endField.getText());
			System.out.println(start);
	    	System.out.println(end);
			Event sampleEvent = new Event(eventField.getText(), start,end);
			
			boolean addResult = model.addEventToDate(date, sampleEvent);
			if(addResult){
				setVisible(false);
				model.update();
			}
			else{
				JOptionPane.showMessageDialog(this, "The time for this event conflicts with an "
						+ "existing event");
				setVisible(true);
			}
		});
		
		panel.add(save);
		this.add(panel, BorderLayout.SOUTH);
		this.pack();
		
	}
	
	public void attachModel(EventDateModel model, LocalDate date){
		this.model = model;
		this.date = date;
		dayLabel.setText(date.toString());
	}
	
	public Event.Time extractTime(String line){
		int colon = line.indexOf(":");
		int hour = Integer.parseInt(line.substring(0, colon));
		int min = Integer.parseInt(line.substring(colon+1,colon+3));
		boolean am = line.substring(colon+3).equalsIgnoreCase("am");
		
		return new Event.Time(hour,min,am);
	}
	
	
	
}
