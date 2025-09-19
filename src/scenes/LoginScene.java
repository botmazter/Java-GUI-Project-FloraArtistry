package scenes;

import controller.MainController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LoginScene {
	
	 private MainController mainController;
	
	 public LoginScene(MainController mainController) {
		super();
		this.mainController = mainController;
	}

	public Pane getScene() {
		
		MenuBar menuBar = new MenuBar();
        Menu navigationMenu = new Menu("Page");

        MenuItem loginItem = new MenuItem("Login Page");
        MenuItem registerItem = new MenuItem("Register Page");
        
        menuBar.getMenus().add(navigationMenu);
        navigationMenu.getItems().addAll(loginItem, registerItem);
        
        loginItem.setOnAction(e -> {
            
        });
        
        registerItem.setOnAction(e -> {
            mainController.getPageController().showRegisterPage();
            
        });
        
        Text title = new Text("Login");
        title.setFont(Font.font("Arial", 30));
        
        Label emailLbl = new Label("Email");
        TextField emailTF = new TextField();
        emailTF.setMaxWidth(250);
        
        Label passwordLbl = new Label("Password");
        PasswordField passwordTF = new PasswordField();
        passwordTF.setMaxWidth(250);
        
        Button loginBtn = new Button("Login");
        loginBtn.setMaxWidth(250);
        
        loginBtn.setOnAction(e -> {
        	String email = emailTF.getText();
        	String password = passwordTF.getText();
        	mainController.getLoginController().login(email, password);
        });
        
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        
        VBox inputLayout = new VBox(10);
        inputLayout.getChildren().addAll(emailLbl, emailTF, passwordLbl, passwordTF, loginBtn);
        inputLayout.setAlignment(Pos.CENTER);
        
        VBox mainLayout = new VBox(20);
        mainLayout.getChildren().addAll(title, inputLayout);
        mainLayout.setAlignment(Pos.CENTER);
        
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(mainLayout);
        
        return root;

	}

}
