/**
 * Copyright @ 2019 Zeljko Drascic
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ddp.hazu.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.*;

public class FileUtils {

    public static String getCurrentWorkingDir() {
        return (System.getProperty("user.dir"));
    }

    public static void writePDFToFile(String outputFile, String outputFolder, PDDocument document) {
        String fileLocation = outputFolder + File.separator + outputFile + ".pdf";
        File exportLocation = new File(fileLocation);
        try {
            document.save(exportLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Document exported to: " + fileLocation);
    }

    public static File createDirectory(String directoryPath) throws IOException {
        File dir = new File(directoryPath);
        if (dir.exists()) {
            return dir;
        }
        if (dir.mkdirs()) {
            return dir;
        }
        throw new IOException("Failed to create directory '" + dir.getAbsolutePath() + "' for an unknown reason.");
    }

    public static String writeBinaryToFile(String outputFile, String outputFolder, byte[] data) {
        String fileLocation = outputFolder + File.separator + outputFile;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileLocation);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileLocation;
    }

    public static void writeStringToFile(String outputFile, String outputFolder, String stringToWrite) {
        String fileLocation = outputFolder + File.separator + outputFile + ".txt";
        try {
            FileWriter writer = new FileWriter(fileLocation);
            writer.append(stringToWrite);
            writer.flush();
            writer.close();
        } catch (Exception exp) {
        }
    }
}
