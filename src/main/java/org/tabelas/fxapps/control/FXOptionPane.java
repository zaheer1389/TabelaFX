package org.tabelas.fxapps.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.Priority;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.tabelas.fxapps.App;
import org.tabelas.fxapps.enums.DialogType;

public class FXOptionPane {

	public enum Response { NO, YES, CANCEL };

	private static Response buttonSelected = Response.CANCEL;

	private static ImageView icon = new ImageView();

	static class Dialog extends Stage {
	    public Dialog( String title, Scene scene, String iconFile ) {
	        setTitle( title );
	        initStyle( StageStyle.UTILITY );
	        initModality( Modality.APPLICATION_MODAL );
	        initOwner( App.appcontroller.getStage() );
	        setResizable( false );
	        setScene( scene );
	        getIcons().add(new Image(App.class.getResourceAsStream("/images/stageIcon.png")));
	        icon.setImage( new Image( getClass().getResourceAsStream( iconFile ) ) );
	    }
	    public void showDialog() {
	        sizeToScene();
	        centerOnScreen();
	        showAndWait();
	    }
	}

	static class Message extends TextArea {
	    public Message( String msg ) {
	        super( msg );
	        setWrapText(true);
	        setPadding(new Insets(00, 0, 0, 0));
	        setId("textArea");
	        setEditable(false);	        
	    }
	}
	
	public static void showInformationDialog( String message, String title ) {
	    showMessageDialog( new Message( message ), title, DialogType.INFORMATION);
	}
	
	public static void showErrorDialog( String message, String title ) {
	    showMessageDialog( new Message( message ), title, DialogType.ERROR);
	}
	
	public static void showWarningDialog( String message, String title ) {
	    showMessageDialog( new Message( message ), title, DialogType.WARNING);
	}
	
	public static void showExceptionDialog( Throwable e, String title ) {
		showExceptionDialog( new Message( App.getException(e) ), title, DialogType.ERROR);
	}
	
	private static void showMessageDialog( Node message, String title, DialogType type ) {
	    VBox vb = new VBox();
	    vb.setPadding( new Insets(10,10,10,10) );
	    vb.setSpacing( 10 );
	    vb.getStylesheets().add("/theme/theme.css");
	    vb.getStyleClass().add("reportform");
	    
	    Scene scene = new Scene( vb , 400, 130);
	    
	    String image = "";
	    if(type == DialogType.INFORMATION){
	    	image = "/images/dialog-information.png";
	    }
	    else if(type == DialogType.ERROR){
	    	image = "/images/dialog-error.png";
	    }
	    else if(type == DialogType.WARNING){
	    	image = "/images/dialog-warning.png";
	    }
	    
	    final Dialog dial = new Dialog( title, scene, image);
	    
	    Button okButton = new Button( "OK" );
	    okButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.CHECK_CIRCLE).size(18));
	    okButton.setAlignment( Pos.CENTER );
	    okButton.setPadding(new Insets(0,20,0,0));
	    okButton.setMinHeight(30);
	    okButton.setOnAction( new EventHandler<ActionEvent>() {
	        @Override public void handle( ActionEvent e ) {
	            dial.close();
	        }
	    } );
	    
	    HBox buttons = new HBox();
	    buttons.setAlignment( Pos.CENTER_RIGHT );
	    buttons.setSpacing( 10 );	   
	    buttons.getChildren().addAll( okButton);
	    buttons.setPadding(new Insets(0,20,0,0));
	    buttons.setPrefHeight(35);
	    
	    HBox msg = new HBox();
	    msg.setMaxHeight(Double.MAX_VALUE);
	    msg.setSpacing( 5 );
	    msg.setHgrow(message, Priority.ALWAYS);
	    msg.getChildren().addAll( icon, message );
	    
	    vb.getChildren().addAll( msg, buttons );
	    vb.setVgrow(msg, Priority.ALWAYS);
	    
	    EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getCode() == KeyCode.ENTER || arg0.getCode() == KeyCode.SPACE){
					dial.close();
				}
			}
		};	    
	    vb.setOnKeyPressed(handler);
	    vb.requestFocus();
	    
	    dial.showDialog();
	}
	
	private static void showExceptionDialog( Node message, String title, DialogType type ) {
	    VBox vb = new VBox();
	    vb.setPadding( new Insets(10,10,10,10) );
	    vb.setSpacing( 10 );
	    vb.getStylesheets().add("/theme/theme.css");
	    vb.getStyleClass().add("reportform");
	    
	    Scene scene = new Scene( vb );
	    
	    String image = "";
	    if(type == DialogType.INFORMATION){
	    	image = "/images/dialog-information.png";
	    }
	    else if(type == DialogType.ERROR){
	    	image = "/images/dialog-error.png";
	    }
	    else if(type == DialogType.WARNING){
	    	image = "/images/dialog-warning.png";
	    }
	    
	    final Dialog dial = new Dialog( title, scene, image);
	    
	    Button okButton = new Button( "OK" );
	    okButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.CHECK_CIRCLE).size(18));
	    okButton.setAlignment( Pos.CENTER );
	    okButton.setPadding(new Insets(0,20,0,0));
	    okButton.setMinHeight(30);
	    okButton.setOnAction( new EventHandler<ActionEvent>() {
	        @Override public void handle( ActionEvent e ) {
	            dial.close();
	        }
	    } );
	    
	    HBox buttons = new HBox();
	    buttons.setAlignment( Pos.CENTER_RIGHT );
	    buttons.setSpacing( 10 );	   
	    buttons.getChildren().addAll( okButton);
	    buttons.setPadding(new Insets(0,20,0,0));
	    buttons.setPrefHeight(35);
	    
	    HBox msg = new HBox();
	    msg.setMaxHeight(Double.MAX_VALUE);
	    msg.setSpacing( 5 );
	    msg.setHgrow(message, Priority.ALWAYS);
	    msg.getChildren().addAll( icon, message );
	    
	    vb.getChildren().addAll( msg, buttons );
	    vb.setVgrow(msg, Priority.ALWAYS);
	    
	    EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getCode() == KeyCode.ENTER || arg0.getCode() == KeyCode.SPACE){
					dial.close();
				}
			}
		};	    
	    vb.setOnKeyPressed(handler);
	    vb.requestFocus();
	    
	    dial.setResizable(true);
	    dial.showDialog();
	    
	    
	}
	
	public static Response showConfirmDialog( String message, String title ) {
	    VBox vb = new VBox();
	    vb.getStylesheets().add("/theme/theme.css");
	    vb.setPadding( new Insets(10,10,10,10) );
	    vb.setSpacing( 10 );
	    vb.getStyleClass().add("reportform");
	    
	    Scene scene = new Scene( vb , 400, 130);
	    
	    final Dialog dial = new Dialog( title, scene, "/images/dialog-information.png" );
	    
	    Button yesButton = new Button( "Yes" );
	    yesButton.setMinHeight(30);
	    yesButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.CHECK_CIRCLE).size(18));
	    yesButton.setOnAction( new EventHandler<ActionEvent>() {
	        @Override public void handle( ActionEvent e ) {
	            dial.close();
	            buttonSelected = Response.YES;
	        }
	    } );
	    
	    Button noButton = new Button( "No" );
	    noButton.setMinHeight(30);
	    noButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.BAN).size(18));
	    noButton.setOnAction( new EventHandler<ActionEvent>() {
	        @Override public void handle( ActionEvent e ) {
	            dial.close();
	            buttonSelected = Response.NO;
	        }
	    } );
	    
	    Button cancelButton = new Button( "Cancel" );
	    cancelButton.setMinHeight(30);
	    cancelButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES).size(18));
	    cancelButton.setOnAction( new EventHandler<ActionEvent>() {
	        @Override public void handle( ActionEvent e ) {
	            dial.close();
	            buttonSelected = Response.CANCEL;
	        }
	    } );
	    
	    HBox buttons = new HBox();
	    buttons.setAlignment( Pos.CENTER_RIGHT );
	    buttons.setSpacing( 10 );	   
	    buttons.getChildren().addAll( yesButton, noButton, cancelButton);
	    buttons.setPadding(new Insets(0,20,0,0));
	    buttons.setPrefHeight(55);
	    
	    TextArea msgg = new TextArea(message);
	    
	    HBox msg = new HBox();
	    msg.setMaxHeight(Double.MAX_VALUE);
	    msg.setSpacing( 5 );
	    msg.setHgrow(msgg, Priority.ALWAYS);
	    msg.getChildren().addAll( icon,  msgg);
	    
	    vb.getChildren().addAll( msg, buttons );
	    vb.setVgrow(msg, Priority.ALWAYS);
	    
	    
	    EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getCode() == KeyCode.ENTER || arg0.getCode() == KeyCode.SPACE){
					dial.close();
					buttonSelected = Response.YES;
				}
				if(arg0.getCode() == KeyCode.ESCAPE){
					dial.close();
					buttonSelected = Response.CANCEL;
				}
			}
		};	    
	    vb.setOnKeyPressed(handler);
	    vb.requestFocus();
	    
	    dial.showDialog();
	    
	    return buttonSelected;
	}

}