package Servicios.Datos;

import DB.DatabaseSetup;
import Repositorios.Datos.LoginRegister;
import Servicios.Datos.LoginRegisterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LoginRegisterServiceTest {

    private static LoginRegisterService loginRegisterService;
    private static Connection connection;

    // Configuración de la base de datos antes de todas las pruebas
    @BeforeAll
    public static void setUpDatabase() throws Exception {
        DatabaseSetup.setUpDatabase(); // Inicializa la base de datos
        connection = new DatabaseSetup().getConnection(); // Obtiene la conexión
        connection.setAutoCommit(false); // Desactiva auto-commit para controlar transacciones en pruebas
        System.out.println("Conexión a la base de datos exitosa.");
    }

    // Configuración antes de cada prueba
    @BeforeEach
    public void setUp() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = new DatabaseSetup().getConnection();
            connection.setAutoCommit(false); // Desactiva auto-commit para control de transacciones
            System.out.println("Conexión a la base de datos restaurada.");
        }

        // Crea instancias de `LoginRegister` y `LoginRegisterService`
        LoginRegister loginRegister = new LoginRegister(connection);
        loginRegisterService = new LoginRegisterService(loginRegister);

        // Limpia las tablas de la base de datos antes de cada prueba
        limpiarBaseDeDatos();
        connection.commit(); // Asegura que los cambios se confirmen
    }

    // Método para limpiar las tablas en el orden correcto
    private void limpiarBaseDeDatos() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM carrito_producto")) {
            ps.executeUpdate();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM carrito")) {
            ps.executeUpdate();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM producto")) {
            ps.executeUpdate();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM tienda")) {
            ps.executeUpdate();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM usuario")) {
            ps.executeUpdate();
        }
        connection.commit(); // Confirma la transacción después de la limpieza
        System.out.println("Limpieza de base de datos completada.");
    }

    // Método auxiliar para insertar datos iniciales para pruebas
    private void insertarDatosIniciales() throws SQLException {
        registrarUsuario("carlos_test@example.com", "Carlos Perez", "12345", "1234567890", "Calle Falsa 123");
    }

    // Método para registrar un usuario de prueba en la base de datos
    private void registrarUsuario(String correo, String nombre, String contrasena, String telefono, String direccion) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO usuario (nombre, correo_electronico, contrasena, esVendedor, saldo_actual, saldo_pagar) " +
                        "VALUES (?, ?, ?, false, 0, 0)")) {
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, contrasena);
            ps.executeUpdate();
            connection.commit(); // Confirma la transacción después del registro del usuario
            System.out.println("Usuario registrado: " + correo);
        }
    }

    // Prueba para verificar login fallido con credenciales incorrectas
    @Test
    public void testLoginFallido() throws SQLException {
        boolean loginFallido = loginRegisterService.handleLogin("carlos_test@example.com", "incorrecta");
        assertFalse(loginFallido, "El login debería fallar con una contraseña incorrecta");
        System.out.println("Login fallido, como se esperaba, con contraseña incorrecta.");
    }

    // Prueba para verificar el registro de un usuario ya existente
    @Test
    public void testRegistrarUsuarioExistente() throws SQLException {
        // Verifica que no se permite registrar un usuario duplicado
        boolean registroDuplicado = loginRegisterService.registrarUsuario("Carlos Perez", "carlos_test@example.com", "12345", "1234567890", "Calle Falsa 123");
        assertFalse(registroDuplicado, "No debería permitirse registrar un usuario con el mismo correo electrónico");
        System.out.println("Registro duplicado bloqueado como se esperaba.");
    }

    // Prueba para verificar que el login falla si los campos están vacíos
    @Test
    public void testLoginConCamposVacios() throws SQLException {
        boolean loginVacio = loginRegisterService.handleLogin("", "");
        assertFalse(loginVacio, "El login debería fallar cuando los campos están vacíos");
        System.out.println("Login fallido, como se esperaba, con campos vacíos.");
    }

    // Prueba para verificar que el login falla con un correo electrónico inválido
    @Test
    public void testLoginConCorreoInvalido() throws SQLException {
        boolean loginInvalido = loginRegisterService.handleLogin("correo_invalido", "12345");
        assertFalse(loginInvalido, "El login debería fallar cuando el correo es inválido");
        System.out.println("Login fallido, como se esperaba, con correo inválido.");
    }

    // Prueba para verificar que el login falla con una contraseña vacía
    @Test
    public void testLoginConContraseñaInvalida() throws SQLException {
        boolean loginInvalido = loginRegisterService.handleLogin("carlos_test@example.com", "");
        assertFalse(loginInvalido, "El login debería fallar cuando la contraseña está vacía");
        System.out.println("Login fallido, como se esperaba, con contraseña vacía.");
    }

    // Prueba para verificar que el registro falla si los campos están vacíos
    @Test
    public void testRegistrarUsuarioConCamposVacios() throws SQLException {
        boolean registroVacio = loginRegisterService.registrarUsuario("", "", "", "", "");
        assertFalse(registroVacio, "El registro debería fallar cuando los campos están vacíos");
        System.out.println("Registro fallido, como se esperaba, con campos vacíos.");
    }

    // Prueba para verificar que el registro falla con un correo inválido
    @Test
    public void testRegistrarUsuarioConCorreoInvalido() throws SQLException {
        boolean registroInvalido = loginRegisterService.registrarUsuario("Juan Perez", "correo_invalido", "12345", "9876543210", "Calle Nueva 456");
        assertFalse(registroInvalido, "El registro debería fallar cuando el correo es inválido");
        System.out.println("Registro fallido, como se esperaba, con correo inválido.");
    }

    // Prueba para verificar que el registro falla con una contraseña demasiado corta
    @Test
    public void testRegistrarUsuarioConContraseñaCorta() throws SQLException {
        boolean registroInvalido = loginRegisterService.registrarUsuario("Juan Perez", "nuevo_usuario_prueba@example.com", "123", "9876543210", "Calle Nueva 456");
        assertFalse(registroInvalido, "El registro debería fallar cuando la contraseña es demasiado corta");
        System.out.println("Registro fallido, como se esperaba, con contraseña corta.");
    }
}
