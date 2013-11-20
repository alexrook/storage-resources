package storageresources.rest.reports;

import java.io.File;
import java.io.StringWriter;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import storageresources.rest.App;

/**
 * @author moroz
 */
@Path("reports")
public class ReportsRS {

    public static final String CFG_KEY = "reports-rs";

    @Context
    private Application app;

    public ReportsRS() {
    }

    @GET
    @Path("cfg")
    @Produces(MediaType.APPLICATION_XML)
    public Response getConfig() throws JAXBException {
        return Response.ok(((App) app).getConfig()).build();
    }

    @GET
    @Produces("application/xml")
    public Response get(@QueryParam("user") String user,
            @QueryParam("reporttype") String reporttype) {

        try {
            String xslfile = getConfigParam(reporttype + "-xsl"),
                    sourcefile = getConfigParam(reporttype + "-xml"),
                    configDirName = ((App) app).getConfigDir();

            StringWriter result = new StringWriter(300);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer(new StreamSource(new File(configDirName + "/" + xslfile)));
            t.setParameter("UserName", user);
            t.setOutputProperty(OutputKeys.INDENT, "yes");

            t.transform(new StreamSource(new File(configDirName + "/" + sourcefile)),
                    new StreamResult(result));

            return Response.ok(result.toString(), MediaType.APPLICATION_XML).build();

        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    private String getConfigParam(String paramKey) throws JAXBException {
        return ((App) app).getConfig().getParamValue(CFG_KEY, paramKey);
    }
}
