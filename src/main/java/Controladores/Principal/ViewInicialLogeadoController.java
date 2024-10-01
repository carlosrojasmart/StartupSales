package Controladores.Principal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ViewInicialLogeadoController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnComprasAr;

    @FXML
    private ImageView usuarioIcono;

    @FXML
    private Button BtnMisTiendas;

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> {
            // Limpiar el campo de búsqueda al hacer clic
            buscarProductos.clear();
        });

        usuarioIcono.setOnMouseClicked(event -> {
            // Lógica para manejar el clic en el icono de usuario
            irAVistaUsuario(); // Método para cambiar a la vista de usuario
        });

        carritoCompra.setOnMouseClicked(event -> {
            irAVistaCarrito();
        });
    }

    private void irAVistaUsuario() {
        try {
            // Cargar la nueva vista de usuario
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/PantallaCuenta/View-MiPerfil.fxml"));
            Stage stage = (Stage) usuarioIcono.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void mostrarMisTiendas() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/PantallaCuenta/View-Tienda.fxml")); // Asegúrate de usar la ruta correcta
            Stage stage = (Stage) BtnMisTiendas.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML

    private void mostrarCompras() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/PantallaCuenta/View-Compras.fxml")); // Asegúrate de usar la ruta correcta
            Stage stage = (Stage) BtnComprasAr.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
