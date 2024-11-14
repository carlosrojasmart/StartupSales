package Controladores.Principal;

import Modelos.Producto;
import Modelos.Tienda;
import Modelos.UsuarioActivo;
import Controladores.Vistas.CambiosVistas;
import Repositorios.Carrito.MostrarCarrito;
import Repositorios.Productos.CrearProducto;
import Repositorios.Productos.MostrarProductos;
import Repositorios.Tienda.CrearTienda;
import Repositorios.Tienda.MostrarTiendas;
import Servicios.Carrito.CarritoService;
import Servicios.Productos.ProductoService;
import Servicios.Tienda.TiendaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ViewInicialLogeadoController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnComprasAr;

    @FXML
    private ImageView usuarioIcono;

    @FXML
    private HBox TopProductos;

    @FXML
    private HBox TopTiendas;

    @FXML
    private Button BtnMisTiendas;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private final ProductoService productoService = new ProductoService(new CrearProducto(), new MostrarProductos());
    private final CarritoService carritoService = new CarritoService(new MostrarCarrito(), productoService);
    private final TiendaService tiendaService = new TiendaService(new MostrarTiendas(), new CrearTienda());

    @FXML
    private void initialize() {

        // Limpiar el campo de búsqueda al hacer clic
        buscarProductos.setOnMouseClicked(event -> {buscarProductos.clear();});

        // Asignar la funcionalidad de búsqueda al presionar "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());

        //inicializa el boton de carrito
        carritoCompra.setOnMouseClicked(event -> {mostrarCarrito();});

        //Inicializa el icono de perfil
        usuarioIcono.setOnMouseClicked(event -> mostrarMiPerfil());

        cargarTopProductosMasVendidos(); //Cargar productos mas vendidos
        cargarTiendasDestacadas(); // Cargar tiendas destacadas
    }

    //Carga las tiendas destacadas al inicialLogeado
    private void cargarTiendasDestacadas() {
        //Crea una lista de tiendas destacas y obtiene las tiendas destacadas usando tiendaService
        List<Tienda> tiendasDestacadas = tiendaService.obtenerTiendasDestacadas();
        // Limpia cualquier contenido anterior
        TopTiendas.getChildren().clear();

        //Crea un vbox para cada tienda destacada y le da un style y un ancho
        for (Tienda tienda : tiendasDestacadas) {
            VBox vboxTienda = new VBox(10);
            vboxTienda.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-alignment: CENTER;");
            vboxTienda.setPrefWidth(150);

            //Agrega el imageView dandole medidas
            ImageView imagenTienda = new ImageView();
            imagenTienda.setFitHeight(80);
            imagenTienda.setFitWidth(80);
            if (tienda.getImagen() != null) { //Si tiene imagen lo agrega al image view
                imagenTienda.setImage(new Image(new ByteArrayInputStream(tienda.getImagen())));
            }

            //Hace set del nombre de la tienda y le da un style
            Label nombreTienda = new Label(tienda.getNombre());
            nombreTienda.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

            //Agrega el boton de ir a la tienda le da un style y agrega la ruta
            Button btnIrTienda = new Button("Ir a Tienda");
            btnIrTienda.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            btnIrTienda.setOnAction(event -> irATienda(tienda.getIdTienda()));

            //Agrega el contenido a la tienda y a topTiendas
            vboxTienda.getChildren().addAll(imagenTienda, nombreTienda, btnIrTienda);
            TopTiendas.getChildren().add(vboxTienda);
        }
    }

    //Carga los productos destacados al inicial logueado
    private void cargarTopProductosMasVendidos() {
        //Pbtiene el top tiendas usando el productoService
        List<Producto> topProductos = productoService.obtenerTopProductosMasVendidos();
        // Limpia cualquier contenido anterior
        TopProductos.getChildren().clear();

        for (Producto producto : topProductos) {
            // Crear un VBox para cada producto
            VBox vboxProducto = new VBox(10);
            vboxProducto.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-alignment: CENTER;");
            vboxProducto.setPrefWidth(150);

            // Imagen del producto
            ImageView imagenProducto = new ImageView();
            imagenProducto.setFitHeight(80);
            imagenProducto.setFitWidth(80);
            if (producto.getImagenProducto() != null) {
                imagenProducto.setImage(new Image(new ByteArrayInputStream(producto.getImagenProducto())));
            }

            // Nombre del producto
            Label nombreProducto = new Label(producto.getNombre());
            nombreProducto.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

            // Precio del producto
            Label precioProducto = new Label("COP " + producto.getPrecio().toString());
            precioProducto.setStyle("-fx-font-size: 12px;");

            // Botón de agregar al carrito
            Button btnAgregarCarrito = new Button("Agregar al Carrito");
            btnAgregarCarrito.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            btnAgregarCarrito.setOnAction(event -> agregarAlCarrito(producto));

            // Añadir componentes al VBox del producto
            vboxProducto.getChildren().addAll(imagenProducto, nombreProducto, precioProducto, btnAgregarCarrito);

            // Añadir VBox del producto al HBox TopProductos
            TopProductos.getChildren().add(vboxProducto);
        }
    }

    //Redirige a la vista de la tienda seleccionada segun id
    private void irATienda(int idTienda) {
        //Usa el tiendaService para obtener la tienda
        Tienda tienda = tiendaService.obtenerTiendaPorId(idTienda);
        if (tienda != null) { //Si tienda esta seleccionada dirige a vista de tienda para cliente
            CambiosVistas.setTiendaSeleccionada(tienda);
            cambiarVista(TopTiendas, "/Vistas/PantallaCuenta/Tienda/View-TiendaACliente.fxml");
        } else {
            System.out.println("Tienda no encontrada con ID: " + idTienda);
        }
    }

    //Agrega el producto al carrito
    private void agregarAlCarrito(Producto producto) {
        //Obtiene el id del carrito del usuario
        int idCarrito = UsuarioActivo.getIdCarrito();
        //Añade la cantidad de 1 item seleccioando al carrito
        producto.setCantidad(1);
        //Usa carritoService para agregar el producto al carrito
        carritoService.agregarProductoAlCarrito(idCarrito, producto);
        System.out.println("Producto agregado al carrito: " + producto.getNombre());
    }

    //Manejo vista busqueda Producto
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
            cambiarVista(BtnComprasAr, "/Vistas/PantallaCuenta/Compras/View-ComprasCreada.fxml");
            //Si usuario no tiene compras muestra vista Compras
        } else {
            cambiarVista(BtnComprasAr, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");
        }
    }

    //Cambio de vista a tiendas
    @FXML
    public void mostrarMisTiendas(ActionEvent event) {
        // Verificar si el usuario es verdeor o no
        if (UsuarioActivo.isVendedor()) {
            // Si es vendedor va a la vista de tiena ya creada
            cambiarVista(BtnMisTiendas, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
        } else {
            // Si no es vendedor va a la vista para crear tienda
            cambiarVista(BtnMisTiendas, "/Vistas/PantallaCuenta/Tienda/View-CrearTienda.fxml");
        }
    }

    //Cambio de vista a perfil
    @FXML
    public void mostrarMiPerfil() {cambiarVista(usuarioIcono, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");}

    //Cambio de vista a carrito
    @FXML
    public void mostrarCarrito() {cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");}

}
