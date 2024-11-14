package Controladores.Cuenta.Tienda;

import Controladores.Vistas.CambiosVistas;
import Modelos.UsuarioActivo;
import Repositorios.Tienda.CrearTienda;
import Repositorios.Tienda.MostrarTiendas;
import Servicios.Tienda.TiendaService;
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

public class ViewCreandoTiendaController {

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
    private TextField nombreTienda;

    @FXML
    private TextArea descTienda;

    @FXML
    private ChoiceBox<String> boxCategoria;

    @FXML
    private Button BtnCargarImagen;

    @FXML
    private ImageView imagenTienda;

    @FXML
    private Button BtnCrearTienda;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private TiendaService tiendaService = new TiendaService(new MostrarTiendas(), new CrearTienda());  // Crear instancias de ambos repositorios
    private File archivoImagenSeleccionado; // Almacenará la imagen seleccionada para la tienda

    @FXML
    private void initialize() {

        // Configurar el buscador de productos
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());

        // Realizar búsqueda cuando el usuario presione "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());

        // Configurar el evento del carrito
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        //inicializa las categorias posibles para la creacion de tienda
        boxCategoria.getItems().addAll("Electrónica", "Ropa y Moda", "Hogar y Jardín", "Salud y Belleza", "Deportes", "Juguetes", "Alimentos", "Automóviles", "Libros", "Mascotas");
    }

    //Cargar imagen
    @FXML
    private void cargarImagen(ActionEvent event) {
        //Crea un seleccionador de archivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                //Filtra el selccionador para asegurar que sea una imagen
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        //Abre el Dialog para que usuario seleccione imagen
        archivoImagenSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoImagenSeleccionado != null) { //Si usuario Carga una imagen
            try {
                // Convierte el archivo de imagen a Image para mostrar en el ImageView
                Image image = new Image(new FileInputStream(archivoImagenSeleccionado));

                //hace set de la nueva imagen
                imagenTienda.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al cargar la imagen en el controlador.");
            }
        }
    }


    //Crar la tienda
    @FXML
    private void crearTienda(ActionEvent event) {
        // Recopila los valores de los campos de entrada del formulario.

        //Nombre tienda
        String nombre = nombreTienda.getText();

        //Descripcion de la tienda
        String descripcion = descTienda.getText();

        //Categoria de la tienda
        String categoria = boxCategoria.getValue();

        //Id de usuario que crea la tienda
        int idUsuario = UsuarioActivo.getIdUsuario();

        //Valida que todos los campos obligatorios para la creacion no esten vacios
        if (nombre.isEmpty() || descripcion.isEmpty() || categoria == null || archivoImagenSeleccionado == null) {
            System.out.println("Por favor, complete todos los campos.");
            return;
        }

        //LLama al producto tiendaService para crear la tienda con los datos ingresados
        boolean exito = tiendaService.crearTienda(nombre, descripcion, idUsuario, categoria, archivoImagenSeleccionado);

        //Mensajes de depuracion de exito y error
        if (exito) {
            cambiarVista((Node) event.getSource(), "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
            System.out.println("Tienda creada exitosamente.");
        } else {
            System.out.println("Error al crear la tienda.");
        }
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

}
