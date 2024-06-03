package com.tritonkor.grouptester.desktop;

import com.tritonkor.grouptester.desktop.presentation.util.SpringFXMLLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * The main class for the GroupTester desktop application.
 */
public class App extends Application {

    public static AnnotationConfigApplicationContext springContext;

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage The primary stage for the application.
     * @throws Exception if an error occurs during application startup.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        var fxmlLoader = new SpringFXMLLoader(springContext);
        var mainFxmlResource = App.class.getResource("view/authorize/authenticate.fxml");
        BorderPane parent = (BorderPane) fxmlLoader.load(mainFxmlResource);

        ImageView backgroundImage = (ImageView) parent.lookup("#backgroundImage");

        backgroundImage.fitWidthProperty().bind(parent.widthProperty());
        backgroundImage.fitHeightProperty().bind(parent.heightProperty());

        Scene scene = new Scene(parent, 1700, 1000);
        scene.getStylesheets().add(getClass().getResource("view/css/styles.css").toExternalForm());
        primaryStage.setTitle("GroupTester");
        primaryStage.setScene(scene);

        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);

        primaryStage.show();

    }

    /**
     * The entry point of the application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        springContext = new AnnotationConfigApplicationContext(AppConfig.class);
        launch(args);
        springContext.close();
    }
}
