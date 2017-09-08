package application;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Controller {
	
	@FXML
	private TextField fixFileName;
	
	@FXML
	private TextField txtFileName;
	
	@FXML
	private Label lTxtLeidas, lTxtEscritas;
	
	
	private Model model = new Model();	
	
	@FXML
	private void buscaArchivos(){
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.setTitle("Busca Archivos TXT");
		fileChooser.setInitialDirectory(new File("..\\..\\SATI\\LeerSetrib"));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT Files", "*.txt"));
		//List<File> selectedFiles = fileChooser.showOpenMultipleDialog(savedStage);

		
		File file = fileChooser.showOpenDialog(null);
		if(file!=null){
			txtFileName.setText(file.getPath());		
			System.out.println(txtFileName.getText());
			int isDot = file.getPath().indexOf(".");
			if (isDot > 0){
				fixFileName.setText(file.getPath().substring(0, isDot) + "_FIX.txt");
			}
			else System.out.println("El Nombre del archivo no traía punto");
		}
		else{
			System.out.println("Nombre del Archivo NO es valido");
		}
	}
		
	@FXML
	private void convertir_txt2fix(ActionEvent event){
		//txtFile = ((Button)event.getSource()).getText();
		//System.out.println(txtFile);
		
		model.exec_conversion(txtFileName, fixFileName);
	}
	
	
	
	public void editarLabelLeidas(String valor){
		lTxtLeidas.setText(valor);
	}
	
	public void editarLabelEscritas(String valor){
		lTxtEscritas.setText(valor);
	}
	

}
