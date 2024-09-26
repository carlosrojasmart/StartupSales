package Controladores.Principal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ViewInicialLogeadoController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private Button misComprasBoton;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private ImageView usuarioIcono;

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> {
            // Limpiar el campo de búsqueda al hacer clic
            buscarProductos.clear();
        });

        misComprasBoton.setOnAction(event -> {
            // Lógica para manejar el botón "Mis compras"
            System.out.println("Ir a Mis Compras");
            // Aquí podrías implementar la navegación a otra vista
        });

        usuarioIcono.setOnMouseClicked(event -> {
            // Lógica para manejar el clic en el icono de usuario
            irAVistaUsuario(); // Método para cambiar a la vista de usuario
        });
    }

    private void irAVistaUsuario() {
        try {
            // Cargar la nueva vista de usuario
            Parent root = FXMLLoader.load(getClass().getResource("/Vistas/PantallaCuenta/View-MiPerfil.fxml"));
            Stage stage = (Stage) usuarioIcono.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
