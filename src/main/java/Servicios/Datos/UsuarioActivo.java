package Servicios.Datos;

public class UsuarioActivo {
    private static int idUsuario;
    private static String nombre;
    private static String correoElectronico;
    private static boolean esVendedor;
    private static int idCarrito; // Añade esta variable para almacenar el idCarrito

    public static void setUsuarioActivo(int id, String nombreUsuario, String correo, boolean vendedor, int idCarritoUsuario) {
        idUsuario = id;
        nombre = nombreUsuario;
        correoElectronico = correo;
        esVendedor = vendedor;
        idCarrito = idCarritoUsuario; // Asigna el idCarrito
    }

    public static int getIdUsuario() {
        return idUsuario;
    }

    public static String getNombre() {
        return nombre;
    }

    public static String getCorreoElectronico() {
        return correoElectronico;
    }

    public static boolean isVendedor() {
        return esVendedor;
    }

    public static int getIdCarrito() {
        return idCarrito;
    }
}
