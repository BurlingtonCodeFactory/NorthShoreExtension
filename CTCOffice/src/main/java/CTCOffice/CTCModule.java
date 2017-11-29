package CTCOffice;

import CTCOffice.Interfaces.IFileService;
import CTCOffice.Interfaces.IRouteService;
import CTCOffice.Services.FileService;
import CTCOffice.Services.RouteService;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

public class CTCModule {
    protected static Injector injector;

    public CTCModule(Injector injector)
    {
        this.injector = injector;
    }

    public void launch()
    {
        CTCOffice ctcOffice = new CTCOffice();
        ctcOffice.start(new Stage());
    }
}
