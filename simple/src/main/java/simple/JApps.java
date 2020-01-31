package simple;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JApps extends Application {

    private static final Logger logger = Logger.getLogger(JApps.class.getName());
    private UserDao userRepository = new UserDao();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Welcome");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Add Duty Date");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userNameLabel = new Label("TeacherID:");
        grid.add(userNameLabel, 0, 1);

        TextField TeacherIDTextField = new TextField();
        grid.add(TeacherIDTextField, 1, 1);

        Label Duty_Date_StartLabel = new Label("Duty Date Start:");
        grid.add(Duty_Date_StartLabel, 0, 2);

        TextField Duty_Date_StartTextField = new TextField();
        grid.add(Duty_Date_StartTextField, 1, 2);

        Label Duty_Date_EndLabel = new Label("Duty Date End:");
        grid.add(Duty_Date_EndLabel, 0, 3);

        TextField Duty_Date_EndTextField = new TextField();
        grid.add(Duty_Date_EndTextField, 1, 3);

        Button saveButton = new Button("Save");
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(saveButton);
        grid.add(hBox, 1, 5);

        saveButton.setOnAction(actionEvent -> {
            String TeacherID = TeacherIDTextField.getText().trim();
            String Duty_Date_Start = Duty_Date_StartTextField.getText().trim();
            String Duty_Date_End = Duty_Date_EndTextField.getText().trim();
            if (!StringPool.BLANK.equals(TeacherID) && !StringPool.BLANK.equals(Duty_Date_Start)
                    && !StringPool.BLANK.equals(Duty_Date_End)) {
                try {
                    if (!userRepository.userExists(TeacherID)) {
                        User user = this.createUserObject(TeacherID, Duty_Date_Start, Duty_Date_End);
                        int userId = userRepository.saveUser(user);
                        if (userId > 0) {
                            this.alert("Save", "Successful!", AlertType.INFORMATION);

                        } else {
                            this.alert("Error", "Failed!", AlertType.ERROR);
                        }
                    } else {
                        this.alert("Error", "User already exists!", AlertType.ERROR);
                    }
                } catch (Exception exception) {
                    logger.log(Level.SEVERE, exception.getMessage());
                }
            } else {
                this.alert("Error", "Please complete fields!", AlertType.ERROR);
            }
        });

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public void alert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public User createUserObject(String TeacherID, String Duty_Date_Start, String Duty_Date_End) {
        User user = new User();
        user.setTeacherID(TeacherID);
        user.setLastName(Duty_Date_Start);
        user.setFirstName(Duty_Date_End);

        return user;
    }

    public static void main(String[] args) {
        launch(args);
    }

}