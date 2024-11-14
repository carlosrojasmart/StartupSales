package Repositorios.Productos;

import DB.JDBC;
import Modelos.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MostrarProductos {

    // Metodo para obtener la lista de productos de una tienda específica
    public List<Producto> obtenerProductosDeTienda(int idTienda) {
        List<Producto> productos = new ArrayList<>();//se crea una lista de productos
        String sql = "SELECT * FROM Producto WHERE idTienda = ?";//consulta que selecciona todos los productos de una tienda
        try (
                Connection conexion = JDBC.ConectarBD();
                PreparedStatement pstmt = conexion.prepareStatement(sql)
        ) {
            pstmt.setInt(1, idTienda);//se asigna el valor del parámetro ? en la consulta
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {//se itera sobre cada fila del ResultSet
                Producto producto = new Producto();//se crea un nuevo objeto Producto
                //se asignan los valores obtenidos del ResultSet a los atributos del Producto
                producto.setIdProducto(rs.getInt("idProducto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getBigDecimal("precio"));//se usa BigDecimal para manejar valores monetarios
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setStock(rs.getInt("stock"));
                producto.setCategoria(rs.getString("categoria"));
                producto.setImagenProducto(rs.getBytes("imagenProducto")); //obtiene la imagen en bytes
                producto.setIdTienda(rs.getInt("idTienda"));
                productos.add(producto);//se agrega el producto a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;//retorna la lista de productos

    }
    // Metodo para obtener los 5 productos más vendidos
    public List<Producto> obtenerTopProductosMasVendidos() {
        List<Producto> topProductos = new ArrayList<>();//se crea una lista de productos
        //consulta para obtener los productos más vendidos haciendo JOIN entre Producto y compra_producto
        String sql = """
    SELECT p.idProducto, p.nombre, p.precio, p.imagenProducto, SUM(cp.cantidad) AS totalVendida
    FROM Producto p
    JOIN compra_producto cp ON p.idProducto = cp.idProducto
    GROUP BY p.idProducto
    ORDER BY totalVendida DESC
    LIMIT 5
    """;
        try (
                Connection conexion = JDBC.ConectarBD();
                PreparedStatement pstmt = conexion.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()
        ) {

            while (rs.next()) {//se itera sobre cada fila del ResultSet
                Producto producto = new Producto();//se crea un nuevo objeto Producto
                //se asignan los valores obtenidos del ResultSet a los atributos del Producto
                producto.setIdProducto(rs.getInt("idProducto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setImagenProducto(rs.getBytes("imagenProducto"));
                topProductos.add(producto);//se agrega el producto a la lista de productos más vendidos
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topProductos;
    }
}
