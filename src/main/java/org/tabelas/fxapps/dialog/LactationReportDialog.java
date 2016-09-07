package org.tabelas.fxapps.dialog;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
import net.sf.jasperreports.engine.JasperReport;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.tabelas.fxapps.App;
import org.tabelas.fxapps.control.AutoCompleteComboBoxListener;
import org.tabelas.fxapps.controller.AnimalController;
import org.tabelas.fxapps.model.Animal;
import org.tabelas.fxapps.util.DialogFactory;
import org.tabelas.fxapps.util.ReportManager;

public class LactationReportDialog extends StackPane{
	
	private ComboBox<Animal> cbAnimal;
	VBox root;
	VBox loader;
	
	public LactationReportDialog(){
		
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
        Scene dialogScene = new Scene(this, 450, 150);
        dialog.setScene(dialogScene);
        dialog.show();
	}
	
	public void setUI(){
		GridPane form = new GridPane();
		form.setVgap(20);
		form.setHgap(10);				
		root.getChildren().add(form);
		
		Label lblAnimalNo = new Label("Animal No");
		form.add(lblAnimalNo, 0, 0);
		
		cbAnimal = new ComboBox<>();
		cbAnimal.setPrefWidth(250);
		cbAnimal.setItems(FXCollections.observableArrayList(AnimalController.getAnimalsByBranch()));
    	cbAnimal.setCellFactory(new Callback<ListView<Animal>, ListCell<Animal>>() {
			
			@Override
			public ListCell<Animal> call(ListView<Animal> arg0) {
				// TODO Auto-generated method stub
				final ListCell<Animal> cell = new ListCell<Animal>(){

                    @Override
                    protected void updateItem(Animal animal, boolean bln) {
                        super.updateItem(animal, bln);
                        
                        if(animal != null){
                            setText(animal.getAnimalNo());
                        }else{
                            setText(null);
                        }
                    }
 
                };
                
                return cell;
			}
		});
    	cbAnimal.setConverter(new StringConverter<Animal>() {
			Animal animal;
			@Override
			public String toString(Animal animal) {
				// TODO Auto-generated method stub
				if(animal == null){
					return null;
				}
				else{
					this.animal = animal;
					return animal.getAnimalNo()+"";
				}
			}
			
			@Override
			public Animal fromString(String arg0) {
				// TODO Auto-generated method stub
				return animal;
			}
		});
    	new AutoCompleteComboBoxListener<>(cbAnimal);
    	form.add(cbAnimal, 1, 0);
    	
    	HBox controlContiner = new HBox();
		controlContiner.setSpacing(20);
		controlContiner.setAlignment(Pos.CENTER);
		form.add(controlContiner, 1, 1);
		
		Button btnSave = new Button("Show Report");
		btnSave.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.CHECK_CIRCLE).size(20));
		
		Button btnReset = new Button("Reset");
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
				if(cbAnimal.getValue() == null){
		    		DialogFactory.showErrorDialog("Please select animal");
		    		return;
		    	}
				
				loader.setVisible(true);
				root.setDisable(true);
				
				new Thread(){
						public void run() {
							try {
								Animal animal = cbAnimal.getValue();
								System.out.println("Animal"+animal.getAnimalNo());
								Map<String, Object> map = new HashMap<>();
								InputStream is = getClass().getResourceAsStream("/reports/AnimalServiceReport.jrxml");
								JasperReport jr = JasperCompileManager.compileReport(is); 
								InputStream is2 = getClass().getResourceAsStream("/reports/AnimalWeightReport.jrxml");
								JasperReport jr2 = JasperCompileManager.compileReport(is2); 
								map.put("serviceSubReport", jr);
								map.put("milkWeightSubReport", jr2);
								map.put("animalId",animal.getId());
								map.put("animalNo", animal.getAnimalNo());
								map.put("branchId", App.appcontroller.getBranch().getId());
								map.put("branchName",  App.appcontroller.getBranch().getBranchName());
								map.put("purchaseDate", new SimpleDateFormat("dd-MMMMMMM-yyyy").format(animal.getPurchaseDate()));
								map.put("owner", animal.getOwnerName());
								map.put("purchasePrice", animal.getPurchasePrice().toString());
								map.put("soldDate", animal.isSold() ? new SimpleDateFormat("dd-MMMMMMM-yyyy").format(animal.getSoldDate()) : "Not Sold");
								map.put("buyer",  animal.isSold() ? animal.getBuyerName() : "Not Sold");
								map.put("soldPrice",  animal.isSold() ? animal.getSoldPrice().toString() : "Not Sold");
								
								
								InputStream is3 = getClass().getResourceAsStream("/reports/AnimalReport.jasper");
								ReportManager.showReport("/reports/AnimalReport.jrxml", map, "Animal Detailed Report");
							} catch (JRException e) {
								// TODO Auto-generated catch block
								DialogFactory.showExceptionDialog(e);
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
				cbAnimal.setValue(null);
			}
		});
	}
	
}
