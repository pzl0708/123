package com.example.pzl.trying.change;

import com.example.pzl.trying.primary.ICommonModel;
import com.example.pzl.trying.primary.ICommonView;
import com.example.pzl.trying.primary.MyServer;
import com.example.pzl.trying.primary.config.IntentConfig;
import com.example.pzl.trying.primary.utils.BaseObserver;
import com.example.pzl.trying.primary.utils.HttpUtils;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterModel implements ICommonModel {

    private MyServer mMyServer;

    @Override
    public void getData(ICommonView commonView, int api, int intent, String... params) {
        mMyServer = HttpUtils.getInstance().getServer();
        if (intent == IntentConfig.LOAD)setUserImage(commonView,api,intent,params);
    }

    private void setUserImage(final ICommonView commonView, final int api, final int intent, String[] params) {
        File file = new File(params[0]);
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId","fb0327c3c8154d829f11c99db9f3eba3")
                .addFormDataPart("headImageFile",file.getName(),RequestBody.create(MediaType.parse("image/*"),file))
                .build();
        mMyServer.setUserImage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(){
                    @Override
                    public void onNext(Object value) {
                        super.onNext(value);
                        commonView.getList(value,api,intent);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        commonView.onCompleted(api);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        commonView.onError(e,api);
                    }
                });
    }
}
