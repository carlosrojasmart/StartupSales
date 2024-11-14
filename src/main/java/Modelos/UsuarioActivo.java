package Modelos;

import java.math.BigDecimal;

public class UsuarioActivo {
    // Atributos de la clase UsuarioActivo
    private static int idUsuario;
    private static String nombre;
    private static String correoElectronico;
    private static boolean esVendedor;
    private static int idCarrito;
    private static BigDecimal saldoActual = BigDecimal.ZERO;
    private static BigDecimal saldoPagar = BigDecimal.ZERO;

    // Getters y Setters
    public static void setUsuarioActivo(int id, String nombreUsuario, String correo, boolean vendedor, int idCarritoUsuario, BigDecimal saldoActualUsuario, BigDecimal saldoPagarUsuario) {
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

    public static BigDecimal getSaldoActual() {
        return saldoActual;
    }

    public static void setSaldoActual(BigDecimal nuevoSaldoActual) {
        saldoActual = nuevoSaldoActual;
    }

    public static BigDecimal getSaldoPagar() {
        return saldoPagar;
    }

    public static void setSaldoPagar(BigDecimal nuevoSaldoPagar) {
        saldoPagar = nuevoSaldoPagar;
    }
}
