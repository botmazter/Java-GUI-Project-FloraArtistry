package scenes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import controller.MainController;
import database.Connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import main.CustomAlert;
import model.Product;

public class BuyProductScene {

    private MainController mainController;
    private ObservableList<Product> productList;
    private Label flowerNameLbl, flowerTypeLbl, flowerPriceLbl;
    private TableView<Product> productTable;

    public BuyProductScene(MainController mainController) {
        this.mainController = mainController;
        this.productList = FXCollections.observableArrayList();
        loadProductsFromDatabase();
    }

    public Pane getScene() {
        MenuBar menuBar = new MenuBar();
        Menu navMenu = new Menu("Page");

        MenuItem cartItem = new MenuItem("Cart");
        MenuItem logOutItem = new MenuItem("Log Out");

        menuBar.getMenus().add(navMenu);
        navMenu.getItems().addAll(cartItem, logOutItem);

        cartItem.setOnAction(e -> {
        	mainController.getPageController().showCartScene();
        });

        logOutItem.setOnAction(e -> {
            mainController.getPageController().showLoginPage();
        });

        Label greetingLbl = new Label("Welcome, " + mainController.getUser().getUsername());

        Label title= new Label("Product List");
        title.setFont(Font.font("Arial", 30));

        productTable = new TableView<>();
        productTable.setItems(productList);
        productTable.setPrefHeight(400);
        productTable.setPrefWidth(500);
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("flowerName"));

        TableColumn<Product, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Product, Integer> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.getColumns().addAll(nameCol, typeCol, priceCol);

        VBox productDetailsBox = new VBox(10);
        productDetailsBox.setAlignment(Pos.CENTER_LEFT);
        productDetailsBox.setPadding(new Insets(10));

        Label flowerDetailLbl = new Label("Flower Detail");
        flowerDetailLbl.setFont(Font.font("Arial", 20));
        flowerNameLbl = new Label("Flower Name: ");
        flowerTypeLbl = new Label("Flower Type: ");
        flowerPriceLbl = new Label("Flower Price: ");
        productDetailsBox.getChildren().addAll(flowerDetailLbl, flowerNameLbl, flowerTypeLbl, flowerPriceLbl);

        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct == null) {
                showError("Please select a product before adding to cart.");
            } else {
                AddtoCartScene addToCartScene = new AddtoCartScene(mainController, selectedProduct);
                addToCartScene.showAddtoCartScene();
            }
        });
        
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateProductDetails(newSelection);
            }
        });

        HBox mainLayout = new HBox(20);
        VBox leftPanel = new VBox(10);
        leftPanel.setAlignment(Pos.CENTER_LEFT);
        leftPanel.setPadding(new Insets(20));
        leftPanel.getChildren().addAll(title, greetingLbl, productTable);

        VBox rightPanel = new VBox(20);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(20));
        rightPanel.getChildren().addAll(productDetailsBox, addToCartBtn);

        mainLayout.getChildren().addAll(leftPanel, rightPanel);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(mainLayout);

        return root;
    }

    private void updateProductDetails(Product product) {
        flowerNameLbl.setText("Flower Name: " + product.getFlowerName());
        flowerTypeLbl.setText("Flower Type: " + product.getType());
        flowerPriceLbl.setText("Flower Price: $" + product.getPrice());
    }

    private void showError(String message) {
        CustomAlert errorAlert = new CustomAlert(Alert.AlertType.ERROR, "Add to Cart Failed!", "ERROR", message);
        errorAlert.showAlert();
    }
    
    private void loadProductsFromDatabase() {
        String query = "SELECT FlowerID, FlowerName, FlowerType, FlowerPrice FROM msflower";
        try (Connection conn = Connect.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = rs.getString("FlowerID");
                String name = rs.getString("FlowerName");
                String type = rs.getString("FlowerType");
                int price = rs.getInt("FlowerPrice");

                Product product = new Product(id, name, type, price);
                productList.add(product);  
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to load products from the database.");
        }
    }
    
}
