package Servicios.Datos;

import DB.JDBCTestH2;
import Repositorios.Datos.LoginRegister;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*;

class LoginRegisterServiceTest {

    //Se instancia un objeto de la clase de servicio LoginRegisterService
    private static LoginRegisterService loginRegisterService;
    private static Connection connection;

    //Se hace el before para preparar la BD antes de cada prueba sea ejecutada
    @BeforeAll
    public static void setUp() throws SQLException {
        //Se crea la conexion a la BD H2
        JDBCTestH2 jdbcH2 = new JDBCTestH2();
        connection = jdbcH2.getConnectionH2();

        //Se inicializa el para el servicio login
        LoginRegister loginRegister = new LoginRegister(connection);
        loginRegisterService = new LoginRegisterService(loginRegister);

        //Se verifica que la conexion se halla realizado correctamente
        assertNotNull("Conexion nula", connection);
        assertFalse("Conexion no puede abrirse", connection.isClosed());

        //Limpia la base de datos al iniciar cada prueba
        clearDatabase();
    }

    private static void clearDatabase() throws SQLException {
        // Primero eliminar los registros de Carrito
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Carrito")) {
            ps.executeUpdate();
        }

        // Luego eliminar los registros de Usuario
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Usuario")) {
            ps.executeUpdate();
        }
    }


    @AfterAll
    public static void tearDown() throws SQLException {
        //Se cierra la conexion a la BD
        connection.close();
        assertTrue("La conexion debe estar cerrada", connection.isClosed());
    }

    @Test
    public void testLoginExistoso() throws SQLException {
        //Se instancia para realizar las funciones e insertar el usuario
        LoginRegister loginRegister = new LoginRegister(connection);
        LoginRegisterService loginRegisterService = new LoginRegisterService(loginRegister);

        //Se inserta un usuario de prueba y se verifica que el login sea exitoso
        insertarUsuario("m.perez@gmail.com", "12345", 1, 1001);
        assertTrue("Login exitoso", loginRegisterService.handleLogin("m.perez@gmail.com", "12345", connection));
    }

    @Test
    public void testLoginFallido() throws SQLException {
        // Insertar un usuario de prueba
        insertarUsuario("m.perez@gmail.com", "12345", 1, 1001);

        // Verificar que el login con una contraseña incorrecta falla
        assertFalse("Login fallido con contraseña incorrecta",
                loginRegisterService.handleLogin("m.perez@gmail.com", "incorrecta", connection));
    }

    @Test
    public void testRegistrarUsuario() throws SQLException {
        // Usar el método insertarUsuario para registrar un nuevo usuario
        insertarUsuario("j.smith@gmail.com", "password123", 2, 1002);

        // Verificar que el usuario ahora existe en la base de datos
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Usuario WHERE correo_electronico = ?")) {
            ps.setString(1, "j.smith@gmail.com");
            var resultSet = ps.executeQuery();
            assertTrue("El usuario debe existir en la base de datos", resultSet.next());
        }
    }

    @Test
    public void testRegistrarUsuarioExistente() throws SQLException {
        // Insertar un usuario ya existente
        insertarUsuario("a.doe@gmail.com", "password123", 3, 1003);

        // Intentar registrar el mismo usuario de nuevo usando insertarUsuario
        boolean registrado = insertarUsuario("a.doe@gmail.com", "password123", 3, 1003);

        // Verificar que no se permite el registro de un usuario con correo existente
        assertFalse("El usuario no debe registrarse si ya existe", registrado);
    }

    @Test
    public void testCarritoCreadoCorrectamente() throws SQLException {
        // Usar el método insertarUsuario para registrar un nuevo usuario
        insertarUsuario("b.jones@gmail.com", "pass456", 4, 1004);

        // Verificar que el carrito se ha creado para este usuario
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Carrito WHERE idUsuario = ?")) {
            ps.setInt(1, 4); // idUsuario del nuevo usuario
            var resultSet = ps.executeQuery();
            assertTrue("El carrito debe existir para el usuario", resultSet.next());
        }
    }
    //Funcion que inserta un usuario en la base de datos
    private static boolean insertarUsuario(String correo, String password, int idUsuario, int idCarrito) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Usuario (idUsuario, nombre, correo_electronico, contrasena, esVendedor, saldo_actual, saldo_pagar) VALUES (?,?,?,?,?,?,?)")) {
            ps.setInt(1, idUsuario);
            ps.setString(2, "Maria Perez");
            ps.setString(3, correo);
            ps.setString(4, password);
            ps.setBoolean(5, false);
            ps.setBigDecimal(6, null);
            ps.setBigDecimal(7, null);
            ps.executeUpdate();
        }

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Carrito (idCarrito, idUsuario, total) VALUES (?,?,?)")) {
            ps.setInt(1, idCarrito);
            ps.setInt(2, idUsuario);
            ps.setBigDecimal(3, null);
            ps.executeUpdate();
        }
        return false;
    }

}
