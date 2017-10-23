package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.ApplyTitleDanLi;
import com.xgimi.zhushou.bean.CodeMsg;
import com.xgimi.zhushou.bean.Contory;
import com.xgimi.zhushou.bean.ContoryJson;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.inter.CommonCallBack;
import com.xgimi.zhushou.netUtil.HttpRequest;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.RegexUtils;
import com.xgimi.zhushou.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PhoneRegisterActivity extends BaseActivity implements OnTouchListener {


    private int jibu = 1;
    private EditText et;
    private Button zhuce;
    private String phoneNumber;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    Intent intent = new Intent(PhoneRegisterActivity.this, YanZhengMaActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_phone_register, null);
        setContentView(view);
        initView();
        initData();
    }

    private void initView() {
        random = new Random();

        iv_remount = (ImageView) findViewById(R.id.title).findViewById(R.id.iv_remount);
        setYaokongBackground(iv_remount, this, "qita");
        if (Constant.netStatus) {
            iv_remount.setImageResource(R.drawable.yaokongqi);
        } else {
            iv_remount.setImageResource(R.drawable.gimi_yaokong);
        }
        back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        back.setOnTouchListener(this);
        iv_remount.setOnTouchListener(this);
        TextView title = (TextView) findViewById(R.id.tv_titile);
        title.setText("注册");
        et = (EditText) findViewById(R.id.name);
        zhuce = (Button) findViewById(R.id.zhuce);
        quyu = (Button) findViewById(R.id.quyu);
        json = ContoryJson.getInstance(this).json;
        JsonJiXi();
        zhuce.setOnTouchListener(this);
    }

    ArrayList<Contory> contorys = new ArrayList<>();

    public void JsonJiXi() {
        try {
            Gson gson1 = new Gson();
            JSONObject js = new JSONObject(json);

            String js1 = js.getString("usual");
            List<Contory> list1 = new ArrayList<Contory>();
            list1 = gson1.fromJson(js1, new TypeToken<List<Contory>>() {
            }.getType());


            String js2 = js.getString("A");
            List<Contory> list2 = new ArrayList<Contory>();
            list2 = gson1.fromJson(js2, new TypeToken<List<Contory>>() {
            }.getType());

            String js3 = js.getString("B");
            List<Contory> list3 = new ArrayList<Contory>();
            list3 = gson1.fromJson(js3, new TypeToken<List<Contory>>() {
            }.getType());

            String js4 = js.getString("C");
            List<Contory> list4 = new ArrayList<Contory>();
            list4 = gson1.fromJson(js4, new TypeToken<List<Contory>>() {
            }.getType());

            String js5 = js.getString("D");
            List<Contory> list5 = new ArrayList<Contory>();
            list5 = gson1.fromJson(js5, new TypeToken<List<Contory>>() {
            }.getType());

            String js6 = js.getString("E");
            List<Contory> list6 = new ArrayList<Contory>();
            list6 = gson1.fromJson(js6, new TypeToken<List<Contory>>() {
            }.getType());

            String js7 = js.getString("F");
            List<Contory> list7 = new ArrayList<Contory>();
            list7 = gson1.fromJson(js7, new TypeToken<List<Contory>>() {
            }.getType());

            String js8 = js.getString("G");
            List<Contory> list8 = new ArrayList<Contory>();
            list8 = gson1.fromJson(js8, new TypeToken<List<Contory>>() {
            }.getType());

            String js9 = js.getString("H");
            List<Contory> list9 = new ArrayList<Contory>();
            list9 = gson1.fromJson(js9, new TypeToken<List<Contory>>() {
            }.getType());

            String js10 = js.getString("I");
            List<Contory> list10 = new ArrayList<Contory>();
            list10 = gson1.fromJson(js10, new TypeToken<List<Contory>>() {
            }.getType());

            String js11 = js.getString("J");
            List<Contory> list11 = new ArrayList<Contory>();
            list11 = gson1.fromJson(js11, new TypeToken<List<Contory>>() {
            }.getType());

            String js12 = js.getString("K");
            List<Contory> list12 = new ArrayList<Contory>();
            list12 = gson1.fromJson(js12, new TypeToken<List<Contory>>() {
            }.getType());

            String js13 = js.getString("L");
            List<Contory> list13 = new ArrayList<Contory>();
            list13 = gson1.fromJson(js13, new TypeToken<List<Contory>>() {
            }.getType());

            String js14 = js.getString("M");
            List<Contory> list14 = new ArrayList<Contory>();
            list14 = gson1.fromJson(js14, new TypeToken<List<Contory>>() {
            }.getType());

            String js15 = js.getString("N");
            List<Contory> list15 = new ArrayList<Contory>();
            list15 = gson1.fromJson(js15, new TypeToken<List<Contory>>() {
            }.getType());

            String js16 = js.getString("O");
            List<Contory> list16 = new ArrayList<Contory>();
            list16 = gson1.fromJson(js16, new TypeToken<List<Contory>>() {
            }.getType());

            String js17 = js.getString("P");
            List<Contory> list17 = new ArrayList<Contory>();
            list17 = gson1.fromJson(js17, new TypeToken<List<Contory>>() {
            }.getType());

            String js18 = js.getString("Q");
            List<Contory> list18 = new ArrayList<Contory>();
            list18 = gson1.fromJson(js18, new TypeToken<List<Contory>>() {
            }.getType());

            String js19 = js.getString("R");
            List<Contory> list19 = new ArrayList<Contory>();
            list19 = gson1.fromJson(js19, new TypeToken<List<Contory>>() {
            }.getType());


            String js20 = js.getString("S");
            List<Contory> list20 = new ArrayList<Contory>();
            list20 = gson1.fromJson(js20, new TypeToken<List<Contory>>() {
            }.getType());

            String js21 = js.getString("T");
            List<Contory> list21 = new ArrayList<Contory>();
            list21 = gson1.fromJson(js21, new TypeToken<List<Contory>>() {
            }.getType());


            String js22 = js.getString("U");
            List<Contory> list22 = new ArrayList<Contory>();
            list22 = gson1.fromJson(js22, new TypeToken<List<Contory>>() {
            }.getType());

            String js23 = js.getString("V");
            List<Contory> list23 = new ArrayList<Contory>();
            list23 = gson1.fromJson(js23, new TypeToken<List<Contory>>() {
            }.getType());

//			String js24=js.getString("W");
//			if(js24!=null){
//			List<Contory> list24 = new ArrayList<Contory>();
//
//			list24=gson1.fromJson(js24, new TypeToken<List<Contory>>(){}.getType());
//			}

			/*String js25=js.getString("X");
            List<Contory> list25 = new ArrayList<Contory>();
			list25=gson1.fromJson(js25, new TypeToken<List<Contory>>(){}.getType());*/

            String js26 = js.getString("Y");
            List<Contory> list26 = new ArrayList<Contory>();
            list26 = gson1.fromJson(js26, new TypeToken<List<Contory>>() {
            }.getType());

            String js27 = js.getString("Z");
            List<Contory> list27 = new ArrayList<Contory>();
            list27 = gson1.fromJson(js27, new TypeToken<List<Contory>>() {
            }.getType());

            contorys.addAll(list1);
            contorys.addAll(list2);
            contorys.addAll(list3);
            contorys.addAll(list4);
            contorys.addAll(list5);
            contorys.addAll(list6);
            contorys.addAll(list7);
            contorys.addAll(list8);
            contorys.addAll(list9);
            contorys.addAll(list10);
            contorys.addAll(list11);
            contorys.addAll(list12);
            contorys.addAll(list13);
            contorys.addAll(list14);
            contorys.addAll(list15);
            contorys.addAll(list16);
            contorys.addAll(list17);
            contorys.addAll(list18);
            contorys.addAll(list19);
            contorys.addAll(list20);
            contorys.addAll(list21);
            contorys.addAll(list22);
            contorys.addAll(list23);
//			contorys.addAll(list24);
//			contorys.addAll(list25);
            contorys.addAll(list26);
            contorys.addAll(list27);
            Log.e("info", contorys.size() + "");
            for (int i = 0; i < contorys.size(); i++) {

                Log.e("info", contorys.get(i).country + "--");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initData() {
        quyu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showPop();
            }
        });
        zhuce.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDilog("获取中...");
                String edittextString = et.getText().toString().trim();
                getYanZhengMa(edittextString);
            }
        });
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                String edittextString = et.getText().toString().trim();
                if (edittextString.length() > 9) {
//					getYanZhengMa(edittextString);
                    zhuce.setClickable(true);
                    zhuce.setBackgroundResource(R.drawable.logo);
                } else if (!RegexUtils.checkMobile(edittextString)) {
                    zhuce.setClickable(false);
                    zhuce.setBackgroundResource(R.drawable.weixuanzhonglogo);
                }
            }
        });
    }

    public void getYanZhengMa(final String tel) {
        HttpRequest.getInstance(this).getZhuCeYanZhengMa(tel, new CommonCallBack<CodeMsg>() {

            @Override
            public void onSuccess(CodeMsg data) {
                // TODO Auto-generated method stub
                MissDilog();
                if (data.code == 500) {
//                    Toast.makeText(PhoneRegisterActivity.this, data.message, Toast.LENGTH_SHORT).show();
                    ToastUtil.getToast(data.message, PhoneRegisterActivity.this).show();
                } else if (data.code == 200) {
                    ApplyTitleDanLi.getInstance().phoneNumber = et.getText().toString().trim();
                    mHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFailed(String reason) {
                // TODO Auto-generated method stub
                MissDilog();
//				Toast.makeText(PhoneRegisterActivity.this, "服务器出错,请重新发送", Toast.LENGTH_SHORT).show();

            }
        });
    }

    GimiUser user;
    private TextView one;
    private TextView two;
    private TextView three;
    private Random random;
    private Button quyu;
    private View view;
    private ImageView back;
    private ImageView iv_remount;
    private String json;
    private ContryAdapter adapter;

    //zhuce
    public void zhuce(String tel, String yanzhengma) {
        HttpRequest.getInstance(this).getUserZhuCe(this, tel, yanzhengma, new CommonCallBack<GimiUser>() {

            @Override
            public void onSuccess(GimiUser data) {
                // TODO Auto-generated method stub
                MissDilog();

                if (data.code == 200) {

                } else if (data.code == 500) {
//                    Toast.makeText(PhoneRegisterActivity.this, "验证码失效", Toast.LENGTH_SHORT).show();
                    ToastUtil.getToast("验证码失效", PhoneRegisterActivity.this).show();
                }
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFailed(String reason) {
                // TODO Auto-generated method stub
                MissDilog();
//                Toast.makeText(PhoneRegisterActivity.this, reason, Toast.LENGTH_SHORT).show();
                ToastUtil.getToast(reason, PhoneRegisterActivity.this).show();
            }
        });
    }

    //设置用户信息
    public void setUserInformation(String uid, String username, String password) {
        HttpRequest.getInstance(this).setUserInforMation(uid, username, password, new CommonCallBack<CodeMsg>() {
            @Override
            public void onSuccess(CodeMsg data) {
                // TODO Auto-generated method stub
                MissDilog();
//				Intent Intent=new Intent(PhoneRegisterActivity.this,FindFragment.class);
//				startActivity(Intent);
                finish();
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFailed(String reason) {
                // TODO Auto-generated method stub

            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String obj = (String) msg.obj;
            quyu.setText("+" + obj);

        }
    };

    public void showPop() {
        View view1 = View.inflate(this, R.layout.quyu, null);
        final PopupWindow pop = new PopupWindow(view1, width, height
                - getStatusBarHeight());

        ListView listview = (ListView) view1.findViewById(R.id.listview);
        adapter = new ContryAdapter();
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Contory item = adapter.getItem(position);
                Message mesage = handler.obtainMessage();
                mesage.obj = item.code;
                handler.sendMessage(mesage);
                pop.dismiss();
            }
        });
        ImageView iv_remount = (ImageView) view1.findViewById(R.id.title).findViewById(R.id.iv_remount);

        iv_remount.setVisibility(View.GONE);
        ImageView back = (ImageView) view1.findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        TextView title = (TextView) view1.findViewById(R.id.tv_titile);
        title.setText("选择国家和区号");

        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);

        pop.setAnimationStyle(R.style.pull_popup_anim);
        // pop.showAtLocation(view, Gravity.CENTER, 0, 0);
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]);

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        Log.i("info", result + "ccc");
        return result;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.back:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setAlpha(0.6f);
                        break;
                    case MotionEvent.ACTION_UP:
                        back.setAlpha(1.0f);
                        break;
                }
                break;
            case R.id.iv_remount:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        iv_remount.setAlpha(0.6f);
                        break;
                    case MotionEvent.ACTION_UP:
                        iv_remount.setAlpha(1.0f);
                        break;
                }
                break;
            case R.id.zhuce:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        zhuce.setAlpha(0.6f);
                        break;
                    case MotionEvent.ACTION_UP:
                        zhuce.setAlpha(1.0f);
                        break;
                }
                break;
        }
        return false;
    }


    class ContryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return contorys.size();
        }

        @Override
        public Contory getItem(int position) {
            // TODO Auto-generated method stub
            return contorys.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View view2 = convertView;
            ViewHolder vh = null;
            if (view2 == null) {
                vh = new ViewHolder();
                view2 = View.inflate(PhoneRegisterActivity.this, R.layout.contorys_adapter, null);
                vh.tv1 = (TextView) view2.findViewById(R.id.tv1);
                vh.tv2 = (TextView) view2.findViewById(R.id.tv2);
                view2.setTag(vh);
            } else {
                vh = (ViewHolder) view2.getTag();
            }
            Contory con = contorys.get(position);
            vh.tv1.setText(con.country + "");
            vh.tv2.setText(con.code + "");
            return view2;
        }

        class ViewHolder {
            public TextView tv1;
            public TextView tv2;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.netStatus) {
            iv_remount.setImageResource(R.drawable.yaokongqi);
        } else {
            iv_remount.setImageResource(R.drawable.gimi_yaokong);

        }
    }
}
