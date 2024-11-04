package Servicios.Datos;

import DB.JDBC;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.scene.image.ImageView;

import Servicios.Vistas.CambiosVistas;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
public class CrearTienda {


    private File archivoImagen;

    // Método para cargar la imagen de la tienda
    public void cargarImagen(ImageView imagenTienda) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        archivoImagen = fileChooser.showOpenDialog(null);

        if (archivoImagen != null) {
            try {
                Image nuevaImagen = new Image(new FileInputStream(archivoImagen));
                imagenTienda.setImage(nuevaImagen);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para crear la tienda
    public void crearTienda(String nombre, String descripcion, int idUsuario, String categoria, File archivoImagen) throws SQLException, IOException {
        try (Connection conexion = JDBC.ConectarBD()) {
            // Insertar la nueva tienda sin especificar el idTienda (MySQL lo generará automáticamente)
            String sqlTienda = "INSERT INTO Tienda (nombre, descripcion, idUsuario, categoria, imagenTienda) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstmtTienda = conexion.prepareStatement(sqlTienda)) {
                pstmtTienda.setString(1, nombre);
                pstmtTienda.setString(2, descripcion);
                pstmtTienda.setInt(3, idUsuario);
                pstmtTienda.setString(4, categoria);

                // Leer la imagen como un flujo binario
                FileInputStream fis = new FileInputStream(archivoImagen);
                pstmtTienda.setBinaryStream(5, fis, (int) archivoImagen.length());

                pstmtTienda.executeUpdate();
                fis.close();
            }

            // Actualizar el campo esVendedor del usuario a TRUE
            String sqlUsuario = "UPDATE Usuario SET esVendedor = TRUE WHERE idUsuario = ?";

            try (PreparedStatement pstmtUsuario = conexion.prepareStatement(sqlUsuario)) {
                pstmtUsuario.setInt(1, idUsuario);
                pstmtUsuario.executeUpdate();
            }
        }
    }

    public boolean eliminarTienda(int idTienda) {
        String sql = "DELETE FROM Tienda WHERE idTienda = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idTienda);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener el archivo de imagen
    public File getArchivoImagen() {
        return archivoImagen;
    }
}
