package edu.ufg.aulas.controladores;

import java.io.IOException;
import edu.ufg.aulas.App;
import edu.ufg.aulas.dao.AulaDAO;
import edu.ufg.aulas.entidades.Aula;
import edu.ufg.aulas.entidades.EstadoAula;
import edu.ufg.aulas.entidades.Planta;
import edu.ufg.aulas.entidades.TipoAula;
import edu.ufg.aulas.entidades.Usuario;
import edu.ufg.aulas.util.Alertas;
import edu.ufg.aulas.util.SesionUsuario;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
    @FXML private ComboBox<Planta> cbPlanta;
    @FXML private ComboBox<TipoAula> cbTipoAula;
    @FXML private ComboBox<EstadoAula> cbEstado;

    private final AulaDAO aulaDAO = new AulaDAO();

    @FXML
    private void initialize() {
        Usuario usuario = SesionUsuario.getUsuarioActual();
        if (usuario != null) {
            lblBienvenida.setText("Bienvenido, " + usuario.getNombre() + " " + usuario.getApellido());
        }
        cargarCombos();
    }

    // Los tres combos salen de las tablas de catálogo, no de valores fijos.
    private void cargarCombos() {
        try {
            cbPlanta.setItems(FXCollections.observableArrayList(aulaDAO.listarPlantas()));
            cbTipoAula.setItems(FXCollections.observableArrayList(aulaDAO.listarTipos()));
            cbEstado.setItems(FXCollections.observableArrayList(aulaDAO.listarEstados()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            // initialize() corre mientras se carga la vista, así que la alerta espera a que termine.
            Platform.runLater(() -> Alertas.error("Error de conexión",
                    "No se pudieron cargar las plantas, tipos y estados desde la base de datos."));
        }
    }

    @FXML
    private void guardar() {
        lblMensaje.setText("");

        String nombre = txtNombre.getText().trim();
        String edificio = txtEdificio.getText().trim();
        String capacidadTexto = txtCapacidad.getText().trim();
        String responsable = txtResponsable.getText().trim();
        Planta planta = cbPlanta.getValue();
        TipoAula tipoAula = cbTipoAula.getValue();
        EstadoAula estado = cbEstado.getValue();

        if (nombre.isEmpty() || edificio.isEmpty() || capacidadTexto.isEmpty() || responsable.isEmpty()
                || planta == null || tipoAula == null || estado == null) {
            Alertas.advertencia("Campos incompletos", "Complete todos los campos antes de guardar.");
            return;
        }

        int capacidad;
        try {
            capacidad = Integer.parseInt(capacidadTexto);
        } catch (NumberFormatException e) {
            Alertas.advertencia("Capacidad no válida", "La capacidad debe ser un número entero.");
            return;
        }
        if (capacidad <= 0) {
            Alertas.advertencia("Capacidad no válida", "La capacidad debe ser mayor que 0.");
            return;
        }
        if (planta.getNumero() == null || planta.getNumero() <= 0) {
            Alertas.advertencia("Planta no válida", "La planta debe ser un número entero mayor que 0.");
            return;
        }

        try {
            if (aulaDAO.existeAula(nombre, edificio)) {
                Alertas.advertencia("Aula duplicada",
                        "Ya existe un aula llamada " + nombre + " en " + edificio + ".");
                return;
            }

            Aula aula = new Aula();
            aula.setNombre(nombre);
            aula.setEdificio(edificio);
            aula.setPlanta(planta);
            aula.setCapacidad(capacidad);
            aula.setTipoAula(tipoAula);
            aula.setResponsable(responsable);
            aula.setEstado(estado);
            aulaDAO.guardar(aula);
        } catch (RuntimeException e) {
            e.printStackTrace();
            Alertas.error("No se pudo guardar", "Ocurrió un error al guardar el aula en la base de datos.");
            return;
        }

        limpiar();
        lblMensaje.setStyle("-fx-text-fill: #2e7d32;");
        lblMensaje.setText("Aula guardada correctamente.");
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
        lblMensaje.setStyle("");
        lblMensaje.setText("");
        txtNombre.requestFocus();
    }

    @FXML
    private void cerrarSesion() {
        try {
            App.setRoot("Login");
            SesionUsuario.cerrar();
        } catch (IOException e) {
            e.printStackTrace();
            Alertas.error("Error", "No se pudo abrir el inicio de sesión.");
        }
    }
}
