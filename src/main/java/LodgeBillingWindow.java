import controller.BillingController;
import javafx.application.Application;
import javafx.stage.Stage;

public class LodgeBillingWindow extends Application {
    @Override
    public void start(Stage primaryStage) {
        new BillingController(primaryStage);
    }
}
