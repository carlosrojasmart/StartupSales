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

    public void crearTienda(String nombre, String descripcion, int idUsuario, String categoria, File archivoImagen) throws SQLException, IOException {
        String sqlTienda = "INSERT INTO Tienda (nombre, descripcion, idUsuario, categoria, imagenTienda) VALUES (?, ?, ?, ?, ?)";
        String sqlUsuario = "UPDATE Usuario SET esVendedor = TRUE WHERE idUsuario = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmtTienda = conexion.prepareStatement(sqlTienda);
             PreparedStatement pstmtUsuario = conexion.prepareStatement(sqlUsuario)) {

            // Configurar los parámetros de la tienda
            pstmtTienda.setString(1, nombre);
            pstmtTienda.setString(2, descripcion);
            pstmtTienda.setInt(3, idUsuario);
            pstmtTienda.setString(4, categoria);

            // Verificar si hay un archivo de imagen y cargarlo
            if (archivoImagen != null && archivoImagen.exists()) {
                try (InputStream fis = new FileInputStream(archivoImagen)) {
                    pstmtTienda.setBinaryStream(5, fis, (int) archivoImagen.length());
                    pstmtTienda.executeUpdate();
                }
            } else {
                pstmtTienda.setNull(5, java.sql.Types.BLOB);
                pstmtTienda.executeUpdate();
            }

            // Actualizar el estado de `esVendedor` del usuario
            pstmtUsuario.setInt(1, idUsuario);
            pstmtUsuario.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al crear la tienda en la base de datos: " + e.getMessage());
            throw e; // Lanzar nuevamente para que el controlador lo maneje
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
}
