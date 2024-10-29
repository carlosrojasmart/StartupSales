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

        //Ejecutar el metodo al probar una contraseña incorrecta
        loginRegister.handleLogin("m.perez@gmail.com", "contraseñaInvalida", callback);

        //Ejecutar el metodo con la contraseña correcta
        loginRegister.handleLogin("m.perez@gmail.com", "12345", callback);

    }

    @Test
    public void registrarUsuario() {
    }
}