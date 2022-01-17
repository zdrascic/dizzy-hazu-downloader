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

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFWorker {

    /**
     * Generate PDF file from images
     * NO OCR, just image per page
     * */
    public static PDDocument generatePDFFromImages(ArrayList<String> storedImages) {
        PDDocument document = new PDDocument();
        int pageCount = storedImages.size();

        for (int i = 0; i < pageCount; i++) {
            PDPage page = new PDPage();
            document.addPage(page);
        }

        int pageCounter = 0;

        for (String imagePath : storedImages) {
            try {
                PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);

                float pageWidth = (float) pdImage.getWidth();
                float pageHeight = (float) pdImage.getHeight();

                PDPage page = document.getPage(pageCounter);
                page.setMediaBox(new PDRectangle(0f, 0f, pageWidth, pageHeight));

                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.drawImage(pdImage, 0, 0);
                contentStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            pageCounter++;

        }

        return document;
    }

    /**
     * Debug method used for testing with external tool
     *
     * **/
    public static String prepareTesseractList(ArrayList<String> storedImages) {
        StringBuilder ocrList = new StringBuilder();
        for (String imagePath : storedImages) {
            ocrList.append(imagePath).append(System.lineSeparator());
        }
        return ocrList.toString();
    }

    /***
     * Iterate through images, OCR image -> pdf page
     * Merge all OCR'd pages in single document.
     * Clean :)
     * */
    public static void startTesseractWorker(ArrayList<String> pages, String workFolder, String fileName) {
        System.out.println("Starting Tesseract OCR job");
        String testData = FileUtils.getCurrentWorkingDir() + File.separator + "tessdata";
        String mergedDocumentPath = workFolder + File.separator + fileName + "_OCR.pdf";

        ArrayList<String> generatedPages = new ArrayList<>();

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(testData);
        tesseract.setLanguage("hrv"); //currently scanning only Croatian

        List<ITesseract.RenderedFormat> list = new ArrayList<ITesseract.RenderedFormat>();
        list.add(ITesseract.RenderedFormat.PDF);

        int i = 0;
        int max = (pages.size() - 1);

        for (String image : pages) {
            long timeStart = System.currentTimeMillis();
            try {
                if (image != null) {
                    String pageFile = workFolder + File.separator + "page_" + i;
                    tesseract.createDocuments(image, pageFile, list);
                    generatedPages.add(pageFile + ".pdf");
                    i++;
                }
            } catch (TesseractException e) {
                e.printStackTrace();
            }
            long timeEnd = System.currentTimeMillis();
            long timeWork = (timeEnd - timeStart) / 1000;
            System.out.println("(" +i + "/" + max + ")" + " " + image + " (" + timeWork + "s)");
        }

        PDFMergerUtility mergerUtility = new PDFMergerUtility();
        mergerUtility.setDestinationFileName(mergedDocumentPath);

        System.out.println("Combine generated pages");
        for (String page : generatedPages) {
            System.out.println("> " + page);
            try {
                File file = new File(page);
                mergerUtility.addSource(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //TODO:: Check for better merge process
        try {
            mergerUtility.mergeDocuments();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Cleanup start.");
        for (String page : generatedPages) {
            File file = new File(page);
            boolean delete = file.delete();
            if (!delete) {
                System.out.println("Failed page cleanup: " + page);
            }
        }

        System.out.println("Cleanup done.");
        System.out.println("***************************************************");
        System.out.println("Merged document save to: " + mergedDocumentPath);
        System.out.println("***************************************************");
    }

}
