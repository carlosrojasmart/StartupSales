package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {

    // Método para conectar a la base de datos
    public static Connection ConectarBD(String bd) {

        Connection conexion = null;
        String host = "localhost";  // Cambiar según la configuración del host
        String user = "root";       // Cambiar según el usuario configurado
        String pass = "password";   // Cambiar por la contraseña correcta

        System.out.println("Conectando a la base de datos...");

        try {
            // Intentamos cargar el driver (opcional para versiones modernas de JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Intentamos conectar a la base de datos con el nombre proporcionado
            conexion = DriverManager.getConnection("jdbc:mysql://" + host + "/" + bd, user, pass);
            System.out.println("Conexión exitosa a la base de datos: " + bd);

        } catch (SQLException e) {
            System.err.println("Error en la conexión: " + e.getMessage());
            e.printStackTrace(); // Muestra el rastreo completo del error
        } catch (ClassNotFoundException e) {
            System.err.println("Driver de MySQL no encontrado: " + e.getMessage());
        }

        return conexion; // Puede ser null si falló la conexión
    }

    // Método de prueba para conectar a la base principal
    public static Connection ConectarBD() {
        return ConectarBD("FundamentosDatabase");
    }

    // Método de prueba para conectar a la base de pruebas
    public static Connection ConectarBDPruebas() {
        return ConectarBD("modelopruebastartupsales");
    }
}
