
/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.orzangleli.radar.utils;

import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import java.io.IOException;
import java.util.Optional;

/**
 * Resource related utils.
 */
public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * get the path from id.
     *
     * @param context the context
     * @param id      the id
     * @return the path from id
     */
    public static String getPathById(Context context, int id) {
        if (context != null) {
            ResourceManager manager = context.getResourceManager();
            if (manager != null) {
                try {
                    return manager.getMediaPath(id);
                } catch (IOException | NotExistException | WrongTypeException e) {
                    return "";
                }
            }
        }
        return "";
    }

    /**
     * get the pixel map.
     *
     * @param context the context
     * @param id      the id
     * @return the pixel map
     */
    public static Optional<PixelMap> getPixelMap(Context context, int id) {
        String path = getPathById(context, id);
        if (path == null || path.length() == 0) {
            return Optional.empty();
        }

        if (context == null) {
            return Optional.empty();
        }

        RawFileEntry assetManager = context.getResourceManager().getRawFileEntry(path);
        ImageSource.SourceOptions options = new ImageSource.SourceOptions();
        options.formatHint = "image/png";
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        try {
            Resource asset = assetManager.openRawFile();
            ImageSource source = ImageSource.create(asset, options);
            return Optional.ofNullable(source.createPixelmap(decodingOptions));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
