package scenes;

import controller.MainController;
import database.Connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.CustomAlert;
import model.CartItem;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CartScene {

    private MainController mainController;
    private ObservableList<CartItem> cartItemList;
    private Label nameLbl, typeLbl, priceLbl, quantityLbl, subTotalLbl, grandTotalLbl;
    private TableView<CartItem> cartTable;

    public CartScene(MainController mainController) {
        this.mainController = mainController;
        this.cartItemList = FXCollections.observableArrayList();
        loadCartItemsFromDatabase();
    }

    public Pane getScene() {
        MenuBar menuBar = new MenuBar();
        Menu navMenu = new Menu("Page");

        MenuItem buyProductItem = new MenuItem("Buy Product");
        MenuItem logOutItem = new MenuItem("Log Out");

        navMenu.getItems().addAll(buyProductItem, logOutItem);
        menuBar.getMenus().add(navMenu);

        buyProductItem.setOnAction(e -> {
            mainController.getPageController().showBuyProductScene();
        });

        logOutItem.setOnAction(e -> {
            mainController.getPageController().showLoginPage();
        });

        Label titleLbl = new Label("Your Cart List");
        titleLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        cartTable = new TableView<>();
        cartTable.setItems(cartItemList);
        cartTable.setPrefHeight(300);
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<CartItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("flowerName")); 

        TableColumn<CartItem, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("flowerType")); 

        TableColumn<CartItem, Integer> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("flowerPrice")); 

        TableColumn<CartItem, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity")); 

        TableColumn<CartItem, Integer> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total")); 


        cartTable.getColumns().addAll(nameCol, typeCol, priceCol, quantityCol, totalCol);

        VBox productDetailsBox = new VBox(10);
        productDetailsBox.setAlignment(Pos.CENTER_LEFT);
        productDetailsBox.setPadding(new Insets(10));
        productDetailsBox.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        Label productDetailLbl = new Label("Product Detail:");
        productDetailLbl.setStyle("-fx-font-size: 18px;");

        nameLbl = new Label("Name: ");
        typeLbl = new Label("Type: ");
        priceLbl = new Label("Price: ");
        quantityLbl = new Label("Quantity: ");
        subTotalLbl = new Label("Sub Total: ");
        grandTotalLbl = new Label("Grand Total: ");

        productDetailsBox.getChildren().addAll(productDetailLbl, nameLbl, typeLbl, priceLbl, quantityLbl, subTotalLbl, grandTotalLbl);

        cartTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateProductDetails(newSelection);
            }
        });

        Button checkoutBtn = new Button("Checkout");
        checkoutBtn.setOnAction(e -> handleCheckout());

        HBox mainLayout = new HBox(20);
        VBox leftPanel = new VBox(10);
        leftPanel.setAlignment(Pos.TOP_LEFT);
        leftPanel.setPadding(new Insets(20));
        leftPanel.getChildren().addAll(titleLbl, cartTable);

        VBox rightPanel = new VBox(20);
        rightPanel.setAlignment(Pos.TOP_LEFT);
        rightPanel.setPadding(new Insets(20));
        rightPanel.getChildren().addAll(productDetailsBox, checkoutBtn);

        mainLayout.getChildren().addAll(leftPanel, rightPanel);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(mainLayout);

        return root;
    }

    private void updateProductDetails(CartItem cartItem) {
        nameLbl.setText("Name: " + cartItem.getFlowerName());
        typeLbl.setText("Type: " + cartItem.getFlowerType());
        priceLbl.setText("Price: $" + cartItem.getFlowerPrice());
        quantityLbl.setText("Quantity: " + cartItem.getQuantity());
        subTotalLbl.setText("Sub Total: $" + cartItem.getTotal());
        grandTotalLbl.setText("Grand Total: $" + calculateGrandTotal());
    }


    private int calculateGrandTotal() {
        return cartItemList.stream().mapToInt(CartItem::getTotal).sum();
    }

    private void loadCartItemsFromDatabase() {
        String query = "SELECT c.FlowerID, p.FlowerName, p.FlowerType, p.FlowerPrice, c.Quantity FROM mscart c JOIN msflower p ON c.FlowerID = p.FlowerID WHERE c.UserID = ?";
        try (Connection conn = Connect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, mainController.getUser().getUserId());
            ResultSet rs = stmt.executeQuery();

            cartItemList.clear(); 

            while (rs.next()) {
                String flowerID = rs.getString("FlowerID");
                String flowerName = rs.getString("FlowerName");
                String flowerType = rs.getString("FlowerType");
                int flowerPrice = rs.getInt("FlowerPrice");
                int quantity = rs.getInt("Quantity");

                Product product = new Product(flowerID, flowerName, flowerType, flowerPrice);
                CartItem cartItem = new CartItem(product, quantity);

                cartItemList.add(cartItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to load cart items from the database.");
        }
    }



    private void handleCheckout() {
        if (cartItemList.isEmpty()) {
            showError("Your cart is empty! Please add products before checking out.");
            return;
        }

        String transactionID = generateTransactionID();
        try (Connection conn = Connect.getInstance().getConnection()) {
            
     
            conn.setAutoCommit(false);

            String insertHeaderQuery = "INSERT INTO transactionheader (TransactionID, UserID) VALUES (?, ?)";
            try (PreparedStatement headerStmt = conn.prepareStatement(insertHeaderQuery)) {
                headerStmt.setString(1, transactionID);
                headerStmt.setString(2, mainController.getUser().getUserId());
                headerStmt.executeUpdate();
            }

            String insertDetailQuery = "INSERT INTO transactiondetail (TransactionID, FlowerID, Quantity) VALUES (?, ?, ?)";
            try (PreparedStatement detailStmt = conn.prepareStatement(insertDetailQuery)) {
                for (CartItem cartItem : cartItemList) {
                    detailStmt.setString(1, transactionID);
                    detailStmt.setString(2, cartItem.getProduct().getFlowerID());
                    detailStmt.setInt(3, cartItem.getQuantity());
                    detailStmt.executeUpdate();
                }
            }


            String clearCartQuery = "DELETE FROM mscart WHERE UserID = ?";
            try (PreparedStatement clearCartStmt = conn.prepareStatement(clearCartQuery)) {
                clearCartStmt.setString(1, mainController.getUser().getUserId());
                clearCartStmt.executeUpdate();
            }

            conn.commit();
            showConfirmation("Transaction successful! Your Transaction ID is " + transactionID);

            cartItemList.clear();
            loadCartItemsFromDatabase();

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to complete the transaction. Please try again.");

            try (Connection conn = Connect.getInstance().getConnection()) {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    private String generateTransactionID() {
        String query = "SELECT MAX(TransactionID) AS LastID FROM transactionheader";
        try (Connection conn = Connect.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastID = rs.getString("LastID");
                if (lastID != null) {
                    int nextID = Integer.parseInt(lastID.substring(2)) + 1;
                    return String.format("TR%03d", nextID);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "TR001"; // Default ID if no previous transactions exist
    }

    private void showError(String message) {
        CustomAlert errorAlert = new CustomAlert(Alert.AlertType.ERROR, "Error", "ERROR", message);
        errorAlert.showAlert();
    }

    private void showConfirmation(String message) {
        CustomAlert confirmAlert = new CustomAlert(Alert.AlertType.INFORMATION, "Success", "SUCCESS", message);
        confirmAlert.showAlert();
    }
}
