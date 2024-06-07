package com.example.tugasrecyclerview;

public class ItemList {
    private String judul;
    private String subJudul;
    private String imageUrl;

    public ItemList(String judul, String subJudul, String imageUrl) {
        this.judul = judul;
        this.subJudul = subJudul;
        this.imageUrl = imageUrl;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getSubJudul() {
        return subJudul;
    }

    public void setSubJudul(String subJudul) {
        this.subJudul = subJudul;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
