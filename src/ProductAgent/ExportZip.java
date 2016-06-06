package ProductAgent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by robbin on 6-6-2016.
 */
public class ExportZip {
    //! Source directory
    private String srcDir;
    //! Path to output destination, including filename (must end with .zip)
    private String zipFile;
    //! List of files to zip, automatically generated from srcDir
    private List<String> fileList;
    //! Outputstream
    private OutputStream os;

    /**
     * Create an instance to export a directory to a zip file.
     * @param srcDir Source directory
     * @param zipFile Path to output destination, including filename (must end with .zip)
     */
    public ExportZip(String srcDir, String zipFile) {
        this.srcDir = srcDir;
        this.zipFile = zipFile;
        this.fileList = new ArrayList<String>();
        generateFileList(new File(this.srcDir));

        try {
            this.os = new FileOutputStream(this.zipFile);
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Create an instance to export a directory to a .zip in an outputstream (i.e. used by serving it to browser)
     * @param srcDir Source directory
     * @param os The outputstream
     */
    public ExportZip(String srcDir, OutputStream os) {
        this.srcDir = srcDir;
        this.zipFile = zipFile;
        this.fileList = new ArrayList<String>();
        this.os = os;
        generateFileList(new File(this.srcDir));
    }

    /**
     * Zip the files to the output destination
     */
    public void zipIt() {
        byte[] buffer = new byte[1024];

        try {
            ZipOutputStream zos = new ZipOutputStream(os);

            for(String file : this.fileList) {
                System.out.println("Adding file: " + file);
                ZipEntry ze = new ZipEntry(file);
                zos.putNextEntry(ze);

                FileInputStream in = new FileInputStream(srcDir + File.separator + file);

                int len;
                while((len = in.read(buffer)) > 0) {
                    zos.write(buffer,0,len);
                }

                zos.closeEntry();

                in.close();

            }

            // Close ZipOutputStream
            zos.close();
            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Traverse a directory and get all files,
     * and add the file into fileList
     * @param node file or directory
     */
    private void generateFileList(File node){

        //add file only
        if(node.isFile()){
            fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
        }

        if(node.isDirectory()){
            String[] subNote = node.list();
            for(String filename : subNote){
                generateFileList(new File(node, filename));
            }
        }

    }

    /**
     * Format the file path for zip
     * @param file file path
     * @return Formatted file path
     */
    private String generateZipEntry(String file){
        return file.substring(srcDir.length()+1, file.length());
    }

    /**
     * Add an extra directory to insert in zip file
     * @param dir String path to directory
     */
    public void addExtraDirectory(String dir) {
        generateFileList(new File(dir));
    }

}
