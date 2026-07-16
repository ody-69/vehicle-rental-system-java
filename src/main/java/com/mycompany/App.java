package com.mycompany;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.Optional;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class App extends Application {
    private RentalSystem system = new RentalSystem();
    private Stage mainStage;
    private String currentCustomerName = "Guest";

    private ObservableList<String> listItems = FXCollections.observableArrayList();
    private ObservableList<String> ownerListItems = FXCollections.observableArrayList();
    private ObservableList<String> pendingItems = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        this.mainStage = primaryStage;
        mainStage.setTitle("RENTIFY | Premium Vehicle Rental");
        showLoginScreen();
        mainStage.show();
    }

    //LOGIN SCREEN

    private void showLoginScreen() {

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f4f4f4;");
        
        ImageView logoView = null;
        try {
            Image image = new Image(getClass().getResourceAsStream("Logo.png"));
            logoView = new ImageView(image);
            
            logoView.setFitWidth(300);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Logo image not found!"); 
        }

        Label titleLabel = new Label("Welcome to Rental System");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        VBox customerBox = new VBox(10);
        customerBox.setAlignment(Pos.CENTER);
        customerBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        
        Label lblName = new Label("Customer Sign In");
        lblName.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.setMaxWidth(250);

        Button loginBtn = new Button("Start Renting");
        loginBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        loginBtn.setMinWidth(150);

        customerBox.getChildren().addAll(lblName, nameField, loginBtn);

        Hyperlink ownerLink = new Hyperlink("Owner Login");
        ownerLink.setTextFill(Color.DARKGRAY);
        ownerLink.setFont(Font.font(12));

        loginBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showAlert("Error", "Please enter your name.");
            } else {
                currentCustomerName = name;
                showCustomerPanel();
            }
        });

        ownerLink.setOnAction(e -> showOwnerLogin());
        
        if (logoView != null) {
            root.getChildren().addAll(logoView, customerBox, new Separator(), ownerLink);
        } else {
            Label fallbackTitle = new Label("RENTIFY");
            fallbackTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            root.getChildren().addAll(fallbackTitle, customerBox, new Separator(), ownerLink);
        }

        mainStage.setScene(new Scene(root, 400, 450));
    }

    private void showOwnerLogin() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Owner Authentication");
        dialog.setHeaderText("Please enter Owner credentials");

        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("ID");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Owner ID:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == loginButtonType) {
            if (username.getText().equals("owner") && password.getText().equals("Mireille")) {
                showOwnerPanel();
            } else {
                showAlert("Access Denied", "Invalid Info!");
            }
        }
    }

    //CUSTOMER PANEL
    
    private void showCustomerPanel() {
        VBox customerPanel = new VBox(10);
        customerPanel.setPadding(new Insets(15));
        
        ListView<String> vehicleListView = new ListView<>(listItems);
        refreshAllLists();

        Label header = new Label("Hello, " + currentCustomerName + "!");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        Button requestButton = new Button("Request Vehicle");
        Button returnButton = new Button("Return Vehicle");
        Button sortButton = new Button("Sort Price");
        Button logoutButton = new Button("Log Out");

        HBox actions = new HBox(10, requestButton, returnButton, sortButton, logoutButton);

        customerPanel.getChildren().addAll(header, new Label("Available Inventory:"), vehicleListView, actions);

        requestButton.setOnAction(e -> {
            Vehicle selected = getSelectedVehicle(vehicleListView.getSelectionModel().getSelectedIndex());
            if (selected == null) {
                showAlert("Error", "Select a vehicle.");
            } else if (!selected.isAvailable()) {
                showAlert("Unavailable", "This vehicle is not available.");
            } else {
                system.requestVehicle(selected);
                refreshAllLists();
                showAlert("Success", "Request sent for " + selected.getModel());
            }
        });

        returnButton.setOnAction(e -> {
            Vehicle selected = getSelectedVehicle(vehicleListView.getSelectionModel().getSelectedIndex());
            if (selected == null) {
                showAlert("Error", "Select a vehicle.");
            } else if (selected.isAvailable()) {
                showAlert("Error", "You can't return a vehicle you haven't rented.");
            } else {
                selected.returnVehicle();
                system.saveData();
                refreshAllLists();
                showAlert("Success", "Vehicle returned.");
            }
        });
        
        sortButton.setOnAction(e -> { system.sortInventory(); refreshAllLists(); });

        logoutButton.setOnAction(e -> showLoginScreen());

        mainStage.setScene(new Scene(customerPanel, 600, 500));
    }

    //OWNER PANEL
    
    private void showOwnerPanel() {
        VBox ownerPanel = new VBox(10);
        ownerPanel.setPadding(new Insets(15));

        // -- INPUTS --
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Car", "Bike", "Van");
        typeBox.setValue("Car"); // Default

        TextField idField = new TextField(); idField.setPromptText("ID");
        TextField modelField = new TextField(); modelField.setPromptText("Model");
        TextField rateField = new TextField(); rateField.setPromptText("Rate");
        
        // DYNAMIC FIELD: Text Input for Seats
        TextField seatsField = new TextField(); 
        seatsField.setPromptText("Seats (Max 7)"); // Default for Car

        // DYNAMIC FIELD: Checkbox for Bike Helmet
        CheckBox helmetBox = new CheckBox("Includes Helmet?");
        helmetBox.setVisible(false); // Hidden by default
        helmetBox.setManaged(false); // Does not take space by default

        // CHANGE LOGIC: Switch between Text Input and Checkbox
        typeBox.setOnAction(e -> {
            String type = typeBox.getValue();
            if (type.equals("Car")) {
                seatsField.setVisible(true); seatsField.setManaged(true);
                helmetBox.setVisible(false); helmetBox.setManaged(false);
                seatsField.setPromptText("Seats (Max 7)");
                seatsField.clear();
            } else if (type.equals("Van")) {
                seatsField.setVisible(true); seatsField.setManaged(true);
                helmetBox.setVisible(false); helmetBox.setManaged(false);
                seatsField.setPromptText("Seats (7-14)");
                seatsField.clear();
            } else if (type.equals("Bike")) {
                seatsField.setVisible(false); seatsField.setManaged(false);
                helmetBox.setVisible(true); helmetBox.setManaged(true);
                helmetBox.setSelected(false);
            }
        });

        HBox inputBox = new HBox(10, typeBox, idField, modelField, rateField, seatsField, helmetBox);
        inputBox.setAlignment(Pos.CENTER_LEFT);
        
        // -- BUTTONS --
        Button addButton = new Button("Add Vehicle");
        Button deleteButton = new Button("Delete");
        Button sortButton = new Button("Sort Price");
        Button approveButton = new Button("Approve Request");
        Button logoutButton = new Button("Log Out");

        ListView<String> inventoryList = new ListView<>(ownerListItems);
        ListView<String> pendingList = new ListView<>(pendingItems);
        pendingList.setPrefHeight(150);

        ownerPanel.getChildren().addAll(
            new Label("Manage Inventory"), inputBox, 
            new HBox(10, addButton, deleteButton, sortButton),
            new Label("Current Vehicles"), inventoryList,
            new Label("Pending Requests"), pendingList,
            new HBox(10, approveButton, logoutButton)
        );

        refreshAllLists();

        // ADD BUTTON LOGIC
        addButton.setOnAction((var e) -> {
            String id = idField.getText();
            String model = modelField.getText();
            String type = typeBox.getValue();
            String rateText = rateField.getText();

            // Basic Validation
            if (id.isEmpty() || model.isEmpty() || rateText.isEmpty()) {
                showAlert("Error", "ID, Model, and Rate are required."); return;
            }
            if (system.findVehicleById(id) != null) {
                showAlert("Error", "ID exists."); return;
            }
            if (Double.parseDouble(rateText) < 1) {
                showAlert("Error", "Rate must be a positive number."); return;
            }

            try {
                double rate = Double.parseDouble(rateText);
                Vehicle v = null;

                // LOGIC FOR CAR (Limit 7)
                if (type.equals("Car")) {
                    if(seatsField.getText().isEmpty()) { showAlert("Error", "Enter number of seats."); return; }
                    int seats = Integer.parseInt(seatsField.getText());
                    
                    if (seats < 2 || seats > 7) { 
                        showAlert("Limit Error", "Cars cannot have less than 2 seats or more than 7 seats."); return; 
                    }
                    v = new Car(id, model, rate, seats);
                } 
                // LOGIC FOR VAN (Limit 7 to 14)
                else if (type.equals("Van")) {
                    if(seatsField.getText().isEmpty()) { showAlert("Error", "Enter number of seats."); return; }
                    int seats = Integer.parseInt(seatsField.getText());
                    
                    if (seats < 7 || seats > 14) { 
                        showAlert("Limit Error", "Vans must have between 7 and 14 seats."); return; 
                    }
                    v = new Van(id, model, rate, seats);
                } 
                // LOGIC FOR BIKE (CheckBox)
                else if (type.equals("Bike")) {
                    boolean hasHelmet = helmetBox.isSelected();
                    v = new Bike(id, model, rate, hasHelmet);
                }

                system.addVehicle(v);
                refreshAllLists();
                idField.clear(); modelField.clear(); rateField.clear(); seatsField.clear(); helmetBox.setSelected(false);
                
            } catch (NumberFormatException ex) {
                showAlert("Error", "Rate/Seats must be valid numbers.");
            }
        });

        deleteButton.setOnAction(e -> {
            int idx = inventoryList.getSelectionModel().getSelectedIndex();
            if(idx >= 0) { system.removeVehicle(system.getInventory().get(idx)); refreshAllLists(); }
        });

        sortButton.setOnAction(e -> { system.sortInventory(); refreshAllLists(); });

        approveButton.setOnAction(e -> {
            int idx = pendingList.getSelectionModel().getSelectedIndex();
            if(idx >= 0) { system.approveRequest(system.getPendingRequests().get(idx)); refreshAllLists(); }
        });

        logoutButton.setOnAction(e -> showLoginScreen());

        mainStage.setScene(new Scene(ownerPanel, 800, 700));
    }

    
    
    //Some Useful Methods 
    
    private void refreshAllLists() {
        listItems.clear();
        ownerListItems.clear();
        pendingItems.clear();

        for (Vehicle v : system.getInventory()) {
            String status = v.isRequested() ? "[Requested]" : (v.isAvailable() ? "[Available]" : "[RENTED]");
            String txt = v.getDetails() + "   " + status;
            listItems.add(txt);
            ownerListItems.add(txt);
        }

        for (Vehicle v : system.getPendingRequests()) {
            pendingItems.add(v.getDetails());
        }
    }

    private Vehicle getSelectedVehicle(int index) {
        if (index >= 0 && index < system.getInventory().size()) {
            return system.getInventory().get(index);
        }
        return null;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}