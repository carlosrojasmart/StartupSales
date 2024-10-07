package Controladores.Principal;

import Modelos.Producto;
import Servicios.Datos.MostrarCarrito;
import Servicios.Datos.UsuarioActivo;
import Servicios.Vistas.CambiosVistas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.util.List;


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
    private VBox vboxPrecio;

    @FXML
    private Label lblTotal;

    @FXML
    private Button btnContinuar;

    @FXML
    private TextField codigoPromocional;

    @FXML
    private Button btnAnadirCodigo;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private MostrarCarrito mostrarCarrito = new MostrarCarrito(); // Clase que obtendrá los productos del carrito

    @FXML
    private void initialize() {
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());
        usuarioIcono.setOnMouseClicked(event -> mostrarMiPerfil());
        cargarProductosCarrito();
    }

    private void cargarProductosCarrito() {
        List<Producto> productos = mostrarCarrito.obtenerProductosDeCarrito(UsuarioActivo.getIdCarrito());
        vboxProductos.getChildren().clear();

        double total = 0;

        for (Producto producto : productos) {
            HBox hboxProducto = new HBox(10);
            hboxProducto.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #dddddd;");
            hboxProducto.setPrefWidth(600);

            // Imagen del producto
            ImageView imagenProducto = new ImageView();
            imagenProducto.setFitHeight(80);
            imagenProducto.setFitWidth(80);
            if (producto.getImagenProducto() != null) {
                Image image = new Image(new ByteArrayInputStream(producto.getImagenProducto()));
                imagenProducto.setImage(image);
            }

            // Nombre del producto
            Label nombreProducto = new Label(producto.getNombre());
            nombreProducto.setStyle("-fx-font-size: 14px;");

            // Spinner para la cantidad del producto (ajustar el ancho aquí)
            Spinner<Integer> spinnerCantidad = new Spinner<>();
            spinnerCantidad.setPrefWidth(60); // Ajustar el ancho del Spinner
            spinnerCantidad.setMaxWidth(60);  // Limitar el ancho máximo del Spinner
            spinnerCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, producto.getCantidad()));
            spinnerCantidad.valueProperty().addListener((obs, oldValue, newValue) -> {
                producto.setCantidad(newValue);
                actualizarTotal();
                mostrarCarrito.actualizarCantidadProducto(producto.getIdProducto(), UsuarioActivo.getIdCarrito(), newValue);
            });

            // Precio del producto
            Label precioProducto = new Label(String.format("$ %.2f", producto.getPrecio() * producto.getCantidad()));
            precioProducto.setStyle("-fx-font-size: 14px;");

            // Botón para eliminar el producto del carrito (cambiar texto a "Eliminar")
            Button btnEliminarProducto = new Button("Eliminar");
            btnEliminarProducto.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            btnEliminarProducto.setOnAction(event -> eliminarProductoDelCarrito(producto));

            // Agregar los elementos al HBox
            hboxProducto.getChildren().addAll(imagenProducto, nombreProducto, spinnerCantidad, precioProducto, btnEliminarProducto);

            // Agregar el HBox al VBox de productos
            vboxProductos.getChildren().add(hboxProducto);

            // Sumar al total
            total += producto.getPrecio() * producto.getCantidad();
        }

        // Actualizar el total
        lblTotal.setText(String.format("Total: $ %.2f", total));
    }

    private void actualizarTotal() {
        double total = 0;

        for (Node node : vboxProductos.getChildren()) {
            if (node instanceof HBox hbox) {
                Label precioLabel = (Label) hbox.getChildren().get(3); // Aquí está el precio
                Spinner<Integer> spinnerCantidad = (Spinner<Integer>) hbox.getChildren().get(2); // Aquí está la cantidad

                // Reemplazar las comas por puntos para evitar el error de formato
                String precioTexto = precioLabel.getText().replace("$", "").trim().replace(",", ".");
                try {
                    double precio = Double.parseDouble(precioTexto);
                    total += precio * spinnerCantidad.getValue();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    System.out.println("Error al convertir el precio: " + precioTexto);
                }
            }
        }
        lblTotal.setText(String.format("Total: $ %.2f", total));
    }

    private void eliminarProductoDelCarrito(Producto producto) {
        mostrarCarrito.eliminarProductoDelCarrito(producto.getIdProducto(), UsuarioActivo.getIdCarrito());
        cargarProductosCarrito();
    }

    private void cambiarVista(Node nodo, String rutaFXML) {
        Stage stage = (Stage) nodo.getScene().getWindow();
        cambiosVistas.cambiarVista(stage, rutaFXML);
    }

    @FXML
    public void mostrarMiPerfil() {
        cambiarVista(usuarioIcono, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");
    }

    @FXML
    public void mostrarMisTiendas(ActionEvent event) {
        if (UsuarioActivo.isVendedor()) {
            cambiarVista(BtnMisTiendas, "/Vistas/PantallaCuenta/Tienda/View-TiendaCreada.fxml");
        } else {
            cambiarVista(BtnMisTiendas, "/Vistas/PantallaCuenta/Tienda/View-CrearTienda.fxml");
        }
    }

    @FXML
    public void mostrarCompras(ActionEvent event) {
        cambiarVista(BtnComprasAr, "/Vistas/PantallaCuenta/Compras/View-Compras.fxml");
    }

}
