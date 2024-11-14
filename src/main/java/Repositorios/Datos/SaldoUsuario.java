package Repositorios.Datos;

import DB.JDBC;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaldoUsuario {

    public void actualizarSaldo(int idUsuario, BigDecimal nuevoSaldo) {
        //consulta para actualizar el saldo actual de un usuario
        String sql = "UPDATE Usuario SET saldo_actual = ? WHERE idUsuario = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, nuevoSaldo);//asigna el nuevo saldo al primer indice de la consulta
            pstmt.setInt(2, idUsuario);//asigna el idUsuario al segundo indice de la consulta


            // Ejecuta la consulta de actualización en la base de datos
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Imprime la excepción si ocurre un error al interactuar con la base de datos
            e.printStackTrace();
            System.out.println("Error al actualizar el saldo en la base de datos: " + e.getMessage());
        }
    }
}
