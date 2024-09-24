package Controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ViewLoginController {
    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

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
}
