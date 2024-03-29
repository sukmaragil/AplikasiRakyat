//package com.example.user.aplikasirakyat.activity;
//
//import android.Manifest;
//import android.app.DatePickerDialog;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.design.widget.TextInputLayout;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.example.user.aplikasirakyat.Config;
//import com.example.user.aplikasirakyat.R;
//import com.kosalgeek.genasync12.AsyncResponse;
//import com.kosalgeek.genasync12.EachExceptionsHandler;
//import com.kosalgeek.genasync12.PostResponseAsyncTask;
//
//import net.gotev.uploadservice.MultipartUploadRequest;
//import net.gotev.uploadservice.UploadNotificationConfig;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.UUID;
//
///**
// * Created by USER on 20/04/2016.
// */
//public class PermohonanActivityOld extends AppCompatActivity implements View.OnClickListener {
//
//    // declare input atribut and button to select
//    private EditText editTextNik, editTextKeperluan;
//    private EditText editTextEmail;
//    private Button buttonSubmit;
//
//    final String TAG = this.getClass().getName();
//    private ImageView ivImage;
//    private ImageView ivImage2;
//    private TextInputLayout input_layout_nik, input_layout_email, input_layout_keperluan_belum_memiliki_rumah;
//
//    private final int GALLERY_REQUEST = 123;
//    private final int GALLERY_REQUEST2 = 312;
//
//    private static final int STORAGE_PERMISSION_CODE = 123;
//    private String nik, email, keperluan;
//
//    private Bitmap bitmap, bitmap2;
//    private Uri filePath, filePath2;
//
//    private ProgressDialog loading;
//
//    private EditText date;
//    DatePickerDialog datePickerDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_permohonan_old);
//
//        editTextNik = (EditText)findViewById(R.id.editTextNik);
//        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
//        editTextKeperluan =  (EditText)findViewById(R.id.editTextKeperluan);
//        buttonSubmit = (Button)findViewById(R.id.buttonSubmit);
//        date = (EditText) findViewById(R.id.birthday);
//
//        date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //calendar classes instance and get current date
//                final Calendar calendar = Calendar.getInstance();
//                int mYear = calendar.get(Calendar.YEAR); // current year
//                int mMonth = calendar.get(Calendar.MONTH); // current month
//                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
//                // date picker dialog
//                datePickerDialog = new DatePickerDialog(PermohonanActivityOld.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                            }
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.show();
//            }
//        });
//
//
//        editTextNik.addTextChangedListener(new MyTextWatcher(editTextNik));
//        editTextEmail.addTextChangedListener(new MyTextWatcher(editTextEmail));
//        editTextKeperluan.addTextChangedListener(new MyTextWatcher(editTextKeperluan));
//        input_layout_nik = (TextInputLayout)findViewById(R.id.inputNIK);
//        //input_layout_email = (TextInputLayout)findViewById(R.id.input_layout_email);
//        input_layout_keperluan_belum_memiliki_rumah = (TextInputLayout)findViewById(R.id.input_layout_keperluan_belum_memiliki_rumah);
//
//
//        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        buttonSubmit.setOnClickListener(this);
//
//    }
//
//
//    public void cekNikdanPhoto(){
//
//        String nik = editTextNik.getText().toString().trim();
//
//        HashMap<String, String> postData = new HashMap<>();
//        postData.put(Config.KEY_PERMOHONAN_NIK, nik);
//
//        PostResponseAsyncTask task = new PostResponseAsyncTask(PermohonanActivityOld.this, postData, new AsyncResponse() {
//            @Override
//            public void processFinish(String s) {
//                if(s.contains("Berhasil")){
//                    uploadMultiPart();
//                } else if(s.contains("NIK")) {
//                    new AlertDialog.Builder(PermohonanActivityOld.this)
//                            .setTitle("Permohonan Gagal")
//                            .setMessage("Maaf, Nomor Induk Kependudukan (NIK) Anda belum terdaftar. Silahkan hubungi kantor kelurahan Anda")
//                            .setPositiveButton("Oke", null)
//                            .setIcon(R.drawable.ic_warning_black_24dp)
//                            .show();
//                }
//            }
//        });
//        task.execute(Config.URL_CEK_NIK);
//        task.setEachExceptionsHandler(new EachExceptionsHandler() {
//            @Override
//            public void handleIOException(IOException e) {
//                Toast.makeText(getApplicationContext(),
//                        "Tidak Bisa Terhubung ke Server", Toast.LENGTH_SHORT ).show();
//            }
//
//            @Override
//            public void handleMalformedURLException(MalformedURLException e) {
//                Toast.makeText(getApplicationContext(),
//                        "URL Error", Toast.LENGTH_SHORT ).show();
//            }
//
//            @Override
//            public void handleProtocolException(ProtocolException e) {
//                Toast.makeText(getApplicationContext(),
//                        "Protocol Error", Toast.LENGTH_SHORT ).show();
//            }
//
//            @Override
//            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
//                Toast.makeText(getApplicationContext(),
//                        "Encoding Error", Toast.LENGTH_SHORT ).show();
//            }
//        });
//    }
//
//    public void uploadMultiPart(){
//
//        nik = editTextNik.getText().toString().trim();
//        email = editTextEmail.getText().toString().trim();
//        keperluan = editTextKeperluan.getText().toString().trim();
//        final String path = getPath(filePath);
//        final String path2 = getPath(filePath2);
//        Calendar calender = Calendar.getInstance();
//        final String myFormat = "yyyy-MM-dd";
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//        final String pelayananId = "5";
//
//        final String tanggalPelayanan = sdf.format(calender.getTime());
//
//        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//                    //   ProgressDialog.show(PermohonanBelumMenikah.this, "Loading", "Mohon Menunggu", false, false);
//                    Thread.sleep(3000);
//
//                    String uploadId = UUID.randomUUID().toString();
//                    new MultipartUploadRequest(PermohonanActivityOld.this, uploadId, Config.URL_ADD_PERMOHONAN_BELUM_MEMILIKI_RUMAH)
//                            .addFileToUpload(path, "ktp")
//                            .addFileToUpload(path2, "kk")
//                            .addParameter(Config.KEY_PERMOHONAN_NIK, nik)
//                            .addParameter(Config.KEY_PERMOHONAN_EMAIL, email)
//                            .addParameter(Config.KEY_PELAYANAN_ID, pelayananId)
//                            .addParameter(Config.KEY_TANGGAL_PELAYANAN, tanggalPelayanan)
//                            .addParameter(Config.KEY_KEPERLUAN, keperluan)
//                            .setNotificationConfig(new UploadNotificationConfig())
//                            .setMaxRetries(2)
//                            .startUpload();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//
//            @Override
//            protected void onPreExecute() {
//
//                loading = new ProgressDialog(PermohonanActivityOld.this);
//                loading.setTitle("Loading");
//                loading.setMessage("Mohon Tunggu");
//                loading.setCancelable(false);
//                loading.setIndeterminate(false);
//                loading.show();
//            }
//
//            @Override
//            protected void onPostExecute(Void result) {
//                if (loading != null) {
//                    loading.dismiss();
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(PermohonanActivityOld.this);
//                    builder.setTitle("Permohonan Berhasil");
//                    builder.setIcon(R.drawable.ic_check_black_24dp);
//                    builder.setMessage("Selamat ! Pengajuan Anda berhasil dilakukan. Silahkan cek Email untuk melihat nomer resi pelayanan");
//                    builder.setPositiveButton("Oke", null);
//                    final AlertDialog alert = builder.create();
//                    PermohonanActivityOld.this.runOnUiThread(new java.lang.Runnable() {
//                        public void run() {
//                            //show AlertDialog
//                            alert.show();
//                        }
//                    });
//                }
//            }
//        };
//        task.execute();
//    }
//
//    public String getPath(Uri uri){
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//
//        cursor.close();
//
//        cursor = getContentResolver().query(
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//
//        return path;
//    }
//
//    private void requestStoragePermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//            return;
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            //If the user has denied the permission previously your code will come to this block
//            //Here you can explain why you need this permission
//            //Explain here why you need this permission
//        }
//        //And finally ask for the permission
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        //Checking the request code of our request
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//
//            //If permission is granted
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //Displaying a toast
//                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
//            } else {
//                //Displaying another toast if permission is not granted
//                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                ivImage.setImageBitmap(bitmap);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else if (requestCode == GALLERY_REQUEST2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            filePath2 = data.getData();
//            try {
//                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
//                ivImage2.setImageBitmap(bitmap2);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void showFileChooser(){
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
//    }
//
//    private void showFileChooser2(){
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST2);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//
//    private class MyTextWatcher implements TextWatcher {
//
//        private View view;
//
//        private MyTextWatcher(View view){
//            this.view = view;
//        }
//
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void afterTextChanged(Editable editable) {
//            switch (view.getId()) {
//                case R.id.editTextNik:
//                    validateNik();
//                    break;
//                case R.id.editTextEmail:
//                    validateEmail();
//                    break;
//                case R.id.editTextKeperluan:
//                    validateKeperluan();
//                    break;
//            }
//        }
//    }
//
//    private void submitForm() {
//        if (!validateNik()) {
//            return;
//        }
//
//        if (!validateEmail()) {
//            return;
//        }
//
//        if(!validateKeperluan()){
//            return;
//        }
//
//        cekNikdanPhoto();
//    }
//
//    private boolean validateKeperluan() {
//        if (editTextKeperluan.getText().toString().trim().isEmpty()) {
//            input_layout_keperluan_belum_memiliki_rumah.setError(getString(R.string.err_keperluan_belum_menikah));
//            requestFocus(editTextKeperluan);
//            return false;
//        } else {
//            input_layout_keperluan_belum_memiliki_rumah.setErrorEnabled(false);
//        }
//        return true;
//    }
//
//    private boolean validateNik() {
//        if (editTextNik.getText().toString().trim().isEmpty()) {
//            input_layout_nik.setError(getString(R.string.err_nik));
//            requestFocus(editTextNik);
//            return false;
//        } else {
//            input_layout_nik.setErrorEnabled(false);
//        }
//        return true;
//    }
//
//    private boolean validateEmail(){
//        String email = editTextEmail.getText().toString().trim();
//        if(email.isEmpty() || !isValidEmail(email)){
//            input_layout_email.setError(getString(R.string.err_email));
//            requestFocus(editTextEmail);
//            return false;
//        } else {
//            input_layout_email.setErrorEnabled(false);
//        }
//        return true;
//    }
//
//
//    private static boolean isValidEmail(String email) {
//        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
//
//    private void requestFocus(View view) {
//        if (view.requestFocus()) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }
//}
