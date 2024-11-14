package Controladores.Cuenta.Tienda;

import Modelos.Producto;
import Modelos.Tienda;
import Servicios.Carrito.CarritoService;
import Repositorios.Carrito.MostrarCarrito;
import Repositorios.Productos.MostrarProductos;
import Modelos.UsuarioActivo;
import Controladores.Vistas.CambiosVistas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import Repositorios.Productos.CrearProducto;
import Servicios.Productos.ProductoService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class ViewTiendaAClienteController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private TextField nombreTienda;

    @FXML
    private TextField categoriaTienda;

    @FXML
    private TextField DescTienda;

    @FXML
    private ImageView imagenTienda;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnMiPerfil;

    @FXML
    private Button BtnCompras;

    @FXML
    private Button BtnFacturacion;

    @FXML
    private VBox vboxProductos;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private MostrarProductos mostrarProductos = new MostrarProductos();
    private final ProductoService productoService = new ProductoService(new CrearProducto(), new MostrarProductos());
    private final CarritoService carritoService = new CarritoService(new MostrarCarrito(), productoService);

    @FXML
    private void initialize() {

        // Configurar el buscador de productos
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());

        // Realizar búsqueda cuando el usuario presione "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());

        // Configurar el evento del carrito
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());


        //Obtiene la tienda seleccionada desde cambiosVistas y carga los datos
        Tienda tienda = CambiosVistas.getTiendaSeleccionada();
        if (tienda != null) {
            cargarDatosTienda(tienda);
            cargarProductos(tienda);
        } else {
            System.out.println("No hay tienda seleccionada.");
        }

        //Hace que los campos de la tienda no sean editables
        nombreTienda.setEditable(false);
        categoriaTienda.setEditable(false);
        DescTienda.setEditable(false);

    }

    //Carga datos de tienda
    private void cargarDatosTienda(Tienda tienda) {
        //Carga nombre tienda
        nombreTienda.setText(tienda.getNombre());
        //Carga categoria tienda
        categoriaTienda.setText(tienda.getCategoria());
        //Carga descripcion tienda
        DescTienda.setText(tienda.getDescripcion());

        //si la tienda tiene una imagen la carga
        if (tienda.getImagen() != null) {
            Image image = new Image(new ByteArrayInputStream(tienda.getImagen()));
            imagenTienda.setImage(image);
        }
    }

    //Carga los productos de la tienda y los muestra en el vbox
    private void cargarProductos(Tienda tienda) {
        List<Producto> productos = mostrarProductos.obtenerProductosDeTienda(tienda.getIdTienda());
        vboxProductos.getChildren().clear();

        //Agrega producto por producto a la vista
        for (Producto producto : productos) {
            agregarProductoAVista(producto);
        }
    }

    //Carga productos a la vista
    private void agregarProductoAVista(Producto producto) {
        //Crea el hbox para el producto con margen de 10 pixeles
        HBox hboxProducto = new HBox(10);
        //Hace set de un style en el hbox
        hboxProducto.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-alignment: CENTER_LEFT; -fx-border-color: #dddddd;");
        hboxProducto.setPrefWidth(600);

        //Ajusta la imagen del producto y la carga si existe
        ImageView imagenProducto = new ImageView();
        imagenProducto.setFitHeight(80);
        imagenProducto.setFitWidth(80);
        if (producto.getImagenProducto() != null) {
            Image image = new Image(new ByteArrayInputStream(producto.getImagenProducto()));
            imagenProducto.setImage(image);
        }

        //Crea la etiqueta del nombre del producto y le da un style
        Label nombreProducto = new Label(producto.getNombre());
        nombreProducto.setStyle("-fx-font-size: 14px; -fx-padding: 0 10 0 10;");

        //Crea la etiqueta del precio del producto y le da un style
        Label precioProducto = new Label(String.format("$ %.2f", producto.getPrecio()));
        precioProducto.setStyle("-fx-font-size: 14px;");

        //Agrega el boton agregar producto al carrito y le da un style
        Button btnAgregarCarrito = new Button("Agregar al Carrito");
        btnAgregarCarrito.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        btnAgregarCarrito.setOnAction(event -> agregarAlCarrito(producto));

        //Ajusta el margen del hbox del producto
        HBox.setMargin(hboxProducto, new Insets(5, 0, 10, 0));
        hboxProducto.getChildren().addAll(imagenProducto, nombreProducto, precioProducto, btnAgregarCarrito);

        //Configura margen del primer producto con fines esteticos
        if (vboxProductos.getChildren().isEmpty()) {
            VBox.setMargin(hboxProducto, new Insets(1, 0, 5, 0));
        } else {
            VBox.setMargin(hboxProducto, new Insets(5, 0, 10, 0));
        }

        //Agrega al hbox al vbox
        vboxProductos.getChildren().add(hboxProducto);
    }

    //Agrega producto al carrito de usuarioActivo
    private void agregarAlCarrito(Producto producto) {
        //Toma el id del carrito del usuario
        int idCarrito = UsuarioActivo.getIdCarrito();

        //Agrega el producto con cantidad del 1 al carrito
        producto.setCantidad(1);

        //Agrega producto al carrito usando el carritoService
        carritoService.agregarProductoAlCarrito(idCarrito, producto);
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

    //Cambio vista carrito
    @FXML
    public void mostrarCarrito() {
        cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");
    }

    //Cambio vista perfil
    @FXML
    public void mostrarMiPerfil() {
        cambiarVista(BtnMiPerfil, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");
    }


    //Cambio vista Facturacion
    @FXML
    public void mostrarFacturacion(ActionEvent event) {cambiarVista(BtnFacturacion, "/Vistas/PantallaCuenta/Facturacion/View-Facturacion.fxml");}


    //Cambio vista inicio
    @FXML
    public void mostrarInicio(ActionEvent event) {cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");}

}
