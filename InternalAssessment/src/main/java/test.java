import java.time.LocalDate;

public class test {

	public static Event.Time extractTime(String line){
		int colon = line.indexOf(":");
		int hour = Integer.parseInt(line.substring(0, colon));
		int min = Integer.parseInt(line.substring(colon+1,colon+3));
		boolean am = line.substring(colon+3).equalsIgnoreCase("am");

		return new Event.Time(hour,min,am);
	}

	public static void main(String[] args) {
		EventDateModel events = new EventDateModel();

		Event e1 = new Event("Go to eat", new Event.Time(10,20,true), new Event.Time(11,30,true));

		Event e2 = new Event("Go to school", new Event.Time(12,20,true), new Event.Time(1,30,false));
		Event e3 = new Event("Go shower", new Event.Time(12,20,true), new Event.Time(1,30,false));
		Event e4 = new Event("Swimming", new Event.Time(1,40,true), new Event.Time(3,30,false));
		Event e5 = new Event("Taco day", new Event.Time(4,20,false), new Event.Time(5,30,false));
		Event e6 = new Event("Burger day", new Event.Time(9, 30, true), new Event.Time(11, 30, true));



		EventDate day1 = new EventDate(LocalDate.of(2016, 11, 13));
		EventDate day2 = new EventDate(LocalDate.of(2016, 11, 14));
		EventDate day3 = new EventDate(LocalDate.of(2016, 11, 15));
		EventDate day4 = new EventDate(LocalDate.of(2016, 12, 15));


		day1.add(e1);
		day2.add(e2);
		day2.add(e4);
		day3.add(e3);
		day3.add(e5);
		day4.add(e6);

		events.add(day1);
		events.add(day2);
		events.add(day3);
		events.add(day4);

		//	System.out.println(events.toString());

		//System.out.println(events.search(LocalDate.of(2016, 11, 15)));


	}

}