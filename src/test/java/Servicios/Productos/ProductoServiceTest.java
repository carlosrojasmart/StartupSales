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

    private static ProductoService productoService; // Servicio a probar
    private static Connection connection; // Conexión a la base de datos
    private static CrearProducto crearProducto; // Repositorio de creación de productos
    private static MostrarProductos mostrarProductos; // Repositorio de visualización de productos
    private static File imagenPrueba; // Imagen de prueba para simular un archivo de imagen

    // Configuración inicial de la base de datos, se ejecuta una sola vez
    @BeforeAll
    public static void setUpDatabase() throws Exception {
        DatabaseSetup.setUpDatabase();
        connection = new DatabaseSetup().getConnection();
        connection.setAutoCommit(false); // Permite revertir cambios en cada prueba
        System.out.println("Conexión a la base de datos exitosa.");

        // Crear archivo de imagen de prueba
        imagenPrueba = new File("src/test/resources/test-image.jpg");
        if (!imagenPrueba.exists()) {
            imagenPrueba.getParentFile().mkdirs();
            imagenPrueba.createNewFile();
        }
    }

    // Configuración que se ejecuta antes de cada prueba
    @BeforeEach
    public void setUp() throws SQLException {
        // Restablece la conexión si está cerrada
        if (connection == null || connection.isClosed()) {
            connection = new DatabaseSetup().getConnection();
            connection.setAutoCommit(false);
            System.out.println("Conexión a la base de datos restaurada.");
        }

        // Instanciar repositorios y servicio a probar
        crearProducto = new CrearProducto();
        mostrarProductos = new MostrarProductos();
        productoService = new ProductoService(crearProducto, mostrarProductos);

        // Limpiar las tablas de la base de datos antes de cada prueba
        limpiarBaseDeDatos();
        connection.commit();

        // Insertar datos de prueba en la base de datos
        insertarDatosIniciales();
    }

    // Elimina los datos de las tablas para evitar interferencia entre pruebas
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
        connection.commit(); // Confirma la limpieza de la base de datos
        System.out.println("Limpieza de base de datos completada.");
    }

    // Inserta un usuario y tienda de prueba en la base de datos
    private void insertarDatosIniciales() throws SQLException {
        // Insertar usuario vendedor de prueba
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO usuario (nombre, correo_electronico, contrasena, esVendedor) VALUES (?, ?, ?, true)")) {
            ps.setString(1, "Vendedora mari");
            ps.setString(2, "mari@gmail.com");
            ps.setString(3, "1234");
            ps.executeUpdate();
        }

        // Insertar tienda asociada al usuario vendedor
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO tienda (nombre, descripcion, idUsuario) VALUES (?, ?, LAST_INSERT_ID())")) {
            ps.setString(1, "Tienda mar");
            ps.setString(2, "Tienda para las vacaciones");
            ps.executeUpdate();
        }

        connection.commit(); // Confirma la inserción de los datos iniciales
    }

    // Método para crear una instancia de producto de prueba
    private Producto crearProductoPrueba() {
        Producto producto = new Producto();
        producto.setNombre("Addidas");
        producto.setPrecio(new BigDecimal("99.99"));
        producto.setDescripcion("tienda de zapatos");
        producto.setStock(10);
        producto.setCategoria("Test");
        producto.setIdTienda(1); // Asigna el ID de la tienda insertada en datos iniciales
        return producto;
    }

    // Prueba para obtener el top de productos más vendidos
    @Test
    public void testObtenerTopProductosMasVendidos() {
        List<Producto> productos = productoService.obtenerTopProductosMasVendidos();
        assertNotNull(productos, "La lista de productos no debería ser null");
        // Verifica que la lista de productos no tenga más de 10 elementos
        assertTrue(productos.size() <= 10, "No deberían haber más de 10 productos en el top. Prueba exitosa");
    }

    // Prueba para intentar actualizar un producto inexistente
    @Test
    public void testActualizarProductoInexistente() throws SQLException {
        Producto producto = crearProductoPrueba();
        producto.setIdProducto(999); // ID que no existe en la base de datos
        boolean resultado = productoService.actualizarProducto(producto, imagenPrueba);
        // Verifica que no se permita actualizar un producto inexistente
        assertFalse(resultado, "No debería permitir actualizar un producto inexistente. Prueba exitosa");
    }

    // Prueba para intentar eliminar un producto inexistente
    @Test
    public void testEliminarProductoInexistente() {
        boolean resultado = productoService.eliminarProducto(999); // ID que no existe en la base de datos
        // Verifica que no se permita eliminar un producto inexistente
        assertFalse(resultado, "No debería permitir eliminar un producto inexistente Prueba exitosa");
    }
}
