package com.deveu.copus.app.Datas;

public class slide {
    private String Image;
    private String Title;

    public slide(String image, String title) {
        Image = image;
        Title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }
}
