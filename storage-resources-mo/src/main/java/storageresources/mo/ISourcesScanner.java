package storageresources.mo;

import storageresources.mo.config.Config;
import java.io.IOException;
import javax.xml.transform.stream.StreamSource;

/**
 * @author moroz
 */
public interface ISourcesScanner {

    StreamSource getLatestSource() throws IOException;
    StreamSource getSource(Ikey key) throws IOException;
    
    void setSourceChecker(ISourceChecker checker);
    void setConfig(Config config);
    
    public interface Ikey extends Comparable{
        
    }
    
    public interface ISourceChecker{
       // void setResource
    }

}
