package Servicios.Datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigDecimal;
import DB.JDBC;

public class LoginRegister {
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar el hash de la contraseña", e);
        }
    }

    public void handleLogin(String username, String password, LoginCallback callback) {
        if (username.isEmpty() || password.isEmpty()) {
            callback.onFailure("Por favor, completa todos los campos.");
            return;
        }

        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "SELECT idUsuario, nombre, correo_electronico, contraseña, esVendedor, saldo_actual, saldo_pagar FROM Usuario WHERE correo_electronico = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String storedPassword = rs.getString("contraseña");
                    String hashedPassword = hashPassword(password);

                    if (storedPassword.equals(hashedPassword)) {
                        int idUsuario = rs.getInt("idUsuario");
                        String nombre = rs.getString("nombre");
                        String correo = rs.getString("correo_electronico");
                        boolean esVendedor = rs.getBoolean("esVendedor");
                        BigDecimal saldoActual = rs.getBigDecimal("saldo_actual");
                        BigDecimal saldoPagar = rs.getBigDecimal("saldo_pagar");

                        int idCarritoUsuario = obtenerIdCarritoDesdeBD(idUsuario);
                        if (idCarritoUsuario == -1) {
                            idCarritoUsuario = crearCarritoParaUsuario(idUsuario);
                        }

                        UsuarioActivo.setUsuarioActivo(idUsuario, nombre, correo, esVendedor, idCarritoUsuario, saldoActual, saldoPagar);
                        callback.onSuccess("Login exitoso.");
                    } else {
                        callback.onFailure("Contraseña incorrecta.");
                    }
                } else {
                    callback.onFailure("Correo no existe.");
                }
            }
        } catch (SQLException e) {
            callback.onFailure("Error de conexión a la base de datos: " + e.getMessage());
        }
    }

    private int obtenerIdCarritoDesdeBD(int idUsuario) {
        String sql = "SELECT idCarrito FROM carrito WHERE idUsuario = ?";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int idCarrito = rs.getInt("idCarrito");
                System.out.println("Carrito encontrado para el usuario: " + idCarrito);
                return idCarrito;
            } else {
                System.out.println("No se encontró carrito para el usuario con id: " + idUsuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void registrarUsuario(String usuario, String correo, String contraseña, String telefono, String direccion, RegistrationCallback callback) {
        if (usuario.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            callback.onFailure("Por favor, completa todos los campos.");
            return;
        }

        String hashedPassword = hashPassword(contraseña);
        BigDecimal saldoInicial = BigDecimal.ZERO;

        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "INSERT INTO Usuario (nombre, direccion, correo_electronico, telefono, contraseña, saldo_actual, saldo_pagar) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, usuario);
                pstmt.setString(2, direccion);
                pstmt.setString(3, correo);
                pstmt.setString(4, telefono);
                pstmt.setString(5, hashedPassword);
                pstmt.setBigDecimal(6, saldoInicial);
                pstmt.setBigDecimal(7, saldoInicial);

                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idUsuario = generatedKeys.getInt(1);

                        int idCarrito = crearCarritoParaUsuario(idUsuario);

                        UsuarioActivo.setUsuarioActivo(idUsuario, usuario, correo, false, idCarrito, saldoInicial, saldoInicial);
                        callback.onSuccess("Usuario registrado exitosamente.");
                    } else {
                        callback.onFailure("Error al obtener el ID del usuario recién registrado.");
                    }
                }
            } catch (SQLException e) {
                callback.onFailure("Error al registrar usuario: " + e.getMessage());
            }
        } catch (SQLException e) {
            callback.onFailure("Error de conexión a la base de datos: " + e.getMessage());
        }
    }

    private int crearCarritoParaUsuario(int idUsuario) {
        String sql = "INSERT INTO carrito (idUsuario) VALUES (?)";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public interface LoginCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    public interface RegistrationCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }
}
