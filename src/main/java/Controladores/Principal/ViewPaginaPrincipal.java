package Controladores.Principal;

import Controladores.Vistas.CambiosVistas;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ViewPaginaPrincipal {

    @FXML
    private Button crearCuentaBoton;

    @FXML
    private Button ingresarBoton;

    private CambiosVistas cambiosVistas = new CambiosVistas();

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
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
