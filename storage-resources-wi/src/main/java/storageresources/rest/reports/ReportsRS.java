package storageresources.rest.reports;

import java.io.File;
import java.io.StringWriter;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author moroz
 */
@Path("reports")
public class ReportsRS {

    @Context
    private ServletContext sctx;

    public ReportsRS() {
    }

    @GET
    @Produces("application/xml")
    public Response get(@QueryParam("user") String user,
            @QueryParam("reporttype") String reporttype) {
        
        String xslfile = sctx.getInitParameter(reporttype + "-xsl"),
                sourcefile = sctx.getInitParameter(reporttype + "-xml"),
                configDirName = sctx.getInitParameter("config-dir");
        try {
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

}