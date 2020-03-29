import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JComboBox;
import javax.xml.transform.Result;

public class MainCalendarScene extends JFrame implements ChangeListener{
    private CalendarModel calModel;
    private EventDateModel events;
    private int chosenButton;
    private LocalDate dateChosen;

    private JPanel calendarPanel;
    private JButton[] titleButtons;
    private JButton[] daybuttons;
    private JLabel monthYearLabel;
    private JButton createButton;
    private JButton swapButton;
    private addTeacherDialog teacherDialog;
    private swapTeacherDialog swapDialog;
    private UserInputDialog userinput;
    private DeleteDialog dialog3;
    private JPanel buttonPanel;
    private JButton deleteButton;
    private JButton addTeacherButton;


    private JPanel eventPanel;
    private JTextArea eventArea;
    private JLabel dayLabel;

    private JPanel navigationPanel1;
    private JPanel navigationPanel2;
    private JButton next;
    private JButton pre;
    private JButton quit;


    public MainCalendarScene(CalendarModel model, EventDateModel model2){
        calModel = model;
        events = model2;
        dateChosen = calModel.getDateNow();

        //////////////////////////////
        //Setting up navigation panel

        next = new JButton("Next");
        pre = new JButton("Pre");
        quit = new JButton("Quit");

        //navigate to next month
        next.addActionListener(navigateMonth(1));
        pre.addActionListener(navigateMonth(-1));
        quit.addActionListener(event->{
            events.write("events.txt");
            System.exit(0);
        });
        createButton = new JButton("Assign Duty Date");
        createButton.addActionListener(event->{
            if(userinput==null){
                userinput = new UserInputDialog(MainCalendarScene.this);
                userinput.attachModel(events, dateChosen);
            }
            userinput.attachModel(events, dateChosen);
            userinput.setVisible(true);
        });
        addTeacherButton = new JButton("Add Teacher");
        addTeacherButton.addActionListener(event->{
            if(teacherDialog==null){
                teacherDialog = new addTeacherDialog(MainCalendarScene.this);
            }
            teacherDialog.setVisible(true);
        });
        swapButton = new JButton("Swap Duty Dates");
        swapButton.addActionListener(event->{
            if(swapDialog==null){
                swapDialog = new swapTeacherDialog(MainCalendarScene.this);
            }
            swapDialog.setVisible(true);
        });

        //adding buttons to navigation panel
        navigationPanel1 = new JPanel();
        navigationPanel1.add(pre);
        navigationPanel1.add(next);
        navigationPanel1.add(quit);
        navigationPanel2 = new JPanel();
        navigationPanel2.add(createButton);
        navigationPanel2.add(addTeacherButton);

        //add navigation panel to frame
        this.add(navigationPanel1, BorderLayout.NORTH);
        //this.add(navigationPanel2, BorderLayout.NORTH);

        //////////////////////////////////
        //setting up event panel
        eventArea = new JTextArea(20,20);
        eventArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(eventArea);
        dayLabel = new JLabel(this.getDateString(calModel.getDateNow()));

        eventPanel = new JPanel();
        eventPanel.setLayout(new BoxLayout (eventPanel, BoxLayout.Y_AXIS));
        eventPanel.add(dayLabel);
        eventPanel.add(scrollPane);

        this.add(eventPanel, BorderLayout.EAST);

        ///////////////////////////////////////////
        //setting up calendar panel


        //button for adding a new teacher


        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(event->{
            if(dialog3==null){
                dialog3 = new DeleteDialog(MainCalendarScene.this,events, dateChosen);
                dialog3.attachModel2(events, dateChosen);
            }
            dialog3.attachModel2(events, dateChosen);
            dialog3.setVisible(true);
        });


        monthYearLabel = new JLabel(this.getMonthYear(calModel.getDateNow()));

        //set up button panel and attach listener
        this.setupButtonPanel();

        calendarPanel = new JPanel();
        calendarPanel.setLayout(new BoxLayout (calendarPanel, BoxLayout.Y_AXIS));
        calendarPanel.add(deleteButton);
        calendarPanel.add(createButton);
        calendarPanel.add(swapButton);
        calendarPanel.add(addTeacherButton);
        calendarPanel.add(monthYearLabel);
        calendarPanel.add(buttonPanel);


        this.add(calendarPanel, BorderLayout.WEST);

        //general frame setting

        this.pack();

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public String getDateString(LocalDate date){
        String line = date.getDayOfWeek() + " "+date.getMonthValue()+"/"+date.getDayOfMonth();
        return line;
    }

    public String getMonthYear(LocalDate date){
        String line = date.getMonth()+" "+date.getYear();
        return line;
    }

    private void setupButtonPanel(){
        String[] row = calModel.getRowData();

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 7));

        titleButtons = new JButton[7];

        //drawing titles
        String[] title = new String[]{"S","M","T","W","T","F","S"};
        for(int i = 0; i < titleButtons.length; i++){
            titleButtons[i] = new JButton(title[i]);
            buttonPanel.add(titleButtons[i]);
        }


        daybuttons = new JButton[row.length];
        for(int i = 0; i < row.length; i++){
            daybuttons[i] = new JButton(row[i]);
            buttonPanel.add(daybuttons[i]);
            String currentDate = calModel.getDateNow().getDayOfMonth()+"";
            daybuttons[i].addActionListener(dayButtonClicked(i));
            if(row[i].equals(currentDate)){
                daybuttons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                chosenButton = i;
            }
        }


    }

    @Override
    public void stateChanged(ChangeEvent e) {
        String updated = events.search(dateChosen);
        eventArea.setText(updated);
    }

    public ActionListener dayButtonClicked(int i){
        return new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate day = calModel.getRequestedDay();
                String dayNumber = daybuttons[i].getText();
                if(!dayNumber.trim().equals("")){
                    LocalDate date = LocalDate.of(day.getYear(),
                            day.getMonthValue(), Integer.parseInt(dayNumber));
                    dateChosen = date;

                    System.out.println("Button clicked "+dateChosen);


                    dayLabel.setText(getDateString(date));
                    //search for the EventDate
                    String result = events.search(date);
                    if(!result.equals(""))
                        eventArea.setText(result);
                    else
                        eventArea.setText("");
                    Connection conn = null;
                    String url = "jdbc:mysql://127.0.0.1:3306/Boarding"; //Database -> db_java
                    String user = "root";
                    String pass = "pavithra15";
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        conn = DriverManager.getConnection(url, user, pass);
                        System.out.println("Connection Successful");
                        PreparedStatement ps = conn.prepareStatement("SELECT * FROM current_allocations WHERE DUTY_DATE_START='"+dateChosen+"' OR DUTY_DATE_END='"+dateChosen+"'");

                        ResultSet result1 = ps.executeQuery();
                        while(result1.next()){
                            String teacherID = result1.getString("TeacherID");
                            PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM teacher WHERE TeacherID='"+teacherID+"'");
                            ResultSet result2 = ps1.executeQuery();
                            result2.next();
                            String teacherName = result2.getString("" +
                                    "FirstName")+ " "+result2.getString("LastName");
                            eventArea.append("\n"+teacherName);



                        }
                    } catch (Exception ex) {
                        System.out.println("Error" + ex);
                    }

                }
            }

        };

    }


    public ActionListener navigateMonth(int nav){
        return new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nav == -1)
                    calModel.previousMonth();
                else calModel.nextMonth();

                //reset the button
                Border border = (new JButton()).getBorder();
                daybuttons[chosenButton].setBorder(border);

                String[] row = calModel.getRowData();

                LocalDate current = calModel.getRequestedDay();
                LocalDate now = calModel.getDateNow();

                for(int i = 0; i < daybuttons.length; i++){
                    daybuttons[i].setText(row[i]);
                    if(current.getMonthValue()==now.getMonthValue() &&
                            row[i].equals(now.getDayOfMonth()+""))
                        daybuttons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                }

                String text = current.getMonth() +" "+current.getYear();
                monthYearLabel.setText(text);

                repaint();
            }


        };
    }


}

