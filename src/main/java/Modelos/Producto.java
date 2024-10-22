package Modelos;

public class Producto {
    private int idProducto;
    private String nombre;
    private double precio;
    private String descripcion;
    private int stock;
    private String categoria;
    private byte[] imagenProducto;
    private int idTienda;
    private int cantidad; // Este atributo se añadió para la gestión del carrito

    // Constructor vacío
    public Producto() {
    }

    // Constructor con todos los atributos
    public Producto(int idProducto, String nombre, double precio, byte[] imagenProducto, int cantidad) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.imagenProducto = imagenProducto;
        this.cantidad = cantidad;
    }

    // Getters y Setters (incluyendo el nuevo atributo 'cantidad')
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public byte[] getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(byte[] imagenProducto) {
        this.imagenProducto = imagenProducto;
    }

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
