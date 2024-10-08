package Modelos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class Compra {
    private int idCompra;
    private int idUsuario;
    private double totalCompra;
    private LocalDate fecha;
    private LocalTime hora;
    private Map<Producto, Integer> productos; // Mapa que relaciona productos con su cantidad

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
    }

    // Getters y Setters
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
    }

    // Método para calcular el total de la compra a partir de los productos y sus cantidades
    public void calcularTotalCompra() {
        totalCompra = productos.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrecio() * entry.getValue())
                .sum();
    }
}
