package simple;

import java.time.LocalDate;
import java.util.ArrayList;

public class EventDate {
	LocalDate date;
	ArrayList<Event> events;
	
	public EventDate(LocalDate date){
		this.date = date;
		events = new ArrayList<>();
	}
	
	public int getEventCount(){
		return events.size();
	}
	
	public boolean add(Event anEvent){
		boolean conflict = false;
		int i = 0;
		if(anEvent.startTime().compareTo(anEvent.endTime()) > 0)
			return false;

		while(i < events.size() && !conflict){
			int start12 = anEvent.startTime().compareTo(events.get(i).startTime());
			int end1start2 = anEvent.endTime().compareTo(events.get(i).startTime());
			int start1end2 = anEvent.startTime().compareTo(events.get(i).endTime());
			if(start12 <= 0 && end1start2 >= 0 )
				conflict = true;
			else if(start12 >= 0 && start1end2 <= 0)
				conflict = true;
			else 
				conflict = false;
			i++;
		}
		
		if(!conflict)
			events.add(anEvent);
		return !conflict;
	}
	
	public void remove(Event anEvent){
		events.remove(anEvent);
	}
	
	public String[] getEventList(){
		String[] list = new String[events.size()];
		for(int i = 0; i < list.length; i++){
			list[i] = events.get(i).toString();
		}
		return list;
	}
	
	public String toString(){
		String line = "";
		
		events.sort((e1, e2)->{
			return e1.endTime().compareTo(e2.startTime());
		});
		
		for(int i = 0; i < events.size(); i++){
			line+= events.get(i).toString()+"\n";
		}
		
		return line+"\n";
	}
	
	public LocalDate getDate(){
		return date;
	}
}
