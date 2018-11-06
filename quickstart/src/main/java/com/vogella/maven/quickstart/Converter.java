package com.vogella.maven.quickstart;
import java.sql.*;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;




public class Converter {
	private static String ROOT_NODE_NAME = "Pacientes";
    private static String ROW_NODE_NAME = "Paciente";
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	//static final String DB_URL = "jdbc:mysql://localhost/mydb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	
	 //  Database credentials
	   //static final String USER = "User";
	  // static final String PASS = "14031994";
	   

	    public static String resultSetToXML(ResultSet rs) {
	        String result = null;

	        try {
	            Document root = new Document(new Element(ROOT_NODE_NAME));
	            ResultSetMetaData metaData = rs.getMetaData();

	            // Get all rows.
	            while (rs.next()) {
	                Element row = new Element(ROW_NODE_NAME);
	                // Add to root
	                root.getRootElement().addContent(row);

	                for (int i = 1; i <= metaData.getColumnCount(); i++) {
	                    // Use the column label as the node name
	                    Element val = new Element(metaData.getColumnLabel(i));
	                    // Use the String value of the data as the value node's child
	                    val.setText("" + rs.getObject(i));
	                    row.addContent(val);
	                }
	            }

	            XMLOutputter out = new XMLOutputter();
	            out.setFormat(Format.getPrettyFormat());
	            result = out.outputString(root);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return result;
	    }
	   
	   public static void main(String[] args) throws ParserConfigurationException {
		  Scanner keyboard = new Scanner(System.in);
		  System.out.println("Introduce el Usuario :");
		  String USER = keyboard.nextLine();
		  System.out.println("Introduce la contraseña :");
		  String PASS = keyboard.nextLine();
		  System.out.println("Introduce el nombre de la bd :");
		  String bdname = keyboard.nextLine();
		  String DB_URL = "jdbc:mysql://localhost/"+bdname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	 
		   Connection conn = null;
		   Statement stmt = null;
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.cj.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);

		      //STEP 4: Execute a query
		      System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      System.out.println("Nombre de la tabla de pacientes: ");
		      String Paciente = keyboard.nextLine();
		      sql = "SELECT * FROM "+Paciente;
		      ResultSet rs = stmt.executeQuery(sql);
		 
		     // System.out.println(resultSetToXML(rs));
		      System.out.println("Especifica un nombre para el fichero :");
		      String filename = keyboard.nextLine();
		      FileUtils.writeStringToFile(new File(filename), resultSetToXML(rs) );
		
		      rs.close();
		      stmt.close();
		      conn.close();
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   System.out.println("Goodbye!");
		   
		}//end main
		

}
