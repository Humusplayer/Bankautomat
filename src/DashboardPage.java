import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import java.sql.*;

public class DashboardPage {
    private final Connection connection;
    private final String identNum;

    public DashboardPage(Connection connection, String identNum) {
        this.connection = connection;
        this.identNum = identNum;
    }

    public void showDashboard() {
        Stage dashboardStage = new Stage();
        dashboardStage.setTitle("Dashboard - " + getNameOfCustomer());
        dashboardStage.setMaximized(true);

        Image iconImage = new Image("images/MAIT_Background.png");
        dashboardStage.getIcons().add(iconImage);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        Label identLabel = new Label("Identifikationsnummer: " + identNum);
        Label balanceLabel = new Label("Kontostand: Laden...");
        balanceLabel.getStyleClass().add("balance-label");

        Button depositButton = new Button("Einzahlen(*)");
        Button withdrawButton = new Button("Abheben(*)");
        Button logoutButton = new Button("Abmelden");

        // Abfrage des Kontostands aus der Datenbank
        updateBalanceLabel(balanceLabel);

        depositButton.setOnAction(e -> {
            // Implementiere die Einzahlungslogik hier
        });

        withdrawButton.setOnAction(e -> {
            // Implementiere die Abhebungslogik hier
        });

        logoutButton.setOnAction(e -> {
            performLogoutAndExit(dashboardStage);
        });

        root.getChildren().addAll(identLabel, balanceLabel, depositButton, withdrawButton, logoutButton);

        dashboardStage.setOnCloseRequest(event -> {
            event.consume();
            performLogoutAndExit(dashboardStage);
        });

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("dashboardStyles.css");
        dashboardStage.setScene(scene);
        dashboardStage.show();
    }

    private String getNameOfCustomer() {
        try {
            String query = "SELECT p.Vorname, p.Nachname\n" +
                            "FROM personen p\n" +
                            "WHERE p.Identifikationsnummer = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, identNum);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String vorname = resultSet.getString("Vorname");
                String nachname = resultSet.getString("Nachname");

                String vollerName = vorname + " " + nachname;
                return vollerName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler in Verarbeitung");
        }
        return null;
    }

    private void performLogoutAndExit(Stage dashboardStage) {
        dashboardStage.close();
        Anmeldemaske anmeldemaske = new Anmeldemaske();
        Stage primaryStage = new Stage();
        anmeldemaske.start(primaryStage);
    }

    private void updateBalanceLabel(Label balanceLabel) {
        try {
            String query = "SELECT p.Identifikationsnummer, t.Geldmenge\n" +
                            "FROM personen p\n" +
                            "JOIN transaktionen t ON p.PersonID = t.PersonID\n" +
                            "WHERE p.Identifikationsnummer = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, identNum);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double balance = resultSet.getDouble("Geldmenge");
                balanceLabel.setText("Kontostand: " + balance + " EUR");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            balanceLabel.setText("Fehler beim Laden des Kontostands");
        }
    }
}