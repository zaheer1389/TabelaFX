package org.tabelas.fxapps.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.tabelas.fxapps.App;
import org.tabelas.fxapps.control.FXOptionPane.Response;
import org.tabelas.fxapps.dialog.AnimalServiceReportDialog;
import org.tabelas.fxapps.dialog.LactationReportDialog;
import org.tabelas.fxapps.dialog.MilkWeightReportDialog;
import org.tabelas.fxapps.enums.DialogType;
import org.tabelas.fxapps.model.Branch;
import org.tabelas.fxapps.persistence.FacadeFactory;
import org.tabelas.fxapps.util.DialogFactory;
import org.tabelas.fxapps.util.ReportManager;

public class AppController implements EventHandler<ActionEvent>{

   	public Stage stage;

    @FXML
    private MenuItem menuItemFile_ManageBranch,menuItemFile_Exit,menuItemHelp_Help;
    
    @FXML
    private MenuItem menuItemAnimal_ManageAnimal,menuItemAnimal_Lactation,menuItemAnimal_Service,menuItemAnimal_MilkWeight,menuItemAnimal_Sold;
    
    @FXML
    private MenuItem menuItemReport_Animal_List,menuItemReport_Animal_DetailedReport,menuItemReport_Animal_ServiceReport
    			,menuItemReport_Animal_MilkWeightReport,menuItemReport_Animal_SoldAnimalReport;
    
    @FXML
    private MenuBar menuBar;
    
    @FXML
    private ComboBox<Branch> cbBranch;
    
    @FXML
    private Button btnDashboard;

    @FXML
    private VBox approot;

    @FXML
    private SplitPane context;

    @FXML
    private HBox statusBar;
    
    @FXML
    private Label lblPageCaption;

    @FXML
    void initialize() {
    	
    	btnDashboard.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.DASHBOARD).size(17));
    	
    	cbBranch.setCellFactory(new Callback<ListView<Branch>, ListCell<Branch>>() {

			@Override
			public ListCell<Branch> call(ListView<Branch> arg0) {
				// TODO Auto-generated method stub
				final ListCell<Branch> cell = new ListCell<Branch>() {

					@Override
					protected void updateItem(Branch branch, boolean bln) {
						super.updateItem(branch, bln);

						if (branch != null) {
							setText(branch.getBranchName());
						} else {
							setText(null);
						}
					}

				};

				return cell;
			}
		});
		cbBranch.setConverter(new StringConverter<Branch>() {
		
			@Override
			public String toString(Branch branch) {
				// TODO Auto-generated method stub
				if (branch == null) {
					return null;
				} else {
					return branch.getBranchName() + "";
				}
			}
		
			@Override
			public Branch fromString(String arg0) {
				// TODO Auto-generated method stub
				return FacadeFactory.getFacade().find(Branch.class,Long.parseLong(arg0));
			}
		});
		
    	for(Menu menu : menuBar.getMenus()){
    		setMenuActions(menu);
    	}
    	//menuItemReport_Animal_MilkWeightReport.setOnAction(this);
    	menuItemAnimal_Sold.setOnAction(this);
    	menuItemReport_Animal_SoldAnimalReport.setOnAction(this);
    	
    	updateBranchList();
    	showDashboard();
    }
    
    @FXML
    public void showDashboard(){
    	showView("/fxml/DashboardView.fxml");
    	lblPageCaption.setText("Dashboard");
    }
    
    @FXML
    public void changeBranch(){
    	
    }
    
    public void updateBranchList(){
    	cbBranch.setItems(FXCollections.observableArrayList(FacadeFactory.getFacade().list(Branch.class)));
    }
    
    public void setMenuActions(Menu menu){
    	for(MenuItem item : menu.getItems()){
			if(item.getClass().toString().equals("class javafx.scene.control.MenuItem")){
				item.setOnAction(this);	
			}
			else if(item.getClass().toString().equals("class javafx.scene.control.Menu")){
				setMenuActions((Menu)item);
			}
		}
    }
    
    public SplitPane getContext(){
    	return context;
    }

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public Branch getBranch(){
		return cbBranch.getValue();
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getSource() == menuItemFile_Exit){
			Response response = DialogFactory.showConfirmationDialog("Do you really want to exit TABELAS?",DialogType.YESNOCANCEL);
			if(response == Response.YES){
				System.exit(0);
				return;
			}	
		}
		else if(event.getSource() == menuItemFile_ManageBranch){
			showView("/fxml/BranchView.fxml");
			lblPageCaption.setText("Manage Branches");
			return;
		}
		
		//Branch Specific actions, So checking branch selection before calling action
		if(cbBranch.getValue() == null){
			DialogFactory.showErrorDialog("No branch has been selected.Please select branch to add/edit records");
			return;
		}
		else if(event.getSource() == menuItemAnimal_ManageAnimal){
			showView("/fxml/AnimalView.fxml");
			lblPageCaption.setText("Manage Animals");
		}
		else if(event.getSource() == menuItemAnimal_Lactation){
			showView("/fxml/LactationView.fxml");
			lblPageCaption.setText("Manage Lactations");
		}
		else if(event.getSource() == menuItemAnimal_Service){
			showView("/fxml/ServiceView.fxml");
			lblPageCaption.setText("Manage Animal Services");
		}
		else if(event.getSource() == menuItemAnimal_MilkWeight){
			showView("/fxml/MilkWeightView.fxml");
			lblPageCaption.setText("Manage Animal Milk Weights");
		}
		else if(event.getSource() == menuItemAnimal_Sold){
			showView("/fxml/SoldAnimalView.fxml");
			lblPageCaption.setText("Manage Sold Animal");
		}
		else if(event.getSource() == menuItemReport_Animal_List){
			Map<String, Object> map = new HashMap<>();
			map.put("bid", App.appcontroller.getBranch().getId());
			map.put("bname",  App.appcontroller.getBranch().getBranchName());
			InputStream is3 = getClass().getResourceAsStream("/reports/AnimalList.jasper");
			ReportManager.showReport("/reports/AnimalList.jrxml", map, "Animal List");
		}
		else if(event.getSource() == menuItemReport_Animal_DetailedReport){
			new LactationReportDialog().showDialog();
		}
		else if(event.getSource() == menuItemReport_Animal_ServiceReport){
			new AnimalServiceReportDialog().showDialog();
		}
		else if(event.getSource() == menuItemReport_Animal_MilkWeightReport){
			System.out.println("Click");
			new MilkWeightReportDialog().showDialog();
		}
		else if(event.getSource() == menuItemReport_Animal_SoldAnimalReport){
			Map<String, Object> map = new HashMap<>();
			map.put("bid", App.appcontroller.getBranch().getId());
			map.put("bname",  App.appcontroller.getBranch().getBranchName());
			InputStream is3 = getClass().getResourceAsStream("/reports/SoldAnimalList.jasper");
			ReportManager.showReport("/reports/SoldAnimalList.jrxml", map, "Sold Animal List");
		}
	}
    
    public void showView(String fxml){
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
			VBox page = (VBox) loader.load();
			getContext().getItems().clear();
			getContext().getItems().add(page);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}