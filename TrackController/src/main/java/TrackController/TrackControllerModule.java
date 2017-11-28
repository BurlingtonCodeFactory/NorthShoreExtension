package TrackController;


import javafx.application.Application;

public class TrackControllerModule {
    //private static TrackControllerManager manager;
    private static TrackControllerGUI gui;

    public TrackControllerModule()
    {
        //manager = new TrackControllerManager();
        //gui = new TrackControllerGUI(manager);
    }

    public static void main(String[] args)
    {
        Application.launch(TrackControllerGUI.class);
    }
}
