package Controladores.Sesion;

import DB.JDBC;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

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

    @FXML
    public void initialize() {
        // Configurar el botón para que ejecute la acción de registro
        btnLogin.setOnAction(event -> registrarUsuario());
    }

    private void registrarUsuario() {
        // Obtener los valores ingresados por el usuario
        String usuario = txtUser.getText();
        String correo = txtCorreoElectronico.getText();
        String contraseña = txtContraseña.getText(); // Obtener la contraseña
        String telefono = txtTelefono.getText();
        String direccion = txtDireccion.getText();

        // Validación de campos
        if (usuario.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            mostrarMensaje("Por favor, completa todos los campos.");
            return;
        }

        // Generar un idUsuario aleatorio
        int idUsuario = generarIdAleatorio();

        // Conexión a la base de datos y ejecución de la consulta
        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "INSERT INTO Usuario (idUsuario, nombre, direccion, correo_electronico, telefono, idTienda, contraseña) VALUES (?, ?, ?, ?, ?, NULL, ?)";

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario); // idUsuario generado aleatoriamente
                pstmt.setString(2, usuario); // nombre
                pstmt.setString(3, direccion); // direccion
                pstmt.setString(4, correo); // correo_electronico
                pstmt.setString(5, telefono); // telefono
                pstmt.setString(6, contraseña); // contraseña

                // Ejecutar la inserción
                pstmt.executeUpdate();
                mostrarMensaje("Usuario registrado exitosamente.");
                limpiarCampos();
                mostrarLogin(); //Muestra login
            } catch (SQLException e) {
                mostrarMensaje("Error al registrar usuario: " + e.getMessage());
            }
        } catch (SQLException e) {
            mostrarMensaje("Error de conexión a la base de datos: " + e.getMessage());
        }
    }


    private int generarIdAleatorio() {
        // Generar un número aleatorio de 6 dígitos como idCliente
        Random random = new Random();
        return random.nextInt(900000) + 100000; // Genera un número entre 100000 y 999999
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
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/PantallaSesion/View-Login.fxml")); // Asegúrate de usar la ruta correcta
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
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/PantallaPrincipal/pantalla-principal.fxml"));
            Stage stage = (Stage) btnSeguirSinCuenta.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
