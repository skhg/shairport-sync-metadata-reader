package ie.jackhiggins.shairportsyncmetadatareader.reader;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Async method to begin reading the metadata stream must be called from another bean.
 * This component's only job is to make that call.
 */
@Component
public class StartupAsyncInitialiser implements InitializingBean {

    final ReaderService readerService;

    public StartupAsyncInitialiser(ReaderService readerService) {
        this.readerService = readerService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        readerService.runReader();
    }
}
