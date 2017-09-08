package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppInit extends Application{

	public static Controller controller;
	
	public static void main(String[] args) {
		launch(args);
		
	}
	
	public Controller getController(){
		return AppInit.controller;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("IOFiles.fxml"));
		Parent root = loader.load();
		AppInit.controller = loader.getController();
		
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Convierte Archivo de Texto en Archivo de Mensajes FIX");
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}

}
