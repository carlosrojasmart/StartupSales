package Controladores.Vistas;

import Modelos.Tienda;
import Servicios.Compras.ComprasService;
import Repositorios.Compras.ComprasRepo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CambiosVistas {

    // Servicio de compras para verificar si el usuario tiene compras, usando el repositorio correspondiente
    private static final ComprasService comprasService = new ComprasService(new ComprasRepo());

    // Cambia la vista actual del stage a la vista especificada en la ruta FXML
    public void cambiarVista(Stage stage, String rutaFXML) {
        try {
            // Cargar el archivo FXML
            Parent root = FXMLLoader.load(getClass().getResource(rutaFXML));

            // Configurar y mostrar la nueva escena en el stage
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            // Imprimir el stack trace en caso de error de carga
            e.printStackTrace();
        }
    }

    // Carga una vista en el stage actual con un tamaño específico y título
    public void cargarVista(Stage stage, String rutaFXML, String titulo, double ancho, double alto) {
        try {
            // Cargar el archivo FXML y crear una nueva escena
            Parent root = FXMLLoader.load(getClass().getResource(rutaFXML));
            Scene scene = new Scene(root, ancho, alto);

            // Configurar el título de la ventana y mostrar la nueva escena
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            // Imprimir el stack trace en caso de error de carga
            e.printStackTrace();
        }
    }

    // Almacena el término de búsqueda actual para compartir entre vistas
    private static String terminoBusqueda;

    // Establece el término de búsqueda
    public static void setTerminoBusqueda(String termino) {
        terminoBusqueda = termino;
    }

    // Retorna el término de búsqueda actual
    public static String getTerminoBusqueda() {
        return terminoBusqueda;
    }

    // Almacena la tienda seleccionada para acceso en otras vistas
    private static Tienda tiendaSeleccionada;

    // Establece la tienda seleccionada
    public static void setTiendaSeleccionada(Tienda tienda) {
        tiendaSeleccionada = tienda;
    }

    // Retorna la tienda seleccionada
    public static Tienda getTiendaSeleccionada() {
        return tiendaSeleccionada;
    }

    // Establece el ID de la tienda seleccionada
    public static void setIdTiendaSeleccionada(int idTienda) {
        // Si no hay tienda seleccionada, crear una nueva instancia de Tienda
        if (tiendaSeleccionada == null) {
            tiendaSeleccionada = new Tienda();
        }
        tiendaSeleccionada.setIdTienda(idTienda);
    }

    // Retorna el ID de la tienda seleccionada, o -1 si no hay tienda seleccionada
    public static int getIdTiendaSeleccionada() {
        return tiendaSeleccionada != null ? tiendaSeleccionada.getIdTienda() : -1;
    }

    // Verifica si el usuario tiene compras registradas, usando el servicio de compras
    public static boolean usuarioTieneCompras(int idUsuario) {
        return comprasService.usuarioTieneCompras(idUsuario);
    }
}
