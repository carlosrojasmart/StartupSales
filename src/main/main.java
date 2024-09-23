package main;

import java.sql.Connection;
import static DB.JDBC.ConectarBD;

public class main {

    public static void main(String[] args){
        Connection bd = ConectarBD();
    }
}
