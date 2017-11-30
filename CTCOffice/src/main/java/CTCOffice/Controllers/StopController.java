package CTCOffice.Controllers;

import CTCOffice.Models.Stop;
import CTCOffice.Models.Train;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class StopController {
    @FXML
    public HBox root;

    @FXML
    public Label contentLabel;

    @FXML
    public Button deleteButton;

    private Stop stop;
    private Train train;

    public StopController(Stop stop, Train train) {
        this.stop = stop;
        this.train = train;
    }

    public void initialize() {
        contentLabel.setText(stop.getBlock().toString());
        deleteButton.visibleProperty().bind(root.hoverProperty());

        deleteButton.setOnAction(this::delete);
    }

    public void delete(ActionEvent e) {
        train.removeStop(stop); // TODO: Figure out weird bug were something tries to delete other trains stop
    }
}
