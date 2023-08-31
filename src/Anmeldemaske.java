import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import java.sql.*;

//Zeichen und Bedeutungen
//(*) -> Keine Funktion Hinterlegt
//(-) -> Hat noch Debug Funktionen

public class Anmeldemaske extends Application {
    private Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Lade SQL Treiber
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        primaryStage.setTitle("Anmeldung");
        primaryStage.setMaximized(true);

        Image iconImage = new Image("images/MAIT_Background.png");
        primaryStage.getIcons().add(iconImage);

        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("images/MAIT_Background.png"),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
        Background background = new Background(backgroundImage);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        //root.setBackground(background);

        Label titleLabel = new Label("Anmelden");
        titleLabel.getStyleClass().add("title-label");

        TextField identNumField = new TextField();
        identNumField.setPromptText("Identifikationsnummer");
        identNumField.getStyleClass().add("input-field");
        identNumField.setMaxWidth(200);

        PasswordField pinField = new PasswordField();
        pinField.setPromptText("PIN");
        pinField.getStyleClass().add("input-field");
        pinField.setMaxWidth(200);

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);

        Button loginButton = new Button("Einloggen(-)");
        loginButton.getStyleClass().add("login-button");

        loginButton.setOnAction(e -> {
            String identNum = identNumField.getText();
            String pin = pinField.getText();
            if (validateLogin(identNum, pin)) {
                System.out.println("Einloggen erfolgreich");//Debug
                errorLabel.setText("");

                DashboardPage dashboardPage = new DashboardPage(connection, identNum);
                dashboardPage.showDashboard();

                primaryStage.close();
            } else {
                System.out.println("Einloggen fehlgeschlagen");//Debug
                errorLabel.setText("Falsche Eingabe");
            }
        });

        Button newAccountButton = new Button("Neues Konto hinzufügen(*)");
        newAccountButton.getStyleClass().add("new-account-button");

        newAccountButton.setOnAction(e -> {
            NeuesKontoAnlegen neuesKontoAnlegen = new NeuesKontoAnlegen(connection);
            neuesKontoAnlegen.show();
            primaryStage.close();
        });

        root.getChildren().addAll(titleLabel, identNumField, pinField, errorLabel, loginButton, newAccountButton);


        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginButton.fire();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean validateLogin(String identNum, String pin) {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Bankautomat_Neu_Neu", "Connector", "jade");
            }

            String query = "SELECT p.* FROM Personen p " +
                            "JOIN Pins pin ON p.PersonID = pin.PersonID " +
                            "WHERE p.Identifikationsnummer = ? AND pin.Pin = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, identNum);
            preparedStatement.setString(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}