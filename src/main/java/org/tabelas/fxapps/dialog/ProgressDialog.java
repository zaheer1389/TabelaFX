package org.tabelas.fxapps.dialog;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProgressDialog {
	private final Stage dialogStage;
    private final ProgressIndicator pin = new ProgressIndicator();

    public ProgressDialog() {
        dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        // PROGRESS BAR
        final Label label = new Label();
        label.setText("Please wait....");

        pin.setProgress(-1F);

        final HBox hb = new HBox();
        hb.setPadding(new Insets(20));
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(label, pin);

        Scene scene = new Scene(hb);
        dialogStage.setTitle("Please wait........");
        dialogStage.setScene(scene);
    }

    public void activateProgressBar(final Task<?> task)  {
        pin.progressProperty().bind(task.progressProperty());
        dialogStage.show();
    }

    public Stage getDialogStage() {
        return dialogStage;
    }
}
