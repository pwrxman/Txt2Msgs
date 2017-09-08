package application;

import java.sql.Connection;
import java.sql.DriverManager;


public class TxtFixDB {
	private Connection conexion;
	    
	public TxtFixDB() {
	    try {
	    	
	    	 Class.forName("com.mysql.jdbc.Driver");	    	 
	         
	         conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/Sati", "sati_usr", "S@ti_dis");
	         
	         if(conexion != null){
	             System.out.println("Conexion Exitosa!");	         }
	         else{
	        	 System.out.println("Conexion Fallida!");                
	         }
	     } catch (Exception e) {
	             e.printStackTrace();
	     }
	    
	 }
	
	public Connection getConection() {
        return conexion;
    }
}
