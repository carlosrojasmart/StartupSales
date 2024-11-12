package Servicios.Tienda;

import DB.DatabaseSetup;
import Modelos.Tienda;
import Repositorios.Tienda.CrearTienda;
import Repositorios.Tienda.MostrarTiendas;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TiendaServiceTest {

    private static TiendaService tiendaService;
    private static Connection connection;
    private static CrearTienda crearTienda;
    private static MostrarTiendas mostrarTiendas;
    private static File imagenPrueba;
    private static int usuarioTestId;

    @BeforeAll
    public static void setUpDatabase() throws Exception {
        DatabaseSetup.setUpDatabase();
        connection = new DatabaseSetup().getConnection();
        connection.setAutoCommit(false);
        System.out.println("Conexión a la base de datos exitosa.");

        // Crear archivo de imagen de prueba
        imagenPrueba = new File("src/test/resources/test-store-image.jpg");
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
        crearTienda = new CrearTienda();
        mostrarTiendas = new MostrarTiendas();
        tiendaService = new TiendaService(mostrarTiendas, crearTienda);

        // Limpiar las tablas antes de cada prueba
        limpiarBaseDeDatos();
        connection.commit();

        // Insertar datos de prueba
        insertarDatosIniciales();
    }

    private void limpiarBaseDeDatos() throws SQLException {
        String[] tablas = {
                "carrito_producto",
                "producto",
                "tienda",
                "usuario"
        };

        for (String tabla : tablas) {
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM " + tabla)) {
                ps.executeUpdate();
            }
        }
        connection.commit();
        System.out.println("Limpieza de base de datos completada.");
    }

    private void insertarDatosIniciales() throws SQLException {
        // Insertar usuario de prueba
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO usuario (nombre, correo_electronico, contrasena, esVendedor) VALUES (?, ?, ?, false)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "Usuario Test");
            ps.setString(2, "test@example.com");
            ps.setString(3, "password");
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                usuarioTestId = rs.getInt(1);
            }
        }
        connection.commit();
    }

    @Test
    public void testCrearTiendaExitoso() throws SQLException {
        boolean resultado = tiendaService.crearTienda(
                "Tienda Test",
                "Descripción de prueba",
                usuarioTestId,
                "Categoría Test",
                imagenPrueba
        );
        assertTrue(resultado, "La tienda debería crearse correctamente");

        // Verificar que la tienda existe en la base de datos
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM tienda WHERE nombre = ? AND idUsuario = ?")) {
            ps.setString(1, "Tienda Test");
            ps.setInt(2, usuarioTestId);
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next(), "La tienda debería existir en la base de datos");

            // Verificar que el usuario ahora es vendedor
            try (PreparedStatement psUser = connection.prepareStatement(
                    "SELECT esVendedor FROM usuario WHERE idUsuario = ?")) {
                psUser.setInt(1, usuarioTestId);
                ResultSet rsUser = psUser.executeQuery();
                assertTrue(rsUser.next() && rsUser.getBoolean("esVendedor"),
                        "El usuario debería ser marcado como vendedor");
            }
        }
    }

    @Test
    public void testCrearTiendaSinImagen() throws SQLException {
        boolean resultado = tiendaService.crearTienda(
                "Tienda Sin Imagen",
                "Descripción de prueba",
                usuarioTestId,
                "Categoría Test",
                null
        );
        assertTrue(resultado, "La tienda debería crearse correctamente sin imagen");
    }

    @Test
    public void testObtenerTiendasDestacadas() {
        List<Tienda> tiendasDestacadas = tiendaService.obtenerTiendasDestacadas();
        assertNotNull(tiendasDestacadas, "La lista de tiendas destacadas no debería ser null");
    }

    @Test
    public void testObtenerTiendasPorUsuario() throws SQLException {
        // Crear algunas tiendas para el usuario
        tiendaService.crearTienda("Tienda 1", "Desc 1", usuarioTestId, "Cat 1", null);
        tiendaService.crearTienda("Tienda 2", "Desc 2", usuarioTestId, "Cat 2", null);

        List<Tienda> tiendas = tiendaService.obtenerTiendas(usuarioTestId);
        assertNotNull(tiendas, "La lista de tiendas no debería ser null");
        assertEquals(2, tiendas.size(), "El usuario debería tener 2 tiendas");
    }

    @Test
    public void testObtenerTiendaPorId() throws SQLException {
        // Crear una tienda primero
        tiendaService.crearTienda("Tienda Test", "Desc", usuarioTestId, "Cat", null);

        // Obtener el ID de la tienda creada
        int idTienda;
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT idTienda FROM tienda WHERE nombre = ?")) {
            ps.setString(1, "Tienda Test");
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());
            idTienda = rs.getInt("idTienda");
        }

        Tienda tienda = tiendaService.obtenerTiendaPorId(idTienda);
        assertNotNull(tienda, "La tienda no debería ser null");
        assertEquals("Tienda Test", tienda.getNombre());
    }

    @Test
    public void testBuscarTiendasPorNombre() throws SQLException {
        // Crear algunas tiendas con nombres similares
        tiendaService.crearTienda("Tienda ABC", "Desc 1", usuarioTestId, "Cat 1", null);
        tiendaService.crearTienda("Tienda XYZ", "Desc 2", usuarioTestId, "Cat 2", null);

        List<Tienda> tiendas = tiendaService.buscarTiendasPorNombre("ABC");
        assertNotNull(tiendas, "La lista de tiendas no debería ser null");
        assertTrue(tiendas.stream().anyMatch(t -> t.getNombre().contains("ABC")),
                "Debería encontrar la tienda con 'ABC' en el nombre");
    }

    @Test
    public void testEliminarTienda() throws SQLException {
        // Crear una tienda primero
        tiendaService.crearTienda("Tienda a Eliminar", "Desc", usuarioTestId, "Cat", null);

        // Obtener el ID de la tienda creada
        int idTienda;
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT idTienda FROM tienda WHERE nombre = ?")) {
            ps.setString(1, "Tienda a Eliminar");
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());
            idTienda = rs.getInt("idTienda");
        }

        boolean resultado = tiendaService.eliminarTienda(idTienda);
        assertTrue(resultado, "La tienda debería eliminarse correctamente");

        // Verificar que la tienda ya no existe
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM tienda WHERE idTienda = ?")) {
            ps.setInt(1, idTienda);
            ResultSet rs = ps.executeQuery();
            assertFalse(rs.next(), "La tienda no debería existir después de eliminarla");
        }
    }

    @Test
    public void testActualizarTienda() throws SQLException {
        // Crear una tienda primero
        tiendaService.crearTienda("Tienda Original", "Desc", usuarioTestId, "Cat", null);

        // Obtener la tienda creada
        Tienda tienda;
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM tienda WHERE nombre = ?")) {
            ps.setString(1, "Tienda Original");
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());
            tienda = new Tienda();
            tienda.setIdTienda(rs.getInt("idTienda"));
            tienda.setNombre("Tienda Actualizada");
            tienda.setDescripcion("Nueva descripción");
            tienda.setCategoria("Nueva categoría");
        }

        tiendaService.actualizarTienda(tienda);

        // Verificar la actualización
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM tienda WHERE idTienda = ?")) {
            ps.setInt(1, tienda.getIdTienda());
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next(), "La tienda debería existir");
            assertEquals("Tienda Actualizada", rs.getString("nombre"),
                    "El nombre debería estar actualizado");
        }
    }

    @Test
    public void testObtenerCategorias() {
        List<String> categorias = tiendaService.obtenerCategorias();
        assertNotNull(categorias, "La lista de categorías no debería ser null");
    }

    @Test
    public void testCrearTiendaUsuarioInexistente() {
        boolean resultado = tiendaService.crearTienda(
                "Tienda Test",
                "Descripción",
                99999, // ID de usuario que no existe
                "Categoría",
                imagenPrueba
        );
        assertFalse(resultado, "No debería permitir crear una tienda para un usuario inexistente");
    }

    @Test
    public void testEliminarTiendaInexistente() {
        boolean resultado = tiendaService.eliminarTienda(99999); // ID que no existe
        assertFalse(resultado, "No debería permitir eliminar una tienda inexistente");
    }
}