package org.tabelas.fxapps.dialog;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToggleGroup;
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

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.tabelas.fxapps.App;
import org.tabelas.fxapps.control.AutoCompleteComboBoxListener;
import org.tabelas.fxapps.controller.AnimalController;
import org.tabelas.fxapps.model.Animal;
import org.tabelas.fxapps.util.AppUtil;
import org.tabelas.fxapps.util.DialogFactory;
import org.tabelas.fxapps.util.ReportManager;

public class MilkWeightSheetReportDialog extends StackPane{
	
	private DatePicker txtFromDate,txtToDate;
	Button btnSave,btnReset;
	ListView<Item> listView;
	VBox root;
	VBox loader;
	
	Set<Item> selectedItems = new HashSet<Item>();
	
	public MilkWeightSheetReportDialog(){
		
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
        Scene dialogScene = new Scene(this, 450, 400);
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
    	
    	HBox checkBoxContainer = new HBox();
    	checkBoxContainer.setSpacing(20);
    	checkBoxContainer.setAlignment(Pos.CENTER);
		form.add(checkBoxContainer, 1, 2);
		
		CheckBox cbSelectAll = new CheckBox("Select All");
		CheckBox cbDeSelectAll = new CheckBox("DeSelect All");
	
		checkBoxContainer.getChildren().addAll(cbSelectAll,cbDeSelectAll);
		
		cbSelectAll.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(cbSelectAll.isSelected()){
					changeSelection(true);
					cbDeSelectAll.setSelected(false);
				}
				
			}
		});
		
		cbDeSelectAll.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(cbDeSelectAll.isSelected()){
					changeSelection(false);
				}
				cbSelectAll.setSelected(false);
			}
		});
    	
    	List<Animal> animals = AnimalController.getAnimalsByBranch();
    	
    	lblAnimalNo = new Label("Select animals");
		form.add(lblAnimalNo, 0, 3);
		
    	listView = new ListView<>();
    	form.add(listView, 1, 3);
    	
        for (Animal animal : animals) {
            Item item = new Item(animal.getAnimalNo(), false);

            // observe item's on property and display message if it changes:
            item.onProperty().addListener((obs, wasOn, isNowOn) -> {
                System.out.println(item.getName() + " changed on state from "+wasOn+" to "+isNowOn);
                if(isNowOn){
                	selectedItems.add(item);
                }
                else{
                	selectedItems.remove(item);
                }
            });

            listView.getItems().add(item);
        }

        listView.setCellFactory(CheckBoxListCell.forListView(new Callback<Item, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Item item) {
                return item.onProperty();
            }
        }));
    	
    	HBox controlContiner = new HBox();
		controlContiner.setSpacing(20);
		controlContiner.setAlignment(Pos.CENTER);
		form.add(controlContiner, 1, 4);
		
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
								
								if(cbSelectAll.isSelected()){
									map.put("queryType", "All");
								}
								else{
									map.put("queryType", "Selected");
								}
								String selected = StringUtils.join(getSelectedItemAsString(), ",");
								map.put("selectedAnimals", "("+selected+")");
								
								String queryStr = map.get("queryType").equals("All")
								?
								"Select *,substr('0000000000'||AnimalNo, length('0000000000'||AnimalNo)-9, 10) as sorID "+
								"From AnimalMilkWeight w, Lactation l, Animal a "+
								"WHERE a.branchId = $P{bid} "+
								"AND w.LactationId = l.ID "+
								"AND l.AnimalId = a.ID "+
								"AND w.WeightDate BETWEEN $P{fromDate} AND $P{toDate} "+
								"ORDER BY sorID "
								:
								"Select *,substr('0000000000'||AnimalNo, length('0000000000'||AnimalNo)-9, 10) as sorID "+
								"From AnimalMilkWeight w, Lactation l, Animal a "+
								"WHERE a.branchId = $P{bid} "+
								"AND w.LactationId = l.ID "+
								"AND l.AnimalId = a.ID "+
								"AND w.WeightDate BETWEEN $P{fromDate} AND $P{toDate} "+
								"AND a.AnimalNo IN  ("+selected+") "+
								"ORDER BY sorID ";
								
								System.out.println(queryStr);
								
								map.put("queryStr", queryStr);
								
								InputStream is3 = getClass().getResourceAsStream("/reports/MilkWeightReport.jasper");
								ReportManager.showReport("/reports/MilkWeightReport_2.jrxml", map, "Milk Weight Report");
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
				
				cbSelectAll.setSelected(false);
				cbDeSelectAll.setSelected(false);
			}
		});
	}
	
	public void changeSelection(boolean selection){
		List<Item> items = listView.getItems();
		for(Item item : items){
			item.setOn(selection);
		}
	}
	
	public List<String> getSelectedItemAsString(){
		System.out.println("Selection size : "+selectedItems.size());
		List<String> strings = new ArrayList<String>();
		for(Item item : selectedItems){
			strings.add(item.getName());
		}
		return strings;
	}
	
	public static class Item {
        private final StringProperty name = new SimpleStringProperty();
        private final BooleanProperty on = new SimpleBooleanProperty();

        public Item(String name, boolean on) {
            setName(name);
            setOn(on);
        }

        public final StringProperty nameProperty() {
            return this.name;
        }

        public final String getName() {
            return this.nameProperty().get();
        }

        public final void setName(final String name) {
            this.nameProperty().set(name);
        }

        public final BooleanProperty onProperty() {
            return this.on;
        }

        public final boolean isOn() {
            return this.onProperty().get();
        }

        public final void setOn(final boolean on) {
            this.onProperty().set(on);
        }

        @Override
        public String toString() {
            return getName();
        }

    }
	
}
