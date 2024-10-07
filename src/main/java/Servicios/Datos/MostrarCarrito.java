package Servicios.Datos;

import DB.JDBC;
import Modelos.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MostrarCarrito {

    public void agregarProductoAlCarrito(int idCarrito, Producto producto) {
        String sql = "INSERT INTO carrito_producto (idCarrito, idProducto, cantidad) VALUES (?, ?, ?)";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idCarrito); // Asegúrate de que el ID del carrito es correcto
            pstmt.setInt(2, producto.getIdProducto()); // ID del producto
            pstmt.setInt(3, producto.getCantidad()); // Cantidad del producto

            pstmt.executeUpdate();
            System.out.println("Producto agregado al carrito correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al agregar el producto al carrito: " + e.getMessage());
        }
    }
    public List<Producto> obtenerProductosDeCarrito(int idCarrito) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.idProducto, p.nombre, p.precio, p.imagenProducto, cp.cantidad " +
                "FROM Producto p " +
                "JOIN carrito_producto cp ON p.idProducto = cp.idProducto " +
                "WHERE cp.idCarrito = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idCarrito);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("idProducto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setImagenProducto(rs.getBytes("imagenProducto"));
                producto.setCantidad(rs.getInt("cantidad"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }

    public void eliminarProductoDelCarrito(int idProducto, int idCarrito) {
        System.out.println("Intentando eliminar producto con idProducto: " + idProducto + " y idCarrito: " + idCarrito);
        String sql = "DELETE FROM carrito_producto WHERE idCarrito = ? AND idProducto = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idCarrito);
            pstmt.setInt(2, idProducto);

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Producto eliminado del carrito exitosamente.");
            } else {
                System.out.println("No se encontró el producto en el carrito.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el producto del carrito: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarCantidadProducto(int idProducto, int idCarrito, int nuevaCantidad) {
        String sql = "UPDATE carrito_producto SET cantidad = ? WHERE idProducto = ? AND idCarrito = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, nuevaCantidad);
            pstmt.setInt(2, idProducto);
            pstmt.setInt(3, idCarrito);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar la cantidad del producto en el carrito: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
