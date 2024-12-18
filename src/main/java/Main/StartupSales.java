package Main;

import javafx.application.Application;
import javafx.stage.Stage;
import Controladores.Vistas.CambiosVistas;
import DB.JDBC;

public class StartupSales extends Application {
    private final CambiosVistas cambiosVistas = new CambiosVistas();

    @Override
    public void start(Stage stage) {
        cambiosVistas.cargarVista(stage, "/Vistas/PantallaPrincipal/View-PantallaPrincipal.fxml", "Bienvenido a StartupSales!", 854, 503);
    }

    public static void main(String[] args) {
        JDBC.ConectarBD();
        launch();
    }
}