package ProductAgent;

import java.util.List;
import java.util.Vector;

/**
 * Created by Bart on 10-5-2016.
 */
public class Step {
    private String title;
    private String description;
    private Vector<Attachment> attachments;

    public Step(String title, String description, Vector<Attachment> attachments){
        this.title = title;
        this.description = description;
        this.attachments = attachments;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Vector<Attachment> getAttachments() {
        return attachments;
    }
}
