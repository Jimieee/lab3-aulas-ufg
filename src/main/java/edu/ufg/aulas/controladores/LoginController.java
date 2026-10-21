package edu.ufg.aulas.controladores;

import java.io.IOException;
import edu.ufg.aulas.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import edu.ufg.aulas.dao.UsuarioDAO;
import edu.ufg.aulas.entidades.Usuario;
import edu.ufg.aulas.util.SesionUsuario;
import edu.ufg.aulas.util.HashUtil;

public class LoginController {
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtClave;
    @FXML private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // valida las credenciales y conserva la sesion al abrir la bienvenida.
    @FXML
    private void ingresar() {
        String correo = txtCorreo.getText().trim();
        String clave = txtClave.getText();
        lblMensaje.setText("");

        if (correo.isEmpty() || clave.isEmpty()) {
            lblMensaje.setText("Complete todos los campos.");
            return;
        }

        Usuario usuario;
        try {
            usuario = usuarioDAO.login(correo, HashUtil.md5(clave));
        } catch (RuntimeException e) {
            lblMensaje.setText("No se pudo conectar con la base de datos.");
            e.printStackTrace();
            return;
        }

        if (usuario == null) {
            lblMensaje.setText("Correo o contraseña incorrectos.");
            txtClave.clear();
            return;
        }

        SesionUsuario.iniciar(usuario);
        try {
            App.setRoot("Home");
            txtClave.clear();
        } catch (IOException e) {
            SesionUsuario.cerrar();
            lblMensaje.setText("No se pudo abrir la pantalla de bienvenida.");
            e.printStackTrace();
        }
    }
}
