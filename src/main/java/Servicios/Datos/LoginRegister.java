package Servicios.Datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


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
                        double saldoActual = rs.getDouble("saldo_actual");
                        double saldoPagar = rs.getDouble("saldo_pagar");

                        // Obtener el idCarrito del usuario
                        int idCarritoUsuario = obtenerIdCarritoDesdeBD(idUsuario);
                        if (idCarritoUsuario == -1) {
                            // Si no hay un carrito, podrías crear uno automáticamente para el usuario
                            idCarritoUsuario = crearCarritoParaUsuario(idUsuario);
                        }

                        // Establecer los datos del usuario activo, incluyendo el saldo actual y el saldo a pagar
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
        return -1; // Devuelve -1 si no se encontró ningún carrito para el usuario
    }


    // Método para obtener el idCarrito dado un idUsuario
    private int obtenerIdCarrito(int idUsuario, Connection conexion) throws SQLException {
        String sql = "SELECT idCarrito FROM Carrito WHERE idUsuario = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("idCarrito");
            } else {
                return -1;
            }
        }
    }

    public void registrarUsuario(String usuario, String correo, String contraseña, String telefono, String direccion, RegistrationCallback callback) {
        if (usuario.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            callback.onFailure("Por favor, completa todos los campos.");
            return;
        }

        int idUsuario = generarIdAleatorio();
        String hashedPassword = hashPassword(contraseña);
        double saldoInicial = 0.0; // El saldo inicial puede ser 0 para un nuevo usuario

        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "INSERT INTO Usuario (idUsuario, nombre, direccion, correo_electronico, telefono, contraseña, saldo_actual, saldo_pagar) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                pstmt.setString(2, usuario);
                pstmt.setString(3, direccion);
                pstmt.setString(4, correo);
                pstmt.setString(5, telefono);
                pstmt.setString(6, hashedPassword);
                pstmt.setDouble(7, saldoInicial); // Asignar el saldo inicial
                pstmt.setDouble(8, saldoInicial); // Asignar el saldo a pagar inicial

                pstmt.executeUpdate();

                // Crear un carrito para el usuario registrado
                int idCarrito = crearCarritoParaUsuario(idUsuario);

                // Configurar el usuario activo con el saldo inicial
                UsuarioActivo.setUsuarioActivo(idUsuario, usuario, correo, false, idCarrito, saldoInicial, saldoInicial);

                callback.onSuccess("Usuario registrado exitosamente.");
            } catch (SQLException e) {
                callback.onFailure("Error al registrar usuario: " + e.getMessage());
            }
        } catch (SQLException e) {
            callback.onFailure("Error de conexión a la base de datos: " + e.getMessage());
        }
    }

    private int crearCarritoParaUsuario(int idUsuario) {
        int idCarrito = generarIdAleatorio(); // Generar un ID para el carrito
        String sql = "INSERT INTO carrito (idCarrito, idUsuario) VALUES (?, ?)";
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idCarrito);
            pstmt.setInt(2, idUsuario);
            pstmt.executeUpdate();
            return idCarrito;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Devuelve -1 si no se pudo crear el carrito
    }

    private int generarIdAleatorio() {
        // Generar un número aleatorio de 6 dígitos como idCliente
        Random random = new Random();
        return random.nextInt(900000) + 100000; // Genera un número entre 100000 y 999999
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
