package Controladores.Principal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class paginaPrincipal {
    @FXML
    private TextField buscarProductos;

    @FXML
    private Button crearCuentaBoton;

    @FXML
    private Button ingresarBoton;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> {
            // Limpiar el campo de búsqueda al hacer clic
            buscarProductos.clear();
        });

        //carritoCompra.setOnMouseClicked(event -> {
        //    irAVistaCarrito();
        //});
    }


    private void irAVistaCarrito() {
        try {
            // Cargar la nueva vista de usuario
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/PantallaPrincipal/View-CarritoCompras.fxml"));
            Stage stage = (Stage) carritoCompra.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML

    private void mostrarCrearCuenta() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/PantallaSesion/View-Register.fxml")); // Asegúrate de usar la ruta correcta
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
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/PantallaSesion/View-Login.fxml")); // Asegúrate de usar la ruta correcta
            Stage stage = (Stage) ingresarBoton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
