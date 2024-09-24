CREATE TABLE Cliente (
    idCliente INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(255),
    correo_electronico VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    idTienda INT
);

CREATE TABLE Vendedor (
    idEmpleado INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE Producto (
    idProducto INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    descripcion TEXT,
    stock INT DEFAULT 0,
    categoria VARCHAR(50)
);

CREATE TABLE Venta (
    idVenta INT PRIMARY KEY,
    fecha DATE NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    metodo_pago VARCHAR(50),
    idCliente INT,
    idEmpleado INT,
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
    FOREIGN KEY (idEmpleado) REFERENCES Vendedor(idEmpleado)
);

CREATE TABLE DetalleVenta (
    idDetalleVenta INT PRIMARY KEY,
    idVenta INT,
    idProducto INT,
    cantidad INT NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (idVenta) REFERENCES Venta(idVenta),
    FOREIGN KEY (idProducto) REFERENCES Producto(idProducto)
);

CREATE TABLE Inventario (
    idInventario INT PRIMARY KEY,
    idProducto INT,
    cantidad_total INT NOT NULL,
    tipo_producto VARCHAR(50),
    caracteristica_producto TEXT,
    FOREIGN KEY (idProducto) REFERENCES Producto(idProducto)
);

CREATE TABLE Envio (
    idEnvio INT PRIMARY KEY,
    direccionDestino VARCHAR(255),
    fechaEnvio DATE,
    fechaEntregaEstimada DATE,
    estadoEnvio VARCHAR(50),
    costoEnvio DECIMAL(10, 2),
    metodoEnvio VARCHAR(50),
    idVenta INT,
    FOREIGN KEY (idVenta) REFERENCES Venta(idVenta)
);

CREATE TABLE Factura (
    numero_factura INT PRIMARY KEY,
    fecha DATE NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    idVenta INT,
    cantidad_total INT NOT NULL, 
    FOREIGN KEY (idVenta) REFERENCES Venta(idVenta)
);
