package ProductAgent.Web;

import ProductAgent.*;
import ProductAgent.Exceptions.UnableToReadAttachmentException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

/**
 * Created by Bart on 23-5-2016.
 */
public class DownloadAttachmentPage extends Page implements HttpHandler {
    private Vector<Component> components;

    public DownloadAttachmentPage(Vector<Component> components){
        this.components = components;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        HttpQuery[] queries = parseQuery(t.getRequestURI().getQuery());

        HttpQuery componentQuery = findQuery("component", queries);
        HttpQuery replacementStepQuery = findQuery("replacementStep", queries);
        HttpQuery attachemntQuery = findQuery("attachment", queries);

        for (Component component: components ) {
            if(component.getName().equals(componentQuery.getValue())){
                for (Step replacementStep: component.getReplaceingSteps() ) {
                    if(replacementStep.getTitle().equals(replacementStepQuery.getValue())){
                        for(Attachment attachment: replacementStep.getAttachments()){
                            if(attachment.getName().equals(attachemntQuery.getValue())){
                                try {
                                    FileInputStream fileStream = attachment.getFileContent();

                                    fileStream.getChannel().size();
                                    t.getResponseHeaders().add("Content-Type", "application/octet-stream");
                                    t.getResponseHeaders().add("Content-Disposition", "attachment; filename="+attachment.getFileName());
                                    t.sendResponseHeaders(200, fileStream.getChannel().size());
                                    OutputStream os = t.getResponseBody();
                                    while(fileStream.available() > 0) {
                                        os.write(fileStream.read());
                                    }
                                    os.close();
                                    return;
                                } catch (UnableToReadAttachmentException e) {
                                    String response = "couldn't load attachment :(";
                                    t.sendResponseHeaders(500, response.length());
                                    OutputStream os = t.getResponseBody();
                                    os.write(response.getBytes());
                                    os.close();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

        String response = "attachment not found :(";
        t.sendResponseHeaders(404, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
        return;
    }
}
