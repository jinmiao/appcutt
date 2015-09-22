package com.appcutt.demo.fragment;

public interface IBaseFragment {

    /**
     * 当界面显示的时候调用，仅仅是显示时
     */
    void attachToWindow();

    /**
     * 当界面移走的时候调用
     */
    void detachToWindow();

    /**
     * 仅用于Tabs中的Fragment，点击Tab刷新
     */
    void onTabRefresh();

}
