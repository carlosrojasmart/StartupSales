package DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCTestH2 {
    Connection connection = null;

    //Se definen las variables para conectarse al H2
    public String Username = "sa";
    public String Password = "";
    public String Url = "jdbc:h2:~/test";
    public String Driver = "org.h2.Driver";

    public void getConnectionH2() {
        try {
            Class.forName(Driver);
            connection = DriverManager.getConnection(Url, Username, Password);
            System.out.println("Conexión exitosa a la base de datos H2");
        } catch (Exception e) {
            System.out.println("Error en la conexión a la base de datos H2");
            e.printStackTrace();

        }
    }
}


