package Controladores.Cuenta.Compras;

import Modelos.Compra;
import Modelos.UsuarioActivo;
import Controladores.Vistas.CambiosVistas;
import Repositorios.Compras.ComprasRepo;
import Servicios.Compras.ComprasService;
import Servicios.Util.FormatoUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.List;

public class ViewComprasCreadaController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private Button BtnMiPerfil;

    @FXML
    private Button BtnTienda;

    @FXML
    private Button BtnFacturacion;

    @FXML
    private VBox vboxCompras; //vbox de carga de compras

    //inicializamos CambioVistas
    private final CambiosVistas cambiosVistas = new CambiosVistas();
    //inicializamos ComprasService
    private final ComprasService comprasService;

    // Constructor para inicializar el servicio de compras
    public ViewComprasCreadaController() {
        this.comprasService = new ComprasService(new ComprasRepo());
    }

    @FXML
    private void initialize() {
        // Limpiar el campo de búsqueda al hacer clic
        buscarProductos.setOnMouseClicked(event -> {buscarProductos.clear();});

        // Asignar la funcionalidad de búsqueda al presionar "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());

        //inicializa el boton de carrito
        carritoCompra.setOnMouseClicked(event -> {mostrarCarrito();});
        cargarCompras();
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

    //Carga y muestra las compras realizadas por el usuario en pantalla
    private void cargarCompras() {
        //Obtiene las compras que realizo el usuario y las ordena en orden descendente
        List<Compra> compras = comprasService.obtenerComprasUsuario(UsuarioActivo.getIdUsuario());
        //Coloca las compras mas recientes el usuario al inicio
        Collections.reverse(compras);

        //Limpia el vbox(Contenedor) de las compras
        vboxCompras.getChildren().clear();

        //Inicia un for para realizar los contenedores de todas las compras
        for (Compra compra : compras) {

            //Usa un hbox para mostrar los datos de la compra y asigna 10 pixles entre cada elemento
            HBox hboxCompra = new HBox(10);
            //hbox recibe un style usado frecuentemente en la aplicacion
            hboxCompra.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #dddddd;");
            //le asigna un tamaño ancho de 600
            hboxCompra.setPrefWidth(600);

            //Se crea y configura las etiquetas de la fecha y hora de la compra

            //Se hace getFecha y se le da un style
            Label fechaLabel = new Label("Fecha: " + compra.getFecha());
            fechaLabel.setStyle("-fx-font-size: 14px;");

            //Se hace getHora y se le da un style
            Label horaLabel = new Label("Hora: " + compra.getHora());
            horaLabel.setStyle("-fx-font-size: 14px;");

            //Se agrega etiqueda de total de la compra utilizando el formatoUtil de precio ys e le da un style
            Label totalCompra = new Label("Total: " + FormatoUtil.formatearPrecio(compra.getTotalCompra()));
            totalCompra.setStyle("-fx-font-size: 14px;");

            //Crea un vbox para mostrar los productos realizados en la compra y se le da un style
            VBox vboxProductos = new VBox(5);
            Label productosTitulo = new Label("Productos:");
            productosTitulo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            vboxProductos.getChildren().add(productosTitulo);

            //Añade cada producto al contenedor creado asegurando que se cree uno por linea
            String[] productosArray = compra.getProductosResumen().split(", ");
            for (String producto : productosArray) {
                Label productoLabel = new Label(producto);
                productoLabel.setStyle("-fx-font-size: 14px;");
                vboxProductos.getChildren().add(productoLabel);
            }

            //Crea el vbox de la fecha y hora para que queden ajustados
            VBox vboxFechaHora = new VBox(5);
            vboxFechaHora.getChildren().addAll(fechaLabel, horaLabel);


            //añade los contenedores a la Hbox principal de la compra
            hboxCompra.getChildren().addAll(vboxFechaHora, totalCompra, vboxProductos);

            //añade esta hbox a el xbox previamente creado
            vboxCompras.getChildren().add(hboxCompra);

        }
    }

    //Cambio de vista general
    private void cambiarVista(Node nodo, String rutaFXML) {
        //Inicializa el stage usando el nodo para cambio de vista
        Stage stage = (Stage) nodo.getScene().getWindow();
        //Usa CambiosVista para realizar el cambio usando el stage y la ruta del fxml
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    //Cambio de vista a tiendas
    @FXML
    public void mostrarMisTiendas(ActionEvent event) {
        // Verificar si el usuario es verdeor o no
        if (UsuarioActivo.isVendedor()) {
            // Si es vendedor va a la vista de tiena ya creada
            cambiarVista(BtnTienda, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
        } else {
            // Si no es vendedor va a la vista para crear tienda
            cambiarVista(BtnTienda, "/Vistas/PantallaCuenta/Tienda/View-CrearTienda.fxml");
        }
    }

    //Cambio de vista a Carrito
    @FXML
    public void mostrarCarrito() {
        cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");
    }

    //Cambio de vista a Perfil
    @FXML
    public void mostrarMiPerfil() {
        cambiarVista(BtnMiPerfil, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");
    }

    //Cambio de vista a facturacion
    @FXML
    public void mostrarFacturacion(ActionEvent event) {
        cambiarVista(BtnFacturacion, "/Vistas/PantallaCuenta/Facturacion/View-Facturacion.fxml");
    }

    //Cambio de vista a Inicio boton superior
    @FXML
    public void mostrarInicio(ActionEvent event) {
        cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");
    }
}
