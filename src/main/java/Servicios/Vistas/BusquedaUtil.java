package Servicios.Vistas;

import Servicios.Vistas.CambiosVistas;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BusquedaUtil {

    public static void realizarBusqueda(TextField buscarProductos, Node nodo) {
        String terminoBusqueda = buscarProductos.getText().trim();

        if (!terminoBusqueda.isEmpty()) {
            // Almacenar el término de búsqueda para que esté disponible en la vista de búsqueda de productos
            CambiosVistas.setTerminoBusqueda(terminoBusqueda);

            // Cambiar a la vista de búsqueda de productos
            cambiarVista(nodo, "/Vistas/PantallaPrincipal/View-BusquedaProductos.fxml");
        } else {
            System.out.println("El término de búsqueda está vacío.");
        }
    }

    private static void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        CambiosVistas cambiosVistas = new CambiosVistas();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }
}
