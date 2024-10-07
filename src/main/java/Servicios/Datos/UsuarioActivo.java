package Servicios.Datos;

public class UsuarioActivo {
    private static int idUsuario;
    private static String nombre;
    private static String correoElectronico;
    private static boolean esVendedor;  // Nuevo campo para almacenar si es vendedor o no

    // Método para configurar los datos del usuario activo
    public static void setUsuarioActivo(int id, String nombreUsuario, String correo, boolean vendedor) {
        idUsuario = id;
        nombre = nombreUsuario;
        correoElectronico = correo;
        esVendedor = vendedor;  // Configurar si es vendedor
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
}
