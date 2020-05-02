package com.example.tiongradua.graduationproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.HeadPicture.BaseActivity;
import com.example.tiongradua.graduationproject.HeadPicture.PhotoUtils;
import com.example.tiongradua.graduationproject.domain.MyUser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.widget.Toast.LENGTH_SHORT;

public class ImproveUserActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Spinner mSpinner;
    Button mButton;
    EditText mEt_name,mEt_introduce;
    RadioGroup mRadioFroup;
    RadioButton mRadio1,mRadio2;
    private ImageView photo;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis()+ ".jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis()+".jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private String[] spinnerDatalist={"学生党","上班族","全职父母","企业家"};
    private ArrayAdapter<String> adapter;
    String ItemValue,ItemValueSex="男";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improve_user);
        initView();
    }


    private void initView() {
        Bmob.initialize(this, "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化
        mSpinner = (Spinner)findViewById(R.id.Im_profession);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerDatalist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        mButton = (Button)findViewById(R.id.Im_button);
        mButton.setOnClickListener(this);

        mEt_name = (EditText)findViewById(R.id.Im_name);
        mEt_introduce = (EditText)findViewById(R.id.Im_introduce);

        mRadioFroup = (RadioGroup)findViewById(R.id.Im_RadioGroup);
        mRadio1 = (RadioButton)findViewById(R.id.Im_radio1);
        mRadio2 = (RadioButton)findViewById(R.id.Im_radio2);
        mRadioFroup.setOnCheckedChangeListener(this);

        photo = (ImageView)findViewById(R.id.photo);
        photo.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int output_X = 480, output_Y = 480;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.example.tiongradua.graduationproject.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        Toast.makeText(ImproveUserActivity.this, "设备没有SD卡!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        showImages(bitmap);
                    }
                    break;
            }
        }
    }

    private void showImages(Bitmap bitmap) {
            photo.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.photo){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String[] getPicture={"拍照","相册"};
            builder.setItems(getPicture, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            requestPermissions(ImproveUserActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                                @Override
                                public void granted() {
                                    if (hasSdcard()) {
                                        imageUri = Uri.fromFile(fileUri);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                            //通过FileProvider创建一个content类型的Uri
                                            imageUri = FileProvider.getUriForFile(ImproveUserActivity.this, "com.example.tiongradua.graduationproject.fileprovider", fileUri);
                                        PhotoUtils.takePicture(ImproveUserActivity.this, imageUri, CODE_CAMERA_REQUEST);
                                    } else {
                                        Toast.makeText(ImproveUserActivity.this, "设备没有SD卡！", Toast.LENGTH_SHORT).show();
                                        Log.e("asd", "设备没有SD卡");
                                    }
                                }

                                @Override
                                public void denied() {
                                    Toast.makeText(ImproveUserActivity.this, "部分权限获取失败，正常功能受到影响", Toast.LENGTH_LONG).show();
                                }
                            });
                           break;
                        case 1:
                            requestPermissions(ImproveUserActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                                @Override
                                public void granted() {
                                    PhotoUtils.openPic(ImproveUserActivity.this, CODE_GALLERY_REQUEST);
                                }

                                @Override
                                public void denied() {
                                    Toast.makeText(ImproveUserActivity.this, "部分权限获取失败，正常功能受到影响", Toast.LENGTH_LONG).show();
                                }
                            });
                            break;
                    }
                }
            });
            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();

        }
        if (v.getId() == R.id.Im_button){
                       String name = mEt_name.getText().toString();
                       String introduce = mEt_introduce.getText().toString();
                       BmobUser currentUser = BmobUser.getCurrentUser(BmobUser.class);
                       final MyUser myUser = new MyUser();
                       myUser.setUsername(currentUser.getUsername());
                       myUser.setName(name);
                       myUser.setSex(ItemValueSex);
                       myUser.setProfession(ItemValue);
                       myUser.setSpecialist(false);
                       myUser.setIntroduce(introduce);
                       myUser.setImage(fileCropUri);
                       myUser.save(new SaveListener<String>() {
                           @Override
                           public void done(String s, BmobException e) {
                               Toast.makeText(ImproveUserActivity.this,"信息完善成功", LENGTH_SHORT).show();
                               new Thread(new Runnable() {
                                   @Override
                                   public void run() {
                                       Intent intent = new Intent(ImproveUserActivity.this,MainActivity.class);
                                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                       intent.putExtra("flag",1);
                                       startActivity(intent);
                                       finish();
                                   }
                               }).start();
                           }
                       });

        }
    }

    private boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ItemValue = adapter.getItem(position);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (mRadio1.isChecked()){
            ItemValueSex = "男";
        }
        if (mRadio2.isChecked()){
            ItemValueSex = "女";
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
