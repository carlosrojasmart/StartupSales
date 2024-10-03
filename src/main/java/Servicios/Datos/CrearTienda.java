package Servicios.Datos;

import DB.JDBC;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrearTienda {
    public void crearTienda(int idTienda, String nombre, String descripcion, int idUsuario, String categoria, File archivoImagen) throws SQLException, IOException {
        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "INSERT INTO Tienda (idTienda, nombre, descripcion, idUsuario, categoria, imagenTienda) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idTienda);
                pstmt.setString(2, nombre);
                pstmt.setString(3, descripcion);
                pstmt.setInt(4, idUsuario);
                pstmt.setString(5, categoria);

                // Leer la imagen como un flujo binario
                FileInputStream fis = new FileInputStream(archivoImagen);
                pstmt.setBinaryStream(6, fis, (int) archivoImagen.length());

                pstmt.executeUpdate();
                fis.close();
            }
        }
    }
}
