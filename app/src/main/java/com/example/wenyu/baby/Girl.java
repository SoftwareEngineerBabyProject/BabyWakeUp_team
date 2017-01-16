package com.example.wenyu.baby;


import java.io.Serializable;

public class Girl implements Serializable {
    //初始設定
    //private static final long serialVersionUID = 8699489847426803789L;
    private int id;
    private String girlsPhotoPath = " ";
    private String girlRecordPath = " ";

    public Girl() {
        girlsPhotoPath = " ";
        girlRecordPath = " ";
        id=0;
    }

    public String getGirlsPhotoPath() {
        return girlsPhotoPath;
    }
    public void setGirlsPhotoPath(String girlsPhotoPath) {
        this.girlsPhotoPath = girlsPhotoPath;
    }
    public String getGirlRecordPath(){return girlRecordPath;}
    public void setGirlRecordPath(String girlRecordPath) { this.girlRecordPath = girlRecordPath; }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
