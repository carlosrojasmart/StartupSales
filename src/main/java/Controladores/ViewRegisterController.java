package Controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private PasswordField txtTelefono;

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

    @FXML
    public void initialize() {
        // Configurar el botón para que ejecute la acción de registro
        btnLogin.setOnAction(event -> registrarUsuario());
    }

    private void registrarUsuario() {
        // Obtener los valores ingresados por el usuario
        String usuario = txtUser.getText();
        String correo = txtCorreoElectronico.getText();
        String contraseña = txtContraseña.getText();
        String telefono = txtTelefono.getText();
        String direccion = txtDireccion.getText();

        // Validación de campos
        if (usuario.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            mostrarMensaje("Por favor, completa todos los campos.");
            return;
        }

        // Aquí puedes añadir la lógica para almacenar los datos del usuario
        // Por ejemplo, guardar en una base de datos o en un archivo

        // Simulación de éxito
        mostrarMensaje("Usuario registrado exitosamente.");
        // Limpiar campos después del registro
        limpiarCampos();
    }

    private void limpiarCampos() {
        txtUser.clear();
        txtCorreoElectronico.clear();
        txtContraseña.clear();
        txtTelefono.clear();
        txtDireccion.clear();
    }

    private void mostrarMensaje(String mensaje) {
        // Aquí puedes mostrar el mensaje de alguna forma, por ejemplo en un Label
        System.out.println(mensaje); // Solo para propósitos de depuración
        // Si tienes un Label en tu FXML para mostrar mensajes, puedes usarlo así:
        // lblMensaje.setText(mensaje);
    }

    @FXML

    private void mostrarLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/View-Login.fxml")); // Asegúrate de usar la ruta correcta
            Stage stage = (Stage) ingresarBoton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML

    private void volverVistaInicial() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/pantalla-principal.fxml"));
            Stage stage = (Stage) btnSeguirSinCuenta.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
