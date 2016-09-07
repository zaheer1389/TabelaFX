package org.tabelas.fxapps.dialog;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

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
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.tabelas.fxapps.App;
import org.tabelas.fxapps.model.Animal;
import org.tabelas.fxapps.util.AppUtil;
import org.tabelas.fxapps.util.DialogFactory;
import org.tabelas.fxapps.util.ReportManager;

public class MilkWeightReportDialog extends VBox{
	
	private DatePicker txtFromDate,txtToDate;
	Button btnSave,btnReset;
	ProgressBar pb;
	
	public MilkWeightReportDialog(){
		setSpacing(20);
		setPadding(new Insets(30));
		getStylesheets().add("/theme/theme.css");
		getStyleClass().add("reportform");
		setUI();
	}
	
	public void showDialog(){
		final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(App.appcontroller.getStage());   
        dialog.setResizable(false);
        dialog.setTitle("Animal Milk Weight Report");
        Scene dialogScene = new Scene(this, 450, 250);
        dialog.setScene(dialogScene);
        dialog.show();
	}
	
	public void setUI(){

		pb = new ProgressBar(0);
		pb.setPrefWidth(Double.MAX_VALUE);
		pb.setVisible(false);
		getChildren().add(pb);
		
		GridPane form = new GridPane();
		form.setVgap(20);
		form.setHgap(10);				
		getChildren().add(form);
		
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
				
				btnSave.setDisable(true);
				pb.setVisible(true);
				
				processReport();
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
	
	public void processReport(){

		//Task for generating report
	    Task<Void> task = new Task<Void>() {
	        @Override
	        protected Void call() throws Exception {
	        	try {
	        		pb.setProgress(0.25f);
	        		
	        		Map<String, Object> map = new HashMap<>();					
					map.put("bid", App.appcontroller.getBranch().getId());
					map.put("bname",  App.appcontroller.getBranch().getBranchName());
					map.put("fromDate", AppUtil.toUtilDate(txtFromDate.getValue()));
					map.put("toDate", AppUtil.toUtilDate(txtToDate.getValue()));
					
					pb.setProgress(0.5f);
					
					InputStream is3 = getClass().getResourceAsStream("/reports/MilkWeightReport.jasper");
					ReportManager.showReport("/reports/MilkWeightReport.jrxml", map, "Milk Weight Report");
					
					pb.setProgress(1f);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					DialogFactory.showExceptionDialog(e);
				}
	            return null;
	        }
	    };
	    
	    //stateProperty for Task:
	    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent arg0) {
				// TODO Auto-generated method stub
				btnSave.setDisable(false);
                pb.setVisible(false);
			}
		});
	    
	    //start Task
	    new Thread(task).start();
	    
	}
	
}
