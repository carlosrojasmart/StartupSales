package Controladores.Cuenta.Tienda;

import Controladores.Cuenta.Producto.ViewEditarProductoController;
import Modelos.Producto;
import Modelos.Tienda;
import Repositorios.Tienda.CrearTienda;
import Repositorios.Productos.MostrarProductos;
import Repositorios.Tienda.MostrarTiendas;
import Modelos.UsuarioActivo;
import Controladores.Vistas.CambiosVistas;
import Servicios.Util.FormatoUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ViewMirarTiendaController {

    private static Tienda tiendaSeleccionada;

    @FXML
    private TextField buscarProductos;

    @FXML
    private TextField nombreTienda;

    @FXML
    private ChoiceBox<String> categoriaTienda;

    @FXML
    private TextField DescTienda;

    @FXML
    private ImageView imagenTienda;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private Button btnCargarImagen;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnVolverTiendas;

    @FXML
    private Button BtnCompras;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnMiPerfil;

    @FXML
    private Button BtnFacturacion;

    @FXML
    private Button btnAgregarProducto;

    @FXML
    private Button btnEliminar;

    @FXML
    private VBox vboxProductos; // VBox donde se agregarán dinámicamente los productos

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private MostrarTiendas mostrarTiendas = new MostrarTiendas();
    private MostrarProductos mostrarProductos = new MostrarProductos();
    private CrearTienda crearTienda = new CrearTienda();
    private File archivoImagen;

    // Setter estático para recibir la tienda seleccionada
    public static void setTiendaSeleccionada(Tienda tienda) {
        tiendaSeleccionada = tienda;
    }

    @FXML
    private void initialize() {
        int idTienda = CambiosVistas.getIdTiendaSeleccionada();

        // Configurar el buscador de productos
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());

        // Realizar búsqueda cuando el usuario presione "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());

        // Configurar el evento del carrito
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        //Realiza carga de datos tienda
        cargarDatosTienda();

        //Realiza carga de datos tienda por id
        cargarDatosTiendaPorId(idTienda);

        //Realiza carga de evento
        configurarEventos();

        //Carga los productos de la tienda
        cargarProductos();

        //Inicializa boton eliminar tienda
        btnEliminar.setOnAction(event -> eliminarTienda());
    }

    //Carga de datos de tienda por id
    private void cargarDatosTiendaPorId(int idTienda) {
        // Usa Método para obtener la tienda por ID
        Tienda tienda = mostrarTiendas.obtenerTiendaPorId(idTienda);
        if (tienda != null) { //Si tienda es diferente de null
            tiendaSeleccionada = tienda;
            cargarDatosTienda(); // Usar el método existente para cargar los datos de la tienda
        }
    }

    //Carga de datos de tienda a vista
    public void cargarDatosTienda() {
        if (tiendaSeleccionada != null) { // Si hay una tienda seleccionada
            //hace set de nombre tienda
            nombreTienda.setText(tiendaSeleccionada.getNombre());
            //hace set de descripcion tienda
            DescTienda.setText(tiendaSeleccionada.getDescripcion());

            // Cargar la imagen de la tienda si existe
            if (tiendaSeleccionada.getImagen() != null) {
                Image image = new Image(new ByteArrayInputStream(tiendaSeleccionada.getImagen()));
                imagenTienda.setImage(image);
            }

            // Cargar las categorías y seleccionar la actual
            List<String> categorias = mostrarTiendas.obtenerCategorias();
            categoriaTienda.getItems().setAll(categorias);
            categoriaTienda.setValue(tiendaSeleccionada.getCategoria());
        }
    }

    //Carga productos de tienda
    private void cargarProductos() {
        if (tiendaSeleccionada != null) { //Si hay una tienda seleccionada
            List<Producto> productos = mostrarProductos.obtenerProductosDeTienda(tiendaSeleccionada.getIdTienda());
            //muestra productos en vbox productos
            vboxProductos.getChildren().clear();

            //agrega cada producto a la vista
            for (Producto producto : productos) {
                agregarProductoAVista(producto);
            }
        }
    }

    //Logica para agregar el producto a vista
    private void agregarProductoAVista(Producto producto) {
        // Crear el HBox para el producto con un espaciado de 10 entre los elementos
        HBox hboxProducto = new HBox(10);

        //Añade style al Hbox del prouucto
        hboxProducto.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-alignment: CENTER_LEFT; -fx-border-color: #dddddd;");
        hboxProducto.setPrefWidth(600); // Ajustar el ancho del HBox

        // Imagen del producto
        ImageView imagenProducto = new ImageView();

        //Ajusta Imagen del producto
        imagenProducto.setFitHeight(80);
        imagenProducto.setFitWidth(80);

        if (producto.getImagenProducto() != null) { //si el producto tiene una imagen la carga
            Image image = new Image(new ByteArrayInputStream(producto.getImagenProducto()));
            imagenProducto.setImage(image);
        }

        // Nombre del producto
        Label nombreProducto = new Label(producto.getNombre());
        nombreProducto.setStyle("-fx-font-size: 14px; -fx-padding: 0 10 0 10;");

        // Formatear el precio del producto usando FormatoUtil
        Label precioProducto = new Label(FormatoUtil.formatearPrecio(producto.getPrecio()));
        precioProducto.setStyle("-fx-font-size: 14px; -fx-padding: 0 10 0 10;");

        // Botón para editar el producto
        Button btnEditarProducto = new Button("Editar Producto");
        btnEditarProducto.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        btnEditarProducto.setOnAction(event -> editarProducto(producto));

        // Agregar los elementos al HBox
        hboxProducto.getChildren().addAll(imagenProducto, nombreProducto, precioProducto, btnEditarProducto);

        // Ajustar margen del HBox dentro del VBox
        VBox.setMargin(hboxProducto, new Insets(5, 0, 10, 0)); // Espacio superior e inferior de 10px

        // Agregar el HBox al VBox de productos
        vboxProductos.getChildren().add(hboxProducto);
    }


    //Edicion del producto
    private void editarProducto(Producto producto) {
        try {
            // Cargar la vista de edición del producto
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/PantallaCuenta/Producto/View-EditarProducto.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la vista de edición
            ViewEditarProductoController controller = loader.getController();

            // Pasar el producto al controlador de edición
            controller.setProductoSeleccionado(producto);

            // Obtener el Stage actual y mostrar la nueva vista
            Stage stage = (Stage) vboxProductos.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la vista de edición de producto.");
        }
    }


    //Configura los eventos de los botones de la vista
    private void configurarEventos() {
        btnCargarImagen.setOnAction(event -> cargarImagen());
        btnGuardar.setOnAction(event -> guardarCambios());
        btnVolverTiendas.setOnAction(event -> volverATiendas());
    }


    //Guarda cambios realizados en la vista
    private void guardarCambios() {
        // Verificar si se realizaron cambios y actualizar la tienda
        boolean cambiosRealizados = false;

        //Verfica cambios en nombre de la tienda
        if (!nombreTienda.getText().equals(tiendaSeleccionada.getNombre())) {
            tiendaSeleccionada.setNombre(nombreTienda.getText());
            cambiosRealizados = true;
        }

        //Verfica cambios en descripcion de la tienda
        if (!DescTienda.getText().equals(tiendaSeleccionada.getDescripcion())) {
            tiendaSeleccionada.setDescripcion(DescTienda.getText());
            cambiosRealizados = true;
        }

        //Verfica cambios en categoria de la tienda
        if (!categoriaTienda.getValue().equals(tiendaSeleccionada.getCategoria())) {
            tiendaSeleccionada.setCategoria(categoriaTienda.getValue());
            cambiosRealizados = true;
        }

        //Verfica cambios en nombre de la tienda
        if (archivoImagen != null) {
            try {
                tiendaSeleccionada.setImagen(new FileInputStream(archivoImagen).readAllBytes());
                cambiosRealizados = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Actualizar la tienda en la base de datos si hubo cambios
        if (cambiosRealizados) {
            mostrarTiendas.actualizarTienda(tiendaSeleccionada);
            System.out.println("Tienda actualizada correctamente.");
        } else {
            System.out.println("No se realizaron cambios.");
        }
    }


    @FXML
    private void eliminarTienda() {
        //Verifica que exista tienda seleccionada
        if (tiendaSeleccionada == null) {
            System.out.println("No hay tienda seleccionada para eliminar.");
            return;
        }

        // Confirmación de eliminación
        boolean confirmacion = true; // Aquí podrías implementar un diálogo de confirmación
        if (!confirmacion) {
            return;
        }

        // Eliminar la tienda de la base de datos usando mostrarTiendas
        boolean exito = mostrarTiendas.eliminarTienda(tiendaSeleccionada.getIdTienda());
        if (exito) {
            System.out.println("Tienda eliminada correctamente.");
            volverATiendas(); // Regresar a la vista principal de tiendas
        } else {
            System.out.println("Error al eliminar la tienda.");
        }
    }

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
                imagenTienda.setImage(nuevaImagen);
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

    //Cambio vista Tiendas
    @FXML
    private void volverATiendas() {cambiarVista(btnVolverTiendas, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");}

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

    //Cambio vista Inicio
    @FXML
    public void mostrarInicio(ActionEvent event) {cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");}

    //Cambio vista crearProducto
    @FXML
    public void mostrarCrearProducto(ActionEvent event) {cambiarVista(btnAgregarProducto, "/Vistas/PantallaCuenta/Producto/View-Producto.fxml");}

    //get Tienda seleccionada
    public static Tienda getTiendaSeleccionada() {
        return tiendaSeleccionada;
    }
}
