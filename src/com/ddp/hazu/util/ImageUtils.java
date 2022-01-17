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

import java.io.File;
import java.util.ArrayList;

public class ImageUtils {

    public static ArrayList<String> processImagesFromServer(ArrayList<String> bundle, File storage) {
        System.out.println("Starting download worker");
        ArrayList<String> storedImages = new ArrayList<String>();
        int counter = 0;
        long max = bundle.size();
        for (String imagePath : bundle) {
            System.out.println("Downloading (" + (counter+1) + "/" + max + ") " + imagePath);
            String fileName = NetworkUtils.getUrlParameterValue(imagePath, "pi") + ".jpg";
            byte[] imageRaw = NetworkUtils.getBinaryFileFromServer(imagePath);
            String imageStoredPath = FileUtils.writeBinaryToFile(fileName, storage.getAbsolutePath(), imageRaw);
            storedImages.add(imageStoredPath);
            counter++;
        }
        System.out.println("Job done. Downloaded " + counter + " pages");
        return storedImages;
    }

}
