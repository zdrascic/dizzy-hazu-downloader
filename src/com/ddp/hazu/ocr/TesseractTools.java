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

package com.ddp.hazu.ocr;

import com.ddp.hazu.util.FileUtils;
import com.ddp.hazu.util.NetworkUtils;

import java.io.File;
import java.io.IOException;

public class TesseractTools {

    public static void initTesseract(String param) {
        System.out.println("Initialization of Tesseract OCR library");
        System.out.println("More info on: https://github.com/tesseract-ocr");

        File dataLocation = getTestDataLocation();
        System.out.println("Working folder: " + dataLocation.getAbsolutePath());

        String baseDownloadUrl = "https://github.com/tesseract-ocr/tessdata_best/blob/main/";
        String[] testDataFiles = getTestData(param);

        for (String fileName : testDataFiles) {
            String fileEndpoint = baseDownloadUrl + fileName + "?raw=true";
            System.out.println("Downloading: " + fileEndpoint);
            NetworkUtils.getRawFileFromServer(fileEndpoint, fileName, dataLocation.getAbsolutePath());
        }
    }

    private static String[] getTestData(String param) {
        if (param.equalsIgnoreCase("init-ocr-basic")) {
            System.out.println("Getting basic ocr learning data(Croatian language files)");
            return testDataFilesBasic;
        }
        System.out.println("Getting full ocr learning data");
        return testDataFilesFull;
    }

    private static File getTestDataLocation() {
        try {
            return FileUtils.createDirectory("tessdata");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * Minimum set of files needed for Tesseract OCR to work
     **/
    private static final String[] testDataFilesBasic = {
            "LICENSE",
            "README.md",
            "configs",
            "pdf.ttf",
            "hrv.traineddata"
    };

    /****/
    private static final String[] testDataFilesFull = {
            "LICENSE",
            "README.md",
            "afr.traineddata",
            "amh.traineddata",
            "ara.traineddata",
            "asm.traineddata",
            "aze.traineddata",
            "aze_cyrl.traineddata",
            "bel.traineddata",
            "ben.traineddata",
            "bod.traineddata",
            "bos.traineddata",
            "bre.traineddata",
            "bul.traineddata",
            "cat.traineddata",
            "ceb.traineddata",
            "ces.traineddata",
            "chi_sim.traineddata",
            "chi_sim_vert.traineddata",
            "chi_tra.traineddata",
            "chi_tra_vert.traineddata",
            "chr.traineddata",
            "configs",
            "cos.traineddata",
            "cym.traineddata",
            "dan.traineddata",
            "deu.traineddata",
            "div.traineddata",
            "dzo.traineddata",
            "ell.traineddata",
            "eng.traineddata",
            "enm.traineddata",
            "epo.traineddata",
            "est.traineddata",
            "eus.traineddata",
            "fao.traineddata",
            "fas.traineddata",
            "fil.traineddata",
            "fin.traineddata",
            "fra.traineddata",
            "frk.traineddata",
            "frm.traineddata",
            "fry.traineddata",
            "gla.traineddata",
            "gle.traineddata",
            "glg.traineddata",
            "grc.traineddata",
            "guj.traineddata",
            "hat.traineddata",
            "heb.traineddata",
            "hin.traineddata",
            "hrv.traineddata",
            "hun.traineddata",
            "hye.traineddata",
            "iku.traineddata",
            "ind.traineddata",
            "isl.traineddata",
            "ita.traineddata",
            "ita_old.traineddata",
            "jav.traineddata",
            "jpn.traineddata",
            "jpn_vert.traineddata",
            "kan.traineddata",
            "kat.traineddata",
            "kat_old.traineddata",
            "kaz.traineddata",
            "khm.traineddata",
            "kir.traineddata",
            "kmr.traineddata",
            "kor.traineddata",
            "kor_vert.traineddata",
            "lao.traineddata",
            "lat.traineddata",
            "lav.traineddata",
            "lit.traineddata",
            "ltz.traineddata",
            "mal.traineddata",
            "mar.traineddata",
            "mkd.traineddata",
            "mlt.traineddata",
            "mon.traineddata",
            "mri.traineddata",
            "msa.traineddata",
            "mya.traineddata",
            "nep.traineddata",
            "nld.traineddata",
            "nor.traineddata",
            "oci.traineddata",
            "ori.traineddata",
            "osd.traineddata",
            "pan.traineddata",
            "pdf.ttf",
            "pol.traineddata",
            "por.traineddata",
            "pus.traineddata",
            "que.traineddata",
            "ron.traineddata",
            "rus.traineddata",
            "san.traineddata",
            "sin.traineddata",
            "slk.traineddata",
            "slv.traineddata",
            "snd.traineddata",
            "spa.traineddata",
            "spa_old.traineddata",
            "sqi.traineddata",
            "srp.traineddata",
            "srp_latn.traineddata",
            "sun.traineddata",
            "swa.traineddata",
            "swe.traineddata",
            "syr.traineddata",
            "tam.traineddata",
            "tat.traineddata",
            "tel.traineddata",
            "tgk.traineddata",
            "tha.traineddata",
            "tir.traineddata",
            "ton.traineddata",
            "tur.traineddata",
            "uig.traineddata",
            "ukr.traineddata",
            "urd.traineddata",
            "uzb.traineddata",
            "uzb_cyrl.traineddata",
            "vie.traineddata",
            "yid.traineddata",
            "yor.traineddata"
    };
}
