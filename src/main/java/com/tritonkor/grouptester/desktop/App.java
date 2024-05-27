package com.tritonkor.grouptester.desktop;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import com.tritonkor.grouptester.desktop.presentation.util.SpringFXMLLoader;
import java.nio.file.Path;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App extends Application {

    public static AnnotationConfigApplicationContext springContext;

    @Override
    public void start(Stage primaryStage) throws Exception {

        var fxmlLoader = new SpringFXMLLoader(springContext);
        //var mainFxmlResource = App.class.getResource("view/main_teacher.fxml");
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

    public static void main(String[] args) {
        springContext = new AnnotationConfigApplicationContext(AppConfig.class);
        launch(args);
        springContext.close();
    }
}
