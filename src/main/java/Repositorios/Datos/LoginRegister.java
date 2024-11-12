package Repositorios.Datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigDecimal;
import DB.JDBC;
import Modelos.UsuarioActivo;

public class LoginRegister {
    private Connection connection;

    public LoginRegister(Connection connection) {
        this.connection = connection;
    }

    public LoginRegister() {
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar el hash de la contraseña", e);
        }
    }

    public boolean buscarUsuarioPorCorreo(String correo, String password) {
        return buscarUsuario(correo, password, JDBC.ConectarBD());
    }

    public boolean buscarUsuarioPorCorreoH2(String correo, String password, Connection conexionH2) {
        return buscarUsuario(correo, password, conexionH2);
    }

    // Método de búsqueda común para la autenticación
    private boolean buscarUsuario(String correo, String password, Connection conexion) {
        String sql = "SELECT idUsuario, nombre, correo_electronico, contraseña, esVendedor, saldo_actual, saldo_pagar FROM Usuario WHERE correo_electronico = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("contraseña");
                if (storedPassword.equals(hashPassword(password))) {
                    int idUsuario = rs.getInt("idUsuario");
                    String nombre = rs.getString("nombre");
                    boolean esVendedor = rs.getBoolean("esVendedor");
                    BigDecimal saldoActual = rs.getBigDecimal("saldo_actual");
                    BigDecimal saldoPagar = rs.getBigDecimal("saldo_pagar");
                    int idCarrito = obtenerIdCarritoDesdeBD(idUsuario);

                    UsuarioActivo.setUsuarioActivo(idUsuario, nombre, correo, esVendedor, idCarrito, saldoActual, saldoPagar);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int insertarUsuario(String usuario, String direccion, String correo, String telefono, String hashedPassword) {
        String sql = "INSERT INTO Usuario (nombre, direccion, correo_electronico, telefono, contraseña, saldo_actual, saldo_pagar) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, direccion);
            pstmt.setString(3, correo);
            pstmt.setString(4, telefono);
            pstmt.setString(5, hashedPassword);
            pstmt.setBigDecimal(6, BigDecimal.ZERO);
            pstmt.setBigDecimal(7, BigDecimal.ZERO);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int obtenerIdCarritoDesdeBD(int idUsuario) {
        String sql = "SELECT idCarrito FROM carrito WHERE idUsuario = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idCarrito");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int crearCarritoParaUsuario(int idUsuario) {
        String sql = "INSERT INTO carrito (idUsuario) VALUES (?)";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
