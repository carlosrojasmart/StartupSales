package Controladores.Cuenta.Tienda;

import Servicios.Vistas.CambiosVistas;
import Servicios.Datos.UsuarioActivo;
import Servicios.Datos.CrearTienda;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class ViewCreandoTiendaController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnMiPerfil;

    @FXML
    private Button BtnCompras;

    @FXML
    private Button BtnFacturacion;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private TextField nombreTienda;

    @FXML
    private TextArea descTienda;

    @FXML
    private ChoiceBox<String> boxCategoria;

    @FXML
    private Button BtnCargarImagen;

    @FXML
    private ImageView imagenTienda;

    @FXML
    private Button BtnCrearTienda;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private CrearTienda crearTienda = new CrearTienda();

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        boxCategoria.getItems().addAll("Electrónica", "Ropa y Moda", "Hogar y Jardín", "Salud y Belleza", "Deportes", "Juguetes", "Alimentos", "Automóviles", "Libros", "Mascotas");

        buscarProductos.setOnAction(event -> realizarBusqueda());
    }

    @FXML
    public void mostrarCarrito() {
        cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");
    }

    @FXML
    public void mostrarMiPerfil() {
        cambiarVista(BtnMiPerfil, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");
    }

    @FXML
    public void mostrarCompras(ActionEvent event) {
        if (CambiosVistas.usuarioTieneCompras(UsuarioActivo.getIdUsuario())) {
            cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-ComprasCreada.fxml");
        } else {
            cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");
        }
    }

    @FXML
    public void mostrarFacturacion(ActionEvent event) {
        cambiarVista(BtnFacturacion, "/Vistas/PantallaCuenta/Facturacion/View-Facturacion.fxml");
    }

    @FXML
    public void mostrarInicio(ActionEvent event) {
        cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");
    }

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    @FXML
    private void cargarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        // Selecciona el archivo de imagen
        File archivoSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoSeleccionado != null) {
            crearTienda.setArchivoImagen(archivoSeleccionado); // Asigna la imagen al servicio

            try {
                // Convierte el archivo de imagen a Image para mostrar en el ImageView
                Image image = new Image(new FileInputStream(archivoSeleccionado));
                imagenTienda.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al cargar la imagen en el controlador.");
            }
        }
    }


    @FXML
    private void crearTienda(ActionEvent event) {
        try {
            String nombre = nombreTienda.getText();
            String descripcion = descTienda.getText();
            String categoria = boxCategoria.getValue();
            int idUsuario = UsuarioActivo.getIdUsuario();

            if (nombre.isEmpty() || descripcion.isEmpty() || categoria == null || crearTienda.getArchivoImagen() == null) {
                System.out.println("Por favor, complete todos los campos.");
                return;
            }

            crearTienda.crearTienda(nombre, descripcion, idUsuario, categoria, crearTienda.getArchivoImagen());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            cambiosVistas.cambiarVista(stage, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void realizarBusqueda() {
        String terminoBusqueda = buscarProductos.getText().trim();
        if (!terminoBusqueda.isEmpty()) {
            CambiosVistas.setTerminoBusqueda(terminoBusqueda);
            cambiarVista(buscarProductos, "/Vistas/PantallaPrincipal/View-BusquedaProductos.fxml");
        } else {
            System.out.println("El término de búsqueda está vacío.");
        }
    }
}
