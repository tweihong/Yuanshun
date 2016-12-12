/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.gogtz.yuanshun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gogtz.yuanshun.util.HttpRequest;
import com.gogtz.yuanshun.zxing.ScanListener;
import com.gogtz.yuanshun.zxing.ScanManager;
import com.gogtz.yuanshun.zxing.decode.DecodeThread;
import com.google.zxing.Result;

import butterknife.Bind;
import butterknife.ButterKnife;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.TopMenuWindow;
import zuo.biao.library.util.Log;

/**
 * 扫描二维码Activity
 *
 * @author Lemon
 * @use toActivity(ScanActivity.createIntent(...));
 */
public class ScanActivity extends BaseActivity implements ScanListener, View.OnClickListener, HttpManager.OnHttpResponseListener, AlertDialog.OnDialogButtonClickListener {
    public static final String TAG = "ScanActivity";

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 启动这个Activity的Intent
     *
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, ScanActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void onResume() {
        super.onResume();
        scanManager.onResume();
        rescan.setVisibility(View.GONE);
        scan_image.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        scanManager.onPause();
    }

    //UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    SurfaceView scanPreview = null;
    View scanContainer;
    View scanCropView;
    ImageView scanLine;
    ScanManager scanManager;

    @Bind(R.id.service_register_rescan)
    Button rescan;
    @Bind(R.id.scan_image)
    ImageView scan_image;
    @Bind(R.id.ivCameraScanUpdate)
    TextView ivCameraScanUpdate;
    @Bind(R.id.ivCameraScanLight)
    TextView ivCameraScanLight;
    @Bind(R.id.ivCameraScanPush)
    TextView ivCameraScanPush;

    @Override
    public void initView() {//必须调用
        super.initView();
        ButterKnife.bind(getActivity());

        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = findViewById(R.id.capture_container);
        scanCropView = findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        //构造出扫描管理器
        scanManager = new ScanManager(this, scanPreview, scanContainer, scanCropView, scanLine, DecodeThread.ALL_MODE, this);
    }

    //UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    public void initData() {//必须调用
        super.initData();
    }
    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    public void initEvent() {//必须调用
        super.initEvent();
        ivCameraScanPush.setOnClickListener(this);
        ivCameraScanLight.setOnClickListener(this);
        ivCameraScanUpdate.setOnClickListener(this);
        rescan.setOnClickListener(this);
    }


    @Override
    public void onReturnClick(View v) {
        showShortToast("tui");
        new AlertDialog(context, "退出登录", "确定退出登录？", true, 0, this).show();
    }

    @Override
    public void onForwardClick(View v) {
//		CommonUtil.toActivity(context, QRCodeActivity.createIntent(context, 1));
        //showTopMenu();
        showHistory();
    }

    //系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCameraScanUpdate:
                updateDataFromServer();
                break;
            case R.id.ivCameraScanLight:
                scanManager.switchLight();
                break;
            case R.id.ivCameraScanPush:
                Toast.makeText(getActivity(), "ivCameraScanPush", Toast.LENGTH_SHORT).show();
                break;
            case R.id.service_register_rescan://再次开启扫描
                startScan();
                break;
            default:
                break;
        }
    }

    //类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public static final int REQUEST_TO_CAMERA_SCAN = 22;
    private static final int REQUEST_TO_TOP_MENU = 30;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_TO_CAMERA_SCAN:
                if (data != null) {
//					String result = data.getStringExtra(ScanActivity.RESULT_QRCODE_STRING);
//
//					Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
//
//					CommonUtil.copyText(context, result);
//				toActivity(WebViewActivity.createIntent(context, "扫描结果", result));
//					CommonUtil.openWebSite(context, result);

                }
                break;
            case REQUEST_TO_TOP_MENU:
                if (data != null) {
                    switch (data.getIntExtra(TopMenuWindow.RESULT_POSITION, -1)) {
                        case 0:
                            updateDataFromServer();
                            break;
                        case 1:
                            sendOfflineData();
                            break;
                        default:
                            showHistory();
                            break;
                    }
                }
                break;
            default:
                break;
        }

    }
    //类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 显示顶部菜单
     */
    private void showTopMenu() {
        toActivity(TopMenuWindow.createIntent(context, new String[]{"同步数据", "上传离线数据", "历史扫码记录"}), REQUEST_TO_TOP_MENU, false);
    }

    // 跳转到同步数据画面
    private void updateDataFromServer() {
        // 显示加载对话框
        showProgressDialog(R.string.updating);
        // 从服务器请求数据
        HttpRequest.getGpsInfo(100, ScanActivity.this);

    }

    // 跳转到上传离线数据画面
    private void sendOfflineData() {
        // 显示加载对话框
        showProgressDialog(R.string.sending);
        // 从服务器请求数据
        HttpRequest.getGpsInfo(100, ScanActivity.this);
    }

    // 跳转到历史数据画面
    private void showHistory() {
        toActivity(ListActivity.createIntent(context, 1), REQUEST_TO_CAMERA_SCAN, false);
    }

    /**
     * 扫码结果
     *
     * @param rawResult 结果对象
     * @param bundle    存放了截图，或者是空的
     */
    @Override
    public void scanResult(Result rawResult, Bundle bundle) {
//扫描成功后，扫描器不会再连续扫描，如需连续扫描，调用reScan()方法。
        //scanManager.reScan();
//		Toast.makeText(that, "result="+rawResult.getText(), Toast.LENGTH_LONG).show();

        showShortToast(rawResult.getText());

        if (!scanManager.isScanning()) { //如果当前不是在扫描状态
            //设置再次扫描按钮出现
            rescan.setVisibility(View.VISIBLE);
            scan_image.setVisibility(View.VISIBLE);
            Bitmap barcode = null;
            byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
            }
            scan_image.setImageBitmap(barcode);
        }
        rescan.setVisibility(View.VISIBLE);
        scan_image.setVisibility(View.VISIBLE);
//        Toast.makeText(getActivity(), rawResult.getText(), Toast.LENGTH_SHORT).show();
    }

    public void startScan() {
        if (rescan.isClickable()) {
            rescan.setVisibility(View.GONE);
            scan_image.setVisibility(View.GONE);
            scanManager.reScan();
        }
    }

    @Override
    public void scanError(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        //相机扫描出错时
        if (e.getMessage() != null && e.getMessage().startsWith("相机")) {
            scanPreview.setVisibility(View.INVISIBLE);
        }
    }


    //内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    public void onHttpResponse(int requestCode, String resultJson, Exception e) {
        try {//如果服务器返回的json一定在最外层有个data，可以用OnHttpResponseListenerImpl解析
//            JSONObject jsonObject = new JSONObject(resultJson);
//            JSONObject data = jsonObject.getJSONObject("data");

            super.showShortToast(resultJson, true);
            // 隐藏加载框
            dismissProgressDialog();
        } catch (Exception e1) {
            Log.e(TAG, "onHttpResponse  try { user = Json.parseObject(... >>" +
                    " } catch (JSONException e1) {\n" + e1.getMessage());
        }
    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {
        if (! isPositive) {
            return;
        }

        switch (requestCode) {
            case 0:
                logout();
                break;
            default:
                break;
        }
    }

    private void logout() {
        context.finish();
    }

    //内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}