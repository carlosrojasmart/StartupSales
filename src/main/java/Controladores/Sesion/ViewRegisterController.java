package Controladores.Sesion;

import Controladores.Vistas.CambiosVistas;
import Servicios.Datos.LoginRegisterService;
import Repositorios.Datos.LoginRegister;
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
    private Label lblMensaje;

    private CambiosVistas cambiosVistas = new CambiosVistas();

    private LoginRegisterService loginRegisterService = new LoginRegisterService(new LoginRegister());

    @FXML
    public void initialize() {
        //Inicializa el boton de login
        btnLogin.setOnAction(event -> registrarUsuario());
    }


    //Registro de usuario
    @FXML
    private void registrarUsuario() {
        //Hace get de el User
        String usuario = txtUser.getText();
        //Hace get del correo
        String correo = txtCorreoElectronico.getText();
        //Hace get de la contraseña
        String contraseña = txtContraseña.getText();
        //Hace get del telefono
        String telefono = txtTelefono.getText();
        //Hace get de la direccion
        String direccion = txtDireccion.getText();

        //Usa el servicio para registrar el usuario y mostrarle si su login fue exitoso o hubo un error
        boolean exito = loginRegisterService.registrarUsuario(usuario, correo, contraseña, telefono, direccion);
        if (exito) {
            mostrarMensaje("Usuario registrado exitosamente.");
            limpiarCampos();
            mostrarLogin();
        } else {
            mostrarMensaje("Error al registrar usuario.");
        }
    }

    //Limpia los campos de register para el usuario
    private void limpiarCampos() {
        txtUser.clear();
        txtCorreoElectronico.clear();
        txtContraseña.clear();
        txtTelefono.clear();
        txtDireccion.clear();
    }

    //Muestra los mensajes a usuario
    private void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje); // Muestra el mensaje en el Label
    }

    //Cambio de vista general
    private void cambiarVista(Node nodo, String rutaFXML) {
        //Inicializa el stage usando el nodo para cambio de vista
        Stage stage = (Stage) nodo.getScene().getWindow();
        //Usa CambiosVista para realizar el cambio usando el stage y la ruta del fxml
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    //Cambio de vista a login
    @FXML
    private void mostrarLogin() {cambiarVista(ingresarBoton, "/Vistas/PantallaSesion/View-Login.fxml");}

    //Cambio de vista a volver a inicio
    @FXML
    private void volverVistaInicial() {cambiarVista(btnSeguirSinCuenta, "/Vistas/PantallaPrincipal/View-PantallaPrincipal.fxml");
    }
}
