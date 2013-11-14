package storageresources.mo;

import storageresources.mo.config.Config;
import java.io.IOException;
import javax.xml.transform.stream.StreamSource;

/**
 * @author moroz
 */
public interface ISourcesManager {

    StreamSource getLatestSource(final String reporttype) throws IOException;

    void setConfig(Config config);

}
