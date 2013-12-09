package storageresources.mo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author moroz
 * @param <T>
 */
public abstract class AbstractSourcesScanner<T extends StreamSourceEx.Key> {

    private final TreeMap<T, StreamSourceEx<T>> sources = new TreeMap<T, StreamSourceEx<T>>();
    private final Queue<SourceChecker> checkers = new LinkedList<SourceChecker>();

    public interface SourceChecker {

        boolean check() throws IOException;

        void setParams(String[] params);

        void setSource(StreamSourceEx source);

    }

    public static class InNameSourceChecker implements SourceChecker {

        String pattern;
        private StreamSourceEx source;

        @Override
        public boolean check() {
            String path = source.getSystemIdUri().getPath();

            return path.matches(pattern);

        }

        @Override
        public void setParams(String[] params) {
            pattern = params[0];
        }

        @Override
        public void setSource(StreamSourceEx source) {
            this.source = source;
        }

    }

    public static class KeyInNameSourceChecker<T extends StreamSourceEx.Key> implements SourceChecker {

        private int keyStartPosition;
        private StreamSourceEx<T> source;
        private T key;

        @Override
        public boolean check() {
            String date = source.getSystemIdUri()
                    .getPath().substring(keyStartPosition);
            try {
                //   StreamSourceEx.Key nKey=key.parse(date);

                source.setKey( (T) key.parse(date));

                return true;
            } catch (IOException e) {
                //return false;
                return true; //return true to get a chance other checkers find key in source inputstream;
            }
        }

        @Override
        public void setParams(String[] params) {
            keyStartPosition = Integer.parseInt(params[0]);
        }

        @Override
        public void setSource(StreamSourceEx source) {
            this.source = source;
        }

        public void setKey(T key) {
            this.key = key;
        }

    }

    public static class SimpleInStreamSourceChecker extends DefaultHandler
            implements SourceChecker {

        protected String paramName, pattern;
        protected StreamSourceEx source;
        protected boolean checkRet;

        @Override
        public void startElement(String uri, String localName,
                String qName, Attributes attributes) throws SAXException {

            String candidate = attributes.getValue(paramName);
            if (candidate != null) {
                if (candidate.matches(pattern)) {
                    source.addProperty(paramName, candidate);
                    checkRet = true;
                } else {//last param wins
                    source.delProperty(paramName);
                    checkRet = false;
                }
            }
        }

        @Override
        public boolean check() throws IOException {
            InputStream is = null;
            try {

                is = source.getInputStream();

                if (is == null) {
                    is = new URL(source.getSystemId()).openStream();
                }
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                parser.parse(is, this);

                is.close();
                return checkRet;
            } catch (Exception ex) {
                if (is != null) {
                    is.close();
                }

                throw new IOException("check error", ex);
            }
        }

        @Override
        public void setParams(String[] params) {
            paramName = params[0];
            pattern = params[1];
        }

        @Override
        public void setSource(StreamSourceEx source) {
            this.source = source;
        }

    }

    public static class KeyInStreamSourceChecker<T extends StreamSourceEx.Key>
            extends SimpleInStreamSourceChecker {

        private T key;

        @Override
        public void startElement(String uri, String localName,
                String qName, Attributes attributes) throws SAXException {
            String candidate = attributes.getValue(paramName);
            if (candidate != null) {
                try {
                    // StreamSourceEx.Key nKey=key.parse(candidate);
                    if (source.getKey() == null) {//does not override previous key
                        source.setKey(key.parse(candidate));
                    }
                    checkRet = true;
                } catch (IOException e) {
                    checkRet = false;
                }
            }
        }

        public void setKey(T key) {
            this.key = key;
        }
    }

    public StreamSourceEx<T> getLatestSource() {
        return sources.lastEntry().getValue();
    }

    public StreamSourceEx<T> getSource(T key) {
        return sources.get(key);
    }

    public void addSourceChecker(SourceChecker checker) {
        checkers.add(checker);
    }

    protected void addSource(StreamSourceEx<T> source) {
        sources.put(source.getKey(), source);
    }

    protected void check(StreamSourceEx<T> source) throws IOException {
        for (SourceChecker checker : checkers) {

            checker.setSource(source);

            if (!checker.check()) {
                break;
            }
        }
        if (source.getKey() != null) {
            addSource(source);
        }
    }

    public int sourcesSize() {
        return sources.size();
    }

    public Set<T> sourcesKeySet() {
        return sources.keySet();
    }

    public abstract void setParams(String[] params);

    public abstract void scan() throws IOException;
}
