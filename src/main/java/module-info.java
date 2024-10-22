module my.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // Abre los paquetes a JavaFX para permitir la inyección de FXML
    opens Main to javafx.fxml, javafx.graphics; // Exportar el paquete Main a javafx.graphics
    opens Controladores to javafx.fxml;
    opens Controladores.Principal to javafx.fxml;
    opens Controladores.Sesion to javafx.fxml;
    opens Controladores.Cuenta to javafx.fxml;
    opens Controladores.Cuenta.Tienda to javafx.fxml;
    opens Controladores.Cuenta.Facturacion to javafx.fxml;
    opens Controladores.Cuenta.Perfil to javafx.fxml;
    opens Controladores.Cuenta.Compras to javafx.fxml;

    // Exporta los paquetes para permitir el acceso público
    exports Main;
    exports Controladores;
    exports Controladores.Principal;
    exports Controladores.Sesion;
    exports Controladores.Cuenta;
    exports Controladores.Cuenta.Tienda;
    exports Controladores.Cuenta.Facturacion;
    exports Controladores.Cuenta.Perfil;
    exports Controladores.Cuenta.Compras;
}


