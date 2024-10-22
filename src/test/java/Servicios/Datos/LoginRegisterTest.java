package Servicios.Datos;

import DB.JDBC;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LoginRegisterTest {

        // Variables de instancia para la clase de prueba

        // Instancia de la clase a probar
        private LoginRegister loginRegister;
        // Conexión a la base de datos de pruebas
        private Connection conexion;

        @Before
        public void setUp() throws SQLException {
            // Crear una nueva instancia de LoginRegister para cada prueba
            loginRegister = new LoginRegister();

            // Establecer conexión con la base de datos de pruebas
            conexion = JDBC.ConectarBDPruebas();

            // Limpiar datos existentes y preparar datos de prueba
            limpiarBaseDeDatos();
            insertarDatosDePrueba();
        }


        @After
        public void tearDown() throws SQLException {
            // Limpiar los datos creados durante la prueba
            limpiarBaseDeDatos();

            // Cerrar la conexión a la base de datos si está abierta
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        }

        private void limpiarBaseDeDatos() throws SQLException {
            // Array con los nombres de las tablas a limpiar
            String[] tablas = {"carrito", "Usuario"};

            // Iterar sobre cada tabla y eliminar todos sus registros
            for (String tabla : tablas) {
                try (PreparedStatement pstmt = conexion.prepareStatement("DELETE FROM " + tabla)) {
                    pstmt.executeUpdate();
                }
            }
        }


        private void insertarDatosDePrueba() throws SQLException {
            // SQL para insertar un usuario de prueba
            String sql = "INSERT INTO Usuario (idUsuario, nombre, direccion, correo_electronico, " +
                    "telefono, contraseña, saldo_actual, saldo_pagar, esVendedor) VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                // Establecer los valores para el usuario de prueba
                pstmt.setInt(1, 123456);                  // ID de usuario
                pstmt.setString(2, "Usuario Prueba");     // Nombre
                pstmt.setString(3, "Dirección Prueba");   // Dirección
                pstmt.setString(4, "test@test.com");      // Correo
                pstmt.setString(5, "1234567890");         // Teléfono
                // Contraseña hasheada de "password123"
                pstmt.setString(6, "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f");
                pstmt.setDouble(7, 0.0);                  // Saldo actual
                pstmt.setDouble(8, 0.0);                  // Saldo a pagar
                pstmt.setBoolean(9, false);               // No es vendedor

                // Ejecutar la inserción
                pstmt.executeUpdate();
            }
        }


        @Test
        public void testLoginExitoso() {
            // Arreglo para almacenar el resultado del login y el mensaje
            boolean[] loginExitoso = {false};
            String[] mensajeRecibido = {null};

            // Crear callback para manejar la respuesta del login
            LoginRegister.LoginCallback callback = new LoginRegister.LoginCallback() {
                @Override
                public void onSuccess(String message) {
                    loginExitoso[0] = true;
                    mensajeRecibido[0] = message;
                }

                @Override
                public void onFailure(String errorMessage) {
                    loginExitoso[0] = false;
                    mensajeRecibido[0] = errorMessage;
                }
            };

            // Ejecutar el login con credenciales válidas
            loginRegister.handleLogin("test@test.com", "password123", callback);

            // Verificar que el login fue exitoso
            assertTrue(loginExitoso[0], "El login debería ser exitoso");
            assertEquals("Login exitoso.", mensajeRecibido[0]);
        }

        @Test
        public void testLoginCamposVacios() {
            // Arreglos para almacenar el resultado y mensaje de error
            boolean[] loginExitoso = {true};
            String[] mensajeError = {null};

            // Callback para manejar la respuesta
            LoginRegister.LoginCallback callback = new LoginRegister.LoginCallback() {
                @Override
                public void onSuccess(String message) {
                    loginExitoso[0] = true;
                }

                @Override
                public void onFailure(String errorMessage) {
                    loginExitoso[0] = false;
                    mensajeError[0] = errorMessage;
                }
            };

            // Intentar login con campos vacíos
            loginRegister.handleLogin("", "", callback);

            // Verificar que el login falló con el mensaje correcto
            assertFalse(loginExitoso[0], "El login no debería ser exitoso");
            assertEquals("Por favor, completa todos los campos.", mensajeError[0]);
        }

        /**
         * Prueba de registro exitoso
         * Verifica que se pueda registrar un nuevo usuario correctamente
         */
        @Test
        public void testRegistroExitoso() {
            // Arreglos para almacenar el resultado y mensaje
            boolean[] registroExitoso = {false};
            String[] mensajeRecibido = {null};

            // Callback para manejar la respuesta del registro
            LoginRegister.RegistrationCallback callback = new LoginRegister.RegistrationCallback() {
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

            // Intentar registrar un nuevo usuario
            loginRegister.registrarUsuario(
                    "Nuevo Usuario",
                    "nuevo@test.com",
                    "password123",
                    "9876543210",
                    "Nueva Dirección",
                    callback
            );

            // Verificar que el registro fue exitoso
            assertTrue(registroExitoso[0], "El registro debería ser exitoso");
            assertEquals("Usuario registrado exitosamente.", mensajeRecibido[0]);
        }

        /**
         * Prueba de login con usuario inexistente
         * Verifica que el sistema maneje correctamente intentos de login con usuarios no registrados
         */
        @Test
        public void testLoginUsuarioNoExiste() {
            boolean[] loginExitoso = {true};
            String[] mensajeError = {null};

            LoginRegister.LoginCallback callback = new LoginRegister.LoginCallback() {
                @Override
                public void onSuccess(String message) {
                    loginExitoso[0] = true;
                }

                @Override
                public void onFailure(String errorMessage) {
                    loginExitoso[0] = false;
                    mensajeError[0] = errorMessage;
                }
            };

            // Intentar login con un correo no registrado
            loginRegister.handleLogin("noexiste@test.com", "password123", callback);

            // Verificar que el login falló con el mensaje correcto
            assertFalse(loginExitoso[0], "El login no debería ser exitoso");
            assertEquals("Correo no existe.", mensajeError[0]);
        }


        @Test
        public void testLoginContraseñaIncorrecta() {
            boolean[] loginExitoso = {true};
            String[] mensajeError = {null};

            LoginRegister.LoginCallback callback = new LoginRegister.LoginCallback() {
                @Override
                public void onSuccess(String message) {
                    loginExitoso[0] = true;
                }

                @Override
                public void onFailure(String errorMessage) {
                    loginExitoso[0] = false;
                    mensajeError[0] = errorMessage;
                }
            };

            // Intentar login con contraseña incorrecta
            loginRegister.handleLogin("test@test.com", "contraseñaIncorrecta", callback);

            // Verificar que el login falló con el mensaje correcto
            assertFalse(loginExitoso[0], "El login no debería ser exitoso");
            assertEquals("Contraseña incorrecta.", mensajeError[0]);
        }
}
