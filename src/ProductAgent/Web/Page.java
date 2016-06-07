package ProductAgent.Web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Vector;

/**
 * Created by Bart on 23-5-2016.
 */
public class Page {

    protected Page(){

    }


    protected HttpQuery findQuery(String parameter, HttpQuery[] queries){
        for(HttpQuery query: queries){
            if(query.getParameter().equals(parameter)){
                return query;
            }
        }
        return null;
    }


    protected HttpQuery[] parseQuery(String query) throws UnsupportedEncodingException {
        Vector<HttpQuery> queries = new Vector<HttpQuery>();

        if (query != null) {
            String paramValuePairs[] = query.split("[&]");

            for (String pair : paramValuePairs) {
                String paramValuePair[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (paramValuePair.length > 0) {
                    key = URLDecoder.decode(paramValuePair[0],
                            System.getProperty("file.encoding"));
                }

                if (paramValuePair.length > 1) {
                    value = URLDecoder.decode(paramValuePair[1],
                            System.getProperty("file.encoding"));
                }

                if(value == null){
                    queries.add(new HttpQuery(key));
                }
                else{
                    queries.add(new HttpQuery(key, value));
                }

            }
        }

        HttpQuery[] httpQueries = queries.toArray(new HttpQuery[queries.size()]);
        return httpQueries;
    }
}
