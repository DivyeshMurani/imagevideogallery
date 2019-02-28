package org.div.vram.imagegallery.util;

import java.util.ArrayList;
import java.util.List;

public class ImageItem {

    public static final int DATE = 0;
    public static final int IMAGE = 1;

    private String path;
    private String name;
    private String buck;
    private long time;

    private int type;

    public ImageItem() {

    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }



    public static List<ImageItem> ALL_IMAGES = new ArrayList<>();

    public ArrayList<ImageItem> mImages = new ArrayList<>();

    public ImageItem(String path, String name, String buck, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
        this.buck = buck;
    }

    public void setBuck(String buck) {
        this.buck = buck;
    }

    public String getBuck() {
        return buck;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static void addAllImages(ImageItem item) {
        ALL_IMAGES.add(item);
    }

    @Override
    public String toString() {
        return "ImageItem{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", buck='" + buck + '\'' +
                ", time=" + time +
                '}';
    }
}
