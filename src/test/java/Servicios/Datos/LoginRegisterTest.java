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

    //Se instancia un objeto de la clase login y una conexion para la BD
    private LoginRegister loginRegister;
    private Connection connection;

    //Se hace la conexion a la base de datos
    @Before
    public void setUp() throws SQLException{
        loginRegister= new LoginRegister();
        connection= JDBC.ConectarBDPruebas();
    }

    //Despues de la prueba se cierra la conexion y limpia la base de datos
    @After
    public void tearDown() throws SQLException{

        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }


    //Prueba de un login exitoso
    @Test
    public void handleLogin() throws SQLException{
        //Insertar un usuario de prueba
        String sql= "INSERT INTO Usuario (idUsuario, nombre, direccion, correo_electronico, contraseña, saldo_actual, saldo_pagar, esVendedor) VALUES (?,?,?,?,?,?,?,?)";
        try(PreparedStatement pst= connection.prepareStatement(sql)){
            pst.setInt(1,1);
            pst.setString(2, "Mariana");
            pst.setString(3, "calle 88");
            pst.setString(4, "m.perez@gmail.com");
            pst.setString(5, "12345");
            pst.setDouble(6, 100.0 );
            pst.setDouble(7, 50.0);
            pst.setBoolean(8, false);
            //Lo inserta en la BD
            pst.executeUpdate();
        }

        //Se crea un callback de prueba
        LoginRegister.LoginCallback callback= new LoginRegister.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                //Verifica que el mensaje sea el esperado
                assertEquals("Login exitoso ", message);

            }

            @Override
            public void onFailure(String errorMessage) {
               //Falla si el login no es exitoso
                fail("Expected success, but got failurre" + errorMessage);
            }
        };

        //Ejecutar el metodo con la contraseña correcta
        loginRegister.handleLogin("m.perez@gmail.com", "12345", callback);

    }

    @Test
    public void handleLogin_invalidPassword() throws SQLException{
        //Se inserta un usuario de prueba
        String sql = "INSERT INTO Usuario (idUsuario, nombre, direccion, correo_electronico, contraseña, saldo_actual, saldo_pagar, esVendedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, 1);
            ps.setString(2, "Juan");
            ps.setString(3, "calle 7");
            ps.setString(4, "juan@gmail.com");
            ps.setString(5, "12345");
            ps.setDouble(6, 100.0);
            ps.setDouble(7, 50.0);
            ps.setBoolean(8, false);
            ps.executeUpdate();
        }

        // Crear un callback de prueba
        LoginRegister.LoginCallback callback = new LoginRegister.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                // Falla si el login es exitoso con contraseña incorrecta
                fail("Expected failure, but got success: " + message);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Verifica que el mensaje sea el esperado
                assertEquals("Contraseña incorrecta.", errorMessage);
            }
        };

        // Ejecutar el método a probar con una contraseña incorrecta
        loginRegister.handleLogin("juan@gmail.com", "invalidPassword", callback);
    }

    //Prueba cuando no existe el usuario
    @Test
    public void testUserNotFound(){
        // Crear un callback de prueba
        LoginRegister.LoginCallback callback = new LoginRegister.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                // Falla si el login es exitoso
                fail("Expected failure, but got success: " + message);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Verifica que el mensaje sea el esperado
                assertEquals("Correo no existe.", errorMessage);
            }
        };

        // Ejecutar el método a probar
        loginRegister.handleLogin("correoNoExiste@gmail.com", "password", callback);
    }

    //Prueba para verificar el fallo de cuando los campos estan vacios
    @Test
    public void test_EmptyFields(){
        // Crear un callback de prueba
        LoginRegister.LoginCallback callback = new LoginRegister.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                // Falla si el login es exitoso con campos vacíos
                fail("Expected failure, but got success: " + message);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Verifica que el mensaje sea el esperado
                assertEquals("Por favor, completa todos los campos.", errorMessage);
            }
        };

        // Ejecutar el método a probar con campos vacíos
        // Nombre de usuario vacío
        loginRegister.handleLogin("", "password", callback);
        // Contraseña vacía
        loginRegister.handleLogin("username", "", callback);
    }


    @Test
    public void registrarUsuario() {
    }
}