package storageresources.rest;

import java.io.File;
import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import storageresources.mo.config.Config;

/**
 * @author moroz
 */
@ApplicationPath("/rest")
public class App extends Application {
 
  @Context
  private ServletContext sctx;
  
  public Config getConfig() throws JAXBException{
      JAXBContext jbctx=JAXBContext.newInstance(Config.class);
      Config ret = (Config) jbctx.createUnmarshaller()
              .unmarshal(new File(sctx.getInitParameter("config-dir") + "/" + sctx.getInitParameter("config-file-name")));
      
      return ret;
  }
}
