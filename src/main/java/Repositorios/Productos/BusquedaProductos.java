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
    // Metodo para buscar productos por nombre
    public List<Producto> buscarProductosPorNombre(String nombreProducto) throws SQLException {
        //lista para almacenar los productos encontrados
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE nombre LIKE ?";//consulta para buscar productos que coincidan con la busqueda
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nombreProducto + "%");//establece el valor del parámetro para la búsqueda con el nombre del producto
            ResultSet resultSet = pstmt.executeQuery();
            productos = convertirResultSetAListaProductos(resultSet);//convierte el ResultSet en una lista de productos
        }
        return productos;  //retorna la lista de productos encontrados
    }

    //Metodo para buscar productos con varios filtros (nombre, categoría, precio mínimo y máximo)
    public List<Producto> buscarConFiltros(String nombreProducto, String categoria, BigDecimal precioMin, BigDecimal precioMax) throws SQLException {

        List<Producto> productos = new ArrayList<>();//lista para almacenar los productos encontrados
        StringBuilder sql = new StringBuilder("SELECT * FROM Producto WHERE nombre LIKE ?");//StringBuilder para construir la consulta SQL de manera dinámica
        List<Object> parametros = new ArrayList<>();//lista para almacenar los parámetros de la consulta
        parametros.add("%" + nombreProducto + "%");//agrega el parámetro de nombreProducto con el formato adecuado
        //"%" = LIKE
        if (categoria != null && !categoria.isEmpty()) {//si se proporciona una categoría, agregamos el filtro a la consulta
            sql.append(" AND categoria = ?");//agrega una condición adicional a la consulta específicamente para filtrar los productos por categoría.
            //sql.append = agregar texto al final de la cadena existente
            parametros.add(categoria);
        }
        if (precioMin != null) {
            sql.append(" AND precio >= ?");//si se proporciona un precio mínimo, agregamos el filtro a la consulta
            parametros.add(precioMin);
        }
        if (precioMax != null) {//si se proporciona un precio máximo, agregamos el filtro a la consulta
            sql.append(" AND precio <= ?");
            parametros.add(precioMax);
        }
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                pstmt.setObject(i + 1, parametros.get(i));  //establecemos los parámetros de la consulta
            }
            ResultSet resultSet = pstmt.executeQuery();
            productos = convertirResultSetAListaProductos(resultSet);//convierte el ResultSet en una lista de productos
        }
        return productos;  //retorna la lista de productos encontrados
    }

    // Metodo auxiliar para convertir un ResultSet en una lista de objetos Producto
    private List<Producto> convertirResultSetAListaProductos(ResultSet resultSet) throws SQLException {
        List<Producto> productos = new ArrayList<>();//lista para almacenar los productos
        while (resultSet.next()) {//itera sobre el conjunto de resultados
            Producto producto = new Producto();//creamos un nuevo objeto Producto
            //asigna los valores del ResultSet al objeto Producto
            producto.setIdProducto(resultSet.getInt("idProducto"));
            producto.setNombre(resultSet.getString("nombre"));
            producto.setPrecio(resultSet.getBigDecimal("precio"));
            producto.setDescripcion(resultSet.getString("descripcion"));
            producto.setStock(resultSet.getInt("stock"));
            producto.setCategoria(resultSet.getString("categoria"));
            producto.setImagenProducto(resultSet.getBytes("imagenProducto"));
            producto.setIdTienda(resultSet.getInt("idTienda"));
            productos.add(producto);//agregamos el producto a la lista
        }
        return productos;  //retorna la lista de productos
    }
}
