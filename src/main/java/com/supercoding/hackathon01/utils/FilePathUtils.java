package com.supercoding.hackathon01.utils;

import com.supercoding.hackathon01.enums.FilePath;

public class FilePathUtils {
    public static String convertImageUrlToFilePath(String imageUrl){
        return imageUrl.substring(
                imageUrl.indexOf(
                        FilePath.SEPARATE_POINT.getPath()) + 5,
                        imageUrl.length()
        );
    }
}
