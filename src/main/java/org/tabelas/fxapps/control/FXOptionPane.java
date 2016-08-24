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

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.tabelas.fxapps.App;
import org.tabelas.fxapps.enums.DialogType;

public class FXOptionPane {

	public enum Response { NO, YES, CANCEL };

	private static Response buttonSelected = Response.CANCEL;

	private static ImageView icon = new ImageView();

	static class Dialog extends Stage {
	    public Dialog( String title, Stage owner, Scene scene, String iconFile ) {
	        setTitle( title );
	        initStyle( StageStyle.UTILITY );
	        initModality( Modality.APPLICATION_MODAL );
	        initOwner( owner );
	        setResizable( false );
	        setScene( scene );
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
	        setPadding(new Insets(10, 0, 0, 0));
	        setId("textArea");
	        setEditable(false);	        
	    }
	}
	
	public static void showInformationDialog( Stage owner, String message, String title ) {
	    showMessageDialog( owner, new Message( message ), title, DialogType.INFORMATION);
	}
	
	public static void showErrorDialog( Stage owner, String message, String title ) {
	    showMessageDialog( owner, new Message( message ), title, DialogType.ERROR);
	}
	
	public static void showWarningDialog( Stage owner, String message, String title ) {
	    showMessageDialog( owner, new Message( message ), title, DialogType.WARNING);
	}
	
	public static void showExceptionDialog( Stage owner, Throwable e, String title ) {
		showExceptionDialog( owner, new Message( App.getException(e) ), title, DialogType.ERROR);
	}
	
	private static void showMessageDialog( Stage owner, Node message, String title, DialogType type ) {
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
	    
	    final Dialog dial = new Dialog( title, owner, scene,  image);
	    
	    Button okButton = new Button( "OK" );
	    okButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.CHECK));
	    okButton.setAlignment( Pos.CENTER );
	    okButton.setPadding(new Insets(0,20,0,0));
	    okButton.setMinHeight(25);
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
	    msg.setSpacing( 5 );
	    msg.getChildren().addAll( icon, message );
	    vb.getChildren().addAll( msg, buttons );
	    
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
	
	private static void showExceptionDialog( Stage owner, Node message, String title, DialogType type ) {
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
	    
	    final Dialog dial = new Dialog( title, owner, scene,  image);
	    
	    Button okButton = new Button( "OK" );
	    okButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.CHECK));
	    okButton.setAlignment( Pos.CENTER );
	    okButton.setPadding(new Insets(0,20,0,0));
	    okButton.setMinHeight(25);
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
	    msg.setSpacing( 5 );
	    msg.getChildren().addAll( icon, message );
	    vb.getChildren().addAll( msg, buttons );
	    
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
	
	public static Response showConfirmDialog( Stage owner, String message, String title ) {
	    VBox vb = new VBox();
	    vb.getStylesheets().add("/theme/theme.css");
	    vb.setPadding( new Insets(10,10,10,10) );
	    vb.setSpacing( 10 );
	    vb.getStyleClass().add("reportform");
	    
	    Scene scene = new Scene( vb , 400, 130);
	    
	    final Dialog dial = new Dialog( title, owner, scene, "/images/dialog-information.png" );
	    
	    Button yesButton = new Button( "Yes" );
	    yesButton.setMinHeight(25);
	    yesButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.CHECK));
	    yesButton.setOnAction( new EventHandler<ActionEvent>() {
	        @Override public void handle( ActionEvent e ) {
	            dial.close();
	            buttonSelected = Response.YES;
	        }
	    } );
	    
	    Button noButton = new Button( "No" );
	    noButton.setMinHeight(25);
	    noButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.BAN));
	    noButton.setOnAction( new EventHandler<ActionEvent>() {
	        @Override public void handle( ActionEvent e ) {
	            dial.close();
	            buttonSelected = Response.NO;
	        }
	    } );
	    
	    Button cancelButton = new Button( "Cancel" );
	    cancelButton.setMinHeight(25);
	    cancelButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES));
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
	    
	    HBox msg = new HBox();
	    msg.setSpacing( 5 );
	    msg.getChildren().addAll( icon, new Message( message ) );
	    vb.getChildren().addAll( msg, buttons );
	    
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