import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class JDBC {

    public static Connection ConectarBD(){

        Connection conexion;
        String host = "jdbc:mysql://localhost/";
        String user = "root";
        String pass = "password";
        String bd = "FundamentosDatabase";

        System.out.println("Conectando...");

        try {
            conexion = DriverManager.getConnection(host+bd,user,pass);
            System.out.println("Conexion Exitosa!!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        return conexion;

    }


    public static void main(String[] args){
        Connection bd = ConectarBD();
    }

}
