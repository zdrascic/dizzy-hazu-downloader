/**
 * Copyright @ 2019 Zeljko Drascic
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ddp.hazu;

import com.ddp.hazu.ocr.TesseractTools;
import com.ddp.hazu.util.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.File;
import java.io.IOException;
import java.util.*;

/***
 * Dizzy Downloader
 * Simple tool for downloading books from https://dizbi.hazu.hr/ archive
 * Books are available as images without OCR. This tools collects images
 * does OCR and saves them on disk as searchable PDF files.
 * */
public class Dizzy {

    public static void main(String[] args) {
        String documentRootUrl = "";

        if (args.length == 1) {
            if (args[0].contains("init-ocr-basic")) {
                TesseractTools.initTesseract(args[0]);
            } else {
                documentRootUrl = args[0];
            }
        } else {
            info();
        }

        // i know strange but i had sometimes issue when not checking these
        if (!(documentRootUrl.equalsIgnoreCase(""))) {

            Map<String, String> documentMetadata = new HashMap<String, String>();
            String hazuRoot = "https://dizbi.hazu.hr/a/";
            String hazuApiRoot = "https://dizbi.hazu.hr/a/api.php";

            String fileTemplate = GeneralUtils.generateMD5(documentRootUrl);

            System.out.println("Downloading: " + documentRootUrl);
            String pageSource = NetworkUtils.getWebPage(documentRootUrl);
            //debug source
            //String pageSource = "<!DOCTYPE html><html lang=\"hr\"><head><title>Život za milijune </title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css\" integrity=\"sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4\" crossorigin=\"anonymous\"><link href=\"./lib/font-awesome/4.7.0/css/font-awesome.min.css\" rel=\"stylesheet\"><script src=\"https://code.jquery.com/jquery-3.2.1.min.js\" integrity=\"sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=\" crossorigin=\"anonymous\"></script><script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js\" integrity=\"sha384-cs/chFZiN24E4KMATLdqdvsezGxaGsi4hLGOzlXwp5UZB1LY//20VyM2taTB4QvJ\" crossorigin=\"anonymous\"></script><script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js\" integrity=\"sha384-uefMccjFJAIv6A+rW+L4AHf99KvxDjWSu1z9VI8SKNVmz4sk7buKt/6v9KI65qnm\" crossorigin=\"anonymous\"></script><link href=\"./public/core/css/indigo.css?_v18\" rel=\"stylesheet\"><link href=\"./public/core/css/header.css?_v18\" rel=\"stylesheet\"><link href=\"./public/core/css/footer.css?_v18\" rel=\"stylesheet\"><link href=\"./public/core/css/indigoLogo.css?_v18\" rel=\"stylesheet\"><link href=\"./public/core/css/recordInfo.css?_v18\" rel=\"stylesheet\"><link href=\"./public/core/css/recordViewer.css?_v18\" rel=\"stylesheet\"><link href=\"./public/core/css/metadataValue.css?_v18\" rel=\"stylesheet\"><link href=\"./public/core/css/popover.css?_v18\" rel=\"stylesheet\"><link href=\"./public/core/css/similarRecords.css?_v18\" rel=\"stylesheet\"><link href=\"./public/dizbi/css/main.css?_v18\" rel=\"stylesheet\"></head><body><nav class=\"navbar navbar-expand-lg navbar-dark indigo-navbar\"><a class=\"navbar-brand\" href=\"./\"><span class=\"indigo-navbar-text\">DiZbi.HAZU | Digitalna zbirka Hrvatske akademije znanosti i umjetnosti</span></a><button class=\"navbar-toggler indigo-toggle-button\" type=\"button\" data-toggle=\"collapse\" data-target=\"#indigo-toggle-btn\"><span class=\"navbar-toggler-icon\"></span></button><div class=\"navbar-collapse justify-content-end collapse\" id=\"indigo-toggle-btn\"><ul class=\"nav\"><li class=\"nav-item indigo-navbar-nav-item\"><a class=\"indigo-navbar-option\" href=\"./\">Početna</a></li><li class=\"nav-item indigo-navbar-nav-item\"><div class=\"dropdown indigo-dropdown\"><button class=\"btn btn-outline-light dropdown-toggle indigo-dropdown-toggle-button\" type=\"button\" data-toggle=\"dropdown\">Brzi izbornik</button><div class=\"dropdown-menu dropdown-menu-right indigo-dropdown-menu\"><a class=\"dropdown-item indigo-dropdown-item\" href=\"./?sp=person\">Osobe</a><a class=\"dropdown-item indigo-dropdown-item\" href=\"./?sp=place\">Mjesta</a><a class=\"dropdown-item indigo-dropdown-item\" href=\"./?sp=collection\">Zbirke</a></div></div></li><li class=\"nav-item indigo-navbar-nav-item\"><div class=\"dropdown indigo-dropdown\"><button class=\"btn btn-outline-light dropdown-toggle indigo-dropdown-toggle-button\" type=\"button\" data-toggle=\"dropdown\">HR <i class=\"fa fa-language\"></i></button><div class=\"dropdown-menu dropdown-menu-right indigo-dropdown-menu\"><a class=\"dropdown-item indigo-dropdown-item\" href=\"./?locale=ls&lc=hr\">Hrvatski (HR)</a><a class=\"dropdown-item indigo-dropdown-item\" href=\"./?locale=ls&lc=en\">English (EN)</a></div></div></li></ul></div></nav><div class=\"container indigo-container\"><div class=\"indigo-recordinfo\"><div class=\"indigo-recordinfo-toolbar\"><a class=\"indigo-recordinfo-toolbar-back-button-link\" href=\"javascript:history.back()\"><i class=\"fa fa-arrow-left fa-fw\"></i> Povratak</a></div><div class=\"my-3\"><div class=\"indigo-recordinfo-title\">Život za milijune </div></div><div class=\"card indigo-recordinfo-card\"><div class=\"card-body\"><div class=\"row\"><div class=\"col-lg-4 col-sm-12 indigo-recordinfo-image-holder\"><a href=\"./?pr=iiif.v.a&id=33172\" target=\"_blank\"><img class=\"img-fluid indigo-recordinfo-image\" src=\"https://dizbi.hazu.hr/a/./d17b118n/rep/8/gz/53l/8gz53lfv28lr.jpg\" alt=\"Život za milijune \"></a><div class=\"indigo-recordinfo-image-toolbar-option\"><a class=\"btn btn-sm btn-outline-secondary indigo-recordinfo-image-toolbar-option-item indigo-recordinfo-image-toolbar-option-item-sb\" href=\"./?pr=iiif.v.a&id=33172\" target=\"_blank\" title=\"IIIF preglednik\"><i class=\"fa fa-eye fa-fw\"></i></a></div></div><div class=\"col-lg-8 col-sm-12\"><div class=\"indigo-recordinfo-metadata\"><div class=\"row indigo-recordinfo-metadata-row\"><div class=\"col-lg-3 col-md-4 col-sm-12\"><div class=\"indigo-recordinfo-label\">Naslov</div></div><div class=\"col-lg-9 col-md-8 col-sm-12\"><span class=\"indigo-metadatavalue-none\">Život za milijune</span></div></div><div class=\"row indigo-recordinfo-metadata-row\"><div class=\"col-lg-3 col-md-4 col-sm-12\"><div class=\"indigo-recordinfo-label\">Autor</div></div><div class=\"col-lg-9 col-md-8 col-sm-12\"><a class=\"indigo-metadatavalue-concept indigo-pointer\" href=\"./?pr=l&mrf[10068][44298]=a\" tabindex=\"0\" data-concept=\"./?pc=i&id=44298\" data-related=\"./?pr=l&mrf[10068][44298]=a\" data-filter=\"./?pr=l&mrf[10068][44298]=a\" onclick=\"Indigo.Front.metadataPopover(event);\">Matoš, Antun Gustav</a></div></div><div class=\"row indigo-recordinfo-metadata-row\"><div class=\"col-lg-3 col-md-4 col-sm-12\"><div class=\"indigo-recordinfo-label\">Vrijeme nastanka</div></div><div class=\"col-lg-9 col-md-8 col-sm-12\"><span class=\"indigo-metadatavalue-none\">1909</span></div></div><div class=\"row indigo-recordinfo-metadata-row\"><div class=\"col-lg-3 col-md-4 col-sm-12\"><div class=\"indigo-recordinfo-label\">Materijalni opis</div></div><div class=\"col-lg-9 col-md-8 col-sm-12\"><span class=\"indigo-metadatavalue-none\">5</span></div></div><div class=\"row indigo-recordinfo-metadata-row\"><div class=\"col-lg-3 col-md-4 col-sm-12\"><div class=\"indigo-recordinfo-label\">Zbirka</div></div><div class=\"col-lg-9 col-md-8 col-sm-12\"><a class=\"indigo-metadatavalue-concept indigo-pointer\" href=\"./?pr=l&mrf[10277][118846]=a\" tabindex=\"0\" data-concept=\"./?pc=i&id=118846\" data-related=\"./?pr=l&mrf[10277][118846]=a\" data-filter=\"./?pr=l&mrf[10277][118846]=a\" onclick=\"Indigo.Front.metadataPopover(event);\">Književna ostavština A. G. Matoša</a></div></div><div class=\"row indigo-recordinfo-metadata-row\"><div class=\"col-lg-3 col-md-4 col-sm-12\"><div class=\"indigo-recordinfo-label\">Predmet-opći pojam</div></div><div class=\"col-lg-9 col-md-8 col-sm-12\"><a class=\"indigo-metadatavalue-concept indigo-pointer\" href=\"./?pr=l&mrf[10228][105962]=a\" tabindex=\"0\" data-concept=\"./?pc=i&id=105962\" data-related=\"./?pr=l&mrf[10228][105962]=a\" data-filter=\"./?pr=l&mrf[10228][105962]=a\" onclick=\"Indigo.Front.metadataPopover(event);\">Hrvatska književnost</a></div></div><div class=\"row indigo-recordinfo-metadata-row\"><div class=\"col-lg-3 col-md-4 col-sm-12\"><div class=\"indigo-recordinfo-label\">Jezik</div></div><div class=\"col-lg-9 col-md-8 col-sm-12\"><a class=\"indigo-metadatavalue-concept indigo-pointer\" href=\"./?pr=l&mrf[10104][101276]=a\" tabindex=\"0\" data-concept=\"./?pc=i&id=101276\" data-related=\"./?pr=l&mrf[10104][101276]=a\" data-filter=\"./?pr=l&mrf[10104][101276]=a\" onclick=\"Indigo.Front.metadataPopover(event);\">hrv</a></div></div><div class=\"row indigo-recordinfo-metadata-row\"><div class=\"col-lg-3 col-md-4 col-sm-12\"><div class=\"indigo-recordinfo-label\">Tip građe</div></div><div class=\"col-lg-9 col-md-8 col-sm-12\"><a class=\"indigo-metadatavalue-concept indigo-pointer\" href=\"./?pr=l&mrf[10279][118312]=a\" tabindex=\"0\" data-concept=\"./?pc=i&id=118312\" data-related=\"./?pr=l&mrf[10279][118312]=a\" data-filter=\"./?pr=l&mrf[10279][118312]=a\" onclick=\"Indigo.Front.metadataPopover(event);\">tekst</a></div></div><div class=\"row indigo-recordinfo-metadata-row\"><div class=\"col-lg-3 col-md-4 col-sm-12\"><div class=\"indigo-recordinfo-label\">Vrsta građe</div></div><div class=\"col-lg-9 col-md-8 col-sm-12\"><a class=\"indigo-metadatavalue-concept indigo-pointer\" href=\"./?pr=l&mrf[10278][118358]=a\" tabindex=\"0\" data-concept=\"./?pc=i&id=118358\" data-related=\"./?pr=l&mrf[10278][118358]=a\" data-filter=\"./?pr=l&mrf[10278][118358]=a\" onclick=\"Indigo.Front.metadataPopover(event);\">članak</a> &#149; <a class=\"indigo-metadatavalue-concept indigo-pointer\" href=\"./?pr=l&mrf[10278][118365]=a\" tabindex=\"0\" data-concept=\"./?pc=i&id=118365\" data-related=\"./?pr=l&mrf[10278][118365]=a\" data-filter=\"./?pr=l&mrf[10278][118365]=a\" onclick=\"Indigo.Front.metadataPopover(event);\">strojopis</a></div></div><div class=\"row indigo-recordinfo-metadata-row\"><div class=\"col-lg-3 col-md-4 col-sm-12\"><div class=\"indigo-recordinfo-label\">Jedinica HAZU</div></div><div class=\"col-lg-9 col-md-8 col-sm-12\"><a class=\"indigo-metadatavalue-concept indigo-pointer\" href=\"./?pr=l&mrf[10276][118385]=a\" tabindex=\"0\" data-concept=\"./?pc=i&id=118385\" data-related=\"./?pr=l&mrf[10276][118385]=a\" data-filter=\"./?pr=l&mrf[10276][118385]=a\" onclick=\"Indigo.Front.metadataPopover(event);\">Odsjek za povijest hrvatske književnosti</a></div></div><div class=\"row indigo-recordinfo-metadata-row\"><div class=\"col-lg-3 col-md-4 col-sm-12\"><div class=\"indigo-recordinfo-label\">Signatura / Inventarni broj</div></div><div class=\"col-lg-9 col-md-8 col-sm-12\"><span class=\"indigo-metadatavalue-none\">HR HAZU/172 - 19/1114</span></div></div><div class=\"row indigo-recordinfo-metadata-row\"><div class=\"col-lg-3 col-md-4 col-sm-12\"><div class=\"indigo-recordinfo-label\">Licencije</div></div><div class=\"col-lg-9 col-md-8 col-sm-12\"><a class=\"indigo-metadatavalue-concept indigo-pointer\" href=\"./?pr=l&mrf[10379][124064]=a\" tabindex=\"0\" data-concept=\"./?pc=i&id=124064\" data-related=\"./?pr=l&mrf[10379][124064]=a\" data-filter=\"./?pr=l&mrf[10379][124064]=a\" onclick=\"Indigo.Front.metadataPopover(event);\">CC BY-NC-ND</a></div></div></div></div></div></div></div></div></div><div class=\"indigo-dizbi-footer-main\"><div class=\"container\"><div class=\"row\"><div class=\"col-md-4 col-12 mb-2\"><div class=\"indigo-dizbi-footer-header\">O projektu</div><span>DiZbi.HAZU trenutno uključuje građu u sljedećim izvornim formatima: knjige, časopisi, kazališne cedulje, rukopisi, mikrofilmovi, note, fotografije, sadreni odljevi, medalje i plakete, umjetničke slike, arhitektonski nacrti i modeli, video.</span><div class=\"my-3\"><a class=\"indigo-dizbi-footer-links\" href=\"http://www.europeana.eu/portal/search.html?qf=DATA_PROVIDER%3a%22Croatian%20Academy%20of%20Sciences%20and%20Arts%22&rows=24\" target=\"_blank\"> 24.003 jedinica </a>dostupno je u Europeani</div><a href=\"https://www.facebook.com/dizbi.hazu\" target=\"_blank\" class=\"d-block mb-1 indigo-dizbi-footer-links\"><i class=\"fa fa-facebook-official fa-2x mr-3\"></i>facebook.com/dizbi.hazu</a><a class=\"indigo-dizbi-footer-links d-block mb-1\" href=\"https://twitter.com/DizbiHazu\" target=\"_blank\"><i class=\"fa fa-twitter fa-2x mr-3\"></i>twitter.com/DizbiHazu</a></div><div class=\"col-md-4 col-12 mb-2\"><div class=\"indigo-dizbi-footer-header\">Informacije</div><div class=\"mb-2\">Digitalna zbirka Hrvatske akademije znanosti i umjetnosti (DiZbi.HAZU)</div><div class=\"mb-2\">Sadrži digitaliziranu građu 14 Akademijinih istraživačkih i muzejsko-galerijskih jedinica te Akademijine Knjižnice. Osnovana je 2009. godine.</div><div class=\"my-2\"><a class=\"indigo-dizbi-footer-links\" href=\"./?associates\">Suradnici</a></div><div class=\"my-2\">Kontakt: <a class=\"indigo-dizbi-footer-links\" href=\"mailto:dizbi@hazu.hr\" target=\"_blank\">dizbi@hazu.hr</a></div><div class=\"my-2\"><a class=\"indigo-dizbi-footer-links\" href=\"./?terms\">Uvjeti korištenja</a></div><div class=\"my-2\"><a class=\"indigo-dizbi-footer-links\" href=\"./?manual\">Upute za rad</a></div></div><div class=\"col-md-4 col-12 mb-2\"><div class=\"indigo-dizbi-footer-header\">Linkovi</div><div class=\"row\"><div class=\"col-6 mb-2\"><a href=\"http://agregator.arhivx.net/mk/?m=providers\" style=\"background-color: #FFF\" class=\"d-inline-block p-3\"><img src=\"./public/dizbi/img/europeana_logo.png\" class=\"img-fluid\"></a></div><div class=\"col-6 mb-2\"><a href=\"http://pro.europeana.eu/europeana-cloud\" style=\"background-color: #FFF\" class=\"d-inline-block p-3\"><img src=\"./public/dizbi/img/ecloud_l.jpg\" class=\"img-fluid\"></a></div><div class=\"col-6 mb-2\"><a href=\"http://portal.getty.edu/\" style=\"background-color: #FFF\" class=\"d-inline-block p-3\"><img src=\"./public/dizbi/img/GRI_Portal_logo.gif\" class=\"img-fluid\"></a></div></div></div></div></div></div><div class=\"indigo-dizbi-footer-rights\"><div class=\"container\"><div class=\"row\"><div class=\"col-8 indigo-dizbi-footer-rights-left-col\">2018 © DiZbi.HAZU | Digitalna zbirka Hrvatske akademije znanosti i umjetnosti</div><div class=\"col-4 text-right\"><a class=\"in-logo p-1\" href=\"http://www.eindigo.net/\" title=\"Powered by Indigo\" id=\"in_logo\" target=\"_blank\"><img class=\"in-logo-img\" title=\"Powered by Indigo\" src-trans=\"https://a.eindigo.net/cdn/eindigo/logo/indigo-gray.png\" src-color=\"https://a.eindigo.net/cdn/eindigo/logo/indigo-color.png\" src=\"https://a.eindigo.net/cdn/eindigo/logo/indigo-gray.png\" alt=\"indigo\"><span class=\"in-logo-inf\">Powered by Indigo</span></a></div></div></div></div><script src=\"./public/core/js/indigoLogo.js?_v18\"></script><script src=\"./public/core/js/popover.js?_v18\"></script><!-- Global site tag (gtag.js) - Google Analytics --><script async src=\"https://www.googletagmanager.com/gtag/js?id=UA-22263371-3\"></script><script>window.dataLayer = window.dataLayer || [];function gtag(){dataLayer.push(arguments);}gtag('js', new Date());gtag('config', 'UA-22263371-3');</script></body></html>";

            System.out.println("Parsing document metadata");
            Document doc = Jsoup.parse(pageSource, "UTF-8");
            Element details = doc.select("a[title$=IIF Preglednik]").first();

            String detailsUrl = details.attr("href");
            String detailsApiUrl = "";

            // remove .dot at start
            if (detailsUrl.startsWith("./")) {
                detailsUrl = detailsUrl.substring(2);
            }

            // separate api url (cleaning needed later)
            detailsApiUrl = detailsUrl;

            //cleanup of .v.a for api call
            if (detailsApiUrl.contains("iiif.v.a")) {
                detailsApiUrl = detailsApiUrl.replaceAll("iiif.v.a", "iiif");
            }

            // add server root
            detailsApiUrl = hazuApiRoot + detailsApiUrl;
            detailsUrl = hazuRoot + detailsUrl;

            System.out.println("Found document root: " + detailsUrl);
            System.out.println("Found api server root: " + detailsApiUrl);

            String mainJsonMetadata = NetworkUtils.getJSONDataFromAPI(detailsApiUrl);
            System.out.println("Document metadata: " + mainJsonMetadata);

            JSONObject documentMetadataObject = new JSONObject(mainJsonMetadata);
            JSONArray responseMetadata = GeneralUtils.parseDocumentMetadata(documentMetadataObject);
            ArrayList<String> pagesImageUrls = GeneralUtils.parseDocumentForImages(documentMetadataObject);

            if (pagesImageUrls.size() == 0) {
                System.out.println("No pages found");
            } else {
                // prepare download folders
                try {
                    File workPath = FileUtils.createDirectory("work" + File.separator + fileTemplate);

                    // make download job
                    ArrayList<String> storedImages = ImageUtils.processImagesFromServer(pagesImageUrls, workPath);

                    // create image pdf job
                    System.out.println("Building PDF for export...");
                    PDDocument generated = PDFWorker.generatePDFFromImages(storedImages);
                    String exportedDocumentName = GeneralUtils.getDocumentName(responseMetadata);
                    FileUtils.writePDFToFile(exportedDocumentName, workPath.getAbsolutePath(), generated);

                    // prepare Tesseract export list
                    System.out.println("Building OCR list for export...");
                    String ocrList = PDFWorker.prepareTesseractList(storedImages);
                    FileUtils.writeStringToFile(exportedDocumentName, workPath.getAbsolutePath(), ocrList);

                    // Start Tesseract OCR job
                    PDFWorker.startTesseractWorker(storedImages, workPath.getAbsolutePath(),exportedDocumentName);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void info() {
        System.out.println("Dizzy Downloader version 1.0");
        System.out.println("Init OCR: java -jar dizzy.jar init-ocr-basic");
        System.out.println("Download document: java -jar dizzy.jar {main-document-url}");
        System.out.println("-----------------------------------------------");
    }
}
