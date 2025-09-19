package controller;

import javafx.stage.Stage;
import model.User;

public class MainController {
	private PageController pageController;
	private RegisterController registerController;
	private LoginController loginController;
	private CartController cartController;
	private User user;
	
	public MainController(Stage stage) {
		super();
		this.pageController = new PageController(this, stage);
		this.registerController = new RegisterController(this);
		this.loginController = new LoginController(this);
		this.cartController = new CartController(this);
		this.user = null;
	}

	public PageController getPageController() {
		return pageController;
	}

	public RegisterController getRegisterController() {
		return registerController;
	}

	public LoginController getLoginController() {
		return loginController;
	}
	
	public CartController getCartController() {
	    return cartController;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
