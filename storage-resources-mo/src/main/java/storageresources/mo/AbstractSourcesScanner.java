package storageresources.mo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
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
public class AbstractSourcesScanner<T extends StreamSourceEx.Key> {

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
            String rawFragment = source.getSystemIdUri().getRawFragment();
            try {

                key.parse(rawFragment.substring(keyStartPosition));

                source.setKey(key);

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
                } else {
                    source.delProperty(paramName);
                    checkRet = false;
                }
            }
        }

        @Override
        public boolean check() throws IOException {
            try {

                InputStream is = source.getInputStream();

                if (is == null) {
                    is = new URL(source.getSystemId()).openStream();
                }
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                parser.parse(is, this);
                return checkRet;
            } catch (Exception ex) {
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
                    key.parse(candidate);
                    if (source.getKey() == null) {//does not override previous key
                        source.setKey(key);
                    }
                    checkRet = false;
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
            if (!checker.check()) {
                break;
            }
        }
        if (source.getKey() != null) {
            addSource(source);
        }
    }
}
