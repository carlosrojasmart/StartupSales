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
    public void testCrearProductoExitoso() throws SQLException {
        Producto producto = crearProductoPrueba();
        boolean resultado = productoService.crearProducto(producto, imagenPrueba);
        assertTrue(resultado, "El producto debería crearse correctamente");

        // Verificar que el producto existe en la base de datos
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM producto WHERE nombre = ?")) {
            ps.setString(1, producto.getNombre());
            var rs = ps.executeQuery();
            assertTrue(rs.next(), "El producto debería existir en la base de datos");
        }
    }

    @Test
    public void testCrearProductoSinImagen() {
        Producto producto = crearProductoPrueba();
        boolean resultado = productoService.crearProducto(producto, null);
        assertFalse(resultado, "No debería permitir crear un producto sin imagen");
    }

    @Test
    public void testActualizarProducto() throws SQLException {
        // Primero crear un producto
        Producto producto = crearProductoPrueba();
        productoService.crearProducto(producto, imagenPrueba);

        // Actualizar el producto
        producto.setNombre("Producto Actualizado");
        producto.setPrecio(new BigDecimal("199.99"));
        boolean resultado = productoService.actualizarProducto(producto, imagenPrueba);
        assertTrue(resultado, "El producto debería actualizarse correctamente");

        // Verificar la actualización
        try (PreparedStatement ps = connection.prepareStatement("SELECT nombre, precio FROM producto WHERE idProducto = ?")) {
            ps.setInt(1, producto.getIdProducto());
            var rs = ps.executeQuery();
            assertTrue(rs.next(), "El producto debería existir");
            assertEquals("Producto Actualizado", rs.getString("nombre"));
            assertEquals(new BigDecimal("199.99"), rs.getBigDecimal("precio"));
        }
    }

    @Test
    public void testEliminarProducto() throws SQLException {
        // Primero crear un producto
        Producto producto = crearProductoPrueba();
        productoService.crearProducto(producto, imagenPrueba);

        // Obtener el ID del producto creado
        int idProducto;
        try (PreparedStatement ps = connection.prepareStatement("SELECT idProducto FROM producto WHERE nombre = ?")) {
            ps.setString(1, producto.getNombre());
            var rs = ps.executeQuery();
            assertTrue(rs.next(), "El producto debería existir");
            idProducto = rs.getInt("idProducto");
        }

        // Eliminar el producto
        boolean resultado = productoService.eliminarProducto(idProducto);
        assertTrue(resultado, "El producto debería eliminarse correctamente");

        // Verificar que el producto ya no existe
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM producto WHERE idProducto = ?")) {
            ps.setInt(1, idProducto);
            var rs = ps.executeQuery();
            assertFalse(rs.next(), "El producto no debería existir después de eliminarlo");
        }
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