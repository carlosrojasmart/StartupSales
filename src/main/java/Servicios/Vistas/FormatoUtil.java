package Servicios.Vistas;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatoUtil {

    public static String formatearPrecio(double precio) {
        NumberFormat format = NumberFormat.getInstance(Locale.forLanguageTag("es-CO"));
        format.setMaximumFractionDigits(2);
        return format.format(precio) + " COP";
    }
}
