package Modelos;

public class Tienda {

    // Atributos de la clase Tienda
    private int idTienda;
    private String nombre;
    private String descripcion;
    private String categoria;
    private byte[] imagen;

    // Constructor vacío
    public Tienda() {
    }

    // Constructor con todos los atributos
    public Tienda(int idTienda, String nombre, String descripcion, String categoria, byte[] imagen) {
        this.idTienda = idTienda;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.imagen = imagen;
    }

    // Getters y Setters
    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
