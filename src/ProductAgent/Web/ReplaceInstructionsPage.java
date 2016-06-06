package ProductAgent.Web;

import ProductAgent.Attachment;
import ProductAgent.Component;
import ProductAgent.Exceptions.UnableToParseComponentFileException;
import ProductAgent.Log;
import ProductAgent.Step;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

/**
 * Created by Bart on 23-5-2016.
 */
public class ReplaceInstructionsPage extends Page implements HttpHandler {
    private Vector<Component> components;

    public ReplaceInstructionsPage(Vector<Component> components){
        this.components = components;
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpQuery[] queries = parseQuery(httpExchange.getRequestURI().getQuery());

        HttpQuery componentQuery = findQuery("component", queries);

        for (Component component: components ) {
            if(component.getName().equals(componentQuery.getValue())){
                String response ="<html> <head/> <body> <h1>Vervang instructies van "+component.getName()+"</h1><br />" +
                        "<hr />";
                Vector<Step> replacementSteps = component.getReplaceingSteps();
                for (Step step: replacementSteps) {
                    response += "<h2>"+step.getTitle()+"</h2>";
                    response += step.getDescription()+"<br /><br />";
                    response += "<i>attachments</i>";
                    response += "<ul>";
                    for (Attachment attachment: step.getAttachments()){
                        response += "<li><a href=\"/downloadAttachment/?component="+component.getName()+"&replacementStep="+step.getTitle()+"&attachment="+attachment.getName()+"\">"+attachment.getName()+"</a></li>";
                    }
                    response += "</ul><br /><br />";
                }
                response+= "</html>";
                httpExchange.sendResponseHeaders(200, response.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }
        }

        String response = "component not found :(";
        httpExchange.sendResponseHeaders(404, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        return;
    }
}
