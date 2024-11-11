package Controladores.Cuenta.Tienda;

import Modelos.Producto;
import Repositorios.Productos.CrearProducto;
import Repositorios.Productos.MostrarProductos;
import Servicios.Productos.ProductoService;
import Modelos.UsuarioActivo;
import Controladores.Vistas.CambiosVistas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.math.BigDecimal;

import static Controladores.Vistas.BusquedaUtil.realizarBusqueda;

public class ViewEditarProductoController {

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
    private Button btnEditarProducto;

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

    @FXML
    private Button btnEliminarProducto;

    private Producto productoSeleccionado;
    private File archivoImagen;
    private CambiosVistas cambiosVistas = new CambiosVistas();
    private CrearProducto crearProducto = new CrearProducto();
    private ProductoService productoService;


    public void setProductoSeleccionado(Producto producto) {
        this.productoSeleccionado = producto;
        nombreProducto.setText(producto.getNombre());
        precioProducto.setText(String.valueOf(productoSeleccionado.getPrecio()));
        descProducto.setText(producto.getDescripcion());
        stockProducto.getValueFactory().setValue(producto.getStock());
        catProducto.setValue(producto.getCategoria());

        if (producto.getImagenProducto() != null) {
            Image image = new Image(new ByteArrayInputStream(producto.getImagenProducto()));
            imagenProducto.setImage(image);
        }
    }

    @FXML
    private void initialize() {
        // Inicializar `productoService` y sus repositorios
        CrearProducto crearProducto = new CrearProducto();
        MostrarProductos mostrarProductos = new MostrarProductos();
        productoService = new ProductoService(crearProducto, mostrarProductos);

        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        buscarProductos.setOnAction(event -> realizarBusqueda());

        stockProducto.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0));
        catProducto.getItems().addAll("Electrónica", "Ropa y Moda", "Hogar y Jardín", "Salud y Belleza", "Deportes", "Juguetes", "Alimentos", "Automóviles", "Libros", "Mascotas");

        btnEditarProducto.setOnAction(event -> editarProducto());
        btnEliminarProducto.setOnAction(event -> eliminarProducto());
    }


    @FXML
    private void editarProducto() {
        if (productoSeleccionado == null) {
            System.out.println("No hay producto seleccionado para editar.");
            return;
        }

        try {
            // Configurar los valores actualizados del producto
            productoSeleccionado.setNombre(nombreProducto.getText());
            productoSeleccionado.setPrecio(new BigDecimal(precioProducto.getText()));
            productoSeleccionado.setDescripcion(descProducto.getText());
            productoSeleccionado.setStock(stockProducto.getValue());
            productoSeleccionado.setCategoria(catProducto.getValue());

            // Intentar actualizar el producto a través del servicio
            boolean exito = productoService.actualizarProducto(productoSeleccionado, archivoImagen);
            if (exito) {
                System.out.println("Producto actualizado correctamente.");
                mostrarMirarTienda(null);
            } else {
                System.out.println("Error al actualizar el producto.");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Error al procesar los datos del producto.");
        }
    }



    @FXML
    private void eliminarProducto() {
        if (productoSeleccionado == null) {
            System.out.println("No hay producto seleccionado para eliminar.");
            return;
        }

        // Llamar al método para eliminar el producto
        boolean exito = crearProducto.eliminarProducto(productoSeleccionado.getIdProducto());
        if (exito) {
            System.out.println("Producto eliminado correctamente.");
            mostrarMirarTienda(null); // Cambiar a la vista de la tienda después de eliminar el producto
        } else {
            System.out.println("Error al eliminar el producto.");
        }
    }


    @FXML
    private void cargarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        archivoImagen = fileChooser.showOpenDialog(null);

        if (archivoImagen != null) {
            try {
                Image nuevaImagen = new Image(new FileInputStream(archivoImagen));
                imagenProducto.setImage(nuevaImagen);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
