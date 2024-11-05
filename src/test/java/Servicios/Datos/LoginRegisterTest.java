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


    // Se instancia un objeto de la clase login y una conexión para la BD
    private LoginRegister loginRegister;
    private Connection connection;

    // Se hace la conexión a la base de datos y se limpia la base de datos
    @Before
    public void setUp() throws SQLException {
        loginRegister = new LoginRegister();
        connection = JDBC.ConectarBDPruebas();
        assertNotNull("Connection should not be null", connection);
        assertFalse("Connection should not be closed", connection.isClosed());
        clearDatabase();
    }

    // Método para limpiar la base de datos cada vez que se va a realzar una prueba
    private void clearDatabase() throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM carrito")) {
            pstmt.executeUpdate();
        }
        try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Usuario")) {
            pstmt.executeUpdate();
        }
    }

    // Después de la prueba se cierra la conexión
    @After
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // Prueba de un login exitoso
    @Test
    public void testLoginExitoso() throws SQLException {
        System.out.println("Ejecutando handleLogin");
        // Insertar un usuario de prueba
        String insertSql = "INSERT INTO Usuario (idUsuario, nombre, direccion, correo_electronico, contraseña, saldo_actual, saldo_pagar, esVendedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstInsert = connection.prepareStatement(insertSql)) {
            pstInsert.setInt(1, 1);
            pstInsert.setString(2, "Mariana");
            pstInsert.setString(3, "calle88");
            pstInsert.setString(4, "m.perez@gmail.com");
            pstInsert.setString(5, loginRegister.hashPassword("12345")); // Asegurarse de usar el método de hash correcto
            pstInsert.setDouble(6, 0.0);
            pstInsert.setDouble(7, 0.0);
            pstInsert.setBoolean(8, false);
            pstInsert.executeUpdate();
        }


        // Crear un callback de prueba
        LoginRegister.LoginCallback callback = new LoginRegister.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                // Verificar que el mensaje sea el esperado
                assertEquals("Login exitoso.", message);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Fallar si el login no es exitoso
                fail("Expected success, got failure :( : " + errorMessage);
            }
        };

        // Ejecutar el método con la contraseña correcta
        loginRegister.handleLogin("m.perez@gmail.com", "12345", callback);
    }

    // Prueba de login con contraseña incorrecta
    @Test
    public void testLoginContrasenaIncorrecta() throws SQLException {
        // Insertar un usuario de prueba
        String sql = "INSERT INTO Usuario (idUsuario, nombre, direccion, correo_electronico, contraseña, saldo_actual, saldo_pagar, esVendedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, 1);
            ps.setString(2, "Juan");
            ps.setString(3, "calle 7");
            ps.setString(4, "juan@gmail.com");
            ps.setString(5, loginRegister.hashPassword("12345"));
            ps.setDouble(6, 0.0);
            ps.setDouble(7, 0.0);
            ps.setBoolean(8, false);
            ps.executeUpdate();
        }

        // Crear un callback de prueba
        LoginRegister.LoginCallback callback = new LoginRegister.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                assertEquals("Contraseña incorrecta.", message);
            }

            @Override
            public void onFailure(String errorMessage) {
                fail("Expected failure, but got success: " + errorMessage);
            }
        };

        // Ejecutar el método a probar con una contraseña incorrecta
        loginRegister.handleLogin("juan@gmail.com", "contraseñaInvalida", callback);
    }

    // Prueba cuando el usuario no existe
    @Test
    public void testLoginUsuarioNoEncontrado() {
        // Crear un callback de prueba
        LoginRegister.LoginCallback callback = new LoginRegister.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected failure, but got success: " + message);
            }

            @Override
            public void onFailure(String errorMessage) {
                assertEquals("Correo no existe.", errorMessage);
            }
        };

        // Ejecutar el método a probar
        loginRegister.handleLogin("correoNoExiste@gmail.com", "password", callback);
    }

    // Prueba para verificar el fallo cuando los campos están vacíos
    @Test
    public void testLoginCamposVacios() {
        // Crear un callback de prueba
        LoginRegister.LoginCallback callback = new LoginRegister.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected failure, but got success: " + message);
            }

            @Override
            public void onFailure(String errorMessage) {
                assertEquals("Por favor, completa todos los campos.", errorMessage);
            }
        };

        // Ejecutar el método a probar con campos vacíos
        loginRegister.handleLogin("", "password", callback);
        loginRegister.handleLogin("username", "", callback);
    }

    // Prueba para cuando se registra un usuario correctamente
    @Test
    public void testRegistrarUsuario() {
        // Se crean dos variables tipo arreglo mutable para poder modificarlas dentro del callback
        //Se inicializa el booleano en false
        boolean[] registroExitoso = {false};
        //Se inicializa el mensaje en null
        String[] mensajeRecibido = {null};

        //Se crea un callback de prueba para el registro
        LoginRegister.RegistrationCallback callback = new LoginRegister.RegistrationCallback() {

            //Esta para en el caso que el registro sea exitoso el booleano cambia a true y si falla se queda en falso y recibe ensaje de error
            @Override
            public void onSuccess(String message) {
                registroExitoso[0] = true;
                mensajeRecibido[0] = message;
            }

            @Override
            public void onFailure(String errorMessage) {
                registroExitoso[0] = false;
                mensajeRecibido[0] = errorMessage;
            }
        };

        //Se ejecuta el método para registrar un usuario mandando un usuario de ejemplo
        loginRegister.registrarUsuario("Mari", "mari@gmail.com", loginRegister.hashPassword("123"), "536662", "calle87", callback);
        assertTrue(registroExitoso[0]);
        assertEquals("Usuario registrado exitosamente.", mensajeRecibido[0]);
    }

    //Prueba para registrar usuario en el caso que se manden campos vacios
    @Test
    public void testRegistrarUsuarioCamposVacios() {
        // Se crean dos variables tipo arreglo mutable para poder modificarlas dentro del callback
        //Se inicializa el booleano en false
        boolean[] registroExitoso = {true};
        //Se inicializa el mensaje en null
        String[] mensajeError = {null};

        //Se crea un callback de prueba para el registro
        LoginRegister.RegistrationCallback callback = new LoginRegister.RegistrationCallback() {
            @Override
            public void onSuccess(String message) {
                registroExitoso[0] = true;
            }

            @Override
            public void onFailure(String errorMessage) {
                registroExitoso[0] = false;
                mensajeError[0] = errorMessage;
            }
        };

        //Se ejecuta el método para registrar un usuario mandando campos vacíos
        loginRegister.registrarUsuario("", "", "", "", "", callback);
        assertFalse(registroExitoso[0]);
        assertEquals("Por favor, completa todos los campos.", mensajeError[0]);
    }

    //Prueba para registrar usuario en el caso que quiera ingresar con un correo ya registrado
    @Test
    public void testRegistrarUsuarioCorreoDuplicado() throws SQLException {
        // Insertar un usuario para poder probar luego el ingreso de un correo duplicado
        String sql = "INSERT INTO Usuario (idUsuario, nombre, direccion, correo_electronico, telefono, contraseña, saldo_actual, saldo_pagar) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, 12);
            ps.setString(2, "nicolas");
            ps.setString(3, "calle15");
            ps.setString(4, "nicolas@gmail.com");
            ps.setString(5, "7688899");
            ps.setString(6, loginRegister.hashPassword("1234"));
            ps.setDouble(7, 0.0);
            ps.setDouble(8, 0.0);
            ps.executeUpdate();
        }

        // Se crean dos variables tipo arreglo mutable para poder modificarlas dentro del callback
        //Se inicializa el booleano en false
        boolean[] registroExitoso = {true};
        //Se inicializa el mensaje en null
        String[] mensajeError = {null};

        //Se crea un callback de prueba para el registro
        LoginRegister.RegistrationCallback callback = new LoginRegister.RegistrationCallback() {
            @Override
            public void onSuccess(String message) {
                registroExitoso[0] = true;
            }

            @Override
            public void onFailure(String errorMessage) {
                registroExitoso[0] = false;
                mensajeError[0] = errorMessage;
            }
        };

        //Se ejecuta el método para registrar un usuario mandando un correo que ya existe
        loginRegister.registrarUsuario("nico", "nicolas@gmail.com", loginRegister.hashPassword("123"), "654321", "calle8", callback);
        assertFalse(registroExitoso[0]);
        assertTrue(mensajeError[0].contains("Error al registrar usuario"));
    }
}

