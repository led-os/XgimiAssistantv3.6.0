package com.xgimi.zhushou.aes;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by XGIMI on 2017/7/11.
 */

public class MyAesToGsonFactory extends Converter.Factory {

    private Gson gson;
    private static Class mClass;

    public MyAesToGsonFactory(Gson gson) {
        this.gson = gson;
    }

    public static MyAesToGsonFactory create(Class aClass) {
        mClass = aClass;
        return create(new Gson());
    }

    private static MyAesToGsonFactory create(Gson gson) {
        return new MyAesToGsonFactory(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter adapter = gson.getAdapter(TypeToken.get(type));
        return new JsonAesResponseBody(gson, adapter, mClass);
    }
}
