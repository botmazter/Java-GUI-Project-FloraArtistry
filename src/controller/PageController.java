package controller;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import scenes.BuyProductScene;
import scenes.CartScene;
import scenes.LoginScene;
import scenes.ManageProductScene;
import scenes.RegisterScene;

public class PageController {

	private Stage stage;
	private MainController mainController;

	public PageController(MainController mainController, Stage stage) {
		super();
		this.mainController = mainController;
		this.stage = stage;
	}
	
	public void showLoginPage() {
		LoginScene loginScene = new LoginScene(mainController);
		setPage(loginScene.getScene());
	}
	
	public void showRegisterPage() {
		RegisterScene registerScene = new RegisterScene(mainController);
		setPage(registerScene.getScene());
	}
	
	public void showBuyProductScene() {
		BuyProductScene buyProductScene = new BuyProductScene(mainController);
		setPage(buyProductScene.getScene());
	}
	
	public void showCartScene() {
		CartScene cartScene = new CartScene(mainController);
		setPage(cartScene.getScene());
	}
	
	public void showManageProductScene() {
	    ManageProductScene manageProductScene = new ManageProductScene(mainController);
	    setPage(manageProductScene.getManageProductScene()); 
	}
	
	void setPage(Pane pane) {
		Scene scene = new Scene(pane, 800, 600);
		stage.setScene(scene);
		stage.show();
	}
	
}
