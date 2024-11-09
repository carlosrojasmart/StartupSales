package Servicios.Datos;

import DB.JDBC;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginRegisterTest {

    private LoginRegister loginRegister;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        connection = JDBC.ConectarBDPruebas();
        assertNotNull("Connection should not be null", connection);
        assertFalse("Connection should not be closed", connection.isClosed());

        clearDatabase();
    }

    private void clearDatabase() throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM carrito")) {
            pstmt.executeUpdate();
        }
        try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Usuario")) {
            pstmt.executeUpdate();
        }
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testLoginExitoso() throws SQLException {
        String insertSql = "INSERT INTO Usuario (idUsuario, nombre, direccion, correo_electronico, contraseña, saldo_actual, saldo_pagar, esVendedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstInsert = connection.prepareStatement(insertSql)) {
            pstInsert.setInt(1, 1);
            pstInsert.setString(2, "Mariana");
            pstInsert.setString(3, "calle88");
            pstInsert.setString(4, "m.perez@gmail.com");
            pstInsert.setString(5, loginRegister.hashPassword("12345"));
            pstInsert.setDouble(6, 0.0);
            pstInsert.setDouble(7, 0.0);
            pstInsert.setBoolean(8, false);
            pstInsert.executeUpdate();
        }

        String insertCarritoSql = "INSERT INTO carrito (idCarrito, idUsuario) VALUES (?, ?)";
        try (PreparedStatement pstCarrito = connection.prepareStatement(insertCarritoSql)) {
            pstCarrito.setInt(1, 1001);
            pstCarrito.setInt(2, 1);
            pstCarrito.executeUpdate();
        }

        boolean result = loginRegister.handleLogin("m.perez@gmail.com", "12345");
        assertEquals("Login exitoso.", result);
    }

    @Test
    public void testLoginContrasenaIncorrecta() throws SQLException {
        String sql = "INSERT INTO Usuario (idUsuario, nombre, direccion, correo_electronico, contraseña, saldo_actual, saldo_pagar, esVendedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, 2);
            ps.setString(2, "Juan");
            ps.setString(3, "calle 7");
            ps.setString(4, "juan@gmail.com");
            ps.setString(5, loginRegister.hashPassword("12345"));
            ps.setDouble(6, 0.0);
            ps.setDouble(7, 0.0);
            ps.setBoolean(8, false);
            ps.executeUpdate();
        }

        String insertCarritoSql = "INSERT INTO carrito (idCarrito, idUsuario) VALUES (?, ?)";
        try (PreparedStatement pstCarrito = connection.prepareStatement(insertCarritoSql)) {
            pstCarrito.setInt(1, 1002);
            pstCarrito.setInt(2, 2);
            pstCarrito.executeUpdate();
        }

        boolean result = loginRegister.handleLogin("juan@gmail.com", "contraseñaInvalida");
        assertEquals("Contraseña incorrecta.", result);
    }

    @Test
    public void testLoginUsuarioNoEncontrado() {
        boolean result = loginRegister.handleLogin("correoNoExiste@gmail.com", "password");
        assertEquals("Correo no existe.", result);
    }

    @Test
    public void testLoginCamposVacios() {
        boolean result1 = loginRegister.handleLogin("", "password");
        assertEquals("Por favor, completa todos los campos.", result1);

        boolean result2 = loginRegister.handleLogin("username", "");
        assertEquals("Por favor, completa todos los campos.", result2);
    }

    @Test
    public void testRegistrarUsuario() throws SQLException {
        boolean result = loginRegister.registrarUsuario("Mari", "mari@gmail.com", "123", "536662", "calle87");
        assertEquals("Usuario registrado exitosamente.", result);
    }

    @Test
    public void testRegistrarUsuarioCamposVacios() {
        boolean result = loginRegister.registrarUsuario("", "", "", "", "");
        assertEquals("Por favor, completa todos los campos.", result);
    }

    @Test
    public void testRegistrarUsuarioCorreoDuplicado() throws SQLException {
        String sql = "INSERT INTO Usuario (idUsuario, nombre, direccion, correo_electronico, telefono, contraseña, saldo_actual, saldo_pagar) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, 12);
            ps.setString(2, "Nicolás");
            ps.setString(3, "calle15");
            ps.setString(4, "nicolas@gmail.com");
            ps.setString(5, "7688899");
            ps.setString(6, loginRegister.hashPassword("123"));
            ps.setDouble(7, 0.0);
            ps.setDouble(8, 0.0);
            ps.executeUpdate();
        }

        String insertCarritoSql = "INSERT INTO carrito (idCarrito, idUsuario) VALUES (?, ?)";
        try (PreparedStatement pstCarrito = connection.prepareStatement(insertCarritoSql)) {
            pstCarrito.setInt(1, 1003);
            pstCarrito.setInt(2, 12);
            pstCarrito.executeUpdate();
        }

        String result = loginRegister.registrarUsuario("nico", "nicolas@gmail.com", "123", "654321", "calle8");
        assertTrue(result.contains("Error al registrar usuario."));
    }
}