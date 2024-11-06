package Repositorios.Carrito;

import DB.JDBC;
import Modelos.Producto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MostrarCarrito {

    // Método para obtener el ID del carrito de un usuario
    public int obtenerIdCarritoDesdeBD(int idUsuario) throws SQLException {
        String sql = "SELECT idCarrito FROM carrito WHERE idUsuario = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idCarrito");
            } else {
                System.out.println("No se encontró carrito para el usuario con id: " + idUsuario);
                return -1;
            }
        }
    }

    public boolean productoYaExisteEnCarrito(int idCarrito, int idProducto) throws SQLException {
        String verificarSql = "SELECT cantidad FROM carrito_producto WHERE idCarrito = ? AND idProducto = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement verificarStmt = conexion.prepareStatement(verificarSql)) {
            verificarStmt.setInt(1, idCarrito);
            verificarStmt.setInt(2, idProducto);
            ResultSet rs = verificarStmt.executeQuery();
            return rs.next();
        }
    }

    public void actualizarCantidadProducto(int idCarrito, int idProducto, int cantidad) throws SQLException {
        String actualizarSql = "UPDATE carrito_producto SET cantidad = cantidad + ? WHERE idCarrito = ? AND idProducto = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement actualizarStmt = conexion.prepareStatement(actualizarSql)) {
            actualizarStmt.setInt(1, cantidad);
            actualizarStmt.setInt(2, idCarrito);
            actualizarStmt.setInt(3, idProducto);
            actualizarStmt.executeUpdate();
        }
    }

    public void insertarProductoEnCarrito(int idCarrito, int idProducto, int cantidad) throws SQLException {
        String insertarSql = "INSERT INTO carrito_producto (idCarrito, idProducto, cantidad) VALUES (?, ?, ?)";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement insertarStmt = conexion.prepareStatement(insertarSql)) {
            insertarStmt.setInt(1, idCarrito);
            insertarStmt.setInt(2, idProducto);
            insertarStmt.setInt(3, cantidad);
            insertarStmt.executeUpdate();
        }
    }

    public List<Producto> obtenerProductosDeCarrito(int idCarrito) throws SQLException {
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
                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setImagenProducto(rs.getBytes("imagenProducto"));
                producto.setCantidad(rs.getInt("cantidad"));
                productos.add(producto);
            }
        }
        return productos;
    }

    public void eliminarProductoDelCarrito(int idProducto, int idCarrito) throws SQLException {
        String sql = "DELETE FROM carrito_producto WHERE idCarrito = ? AND idProducto = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idCarrito);
            pstmt.setInt(2, idProducto);
            pstmt.executeUpdate();
        }
    }

    public void vaciarCarrito(int idCarrito) throws SQLException {
        String sql = "DELETE FROM carrito_producto WHERE idCarrito = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idCarrito);
            pstmt.executeUpdate();
        }
    }

    public int registrarCompra(int idUsuario, BigDecimal totalCompra) throws SQLException {
        String sql = "INSERT INTO Compra (idUsuario, total_compra, fecha, hora) VALUES (?, ?, CURDATE(), CURTIME())";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setBigDecimal(2, totalCompra);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // ID de la compra generada
            } else {
                throw new SQLException("No se pudo obtener el ID de la compra generada.");
            }
        }
    }

    public void registrarProductosDeCompra(int idCompra, int idCarrito) throws SQLException {
        String sql = "INSERT INTO compra_producto (idCompra, idProducto, cantidad) " +
                "SELECT ?, idProducto, cantidad FROM carrito_producto WHERE idCarrito = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idCompra);
            pstmt.setInt(2, idCarrito);
            pstmt.executeUpdate();
        }
    }
}
