package storageresources.rest.user;

import java.io.File;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;
import storageresources.mo.UsersList;

/**
 * @author moroz
 */
@Path("users")
public class UsersRS {

    @Context
    private ServletContext sctx;

    public UsersRS() {

    }

    @GET
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response get() {
        String usersFileName = sctx.getInitParameter("users-list-file-name"),
                configDirName = sctx.getInitParameter("config-dir");
            try {
                JAXBContext jbctx=JAXBContext.newInstance(UsersList.class);
                UsersList list = (UsersList) jbctx.createUnmarshaller().unmarshal(new File(configDirName + "/" + usersFileName));
                return Response.ok(list).build();
            } catch (Exception e) {
                return Response.serverError().build();
            }
        }

    }
