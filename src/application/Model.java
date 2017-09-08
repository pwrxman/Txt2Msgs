package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

public class Model {
	
	private int cuentaP, cuentaO, cuentaOtros;
	//private MySQLConnection mySQLConnection;
	
	private TxtFixDB txtFixDB;
	private Connection connection = null;
	private Statement sqlStament = null;
	private ResultSet resultSet = null;
	
	public void exec_conversion(TextField fileTxt, TextField fileFix){
		int nLineasLeidas, nLineasEscritas;

		// El Archivo de Entrada en formato de text
		File fileNameTxt = new File(fileTxt.getText());
		FileReader frTxt = null;
		BufferedReader bRdrTxt = null;
		String lineaLeida = "";
		
		//El Archivo de salida para mansajes FIX
		File fileNameFIX = new File(fileFix.getText());
		FileWriter fwFileFIX = null;
		PrintWriter pwWrtrFIX = null;
		
		
		try {
			
			//Archivo de lectura
			frTxt = new FileReader(fileNameTxt);
			bRdrTxt = new BufferedReader(frTxt);
			
			//Archivo de escritura
			fwFileFIX = new FileWriter(fileNameFIX);
	        pwWrtrFIX = new PrintWriter(fwFileFIX);
			
			nLineasEscritas = 0;
			nLineasLeidas = 0;
			String lineaEscrita = "";
			
			txtFixDB = new TxtFixDB();
			connection = txtFixDB.getConection();
			Controller control = AppInit.controller;
			
			while ((lineaLeida = bRdrTxt.readLine()) != null) {
				++nLineasLeidas;
				
				System.out.println("Lineas TXT Leidas: " + nLineasLeidas );
				control.editarLabelEscritas(nLineasLeidas + "");

				System.out.println("Lineas TXT Leidas: " + nLineasLeidas );
				
				System.out.println(lineaLeida);
				if(!(lineaEscrita = obtieneFIX(lineaLeida, nLineasLeidas)).equals("")){
					pwWrtrFIX.println(lineaEscrita);
					System.out.println(lineaEscrita);
					++nLineasEscritas;
				}				
			}
			connection.close();
			
			System.out.println("Lineas TXT Leidas: " + nLineasLeidas + "  ->  " + "Lineas FIX Escritas: " + nLineasEscritas);
			System.out.println("Numero de mensajes:   (P) -> " + cuentaP + "     (O) -> " + cuentaO + "     (Otros) -> " + cuentaOtros);
			
		} catch (Exception e) {
			//textArea.appendText(e.toString());
			System.out.println("Catch 1 Error: " + e.getMessage());
			e.printStackTrace();
		} 
		finally {
			try {
				frTxt.close();
				fwFileFIX.close();
				
			} catch (Exception e2) {
				System.out.println("Catch 2 Error: " + e2.getMessage());
			}
		}
		
		return;
		
	}

	private String obtieneFIX(String vLineaLeida, int vLineasLeidas) {
		String vLineaFIX = "";
		String symbol = "", idOrder = "", volumen = "", precioCompra = "", precioVenta = "", ISIN = "", expirationDate = "", hora_mensaje = "";
		vLineaLeida = vLineaLeida.substring(28);
		//String Fix_compra = "8=FIX.4.235=D49=CIOMS56=F118=H21=122=411=" + (l++) + "38=" + volumen.toString() + 
		//"40=244=" + precio.toString() + "48=" + ISIN + "54=1432=" + amd + "126=" + hora_mensaje + "20001=059=0";
		switch (vLineaLeida.substring(0, 1)){
			case "P":
				++cuentaP;
				symbol = vLineaLeida.substring(15, 23).trim();
			idOrder = vLineasLeidas + "";
				volumen = vLineaLeida.substring(28, 39);
				precioCompra = vLineaLeida.substring(39, 45) + "." + vLineaLeida.substring(45, 51);
				ISIN = consultaISIN(symbol);
				expirationDate = vLineaLeida.substring(127, 135);
				hora_mensaje = vLineaLeida.substring(223, 235);
				vLineaFIX = "8=FIX.4.235=D49=VENDOR99999=P156=F118=H21=122=411=" + idOrder + "38=" + volumen + 
				"40=244=" + precioCompra + "48=" + ISIN + "54=1432=" + expirationDate + "126=" + hora_mensaje + "20001=059=0";
				break;
				
			case "O":
				++cuentaO;
				symbol = vLineaLeida.substring(7, 14).trim();
			idOrder = vLineasLeidas + "";     //No viene definido en el Layout del archivo, est5e valor es una suposicion
				volumen = vLineaLeida.substring(29, 40);     //No viene definido en el Layout del archivo, estos valores son una suposicion
				precioCompra = vLineaLeida.substring(40, 46) + "." + vLineaLeida.substring(46, 48);
				precioVenta = vLineaLeida.substring(63, 69) + "." + vLineaLeida.substring(69, 71);
				ISIN = consultaISIN(symbol);
				expirationDate = vLineaLeida.substring(127, 135);     //No viene definido en el Layout del archivo
				hora_mensaje = vLineaLeida.substring(223, 235);
				vLineaFIX = "8=FIX.4.235=D49=VENDOR99999=O 56=F118=H21=122=411=" + idOrder + "38=" + volumen + 
						"40=244=" + precioCompra + "48=" + ISIN + "54=1432=" + expirationDate + "126=" + hora_mensaje + "20001=059=0";
				
				break;
				
			default:
				++cuentaOtros;
				System.out.println("Tipo de Mensaje: (" + vLineaLeida.substring(0, 1) + ")" + " distinto a P u O");
				vLineaFIX = "";
		}
            
		return vLineaFIX;
		
	}

	private String consultaISIN(String vEmisora) {
		String isin = "";
		String sql = "SELECT ISIN from SATI.Instrumentos WHERE Emision = '" + vEmisora + "'";
				
        try {
        	sqlStament = connection.createStatement(); 
        	ResultSet resultSet = sqlStament.executeQuery(sql);
            if(resultSet.next()){
                isin = resultSet.getString("ISIN");
            }else{
           	    isin = "ISIN DESCONOCIDO";;
            }
            resultSet.close();
			sqlStament.close();
        } catch (SQLException e) {
            e.printStackTrace();
            isin = "ERROR AL TRAER EL ISIN";
            
        }
        
        System.out.println("El ISIN para la emisora (" + vEmisora + ") es: (" + isin + ")");
		return isin;
	}
}
	
