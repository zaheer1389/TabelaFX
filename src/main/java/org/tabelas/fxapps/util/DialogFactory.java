package org.tabelas.fxapps.util;

import javafx.stage.Stage;

import org.tabelas.fxapps.App;
import org.tabelas.fxapps.control.FXOptionPane;
import org.tabelas.fxapps.control.FXOptionPane.Response;
import org.tabelas.fxapps.enums.DialogType;

public class DialogFactory {	
	
	public static void showInformationDialog(String message, Stage stage){		
		FXOptionPane.showInformationDialog(App.appcontroller.stage, message, "Tabelas");
	}
	
	public static void showWarningDialog(String message, Stage stage){
		FXOptionPane.showInformationDialog(stage, message, "Tabelas");
	}
	
	public static void showErrorDialog(String message, Stage stage){
		FXOptionPane.showErrorDialog(stage, message, "Tabelas");
	}
		
	public static Response showConfirmationDialog(String message, DialogType dialogType, Stage stage){		
		Response response = null;		
		if(dialogType == DialogType.OKCANCEL){
			response = FXOptionPane.showConfirmDialog(stage, message, "Please confirm!");
		}
		else if(dialogType == DialogType.YESNOCANCEL){
			response = FXOptionPane.showConfirmDialog(stage, message, "Please confirm!");
		}		
		return response;
	}
	
	public static void showExceptionDialog(Exception e, Stage stage){
		FXOptionPane.showExceptionDialog(stage, e, "Ooops, there was an exception!");
	}
	
}
