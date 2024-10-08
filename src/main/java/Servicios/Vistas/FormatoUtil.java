package Servicios.Vistas;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatoUtil {

    public static String formatearPrecio(double precio) {
        return String.format("%.2f COP", precio);
    }
}
