package Servicios.Datos;

import DB.JDBC;
import Modelos.Tienda;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MostrarTiendas {

    // Método para obtener las tiendas desde la base de datos
    public List<Tienda> obtenerTiendas(int idUsuario) {
        List<Tienda> tiendas = new ArrayList<>();
        String sql = "SELECT nombre, imagenTienda FROM Tienda WHERE idUsuario = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    byte[] imagen = rs.getBytes("imagenTienda");
                    tiendas.add(new Tienda(nombre, imagen));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tiendas;
    }

    // Método para crear la vista de las tiendas con estilos ajustados
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
            irATienda.setOnAction(event -> {
                System.out.println("Ir a la tienda: " + tienda.getNombre());
                // Aquí podrías agregar lógica adicional para abrir la vista de la tienda
            });

            tiendaBox.getChildren().addAll(imagenTienda, nombreTienda, irATienda);
            contenedorTiendas.getChildren().add(tiendaBox);
        }

        return contenedorTiendas;
    }
}
