package Modelos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Envio {
    private int idEnvio;
    private String direccionDestino;
    private Date fechaEnvio;
    private Date fechaEntregaEstimada;
    private String estadoEnvio;
    private BigDecimal costoEnvio; // Cambiado a BigDecimal
    private String metodoEnvio;
    private int idVentda;
    private List<Producto> productos;

    // Getters y Setters
    public int getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(int idEnvio) {
        this.idEnvio = idEnvio;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public Date getFechaEntregaEstimada() {
        return fechaEntregaEstimada;
    }

    public void setFechaEntregaEstimada(Date fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public BigDecimal getCostoEnvio() { // Cambiado a BigDecimal
        return costoEnvio;
    }

    public void setCostoEnvio(BigDecimal costoEnvio) { // Cambiado a BigDecimal
        this.costoEnvio = costoEnvio;
    }

    public String getMetodoEnvio() {
        return metodoEnvio;
    }

    public void setMetodoEnvio(String metodoEnvio) {
        this.metodoEnvio = metodoEnvio;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public int getIdVentda() {
        return idVentda;
    }

    public void setIdVentda(int idVentda) {
        this.idVentda = idVentda;
    }
}
