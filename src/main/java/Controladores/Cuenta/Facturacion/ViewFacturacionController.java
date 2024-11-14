package Controladores.Cuenta.Facturacion;

import Modelos.UsuarioActivo;
import Repositorios.Datos.SaldoUsuario;
import Controladores.Vistas.CambiosVistas;
import Servicios.Util.FormatoUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class ViewFacturacionController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private Button BtnCompras;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnMiPerfil;

    @FXML
    private Button BtnTienda;

    @FXML
    private Button BtnRecargar;

    @FXML
    private Label lblSaldoActual;

    @FXML
    private Label lblSaldoPagar;

    @FXML
    private VBox vboxFacturas;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private final SaldoUsuario saldoUsuarioRepo = new SaldoUsuario();


    @FXML
    private void initialize() {
        // Configurar el buscador de productos
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());

        // Realizar búsqueda cuando el usuario presione "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());

        // Configurar el evento del carrito
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        // Cargar los saldos del usuario
        cargarSaldosUsuario();
    }

    //Cargamos los saldos del usuario
    private void cargarSaldosUsuario() {
        //Se hace get del salgo Actual utiliza el formato y lo muestra
        lblSaldoActual.setText(FormatoUtil.formatearPrecio(UsuarioActivo.getSaldoActual()));
        //Se hace get del salgo a Pagar utiliza el formato y lo muestra
        lblSaldoPagar.setText(FormatoUtil.formatearPrecio(UsuarioActivo.getSaldoPagar()));
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

    //Procesa la compra y hace los cambios en el saldo actual en la vista
    @FXML
    private void procesarCompra() {
        // Lógica para procesar la compra, restando del saldo actual en la vista y en UsuarioActivo
        BigDecimal nuevoSaldo = UsuarioActivo.getSaldoActual().subtract(UsuarioActivo.getSaldoPagar());

        // Actualiza el saldo en UsuarioActivo
        UsuarioActivo.setSaldoActual(nuevoSaldo);

        // Actualiza el saldo en la base de datos
        guardarSaldoEnBaseDeDatos();

        // Actualizar la vista
        cargarSaldosUsuario();
    }

    //Realiza el cambio del saldo del usuario en la base de datos usando el repo
    private void guardarSaldoEnBaseDeDatos() {
        saldoUsuarioRepo.actualizarSaldo(UsuarioActivo.getIdUsuario(), UsuarioActivo.getSaldoActual());
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
    public void mostrarCarrito() {cambiarVista(carritoCompra, "/Vistas/PantallaPrincipal/View-CarritoCompras.fxml");}

    //Cambio de vista a Recarga
    @FXML
    public void mostrarRecarga() {cambiarVista(BtnRecargar, "/Vistas/PantallaCuenta/Facturacion/View-FacturaRecarga.fxml");}

    //Cambio de vista a Perfil
    @FXML
    public void mostrarMiPerfil() {
        cambiarVista(BtnMiPerfil, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");
    }

    //Cambio de vista a Inicio boton superior
    @FXML
    public void mostrarInicio(ActionEvent event) {cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");}

}
