package trackcontroller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseEvent;

public class TrackViewController {

    @FXML
    ListView<String> controllerList;

    @FXML
    TableView<String> blockList;

    ObservableList<String> controllersView = FXCollections.observableArrayList();
    ObservableList<String> blocksView = FXCollections.observableArrayList();

    @FXML
    TableView<String> blockTable;

    @FXML
    private void initialize()
    {
        List<String> controllers = new ArrayList<String>();
        controllers.add("Controller 1");
        controllers.add("Controller 2");
        controllers.add("Controller 1");
        controllers.add("Controller 2");
        controllersView.addAll(controllers);
        controllerList.setItems(controllersView);

    }
}
