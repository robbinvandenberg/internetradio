package ProductAgent.Web;

import PreferenceAgent.Constants;
import ProductAgent.ExportZip;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.xml.internal.bind.CycleRecoverable;

import java.io.IOException;

/**
 * Created by robbin on 6-6-2016.
 */
public class ExportPage implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // add the required response header for a ZIP file
        Headers h = httpExchange.getResponseHeaders();
        h.set("Content-Type", "application/zip");
        h.set("Content-disposition", "attachment; filename=" + "exportRadioData.zip");

        httpExchange.sendResponseHeaders(200, 0);

        // Set default path
        /*Path dir = FileSystems.getDefault().getPath("");
        // Try get the path to project folder
        try {
            dir = FileSystems.getDefault().getPath(new File(".").getCanonicalPath() + "\\src\\components");
        } catch(IOException e) {
            e.printStackTrace();
        }*/

        // Generate the zip file
        //ExportZip zip = new ExportZip(dir.toAbsolutePath().toString(), httpExchange.getResponseBody());
        ExportZip zip = new ExportZip(Constants.FILEPREFIX + "components", httpExchange.getResponseBody());
        //zip.addExtraDirectory(Constants.FILEPREFIX + Constants.PREFERENCE_DIR);
        zip.zipIt();

        return;

    }
}
