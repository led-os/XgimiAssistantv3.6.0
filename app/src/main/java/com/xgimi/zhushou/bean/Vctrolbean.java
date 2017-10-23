package com.xgimi.zhushou.bean;

/**
 * Created by 霍长江 on 2016/9/9.
 */
public class Vctrolbean {

   
    public DataBean data;
   

    public int code;
    public String message;

    

    public static class DataBean {
        public String package_name;
        public String version_code;
        public String download_url;
        public String gimi_device;
        public String file_md5;

     
    }
}
