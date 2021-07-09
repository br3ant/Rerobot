package com.bayi.rerobot.mvp.presenter;


import com.bayi.rerobot.mvp.view.BaseView;

/**
 * Created by tzn
 * on 2019/4/15
 */
public class BasePresenter<V extends BaseView>{
    protected V mView=null;
    /**
     * 绑定view，一般在初始化中调用该方法
     *
     * @param view view
     */
    public void attachView(V view) {
        this.mView = view;
    }
    /**
     * 解除绑定view，一般在onDestroy中调用
     */
    public void detachView() {
        this.mView = null;
    }
    public boolean isViewAttached() {
        return mView != null;
    }
}
