package Modelos;

import java.util.Date;
import java.util.List;

public class Factura {
    private int numeroFactura;
    private Date fecha;
    private double total;
    private int idVenta;
    private int cantidad_total;
    private List<Venta> detalleVenta;

    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public List<Venta> getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(List<Venta> detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdVenta() {return idVenta;}

    public void setIdVenta(int idVenta) {this.idVenta = idVenta;}

    public int getCantidad_total() {return cantidad_total;}

    public void setCantidad_total(int cantidad_total) {this.cantidad_total = cantidad_total;}
}
