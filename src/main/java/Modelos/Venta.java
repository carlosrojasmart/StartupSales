package Modelos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Venta {
    private int idVenta;
    private Date fecha;
    private BigDecimal total; // Cambiado a BigDecimal
    private String metodoPago;
    private List<Producto> productos;
    private Cliente cliente;
    private Factura factura;

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public BigDecimal getTotal() { // Cambiado a BigDecimal
        return total;
    }

    public void setTotal(BigDecimal total) { // Cambiado a BigDecimal
        this.total = total;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
}


