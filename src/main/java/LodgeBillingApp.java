import controller.BillingController;
import javafx.application.Application;
import javafx.stage.Stage;

public class LodgeBillingApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        new BillingController(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}