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

    private File archivoImagen;

    public void setArchivoImagen(File archivoImagen) {
        this.archivoImagen = archivoImagen;
    }

    public File getArchivoImagen() {
        return archivoImagen;
    }

    public void crearTienda(String nombre, String descripcion, int idUsuario, String categoria, File archivoImagen) throws SQLException, IOException {
        try (Connection conexion = JDBC.ConectarBD()) {
            String sqlTienda = "INSERT INTO Tienda (nombre, descripcion, idUsuario, categoria, imagenTienda) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstmtTienda = conexion.prepareStatement(sqlTienda)) {
                pstmtTienda.setString(1, nombre);
                pstmtTienda.setString(2, descripcion);
                pstmtTienda.setInt(3, idUsuario);
                pstmtTienda.setString(4, categoria);

                if (archivoImagen != null && archivoImagen.exists()) {
                    try (InputStream fis = new FileInputStream(archivoImagen)) {
                        pstmtTienda.setBinaryStream(5, fis, (int) archivoImagen.length());
                        pstmtTienda.executeUpdate();
                    }
                } else {
                    pstmtTienda.setNull(5, java.sql.Types.BLOB);
                    pstmtTienda.executeUpdate();
                }
            }

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
}
