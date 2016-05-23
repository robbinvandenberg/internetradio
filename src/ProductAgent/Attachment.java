package ProductAgent;

import ProductAgent.Exceptions.UnableToReadAttachmentException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Bart on 10-5-2016.
 */
public class Attachment {

    private String name;
    private String fileName;

    public Attachment(String name, String fileName){
        this.name = name;
        this.fileName = fileName;
    }

    public String getName(){
        return name;
    }

    public String getFileName(){
        String paths[] = fileName.split("[/]");
        return paths[paths.length-1];}

    public FileInputStream getFileContent() throws UnableToReadAttachmentException {
        try {
            return new FileInputStream(fileName);
        }
        catch(FileNotFoundException e) {
            throw new UnableToReadAttachmentException(e);
        }
        catch (IOException e) {
            throw new UnableToReadAttachmentException(e);
        }
    }
}
