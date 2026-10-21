#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controladores;

import java.io.IOException;
import ${package}.App;
import ${package}.entidades.Usuario;
import ${package}.util.SesionUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HomeController {
    @FXML private Label lblBienvenida;
    @FXML private Label lblMensaje;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEdificio;
    @FXML private TextField txtCapacidad;
    @FXML private TextField txtResponsable;
    @FXML private ComboBox<?> cbPlanta;
    @FXML private ComboBox<?> cbTipoAula;
    @FXML private ComboBox<?> cbEstado;

    @FXML
    private void initialize() {
        Usuario usuario = SesionUsuario.getUsuarioActual();
        if (usuario != null) {
            lblBienvenida.setText("Bienvenido, " + usuario.getNombre() + " " + usuario.getApellido());
        }
    }

    @FXML
    private void guardar() {
        // Rene y Celis implementan el registro de aulas en la Fase B.
    }

    @FXML
    private void limpiar() {
        txtNombre.clear();
        txtEdificio.clear();
        txtCapacidad.clear();
        txtResponsable.clear();
        cbPlanta.getSelectionModel().clearSelection();
        cbTipoAula.getSelectionModel().clearSelection();
        cbEstado.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        txtNombre.requestFocus();
    }

    @FXML
    private void cerrarSesion() {
        try {
            App.setRoot("Login");
            SesionUsuario.cerrar();
        } catch (IOException e) {
            lblMensaje.setText("No se pudo abrir el inicio de sesión.");
            e.printStackTrace();
        }
    }
}
