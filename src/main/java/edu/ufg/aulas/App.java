package edu.ufg.aulas;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.ufg.aulas.util.HibernateUtil;
import edu.ufg.aulas.util.SesionUsuario;

public class App extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Login"), 640, 620);
        stage.setTitle("UFG · Registro de aulas");
        stage.setMinWidth(600);
        stage.setMinHeight(580);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // Las vistas comparten una ruta independiente del paquete generado por Maven.
    private static Parent loadFXML(String fxml) throws IOException {
        URL recurso = App.class.getResource("/vistas/" + fxml + ".fxml");
        if (recurso == null) {
            throw new IOException("No se encontró la vista " + fxml + ".fxml");
        }
        return new FXMLLoader(recurso).load();
    }

    @Override
    public void stop() {
        SesionUsuario.cerrar();
        HibernateUtil.cerrar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
