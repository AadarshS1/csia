package com.Simple;

import java.time.LocalDate;

public class SimpleCalendar {
	public static void main(String[] args) {
		CalendarModel cal = new CalendarModel();
		EventDateModel events = new EventDateModel();
		events.read("events.txt");
		
		MainCalendarScene frame = new MainCalendarScene(cal,events);
		
		events.attach(frame);
		String[] row = cal.getRowData();
	
	}

}



