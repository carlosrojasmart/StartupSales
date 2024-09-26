package Controladores.Sesion;

import DB.JDBC;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    @FXML
    public void initialize() {
        btnLogin.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String username = txtUser.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Por favor, completa todos los campos.");
            return;
        }

        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "SELECT contraseña FROM Usuario WHERE correo_electronico = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String storedPassword = rs.getString("contraseña");

                    if (storedPassword.equals(password)) {
                        mostrarMensaje("Login exitoso.");
                        volverVistaInicialLogeado(); //Vuelve a inicial
                    } else {
                        mostrarMensaje("Contraseña incorrecta.");
                    }
                } else {
                    mostrarMensaje("Correo no existe.");
                }
            }
        } catch (SQLException e) {
            mostrarMensaje("Error de conexión a la base de datos: " + e.getMessage());
        }
    }


    private void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje); // Mostrar el mensaje en el Label
    }

    @FXML

    private void mostrarCrearCuenta() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/PantallaSesion/View-Register.fxml"));
            Stage stage = (Stage) btnCrearUnaCuenta.getScene().getWindow();
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

    @FXML

    private void volverVistaInicialLogeado() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/PantallaPrincipal/View-InicialLogeado.fxml"));
            Stage stage = (Stage) btnLogin.getScene().getWindow(); // Cambia esto según el botón que desees usar
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
