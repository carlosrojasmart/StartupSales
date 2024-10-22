package Controladores.Sesion;

import Servicios.Vistas.CambiosVistas;
import Servicios.Datos.LoginRegister;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewLoginController {
    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnCrearUnaCuenta;

    @FXML
    private Button btnSeguirSinCuenta;

    @FXML
    private Label lblMensaje;

    private CambiosVistas cambiosVistas = new CambiosVistas();

    private LoginRegister loginRegister = new LoginRegister();

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    @FXML
    public void initialize() {
        btnLogin.setOnAction(event -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String username = txtUser.getText().trim();
        String password = txtPassword.getText().trim();

        loginRegister.handleLogin(username, password, new LoginRegister.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                mostrarMensaje(message);
                volverVistaInicialLogeado(); // Llama a tu método para volver a la vista inicial
            }

            @Override
            public void onFailure(String errorMessage) {
                mostrarMensaje(errorMessage);
            }
        });
    }


    private void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje); // Mostrar el mensaje en el Label
    }

    @FXML
    private void mostrarCrearCuenta() {
        cambiarVista(btnCrearUnaCuenta, "/Vistas/PantallaSesion/View-Register.fxml");
    }

    @FXML
    private void volverVistaInicial() {
        cambiarVista(btnSeguirSinCuenta, "/Vistas/PantallaPrincipal/View-PantallaPrincipal.fxml");
    }

    @FXML

    private void volverVistaInicialLogeado() {
        cambiarVista(btnLogin, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");
    }
}
