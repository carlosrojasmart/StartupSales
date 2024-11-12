package Servicios.Productos;

import DB.DatabaseSetup;
import Modelos.Producto;
import Repositorios.Productos.CrearProducto;
import Repositorios.Productos.MostrarProductos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductoServiceTest {

    private static ProductoService productoService;
    private static Connection connection;
    private static CrearProducto crearProducto;
    private static MostrarProductos mostrarProductos;
    private static File imagenPrueba;

    @BeforeAll
    public static void setUpDatabase() throws Exception {
        DatabaseSetup.setUpDatabase();
        connection = new DatabaseSetup().getConnection();
        connection.setAutoCommit(false);
        System.out.println("Conexión a la base de datos exitosa.");

        // Crear archivo de imagen de prueba
        imagenPrueba = new File("src/test/resources/test-image.jpg");
        if (!imagenPrueba.exists()) {
            imagenPrueba.getParentFile().mkdirs();
            imagenPrueba.createNewFile();
        }
    }

    @BeforeEach
    public void setUp() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = new DatabaseSetup().getConnection();
            connection.setAutoCommit(false);
            System.out.println("Conexión a la base de datos restaurada.");
        }

        // Instanciar los repositorios y el servicio
        crearProducto = new CrearProducto();
        mostrarProductos = new MostrarProductos();
        productoService = new ProductoService(crearProducto, mostrarProductos);

        // Limpiar las tablas antes de cada prueba
        limpiarBaseDeDatos();
        connection.commit();

        // Insertar datos de prueba
        insertarDatosIniciales();
    }

    private void limpiarBaseDeDatos() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM carrito_producto")) {
            ps.executeUpdate();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM producto")) {
            ps.executeUpdate();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM tienda")) {
            ps.executeUpdate();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM usuario")) {
            ps.executeUpdate();
        }
        connection.commit();
        System.out.println("Limpieza de base de datos completada.");
    }

    private void insertarDatosIniciales() throws SQLException {
        // Insertar usuario vendedor
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO usuario (nombre, correo_electronico, contrasena, esVendedor) VALUES (?, ?, ?, true)")) {
            ps.setString(1, "Vendedor Test");
            ps.setString(2, "vendedor@test.com");
            ps.setString(3, "password");
            ps.executeUpdate();
        }

        // Insertar tienda
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO tienda (nombre, descripcion, idUsuario) VALUES (?, ?, LAST_INSERT_ID())")) {
            ps.setString(1, "Tienda Test");
            ps.setString(2, "Descripción de tienda test");
            ps.executeUpdate();
        }

        connection.commit();
    }

    private Producto crearProductoPrueba() {
        Producto producto = new Producto();
        producto.setNombre("Producto Test");
        producto.setPrecio(new BigDecimal("99.99"));
        producto.setDescripcion("Descripción del producto test");
        producto.setStock(10);
        producto.setCategoria("Test");
        producto.setIdTienda(1); // ID de la tienda insertada en datos iniciales
        return producto;
    }




    @Test
    public void testObtenerTopProductosMasVendidos() {
        List<Producto> productos = productoService.obtenerTopProductosMasVendidos();
        assertNotNull(productos, "La lista de productos no debería ser null");
        // Verificar que la lista no exceda un límite razonable (por ejemplo, 10)
        assertTrue(productos.size() <= 10, "No deberían haber más de 10 productos en el top");
    }

    @Test
    public void testActualizarProductoInexistente() throws SQLException {
        Producto producto = crearProductoPrueba();
        producto.setIdProducto(999); // ID que no existe
        boolean resultado = productoService.actualizarProducto(producto, imagenPrueba);
        assertFalse(resultado, "No debería permitir actualizar un producto inexistente");
    }

    @Test
    public void testEliminarProductoInexistente() {
        boolean resultado = productoService.eliminarProducto(999); // ID que no existe
        assertFalse(resultado, "No debería permitir eliminar un producto inexistente");
    }
}