package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import DB.JDBC;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Vistas/pantalla-principal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 854.0, 503.0);
        stage.setTitle("Bienvenido a StartupSales!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        JDBC.ConectarBD(); // Conexión a la base de datos
        launch(); // Iniciar la aplicación JavaFX
    }
}