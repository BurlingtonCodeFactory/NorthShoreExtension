package CTCOffice;

import CTCOffice.Interfaces.IFileService;
import CTCOffice.Interfaces.IRouteService;
import CTCOffice.Services.FileService;
import CTCOffice.Services.RouteService;
import com.google.inject.AbstractModule;

public class CTCModule extends AbstractModule {
    @Override
    protected void configure() {
        // Bind services
        bind(IFileService.class).to(FileService.class);
        bind(IRouteService.class).to(RouteService.class);
    }
}
