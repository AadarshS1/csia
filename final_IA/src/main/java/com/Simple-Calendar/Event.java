package com.Simple;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Represent an event schedule on a date
 */
public class Event {
	public static class Time implements Comparable<Time>{
		private int hour;
		private int min;
		private boolean am;
		
		public Time(int hour, int min, boolean am){
			this.hour = hour;
			this.min = min;
			this.am = am;
		}
		
		public Time getTime(){
			return new Time(hour,min,am);
		}

		@Override
		public int compareTo(Time o2) {
			if(this.am!=o2.am){
				if(this.am)
					return -1;
				else return 1;
			}
			else if(this.hour!=o2.hour) return this.hour - o2.hour;
			else return this.min - o2.min;
		}
		
		public boolean equals(Object otherObject){
			if(this == otherObject) return true;
			if(otherObject == null) return false;
			if(getClass() != otherObject.getClass()) return false;
			
			Time b = (Time) otherObject;
			return hour==b.hour && min==b.min && am==b.am;
		}
		
		public String toString(){
			String pad = "";
			if(min < 10)
				pad = "0";
			String line =  hour+":"+pad+min;
			if(am) line+="am";
			else line+="pm";
			return line;
		}
	}
	
	private String title;
	private Time start;
	private Time end;
	
	public Event(String title, Time start, Time end){
		this.title = title;
		this.start = start;
		this.end = end;
	}
	
	public Event(){
		this.title = "";
	}
	
	public Time startTime(){
		return start;
	}
	
	public Time endTime(){
		return end;
	}
	
	public void setStartTime(int hour, int min, boolean am){
		start = new Time(hour, min, am);
	}
	
	public void setEndTime(int hour, int min, boolean am){
		end = new Time(hour, min, am);
	}
	
	public String toString(){
		return start.toString()+" - "+end.toString()+"\n"+title;
	}
	
	public boolean equals(Object otherObject){
		if(this == otherObject) return true;
		if(otherObject == null) return false;
		if(getClass() != otherObject.getClass()) return false;
		
		Event anEvent = (Event) otherObject;
		return end.equals(anEvent.end) && start.equals(anEvent.start) && title.equals(anEvent.title);
	}
}
