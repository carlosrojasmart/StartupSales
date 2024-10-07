package Servicios.Datos;

import DB.JDBC;
import Modelos.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrearProducto {

    public boolean crearProducto(Producto producto) {
        // Generar un idProducto aleatorio antes de guardar
        producto.setIdProducto(generarIdProductoAleatorio());

        String sql = "INSERT INTO Producto (idProducto, nombre, precio, descripcion, stock, categoria, imagenProducto, idTienda) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, producto.getIdProducto());
            pstmt.setString(2, producto.getNombre());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setString(4, producto.getDescripcion());
            pstmt.setInt(5, producto.getStock());
            pstmt.setString(6, producto.getCategoria());
            pstmt.setBytes(7, producto.getImagenProducto());
            pstmt.setInt(8, producto.getIdTienda());

            int filasInsertadas = pstmt.executeUpdate();
            return filasInsertadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para generar un idProducto aleatorio
    public int generarIdProductoAleatorio() {
        Random random = new Random();
        return random.nextInt(900000) + 100000; // Generar un idProducto entre 100000 y 999999
    }

    public List<Producto> obtenerProductosDeTienda(int idTienda) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE idTienda = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idTienda);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(resultSet.getInt("idProducto"));
                producto.setNombre(resultSet.getString("nombre"));
                producto.setPrecio(resultSet.getDouble("precio"));
                producto.setDescripcion(resultSet.getString("descripcion"));
                producto.setStock(resultSet.getInt("stock"));
                producto.setCategoria(resultSet.getString("categoria"));
                producto.setImagenProducto(resultSet.getBytes("imagenProducto"));

                productos.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }
}
