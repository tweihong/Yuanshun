package com.gogtz.yuanshun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gogtz.yuanshun.model.User;
import com.gogtz.yuanshun.util.HttpRequest;
import com.gogtz.yuanshun.util.SystemUiHider;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.manager.CacheManager;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.util.Log;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class LoginActivity extends BaseActivity implements HttpManager.OnHttpResponseListener {
    private static final String TAG = "LoginActivity";

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    /**
     * 启动这个Activity的Intent
     *
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this;
    }

    private long userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

    }


    //UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    Button btnSure;
    EditText phone;
    EditText code;

    @Override
    public void initView() {//必须调用
        super.initView();

        btnSure = (Button) findViewById(R.id.btnSure);
        phone = (EditText) findViewById(R.id.phone);
        code = (EditText) findViewById(R.id.code);
    }


    //UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    private User user;

    @Override
    public void initData() {//必须调用
        super.initData();

        user = CacheManager.getInstance().get(User.class, "user");
        if (user != null) {
            startActivity(ScanActivity.createIntent(LoginActivity.this));
            finish();
        }
    }


    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    public void initEvent() {//必须调用
        super.initEvent();

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone.getText() == null || "".equals(phone.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (code.getText() == null || "".equals(code.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 登录
                HttpRequest.login(phone.getText().toString(), code.getText().toString(), 100, LoginActivity.this);

            }
        });
    }

    //系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    //类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    //类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 登录回调方法
     *
     * @param requestCode 请求码，自定义，在发起请求的类中可以用requestCode来区分各个请求
     * @param resultJson  服务器返回的Json串
     * @param e           异常
     */
    @Override
    public void onHttpResponse(int requestCode, String resultJson, Exception e) {
        try {//如果服务器返回的json一定在最外层有个data，可以用OnHttpResponseListenerImpl解析
//            JSONObject jsonObject = new JSONObject(resultJson);
//            JSONObject data = jsonObject.getJSONObject("data");
            Toast.makeText(getActivity(), resultJson, Toast.LENGTH_LONG).show();

            user = new User();
            user.setPhone(phone.getText().toString());
            CacheManager.getInstance().save(User.class, user, "user");

            startActivity(ScanActivity.createIntent(LoginActivity.this));
            finish();

        } catch (Exception e1) {
            Log.e(TAG, "onHttpResponse  try { user = Json.parseObject(... >>" +
                    " } catch (JSONException e1) {\n" + e1.getMessage());
        }
    }

    //系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    //内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
