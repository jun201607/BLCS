package blcs.lwb.utils;


import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import blcs.lwb.lwbtool.base.BaseApplication;
import blcs.lwb.lwbtool.utils.MultiLanguageUtils;
import blcs.lwb.lwbtool.utils.LinNotify;

public class MyApplication extends BaseApplication {


    public static MyApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initU_APP();
        //多语言设置
        registerActivityLifecycleCallbacks(MultiLanguageUtils.callbacks);
        //通知栏初始化
        LinNotify.setNotificationChannel(context);
    }

    public static  Context getContext(){
        return context;
    }
    /**
     * 友盟统计集成
     */
    private void initU_APP() {
//      appkey   channel  设备类型  推送业务的secret
        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE, null);
//      场景类型设置接口
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
//      secretkey设置接口，防止AppKey被盗用，secretkey需要网站申请 需要企业认证
//        MobclickAgent.setSecret(this, "s10bacedtyz");
//      错误统计  提交错误报告
//        MobclickAgent.reportError(mContext, "Parameter Error");
//      设置日志加密
        UMConfigure.setEncryptEnabled(true);
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        //系统语言等设置发生改变时会调用此方法，需要要重置app语言
//        super.attachBaseContext(MultiLanguageUtils.attachBaseContext(base));
//    }
}
