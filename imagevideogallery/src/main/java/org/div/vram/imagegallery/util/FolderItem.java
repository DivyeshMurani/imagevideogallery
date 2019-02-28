package org.div.vram.imagegallery.util;

import java.util.ArrayList;

public class FolderItem {
    public String path;
    private String name;

    public ArrayList<ImageItem> mImages = new ArrayList<>();

    public FolderItem(String path, String name) {
        this.path = path;
        this.name = name;
    }


    public void addImageItem(ImageItem imageItem) {
        this.mImages.add(imageItem);
     }

    public String getNumOfImages() {
        return String.format("%d", mImages.size());
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FolderItem{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", mImages=" + mImages +
                '}';
    }
}
