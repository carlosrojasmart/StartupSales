package Repositorios.Carrito;

import DB.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObtenerCarrito {

    private int obtenerIdCarritoDesdeBD(int idUsuario) {
        String sql = "SELECT idCarrito FROM carrito WHERE idUsuario = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int idCarrito = rs.getInt("idCarrito");
                return idCarrito;
            } else {
                System.out.println("No se encontró carrito para el usuario con id: " + idUsuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
