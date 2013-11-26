package storageresources.mo;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import storageresources.mo.StreamSourceEx.DateKey;

/**
 * @author moroz
 */
public class StreamSourceExTest {

    private static final Logger log = Logger.getLogger("test.StreamSourceExTest");
   
    @Test
    public void test_DD_DateKey() {
        DateKey dk00 = new StreamSourceEx.DateKey();
        DateKey dk01 = new StreamSourceEx.DateKey();
        DateKey dk02 = new StreamSourceEx.DateKey();
        DateKey dk03 = new StreamSourceEx.DateKey();

        try {

            dk00.setParams(new String[]{"yyyy-MM-dd"});
            dk00.parse("1967-03-22");
            log.info("1967-03-22 parsed as = " + dk00.getValue());

            dk01.setParams(new String[]{"yyyy-MM-dd hh:mm:ss"});
            dk01.parse("1961-01-22 12:34:41");
            log.info("1961-01-22 12:34:41 parsed as = " + dk01.getValue());

            assertTrue(dk00.compareTo(dk01) > 0);

            dk02.setParams(new String[]{"yyyy-MM-dd_hh-mm-ss"});
            dk02.parse("2013-11-24_21-05-01");
            log.info("2013-11-24_21-05-01 parsed as = " + dk02.getValue());

            assertTrue(dk01.compareTo(dk02) < 0);

            dk03.setParams(new String[]{"yyyy-MM-dd_hh-mm-ss"});
            dk03.parse("2013-12-24_21-05-01.xml and bla bla bla");
            log.info("'2013-12-24_21-05-01.xml and bla bla bla' parsed as = " + dk03.getValue());

            assertTrue(dk03.compareTo(dk02) > 0);

        } catch (IOException e) {
            fail(e.getMessage());
        }

    }
}
