package org.tabelas.fxapps.dialog;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.tabelas.fxapps.App;
import org.tabelas.fxapps.control.AutoCompleteComboBoxListener;
import org.tabelas.fxapps.controller.AnimalController;
import org.tabelas.fxapps.model.Animal;
import org.tabelas.fxapps.util.AppUtil;
import org.tabelas.fxapps.util.DialogFactory;
import org.tabelas.fxapps.util.ReportManager;

public class MilkWeightReportDialog extends StackPane{
	
	private DatePicker txtFromDate,txtToDate;
	Button btnSave,btnReset;
	VBox root;
	VBox loader;
	
	public MilkWeightReportDialog(){
		
		root = new VBox();
		root.setSpacing(20);
		root.setPadding(new Insets(30));
		root.getStylesheets().add("/theme/theme.css");
		root.getStyleClass().add("reportform");
		getChildren().add(root);
		
		ProgressIndicator pi = new ProgressIndicator();
		loader = new VBox(pi);
		loader.setAlignment(Pos.CENTER);
		loader.setVisible(false);
        getChildren().add(loader);
        
		setUI();
	}
	
	public void showDialog(){
		final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(App.appcontroller.getStage());   
        dialog.setResizable(false);
        dialog.setTitle("Animal Detailed Report");
        Scene dialogScene = new Scene(this, 450, 200);
        dialog.setScene(dialogScene);
        dialog.show();
	}
	
	public void setUI(){
		GridPane form = new GridPane();
		form.setVgap(20);
		form.setHgap(10);				
		root.getChildren().add(form);
		
		Label lblAnimalNo = new Label("From Date");
		form.add(lblAnimalNo, 0, 0);
		
		txtFromDate = new DatePicker();
		txtFromDate.setPrefWidth(250);
		txtFromDate.setPromptText("Select Date");
		txtFromDate.setConverter(AppUtil.getDatePickerFormatter());
		txtFromDate.setEditable(true);
    	form.add(txtFromDate, 1, 0);
    	
    	lblAnimalNo = new Label("To Date");
		form.add(lblAnimalNo, 0, 1);
		
		txtToDate = new DatePicker();
		txtToDate.setPrefWidth(250);
		txtToDate.setPromptText("Select Date");
		txtToDate.setConverter(AppUtil.getDatePickerFormatter());
		txtToDate.setEditable(true);
    	form.add(txtToDate, 1, 1);
    	
    	HBox controlContiner = new HBox();
		controlContiner.setSpacing(20);
		controlContiner.setAlignment(Pos.CENTER);
		form.add(controlContiner, 1, 3);
		
		btnSave = new Button("Show Report");
		btnSave.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.CHECK_CIRCLE).size(20));
		
		btnReset = new Button("Reset");
		btnReset.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES).size(20));
		
		
		controlContiner.getChildren().addAll(btnSave,btnReset);
		
		ColumnConstraints c = new ColumnConstraints();
		c.setPrefWidth(100);
		
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setPrefWidth(250);
		
		form.getColumnConstraints().addAll(c,c2);
		
		btnSave.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(txtFromDate.getValue() == null){
		    		DialogFactory.showErrorDialog("Please select from date");
		    		return;
		    	}
				else if(txtToDate.getValue() == null){
		    		DialogFactory.showErrorDialog("Please select to date");
		    		return;
		    	}
				
				loader.setVisible(true);
				root.setDisable(true);
				
				new Thread(){
						public void run() {
							try {
								Map<String, Object> map = new HashMap<>();					
								map.put("bid", App.appcontroller.getBranch().getId());
								map.put("bname",  App.appcontroller.getBranch().getBranchName());
								map.put("fromDate", AppUtil.toUtilDate(txtFromDate.getValue()));
								map.put("toDate", AppUtil.toUtilDate(txtToDate.getValue()));
								
								InputStream is3 = getClass().getResourceAsStream("/reports/MilkWeightReport.jasper");
								ReportManager.showReport("/reports/MilkWeightReport.jrxml", map, "Milk Weight Report");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								Platform.runLater(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										DialogFactory.showExceptionDialog(e);
									}
								});
							}
							
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									loader.setVisible(false);
									root.setDisable(false);
								}
							});
						};
					}.start();
				
			}
		});
		
		btnReset.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				txtFromDate.setValue(null);
				txtToDate.setValue(null);
			}
		});
	}
	
}
