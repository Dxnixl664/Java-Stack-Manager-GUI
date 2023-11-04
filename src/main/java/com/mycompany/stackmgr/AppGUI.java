package com.mycompany.stackmgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class AppGUI extends Application {
    
    private StrStack strStack = new StrStack();
    private ListView<String> listView;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Stack Manager");
        
        listView = new ListView<>();
        listView.getItems().addAll(strStack.getElements());
        
        Button btnAddElements = new Button("Add elements");
        btnAddElements.setOnAction(e -> addElements());
        
        Button btnAddElement = new Button("Add element");
        btnAddElement.setOnAction(e -> addElement());
        
        Button btnDeleteElement = new Button("Delete element");
        btnDeleteElement.setOnAction(e -> deleteElement());
        
        Button btnSearchElement = new Button("Search");
        btnSearchElement.setOnAction(e -> searchElement());
        
        VBox buttonsBox = new VBox(10);
        buttonsBox.getChildren().addAll(btnAddElements, btnAddElement, btnDeleteElement, btnSearchElement);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(listView);
        borderPane.setRight(buttonsBox);
        
        Scene scene = new Scene(borderPane, 300, 250);
        primaryStage.setScene(scene);
        
        primaryStage.show();
    }
    
    private void addElements() {
        try {
            int maxElements = 10 - strStack.getSize();
            if (maxElements <= 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("The stack is full");
                alert.setContentText("No more elements can be added.");
                alert.showAndWait();
                return;
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Input elements");
            dialog.setHeaderText("Input up to " + maxElements + " elements");

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(10,10,10,10));

            List<TextField> textFields = new ArrayList<>();
            for (int i = 0; i < maxElements; i++) {
                TextField textField = new TextField();
                textField.setPromptText("Element");
                Label label = new Label((i + 1) + ".");
                gridPane.add(label, i % 2 == 0 ? 0 : 2, i / 2);
                gridPane.add(textField, i % 2 == 0 ? 1 : 3, i / 2);
                textFields.add(textField);
            }

            ButtonType buttonTypeOk = new ButtonType("Add elements", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
            dialog.getDialogPane().setContent(gridPane);

            dialog.showAndWait().ifPresent(response -> {
                if (response == buttonTypeOk) {
                    boolean invalidInputFound = false;
                    for (TextField textField : textFields) {
                        String element = textField.getText().trim();
                        if (isValidElement(element)) {
                            strStack.addElement(element);
                            listView.getItems().add(element);
                        } else if (!element.isEmpty()) {
                            invalidInputFound = true;
                        }
                    }
                    if (invalidInputFound) {
                        showWarningAlert("Some elements are invalid", "Please, input valid elements (letters only, no blank spaces, no number or special characters).");
                    }
                }
            });
        } catch (Exception e) {
            showErrorAlert("An unexpected error has ocurred.");
        }
    }
    
    private void addElement() {
        try {
            if (strStack.getSize() >= 10) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("The stack is full");
                alert.setContentText("No more elements can be added");
                alert.showAndWait();
            } else {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add an element");
                dialog.setHeaderText("Input an element:");
                dialog.setContentText("Element:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(element -> {
                    String elementTrimmed = element.trim();
                    if (isValidElement(elementTrimmed)) {
                        strStack.addElement(elementTrimmed);
                        listView.getItems().add(elementTrimmed);
                    } else {
                        showWarningAlert("Invalid element", "Please, input valid elements (letters only, no blank spaces, no number or special characters).");
                    }
                });
            }
        } catch (Exception e) {
            showErrorAlert("An unexpected error has ocurred.");
        }
    }
    
    private void deleteElement() {
        try {
            if (strStack.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("The stack is empty");
                alert.setContentText("There are no elements to delete.");
                alert.showAndWait();
            } else {
                strStack.delElement();
                listView.getItems().remove(listView.getItems().size() - 1);
            }
        } catch (Exception e) {
            showErrorAlert("An unexpected error has ocurred.");
        }
    }
    
    private void searchElement() {
        List<String> choices = new ArrayList<>();
        choices.add("Search by element");
        choices.add("Search by position");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Search by element", choices);
        dialog.setTitle("Search element");
        dialog.setHeaderText("Choose a search option");
        dialog.setContentText("Select your option");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(choice -> {
            if ("Search by element".equals(choice)) {
                searchByElement();
            } else if ("Search by position".equals(choice)) {
                searchByPos();
            }
        });
    }
    
    private void searchByElement() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Search by element");
            dialog.setHeaderText("Input an element to search:");
            dialog.setContentText("Element:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(element -> {
                int position = strStack.search(element);
                if (position != -1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Search result");
                    alert.setHeaderText("Element found");
                    alert.setContentText("The element \"" + element + "\" is in position: " + position);
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Search result");
                    alert.setHeaderText("Element not found");
                    alert.setContentText("The element \"" + element + "\" is not in the stack.");
                    alert.showAndWait();
                }
            });
        } catch (Exception e) {
            showErrorAlert("An unexpected error has ocurred.");
        }
    }
    
    private void searchByPos() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Search by position");
            dialog.setHeaderText("Input the position to search:");
            dialog.setContentText("Position:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(positionStr -> {
                try {
                    int position = Integer.parseInt(positionStr);
                    String element = strStack.getByPos(position);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Search result");
                    alert.setHeaderText("Position found");
                    alert.setContentText("The element in position " + position + " is: " + element);
                    alert.showAndWait();
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid input");
                    alert.setContentText("Please, input a valid number.");
                    alert.showAndWait();
                } catch (IndexOutOfBoundsException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Search result");
                    alert.setHeaderText("Position not found");
                    alert.setContentText("The position is out of bounds.");
                    alert.showAndWait();
                }
            });
        } catch (Exception e) {
            showErrorAlert("An unexpected error has ocurred.");
        }
    }
    
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private boolean isValidElement(String element) {
    return !element.isEmpty() && element.matches("^[A-Za-z]+$");
    }
    
    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}