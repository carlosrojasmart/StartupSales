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

    // Método para manejar el login
    public void handleLogin(String username, String password, LoginCallback callback) {
        if (username.isEmpty() || password.isEmpty()) {
            callback.onFailure("Por favor, completa todos los campos.");
            return;
        }

        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "SELECT idUsuario, nombre, correo_electronico, contraseña FROM Usuario WHERE correo_electronico = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String storedPassword = rs.getString("contraseña");
                    String hashedPassword = hashPassword(password); // Hashear la contraseña ingresada

                    if (storedPassword.equals(hashedPassword)) {
                        // Guardar la información del usuario activo
                        int idUsuario = rs.getInt("idUsuario");
                        String nombre = rs.getString("nombre");
                        String correo = rs.getString("correo_electronico");

                        UsuarioActivo.setUsuarioActivo(idUsuario, nombre, correo);

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

    // Método para registrar usuario
    public void registrarUsuario(String usuario, String correo, String contraseña, String telefono, String direccion, RegistrationCallback callback) {
        // Validación de campos
        if (usuario.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            callback.onFailure("Por favor, completa todos los campos.");
            return;
        }

        // Generar un idUsuario aleatorio
        int idUsuario = generarIdAleatorio();

        // Hashear la contraseña
        String hashedPassword = hashPassword(contraseña);

        // Conexión a la base de datos y ejecución de la consulta
        try (Connection conexion = JDBC.ConectarBD()) {
            // Aquí eliminamos el campo idTienda de la consulta, ya que no existe en la tabla Usuario
            String sql = "INSERT INTO Usuario (idUsuario, nombre, direccion, correo_electronico, telefono, contraseña) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario); // idUsuario generado aleatoriamente
                pstmt.setString(2, usuario); // nombre
                pstmt.setString(3, direccion); // direccion
                pstmt.setString(4, correo); // correo_electronico
                pstmt.setString(5, telefono); // telefono
                pstmt.setString(6, hashedPassword); // contraseña hasheada

                // Ejecutar la inserción
                pstmt.executeUpdate();
                callback.onSuccess("Usuario registrado exitosamente.");
            } catch (SQLException e) {
                callback.onFailure("Error al registrar usuario: " + e.getMessage());
            }
        } catch (SQLException e) {
            callback.onFailure("Error de conexión a la base de datos: " + e.getMessage());
        }
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
