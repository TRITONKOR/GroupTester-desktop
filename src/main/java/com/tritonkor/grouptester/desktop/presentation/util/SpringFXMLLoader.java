package com.tritonkor.grouptester.desktop.presentation.util;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import org.springframework.context.ApplicationContext;

/**
 * A utility class for loading FXML files using Spring's ApplicationContext to manage controller dependencies.
 */
public class SpringFXMLLoader {
    private final ApplicationContext context;

    /**
     * Constructs a new SpringFXMLLoader with the given ApplicationContext.
     *
     * @param context The Spring ApplicationContext to use for loading FXML files.
     */
    public SpringFXMLLoader(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Loads the FXML file from the specified URL, injecting Spring-managed controllers.
     *
     * @param url The URL of the FXML file to load.
     * @return The root object of the loaded FXML file.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public Object load(URL url) throws IOException {
        FXMLLoader loader = new FXMLLoader(url);
        loader.setControllerFactory(context::getBean);
        return loader.load();
    }
}