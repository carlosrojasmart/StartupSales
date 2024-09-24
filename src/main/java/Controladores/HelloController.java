package Controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class HelloController {

    @FXML
    public MenuItem optSalir;
    @FXML
    public BorderPane contenedor;

    public HelloController() throws IOException {
    }

    @FXML
    public void salir() {
        System.exit(0);
    }

    @FXML
    public void iniciarSesion() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Vistas/View-Login.fxml"));
        contenedor.setCenter(pane);
    }


}