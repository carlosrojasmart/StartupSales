package Repositorios.Perfil;

import DB.JDBC;
import Modelos.UsuarioActivo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModificarPerfil {

    public void guardarImagenPerfil(File archivoImagen) throws SQLException, IOException {
        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "UPDATE Usuario SET imagen_perfil = ? WHERE idUsuario = ?";//consulta para actualizar la imagen de perfil de un usuario
            try (PreparedStatement pstmt = conexion.prepareStatement(sql);//prepara la sentencia SQL y abre un flujo de entrada para el archivo de imagen
                 FileInputStream fis = new FileInputStream(archivoImagen)) {
                pstmt.setBinaryStream(1, fis, (int) archivoImagen.length());//establece el flujo del archivo de imagen como un parámetro binario en la consulta
                pstmt.setInt(2, UsuarioActivo.getIdUsuario());//establece el ID del usuario activo como el segundo parámetro en la consulta
                pstmt.executeUpdate();
            }
        }
    }

    public byte[] cargarImagenPerfil() throws SQLException, IOException {
        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "SELECT imagen_perfil FROM Usuario WHERE idUsuario = ?";//consulta para obtener la imagen de perfil del usuario
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, UsuarioActivo.getIdUsuario());//asigna el ID del usuario activo como parámetro en la consulta
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {//si el resultado contiene la imagen de perfil
                    try (InputStream inputStream = rs.getBinaryStream("imagen_perfil")) {//obtiene el flujo de entrada de la imagen desde la BD
                        if (inputStream != null) {//si se encuentra un flujo de entrada válido, lee y devuelve los bytes de la imagen
                            return inputStream.readAllBytes();
                        }
                    }
                }
            }
        }

        //si no se encuentra una imagen de perfil, carga la imagen predeterminada desde los recursos
        try (InputStream inputStream = getClass().getResourceAsStream("/Imagenes/Cuenta/ImagenPerfilDef.jpg")) {
            //si la imagen predeterminada está disponible, la lee y la devuelve como un array de bytes
            return inputStream != null ? inputStream.readAllBytes() : null;
        }
    }

    public String[] obtenerDatosUsuario(int idUsuario) throws SQLException {
        String[] datos = new String[4];//inicializa un arreglo de Strings para almacenar los datos del usuario
        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "SELECT nombre, correo_electronico, telefono, direccion FROM Usuario WHERE idUsuario = ?";//consulta para obtener los datos del usuario por su ID
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);//asigna el ID del usuario como parámetro en la consulta
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {//si el resultado contiene los datos del usuario
                    //asigna los valores obtenidos a las posiciones correspondientes del arreglo de datos
                    datos[0] = rs.getString("nombre");
                    datos[1] = rs.getString("correo_electronico");
                    datos[2] = rs.getString("telefono");
                    datos[3] = rs.getString("direccion");
                }
            }
        }
        return datos;//devuelve el arreglo de datos del usuario
    }

    public void guardarCambiosPerfil(int idUsuario, String nuevoUsuario, String nuevoCorreo, String nuevaContraseña, String nuevoTelefono, String nuevaDireccion) throws SQLException {
        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "UPDATE Usuario SET nombre = ?, correo_electronico = ?, telefono = ?, direccion = ? WHERE idUsuario = ?";//consulta para actualizar los datos del usuario
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                //establece los nuevos valores para los parámetros de la consulta
                pstmt.setString(1, nuevoUsuario); //establece el nuevo nombre
                pstmt.setString(2, nuevoCorreo);  //setablece el nuevo correo electrónico
                pstmt.setString(3, nuevoTelefono); //establece el nuevo teléfono
                pstmt.setString(4, nuevaDireccion); //establece la nueva dirección
                pstmt.setInt(5, idUsuario); //establece el ID del usuario
                pstmt.executeUpdate();
                if (!nuevaContraseña.isEmpty()) {//si la nueva contraseña no está vacía, actualiza la contraseña del usuario
                    actualizarContraseña(idUsuario, nuevaContraseña); //llama al método para actualizar la contraseña
                }
            }
        }
    }


    private void actualizarContraseña(int idUsuario, String nuevaContraseña) throws SQLException {
        try (Connection conexion = JDBC.ConectarBD()) {
            String sql = "UPDATE Usuario SET contraseña = ? WHERE idUsuario = ?";//consulta para actualizar la contraseña de un usuario
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                String hashedPassword = hashPassword(nuevaContraseña);//genera el hash de la nueva contraseña antes de almacenarla
                pstmt.setString(1, hashedPassword);//establece el valor del parámetro de la consulta (la nueva contraseña)
                pstmt.setInt(2, idUsuario);//establece el ID del usuario
                pstmt.executeUpdate();
            }
        }
    }

    private String hashPassword(String password) {

        try {//se intenta crear un objeto MessageDigest para el algoritmo SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");// Creamos una instancia de MessageDigest con el algoritmo SHA-256
            byte[] hashBytes = md.digest(password.getBytes());//genera el hash de la contraseña en bytes
            StringBuilder hexString = new StringBuilder();//usamos un StringBuilder para construir la cadena hexadecimal del hash
            for (byte b : hashBytes) {//convierte cada byte del hash a su representación hexadecimal
                String hex = Integer.toHexString(0xff & b);//convertimos el byte a un valor hexadecimal
                if (hex.length() == 1) hexString.append('0');//si el valor hexadecimal es de un solo carácter, le agregamos un 0 al inicio

                hexString.append(hex);//añade el valor hexadecimal al StringBuilder
            }
            return hexString.toString();//reto9rna el hash en formato hexadecimal como una cadena
        } catch (NoSuchAlgorithmException e) {
            //si ocurre un error al generar el hash por ejemplo, si el algoritmo no es válido lanza una exception
            throw new RuntimeException("Error al generar el hash de la contraseña", e);
        }
    }
}
