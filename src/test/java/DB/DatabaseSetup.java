package DB;

import org.junit.jupiter.api.BeforeAll;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    // Define las constantes para la conexión a la base de datos
    private static final String URL = "jdbc:h2:~/test;DB_CLOSE_DELAY=-1;MODE=MYSQL";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    @BeforeAll
    public static void setUpDatabase() throws Exception {
        // Conectar a la base de datos H2 en modo MySQL
        String url = "jdbc:h2:~/test";
        String user = "sa";
        String password = "";
        Connection connection = DriverManager.getConnection(url, user, password);

        // Script SQL para crear las tablas necesarias en el orden correcto
        String sqlScript = """
            CREATE TABLE IF NOT EXISTS usuario (
              idUsuario INT AUTO_INCREMENT PRIMARY KEY,
              nombre VARCHAR(100) NOT NULL,
              direccion VARCHAR(255) DEFAULT NULL,
              correo_electronico VARCHAR(100) NOT NULL UNIQUE,
              telefono VARCHAR(20) DEFAULT NULL,
              contrasena VARCHAR(255) NOT NULL,
              imagen_perfil BLOB,
              esVendedor BOOLEAN DEFAULT FALSE,
              saldo_actual DECIMAL(10, 2) DEFAULT 0.00,
              saldo_pagar DECIMAL(10, 2) DEFAULT 0.00
            );

            CREATE TABLE IF NOT EXISTS tienda (
              idTienda INT AUTO_INCREMENT PRIMARY KEY,
              nombre VARCHAR(100) NOT NULL,
              descripcion TEXT,
              idUsuario INT,
              categoria VARCHAR(50) DEFAULT NULL,
              imagenTienda BLOB,
              FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario)
            );

            CREATE TABLE IF NOT EXISTS producto (
              idProducto INT AUTO_INCREMENT PRIMARY KEY,
              nombre VARCHAR(100) NOT NULL,
              precio DECIMAL(12, 2) DEFAULT NULL,
              descripcion TEXT,
              stock INT DEFAULT 0,
              categoria VARCHAR(50) DEFAULT NULL,
              idTienda INT,
              imagenProducto BLOB,
              FOREIGN KEY (idTienda) REFERENCES tienda(idTienda)
            );

            CREATE TABLE IF NOT EXISTS carrito (
              idCarrito INT AUTO_INCREMENT PRIMARY KEY,
              idUsuario INT NOT NULL,
              total DECIMAL(10, 2) DEFAULT 0.00,
              codigoPromocional VARCHAR(50) DEFAULT NULL,
              FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario)
            );

            CREATE TABLE IF NOT EXISTS carrito_producto (
              idCarrito INT NOT NULL,
              idProducto INT NOT NULL,
              cantidad INT NOT NULL,
              PRIMARY KEY (idCarrito, idProducto),
              FOREIGN KEY (idCarrito) REFERENCES carrito(idCarrito),
              FOREIGN KEY (idProducto) REFERENCES producto(idProducto)
            );

            CREATE TABLE IF NOT EXISTS compra (
              idCompra INT AUTO_INCREMENT PRIMARY KEY,
              idUsuario INT,
              total_compra DECIMAL(10, 2) DEFAULT NULL,
              fecha DATE DEFAULT NULL,
              hora TIME DEFAULT NULL,
              FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario)
            );

            CREATE TABLE IF NOT EXISTS compra_producto (
              idCompra INT NOT NULL,
              idProducto INT NOT NULL,
              cantidad INT DEFAULT NULL,
              PRIMARY KEY (idCompra, idProducto),
              FOREIGN KEY (idCompra) REFERENCES compra(idCompra) ON DELETE CASCADE,
              FOREIGN KEY (idProducto) REFERENCES producto(idProducto)
            );

            CREATE TABLE IF NOT EXISTS venta (
              idVenta INT AUTO_INCREMENT PRIMARY KEY,
              fecha DATE NOT NULL,
              total DECIMAL(10, 2) NOT NULL,
              metodo_pago VARCHAR(50) DEFAULT NULL,
              idCliente INT,
              idEmpleado INT,
              FOREIGN KEY (idCliente) REFERENCES usuario(idUsuario),
              FOREIGN KEY (idEmpleado) REFERENCES usuario(idUsuario)
            );

            CREATE TABLE IF NOT EXISTS detalleventa (
              idDetalleVenta INT AUTO_INCREMENT PRIMARY KEY,
              idVenta INT,
              idProducto INT,
              cantidad INT NOT NULL,
              subtotal DECIMAL(10, 2) NOT NULL,
              FOREIGN KEY (idVenta) REFERENCES venta(idVenta),
              FOREIGN KEY (idProducto) REFERENCES producto(idProducto)
            );

            CREATE TABLE IF NOT EXISTS envio (
              idEnvio INT AUTO_INCREMENT PRIMARY KEY,
              direccionDestino VARCHAR(255) DEFAULT NULL,
              fechaEnvio DATE DEFAULT NULL,
              fechaEntregaEstimada DATE DEFAULT NULL,
              estadoEnvio VARCHAR(50) DEFAULT NULL,
              costoEnvio DECIMAL(10, 2) DEFAULT NULL,
              metodoEnvio VARCHAR(50) DEFAULT NULL,
              idVenta INT,
              FOREIGN KEY (idVenta) REFERENCES venta(idVenta)
            );

            CREATE TABLE IF NOT EXISTS factura (
              numero_factura INT AUTO_INCREMENT PRIMARY KEY,
              fecha DATE NOT NULL,
              total DECIMAL(10, 2) NOT NULL,
              idVenta INT,
              cantidad_total INT NOT NULL,
              FOREIGN KEY (idVenta) REFERENCES venta(idVenta)
            );

            CREATE TABLE IF NOT EXISTS inventario (
              idInventario INT AUTO_INCREMENT PRIMARY KEY,
              idProducto INT,
              cantidad_total INT NOT NULL,
              tipo_producto VARCHAR(50) DEFAULT NULL,
              caracteristica_producto TEXT,
              FOREIGN KEY (idProducto) REFERENCES producto(idProducto)
            );
        """;

        // Ejecutar el script de creación de tablas
        Statement statement = connection.createStatement();
        statement.execute(sqlScript);

        // Cerrar la conexión
        statement.close();
        connection.close();
    }

    // Método para obtener una conexión a la base de datos
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
