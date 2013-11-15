package storageresources.mo;

import storageresources.mo.config.Config;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
//import org.apache.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import storageresources.mo.config.Config.Param;

public class XMLObjectsTest {

    //  private static final Logger log = Logger.getLogger("test.XMLObjectsTest");
    private static final String TMP_FILE_EXT = "_XMLObjectsTest.tmp.xml";
    private Collection<User> fixtureUsers;
    private Config testConfig;

    @Before
    public void fixtureForTests() {

        fixtureUsers = new ArrayList<User>(12);

        for (int i = 0; i < 12; i++) {
            User u = new User();
            u.setName("test_" + i);
            fixtureUsers.add(u);
        }

        testConfig = new Config();
        testConfig.setKey("config_main");

        Collection<Config> cfgs = new ArrayList<Config>(7);
        for (int i = 0; i < 7; i++) {
            Config c = new Config("config_" + i);
            c.setParams(createTestParams("config_" + i));
            cfgs.add(c);
        }

        testConfig.setConfigs(cfgs);

    }

    private Collection<Param> createTestParams(String pre) {
        Collection<Param> ret = new ArrayList<Param>(12);
        for (int i = 0; i < 12; i++) {
            Param p = new Param(pre + "_param_" + i, pre + "_value_" + i);
            ret.add(p);
        }
        return ret;
    }

    @BeforeClass
    public static void setupClz() {
        File[] oldfiles = new File(System.getProperty("java.io.tmpdir")).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(TMP_FILE_EXT);
            }
        });

        for (File f : oldfiles) {
            f.delete();
        }

    }

    private void markDeleteOnExtit(File f) {
        // f.deleteOnExit();
    }

    @Test
    public void testUserList() {
        try {
            JAXBContext jc = JAXBContext.newInstance(UsersList.class, User.class);
            UsersList list = new UsersList();
            list.setUsers(fixtureUsers);
            File ulf = File.createTempFile("users____", TMP_FILE_EXT);

            Marshaller ma = jc.createMarshaller();
            ma.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            ma.marshal(list, ulf);
            Unmarshaller unmarsh = jc.createUnmarshaller();
            UsersList actualList = (UsersList) unmarsh.unmarshal(ulf);
            assertEquals(list, actualList);
            markDeleteOnExtit(ulf);

        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testUser() {
        try {
            JAXBContext jc = JAXBContext.newInstance(User.class);
            User user = new User();

            user.setName("DOMAIN\\username");
            assertEquals("username", user.getName());
            user.setName("username@DOMAIN");
            assertEquals("username", user.getName());

            Marshaller marsh = jc.createMarshaller();
            marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            File uf = File.createTempFile("user____", TMP_FILE_EXT);
            markDeleteOnExtit(uf);
            marsh.marshal(user, uf);

            Unmarshaller unmarsh = jc.createUnmarshaller();
            User actualUser = (User) unmarsh.unmarshal(uf);
            assertEquals(user, actualUser);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testConfig_tdd_s1() {
        try {
            JAXBContext jc = JAXBContext.newInstance(Config.class);
            Config config = new Config();

            config.setKey("app");

            ArrayList<Config.Param> params = new ArrayList<Config.Param>(12);
            for (int i = 0; i < 12; i++) {
                Config.Param param = new Config.Param("param_" + i, "param_" + i + "_value");
                params.add(param);
            }
            config.setParams(params);

            Marshaller marsh = jc.createMarshaller();
            marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            File uf = File.createTempFile("config_tdd_s1____", TMP_FILE_EXT);
            markDeleteOnExtit(uf);
            marsh.marshal(config, uf);

            Unmarshaller unmarsh = jc.createUnmarshaller();
            Config actualConfig = (Config) unmarsh.unmarshal(uf);
            assertEquals(config, actualConfig);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testConfig_tdd_s2() {

        try {
            JAXBContext jc = JAXBContext.newInstance(Config.class);
           
            Marshaller marsh = jc.createMarshaller();
            marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            File uf = File.createTempFile("config_tdd_s2____", TMP_FILE_EXT);
            markDeleteOnExtit(uf);
            marsh.marshal(testConfig, uf);
            
            Unmarshaller unmarsh = jc.createUnmarshaller();
            Config actualConfig = (Config) unmarsh.unmarshal(uf);
            assertEquals(testConfig, actualConfig);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
