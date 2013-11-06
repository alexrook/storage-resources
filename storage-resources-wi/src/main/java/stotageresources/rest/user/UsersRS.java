
package stotageresources.rest.user;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * @author moroz
 */
@Path("users")
public class UsersRS {

    @Context
    private UriInfo context;

   
    public UsersRS() {
    }

    @GET
    @Produces("application/xml")
    public String get() {
        
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

   
}
