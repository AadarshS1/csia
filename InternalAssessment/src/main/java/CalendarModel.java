import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.table.AbstractTableModel;

public class CalendarModel{
	private Calendar cal;
	private LocalDate now;
	private int monthsFromCurrent;
	private int daysFromCurrent;
	private String[] rowData;

	public CalendarModel(){
		cal = Calendar.getInstance();
		now = LocalDate.of(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH));
		setRowData();
		monthsFromCurrent = 0;
		daysFromCurrent = 0;
	}

	public LocalDate getRequestedDay(){
		int requestedYear = cal.get(Calendar.YEAR);
		int requestedMonth = cal.get(Calendar.MONTH);
		int requestedDay = cal.get(Calendar.DAY_OF_MONTH);
		return LocalDate.of(requestedYear, requestedMonth+1, requestedDay);
	}
	/**
	 * Displays the next month on the calendar
	 * @param eventMan An eventManager that manages events for this calendar
	 */
	public void nextMonth(){
		monthsFromCurrent = 1;
		this.setRowData();
	}

	/**
	 * Displays the current month on the calendar
	 * @param eventMan An eventManager that manages events for this calendar
	 */
	public void currentMonth(){
		monthsFromCurrent = 0;
		this.setRowData();
	}

	/**
	 * Displays the previous month on the calendar
	 * @param eventMan An eventManager that manages events for this calendar
	 */
	public void previousMonth(){
		monthsFromCurrent = -1;
		this.setRowData();
	}

	private void setRowData(){
		cal.add(Calendar.MONTH, monthsFromCurrent);

		//set calendar to current time
		cal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),1);

		//get other important dates
		int firstDayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
		int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int weeksInMonth = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);

		int day = 1;
		rowData = new String[weeksInMonth*7];
		for(int i = 0; i < weeksInMonth*7; i++){
			if(i < firstDayOfMonth - 1){
				rowData[i]="  ";
			}
			else if(i > firstDayOfMonth-2 + daysInMonth){
				rowData[i]="  ";
			}else{
				rowData[i]=""+day;
				day++;
			}
		}

	}

	public LocalDate getDateNow(){
		return now;
	}

	public String[] getRowData(){
		return rowData.clone();
	}


}