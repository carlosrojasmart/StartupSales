package Controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    public void initialize() {
        btnLogin.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String username = txtUser.getText();
        String password = txtPassword.getText();

        // Lógica para validar el inicio de sesión
        if (username.equals("admin") && password.equals("admin123")) {
            System.out.println("Login exitoso");
        } else {
            System.out.println("Login fallido");
        }
    }

    @FXML

    private void mostrarCrearCuenta() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/View-Register.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/pantalla-principal.fxml"));
            Stage stage = (Stage) btnSeguirSinCuenta.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
