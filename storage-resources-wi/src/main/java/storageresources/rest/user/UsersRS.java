package storageresources.rest.user;

import java.io.File;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import storageresources.mo.UsersList;
import storageresources.rest.App;
import static storageresources.rest.reports.ReportsRS.CFG_KEY;

/**
 * @author moroz
 */
@Path("users")
public class UsersRS {

    public static final String CFG_KEY = "users-rs";

    @Context
    private Application app;

    public UsersRS() {

    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response get() {

        try {
            String usersFileName = getConfigParam("users-list-file-name"),
                    configDirName = ((App) app).getConfigDir();

            JAXBContext jbctx = JAXBContext.newInstance(UsersList.class);

            UsersList list = (UsersList) jbctx.createUnmarshaller()
                    .unmarshal(new File(configDirName + "/" + usersFileName));

            return Response.ok(list).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    private String getConfigParam(String paramKey) throws JAXBException {
        return ((App) app).getConfig().getParamValue(CFG_KEY, paramKey);
    }

}
