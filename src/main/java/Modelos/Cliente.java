package Modelos;

public class Cliente {

    //Se definen los atributos de la clase Cliente
    private int idCliente;
    private String nombre;
    private String direccion;
    private String correoElectronico;
    private String telefono;
    private int idTienda;

    //se crea el constructor de la clase Cliente
    public int getIdCliente() {
        return idCliente;
    }

    // Getters y setters
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdTienda() {return idTienda;}

    public void setIdTienda(int idTienda) {this.idTienda = idTienda;}
}
