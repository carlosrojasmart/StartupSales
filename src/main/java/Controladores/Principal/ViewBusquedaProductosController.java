package Controladores.Principal;

import Modelos.Producto;
import Modelos.Tienda;
import Repositorios.Productos.BusquedaProductos;
import Repositorios.Productos.CrearProducto;
import Repositorios.Productos.MostrarProductos;
import Servicios.Productos.BusquedaProductosService;
import Repositorios.Carrito.MostrarCarrito;
import Modelos.UsuarioActivo;
import Controladores.Vistas.CambiosVistas;
import Servicios.Carrito.CarritoService;
import Servicios.Productos.ProductoService;
import Servicios.Util.FormatoUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Repositorios.Tienda.MostrarTiendas;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;

public class ViewBusquedaProductosController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnComprasAr;

    @FXML
    private ImageView usuarioIcono;

    @FXML
    private Button BtnMisTiendas;

    @FXML
    private Button btnVolverInicio;

    @FXML
    private Label lblProductos;

    @FXML
    private TextField FMin;

    @FXML
    private TextField FMax;

    @FXML
    private ChoiceBox<String> CbCategorias;

    @FXML
    private CheckBox CkTiendas;

    @FXML
    private CheckBox CkProductos;

    @FXML
    private VBox vboxTiendas;
    @FXML
    private VBox vboxProductos;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private BusquedaProductosService busquedaProductosService = new BusquedaProductosService(new BusquedaProductos());
    private MostrarTiendas mostrarTiendas = new MostrarTiendas();
    private final ProductoService productoService = new ProductoService(new CrearProducto(), new MostrarProductos());
    private final CarritoService carritoService = new CarritoService(new MostrarCarrito(), productoService);


    @FXML
    private void initialize() {
        //Inicializa el style vbox de las tiendas y el vbox de los productos
        vboxTiendas.setStyle("-fx-padding: 100 0 0 0;");
        vboxProductos.setStyle("-fx-padding: 20 0 0 0;");

        //Obtiene el termino de busqueda almacenado y realiza la busqueda
        String terminoBusqueda = CambiosVistas.getTerminoBusqueda();
        if (terminoBusqueda != null && !terminoBusqueda.isEmpty()) {
            buscarProductos.setText(terminoBusqueda);
            realizarBusqueda(); //llama ala funcion para realizar la busqueda
        }

        usuarioIcono.setOnMouseClicked(event -> mostrarMiPerfil());
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        cargarCategorias();// Carga las categorias de productos disponibles
    }


    private void cargarCategorias() {
        //Llama al repositorio para obtener las categorias
        List<String> categorias = mostrarTiendas.obtenerCategorias();
        //Limpia el ChoiceBox
        CbCategorias.getItems().clear();
        //Agrega las categorias al choicebox
        CbCategorias.getItems().addAll(categorias);
    }

    @FXML
    private void realizarBusqueda() {
        //Pbtiene la busqueda ingresada
        String terminoBusqueda = buscarProductos.getText().trim();

        //Obtiene la categoria seleccionada
        String categoriaSeleccionada = CbCategorias.getSelectionModel().getSelectedItem();

        //Verifica si las opciones de busqueda para tiendas y productos se seleccionaron
        boolean tiendasSeleccionadas = CkTiendas.isSelected();
        boolean productosSeleccionados = CkProductos.isSelected();

        //Obtiene el precio minimo y maximo del filtro
        BigDecimal precioMin = FMin.getText().isEmpty() ? null : new BigDecimal(FMin.getText());
        BigDecimal precioMax = FMax.getText().isEmpty() ? null : new BigDecimal(FMax.getText());

        //Limpia las vistas de resultado previas para productos y tiendas
        vboxTiendas.getChildren().clear();
        vboxProductos.getChildren().clear();

        //Si se selcciona la busqueda de tiendas
        if (tiendasSeleccionadas) {
            //Llama al repositorio para buscar las tiendas por nombre
            List<Tienda> tiendas = mostrarTiendas.buscarTiendasPorNombre(terminoBusqueda);

            //Filtra las tiendas por categoria si se selecciono
            if (categoriaSeleccionada != null && !categoriaSeleccionada.isEmpty()) {
                tiendas = tiendas.stream()
                        .filter(tienda -> tienda.getCategoria().equals(categoriaSeleccionada))
                        .toList();
            }

            //Si encuentra tiendas con los filtros las muestra en la vista
            if (!tiendas.isEmpty()) {
                Label labelTiendas = new Label("Tiendas:");
                labelTiendas.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                vboxTiendas.getChildren().add(labelTiendas);
                //Muestra tiendas
                mostrarResultadosTiendas(tiendas);
            }
        }


        //Si se selecciona la busqueda de productos
        if (productosSeleccionados) {
            //Llama al servicio para busqcar los productos con los filtros
            List<Producto> productos = busquedaProductosService.buscarConFiltros(terminoBusqueda, categoriaSeleccionada, precioMin, precioMax);
            if (!productos.isEmpty()) {
                Label labelProductos = new Label("Productos:");
                labelProductos.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                vboxProductos.getChildren().add(labelProductos);
                //Muestra los productos
                mostrarResultadosBusqueda(productos);
            }
        }
    }

    //Muestra los resultados de las tiendas en la vista
    private void mostrarResultadosTiendas(List<Tienda> tiendas) {
        for (Tienda tienda : tiendas) {
            //Carga las tiendas
            agregarTiendaAVista(tienda);
        }
    }

    //Muestra los resultados de los productos en la vista
    private void mostrarResultadosBusqueda(List<Producto> productos) {
        for (Producto producto : productos) {
            //Carga los productos
            agregarProductoAVista(producto);
        }
    }

    //Agrega los productos a la vista
    private void agregarProductoAVista(Producto producto) {
        // Configura el HBox para el producto
        HBox hboxProducto = new HBox(10);
        hboxProducto.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #dddddd;");
        hboxProducto.setPrefWidth(600);

        // Configura la imagen del producto
        ImageView imagenProducto = new ImageView();
        imagenProducto.setFitHeight(80);
        imagenProducto.setFitWidth(80);
        if (producto.getImagenProducto() != null) {
            Image image = new Image(new ByteArrayInputStream(producto.getImagenProducto()));
            imagenProducto.setImage(image);
        }

        // Configura el nombre del producto
        Label nombreProducto = new Label(producto.getNombre());
        nombreProducto.setStyle("-fx-font-size: 14px;");

        // Configura el precio del producto
        Label precioProducto = new Label(FormatoUtil.formatearPrecio(producto.getPrecio()));
        precioProducto.setStyle("-fx-font-size: 14px;");

        // Configura el botón para agregar al carrito
        Button btnAgregarCarrito = new Button("Agregar al Carrito");
        btnAgregarCarrito.setOnAction(event -> agregarAlCarrito(producto));

        // Agrega los elementos al HBox y el HBox al VBox de productos
        hboxProducto.getChildren().addAll(imagenProducto, nombreProducto, precioProducto, btnAgregarCarrito);
        vboxProductos.getChildren().add(hboxProducto);
    }

    //Agrega als tiendas a la vista
    private void agregarTiendaAVista(Tienda tienda) {
        // Configura el HBox para la tienda
        HBox hboxTienda = new HBox(10);
        hboxTienda.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #dddddd;");
        hboxTienda.setPrefWidth(600);

        // Configura la imagen de la tienda
        ImageView imagenTienda = new ImageView();
        imagenTienda.setFitHeight(80);
        imagenTienda.setFitWidth(80);

        if (tienda.getImagen() != null) {
            Image image = new Image(new ByteArrayInputStream(tienda.getImagen()));
            imagenTienda.setImage(image);
        }

        // Configura el nombre de la tienda
        Label nombreTienda = new Label(tienda.getNombre());
        nombreTienda.setStyle("-fx-font-size: 14px;");

        // Configura el botón para ver la tienda
        Button btnVerTienda = new Button("Ver Tienda");
        btnVerTienda.setOnAction(event -> irATienda(tienda.getIdTienda()));

        // Agrega los elementos al HBox y el HBox al VBox de tiendas
        hboxTienda.getChildren().addAll(imagenTienda, nombreTienda, btnVerTienda);
        vboxProductos.getChildren().add(hboxTienda);
    }

    //Cambia la vista a tienda seleccionada
    private void irATienda(int idTienda) {
        //obtiene la tienda por id
        Tienda tienda = mostrarTiendas.obtenerTiendaPorId(idTienda);

        //Si se selecciono una tienda cambia a la vista de la tienda
        if (tienda != null) {
            CambiosVistas.setTiendaSeleccionada(tienda);
            cambiarVista(vboxProductos, "/Vistas/PantallaCuenta/Tienda/View-TiendaACliente.fxml");
        } else {
            System.out.println("Tienda no encontrada con el ID: " + idTienda);
        }
    }

    //Agrega un item al carrito
    private void agregarAlCarrito(Producto producto) {
        //Obtene el id del carrito del usuario
        int idCarrito = UsuarioActivo.getIdCarrito();
        //Si no en cuentra el carrito
        if (idCarrito == -1) {
            System.out.println("Error: No se ha encontrado un carrito asociado para el usuario.");
            return;
        }

        MostrarCarrito mostrarCarrito = new MostrarCarrito();
        //Agrega 1 de cantidad del iten al carrito
        producto.setCantidad(1);
        //Usa el carritoService para agregar el producto al carrito del usuario
        carritoService.agregarProductoAlCarrito(idCarrito, producto);
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

    //Cambio de vista a inicio
    @FXML
    public void volverInicio(ActionEvent event) {cambiarVista(btnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");}
}
