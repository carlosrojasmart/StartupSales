package Modelos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Compra {
    private int idCompra;
    private int idUsuario;
    private double totalCompra;
    private LocalDate fecha;
    private LocalTime hora;
    private Map<Producto, Integer> productos;
    private String productosResumen;

    // Constructor vacío
    public Compra() {
    }

    // Constructor con parámetros
    public Compra(int idCompra, int idUsuario, double totalCompra, LocalDate fecha, LocalTime hora, Map<Producto, Integer> productos) {
        this.idCompra = idCompra;
        this.idUsuario = idUsuario;
        this.totalCompra = totalCompra;
        this.fecha = fecha;
        this.hora = hora;
        this.productos = productos;
        this.productosResumen = generarProductosResumen();
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Map<Producto, Integer> getProductos() {
        return productos;
    }

    public void setProductos(Map<Producto, Integer> productos) {
        this.productos = productos;
        this.productosResumen = generarProductosResumen();
    }

    public String getProductosResumen() {
        return productosResumen;
    }

    public void setProductosResumen(String productosResumen) {
        this.productosResumen = productosResumen;
    }

    // Método para calcular el total de la compra a partir de los productos y sus cantidades
    public void calcularTotalCompra() {
        totalCompra = productos.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrecio() * entry.getValue())
                .sum();
    }

    // Método para generar un resumen de los productos comprados como un String
    private String generarProductosResumen() {
        if (productos == null || productos.isEmpty()) {
            return "";
        }
        return productos.entrySet().stream()
                .map(entry -> entry.getKey().getNombre() + " x" + entry.getValue())
                .collect(Collectors.joining(", "));
    }

}
