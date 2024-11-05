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

    public List<Producto> buscarConFiltros(String nombreProducto, String categoria, BigDecimal precioMin, BigDecimal precioMax) {
        List<Producto> productos = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Producto WHERE nombre LIKE ?");

        // Filtros opcionales
        if (categoria != null && !categoria.isEmpty()) {
            sql.append(" AND categoria = ?");
        }
        if (precioMin != null) {
            sql.append(" AND precio >= ?");
        }
        if (precioMax != null) {
            sql.append(" AND precio <= ?");
        }

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql.toString())) {

            pstmt.setString(1, "%" + nombreProducto + "%");

            int index = 2; // El primer parámetro ya está ocupado
            if (categoria != null && !categoria.isEmpty()) {
                pstmt.setString(index++, categoria);
            }
            if (precioMin != null) {
                pstmt.setBigDecimal(index++, precioMin);
            }
            if (precioMax != null) {
                pstmt.setBigDecimal(index++, precioMax);
            }

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(resultSet.getInt("idProducto"));
                producto.setNombre(resultSet.getString("nombre"));
                producto.setPrecio(resultSet.getBigDecimal("precio"));
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
