package CTCOffice;

import CTCOffice.Interfaces.ITestService;
import CTCOffice.Services.TestService;
import com.google.inject.AbstractModule;

public class CTCModule extends AbstractModule {
    @Override
    protected void configure() {
        // Bind services
        bind(ITestService.class).to(TestService.class);
    }
}
