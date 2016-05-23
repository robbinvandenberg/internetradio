package ProductAgent.Web;

/**
 * Created by Bart on 23-5-2016.
 */
public class HttpQuery {
    private String parameter;
    private String value;

    public HttpQuery(String parameter){
        this.parameter = parameter;
        this.value = null;
    }

    public HttpQuery(String parameter, String value){
        this.parameter = parameter;
        this.value = value;
    }

    public String getParameter() {
        return parameter;
    }

    public String getValue() {
        return value;
    }
}
