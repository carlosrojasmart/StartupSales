package Controladores.Cuenta.Facturacion;

import Controladores.Vistas.CambiosVistas;
import Modelos.UsuarioActivo;
import Repositorios.Datos.SaldoUsuario;
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

public class ViewFacturaRecargaController {

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
    private Button BtnRealizarRecarga;

    @FXML
    private TextField lblValorRecarga;

    @FXML
    private Label lblInforme;

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

        // Evento para el botón de recarga
        BtnRealizarRecarga.setOnAction(event -> realizarRecarga());
    }

    private void realizarRecarga() {
        try {
            // Obtener el valor ingresado en lblValorRecarga y convertirlo a BigDecimal
            BigDecimal valorRecarga = new BigDecimal(lblValorRecarga.getText().trim());

            // Valida si el valor de recarga es positivo
            if (valorRecarga.compareTo(BigDecimal.ZERO) <= 0) {
                lblInforme.setText("Valor de Recarga no puede ser negativo o cero.");
                return;  // Salir del método sin hacer la recarga ni cambiar de vista
            }

            // Obtener el saldo actual de UsuarioActivo
            BigDecimal saldoActual = UsuarioActivo.getSaldoActual();

            // Sumar el valor de la recarga al saldo actual
            BigDecimal nuevoSaldo = saldoActual.add(valorRecarga);
            UsuarioActivo.setSaldoActual(nuevoSaldo);

            // Actualizar el saldo en la base de datos
            saldoUsuarioRepo.actualizarSaldo(UsuarioActivo.getIdUsuario(), nuevoSaldo);

            // Limpiar el campo de recarga después de la operación
            lblValorRecarga.clear();
            lblInforme.setText("Recarga exitosa. Nuevo saldo: " + FormatoUtil.formatearPrecio(nuevoSaldo));

            // Redirigir a la vista de facturación
            cambiarVista(BtnRealizarRecarga, "/Vistas/PantallaCuenta/Facturacion/View-Facturacion.fxml");

        } catch (NumberFormatException e) {
            lblInforme.setText("Error: Ingresa un valor válido para la recarga.");
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

    //Cambio de vista a Perfil
    @FXML
    public void mostrarMiPerfil() {
        cambiarVista(BtnMiPerfil, "/Vistas/PantallaCuenta/MiPerfil/View-MiPerfil.fxml");
    }

    //Cambio de vista a Inicio boton superior
    @FXML
    public void mostrarInicio(ActionEvent event) {cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");}

}
