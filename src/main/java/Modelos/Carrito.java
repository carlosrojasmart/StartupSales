package Modelos;

import java.util.List;

public class Carrito {

    private int idCarrito;
    private int idUsuario;
    private List<Producto> productos;
    private double total;
    private String codigoPromocional;

    public Carrito(int idCarrito, int idUsuario, List<Producto> productos, double total, String codigoPromocional) {
        this.idCarrito = idCarrito;
        this.idUsuario = idUsuario;
        this.productos = productos;
        this.total = total;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCodigoPromocional() {
        return codigoPromocional;
    }

    public void setCodigoPromocional(String codigoPromocional) {
        this.codigoPromocional = codigoPromocional;
    }
}
