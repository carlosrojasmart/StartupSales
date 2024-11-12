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

    private File archivoImagen;

    public void setArchivoImagen(File archivoImagen) {
        this.archivoImagen = archivoImagen;
    }

    public boolean crearProducto(Producto producto) {
        String sql = "INSERT INTO Producto (nombre, precio, descripcion, stock, categoria, imagenProducto, idTienda) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql);
             FileInputStream fis = archivoImagen != null ? new FileInputStream(archivoImagen) : null) {

            pstmt.setString(1, producto.getNombre());
            pstmt.setBigDecimal(2, producto.getPrecio());
            pstmt.setString(3, producto.getDescripcion());
            pstmt.setInt(4, producto.getStock());
            pstmt.setString(5, producto.getCategoria());

            if (fis != null) {
                pstmt.setBinaryStream(6, fis, (int) archivoImagen.length());
            } else {
                pstmt.setNull(6, java.sql.Types.BLOB);
            }

            pstmt.setInt(7, producto.getIdTienda());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarProducto(Producto producto, File archivoImagen) {
        String sql = archivoImagen != null ?
                "UPDATE Producto SET nombre = ?, precio = ?, descripcion = ?, stock = ?, categoria = ?, imagenProducto = ? WHERE idProducto = ?" :
                "UPDATE Producto SET nombre = ?, precio = ?, descripcion = ?, stock = ?, categoria = ? WHERE idProducto = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, producto.getNombre());
            pstmt.setBigDecimal(2, producto.getPrecio());
            pstmt.setString(3, producto.getDescripcion());
            pstmt.setInt(4, producto.getStock());
            pstmt.setString(5, producto.getCategoria());

            if (archivoImagen != null) {
                try (FileInputStream fis = new FileInputStream(archivoImagen)) {
                    pstmt.setBinaryStream(6, fis, (int) archivoImagen.length());
                    pstmt.setInt(7, producto.getIdProducto());
                }
            } else {
                pstmt.setInt(6, producto.getIdProducto());
            }

            return pstmt.executeUpdate() > 0;

        } catch (SQLException | IOException e) {
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
