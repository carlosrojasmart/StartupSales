package Controladores.Vistas;

import Modelos.Tienda;
import Servicios.Compras.ComprasService;
import Repositorios.Compras.ComprasRepo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CambiosVistas {

    private static final ComprasService comprasService = new ComprasService(new ComprasRepo());

    public void cambiarVista(Stage stage, String rutaFXML) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(rutaFXML));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarVista(Stage stage, String rutaFXML, String titulo, double ancho, double alto) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(rutaFXML));
            Scene scene = new Scene(root, ancho, alto);
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String terminoBusqueda;

    public static void setTerminoBusqueda(String termino) {
        terminoBusqueda = termino;
    }

    public static String getTerminoBusqueda() {
        return terminoBusqueda;
    }

    private static Tienda tiendaSeleccionada;

    public static void setTiendaSeleccionada(Tienda tienda) {
        tiendaSeleccionada = tienda;
    }

    public static Tienda getTiendaSeleccionada() {
        return tiendaSeleccionada;
    }

    public static void setIdTiendaSeleccionada(int idTienda) {
        if (tiendaSeleccionada == null) {
            tiendaSeleccionada = new Tienda();
        }
        tiendaSeleccionada.setIdTienda(idTienda);
    }

    public static int getIdTiendaSeleccionada() {
        return tiendaSeleccionada != null ? tiendaSeleccionada.getIdTienda() : -1;
    }

    public static boolean usuarioTieneCompras(int idUsuario) {
        // Ahora usa ComprasService en lugar de ejecutar SQL directamente
        return comprasService.usuarioTieneCompras(idUsuario);
    }
}
