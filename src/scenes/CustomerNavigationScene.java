package scenes;

import controller.MainController;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class CustomerNavigationScene {

    private MainController mainController;

    public CustomerNavigationScene(MainController mainController) {
        this.mainController = mainController;
    }

    public Pane getScene() {
    	
    	MenuBar menuBar = new MenuBar();
    	Menu navMenu = new Menu("Page");
    	
    	MenuItem buyFlowerItem = new Menu("Buy Flower");
    	MenuItem cartItem = new Menu("Cart");
    	MenuItem logOutItem = new Menu("Log Out");
    	
    	menuBar.getMenus().add(navMenu);
    	navMenu.getItems().addAll(buyFlowerItem, cartItem, logOutItem);
    	
    	buyFlowerItem.setOnAction(e -> {
    		mainController.getPageController().showBuyProductScene();
    	});
    	
    	cartItem.setOnAction(e -> {
   		
    		mainController.getPageController().showCartScene();
    	});
    	
    	logOutItem.setOnAction(e -> {
    		mainController.getPageController().showLoginPage();
    	});
    	
    	BorderPane root = new BorderPane();
    	root.setTop(menuBar);
    	
		return root;
    
    }
}
