package Servicios.Vistas;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormatoUtil {

    public static String formatearPrecio(BigDecimal precio) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("es-CO"));
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');

        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);
        return df.format(precio) + " COP";
    }
}
