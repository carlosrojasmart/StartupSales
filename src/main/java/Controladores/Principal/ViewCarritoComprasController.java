package Controladores.Principal;

import Modelos.Producto;
import Repositorios.Carrito.MostrarCarrito;
import Modelos.UsuarioActivo;
import Controladores.Vistas.CambiosVistas;
import Repositorios.Datos.SaldoUsuario;
import Repositorios.Productos.CrearProducto;
import Repositorios.Productos.MostrarProductos;
import Servicios.Carrito.CarritoService;
import Servicios.Productos.ProductoService;
import Servicios.Util.FormatoUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.io.ByteArrayInputStream;

public class ViewCarritoComprasController {

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
    private VBox vboxProductos;

    @FXML
    private Label lblTotal;

    @FXML
    private Button btnContinuar;

    @FXML
    private Label lblAvisoSaldo;


    private final ProductoService productoService = new ProductoService(new CrearProducto(), new MostrarProductos());
    private final CarritoService carritoService = new CarritoService(new MostrarCarrito(), productoService);
    private final CambiosVistas cambiosVistas = new CambiosVistas();

    @FXML
    private void initialize() {

        // Limpiar el campo de búsqueda al hacer clic
        buscarProductos.setOnMouseClicked(event -> {buscarProductos.clear();});

        // Asignar la funcionalidad de búsqueda al presionar "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());

        //Inicializa el icono de perfil
        usuarioIcono.setOnMouseClicked(event -> mostrarMiPerfil());

        //Carga los productos al carrito
        cargarProductosCarrito();

    }

    //Carga y muestra los productos en el carrito de compras
    private void cargarProductosCarrito() {
        //Crea la lista de productos y usa el carrito service para obtener los productos del carrito
        List<Producto> productos = carritoService.obtenerProductosDeCarrito(UsuarioActivo.getIdCarrito());
        vboxProductos.getChildren().clear(); //Limpia vbox de productos

        //Crea producto por producto
        for (Producto producto : productos) {
            //Crea un hbox para el producto con margen de 10 pixeles le da un style y un ancho
            HBox hboxProducto = new HBox(10);
            hboxProducto.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #dddddd;");
            hboxProducto.setPrefWidth(600);

            //Crea el imageView del producto y le da medidas
            ImageView imagenProducto = new ImageView();
            imagenProducto.setFitHeight(80);
            imagenProducto.setFitWidth(80);
            if (producto.getImagenProducto() != null) {//Verifica que el producto tenga imagen y la carga al imageView
                Image image = new Image(new ByteArrayInputStream(producto.getImagenProducto()));
                imagenProducto.setImage(image);
            }

            //Agrega el nombre del producto
            Label nombreProducto = new Label(producto.getNombre());
            nombreProducto.setStyle("-fx-font-size: 14px;");

            //Inicializa el spinner para que el usuario pueda aumentar o disminuir cantidad el producto
            Spinner<Integer> spinnerCantidad = new Spinner<>();
            spinnerCantidad.setPrefWidth(55);
            spinnerCantidad.setMaxWidth(55);
            spinnerCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, producto.getCantidad()));
            spinnerCantidad.valueProperty().addListener((obs, oldValue, newValue) -> {
                //Usa el carritoService para actualizar la cantidad del producto
                carritoService.actualizarCantidadProducto(producto.getIdProducto(), UsuarioActivo.getIdCarrito(), newValue);
                actualizarTotal();
            });

            //Agrega el precio del producto
            Label precioProducto = new Label(FormatoUtil.formatearPrecio(producto.getPrecio()));
            precioProducto.setStyle("-fx-font-size: 14px;");

            //Agrega el boton eliminar producto
            Button btnEliminarProducto = new Button("Eliminar");
            btnEliminarProducto.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");
            btnEliminarProducto.setOnAction(event -> eliminarProductoDelCarrito(producto));

            ///Agrega el item al hbox y al vbox de productos
            hboxProducto.getChildren().addAll(imagenProducto, nombreProducto, spinnerCantidad, precioProducto, btnEliminarProducto);
            vboxProductos.getChildren().add(hboxProducto);
        }

        //Actualiza el total del carrito
        actualizarTotal();
    }

    //Calcula y actualiza el total del carrito
    private void actualizarTotal() {
        //Inicializa un total en cero
        BigDecimal total = BigDecimal.ZERO;

        //Recorre cada nodo dentro del vbox de productos pare procesar cada producto
        for (Node node : vboxProductos.getChildren()) {
            //Verifica si el nodo es un Hbox que contiene informacion del producto
            if (node instanceof HBox hbox) {
                //Obtiene el label que muestra el precio del producto y el spinner que muestra la cantidad de este
                Label precioLabel = (Label) hbox.getChildren().get(3); //marca el inice 3 como precio del producto
                Spinner<Integer> spinnerCantidad = (Spinner<Integer>) hbox.getChildren().get(2); //marca el indice 2 como la cantidad el producto

                //Obtiene el texto del precio y lo formatea a BigDecimal
                String precioTexto = precioLabel.getText()
                        .replace("COP", "") //Elimina la moneda
                        .replace(",", "") //Elimina las comas
                        .trim(); //Elimina espacios

                try {
                    //Convierte el precio del texto a BigDecimal
                    BigDecimal precio = new BigDecimal(precioTexto);
                    // Calcula el subtotal multiplicando el precio por la cantidad
                    BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(spinnerCantidad.getValue()));
                    // Agrega el subtotal al total general
                    total = total.add(subtotal);
                } catch (NumberFormatException e) {
                    System.out.println("Error al convertir el precio: " + precioTexto);
                }
            }
        }

        //Muestra el total calculado al usuario
        lblTotal.setText("Total: " + FormatoUtil.formatearPrecio(total));
    }

    //Elimina el producto del carrito y actualiza la vista
    private void eliminarProductoDelCarrito(Producto producto) {
        //Usa el carrito service para eliminar el producto del carrito del usuario
        carritoService.eliminarProductoDelCarrito(producto.getIdProducto(), UsuarioActivo.getIdCarrito());
        cargarProductosCarrito();
    }

    @FXML
    public void procesarCompra() {
        BigDecimal saldoActual = UsuarioActivo.getSaldoActual();
        BigDecimal totalCarrito = obtenerTotalCarrito();

        if (saldoActual.compareTo(totalCarrito) < 0) {
            lblAvisoSaldo.setText("Saldo Insuficiente");
        } else {
            // Actualiza el saldo en UsuarioActivo
            BigDecimal nuevoSaldo = saldoActual.subtract(totalCarrito);
            UsuarioActivo.setSaldoActual(nuevoSaldo);

            // Procesa la compra en la base de datos
            carritoService.procesarCompra(UsuarioActivo.getIdUsuario(), UsuarioActivo.getIdCarrito(), totalCarrito);

            // Guarda el saldo actualizado en la base de datos
            SaldoUsuario saldoUsuarioRepo = new SaldoUsuario(); // Crea una instancia
            saldoUsuarioRepo.actualizarSaldo(UsuarioActivo.getIdUsuario(), nuevoSaldo); // Actualiza en la base de datos

            // Actualiza la vista y muestra el éxito de la compra
            lblAvisoSaldo.setText("Compra realizada con éxito");
            lblTotal.setText("Total: 0.00 COP");
            carritoService.vaciarCarrito(UsuarioActivo.getIdCarrito());
            cargarProductosCarrito();
        }
    }

    private BigDecimal obtenerTotalCarrito() {
        BigDecimal total = BigDecimal.ZERO;

        for (Node node : vboxProductos.getChildren()) {
            if (node instanceof HBox hbox) {
                Label precioLabel = (Label) hbox.getChildren().get(3);
                Spinner<Integer> spinnerCantidad = (Spinner<Integer>) hbox.getChildren().get(2);

                String precioTexto = precioLabel.getText()
                        .replace("COP", "")
                        .replace(",", "")
                        .trim();

                try {
                    BigDecimal precio = new BigDecimal(precioTexto);
                    total = total.add(precio.multiply(BigDecimal.valueOf(spinnerCantidad.getValue())));
                } catch (NumberFormatException e) {
                    System.out.println("Error al convertir el precio: " + precioTexto);
                }
            }
        }

        return total.setScale(2, RoundingMode.HALF_UP);
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

    //Cambio de vista a perfil
    @FXML
    public void mostrarMiPerfil() {cambiarVista(usuarioIcono, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");}

}
