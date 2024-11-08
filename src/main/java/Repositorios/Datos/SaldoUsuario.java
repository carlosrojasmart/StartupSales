package Repositorios.Datos;

import DB.JDBC;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaldoUsuario {

    public void actualizarSaldo(int idUsuario, BigDecimal nuevoSaldo) {
        String sql = "UPDATE Usuario SET saldo_actual = ? WHERE idUsuario = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, nuevoSaldo);
            pstmt.setInt(2, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al actualizar el saldo en la base de datos: " + e.getMessage());
        }
    }
}
