package Controladores.Cuenta.Tienda;

import Modelos.Producto;
import Servicios.Datos.CrearProducto;
import Servicios.Datos.UsuarioActivo;
import Servicios.Vistas.CambiosVistas;
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
import java.math.BigDecimal;

public class ViewProductoController {

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
    private Button btnCrearProducto;

    @FXML
    private TextField nombreProducto;

    @FXML
    private TextField precioProducto;

    @FXML
    private TextField descProducto;

    @FXML
    private Spinner<Integer> stockProducto;

    @FXML
    private ChoiceBox<String> catProducto;

    @FXML
    private ImageView imagenProducto;

    @FXML
    private Button btnCargarImagen;

    @FXML
    private Button btnVolverTienda;

    private CrearProducto crearProducto = new CrearProducto();
    private CambiosVistas cambiosVistas = new CambiosVistas();

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        stockProducto.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0));
        catProducto.getItems().addAll(
                "Electrónica", "Ropa y Moda", "Hogar y Jardín", "Salud y Belleza",
                "Deportes", "Juguetes", "Alimentos", "Automóviles", "Libros", "Mascotas"
        );
        catProducto.setValue("Electrónica");

        btnCargarImagen.setOnAction(event -> cargarImagen());
        btnCrearProducto.setOnAction(event -> crearProducto());

        buscarProductos.setOnAction(event -> realizarBusqueda());
    }

    private void cargarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        // Selecciona el archivo de imagen
        File archivoSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoSeleccionado != null) {
            crearProducto.setArchivoImagen(archivoSeleccionado); // Asigna la imagen al servicio
            try {
                // Convierte el archivo de imagen a Image para mostrar en el ImageView
                Image image = new Image(new FileInputStream(archivoSeleccionado));
                imagenProducto.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al cargar la imagen en el controlador.");
            }
        }
    }


    private void crearProducto() {
        if (nombreProducto.getText().isEmpty() || precioProducto.getText().isEmpty() ||
                descProducto.getText().isEmpty() || stockProducto.getValue() == null ||
                catProducto.getValue() == null || crearProducto.getArchivoImagen() == null) {
            System.out.println("Todos los campos son obligatorios.");
            return;
        }

        try {
            Producto producto = new Producto();
            producto.setNombre(nombreProducto.getText());
            producto.setPrecio(new BigDecimal(precioProducto.getText()));
            producto.setDescripcion(descProducto.getText());
            producto.setStock(stockProducto.getValue());
            producto.setCategoria(catProducto.getValue());
            producto.setImagenProducto(new FileInputStream(crearProducto.getArchivoImagen()).readAllBytes());

            if (ViewMirarTiendaController.getTiendaSeleccionada() != null) {
                producto.setIdTienda(ViewMirarTiendaController.getTiendaSeleccionada().getIdTienda());
            } else {
                System.out.println("No hay tienda seleccionada para asociar el producto.");
                return;
            }

            boolean exito = crearProducto.crearProducto(producto);
            if (exito) {
                System.out.println("Producto creado exitosamente.");
                cambiarVista(btnCrearProducto, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
            } else {
                System.out.println("Error al crear el producto.");
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Error al procesar los datos del producto.");
        }
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

    @FXML
    public void mostrarMirarTienda(ActionEvent event) {
        cambiarVista(btnVolverTienda, "/Vistas/PantallaCuenta/Tienda/View-MirarTienda.fxml");
    }

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
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
