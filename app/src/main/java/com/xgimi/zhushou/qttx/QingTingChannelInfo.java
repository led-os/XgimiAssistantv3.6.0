package com.xgimi.zhushou.qttx;

import java.util.List;

public class QingTingChannelInfo {

      public int errorno;
      public String errormsg;
      public List<Data> data;

      public class Data {
            public int id;
            public String title;
            public String description;
            public String update_time;
            public int chatgroup_id;
            public Thumb thumbs;
            public int category_id;
            public String type;
            public int link_id;
            public int sale_type;
            public int auto_play;
            public int record_enabled;
            public String latest_program;
            public int star;
            public Detail detail;
      }

      public class Thumb {
            public String small_thumb;
            public String medium_thumb;
            public String large_thumb;
//            public String 200_thumb;
//            public String 400_thumb;
//            public String 800_thumb;
      }

      public class Detail {
            public int program_count;
      }

}
