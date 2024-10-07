package Servicios.Vistas;

import Modelos.Tienda;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CambiosVistas {

    public void cambiarVista(Stage stage, String rutaFXML) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(rutaFXML));
            stage.setScene(new Scene(root));
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




}
