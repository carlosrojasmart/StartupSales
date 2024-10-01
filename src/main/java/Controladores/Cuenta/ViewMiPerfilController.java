package Controladores.Cuenta;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class ViewMiPerfilController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private ImageView usuarioIcono;

    @FXML
    private Button BtnMiPerfil;

    @FXML
    private Button BtnComprasAr;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private Button BtnCompras;

    @FXML
    private Button BtnTienda;

    @FXML
    private Button BtnFacturacion;

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> {
            // Limpiar el campo de búsqueda al hacer clic
            buscarProductos.clear();
        });

        carritoCompra.setOnMouseClicked(event -> {
            irAVistaCarrito();
        });
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

    private void mostrarInicio() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/PantallaPrincipal/View-InicialLogeado.fxml")); // Asegúrate de usar la ruta correcta
            Stage stage = (Stage) BtnVolverInicio.getScene().getWindow();
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
            Stage stage = (Stage) BtnCompras.getScene().getWindow();
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
            Stage stage = (Stage) BtnTienda.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML

    private void mostrarFacturacion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/PantallaCuenta/View-Facturacion.fxml")); // Asegúrate de usar la ruta correcta
            Stage stage = (Stage) BtnFacturacion.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
