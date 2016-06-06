package ProductAgent;

import java.util.List;
import java.util.Vector;

/**
 * Created by Bart on 10-5-2016.
 *
 * Class which contains a replacement step.
 */
public class Step {
    private String title;
    private String description;
    private Vector<Attachment> attachments;

    /**
     * Constructor of step.
     *
     * @param title title of step.
     * @param description description of step.
     * @param attachments List of attachments.
     */
    public Step(String title, String description, Vector<Attachment> attachments){
        this.title = title;
        this.description = description;
        this.attachments = attachments;
    }

    /**
     * Get title of replacement step.
     *
     * @return title of step.
     */
    public String getTitle(){
        return title;
    }

    /**
     * Get description of replacement step.
     *
     * @return description of step.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get attachment of replacement step.
     *
     * @return Vector containing Attachments.
     */
    public Vector<Attachment> getAttachments() {
        return attachments;
    }
}
