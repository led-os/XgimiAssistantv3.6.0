package com.xgimi.zhushou.qttx;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/11.
 */
public class QingTingListInfo {

      public ArrayList<Data> data;

      public class Data {
            public int chatgroup_id;
            public String description;
            public Detail detail;
            public int duration;
            public int id;
            public Mediainfo mediainfo;
            public int sequence;
            public String thumbs;
            public String title;
            public String type;
            public String update_time;
      }

      public class Mediainfo {
            public ArrayList<BitratesUrl> bitrates_url;
            public int duration;
            public int id;
      }
      public class Detail {
      }
      public class BitratesUrl {
            public int bitrate;
            public String file_path;
            public String qetag;
      }


}
