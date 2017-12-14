//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Robert Taylor
//**************************************************
package CTCOffice;

import com.google.inject.Injector;
import javafx.stage.Stage;

public class CTCModule
{
    static Injector injector;

    public CTCModule(Injector injector)
    {
        CTCModule.injector = injector;
    }

    public void launch()
    {
        CTCOffice ctcOffice = new CTCOffice();
        ctcOffice.start(new Stage());
    }
}
