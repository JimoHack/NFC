package com.nfctool.maintenancesystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;g
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnNdefPushCompleteCallback,CreateNdefMessageCallback{
    private String TAG = "MainActivity-DEBUG" ;
    private NfcAdapter mAdapter;
    private TextView mText;
    private ScrollView mScrollView ;
    private Button btSearch,btSelect,btDownload ;
    private ListView mList;
    private NdefMessage mMessage;
    private static final int MSG_1 = 1,MSG_2 = 2,MSG_3 = 3,MSG_4 = 4 ;
    List<Map<String,Object>> listData ;
    ArrayList<File> files ;

    private final Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) { ///异步刷新GUI
            switch (msg.what) {
                case MSG_1: //正在搜索时显示文字
                    printCmdln("正在搜索所有HEX文件......");
                    break;
                case MSG_2: ///搜索完成,把搜索到的文件添加到list控件中
                    print("共搜索到");
                    print(String.valueOf(files.size()));
                    println("个文件:");
                    listData = new ArrayList();
                    for (int i = 0; i < files.size(); ++i) {
                        File file = files.get(i);
                        if (file != null) {
                            println(file.getPath());
                            long time = file.lastModified();
                            print("\t\t\t\t\t<最后修改时间:");
                            String TimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
                            Map<String,Object> mpBuf = new HashMap() ;
                            mpBuf.put("path",file) ;
                            mpBuf.put("date",TimeStr) ;
                            mpBuf.put("icon",R.drawable.ic_file) ;
                            listData.add(mpBuf) ;
                            print(TimeStr);
                            println(">");
                        }
                    }
                    printCmdln("搜索文件完成!");
                    if (files.size() != 0) {
                        mList.setAdapter(new SimpleAdapter(MainActivity.this, listData, R.layout.list_item, new String[]{"path", "date", "icon"}, new int[]{R.id.text_filepath, R.id.text_filedate, R.id.image_image}));
                    }
                    break;
                case MSG_3: ///发送消息完成

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("消息")
                            .setMessage("发送成功!")
                            .setPositiveButton("确定", null)
                            .show();
                    Log.d(TAG,"Sended!!") ;
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressLint("NewApi")
    private void requestReadExternalPermission() { /// 获取读文件的权限
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "READ permission IS NOT granted...");
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            Log.d(TAG, "READ permission is granted...");
        }
    }

    @SuppressLint("NewApi")
    private void requestWriteExternalPermission() { /// 获取写文件的权限
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "WRITE permission IS NOT granted...");
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            Log.d(TAG, "WRITE permission is granted...");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        setContentView(R.layout.activity_main);
        mText = findViewById(R.id.text_command);
        mScrollView = findViewById(R.id.text_command_ScrollView) ;
        mList = findViewById(R.id.list_list) ;
        btSearch = findViewById(R.id.button_search) ;
        btDownload = findViewById(R.id.button_download) ;
        btSelect = findViewById(R.id.button_select) ;
        mText.setMovementMethod(ScrollingMovementMethod.getInstance());
        btSelect.setOnClickListener(MainActivity.this);
        btDownload.setOnClickListener(MainActivity.this) ;
        btSearch.setOnClickListener(MainActivity.this);
        if (mAdapter != null) {
            mAdapter.setOnNdefPushCompleteCallback(this, this);
            mAdapter.setNdefPushMessageCallback(this, this); // 注册NDEF回调消息
            if(mAdapter.isEnabled()) {
                mText.setText("请把PN5352芯片靠近手机,手机会发送'Hello,Word!'到芯片!\r\n");
            } else {
                mText.setText("Error(1): 手机NFC功能已关闭,请手动打开NFC功能!\r\n");
            }
        } else {
            mText.setText("Error(0): 该手机不支持NFC功能!\r\n");
        }
        PackageManager packageManager = this.getPackageManager();
        boolean isBeamOpen = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION);
        mText.append(isBeamOpen?"手机支持AndroidBeam!\r\n":"Error(2): 手机不支持AndroidBeam!\r\n");
        requestReadExternalPermission()  ; /// 获取读文件的权限
        requestWriteExternalPermission() ; /// 获取写文件的权限
        Log.d(TAG,"Created!!!") ;
    }

    private void print(String str) {
        mText.append(str) ;
        updateList() ;
    }

    private void println(String str) {
        mText.append(str) ;
        mText.append("\r\n");
        updateList() ;
    }

    private void printCmdln(String str) {
        mText.append("\r\n\t\t>>") ;
        mText.append(str);
        mText.append("\r\n\r\n");
        updateList() ;
    }

    private void updateList() {
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private ArrayList<File> SearchFile(String filename,File fFileName) { ///dfs搜索文件
        ArrayList<File> FileName  = new ArrayList<>() ;
        try{
            File[] files = fFileName.listFiles();
            if(files.length > 0) {
                for(int i = 0;i < files.length; ++i) {
                    if(!files[i].isDirectory()) {
                        String name = files[i].getName() ;
                        if(name.length() >= 4 && name.substring(name.length()-4).toUpperCase().compareTo(".HEX") == 0)
                        {
                            FileName.add(files[i]) ;
                        }
                    } else{
                        FileName.addAll(SearchFile(filename,files[i])) ;
                        ///Log.d(TAG,files[i].getPath()) ;
                    }
                }
            }
        } catch (Exception e) {

        }
        return FileName ;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        println("Pressed!!!");
        switch (v.getId()){
            case R.id.button_search: {
                new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        //处理耗时逻辑
                        Message msg1 = new Message(),msg2 = new Message();
                        msg1.what = MSG_1 ; handler.sendMessage(msg1);
                        files = SearchFile(".hex", new File("/sdcard/"));
                        Log.d(TAG,"files count == " + files.size()) ;
                        files.sort(
                                new Comparator<File>() {
                                    @Override
                                    public int compare(File arg0, File arg1) {
                                        return (arg0.lastModified() < arg1.lastModified()) ? 1 : -1;
                                    }
                                }
                        );
                        msg2.what = MSG_2 ; handler.sendMessage(msg2);
                    }
                }).start();
                break;
            }
            case R.id.button_download:

                break ;
            case R.id.button_select:

                break ;
            default: break ;
        }
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        Message msgSended = new Message();
        msgSended.what = MSG_3 ;
        handler.sendMessage(msgSended) ;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        int len = 110 ;
        byte[] data = new byte[len] ;
        for(int i = 0;i<len;++i) data[i] = (byte)((i%10!=0)?i%10+1:'_');
        mMessage = new NdefMessage(
                new NdefRecord[] {NdefRecord.createExternal("_","+",data)});
        return mMessage ;
    }
}
