package Repositorios.Compras;

import DB.JDBC;
import Modelos.Compra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComprasRepo {

    public List<Compra> obtenerComprasPorUsuario(int idUsuario) {
        List<Compra> compras = new ArrayList<>();//crea una lista para almacenar las compras del usuario
        //consulta para obtener las compras de un usuario junto a los productos
        String sql = "SELECT c.idCompra, c.total_compra, c.fecha, c.hora, " +
                "GROUP_CONCAT(CONCAT(p.nombre, ' x', cp.cantidad) SEPARATOR ', ') AS productos " +
                "FROM Compra c " +
                "JOIN compra_producto cp ON c.idCompra = cp.idCompra " +
                "JOIN Producto p ON cp.idProducto = p.idProducto " +
                "WHERE c.idUsuario = ? " +
                "GROUP BY c.idCompra";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);//obtiene la consulta de idUsuario en el indice 1
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {//itera sobre los resultados de la consulta
                Compra compra = new Compra();//crea un nuevo objeto Compra para almacenar los datos de cada compra
                compra.setIdCompra(rs.getInt("idCompra")); //establece el ID de la compra
                compra.setTotalCompra(rs.getBigDecimal("total_compra"));//establece el total de la compra
                compra.setFecha(rs.getDate("fecha").toLocalDate());//establece la fecha de la compra como LocalDate
                compra.setHora(rs.getTime("hora").toLocalTime());//establece la hora de la compra como LocalTime

                String productosResumen = rs.getString("productos");//obtiene los productos con cantidades y lo establece en la compra
                compra.setProductosResumen(productosResumen);

                compras.add(compra);//añade la compra a la lista de compras
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener las compras del usuario: " + e.getMessage());
        }

        //retorna la lista de compras del usuario
        return compras;
    }

    // Nuevo metodo para contar las compras de un usuario
    public int contarComprasPorUsuario(int idUsuario) {
        String sql = "SELECT COUNT(*) AS total FROM Compra WHERE idUsuario = ?";//consulta para contar el número total de compras de un usuario
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);//asigna la consulta de idUsuario en el indice 1
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {//si la consulta devuelve un resultado, obtiene el número total de compras
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al contar las compras del usuario: " + e.getMessage());
        }
        return 0;
    }

}
