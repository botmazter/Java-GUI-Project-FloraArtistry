package scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.CustomAlert;
import model.Product;
import database.Connect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import controller.MainController;

public class ManageProductScene {

    private MainController mainController;
    private ObservableList<Product> flowerList;
    private TableView<Product> flowerTable;

    private TextField flowerNameField;
    private TextField flowerTypeField;
    private TextField flowerPriceField;

    public ManageProductScene(MainController mainController) {
        this.mainController = mainController;
        this.flowerList = FXCollections.observableArrayList();
    }

    public BorderPane getManageProductScene() {
        BorderPane layout = new BorderPane();
        
        MenuBar menuBar = new MenuBar();
        Menu navMenu = new Menu("Account");

        MenuItem logOutItem = new MenuItem("Log Out");
        
        menuBar.getMenus().add(navMenu);
        navMenu.getItems().addAll(logOutItem);
        
        logOutItem.setOnAction(e -> {
            mainController.getPageController().showLoginPage();
        });

        Label greetingLbl = new Label("Welcome, " + mainController.getUser().getUsername());
        Label flowerListTitle = new Label("Flower List");
        flowerListTitle.setFont(Font.font(30));
        VBox titleBox = new VBox(5, flowerListTitle, greetingLbl);

        flowerTable = new TableView<>();
        flowerTable.setItems(flowerList);
        flowerTable.setPrefHeight(300);
        flowerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        setupTableView();
        loadFlowerData();

        VBox centerBox = new VBox(10, titleBox, flowerTable);
        centerBox.setStyle("-fx-padding: 10;");
        
        GridPane flowerForm = setupFlowerForm();

        layout.setCenter(centerBox);
        layout.setRight(flowerForm);
        layout.setTop(menuBar);

        return layout;
    }

    private void setupTableView() {
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("flowerName"));

        TableColumn<Product, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Product, Integer> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        flowerTable.getColumns().addAll(nameCol, typeCol, priceCol);
        flowerTable.setItems(flowerList);

        flowerTable.setOnMouseClicked(event -> populateFlowerDetails());
    }

    private GridPane setupFlowerForm() {
        GridPane form = new GridPane();
        form.setVgap(10); 
        form.setHgap(10); 

        Label flowerDetailsTitle = new Label("Flower Details");
        flowerDetailsTitle.setFont(Font.font(18)); 
        form.add(flowerDetailsTitle, 0, 0, 2, 1); 

        form.add(new Label("Flower Name:"), 0, 1);
        flowerNameField = new TextField();
        form.add(flowerNameField, 1, 1);

        form.add(new Label("Flower Type:"), 0, 2);
        flowerTypeField = new TextField();
        form.add(flowerTypeField, 1, 2);

        form.add(new Label("Flower Price:"), 0, 3);
        flowerPriceField = new TextField();
        form.add(flowerPriceField, 1, 3);

        Button updateButton = new Button("Update Flower");
        Button deleteButton = new Button("Delete Flower");
        Button addButton = new Button("Add Flower");

        form.add(updateButton, 0, 4, 2, 1); 
        form.add(deleteButton, 0, 5, 2, 1);
        form.add(addButton, 0, 6, 2, 1);

        updateButton.setOnAction(e -> updateFlower());
        deleteButton.setOnAction(e -> deleteFlower());
        addButton.setOnAction(e -> addFlower());

        return form;
    }


    private void loadFlowerData() {
        flowerList.clear();
        String query = "SELECT * FROM msflower";
        try (ResultSet rs = Connect.getInstance().executeQuery(query)) {
            while (rs.next()) {
                flowerList.add(new Product(
                        rs.getString("FlowerID"),
                        rs.getString("FlowerName"),
                        rs.getString("FlowerType"),
                        rs.getInt("FlowerPrice")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateFlowerDetails() {
        Product selectedFlower = flowerTable.getSelectionModel().getSelectedItem();
        if (selectedFlower != null) {
            flowerNameField.setText(selectedFlower.getFlowerName());
            flowerTypeField.setText(selectedFlower.getType());
            flowerPriceField.setText(String.valueOf(selectedFlower.getPrice()));
        }
    }

    public void updateFlower() {
        Product selectedFlower = flowerTable.getSelectionModel().getSelectedItem();
        if (selectedFlower == null) {
            showError("Please select a flower to update.");
            return;
        }

        String name = flowerNameField.getText();
        String type = flowerTypeField.getText();
        String priceText = flowerPriceField.getText();

        if (name.isEmpty() || type.isEmpty() || priceText.isEmpty()) {
            showError("All fields must be filled.");
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceText);
            if (price <= 0) {
                showError("Flower price must be more than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Flower price must be a number.");
            return;
        }

        String updateQuery = "UPDATE msflower SET FlowerName = ?, FlowerType = ?, FlowerPrice = ? WHERE FlowerID = ?";
        try (PreparedStatement statement = Connect.getInstance().getConnection().prepareStatement(updateQuery)) {
            statement.setString(1, name);
            statement.setString(2, type);
            statement.setInt(3, price);
            statement.setString(4, selectedFlower.getFlowerID());
            statement.executeUpdate();
            showSuccess("Flower successfully updated!");
            loadFlowerData(); 
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to update flower.");
        }
    }


    public void deleteFlower() {
        Product selectedFlower = flowerTable.getSelectionModel().getSelectedItem();
        if (selectedFlower == null) {
            showError("Please select a flower to delete.");
            return;
        }

        String deleteQuery = "DELETE FROM msflower WHERE FlowerID = ?";
        try (PreparedStatement statement = Connect.getInstance().getConnection().prepareStatement(deleteQuery)) {
            statement.setString(1, selectedFlower.getFlowerID());
            statement.executeUpdate();
            showSuccess("Flower successfully deleted!");
            loadFlowerData(); 
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to delete flower.");
        }
    }

    public void addFlower() {
        String name = flowerNameField.getText();
        String type = flowerTypeField.getText();
        String priceText = flowerPriceField.getText();

        if (name.isEmpty() || type.isEmpty() || priceText.isEmpty()) {
            showError("All fields must be filled.");
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceText );
            if (price <= 0) {
                showError("Flower price must be more than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Flower price must be a number.");
            return;
        }

        String newFlowerID = generateNewFlowerID();
        String insertQuery = "INSERT INTO msflower (FlowerID, FlowerName, FlowerType, FlowerPrice) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = Connect.getInstance().getConnection().prepareStatement(insertQuery)) {
            statement.setString(1, newFlowerID);
            statement.setString(2, name);
            statement.setString(3, type);
            statement.setInt(4, price);
            statement.executeUpdate();
            showSuccess("Flower successfully added!");
            loadFlowerData();
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to add flower.");
        }
    }

    private String generateNewFlowerID() {
        String query = "SELECT FlowerID FROM msflower ORDER BY FlowerID DESC LIMIT 1";
        try (ResultSet rs = Connect.getInstance().executeQuery(query)) {
            if (rs.next()) {
                String lastID = rs.getString("FlowerID").substring(2);
                int newID = Integer.parseInt(lastID) + 1;
                return String.format("FL%03d", newID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "FL001";
    }

    
    private void showError(String message) {
  		CustomAlert errorAlert = new CustomAlert(Alert.AlertType.ERROR, "Registration Error", "ERROR", message);
  		errorAlert.showAlert();
  	}

  	private void showSuccess(String message) {
  		CustomAlert successAlert = new CustomAlert(Alert.AlertType.INFORMATION, "Registration Successful", "SUCCESSFULL", message);
  		successAlert.showAlert();
  	}

}
