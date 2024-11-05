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
    String hashPassword(String password) {
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

    public boolean handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Por favor, completa todos los campos.");
            return false;
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
                        System.out.println("Login exitoso.");
                        return true;
                    } else {
                        System.out.println("Contraseña incorrecta.");
                        return false;
                    }
                } else {
                    System.out.println("Correo no existe.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión a la base de datos: " + e.getMessage());
            return false;
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

    public boolean registrarUsuario(String usuario, String correo, String contraseña, String telefono, String direccion) {
        if (usuario.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            System.out.println("Por favor, completa todos los campos.");
            return false;
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

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int idUsuario = generatedKeys.getInt(1);

                            int idCarrito = crearCarritoParaUsuario(idUsuario);
                            UsuarioActivo.setUsuarioActivo(idUsuario, usuario, correo, false, idCarrito, saldoInicial, saldoInicial);
                            System.out.println("Usuario registrado exitosamente.");
                            return true;
                        }
                    }
                }
                System.out.println("Error al registrar usuario.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión a la base de datos: " + e.getMessage());
            return false;
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
    
}
