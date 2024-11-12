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

    @BeforeAll
    public static void setUpDatabase() throws Exception {
        DatabaseSetup.setUpDatabase();
        connection = new DatabaseSetup().getConnection();
        connection.setAutoCommit(false); // Desactivar auto-commit para un mejor control
        System.out.println("Conexión a la base de datos exitosa.");
    }

    @BeforeEach
    public void setUp() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = new DatabaseSetup().getConnection();
            connection.setAutoCommit(false);
            System.out.println("Conexión a la base de datos restaurada.");
        }

        // Instanciar LoginRegister y LoginRegisterService
        LoginRegister loginRegister = new LoginRegister(connection);
        loginRegisterService = new LoginRegisterService(loginRegister);

        // Limpiar las tablas antes de cada prueba
        limpiarBaseDeDatos();
        connection.commit(); // Asegura que los cambios de limpieza se confirmen
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

    // Método para insertar datos de prueba iniciales
    private void insertarDatosIniciales() throws SQLException {
        registrarUsuario("carlos_test@example.com", "Carlos Perez", "12345", "1234567890", "Calle Falsa 123");
    }

    private void registrarUsuario(String correo, String nombre, String contrasena, String telefono, String direccion) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO usuario (nombre, correo_electronico, contrasena, esVendedor, saldo_actual, saldo_pagar) " +
                        "VALUES (?, ?, ?, false, 0, 0)")) {
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, contrasena);
            ps.executeUpdate();
            connection.commit(); // Confirma la transacción después de registrar el usuario
            System.out.println("Usuario registrado: " + correo);
        }
    }

    @Test
    public void testLoginExitoso() throws SQLException {
        // Realizar el login con credenciales correctas
        boolean loginExitoso = loginRegisterService.handleLogin("carlos_test@example.com", "12345");
        assertTrue(loginExitoso, "El login debería ser exitoso con las credenciales correctas");
        System.out.println("Login exitoso.");
    }

    @Test
    public void testLoginFallido() throws SQLException {
        // Intentar hacer login con credenciales incorrectas
        boolean loginFallido = loginRegisterService.handleLogin("carlos_test@example.com", "incorrecta");
        assertFalse(loginFallido, "El login debería fallar con una contraseña incorrecta");
        System.out.println("Login fallido, como se esperaba, con contraseña incorrecta.");
    }

    @Test
    public void testRegistrarUsuario() throws SQLException {
        // Usar un correo electrónico único para esta prueba
        String emailPrueba = "nuevo_usuario_prueba@example.com";
        boolean registroExitoso = loginRegisterService.registrarUsuario("Juan Perez", emailPrueba, "12345", "9876543210", "Calle Nueva 456");
        assertTrue(registroExitoso, "El usuario debería registrarse correctamente");
        connection.commit(); // Confirma la transacción después de registrar el usuario

        // Verificar que el usuario ahora existe en la base de datos
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM usuario WHERE correo_electronico = ?")) {
            ps.setString(1, emailPrueba);
            var resultSet = ps.executeQuery();
            assertTrue(resultSet.next(), "El usuario debería existir en la base de datos");
            System.out.println("El usuario existe en la base de datos: " + emailPrueba);
        }
    }

    @Test
    public void testRegistrarUsuarioExistente() throws SQLException {
        // Asegúrate de que el usuario 'carlos_test@example.com' esté registrado antes de probar el registro duplicado
        boolean registroDuplicado = loginRegisterService.registrarUsuario("Carlos Perez", "carlos_test@example.com", "12345", "1234567890", "Calle Falsa 123");
        assertFalse(registroDuplicado, "No debería permitirse registrar un usuario con el mismo correo electrónico");
        System.out.println("Registro duplicado bloqueado como se esperaba.");
    }

    @Test
    public void testLoginConCamposVacios() throws SQLException {
        // Intentar hacer login con campos vacíos
        boolean loginVacio = loginRegisterService.handleLogin("", "");
        assertFalse(loginVacio, "El login debería fallar cuando los campos están vacíos");
        System.out.println("Login fallido, como se esperaba, con campos vacíos.");
    }

    @Test
    public void testLoginConCorreoInvalido() throws SQLException {
        // Intentar hacer login con un correo electrónico inválido
        boolean loginInvalido = loginRegisterService.handleLogin("correo_invalido", "12345");
        assertFalse(loginInvalido, "El login debería fallar cuando el correo es inválido");
        System.out.println("Login fallido, como se esperaba, con correo inválido.");
    }

    @Test
    public void testLoginConContraseñaInvalida() throws SQLException {
        // Intentar hacer login con una contraseña en blanco
        boolean loginInvalido = loginRegisterService.handleLogin("carlos_test@example.com", "");
        assertFalse(loginInvalido, "El login debería fallar cuando la contraseña está vacía");
        System.out.println("Login fallido, como se esperaba, con contraseña vacía.");
    }

    @Test
    public void testRegistrarUsuarioConCamposVacios() throws SQLException {
        // Intentar registrar un usuario con campos vacíos
        boolean registroVacio = loginRegisterService.registrarUsuario("", "", "", "", "");
        assertFalse(registroVacio, "El registro debería fallar cuando los campos están vacíos");
        System.out.println("Registro fallido, como se esperaba, con campos vacíos.");
    }

    @Test
    public void testRegistrarUsuarioConCorreoInvalido() throws SQLException {
        // Intentar registrar un usuario con un correo inválido
        boolean registroInvalido = loginRegisterService.registrarUsuario("Juan Perez", "correo_invalido", "12345", "9876543210", "Calle Nueva 456");
        assertFalse(registroInvalido, "El registro debería fallar cuando el correo es inválido");
        System.out.println("Registro fallido, como se esperaba, con correo inválido.");
    }

    @Test
    public void testRegistrarUsuarioConContraseñaCorta() throws SQLException {
        // Intentar registrar un usuario con una contraseña demasiado corta
        boolean registroInvalido = loginRegisterService.registrarUsuario("Juan Perez", "nuevo_usuario_prueba@example.com", "123", "9876543210", "Calle Nueva 456");
        assertFalse(registroInvalido, "El registro debería fallar cuando la contraseña es demasiado corta");
        System.out.println("Registro fallido, como se esperaba, con contraseña corta.");
    }
}
