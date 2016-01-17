package io.github.shakdwipeea;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by akash on 17/1/16.
 */
public class SMTPApplication {
    /**
     * Main method to be called
     * @param args Here args[0] contains the directory which is to be read
     *             and all files sent as attachments
     */
    public static void main(String args[]) {

        /**
         * Read the folder and create list of fileNames
         */
        File folderToRead = new File(args[0]);
        File[] listOfFiles = folderToRead.listFiles();

        ArrayList<String> fileNames = new ArrayList<String>();
        for (File listOfFile : listOfFiles) {
            fileNames.add(listOfFile.getAbsolutePath());
        }

        /**
         * Send the email
         */
        Mailer.sendEmail("ashakdwipeea@gmail.com", "message body", "Test", fileNames);
    }
}
