import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;


import static javax.swing.JOptionPane.getRootFrame;
import static javax.swing.JOptionPane.showMessageDialog;


public class addTeacherDialog extends JDialog {
    private JTextField firstname = new JTextField("First Name");
    private JTextField lastname = new JTextField("Last Name");
    private JTextField subject = new JTextField("Subject");
    private EventDateModel model;
    private LocalDate date;

    public addTeacherDialog(JFrame owner){
        super(owner, "Create Event", true);
        JPanel panel = new JPanel();
        panel.add(firstname);
        panel.add(lastname);
        panel.add(subject);

        JButton save = new JButton("Save");
        save.addActionListener(event->{
            String fName = firstname.getText().toString();
            String lname = lastname.getText().toString();
            String sub = subject.getText().toString();
            addTeacher(fName, lname, sub);
            showMessageDialog(getRootFrame(), "Teacher Has Been Successfully Added!");
        });

        panel.add(save);
        this.add(panel, BorderLayout.SOUTH);
        this.pack();

    }

    public void addTeacher(String fname , String lname, String sub){
        Connection conn = null;
        String url = "jdbc:mysql://127.0.0.1:3306/Boarding"; //Database -> db_java
        String user = "root";
        String pass = "pavithra15";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection Successful");
            PreparedStatement ps = conn.prepareStatement("INSERT INTO teacher(LastName, FirstName, Subject) VALUES('"+lname+"'," +
                    "'"+fname+"','"+sub+"');");

            int result = ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error" + ex);
        }
        UserInputDialog.updateDrop();
    }


}