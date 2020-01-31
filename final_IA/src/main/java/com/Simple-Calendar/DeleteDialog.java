package com.Simple;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class DeleteDialog extends JDialog{
	private JLabel dayLabel = new JLabel("Day field here");
	private EventDateModel model;
	private LocalDate date;
	private JPanel buttonPanel;
	private ButtonGroup buttonGroup;
	private String chosenEvent = "";
	private JButton ok;
	
	public DeleteDialog(JFrame owner, EventDateModel model, LocalDate date){
		super(owner, "Delete Event", true);
		
		
		this.model = model;
		this.date = date;
		
		
		this.setLayout(new FlowLayout());
		this.add(dayLabel);
		this.add(new JLabel("Select an event to delete"));
		buttonPanel = new JPanel();
		buttonGroup = new ButtonGroup();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		
		System.out.println(date);
		
		String[] eventsToDelete = model.getEventList(date);
		if(eventsToDelete.length==0)
			JOptionPane.showMessageDialog(this, "No more event to delete");
		
		JRadioButton none = new JRadioButton("None", true);
		JRadioButton[] buttons = new JRadioButton[eventsToDelete.length];
		System.out.println(eventsToDelete[0]);
		buttonGroup.add(none);
		buttonPanel.add(none);
		for(int i = 0; i < eventsToDelete.length; i++){
			buttons[i] = new JRadioButton(eventsToDelete[i], false);
			
			buttonGroup.add(buttons[i]);
			buttonPanel.add(buttons[i]);
			buttons[i].addActionListener(event->{
				JRadioButton button = (JRadioButton) event.getSource();
				chosenEvent = button.getText();
				int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure?");
				if(confirmation == JOptionPane.OK_OPTION){
					delete();
					model.update();
					button.setVisible(false);
				}
			});
		}
		this.add(buttonPanel);
		ok = new JButton("Done");
		this.add(ok);
		ok.addActionListener(event->{
			setVisible(false);
		});
		this.pack();
		
	}
	
	public void attachModel2(EventDateModel model, LocalDate date){
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

	public void delete(){
		if(!chosenEvent.equals("")){
			String[] arg = chosenEvent.split("\n");//get time and event seperated
			
			//get start and end time
			String[] t = arg[0].split("-");
			Event.Time start = extractTime(t[0].trim());
			Event.Time end = extractTime(t[1].trim());
			
			Event tobeDelete = new Event(arg[1], start, end);
			model.deleteEventFromDate(date, tobeDelete);
			model.update();
		}
	}
	
	
	
	
}
