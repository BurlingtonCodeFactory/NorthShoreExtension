package TrackController;


import com.google.inject.Injector;
import javafx.stage.Stage;

public class TrackControllerModule
{
    protected static Injector injector;

    public TrackControllerModule(Injector injector)
    {
        this.injector = injector;
    }

    public void launch()
    {
        System.out.println("Here TC");
        TrackControllerGUI gui = new TrackControllerGUI(injector.getInstance(TrackControllerManager.class));
        gui.start(new Stage());
    }

}
