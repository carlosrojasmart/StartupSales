package Servicios.Perfil;

import DB.JDBC;
import Servicios.Datos.UsuarioActivo;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModificarPerfil {

    // Método para guardar la imagen de perfil en la base de datos
    public void guardarImagenPerfil(File archivoImagen) throws SQLException, IOException {
        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "UPDATE Usuario SET imagen_perfil = ? WHERE idUsuario = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                FileInputStream fis = new FileInputStream(archivoImagen);
                pstmt.setBinaryStream(1, fis, (int) archivoImagen.length());
                pstmt.setInt(2, UsuarioActivo.getIdUsuario());
                pstmt.executeUpdate();
                fis.close();
            }
        }
    }

    // Método para cargar la imagen de perfil desde la base de datos
    public Image cargarImagenPerfil() throws SQLException {
        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "SELECT imagen_perfil FROM Usuario WHERE idUsuario = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, UsuarioActivo.getIdUsuario());
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    // Si hay una imagen almacenada, devolverla como objeto Image
                    if (rs.getBinaryStream("imagen_perfil") != null) {
                        return new Image(rs.getBinaryStream("imagen_perfil"));
                    }
                }
            }
        }
        // Si no hay imagen, devolver la imagen por defecto
        return new Image(getClass().getResourceAsStream("/Imagenes/Cuenta/ImagenPerfilDef.jpg"));
    }

    public String[] obtenerDatosUsuario(int idUsuario) throws SQLException {
        String[] datos = new String[4]; // Almacenar usuario, correo, telefono, direccion
        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "SELECT nombre, correo_electronico, telefono, direccion FROM Usuario WHERE idUsuario = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    datos[0] = rs.getString("nombre");
                    datos[1] = rs.getString("correo_electronico");
                    datos[2] = rs.getString("telefono");
                    datos[3] = rs.getString("direccion");
                }
            }
        }
        return datos;
    }

    // Método para guardar los cambios en el perfil del usuario
    public void guardarCambiosPerfil(int idUsuario, String nuevoUsuario, String nuevoCorreo, String nuevaContraseña, String nuevoTelefono, String nuevaDireccion) throws SQLException {
        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "UPDATE Usuario SET nombre = ?, correo_electronico = ?, telefono = ?, direccion = ? WHERE idUsuario = ?";

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, nuevoUsuario);
                pstmt.setString(2, nuevoCorreo);
                pstmt.setString(3, nuevoTelefono);
                pstmt.setString(4, nuevaDireccion);
                pstmt.setInt(5, idUsuario);

                pstmt.executeUpdate();

                // Si se cambió la contraseña, actualizarla
                if (!nuevaContraseña.isEmpty()) {
                    actualizarContraseña(idUsuario, nuevaContraseña);
                }
            }
        }
    }

    // Método separado para actualizar la contraseña
    private void actualizarContraseña(int idUsuario, String nuevaContraseña) throws SQLException {
        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "UPDATE Usuario SET contraseña = ? WHERE idUsuario = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                String hashedPassword = hashPassword(nuevaContraseña); // Método para hashear la contraseña
                pstmt.setString(1, hashedPassword);
                pstmt.setInt(2, idUsuario);
                pstmt.executeUpdate();
            }
        }
    }

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





}
