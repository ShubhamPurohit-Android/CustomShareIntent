package com.sharepoc;


import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button Share, Choose;
    Context context = MainActivity.this;
    ImageView mainimage;
    Bitmap bitmapimagegallery;
    Drawable drawableBG;
    int RESULT_LOAD_IMG = 1;
    int RESULT_LOAD_BG = 2;
    String imgDecodableString;
    Uri imageuri;
    RelativeLayout ScreenBG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainimage = (ImageView) findViewById(R.id.mainimage);
        Share = (Button) findViewById(R.id.ShareButton);
        Choose = (Button) findViewById(R.id.Choose);
        ScreenBG = (RelativeLayout) findViewById(R.id.screen1);


        Choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

    // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });


        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageuri != null) {
                    List<LabeledIntent> intents3 = getNativeShareIntent(context, imageuri);
                    LabeledIntent[] extraIntents = new LabeledIntent[intents3.size()];

                    extraIntents = intents3.toArray(extraIntents);
                    Intent openInChooser = Intent.createChooser(intents3.remove(0),
                            "Share");
                    openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
                    startActivity(openInChooser);

                } else {
                    Toast.makeText(context, "Choose a picture", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                bitmapimagegallery = decodeFile(imgDecodableString,1);
//                bitmapimagegallery = BitmapFactory.decodeFile(imgDecodableString);
                File file = new File(imgDecodableString);
                imageuri = Uri.fromFile(file);
                bitmapimagegallery = Bitmap.createScaledBitmap(bitmapimagegallery, 300, 450, false);
                mainimage.setImageBitmap(bitmapimagegallery);


            }else if(requestCode == RESULT_LOAD_BG && resultCode == RESULT_OK
                    && null != data){
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                bitmapimagegallery = decodeFile(imgDecodableString,1);
//                bitmapimagegallery = BitmapFactory.decodeFile(imgDecodableString);
                drawableBG = new BitmapDrawable(getResources(), bitmapimagegallery);
                ScreenBG.setBackground(drawableBG);
            }
            else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }


    public List<LabeledIntent> getNativeShareIntent(final Context context, final Uri imageuri) {
//        File imageFileToShare = new File("/storage/emulated/0/NYC_FULL/EXPO/493.png");

        final PackageManager pm = context.getPackageManager();
        final Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageuri);
        sendIntent.setType("image/jpeg/png");

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<>();

        for (int i = 0; i < resInfo.size(); i++) {
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/jpeg/png");
            intent.putExtra(Intent.EXTRA_STREAM, imageuri);
            intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm),
                    ri.getIconResource()));
        }

        // TODO Implement the method getSaveToGalleryIntent,
        // Could be a simple intent linking to activity.
        intentList.add(getSaveToGalleryIntent(context, imageuri));

        return intentList;
    }


    private LabeledIntent getSaveToGalleryIntent(final Context context, Uri imageuri) {
        final Intent intent = new Intent(context, SaveToGalleryActivity.class);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra("Gallery", imageuri);
        intent.setType("image/jpeg/png");
        return new LabeledIntent(intent, BuildConfig.APPLICATION_ID,
                "Save to gallery",
                R.mipmap.ic_launcher);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.changebg:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_BG);
//                newGame();//////////////
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Bitmap decodeFile(String path, int sampleSize) {
        try {

            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;


            BitmapFactory.decodeFile(path, o);

            o.inSampleSize = sampleSize;
            o.inJustDecodeBounds = false;

            Bitmap b = BitmapFactory.decodeFile(path, o);

            return b;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return decodeFile(path, sampleSize + 1);

        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(context , "Exception in decoding", Toast.LENGTH_SHORT ).show();
            return decodeFile(path, sampleSize + 1);
        }

    }
}


