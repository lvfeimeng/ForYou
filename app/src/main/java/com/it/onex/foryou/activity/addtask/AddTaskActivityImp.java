package com.it.onex.foryou.activity.addtask;

import com.it.onex.foryou.base.BasePresenter;
import com.it.onex.foryou.bean.AddToDoDetail;
import com.it.onex.foryou.bean.DataResponse;
import com.it.onex.foryou.constant.Constant;
import com.it.onex.foryou.net.ApiService;
import com.it.onex.foryou.net.RetrofitManager;
import com.it.onex.foryou.utils.RxSchedulers;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by OnexZgj on 2018/8/20:11:34.
 * des:
 */

public class AddTaskActivityImp extends BasePresenter<AddTaskActivityContract.View> implements AddTaskActivityContract.Presenter {

    @Inject
    public AddTaskActivityImp() {

    }


    @Override
    public void addTask(String title, String content, String date, String state) {
        RetrofitManager.create(ApiService.class).addTask(title,content,date,state)
                .compose(RxSchedulers.<DataResponse<AddToDoDetail>>applySchedulers())
                .compose(mView.<DataResponse<AddToDoDetail>>bindToLife())
                .subscribe(new Consumer<DataResponse<AddToDoDetail>>() {
                    @Override
                    public void accept(DataResponse<AddToDoDetail> dataResponse) throws Exception {


                        if (dataResponse.getErrorCode() != 0) {
                            mView.showAddTaskSuccess();
                        } else {
                            mView.showFaild(dataResponse.getErrorMsg());
                            if (dataResponse.getErrorMsg().equals(Constant.LOGIN_WARN)) {
                                mView.jumpToLogin();
                            }
                        }
                        mView.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showFaild("添加待办失败,请重试...");
                        mView.hideLoading();
                    }
                });
    }
}