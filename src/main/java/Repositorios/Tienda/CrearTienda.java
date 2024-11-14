package Repositorios.Tienda;

import DB.JDBC;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrearTienda {

    //Metodo para crear una tienda
    public void crearTienda(String nombre, String descripcion, int idUsuario, String categoria, File archivoImagen) throws SQLException, IOException {
        //consulta insertar una tienda en la BD
        String sqlTienda = "INSERT INTO Tienda (nombre, descripcion, idUsuario, categoria, imagenTienda) VALUES (?, ?, ?, ?, ?)";

        //consulta para actualizar el estado de 'esVendedor' del usuario
        String sqlUsuario = "UPDATE Usuario SET esVendedor = TRUE WHERE idUsuario = ?";

        try (
                Connection conexion = JDBC.ConectarBD();
                PreparedStatement pstmtTienda = conexion.prepareStatement(sqlTienda);
                PreparedStatement pstmtUsuario = conexion.prepareStatement(sqlUsuario)
        ) {
            //asignalos parámetros de la tienda
            pstmtTienda.setString(1, nombre);//asigna el nombre de la tienda
            pstmtTienda.setString(2, descripcion);//asigna la descripción de la tienda
            pstmtTienda.setInt(3, idUsuario);//asigna el ID del usuario propietario de la tienda
            pstmtTienda.setString(4, categoria);//asigna la categoría de la tienda

            if (archivoImagen != null && archivoImagen.exists()) {//verificar si hay un archivo de imagen y cargarlo
                try (InputStream fis = new FileInputStream(archivoImagen)) {//si el archivo de imagen existe, se carga en un InputStream
                    //se configura el parámetro de la imagen de la tienda como un flujo binario
                    pstmtTienda.setBinaryStream(5, fis, (int) archivoImagen.length());
                    pstmtTienda.executeUpdate();
                }
            } else {
                pstmtTienda.setNull(5, java.sql.Types.BLOB);//si no hay archivo de imagen, se asigna un valor NULL al campo de imagen
                pstmtTienda.executeUpdate();
            }

            //actualiza el estado de 'esVendedor' del usuario a TRUE, indicando que ahora es un vendedor
            pstmtUsuario.setInt(1, idUsuario);//asigna el ID del usuario
            pstmtUsuario.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al crear la tienda en la base de datos: " + e.getMessage());
            throw e;
        }
    }
    /*
    // Metodo para eliminar una tienda
    public boolean eliminarTienda(int idTienda) {
        //consulta para eliminar una tienda en la base de datos
        String sql = "DELETE FROM Tienda WHERE idTienda = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idTienda);//asigna el ID de la tienda que se va a eliminar
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }*/

}
