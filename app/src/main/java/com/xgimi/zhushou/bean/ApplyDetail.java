package com.xgimi.zhushou.bean;

import java.util.List;

public class ApplyDetail {
public AppDetaillitem data;
    public static class AppDetaillitem{
        public String id;
        public String icon;
        public String title;
        public List<String> handler;
        public List<String> kind;
        public String version;
        public String description;
        public String publisher;
        public List<String> screenshots;
        public String download_url;
        public String update_time;
        public String file_size;
        public String package_name;
        public String version_code;
    }
}
