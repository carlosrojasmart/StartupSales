package Controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class HelloController {

    public HelloController() throws IOException {
    }

    @FXML
    public MenuItem optIniciarSesion;

    @FXML
    public void salir() {
        System.exit(0);
    }

    @FXML
    private BorderPane contenedor;





}