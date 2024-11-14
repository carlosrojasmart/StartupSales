package Servicios.Util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormatoUtil {

    // Método para dar formato a un precio en BigDecimal y agregar la moneda "COP"
    public static String formatearPrecio(BigDecimal precio) {
        // Configura los símbolos de formato específicos para el idioma "es-CO" (Colombia)
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("es-CO"));

        // Define el separador decimal como un punto "."
        symbols.setDecimalSeparator('.');

        // Define el separador de miles como una coma ","
        symbols.setGroupingSeparator(',');

        // Crea una instancia de DecimalFormat con el formato "#,##0.00" usando los símbolos configurados
        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

        // Retorna el precio formateado como una cadena seguida de la moneda "COP"
        return df.format(precio) + " COP";
    }
}
