package storageresources.mo;

import org.apache.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import storageresources.mo.AbstractSourcesScanner.InNameSourceChecker;

/**
 * @author moroz
 */
public class AbstractSourceScannerTest {

    private static final Logger log = Logger.getLogger("test.AbstractSourceScannerTest");

    
    @Test
    public void test_DD_InNameSourceChecker() {
        InNameSourceChecker insc = new AbstractSourcesScanner.InNameSourceChecker();

        StreamSourceEx sse = new StreamSourceEx();
        sse.setSystemId("file:///home/user/some.xml");

        insc.setSource(sse);
        insc.setParams(new String[]{".*xml"});
        assertTrue(insc.check());
    }
}
