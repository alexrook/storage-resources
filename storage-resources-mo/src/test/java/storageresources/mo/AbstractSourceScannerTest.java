package storageresources.mo;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import storageresources.mo.AbstractSourcesScanner.*;
import storageresources.mo.StreamSourceEx.*;

/**
 * @author moroz
 */
public class AbstractSourceScannerTest {

    private static final Logger log
            = Logger.getLogger("test.AbstractSourceScannerTest");

    @Test
    public void test_DD_InNameSourceChecker() {
        InNameSourceChecker insc = new InNameSourceChecker();

        StreamSourceEx sse = new StreamSourceEx();
        sse.setSystemId("file:///home/user/some.xml");

        insc.setSource(sse);
        insc.setParams(new String[]{".*xml"});
        assertTrue(insc.check());
    }

    @Test
    public void test_DD_KeyInNameSourceCheckerTest() throws ParseException {

        KeyInNameSourceChecker<StreamSourceEx.DateKey> kinsc
                = new KeyInNameSourceChecker<StreamSourceEx.DateKey>();

        StreamSourceEx<StreamSourceEx.DateKey> sse
                = new StreamSourceEx<StreamSourceEx.DateKey>();

        sse.setSystemId("file:///home/user/some_2013-03-12.xml");

        StreamSourceEx.DateKey key = new StreamSourceEx.DateKey();
        key.setParams(new String[]{"yyyy-MM-dd"});

        kinsc.setKey(key);

        kinsc.setParams(new String[]{"16"});//datekey position in path string

        kinsc.setSource(sse);

        assertTrue(kinsc.check());

        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2013-03-12.xml"),
                sse.getKey().getValue());

    }

    @Test
    public void test_DD_SimpleInStreamSourceChecker() throws IOException {
        SimpleInStreamSourceChecker sinsc
                = new SimpleInStreamSourceChecker();
        StreamSourceEx sse = new StreamSourceEx();

        File tf = new File("target/classes/big-files-source.xml");

        log.info("test file uri = " + tf.toURI().toASCIIString());
        assertTrue("test file can not be read", tf.canRead());
        sse.setSystemId(tf.toURI().toASCIIString());

        sinsc.setParams(new String[]{"GeneratedAt", ".*"});
        sinsc.setSource(sse);

        assertTrue(sinsc.check());

        assertNotNull(sse.getProperty("GeneratedAt"));
        log.info("GeneratedAt:" + sse.getProperty("GeneratedAt"));
    }

    @Test
    public void test_DD_KeyInStreamSourceChecker() throws IOException {
        KeyInStreamSourceChecker<StreamSourceEx.DateKey> kinsc
                = new KeyInStreamSourceChecker<StreamSourceEx.DateKey>();

        StreamSourceEx<StreamSourceEx.DateKey> sse
                = new StreamSourceEx<StreamSourceEx.DateKey>();

        File tf = new File("target/classes/big-files-source.xml");
        log.info("test file uri = " + tf.toURI().toASCIIString());
        assertTrue("test file can not be read", tf.canRead());
        sse.setSystemId(tf.toURI().toASCIIString());

        StreamSourceEx.DateKey key = new StreamSourceEx.DateKey();
        key.setParams(new String[]{"dd.MM.yyyy hh:mm:ss"});//16.11.2013 23:05:00

        kinsc.setKey(key);
        kinsc.setParams(new String[]{"GeneratedAt", ".*"});
        kinsc.setSource(sse);

        assertTrue(kinsc.check());

        assertNotNull(sse.getKey());
        log.info("StreamSourceEx.Key = " + sse.getKey().getValue());

    }

    @Test
    public void test_DD_Scanner() throws IOException {
        Key<String> key =new Key<String>() {
            String val;
           
            @Override
            public void parse(String value) throws IOException {
                val=value;
            }

            @Override
            public void setParams(String[] params) {
                //noop
            }

            @Override
            public String getValue() {
               return val;
            }

            @Override
            public int compareTo(Key<String> o) {
                return val.compareTo(o.getValue());
            }

         
        };

        AbstractSourcesScanner<Key<String>> scanner
                = new AbstractSourcesScanner<Key<String>>() {
                    String sd;

                    @Override
                    public void setParams(String[] params) {
                        sd = params[0];
                        assertNotNull(sd);
                    }

                    @Override
                    public void scan() throws IOException {

                        File reportsDir = new File(sd);
                        assertTrue(reportsDir.isDirectory());
                        assertTrue(reportsDir.canRead());

                        for (File f : reportsDir.listFiles()) {
                            StreamSourceEx<Key<String>> sse = new StreamSourceEx<Key<String>>();
                            sse.setSystemId(f.toURI().toASCIIString());
                            check(sse);//check will add sources with key
                        }
                    }
                };

        scanner.setParams(new String[]{"target/classes/"});

        InNameSourceChecker insc = new AbstractSourcesScanner.InNameSourceChecker();
        insc.setParams(new String[]{".*xml"});

        KeyInStreamSourceChecker<Key<String>> kinsc
                = new KeyInStreamSourceChecker<Key<String>>();

        
        kinsc.setKey(key);
        kinsc.setParams(new String[]{"Type", ".*"});

        scanner.addSourceChecker(insc);
        scanner.addSourceChecker(kinsc);

        scanner.scan();

        assertNotNull(scanner.getLatestSource());
        assertNotNull(scanner.getLatestSource().getKey());
        log.info("latest source key = " + scanner.getLatestSource().getKey().getValue());
        log.info("sources size = " + scanner.sourcesSize());//

    }
}
