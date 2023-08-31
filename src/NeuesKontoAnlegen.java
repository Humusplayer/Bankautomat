import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.sql.Connection;

public class NeuesKontoAnlegen {
    private final Connection connection;

    public NeuesKontoAnlegen(Connection connection){
        this.connection = connection;
    }

    public void show() {
        Stage newCustomerStage = new Stage();
        newCustomerStage.setTitle("Neues Konto anlegen");
        newCustomerStage.setMaximized(true);

        Image iconImage = new Image("images/MAIT_Background.png");
        newCustomerStage.getIcons().add(iconImage);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Neues Konto anlegen");
        titleLabel.getStyleClass().add("title-label");

        TextField vornameField = new TextField();
        vornameField.setPromptText("Vorname");
        vornameField.getStyleClass().add("input-field");
        vornameField.setMaxWidth(200);

        TextField nachnameField = new TextField();
        nachnameField.setPromptText("Nachname");
        nachnameField.getStyleClass().add("input-field");
        nachnameField.setMaxWidth(200);

        PasswordField pinField = new PasswordField();
        pinField.setPromptText("PIN");
        pinField.getStyleClass().add("input-field");
        pinField.setMaxWidth(200);

        PasswordField pinBestaetigenField = new PasswordField();
        pinBestaetigenField.setPromptText("PIN bestätigen");
        pinBestaetigenField.getStyleClass().add("input-field");
        pinBestaetigenField.setMaxWidth(200);

        TextField startkapitalField = new TextField();
        startkapitalField.setPromptText("Startkapital");
        startkapitalField.getStyleClass().add("input-field");
        startkapitalField.setMaxWidth(200);

        Button anlegenButton = new Button("Neues Konto anlegen");
        anlegenButton.getStyleClass().add("login-button");

        anlegenButton.setOnAction(e -> {

        });

        newCustomerStage.setOnCloseRequest(event -> {
            event.consume();
            performExit(newCustomerStage);
        });

        root.getChildren().addAll(
                titleLabel, vornameField, nachnameField, pinField, pinBestaetigenField, startkapitalField, anlegenButton
        );

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        scene.setFill(Color.BLACK);

        newCustomerStage.setScene(scene);
        newCustomerStage.show();
    }

    private void performExit(Stage newCustomerStage) {
        newCustomerStage.close();
        Anmeldemaske anmeldemaske = new Anmeldemaske();
        Stage primaryStage = new Stage();
        anmeldemaske.start(primaryStage);
    }
}
