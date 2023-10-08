package com.supercoding.hackathon01.enums;

public enum FilePath {
    USER_DIR("user/"),
    CONTRACT_DIR("contract/"),
    SEPARATE_POINT(".com/"),
    HOME_DIR("home/"),
    MENU_DIR("menu/"),
    REVIEW_DIR("review/");

    FilePath(String path) {
        this.path = path;
    }

    private final String path;

    public String getPath() {
        return path;
    }
}
