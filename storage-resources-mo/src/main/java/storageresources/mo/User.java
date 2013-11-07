package storageresources.mo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author moroz
 */
@XmlRootElement(name = "user")
public class User {

    private String Name = "";

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        if (name != null) {
            if (name.contains("\\")) { //DOMAIN\ username
                name = name.substring(name.lastIndexOf("\\") + 1);
            }
            if (name.contains("@")) {//username@DOMAIN
                name = name.substring(0, name.indexOf("@"));
            }
            this.Name = name;
        }

    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final User other = (User) obj;
        return !((this.Name == null) ? (other.Name != null) : !this.Name.equals(other.Name));
    }

}
