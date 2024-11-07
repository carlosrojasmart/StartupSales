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

    // Método para obtener la lista de productos de una tienda específica
    public List<Producto> obtenerProductosDeTienda(int idTienda) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE idTienda = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idTienda);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("idProducto"));
                producto.setNombre(rs.getString("nombre"));

                // Usar BigDecimal para el precio
                producto.setPrecio(rs.getBigDecimal("precio"));

                producto.setDescripcion(rs.getString("descripcion"));
                producto.setStock(rs.getInt("stock"));
                producto.setCategoria(rs.getString("categoria"));
                producto.setImagenProducto(rs.getBytes("imagenProducto"));
                producto.setIdTienda(rs.getInt("idTienda"));
                productos.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }

    public List<Producto> obtenerTopProductosMasVendidos() {
        List<Producto> topProductos = new ArrayList<>();
        String sql = """
        SELECT p.idProducto, p.nombre, p.precio, p.imagenProducto, SUM(cp.cantidad) AS totalVendida
        FROM Producto p
        JOIN compra_producto cp ON p.idProducto = cp.idProducto
        GROUP BY p.idProducto
        ORDER BY totalVendida DESC
        LIMIT 5
    """;

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("idProducto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setImagenProducto(rs.getBytes("imagenProducto"));
                topProductos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topProductos;
    }

}
