package Controladores.Sesion;

import Controladores.Vistas.CambiosVistas;
import Servicios.Datos.LoginRegisterService;
import Repositorios.Datos.LoginRegister;
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

    private LoginRegisterService loginRegisterService = new LoginRegisterService(new LoginRegister());

    @FXML
    public void initialize() {
        //Inicializa el boton de login
        btnLogin.setOnAction(event -> handleLogin());
    }

    //Realiza el login de usuario
    @FXML
    private void handleLogin() {
        //Hace get del user del usuario
        String username = txtUser.getText().trim();
        //Hace get del password del usuario
        String password = txtPassword.getText().trim();

        //Usa el servicio para completar login del usuario
        boolean loginExitoso = loginRegisterService.handleLogin(username, password);

        //Mensajes de exito o error a usuario
        if (loginExitoso) {
            mostrarMensaje("Login exitoso.");
            volverVistaInicialLogeado();
        } else {
            mostrarMensaje("Error en el inicio de sesión.");
        }
    }

    //Muestra los mensajes a el usuario
    private void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje); // Mostrar el mensaje en el Label
    }

    //Cambio de vista general
    private void cambiarVista(Node nodo, String rutaFXML) {
        //Inicializa el stage usando el nodo para cambio de vista
        Stage stage = (Stage) nodo.getScene().getWindow();
        //Usa CambiosVista para realizar el cambio usando el stage y la ruta del fxml
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    //Cambio de vista a Crear Cuenta
    @FXML
    private void mostrarCrearCuenta() {cambiarVista(btnCrearUnaCuenta, "/Vistas/PantallaSesion/View-Register.fxml");}

    //Cambio de vista a vovler a inicio
    @FXML
    private void volverVistaInicial() {cambiarVista(btnSeguirSinCuenta, "/Vistas/PantallaPrincipal/View-PantallaPrincipal.fxml");}

    //Cambio de vista inicial logeado
    @FXML
    private void volverVistaInicialLogeado() {cambiarVista(btnLogin, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");}
}
