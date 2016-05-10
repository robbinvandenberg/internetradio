package ProductAgent;

import java.util.List;

/**
 * Created by Bart on 10-5-2016.
 */
public class Step {
    private String title;
    private String description;
    private List<Attachment> attachments;

    public Step(String title, String description, List<Attachment> attachments){
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

    public List<Attachment> getAttachments() {
        return attachments;
    }
}
