package Controladores.Cuenta.Tienda;

import Modelos.Tienda;
import Repositorios.Tienda.MostrarTiendas;
import Modelos.UsuarioActivo;
import Controladores.Vistas.CambiosVistas;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ViewTiendaCreadaController {
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
    private Button BtnCrearTienda;

    @FXML
    private ScrollPane scrollPaneTiendas;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private MostrarTiendas mostrarTiendas = new MostrarTiendas();

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> {
            buscarProductos.clear();
        });
        buscarProductos.setOnMouseClicked(event -> {buscarProductos.clear();});
        // Realizar búsqueda cuando el usuario presione "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());
        // Configurar el evento del carrito
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        int idUsuario = obtenerIdUsuario();

        // Obtener la lista de tiendas y crear la vista
        List<Tienda> tiendas = mostrarTiendas.obtenerTiendas(idUsuario);
        VBox vistaTiendas = crearVistaTiendas(tiendas);
        scrollPaneTiendas.setContent(vistaTiendas);
    }

    //Crea la vista de las tiendas creadas
    private VBox crearVistaTiendas(List<Tienda> tiendas) {
        //Inicializa el contenedor de tiendas
        VBox contenedorTiendas = new VBox();
        contenedorTiendas.setSpacing(15);

        //Usa un for para realizar la creacion de todas las tiendas
        for (Tienda tienda : tiendas) {
            //Crea el Hbox para una tienda y le da un espaciado y un style
            HBox tiendaBox = new HBox();
            tiendaBox.setSpacing(15);
            tiendaBox.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #DDDDDD; -fx-border-radius: 5; -fx-background-radius: 5;");


            //Configura la imagen de la tienda en el imageView haciendo set de ahco y alto
            ImageView imagenTienda = new ImageView();
            imagenTienda.setFitHeight(100);
            imagenTienda.setFitWidth(100);

            //Carha la imagen si la tienda tiene una
            if (tienda.getImagen() != null) {
                Image image = new Image(new ByteArrayInputStream(tienda.getImagen()));
                imagenTienda.setImage(image);
            }


            //Etiqueta el nombre de la tienda y le da un style
            Label nombreTienda = new Label(tienda.getNombre());
            nombreTienda.setStyle("-fx-font-family: 'MS Reference Sans Serif'; -fx-font-size: 14px;");

            //Crea el boton para ir a la tienda
            Button irATienda = new Button("Ir a tienda");
            irATienda.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-family: 'MS Reference Sans Serif'; -fx-font-size: 14px;");
            irATienda.setOnAction(event -> irATienda(tienda, event));

            //Agrega los elementos al hbox de la tienda
            tiendaBox.getChildren().addAll(imagenTienda, nombreTienda, irATienda);
            contenedorTiendas.getChildren().add(tiendaBox);
        }

        //Hace return del contenedor de tiendas
        return contenedorTiendas;
    }

    // Ir a tienda seleccionada de lista de tiendas
    private void irATienda(Tienda tienda, ActionEvent event) {
        ViewMirarTiendaController.setTiendaSeleccionada(tienda);
        cambiarVista((Node) event.getSource(), "/Vistas/PantallaCuenta/Tienda/View-MirarTienda.fxml");
    }

    private int obtenerIdUsuario() {
        return UsuarioActivo.getIdUsuario();
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
            cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-ComprasCreada.fxml");
            //Si usuario no tiene compras muestra vista Compras
        } else {
            cambiarVista(BtnCompras, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");
        }
    }


    //Cambio vista carrito
    @FXML
    public void mostrarCarrito() {cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");}

    //Cambio vista perfil
    @FXML
    public void mostrarMiPerfil() {cambiarVista(BtnMiPerfil, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");}

    //Cambio vista Facturacion
    @FXML
    public void mostrarFacturacion(ActionEvent event) {cambiarVista(BtnFacturacion, "/Vistas/PantallaCuenta/Facturacion/View-Facturacion.fxml");}

    //Cambio vista inicio
    @FXML
    public void mostrarInicio(ActionEvent event) {cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");}

    //Cambio a vista crear tienda
    @FXML
    public void crearTienda(ActionEvent event) {cambiarVista(BtnCrearTienda, "/Vistas/PantallaCuenta/Tienda/View-CreandoTienda.fxml");}

}
