package com.tritonkor.grouptester.desktop.presentation.viewmodel;

import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.UUID;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class QuestionViewModel {

    private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
    private final StringProperty text = new SimpleStringProperty();
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();
    private Path imagePath;

    public QuestionViewModel(UUID id, String text, Image image, Path imagePath) {
        this.id.set(id);
        this.text.set(text);
        this.image.set(image);
        this.imagePath = imagePath;
    }

    public UUID getId() {
        return id.get();
    }

    public ObjectProperty<UUID> idProperty() {
        return id;
    }

    public void setId(UUID id) {
        this.id.set(id);
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public Image getImage() {
        return image.get();
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    public void setImage(Image image) {
        this.image.set(image);
    }

    public Path getImagePath() {
        return imagePath;
    }

    public void setImagePath(Path imagePath) {
        this.imagePath = imagePath;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", QuestionViewModel.class.getSimpleName() + "[", "]")
            .add("id=" + id.get())
            .add("text=" + text.get())
            .add("image=" + image.get())
            .toString();
    }
}
