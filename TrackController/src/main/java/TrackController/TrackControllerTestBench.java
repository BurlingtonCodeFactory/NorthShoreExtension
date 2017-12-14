package TrackController;

import TrackModel.TrackModel;
import com.google.inject.Injector;
import javafx.stage.Stage;

public class TrackControllerTestBench
{
    protected static Injector injector;

    public TrackControllerTestBench(Injector injector)
    {
        this.injector = injector;
    }

    public void launch()
    {
        TrackModel track = injector.getInstance(TrackModel.class);
        TestBenchGUI gui = new TestBenchGUI(track);
        gui.start(new Stage());
    }
}
