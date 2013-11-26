package storageresources.mo;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.xml.transform.stream.StreamSource;
import storageresources.mo.StreamSourceEx.Key;

/**
 * @author moroz
 * @param <T>
 */
public class StreamSourceEx<T extends Key> extends StreamSource {

    public interface Key<T> extends Comparable<Key<T>> {

        void parse(String value) throws IOException;

        void setParams(String[] params);

        T getValue();
    }

    public static class DateKey implements Key<Date> {

        private Date keyval;
        private String pattern;

        @Override
        public void parse(String value) throws IOException {
            try {
                keyval = (new SimpleDateFormat(pattern)).parse(value);
            } catch (ParseException ex) {
                throw new IOException("date key parse exception", ex);
            }
        }

        @Override
        public void setParams(String[] params) {
            pattern = params[0];
        }

        @Override
        public Date getValue() {
            return keyval;
        }

        @Override
        public int compareTo(Key<Date> o) {
            return keyval.compareTo(o.getValue());
        }

    }
    //
    private T key;
    private final Properties props = new Properties();
    private URI uri;

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public void addProperty(Object key, Object val) {
        props.put(key, val);
    }

    public void delProperty(Object key) {
        props.remove(key);
    }

    public Object getProperty(Object key) {
        return props.get(key);
    }

    @Override
    public void setSystemId(File f) {
        super.setSystemId(f);
        uri = f.toURI();
    }

    @Override
    public void setSystemId(String systemId) {
        super.setSystemId(systemId);
        uri = URI.create(systemId);

    }

    public URI getSystemIdUri() {
        return uri;
    }
}
