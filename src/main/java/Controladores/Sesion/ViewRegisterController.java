package Controladores.Sesion;

import Servicios.Vistas.CambiosVistas;
import Servicios.Datos.LoginRegister;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ViewRegisterController {
    @FXML
    private TextField txtUser;

    @FXML
    private TextField txtCorreoElectronico;

    @FXML
    private PasswordField txtContraseña;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtDireccion;

    @FXML
    private Button btnLogin;

    @FXML
    private Button ingresarBoton;

    @FXML
    private Button btnSeguirSinCuenta;


    @FXML
    private Label lblMensaje; // Para mostrar mensajes de éxito o error

    private CambiosVistas cambiosVistas = new CambiosVistas();

    private LoginRegister loginRegister = new LoginRegister();

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    @FXML
    public void initialize() {
        // Configurar el botón para que ejecute la acción de registro
        btnLogin.setOnAction(event -> registrarUsuario());
    }

    @FXML
    private void registrarUsuario() {
        String usuario = txtUser.getText();
        String correo = txtCorreoElectronico.getText();
        String contraseña = txtContraseña.getText();
        String telefono = txtTelefono.getText();
        String direccion = txtDireccion.getText();

        loginRegister.registrarUsuario(usuario, correo, contraseña, telefono, direccion, new LoginRegister.RegistrationCallback() {
            @Override
            public void onSuccess(String message) {
                mostrarMensaje(message);
                limpiarCampos(); // Llama a limpiar los campos después de registrar
                mostrarLogin(); // Muestra la vista de login
            }

            @Override
            public void onFailure(String errorMessage) {
                mostrarMensaje(errorMessage);
            }
        });
    }

    private void limpiarCampos() {
        txtUser.clear();
        txtCorreoElectronico.clear();
        txtContraseña.clear();
        txtTelefono.clear();
        txtDireccion.clear();
    }

    private void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje); // Muestra el mensaje en el Label
    }


    @FXML
    private void mostrarLogin() {
        cambiarVista(ingresarBoton, "/Vistas/PantallaSesion/View-Login.fxml");
    }

    @FXML
    private void volverVistaInicial() {
        cambiarVista(btnSeguirSinCuenta, "/Vistas/PantallaPrincipal/View-PantallaPrincipal.fxml");
    }
}
