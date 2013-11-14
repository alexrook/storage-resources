package storageresources.mo.config;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author moroz
 */
@XmlRootElement(name = "config")
public class Config {

    public static class Param {

        @XmlAttribute
        public String key;

        @XmlValue
        public String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String get(String key) {
            return (this.key.equalsIgnoreCase(key)) ? value : null;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 61 * hash + (this.key != null ? this.key.hashCode() : 0);
            hash = 61 * hash + (this.value != null ? this.value.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Param other = (Param) obj;
            if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
                return false;
            }
            if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value)) {
                return false;
            }
            return true;
        }

    }

    private String key;

    private Collection<Param> params;

    private Collection<Config> configs;

    public String getParamValue(String configKey, String paramKey) {

        if (this.key.equalsIgnoreCase(configKey)) {
            for (Param param : params) {
                String ret = param.get(paramKey);
                if (ret != null) {
                    return ret;
                }
            }
        } else {
            for (Config cfg : configs) {
                String ret = cfg.getParamValue(configKey, paramKey);
                if (ret != null) {
                    return ret;
                }
            }
        }

        return null;
    }

    @XmlAttribute(name = "key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @XmlElementWrapper(name = "params")
    @XmlElement(name = "param")
    public Collection<Param> getParams() {
        return params;
    }

    public void setParams(Collection<Param> params) {
        this.params = params;
    }

    @XmlElementWrapper(name = "configs")
    @XmlElement(name = "config")
    public Collection<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(Collection<Config> configs) {
        this.configs = configs;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.key != null ? this.key.hashCode() : 0);
        hash = 79 * hash + (this.params != null ? this.params.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Config other = (Config) obj;
        if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
            return false;
        }
        if (this.params != other.params && (this.params == null || !this.params.equals(other.params))) {
            return false;
        }
        if (this.configs != other.configs && (this.configs == null || !this.configs.equals(other.configs))) {
            return false;
        }
        return true;
    }

}
