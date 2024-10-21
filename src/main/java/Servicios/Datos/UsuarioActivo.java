package Servicios.Datos;

public class UsuarioActivo {
    private static int idUsuario;
    private static String nombre;
    private static String correoElectronico;
    private static boolean esVendedor;
    private static int idCarrito;
    private static double saldoActual;
    private static double saldoPagar;

    public static void setUsuarioActivo(int id, String nombreUsuario, String correo, boolean vendedor, int idCarritoUsuario, double saldoActualUsuario, double saldoPagarUsuario) {
        idUsuario = id;
        nombre = nombreUsuario;
        correoElectronico = correo;
        esVendedor = vendedor;
        idCarrito = idCarritoUsuario;
        saldoActual = saldoActualUsuario;
        saldoPagar = saldoPagarUsuario;
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

    public static double getSaldoActual() {
        return saldoActual;
    }

    public static void setSaldoActual(double nuevoSaldoActual) {
        saldoActual = nuevoSaldoActual;
    }

    public static double getSaldoPagar() {
        return saldoPagar;
    }

    public static void setSaldoPagar(double nuevoSaldoPagar) {
        saldoPagar = nuevoSaldoPagar;
    }
}
