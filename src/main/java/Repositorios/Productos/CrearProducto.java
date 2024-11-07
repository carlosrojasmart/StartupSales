package Repositorios.Productos;

import DB.JDBC;
import Modelos.Producto;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrearProducto {

    // Atributo para almacenar el archivo de imagen seleccionado
    private File archivoImagen;

    public void setArchivoImagen(File archivoImagen) {
        this.archivoImagen = archivoImagen;
    }

    public File getArchivoImagen() {
        return archivoImagen;
    }

    public boolean crearProducto(Producto producto) {
        String sql = "INSERT INTO Producto (nombre, precio, descripcion, stock, categoria, imagenProducto, idTienda) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(archivoImagen)) {  // Usar archivoImagen directamente

            pstmt.setString(1, producto.getNombre());
            pstmt.setBigDecimal(2, producto.getPrecio());
            pstmt.setString(3, producto.getDescripcion());
            pstmt.setInt(4, producto.getStock());
            pstmt.setString(5, producto.getCategoria());

            // Cargar la imagen desde archivoImagen
            pstmt.setBinaryStream(6, fis, (int) archivoImagen.length());

            pstmt.setInt(7, producto.getIdTienda());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE Producto SET nombre = ?, precio = ?, descripcion = ?, stock = ?, categoria = ?, imagenProducto = ? WHERE idProducto = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, producto.getNombre());
            pstmt.setBigDecimal(2, producto.getPrecio());
            pstmt.setString(3, producto.getDescripcion());
            pstmt.setInt(4, producto.getStock());
            pstmt.setString(5, producto.getCategoria());

            // Verificar si hay una imagen para actualizar
            if (archivoImagen != null) {
                try (FileInputStream fis = new FileInputStream(archivoImagen)) {
                    pstmt.setBinaryStream(6, fis, (int) archivoImagen.length());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                pstmt.setNull(6, java.sql.Types.BLOB);
            }

            pstmt.setInt(7, producto.getIdProducto());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarProducto(int idProducto) {
        String sqlEliminarDeCarrito = "DELETE FROM carrito_producto WHERE idProducto = ?";
        String sqlEliminarProducto = "DELETE FROM Producto WHERE idProducto = ?";
        try (Connection conexion = JDBC.ConectarBD()) {
            try (PreparedStatement pstmtCarrito = conexion.prepareStatement(sqlEliminarDeCarrito)) {
                pstmtCarrito.setInt(1, idProducto);
                pstmtCarrito.executeUpdate();
            }
            try (PreparedStatement pstmtProducto = conexion.prepareStatement(sqlEliminarProducto)) {
                pstmtProducto.setInt(1, idProducto);
                return pstmtProducto.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
