package edu.ufg.aulas.controladores;

import java.io.IOException;
import edu.ufg.aulas.App;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import edu.ufg.aulas.dao.UsuarioDAO;
import edu.ufg.aulas.entidades.Usuario;
import edu.ufg.aulas.util.Alertas;
import edu.ufg.aulas.util.SesionUsuario;
import edu.ufg.aulas.util.HashUtil;

public class LoginController {
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtClave;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // valida las credenciales y conserva la sesion al abrir la bienvenida.
    @FXML
    private void ingresar() {
        String correo = txtCorreo.getText().trim();
        String clave = txtClave.getText();

        if (correo.isEmpty() || clave.isEmpty()) {
            Alertas.advertencia("Campos incompletos", "Ingrese su correo y su contraseña.");
            return;
        }

        Usuario usuario;
        try {
            usuario = usuarioDAO.login(correo, HashUtil.md5(clave));
        } catch (RuntimeException e) {
            e.printStackTrace();
            Alertas.error("Error de conexión", "No se pudo conectar con la base de datos.");
            return;
        }

        if (usuario == null) {
            Alertas.error("Acceso denegado", "Correo o contraseña incorrectos.");
            txtClave.clear();
            return;
        }

        SesionUsuario.iniciar(usuario);
        try {
            App.setRoot("Home");
            txtClave.clear();
        } catch (IOException e) {
            SesionUsuario.cerrar();
            e.printStackTrace();
            Alertas.error("Error", "No se pudo abrir la pantalla de bienvenida.");
        }
    }
}
