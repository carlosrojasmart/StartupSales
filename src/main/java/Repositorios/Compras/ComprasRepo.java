package Repositorios.Compras;

import DB.JDBC;
import Modelos.Compra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComprasRepo {

    public List<Compra> obtenerComprasPorUsuario(int idUsuario) {
        List<Compra> compras = new ArrayList<>();
        String sql = "SELECT c.idCompra, c.total_compra, c.fecha, c.hora, " +
                "GROUP_CONCAT(CONCAT(p.nombre, ' x', cp.cantidad) SEPARATOR ', ') AS productos " +
                "FROM Compra c " +
                "JOIN compra_producto cp ON c.idCompra = cp.idCompra " +
                "JOIN Producto p ON cp.idProducto = p.idProducto " +
                "WHERE c.idUsuario = ? " +
                "GROUP BY c.idCompra";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Compra compra = new Compra();
                compra.setIdCompra(rs.getInt("idCompra"));
                compra.setTotalCompra(rs.getBigDecimal("total_compra"));
                compra.setFecha(rs.getDate("fecha").toLocalDate());
                compra.setHora(rs.getTime("hora").toLocalTime());

                // Resumen de productos con cantidades
                String productosResumen = rs.getString("productos");
                compra.setProductosResumen(productosResumen);

                compras.add(compra);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener las compras del usuario: " + e.getMessage());
        }

        return compras;
    }

    // Nuevo método para contar las compras de un usuario
    public int contarComprasPorUsuario(int idUsuario) {
        String sql = "SELECT COUNT(*) AS total FROM Compra WHERE idUsuario = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al contar las compras del usuario: " + e.getMessage());
        }
        return 0;
    }
}
