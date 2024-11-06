package Repositorios.Productos;

import DB.JDBC;
import Modelos.Producto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusquedaProductos {

    public List<Producto> buscarProductosPorNombre(String nombreProducto) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE nombre LIKE ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombreProducto + "%");
            ResultSet resultSet = pstmt.executeQuery();
            productos = convertirResultSetAListaProductos(resultSet);
        }
        return productos;
    }

    public List<Producto> buscarConFiltros(String nombreProducto, String categoria, BigDecimal precioMin, BigDecimal precioMax) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Producto WHERE nombre LIKE ?");
        List<Object> parametros = new ArrayList<>();
        parametros.add("%" + nombreProducto + "%");

        if (categoria != null && !categoria.isEmpty()) {
            sql.append(" AND categoria = ?");
            parametros.add(categoria);
        }
        if (precioMin != null) {
            sql.append(" AND precio >= ?");
            parametros.add(precioMin);
        }
        if (precioMax != null) {
            sql.append(" AND precio <= ?");
            parametros.add(precioMax);
        }

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                pstmt.setObject(i + 1, parametros.get(i));
            }
            ResultSet resultSet = pstmt.executeQuery();
            productos = convertirResultSetAListaProductos(resultSet);
        }
        return productos;
    }

    private List<Producto> convertirResultSetAListaProductos(ResultSet resultSet) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        while (resultSet.next()) {
            Producto producto = new Producto();
            producto.setIdProducto(resultSet.getInt("idProducto"));
            producto.setNombre(resultSet.getString("nombre"));
            producto.setPrecio(resultSet.getBigDecimal("precio"));
            producto.setDescripcion(resultSet.getString("descripcion"));
            producto.setStock(resultSet.getInt("stock"));
            producto.setCategoria(resultSet.getString("categoria"));
            producto.setImagenProducto(resultSet.getBytes("imagenProducto"));
            producto.setIdTienda(resultSet.getInt("idTienda"));
            productos.add(producto);
        }
        return productos;
    }
}
