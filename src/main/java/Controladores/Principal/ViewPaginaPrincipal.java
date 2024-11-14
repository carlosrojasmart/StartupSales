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

    //Cambio de vista general
    private void cambiarVista(Node nodo, String rutaFXML) {
        //Inicializa el stage usando el nodo para cambio de vista
        Stage stage = (Stage) nodo.getScene().getWindow();
        //Usa CambiosVista para realizar el cambio usando el stage y la ruta del fxml
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    //Cambio de vista a Crear Cuenta
    @FXML
    private void mostrarCrearCuenta() {
        cambiarVista(crearCuentaBoton, "/Vistas/PantallaSesion/View-Register.fxml");
    }

    //Cambio de vista a login
    @FXML
    private void mostrarLogin() {
        cambiarVista(ingresarBoton, "/Vistas/PantallaSesion/View-Login.fxml");
    }

}
