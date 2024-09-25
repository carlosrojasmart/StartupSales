package Controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.io.IOException;

public class paginaPrincipal {
    @FXML
    private TextField buscarProductos;

    @FXML
    private Button crearCuentaBoton;

    @FXML
    private Button ingresarBoton;

    @FXML
    private Button misComprasBoton;

    @FXML
    private ImageView carritoCompras;

    @FXML
    private ImageView deporteImagen;

    @FXML
    private ImageView hogarImagen;
    @FXML
    private ImageView joyasImagen;

    @FXML
    private ImageView modaImagen;

    @FXML
    private ImageView tecnologiaImagen;


    @FXML

    private void mostrarCrearCuenta() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/View-Register.fxml")); // Asegúrate de usar la ruta correcta
            Stage stage = (Stage) crearCuentaBoton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML

    private void mostrarLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/View-Login.fxml")); // Asegúrate de usar la ruta correcta
            Stage stage = (Stage) ingresarBoton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarMisCompras() {
        // Implementa el cambio de vista para "Mis Compras"
        // Similar a los métodos anteriores
    }

}
