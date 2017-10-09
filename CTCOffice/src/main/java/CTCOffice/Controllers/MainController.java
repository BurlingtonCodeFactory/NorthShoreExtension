package CTCOffice.Controllers;

import CTCOffice.Interfaces.ITestService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class MainController {
    @FXML
    public TextField testField;

    private final ITestService testService;

    @Inject
    public MainController(ITestService testService) {
        this.testService = testService;
    }

    public void initialize() {
        testField.setText(testService.getWord());
    }
}
