package storageresources.mo;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class XMLObjectsTest {

    private static final Logger log = Logger.getLogger("test.XMLObjectsTest");
    private static final String TMP_FILE_EXT = "_XMLObjectsTest.tmp.xml";
    private Collection<User> fixtureUsers;
   

    @Before
    public void fixtureForTests() {
        fixtureUsers =new ArrayList<User>(12);

        for (int i = 0; i < 12; i++) {
            User u = new User();
            u.setName("test_" + i);
            fixtureUsers.add(u);
        }
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
        //иногда для просмотра и конроля содержимого временных тестовых файлов необходимо их сохранить
        //для упрощения администрирования кода вызов deleteOnExit() для всех временных файлов этого теста вынесен сюда
        // f.deleteOnExit();
    }

    @Test
    public void testUserList() {
        try {
            JAXBContext jc = JAXBContext.newInstance(UsersList.class, User.class);
            UsersList list=new UsersList();
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
}
