package Servicios.Datos;

import DB.JDBCTestH2;
import Repositorios.Datos.LoginRegister;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*; // Asegúrate de tener esta línea para importar todas las aserciones de JUnit

import static org.junit.jupiter.api.Assertions.*;

class LoginRegisterServiceTest {

    //Se instancia un objeto de la clase de servicio LoginRegisterService
    private LoginRegisterService loginRegisterService;
    private Connection connection;

    //Se hace el before para preparar la BD antes de cada prueba sea ejecutada
    @Before
    public void setUp() throws SQLException {
        //Se crea la conexion a la BD H2
        JDBCTestH2 jdbcH2 = new JDBCTestH2();
        connection = jdbcH2.getConnectionH2();

        //Se inicializa el para el servicio login
        LoginRegister loginRegister = new LoginRegister(connection);
        loginRegisterService = new LoginRegisterService(loginRegister);

        //Se verifica que la conexion se halla realizado correctamente
        assertNotNull("Conexion no esta en nulo",connection);
        assertFalse("Conexion no puede estar cerrada",connection.isClosed());

        //Limpia la base de datos al iniciar cada prueba
        clearDatabase();

    }

    private void clearDatabase() throws SQLException {
        //Se limpia la base de datos
        try(PreparedStatement ps = connection.prepareStatement("DELETE FROM Usuario")){
            ps.executeUpdate();
        }

        try(PreparedStatement ps = connection.prepareStatement("DELETE FROM Carrito")){
            ps.executeUpdate();
        }

    }

    @After
    public void tearDown() throws SQLException {
        //Se cierra la conexion a la BD
        connection.close();
        assertTrue("La conexion debe estar cerrada",connection.isClosed());
    }

    @Test
    public void testLoginExistoso() throws SQLException{
        //Se instancia para realizar las funciones e insertar el usuario
        LoginRegister loginRegister = new LoginRegister(connection);
        LoginRegisterService loginRegisterService = new LoginRegisterService(loginRegister);

        //Se inserta un usuario de prueba y se verifica que el login sea exitoso
        insertarUsuario("m.perez@gmail.com", "12345", 1, 1001);
        assertTrue("Login exitoso",loginRegisterService.handleLogin("m.perez@gmail.com", "12345"));
    }



    @Test
    void handleLogin() {
    }

    @Test
    void registrarUsuario() {
    }


    //Funcion que inserta un usuario en la base de datos
    private void insertarUsuario(String correo, String password, int idUsuario, int idCarrito) throws SQLException {
        try(PreparedStatement ps = connection.prepareStatement("INSERT INTO Usuario (idUsuario, nombre, correo_electronico, contraseña, esVendedor, saldo_actual, saldo_pagar) VALUES (?,?,?,?,?,?,?)")){
            ps.setInt(1,idUsuario);
            ps.setString(2,"Maria Perez");
            ps.setString(3,"m.perez@gmail.com");
            ps.setString(4,"12345");
            ps.setBoolean(5,false);
            ps.setBigDecimal(6,null);
            ps.setBigDecimal(7,null);
            ps.executeUpdate();
        }

        try(PreparedStatement ps = connection.prepareStatement("INSERT INTO Carrito (idCarrito, idUsuario, total) VALUES (?,?,?)")){
            ps.setInt(1,idCarrito);
            ps.setInt(2,idUsuario);
            ps.setBigDecimal(3,null);
            ps.executeUpdate();
        }
    }
}