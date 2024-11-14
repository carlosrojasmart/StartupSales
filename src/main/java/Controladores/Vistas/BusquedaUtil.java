package Controladores.Vistas;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BusquedaUtil {

    //Realiza busqueda de producto a partir de termino de bsuqueda ingresado
    public static void realizarBusqueda(TextField buscarProductos, Node nodo) {
        // Obtener el término de búsqueda desde el campo de texto
        String terminoBusqueda = buscarProductos.getText().trim();

        // Verificar si el término de búsqueda no está vacío
        if (!terminoBusqueda.isEmpty()) {
            // Almacenar el término de búsqueda para que esté disponible en la vista de búsqueda de productos
            CambiosVistas.setTerminoBusqueda(terminoBusqueda);

            // Cambiar a la vista de búsqueda de productos
            cambiarVista(nodo, "/Vistas/PantallaPrincipal/View-BusquedaProductos.fxml");
        } else {
            // Imprimir un mensaje si el término de búsqueda está vacío
            System.out.println("El término de búsqueda está vacío.");
        }
    }

    //Cambia vista actual a vista especifica
    private static void cambiarVista(Node nodo, String rutaFXML) {
        // Obtener el stage desde el nodo para realizar el cambio de vista
        Stage stage = (Stage) nodo.getScene().getWindow();

        // Crear una instancia de CambiosVistas para cambiar la vista
        CambiosVistas cambiosVistas = new CambiosVistas();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }
}
