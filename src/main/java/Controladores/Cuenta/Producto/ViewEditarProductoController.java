package Controladores.Cuenta.Producto;

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

    @FXML
    private void initialize() {
        // Inicializar `productoService` y sus repositorios
        CrearProducto crearProducto = new CrearProducto();
        MostrarProductos mostrarProductos = new MostrarProductos();
        productoService = new ProductoService(crearProducto, mostrarProductos);

        // Configurar el buscador de productos
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());

        // Realizar búsqueda cuando el usuario presione "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());

        // Configurar el evento del carrito
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        //Configura el spinner y el choiceBox para las categorias del producto
        stockProducto.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0));
        catProducto.getItems().addAll("Electrónica", "Ropa y Moda", "Hogar y Jardín", "Salud y Belleza", "Deportes", "Juguetes", "Alimentos", "Automóviles", "Libros", "Mascotas");

        //Inicializa el boton editar producto
        btnEditarProducto.setOnAction(event -> editarProducto());

        //Inicializa el botoneliminar producto
        btnEliminarProducto.setOnAction(event -> eliminarProducto());
    }

    //Set del producto
    public void setProductoSeleccionado(Producto producto) {
        //Selecciona el producto para hacer set de sus datos
        this.productoSeleccionado = producto;
        //Realiza set de nombre del producto
        nombreProducto.setText(producto.getNombre());
        //Realiza set de precio de prodcuto
        precioProducto.setText(String.valueOf(productoSeleccionado.getPrecio()));
        //Realiza set de la descripcion del producto
        descProducto.setText(producto.getDescripcion());
        //Realiza set del stock del producto
        stockProducto.getValueFactory().setValue(producto.getStock());
        //Realiza set de la categoria del producto
        catProducto.setValue(producto.getCategoria());

        //Si el producto tiene una imagen carga la imagen en el imageView
        if (producto.getImagenProducto() != null) {
            Image image = new Image(new ByteArrayInputStream(producto.getImagenProducto()));
            imagenProducto.setImage(image);
        }
    }


    //Editar producto
    @FXML
    private void editarProducto() {
        //Revisa si hay productos seleccionadoas para editar
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

            // Actualiza el producto usando el servicio de producto
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

    //Eliminacion de producto
    @FXML
    private void eliminarProducto() {

        //Revisa si hay producto seleccionado para eliminar
        if (productoSeleccionado == null) {
            System.out.println("No hay producto seleccionado para eliminar.");
            return;
        }

        // Llamar al método en crearProducto para eliminar el producto
        boolean exito = crearProducto.eliminarProducto(productoSeleccionado.getIdProducto());
        if (exito) {
            System.out.println("Producto eliminado correctamente.");
            mostrarMirarTienda(null); // Cambiar a la vista de la tienda después de eliminar el producto
        } else {
            System.out.println("Error al eliminar el producto.");
        }
    }


    //Carga de imagen del producto
    @FXML
    private void cargarImagen() {
        //Crea un seleccionador de archivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                //Filtra el selccionador para asegurar que sea una imagen
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        //Abre el Dialog para que usuario seleccione imagen
        archivoImagen = fileChooser.showOpenDialog(null);

        if (archivoImagen != null) { //Si usuario Carga una imagen
            try {
                // Convierte el archivo de imagen a Image para mostrar en el ImageView
                Image nuevaImagen = new Image(new FileInputStream(archivoImagen));

                //hace set de la nueva imagen
                imagenProducto.setImage(nuevaImagen);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void realizarBusqueda() {
        //inicializa el terminoBusqueda
        String terminoBusqueda = buscarProductos.getText().trim();
        if (!terminoBusqueda.isEmpty()) {
            // Si no esta vacio almacena el termino el término de búsqueda para usarlo en la vista de búsqueda de productos
            CambiosVistas.setTerminoBusqueda(terminoBusqueda);

            // Cambiar a la vista de búsqueda de productos
            cambiarVista(buscarProductos, "/Vistas/PantallaPrincipal/View-BusquedaProductos.fxml");
        } else {
            // Si el termino de busqueda esta avcio imprime
            System.out.println("El término de búsqueda está vacío.");
        }
    }

    //Cambio de vista general
    private void cambiarVista(Node nodo, String rutaFXML) {
        //Inicializa el stage usando el nodo para cambio de vista
        Stage stage = (Stage) nodo.getScene().getWindow();
        //Usa CambiosVista para realizar el cambio usando el stage y la ruta del fxml
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    //Cambio de vista a Compras
    @FXML
    public void mostrarCompras(ActionEvent event) {
        //Verifica si el usuario tiene compras y muestra la vista de comprasCreada
        if (CambiosVistas.usuarioTieneCompras(UsuarioActivo.getIdUsuario())) {
            cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-ComprasCreada.fxml");
            //Si usuario no tiene compras muestra vista Compras
        } else {
            cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");
        }
    }

    //Cambio de vista a Carrito
    @FXML
    public void mostrarCarrito() {cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");}

    //Cambio de vista a mi perfil
    @FXML
    public void mostrarMiPerfil() {cambiarVista(BtnMiPerfil, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");}

    //Cambio de vista a Facturacion
    @FXML
    public void mostrarFacturacion(ActionEvent event) {cambiarVista(BtnFacturacion, "/Vistas/PantallaCuenta/Facturacion/View-Facturacion.fxml");}

    //Cambio de vista a Inicio boton superior
    @FXML
    public void mostrarInicio(ActionEvent event) {cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");}

    //Cambio de vista a mirar tienda
    @FXML
    public void mostrarMirarTienda(ActionEvent event) {cambiarVista(btnVolverTienda, "/Vistas/PantallaCuenta/Tienda/View-MirarTienda.fxml");}

}
