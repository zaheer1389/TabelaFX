package org.tabelas.fxapps.controller;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.tabelas.fxapps.App;
import org.tabelas.fxapps.model.AnimalPojo;
import org.tabelas.fxapps.persistence.JPAFacade;
import org.tabelas.fxapps.util.DialogFactory;
import org.tabelas.fxapps.util.ReportManager;

public class DashboardController {

    @FXML
    private TableView<AnimalPojo> tableServiceDueList;

    @FXML
    private Button btnPrintSalvageList;

    @FXML
    private SplitPane splitPane;

    @FXML
    private TableView<AnimalPojo> tableSalvageAnimalList;

    @FXML
    private Button btnPrintServiceDueList;
    
    @FXML
    private TableColumn<AnimalPojo, String> colAnimalNo,colAnimalNo2,colBrnachName,colBrnachName2;
    
    @FXML
    private TableColumn<AnimalPojo, String>  colServiceDate2,colDepartureDate,colExpectedArrivalDate;
    
    @FXML
    private TableColumn<AnimalPojo, String>  colArrivalDate,colServiceDate;

    @FXML
    void print(ActionEvent event) {
    	
    	/*if(App.appcontroller.getBranch() == null){
			DialogFactory.showErrorDialog("No branch has been selected.Please select branch to print report", null);
			return;
		}*/
    	
    	if(event.getSource() == btnPrintSalvageList){
    		Map<String, Object> map = new HashMap<>();
			//map.put("branchName",  App.appcontroller.getBranch().getBranchName());
			InputStream is3 = getClass().getResourceAsStream("/reports/SalvageAnimalReport2.jasper");
			ReportManager.showReport("/reports/SalvageAnimalReport2.jrxml", map, "Salvage Due Animal List");
    	}
    	else if(event.getSource() == btnPrintServiceDueList){
    		Map<String, Object> map = new HashMap<>();
			//map.put("branchName",  App.appcontroller.getBranch().getBranchName());
			InputStream is3 = getClass().getResourceAsStream("/reports/ServiceDueReport2.jasper");
			ReportManager.showReport("/reports/ServiceDueReport2.jrxml", map, "Service Due Animal List");
    	}
    }
    
    @FXML
    void initialize() {
    	btnPrintSalvageList.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.PRINT).size(20));
    	btnPrintServiceDueList.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.PRINT).size(20));
    	
    	setServiceDueTable();
    	setSalvageDueTable();
    }
    
    public void setServiceDueTable(){
    	colAnimalNo.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("animalNo"));
    	colBrnachName.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("branchName"));
		colArrivalDate.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("arrivalDate"));
		colServiceDate.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("serviceDate"));
		
		for(TableColumn col : tableServiceDueList.getColumns()){
			makeHeaderWrappable(col);
		}
		tableServiceDueList.setItems(FXCollections.observableArrayList(getServiceDueAnimals()));
    }
    
    public void setSalvageDueTable(){
    	colAnimalNo2.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("animalNo"));
    	colBrnachName2.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("branchName"));
    	colServiceDate2.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("serviceDate"));
    	colDepartureDate.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("departureDate"));
		colExpectedArrivalDate.setCellValueFactory(new PropertyValueFactory<AnimalPojo, String>("expectedArrivalDate"));
		
		for(TableColumn col : tableSalvageAnimalList.getColumns()){
			makeHeaderWrappable(col);
		}
		tableSalvageAnimalList.setItems(FXCollections.observableArrayList(getSalvageAnimals()));
    }
    
    private void makeHeaderWrappable(TableColumn col) {
        Label label = new Label(col.getText());
        label.setStyle("-fx-padding: 2px;");
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
     
        StackPane stack = new StackPane();
        stack.getChildren().add(label);
        stack.prefWidthProperty().bind(col.widthProperty().subtract(5));
        label.prefWidthProperty().bind(stack.prefWidthProperty());
        col.setGraphic(stack);
    }
    
    public List<AnimalPojo> getServiceDueAnimals(){
		List<AnimalPojo> list = new ArrayList<AnimalPojo>();
		String queryStr = "SELECT BranchName,AnimalNo,ArrivalDate From Animal a,Lactation l,Branch b "
				+ "WHERE a.id = l.AnimalId "
				+ "AND a.BranchId = b.id "
				+ "AND DepartureDate IS NULL "
				+ "AND date('now') > date(l.ArrivalDate, '+60 day') "
				+ "AND l.CurrentLactation = 1 "
				+ "AND (select count(id) from AnimalService where lactationId = l.id) = 0";
		EntityManager em = JPAFacade.getEntityManager();
		Query query = em.createNativeQuery(queryStr);
		System.out.println(queryStr);
		List<Object[]> data = query.getResultList();
		for (Object[] objects : data) {
			String branchName = objects[0].toString();
			String animalNo = objects[1].toString();
			Date dt = new Date((Long)objects[2]);
			Date dt2 = new Date(dt.getTime()+(60L*24*60*60*1000));
			System.out.println(dt);
			System.out.println(dt2);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMMM-yyyy");
			list.add(new AnimalPojo(branchName, animalNo, sdf.format(dt), "", sdf.format(dt2), ""));
		}
		
		return list;
	}
    
    public List<AnimalPojo> getSalvageAnimals(){
		List<AnimalPojo> list = new ArrayList<AnimalPojo>();
		String queryStr = "Select BranchName,AnimalNo,DepartureDate,ServiceDate From Animal a, Lactation l, AnimalService s, Branch b "
				+ "WHERE a.id = l.AnimalId "
				+ "AND l.id = s.LactationId "
				+ "AND a.BranchId = b.id "
				+ "AND s.result = 'PREGNANT' "
				+ "AND l.currentLactation = 0 "
				+ "AND l.id IN (select max(id) from Lactation l where l.animalId = a.id)";	
		System.out.println(queryStr);
		EntityManager em = JPAFacade.getEntityManager();
		Query query = em.createNativeQuery(queryStr);
		
		List<Object[]> data = query.getResultList();
		for (Object[] objects : data) {
			String branchName = objects[0].toString();
			String animalNo = objects[1].toString();
			Date dt = new Date((Long)objects[2]);
			Date dtt = new Date((Long)objects[3]);
			Date dt2 = new Date(dtt.getTime()+(320L*24*60*60*1000));
			System.out.println(dt);
			System.out.println(dt2);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMMM-yyyy");
			list.add(new AnimalPojo(branchName, animalNo, "", sdf.format(dt), sdf.format(dtt), sdf.format(dt2)));
		}
		
		return list;
	}
}
