package Modelos;

import java.math.BigDecimal;
import java.util.List;

public class Carrito {

    private int idCarrito;
    private int idUsuario;
    private List<Producto> productos;
    private BigDecimal total;
    private String codigoPromocional;

    public Carrito(int idCarrito, int idUsuario, List<Producto> productos, BigDecimal total, String codigoPromocional) {
        this.idCarrito = idCarrito;
        this.idUsuario = idUsuario;
        this.productos = productos;
        this.total = total != null ? total : BigDecimal.ZERO; // Inicializa total con 0 si es null
        this.codigoPromocional = codigoPromocional;
    }

    // Getters y setters
    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total != null ? total : BigDecimal.ZERO;
    }

    public String getCodigoPromocional() {
        return codigoPromocional;
    }

    public void setCodigoPromocional(String codigoPromocional) {
        this.codigoPromocional = codigoPromocional;
    }

    public void calcularTotalCarrito() {
        total = productos.stream()
                .map(producto -> producto.getPrecio() // Asumiendo que getPrecio() retorna BigDecimal
                        .multiply(BigDecimal.valueOf(producto.getCantidad()))) // Multiplicar por la cantidad
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sumar todos los valores
    }


}
