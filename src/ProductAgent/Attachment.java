package ProductAgent;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    public byte[] getFileContent(){
        //TODO: lees file in output bytes;
        throw new NotImplementedException();
    }
}
