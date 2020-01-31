package com.Simple;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EventDateModel {
	ArrayList<EventDate> dates;
	ChangeListener listener;

	public EventDateModel(){
		dates = new ArrayList<>();
	}
	
	public void add(EventDate aDate){
		dates.add(aDate);
	}
	
	public String search(LocalDate date){
		for(int i = 0; i < dates.size(); i++){
			if(dates.get(i).getDate().equals(date))
				return dates.get(i).toString();			
		}
		return "";
	}
	
	public String[] getEventList(LocalDate date){
		EventDate dateNeeded = null;
		for(int i = 0; i < dates.size(); i++){
			if(dates.get(i).getDate().equals(date)){
				dateNeeded = dates.get(i);			
				break;
			}
		}
		
		return dateNeeded.getEventList();
	}
	
	public boolean addEventToDate(LocalDate adate, Event event){
		int i = 0;
		boolean found = false;
		boolean addResult = false;
		EventDate date =  null;
		while(i < dates.size() && !found){
			if(dates.get(i).date.equals(adate)){
				date = dates.get(i);
				found = true;
			}
			i++;
		}
		
		if(found)
			addResult = date.add(event);
		if(!found){
			date = new EventDate(adate);
			addResult =date.add(event);
			dates.add(date);
		}
			
		return addResult;
	}
	
	public void deleteEventFromDate(LocalDate adate, Event event){
		int i = 0;
		boolean found = false;
		while(i < dates.size() && !found){
			if(dates.get(i).date.equals(adate)){
				dates.get(i).remove(event);
				found = true;
			}
			i++;
		}
	}
	
	public String toString(){
		String line = "";
		for(int i = 0; i < dates.size(); i++){
			line+=dates.get(i).getDate()+"\n"+dates.get(i).toString()+"\n";
		}
		return line;
	}
	
	public void attach(ChangeListener listener){
		this.listener = listener;
	}
	
	public void update(){
		listener.stateChanged(new ChangeEvent(this));
	}
	
	public void write(String filename){
		try{
			File file = new File(filename);
			FileWriter writer = new FileWriter(file.getAbsoluteFile());
			BufferedWriter buffer = new BufferedWriter(writer);
			for(int i = 0; i < dates.size(); i++){
				buffer.write(dates.get(i).getDate().toString());
				buffer.write("\n");
				buffer.write(dates.get(i).toString());
			}
			buffer.close();
		}catch(IOException ex){
			System.out.println("Error writing to file");
		}
	}
	
	public void read(String filename){
		try{
			File file = new File(filename);
			if(file.exists()){
				Scanner in = new Scanner(file);
				String line = "";
				String previousLine = "";
				EventDate eventDate = null;
				LocalDate date = null;
				Event.Time start = null;
				Event.Time end = null;
				
				boolean newDate = true;
				
				while(in.hasNextLine()){
					if(previousLine.equals("")){// get day
						line = in.nextLine();
						String[] l = line.split("-");
						date = LocalDate.of(Integer.parseInt(l[0]), 
								Integer.parseInt(l[1]),Integer.parseInt(l[2]));
					}
					if(in.hasNextLine()){
						previousLine = line;
						line = in.nextLine();
						if(!line.equals("")){ //get time
							String[] t = line.split("-");
							start = extractTime(t[0].trim());
							end = extractTime(t[1].trim());
						}else previousLine = line;
					}
					if(in.hasNextLine() && !previousLine.equals("")){
						previousLine = line;
						line = in.nextLine();
						if(!line.equals("")){
							this.addEventToDate(date, new Event(line, start,end));
						}
					}
				}	
			}
			else{
			System.out.println("This is the first run");
			}
		}catch(IOException ex){
			System.out.println("Error opening file");
		}
	}
	
	public Event.Time extractTime(String line){
		int colon = line.indexOf(":");
		int hour = Integer.parseInt(line.substring(0, colon));
		int min = Integer.parseInt(line.substring(colon+1,colon+3));
		boolean am = line.substring(colon+3).equalsIgnoreCase("am");
		
		return new Event.Time(hour,min,am);
	}
	
}
