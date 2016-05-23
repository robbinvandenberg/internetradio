package ProductAgent.Web;

import ProductAgent.CheckableComponent;
import ProductAgent.Exceptions.UnableToParseComponentFileException;
import ProductAgent.Exceptions.UnableToStoreComponentFileException;
import ProductAgent.NonCheckableComponent;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

/**
 * Created by Bart on 22-5-2016.
 */
public class HomePage implements HttpHandler {

    private Vector<CheckableComponent> checkableComponents;
    private Vector<NonCheckableComponent> nonCheckableComponents;

    public HomePage(Vector<CheckableComponent> checkableComponents, Vector<NonCheckableComponent> nonCheckableComponents){
        this.checkableComponents = checkableComponents;
        this.nonCheckableComponents = nonCheckableComponents;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String response ="<html> <head/> <body> <h1>Radio component overzicht</h1><br />" +
        "<b>Controleerbare componenten</b><br /><hr />" +
        "<table style=\"width:100%\">" +
        "<tr><th align=\"left\">Component</th><th align=\"left\">Status</th><th align=\"left\">Mileage</th></tr>";

        for (CheckableComponent component: checkableComponents ) {
            response+= " <tr>"+
                    "<td>"+component.getName()+"</td>";
                    try {
                        response+= "<td>"+component.checkStatus().toString()+"</td>";
                    } catch (UnableToParseComponentFileException e) {
                        e.printStackTrace();
                    } catch (UnableToStoreComponentFileException e) {
                        e.printStackTrace();
                    }

                    try {
                        response+= "<td>"+component.getMileage()/60+" minuten</td>";
                    } catch (UnableToParseComponentFileException e) {
                        e.printStackTrace();
                    }
            response +=" <td><a href=\"/logs/?component="+component.getName()+"\">logs</a></td>" +
                    "<td><a href=\"/replaceInstructions/?component="+component.getName()+"\">vervang instructies</a></td>" +
                    " </tr>";
        }
        response +=
        "</table><br />" +
                "<br />" +
                "<br />" +
                "<b>Niet-controleerbare componenten</b><br /><hr />"+
                "<table style=\"width:100%\">" +
                "<tr><th align=\"left\">Component</th><th align=\"left\">Mileage</th></tr>";


        for (NonCheckableComponent component: nonCheckableComponents ) {
            response+= " <tr>"+
                    "<td>"+component.getName()+"</td>";

            try {
                response+= "<td>"+component.getMileage()/60+" minuten</td>";
            } catch (UnableToParseComponentFileException e) {
                e.printStackTrace();
            }
            response +="  </tr>";
        }

        response +="</body> </html>";




        t.sendResponseHeaders(200, response.length());
        t.getResponseHeaders().add("Content-Type", "text/html");
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
