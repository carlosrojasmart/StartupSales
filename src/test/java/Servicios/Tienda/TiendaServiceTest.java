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
    public void testObtenerTiendasDestacadas() {
        List<Tienda> tiendasDestacadas = tiendaService.obtenerTiendasDestacadas();
        assertNotNull(tiendasDestacadas, "La lista de tiendas destacadas no debería ser null");
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