package simple;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserInputDialog extends JDialog {
	private static final Logger logger = Logger.getLogger(UserDao.class.getName());

	private JTextField startDate = new JTextField("Enter the start date here");
	private JTextField endDate = new JTextField("Enter the end date here");
	private JTextField teacherID = new JTextField("Enter teacher ID");
	private EventDateModel model;
	private LocalDate date;
	
	public UserInputDialog(JFrame owner){
		super(owner, "Add a duty date", true);
		JPanel panel = new JPanel();
		panel.add(startDate);
		panel.add(endDate);
		panel.add(teacherID);
		
		JButton save = new JButton("Save");
		save.addActionListener(event->{
			String id= teacherID.getText();
			String start = startDate.getText();
			String end = endDate.getText();
//			Event.Time start = extractTime(startDate.getText());
//			Event.Time end = extractTime(endDate.getText());
//			System.out.println(start);
//	    	System.out.println(end);
//			Event sampleEvent = new Event(startDate.getText(), start,end);
//
//			boolean addResult = model.addEventToDate(date, sampleEvent);
//			if(addResult){
//				setVisible(false);
//				model.update();
//			}
//			else{
//				JOptionPane.showMessageDialog(this, "The time for this event conflicts with an "
//						+ "existing event");
//				setVisible(true);
//			}
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet resultSet = null;
			try {
				connection = Database.getDBConnection();
				connection.setAutoCommit(false);
				String query = "INSERT INTO current_allocations(TeacherID, Duty_Date_Start, Duty_Date_End) VALUES(?, ?, ?)";
				statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				connection.commit();
				String name = "/Users/sundaa1/Desktop/out.csv";
				exportData(connection, name);
				resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {

				}
			} catch (SQLException exception) {
				logger.log(Level.SEVERE, exception.getMessage());
				if (null != connection) {
					try {
						connection.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} finally {
				if (null != resultSet) {
					try {
						resultSet.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				if (null != statement) {
					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				if (null != connection) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			String name = "/Users/sundaa1/Desktop/out.csv";
			exportData(connection, name);
		});

		panel.add(save);
		this.add(panel, BorderLayout.SOUTH);
		this.pack();
		
	}

	private void exportData(Connection conn, String filename) {
		Statement stmt;
		String query;
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

			//For comma separated file
			query = "SELECT TeacherID,Duty_Date_Start,Duty_Date_End into OUTFILE  '" + filename +
					"' FIELDS TERMINATED BY ',' FROM IA_1.current_allocations t";
			stmt.executeQuery(query);

		} catch (Exception e) {
			e.printStackTrace();
			stmt = null;
		}
	}

	public void attachModel(EventDateModel model, LocalDate date){
		this.model = model;
		this.date = date;
	}
	
	public Event.Time extractTime(String line){
		int colon = line.indexOf(":");
		int hour = Integer.parseInt(line.substring(0, colon));
		int min = Integer.parseInt(line.substring(colon+1,colon+3));
		boolean am = line.substring(colon+3).equalsIgnoreCase("am");
		
		return new Event.Time(hour,min,am);
	}
	
	
	
}
