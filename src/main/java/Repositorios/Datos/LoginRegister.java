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

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");//obtiene una instancia del algoritmo SHA-256 para calcular el hash
            byte[] hashBytes = md.digest(password.getBytes());//convierte la contraseña en un array de bytes y calcula el hash
            StringBuilder hexString = new StringBuilder();//crea un StringBuilder para construir el hash en formato hexadecimal

            for (byte b : hashBytes) {// Itera sobre cada byte del hash generado
                String hex = Integer.toHexString(0xff & b);//convierte el byte a hexadecimal y asegura que tenga dos dígitos
                if (hex.length() == 1) hexString.append('0'); //añade un '0' si el hex es de un dígito
                hexString.append(hex); //añade el valor hexadecimal al StringBuilder
            }

            //retorna el hash de la contraseña en formato hexadecimal
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar el hash de la contraseña", e);//lanza una excepción si el algoritmo SHA-256 no está disponible
        }
    }


    public boolean buscarUsuarioPorCorreo(String correo, String password) {
        //consulta SQL para seleccionar los datos del usuario en base al correo
        String sql = "SELECT idUsuario, nombre, correo_electronico, contraseña, esVendedor, saldo_actual, saldo_pagar FROM Usuario WHERE correo_electronico = ?";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, correo);//obtiene laconsulta en el indice 1 de correo
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {//verifica si existe el correo proporcionado
                String storedPassword = rs.getString("contraseña");//obtiene la contraseña en la BD
                if (storedPassword.equals(hashPassword(password))) {//compara la contraseña almacenada con la contraseña ingresada (despues de hacerle hash)
                    int idUsuario = rs.getInt("idUsuario");//si coinciden obtiene los datos del usuario
                    String nombre = rs.getString("nombre");//--->
                    boolean esVendedor = rs.getBoolean("esVendedor");//--->
                    BigDecimal saldoActual = rs.getBigDecimal("saldo_actual");//--->
                    BigDecimal saldoPagar = rs.getBigDecimal("saldo_pagar");//--->
                    int idCarrito = obtenerIdCarritoDesdeBD(idUsuario);//obtiene idCarrito del usuario en la base de datos
                    UsuarioActivo.setUsuarioActivo(idUsuario, nombre, correo, esVendedor, idCarrito, saldoActual, saldoPagar);//establece el usuario activo actualizando sus datos
                    return true;//retorna true indicando que el usuario ha sido verificado
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();//tira excepción si ocurre un error al interactuar con la BD
        }
        return false;//retorna false si no se encontró el usuario o si la autenticación falló
    }


    public int insertarUsuario(String usuario, String direccion, String correo, String telefono, String hashedPassword) {
        //consulta SQL para insertar un nuevo usuario en Usuario
        String sql = "INSERT INTO Usuario (nombre, direccion, correo_electronico, telefono, contraseña, saldo_actual, saldo_pagar) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Asigna valores a los parámetros de la consulta en el orden correspondiente
            pstmt.setString(1, usuario);//usuario
            pstmt.setString(2, direccion);//dirección del usuario
            pstmt.setString(3, correo);//crreo electrónico del usuario
            pstmt.setString(4, telefono);//teléfono del usuario
            pstmt.setString(5, hashedPassword);//contraseña hasheada del usuario
            pstmt.setBigDecimal(6, BigDecimal.ZERO);//saldo actual, inicializado a 0
            pstmt.setBigDecimal(7, BigDecimal.ZERO);//saldo a pagar, inicializado a 0
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {//si se insertó al menos una fila, obtiene el ID generado automáticamente
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);//retorna el ID del usuario recién insertado
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public int obtenerIdCarritoDesdeBD(int idUsuario) {
        String sql = "SELECT idCarrito FROM carrito WHERE idUsuario = ?";//consulta para seleccionar el idCarrito a un idUsuario
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);//obtiene la consulta de idUsuario en el indice 1
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {//verifica si existe un registro que coincida con el idUsuario proporcionado
                return rs.getInt("idCarrito");//retorna el idCarrito obtenido de la consulta
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public int crearCarritoParaUsuario(int idUsuario) {
        String sql = "INSERT INTO carrito (idUsuario) VALUES (?)";//consulta para insertar un nuevo carrito de un idUsuario
        try (Connection conexion = JDBC.ConectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, idUsuario);//obtiene la consulta de idUsuario en el indice 1
            pstmt.executeUpdate();
            ResultSet generatedKeys = pstmt.getGeneratedKeys();//obtiene las claves generadas de idCarrito de la inserción


            if (generatedKeys.next()) {//si se generó un ID, lo retorna
                return generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
