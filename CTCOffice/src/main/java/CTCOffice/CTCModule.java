package CTCOffice;

import CTCOffice.Interfaces.IFileService;
import CTCOffice.Services.FileService;
import com.google.inject.AbstractModule;

public class CTCModule extends AbstractModule {
    @Override
    protected void configure() {
        // Bind services
        bind(IFileService.class).to(FileService.class);
    }
}
