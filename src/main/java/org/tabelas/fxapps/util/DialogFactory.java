package org.tabelas.fxapps.util;

import javafx.stage.Stage;

import org.tabelas.fxapps.App;
import org.tabelas.fxapps.control.FXOptionPane;
import org.tabelas.fxapps.control.FXOptionPane.Response;
import org.tabelas.fxapps.enums.DialogType;

public class DialogFactory {	
	
	public static void showInformationDialog(String message){		
		FXOptionPane.showInformationDialog(message, "Tabelas");
	}
	
	public static void showWarningDialog(String message){
		FXOptionPane.showInformationDialog(message, "Tabelas");
	}
	
	public static void showErrorDialog(String message){
		FXOptionPane.showErrorDialog(message, "Tabelas");
	}
		
	public static Response showConfirmationDialog(String message, DialogType dialogType){		
		Response response = null;
		if(dialogType == DialogType.OKCANCEL){
			response = FXOptionPane.showConfirmDialog(message, "Please confirm!");
		}
		else if(dialogType == DialogType.YESNOCANCEL){
			response = FXOptionPane.showConfirmDialog(message, "Please confirm!");
		}		
		return response;
	}
	
	public static void showExceptionDialog(Exception e){
		FXOptionPane.showExceptionDialog(e, "Ooops, there was an exception!");
	}
	
}
