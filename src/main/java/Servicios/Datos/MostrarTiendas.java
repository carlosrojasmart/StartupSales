package Servicios.Datos;

import Controladores.Cuenta.Tienda.ViewMirarTiendaController;
import DB.JDBC;
import Modelos.Tienda;
import Servicios.Vistas.CambiosVistas;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class MostrarTiendas {

    // Método para obtener las tiendas desde la base de datos
    public List<Tienda> obtenerTiendas(int idUsuario) {
        List<Tienda> tiendas = new ArrayList<>();
        String sql = "SELECT idTienda, nombre, descripcion, categoria, imagenTienda FROM Tienda WHERE idUsuario = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idTienda = rs.getInt("idTienda");
                    String nombre = rs.getString("nombre");
                    String descripcion = rs.getString("descripcion");
                    String categoria = rs.getString("categoria");
                    byte[] imagen = rs.getBytes("imagenTienda");
                    tiendas.add(new Tienda(idTienda, nombre, descripcion, categoria, imagen));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tiendas;
    }

    public VBox crearVistaTiendas(List<Tienda> tiendas) {
        VBox contenedorTiendas = new VBox();
        contenedorTiendas.setSpacing(15); // Ajusta el espacio entre cada tienda
        contenedorTiendas.setPadding(new Insets(10, 20, 10, 20)); // Agrega un margen para que no estén pegadas al borde

        for (Tienda tienda : tiendas) {
            HBox tiendaBox = new HBox();
            tiendaBox.setSpacing(15);
            tiendaBox.setPadding(new Insets(10, 10, 10, 10)); // Margen interno para cada tienda
            tiendaBox.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #DDDDDD; -fx-border-radius: 5; -fx-background-radius: 5;");

            ImageView imagenTienda = new ImageView();
            imagenTienda.setFitHeight(100);
            imagenTienda.setFitWidth(100);
            if (tienda.getImagen() != null) {
                Image image = new Image(new ByteArrayInputStream(tienda.getImagen()));
                imagenTienda.setImage(image);
            }

            Label nombreTienda = new Label(tienda.getNombre());
            nombreTienda.setStyle("-fx-font-family: 'MS Reference Sans Serif'; -fx-font-size: 14px;");

            Button irATienda = new Button("Ir a tienda");
            irATienda.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-family: 'MS Reference Sans Serif'; -fx-font-size: 14px;");

            // Configurar la acción del botón para ir a la tienda
            irATienda.setOnAction(event -> irATienda(tienda, event));

            tiendaBox.getChildren().addAll(imagenTienda, nombreTienda, irATienda);
            contenedorTiendas.getChildren().add(tiendaBox);
        }

        return contenedorTiendas;
    }

    private void irATienda(Tienda tienda, ActionEvent event) {
        try {
            // Establecer la tienda seleccionada antes de cargar la vista
            ViewMirarTiendaController.setTiendaSeleccionada(tienda);

            // Configurar el FXMLLoader y cargar la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/PantallaCuenta/Tienda/View-MirarTienda.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva vista
            ViewMirarTiendaController controlador = loader.getController();

            // Asegurarse de que los datos estén cargados antes de mostrar la vista
            controlador.cargarDatosTienda();

            // Cambiar la vista en el mismo Stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener una tienda por ID
    public Tienda obtenerTiendaPorId(int idTienda) {
        Tienda tienda = null;
        String sql = "SELECT nombre, descripcion, categoria, imagenTienda FROM Tienda WHERE idTienda = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idTienda);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                String categoria = rs.getString("categoria");
                byte[] imagen = rs.getBytes("imagenTienda");
                tienda = new Tienda(idTienda, nombre, descripcion, categoria, imagen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tienda;
    }

    // Método para actualizar la tienda en la base de datos
    public void actualizarTienda(Tienda tienda) {
        String sql = "UPDATE Tienda SET nombre = ?, descripcion = ?, categoria = ?, imagenTienda = ? WHERE idTienda = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, tienda.getNombre());
            pstmt.setString(2, tienda.getDescripcion());
            pstmt.setString(3, tienda.getCategoria());
            pstmt.setBytes(4, tienda.getImagen());
            pstmt.setInt(5, tienda.getIdTienda());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para obtener las categorías disponibles
    public List<String> obtenerCategorias() {
        List<String> categorias = new ArrayList<>();
        categorias.add("Electrónica");
        categorias.add("Ropa y Moda");
        categorias.add("Hogar y Jardín");
        categorias.add("Salud y Belleza");
        categorias.add("Deportes");
        categorias.add("Juguetes");
        categorias.add("Alimentos");
        categorias.add("Automóviles");
        categorias.add("Libros");
        categorias.add("Mascotas");
        return categorias;
    }
}
