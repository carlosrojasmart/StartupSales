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

import java.io.*;

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

    public void setProductoSeleccionado(Producto producto) {
        this.productoSeleccionado = producto;

        // Cargar los datos del producto en los campos de texto
        nombreProducto.setText(producto.getNombre());
        precioProducto.setText(String.valueOf(productoSeleccionado.getPrecio())); // Para edición de precios (sin el símbolo $)
        descProducto.setText(producto.getDescripcion());
        stockProducto.getValueFactory().setValue(producto.getStock());
        catProducto.setValue(producto.getCategoria());

        // Mostrar la imagen del producto
        if (producto.getImagenProducto() != null) {
            Image image = new Image(new ByteArrayInputStream(producto.getImagenProducto()));
            imagenProducto.setImage(image);
        }
    }

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        stockProducto.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0));

        catProducto.getItems().addAll(
                "Electrónica", "Ropa y Moda", "Hogar y Jardín", "Salud y Belleza",
                "Deportes", "Juguetes", "Alimentos", "Automóviles", "Libros", "Mascotas"
        );

        // Enlazar el botón con la acción de editar
        btnEditarProducto.setOnAction(event -> editarProducto());
        btnEliminarProducto.setOnAction(event -> eliminarProducto());

        buscarProductos.setOnMouseClicked(event -> {buscarProductos.clear();});
        // Realizar búsqueda cuando el usuario presione "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());
        // Configurar el evento del carrito
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());
    }

    private void cargarDatosProducto() {
        if (productoSeleccionado != null) {
            nombreProducto.setText(productoSeleccionado.getNombre());
            precioProducto.setText(String.valueOf(productoSeleccionado.getPrecio()));
            descProducto.setText(productoSeleccionado.getDescripcion());
            stockProducto.getValueFactory().setValue(productoSeleccionado.getStock());
            catProducto.setValue(productoSeleccionado.getCategoria());

            if (productoSeleccionado.getImagenProducto() != null) {
                Image image = new Image(new ByteArrayInputStream(productoSeleccionado.getImagenProducto()));
                imagenProducto.setImage(image);
            }
        }
    }

    @FXML
    private void editarProducto() {
        if (productoSeleccionado == null) {
            System.out.println("No hay producto seleccionado para editar.");
            return;
        }

        try {
            // Actualizar los datos del producto desde los campos de texto
            productoSeleccionado.setNombre(nombreProducto.getText());
            productoSeleccionado.setPrecio(Double.parseDouble(precioProducto.getText()));
            productoSeleccionado.setDescripcion(descProducto.getText());
            productoSeleccionado.setStock(stockProducto.getValue());
            productoSeleccionado.setCategoria(catProducto.getValue());

            // Verificar si hay una nueva imagen para actualizar
            if (archivoImagen != null) {
                productoSeleccionado.setImagenProducto(new FileInputStream(archivoImagen).readAllBytes());
            }

            // Actualizar el producto en la base de datos
            boolean exito = crearProducto.actualizarProducto(productoSeleccionado);
            if (exito) {
                System.out.println("Producto actualizado correctamente: " + productoSeleccionado.getNombre());

                // Aquí puedes cambiar de vista si lo deseas, después de la actualización exitosa
                mostrarMirarTienda(null);  // Cambiar la vista de regreso a la tienda
            } else {
                System.out.println("Error al actualizar el producto.");
            }
        } catch (IOException | NumberFormatException e) {
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

        // Llamar al método para eliminar el producto y sus referencias
        boolean exito = crearProducto.eliminarProducto(productoSeleccionado.getIdProducto());
        if (exito) {
            System.out.println("Producto eliminado correctamente.");
            mostrarMirarTienda(null); // Volver a la vista de la tienda
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
