package Controladores.Principal;

import Servicios.CambiosVistas;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ViewPaginaPrincipal {
    @FXML
    private TextField buscarProductos;

    @FXML
    private Button crearCuentaBoton;

    @FXML
    private Button ingresarBoton;

    @FXML
    private ImageView carritoCompra;

    private CambiosVistas cambiosVistas = new CambiosVistas();

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> {
            // Limpiar el campo de búsqueda al hacer clic
            buscarProductos.clear();
        });

        carritoCompra.setOnMouseClicked(event -> {
            mostrarCarrito();
        });
    }

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    @FXML
    public void mostrarCarrito() {
        cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");
    }

    @FXML
    private void mostrarCrearCuenta() {
        cambiarVista(crearCuentaBoton, "/Vistas/PantallaSesion/View-Register.fxml");
    }

    @FXML
    private void mostrarLogin() {
        cambiarVista(ingresarBoton, "/Vistas/PantallaSesion/View-Login.fxml");
    }

}
