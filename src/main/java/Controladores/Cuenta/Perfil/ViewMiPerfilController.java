package Controladores.Cuenta.Perfil;

import Controladores.Vistas.CambiosVistas;
import Modelos.UsuarioActivo;
import Repositorios.Perfil.ModificarPerfil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class ViewMiPerfilController {

    @FXML
    private TextField buscarProductos;

    @FXML
    private ImageView carritoCompra;

    @FXML
    private Button BtnVolverInicio;

    @FXML
    private Button BtnCompras;

    @FXML
    private Button BtnTienda;

    @FXML
    private Button BtnFacturacion;

    @FXML
    private ImageView imagenPerfil;

    @FXML
    private Label nombreUsuario;

    @FXML
    private Label correoUsuario;

    @FXML
    private Button btnCambiarImagen;

    @FXML
    private TextField cambiarUsuario;

    @FXML
    private TextField cambiarCorreo;

    @FXML
    private TextField cambiarContraseña;

    @FXML
    private TextField cambiarTelefono;

    @FXML
    private TextField cambiarDireccion;

    @FXML
    private Button btnGuardarCambio;

    private CambiosVistas cambiosVistas = new CambiosVistas();
    private ModificarPerfil modificarPerfil = new ModificarPerfil();

    @FXML
    private void initialize() {
        // Configurar el buscador de productos
        buscarProductos.setOnMouseClicked(event -> buscarProductos.clear());

        // Realizar búsqueda cuando el usuario presione "Enter"
        buscarProductos.setOnAction(event -> realizarBusqueda());

        // Configurar el evento del carrito
        carritoCompra.setOnMouseClicked(event -> mostrarCarrito());

        //hace set del nombre para mostrar en perfil
        nombreUsuario.setText(UsuarioActivo.getNombre());

        //hace set del perfil para mostrar en perfil
        correoUsuario.setText(UsuarioActivo.getCorreoElectronico());

        //Carha la imagen del usuario y sus datos
        try {
            cargarImagenPerfil();
            cargarDatosUsuario();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    //Cargar imagen de perfil
    private void cargarImagenPerfil() throws SQLException, IOException {
        //Otiene los bytes de la imagen desde el repo
        byte[] imageBytes = modificarPerfil.cargarImagenPerfil();
        if (imageBytes != null) { //Si imagen no es nula
            Image imagen = new Image(new ByteArrayInputStream(imageBytes)); //Convierte los bytes a objeto imagen
            imagenPerfil.setImage(imagen);//Establece la imagen en el imagen view
        }
    }

    //Carga los datos del usuario
    private void cargarDatosUsuario() {
        try {
            //Obtiene los datos del usuario usando el repo
            String[] datosUsuario = modificarPerfil.obtenerDatosUsuario(UsuarioActivo.getIdUsuario());
            //Muestra nombre en campo
            cambiarUsuario.setText(datosUsuario[0]);
            //Muestra correo en campo
            cambiarCorreo.setText(datosUsuario[1]);
            //Muestra telefono en campo
            cambiarTelefono.setText(datosUsuario[2]);
            //Muestra Direccion en campo
            cambiarDireccion.setText(datosUsuario[3]);
            //No muestra contraseña por temas de seguridad
            cambiarContraseña.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Guardar cambios realizados en datos
    @FXML
    public void guardarCambios() {
        try {
            //Mira si se realizo algun cambio en los datos
            String nuevoUsuario = cambiarUsuario.getText();
            String nuevoCorreo = cambiarCorreo.getText();
            String nuevaContraseña = cambiarContraseña.getText();
            String nuevoTelefono = cambiarTelefono.getText();
            String nuevaDireccion = cambiarDireccion.getText();

            //Envia los datos actualizados al repo para hacer cambios en base
            modificarPerfil.guardarCambiosPerfil(UsuarioActivo.getIdUsuario(), nuevoUsuario, nuevoCorreo, nuevaContraseña, nuevoTelefono, nuevaDireccion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Cambiar imagen de perfil
    @FXML
    private void cambiarImagenPerfil() {
        //Crea un seleccionador de archivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                //Filtra el selccionador para asegurar que sea una imagen
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        //Abre el Dialog para que el usuario seleccione una imagen
        File archivoSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoSeleccionado != null) { //Si usuario selecciono una imagen
            try {
                //Carga la imagen seleccionada al imagenView
                Image nuevaImagen = new Image(new FileInputStream(archivoSeleccionado));
                imagenPerfil.setImage(nuevaImagen);

                //Guarda la nueva imagen en la base de datos usando el repo
                modificarPerfil.guardarImagenPerfil(archivoSeleccionado);
            } catch (IOException | SQLException e) {
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

    //Cambio de vista a Facturacion
    @FXML
    public void mostrarFacturacion(ActionEvent event) {cambiarVista(BtnFacturacion, "/Vistas/PantallaCuenta/Facturacion/View-Facturacion.fxml");}

    //Cambio de vista a Inicio boton superior
    @FXML
    public void mostrarInicio(ActionEvent event) {cambiarVista(BtnVolverInicio, "/Vistas/PantallaPrincipal/View-InicialLogeado.fxml");}
}
