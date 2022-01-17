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

import org.json.JSONArray;
import org.json.JSONObject;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class GeneralUtils {

    public static JSONArray parseDocumentMetadata(JSONObject documentObject) {
        return (JSONArray) documentObject.get("metadata");
    }

    /**
     * Parse document containing info needed for web page book renderer
     * Strange formatting..
     */
    public static ArrayList<String> parseDocumentForImages(JSONObject documentObject) {
        ArrayList<String> documentImages = new ArrayList<>();

        JSONArray sequencesArray = (JSONArray) documentObject.get("sequences");
        JSONObject metaObject = sequencesArray.getJSONObject(0);
        JSONArray canvasArray = metaObject.getJSONArray("canvases");

        for (int i = 0; i < canvasArray.length(); i++) {
            JSONObject imageObject = canvasArray.getJSONObject(i);
            JSONArray imagesArray = imageObject.getJSONArray("images");
            JSONObject resourceObjectHolder = imagesArray.getJSONObject(0); //
            JSONObject resourceObject = resourceObjectHolder.getJSONObject("resource");

            // get image data
            String imageFormat = resourceObject.getString("format");
            int imageWidth = resourceObject.getInt("width");
            int imageHeight = resourceObject.getInt("height");

            JSONObject service = resourceObject.getJSONObject("service");
            // get image url
            String imageUrl = service.getString("@id");

            System.out.println(i + ") " + imageUrl + " - " + imageWidth + "x" + imageHeight);
            documentImages.add(imageUrl);
        }

        return documentImages;
    }

    public static String generateMD5(String plaintext) {
        MessageDigest md = null;
        StringBuilder hash = new StringBuilder("0");
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(plaintext.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hash = new StringBuilder(bigInt.toString(16));
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hash.length() < 32) {
                hash.insert(0, "0");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }

    public static ArrayList<String> pullImagesFromMap(LinkedHashMap<String, String> pagesImagesMap) {
        ArrayList<String> listImages = new ArrayList<>();

        for (Map.Entry<String, String> entry : pagesImagesMap.entrySet()) {
            listImages.add(entry.getKey());
        }

        return listImages;
    }

    public static String getDocumentName(JSONArray responseMetadata) {
        for (Object object : responseMetadata) {
            JSONObject jObject = (JSONObject) object;

            String label = jObject.getString("label");
            if (label.equalsIgnoreCase("title")) {
                String docName = jObject.getString("value");
                docName = docName.substring(0, 10);
                return (docName);
            }
        }
        return "ExportedDocument";
    }
}
