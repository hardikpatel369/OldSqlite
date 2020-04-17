package com.example.sqlite.Images;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.system.ErrnoException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.RequestBuilder;
import com.example.sqlite.R;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImageLoadActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 222;
    private CropImageView mCropImageView;
    private Uri mCropImageUri;
    private ImageButton ibRotateLeft, ibRotateRight;
    private ImageDatabase imageDatabase;
    Bitmap bitmap;
    Uri bitmapUri;
    RequestBuilder<Drawable> image;
    Button btnSave;
    String fileName;
    String savedImagePath;
    private static final int REQ_WRITE_EXT_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_load);

        mCropImageView = findViewById(R.id.CropImageView);
        ibRotateRight = findViewById(R.id.ibRotateRight);
        ibRotateLeft = findViewById(R.id.ibRotateLeft);
        btnSave = findViewById(R.id.btnSave);

        imageDatabase = new ImageDatabase(this);

        ibRotateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCropImageView.setRotatedDegrees(mCropImageView.getRotatedDegrees() + 90);
            }
        });

        ibRotateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCropImageView.setRotatedDegrees(mCropImageView.getRotatedDegrees() - 90);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    saveAndRefreshFiles(mCropImageView.getCroppedImage(), ImageLoadActivity.this);
                    //  saveTempBitmap(mCropImageView.getCroppedImage());
                    // BitmapUtils.saveImage(MainActivity.this,mCropImageView.getCroppedImage());
                    Log.d("mCropImageView", "onClick: " + mCropImageView.getCroppedImage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (ContextCompat.checkSelfPermission(ImageLoadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ImageLoadActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_WRITE_EXT_STORAGE);
                }

                long rowId = imageDatabase.InsertData(savedImagePath,fileName,0,0);
                if (rowId > 0) {
                    Toast.makeText(ImageLoadActivity.this, "Image Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ImageLoadActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_WRITE_EXT_STORAGE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // startActivity(new Intent(introActivity.this,MainActivity.class));

            } else {
                ActivityCompat.requestPermissions(ImageLoadActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_WRITE_EXT_STORAGE);
            }
        }
    }


    public void saveAndRefreshFiles(Bitmap bitmap, Context context) throws IOException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        String extension = "jpg";
        savedImagePath = null;
        // String extension = FilenameUtils.getExtension(new File(String.valueOf(sourceFile)).getAbsolutePath());

        fileName = "SqliteImages" + format + "." + extension;

        File destFile = new File(Environment.getExternalStorageDirectory().toString() + "/SqliteImages/", fileName);
        //  MediaScannerConnection.scanFile(context, new String[]{destFile.getPath()}, new String[]{"image/*"}, null);
        MediaScannerConnection.scanFile(this, new String[]{destFile.getPath()}, new String[]{"image/jpeg"}, null);
        Log.d("mCropImageView", "onClick: " + destFile);

        if (!destFile.getParentFile().exists()) {
            Log.d("mCropImageView", "pde: befor " + destFile.getParentFile().exists());

            destFile.getParentFile().mkdirs();
            Log.d("mCropImageView", "pde: after " + destFile.getParentFile().exists());

        }
        if (!destFile.exists()) {
            //  destFile.getParentFile().mkdirs();

            destFile.createNewFile();

            Log.e("mCropImageView", "File Created");
        }


        savedImagePath = destFile.getAbsolutePath();
        Log.d("imageFile", "file :  " + savedImagePath);

        try {
            OutputStream fOut = new FileOutputStream(destFile);
            Log.d("imageFile", "file fOut:  " + fOut);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            Log.d("imageFile", "file fOut:  " + bitmap);

            fOut.close();
            Toast.makeText(context, "save", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add the image to the system gallery
        galleryAddPic(context, savedImagePath);

    }

    private static void galleryAddPic(Context context, String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * On load image button click, start pick image chooser activity.
     */
    public void onLoadImageClick(View view) {
        startActivityForResult(getPickImageChooserIntent(), 200);


//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                Document document = null;
//                try{
//
//                     document = Jsoup.connect("https://m.cricbuzz.com/cricket-pointstable" +
//                             "/2697/icc-cricket-world-cup-2019")
//                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                            .get();
//
//
//                    Log.e("document ", "document:- " + document.toString());
//                }catch (Exception e){
//                    Log.e("document ", "document:- " + e.getMessage());
//                }
//
////                Log.e("document", "zipFilePath:- " + zipFilePath);
//            }
//        });
//
//        thread.start();


    }

    /**
     * Crop the image and set it back to the cropping view.
     */
    public void onCropImageClick(View view) {
        Bitmap cropped = mCropImageView.getCroppedImage(500, 500);
        if (cropped != null) {
            mCropImageView.setImageBitmap(cropped);
            bitmap = cropped;
            Log.d("mCropImageView", "onCropImageClick: " + bitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = getPickImageResultUri(data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }


            if (!requirePermissions) {
                bitmapUri = imageUri;
                mCropImageView.setImageUriAsync(imageUri);

            }
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            bitmapUri=mCropImageUri;
//            mCropImageView.setImageUriAsync(mCropImageUri);
//        } else {
//            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
//        }
//    }

    /**
     * Create a chooser intent to select the source to get image from.<br/>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the intent chooser.
     */
    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        // bitmapUri=outputFileUri;
        return chooserIntent;
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        // bitmapUri=outputFileUri;
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     */
    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (e.getCause() instanceof ErrnoException) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
}
