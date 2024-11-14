package Controladores.Cuenta.Producto;

import Controladores.Cuenta.Tienda.ViewMirarTiendaController;
import Modelos.Producto;
import Modelos.UsuarioActivo;
import Controladores.Vistas.CambiosVistas;
import Repositorios.Productos.MostrarProductos;
import Servicios.Productos.ProductoService;
import Repositorios.Productos.CrearProducto;
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

    private final ProductoService productoService = new ProductoService(new CrearProducto(), new MostrarProductos());
    private CambiosVistas cambiosVistas = new CambiosVistas();
    private File archivoImagen;

    @FXML
    private void initialize() {
        // Configurar el buscador de productos
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());

        // Realizar búsqueda cuando el usuario presione "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());

        // Configurar el evento del carrito
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        //Configura el spinner para la creacion de producto
        stockProducto.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0));

        //inicializa el ChoiceBox de las categorias para el producto
        catProducto.getItems().addAll(
                "Electrónica", "Ropa y Moda", "Hogar y Jardín", "Salud y Belleza",
                "Deportes", "Juguetes", "Alimentos", "Automóviles", "Libros", "Mascotas"
        );

        //Inicializa el boton cargarImagen
        btnCargarImagen.setOnAction(event -> cargarImagen());

        //Inicializa el boton crearProducto
        btnCrearProducto.setOnAction(event -> crearProducto());

    }

    //Carga de imagen de producto
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
                //Crea la imagen
                Image image = new Image(new FileInputStream(archivoImagen));

                //hace set de la nueva imagen
                imagenProducto.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Creacion de Producto
    private void crearProducto() {

        //Verifica que todos los campos del formulario de producto esten llenos
        if (nombreProducto.getText().isEmpty() || precioProducto.getText().isEmpty() ||
                descProducto.getText().isEmpty() || stockProducto.getValue() == null ||
                catProducto.getValue() == null || archivoImagen == null) {
            //Envia mensaje de que todos los campos deben estar llenos
            System.out.println("Todos los campos son obligatorios.");
            return;
        }

        //Crea un nuevo objeto producto y hace set de los atributos a partir del formulario
        Producto producto = new Producto();
        //Establece nombre producto
        producto.setNombre(nombreProducto.getText());
        //Establece precio producto
        producto.setPrecio(new BigDecimal(precioProducto.getText()));
        //Establece descripcion producto
        producto.setDescripcion(descProducto.getText());
        //Establece stock producto
        producto.setStock(stockProducto.getValue());
        //Establece categoria producto
        producto.setCategoria(catProducto.getValue());

        // Verifica si hay una tienda seleccionada para asociar el producto a esta tienda
        if (ViewMirarTiendaController.getTiendaSeleccionada() != null) {
            producto.setIdTienda(ViewMirarTiendaController.getTiendaSeleccionada().getIdTienda());
        } else {
            //Si no hay tienda seleccionada se detiene proceso y muestra mensaje
            System.out.println("No hay tienda seleccionada para asociar el producto.");
            return;
        }

        //LLama al servicio para realizar la creacion del producto y guarda resultado en variable
        boolean exito = productoService.crearProducto(producto, archivoImagen);


        if (exito) {
            //imprime exito
            System.out.println("Producto creado exitosamente.");
            cambiarVista(btnCrearProducto, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
        } else {
            //imprime error
            System.out.println("Error al crear el producto.");
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
