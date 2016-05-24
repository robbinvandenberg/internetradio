package ProductAgent.Web;

import ProductAgent.Component;
import ProductAgent.Exceptions.UnableToParseComponentFileException;
import ProductAgent.Log;
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
                        "<hr />" +
                        "Gooi radio weg, koop nieuwe ;)</html>";
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
