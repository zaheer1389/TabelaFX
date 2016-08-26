package org.tabelas.fxapps;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.persistence.EntityManager;

import org.tabelas.fxapps.control.FXOptionPane.Response;
import org.tabelas.fxapps.controller.AppController;
import org.tabelas.fxapps.enums.DialogType;
import org.tabelas.fxapps.persistence.JPAFacade;
import org.tabelas.fxapps.util.DialogFactory;

public class App extends Application {

	public Stage primaryStage;
	public static AppController appcontroller;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage;
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent arg0) {
				// TODO Auto-generated method stub
				Response response = DialogFactory.showConfirmationDialog("Do you really want to exit TABELAS?",DialogType.YESNOCANCEL);
				if(response == Response.YES){
					System.exit(0);
				}else{
					arg0.consume();
				}
			}
		});

		ClassLoader classLoader = App.class.getClassLoader();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AppView.fxml"));
		VBox root = (VBox) loader.load();
		AppController controller = loader.getController();
		controller.setStage(primaryStage);
		setAppController(controller);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/theme/theme.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("TabelaFX");
		primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("/images/stageIcon.png")));
		primaryStage.show();
		primaryStage.setMaximized(true);
		
	}
	
	public Stage getStage(){
		return primaryStage;
	}
	
	public void setAppController(AppController appcontroller){
		App.appcontroller = appcontroller; 
	}
	
	public AppController getAppController(){
		return appcontroller;
	}
	
	public void connectDatabase(){
		EntityManager em = JPAFacade.getEntityManager();		
		if(em == null){
			DialogFactory.showErrorDialog("Database connection failed!!!!!!!!!!!!!!");
		}
	}

	public static Map<String, String> getDatabaseProperties(){
		String db = getDatabaseDirectory()+"/tabelas.db";
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("javax.persistence.jdbc.driver","org.sqlite.JDBC");
		properties.put("javax.persistence.jdbc.url","jdbc:sqlite:"+db);
		properties.put("javax.persistence.jdbc.user","");
		properties.put("javax.persistence.jdbc.password","");
		return properties;
	}
	
	public static String getDatabaseDirectory(){
		String workingDir = Paths.get(".").toAbsolutePath().normalize().toString();
		System.out.println("Working directory : "+workingDir);
		String dbDir = workingDir+"/database";
		if(!new File(dbDir).exists()){
			new File(dbDir).mkdir();
			System.out.println("creating new database directory");
		}
		return dbDir;
	}
	
	public static String getException(Throwable e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
	public static Connection getConnection(){
		Connection connection = null;
		try{
			Map<String, String> properties = getDatabaseProperties();
			Class.forName(properties.get("javax.persistence.jdbc.driver"));
			connection = DriverManager.getConnection(properties.get("javax.persistence.jdbc.url"),properties.get("javax.persistence.jdbc.username"),properties.get("javax.persistence.jdbc.password"));
			return connection;
		}
		catch(Exception e){			
			DialogFactory.showExceptionDialog(e);
			return null;
		}
	}
	
	public void loadFonts(){
		try {
		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/arial.ttf")));		    
		} catch (IOException|FontFormatException e) {
		     //Handle exception
			e.printStackTrace();
		}
	}

}
