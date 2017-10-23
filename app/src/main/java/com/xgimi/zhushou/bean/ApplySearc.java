package com.xgimi.zhushou.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 霍长江 on 2016/8/18.
 */
public class ApplySearc {
    public List<ApplySearchItem> data = new ArrayList<>();
    public static class ApplySearchItem{
        public String id;
        public String title;
        public String icon;
        public String is_game;
        public String version;
        public int plays;
        public String file_size;
        public List<String> screenshots;
        public String download_url;
        public String package_name;
        public boolean is_install;

        public boolean is_install() {
            return is_install;
        }

        public void setIs_install(boolean is_install) {
            this.is_install = is_install;
        }
    }
}
