package ProductAgent;

import ProductAgent.Exceptions.UnableToReadAttachmentException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Bart on 10-5-2016.
 *
 * Class which contains attachment file. This class contains the filename and path to file but not the actually file
 * itself to avoid high memory usage
 */
public class Attachment {

    private String name;
    private String fileName;

    /**
     * Constructor of Attachment.
     *
     * @param name The name of the file e.g. file.txt
     * @param fileName the path and name to file path/file.txt
     */
    public Attachment(String name, String fileName){
        this.name = name;
        this.fileName = fileName;
    }

    /**
     * Returns the name of the file
     *
     * @return name of file
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the path and name of the file
     *
     * @return path and name of file
     */
    public String getFileName(){
        String paths[] = fileName.split("[/]");
        return paths[paths.length-1];}

    /**
     * Returns a filestream for reading the file
     *
     * @return filestream from file
     */
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
