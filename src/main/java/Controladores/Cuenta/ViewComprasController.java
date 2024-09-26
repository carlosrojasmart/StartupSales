package Controladores.Cuenta;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ViewComprasController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private Button misComprasAr;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private ImageView usuarioIcono;

    @FXML
    private Button btnMiCuenta;
    @FXML
    private Button BtnVolverInicio;

    @FXML
    private Button BtnMiPerfil;

    @FXML
    private Button BtnCompras;

    @FXML
    private Button BtnTienda;

    @FXML
    private Button BtnFacturacion;

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

    private void mostrarMiPerfil() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/PantallaCuenta/View-MiPerfil.fxml")); // Asegúrate de usar la ruta correcta
            Stage stage = (Stage) BtnMiPerfil.getScene().getWindow();
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
