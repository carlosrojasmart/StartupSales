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

    private static TiendaService tiendaService; // Servicio de tiendas a probar
    private static Connection connection; // Conexión a la base de datos
    private static CrearTienda crearTienda; // Repositorio para crear tiendas
    private static MostrarTiendas mostrarTiendas; // Repositorio para mostrar tiendas
    private static File imagenPrueba; // Archivo de imagen para pruebas
    private static int usuarioTestId; // ID de usuario para pruebas

    // Método que se ejecuta antes de todas las pruebas para configurar la base de datos
    @BeforeAll
    public static void setUpDatabase() throws Exception {
        DatabaseSetup.setUpDatabase(); // Configura la base de datos inicial
        connection = new DatabaseSetup().getConnection();
        connection.setAutoCommit(false); // Permite revertir cambios en cada prueba
        System.out.println("Conexión a la base de datos exitosa.");

        // Crea un archivo de imagen de prueba para asociarlo con tiendas
        imagenPrueba = new File("src/test/resources/test-store-image.jpg");
        if (!imagenPrueba.exists()) {
            imagenPrueba.getParentFile().mkdirs();
            imagenPrueba.createNewFile();
        }
    }

    // Método que se ejecuta antes de cada prueba para configurar el entorno
    @BeforeEach
    public void setUp() throws SQLException {
        // Restablece la conexión si está cerrada
        if (connection == null || connection.isClosed()) {
            connection = new DatabaseSetup().getConnection();
            connection.setAutoCommit(false);
            System.out.println("Conexión a la base de datos restaurada.");
        }

        // Instanciar repositorios y servicio a probar
        crearTienda = new CrearTienda();
        mostrarTiendas = new MostrarTiendas();
        tiendaService = new TiendaService(mostrarTiendas, crearTienda);

        // Limpiar las tablas de la base de datos antes de cada prueba
        limpiarBaseDeDatos();
        connection.commit();

        // Insertar datos de prueba en la base de datos
        insertarDatosIniciales();
    }

    // Método para limpiar las tablas de la base de datos
    private void limpiarBaseDeDatos() throws SQLException {
        String[] tablas = {
                "carrito_producto",
                "producto",
                "tienda",
                "usuario"
        };

        // Elimina los datos de cada tabla para evitar interferencia entre pruebas
        for (String tabla : tablas) {
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM " + tabla)) {
                ps.executeUpdate();
            }
        }
        connection.commit(); // Confirma la limpieza
        System.out.println("Limpieza de base de datos completada.");
    }

    // Inserta un usuario de prueba en la base de datos
    private void insertarDatosIniciales() throws SQLException {
        // Inserta un usuario en la tabla `usuario`
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO usuario (nombre, correo_electronico, contrasena, esVendedor) VALUES (?, ?, ?, false)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "Mariaa perez");
            ps.setString(2, "mari@gmail.com");
            ps.setString(3, "1234");
            ps.executeUpdate();

            // Obtiene el ID generado para el usuario insertado
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                usuarioTestId = rs.getInt(1); // Guarda el ID para futuras pruebas
            }
        }
        connection.commit(); // Confirma la inserción
    }

    // Prueba para obtener las tiendas destacadas
    @Test
    public void testObtenerTiendasDestacadas() {
        List<Tienda> tiendasDestacadas = tiendaService.obtenerTiendasDestacadas();
        // Verifica que la lista de tiendas destacadas no sea null
        assertNotNull(tiendasDestacadas, "La lista de tiendas destacadas no debería ser null. Prueba exitosa");
    }

    // Prueba para obtener la lista de categorías
    @Test
    public void testObtenerCategorias() {
        List<String> categorias = tiendaService.obtenerCategorias();
        // Verifica que la lista de categorías no sea null
        assertNotNull(categorias, "La lista de categorías no debería ser null. Prueba exitosa");
    }

    // Prueba para intentar crear una tienda con un usuario inexistente
    @Test
    public void testCrearTiendaUsuarioInexistente() {
        boolean resultado = tiendaService.crearTienda(
                "Tienda Test",
                "Descripción",
                99999, // ID de usuario que no existe en la base de datos
                "Categoría",
                imagenPrueba
        );
        // Verifica que no se permita crear una tienda para un usuario inexistente
        assertFalse(resultado, "No se puede crear una tienda para un usuario inexistente. Prueba exitosa");
    }

    // Prueba para intentar eliminar una tienda inexistente
    @Test
    public void testEliminarTiendaInexistente() {
        boolean resultado = tiendaService.eliminarTienda(99999); // ID de tienda que no existe
        // Verifica que no se permita eliminar una tienda inexistente
        assertFalse(resultado, "La tienda no existe asi que mo se puede eliminar. Prueba exitosa");
    }
}
