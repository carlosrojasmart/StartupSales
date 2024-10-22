package Servicios.Datos;

import DB.JDBC;
import Modelos.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class BusquedaProductos {

    public List<Producto> buscarProductosPorNombre(String nombreProducto) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE nombre LIKE ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            // Usar '%' para buscar productos que contengan el término de búsqueda
            pstmt.setString(1, "%" + nombreProducto + "%");
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(resultSet.getInt("idProducto"));
                producto.setNombre(resultSet.getString("nombre"));

                // Obtener el precio como BigDecimal
                producto.setPrecio(resultSet.getBigDecimal("precio")); // Cambio aquí

                producto.setDescripcion(resultSet.getString("descripcion"));
                producto.setStock(resultSet.getInt("stock"));
                producto.setCategoria(resultSet.getString("categoria"));
                producto.setImagenProducto(resultSet.getBytes("imagenProducto"));
                producto.setIdTienda(resultSet.getInt("idTienda"));
                productos.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }
}
