package cr.ac.una.unaplanilla;

import cr.ac.una.unaplanilla.model.Salonero;
import cr.ac.una.unaplanilla.util.FlowController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javafx.scene.image.Image;

/**
 * JavaFX App
 */
public class App extends Application {

    Salonero salonero;
    List<Salonero> listSalonero = new ArrayList<>();

    @Override
    public void start(Stage stage) throws IOException {
        FlowController.getInstance().InitializeFlow(stage, null);
        stage.getIcons().add(new Image(App.class.getResourceAsStream("/cr/ac/una/unaplanilla/resources/LogoUNArojo.png")));
        stage.setTitle("UNA PLANILLA");
        FlowController.getInstance().goViewInWindow("LogInView");
    }

    public void salonerosMetodo() {
        Predicate<Salonero> salonTemporal = x -> x.getTemporal();
    }

    public static void main(String[] args) {
        launch();
    }

}
