package com.sharepoc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;
import java.io.File;

/**
 * Created by 1006258 on 7/8/2016.
 */
public class SaveToGalleryActivity extends AppCompatActivity implements View.OnClickListener {

    Bitmap mainbitmap, b, overlay, framedbitmap,selectedbitmap;
    ImageView imageview,frame,frame2,frame3,frame4,frameimage;
    Button save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        imageview= (ImageView) findViewById(R.id.galleryimage);
        frameimage = (ImageView) findViewById(R.id.final_image);

        frame = (ImageView) findViewById(R.id.frame_image1);
        frame2 = (ImageView) findViewById(R.id.frame_image2);
        frame3 = (ImageView) findViewById(R.id.frame_image3);
        frame4 = (ImageView) findViewById(R.id.frame_image4);
        save = (Button) findViewById(R.id.SaveButton);

        frame.setOnClickListener(this);
        frame2.setOnClickListener(this);
        frame3.setOnClickListener(this);
        frame4.setOnClickListener(this);
        save.setOnClickListener(this);

        Uri data = getIntent().getExtras().getParcelable("Gallery");
        String path = data.getPath();
        selectedbitmap = BitmapFactory.decodeFile(path);
        selectedbitmap = Bitmap.createScaledBitmap(selectedbitmap, 400, 550, false);
        imageview.setImageBitmap(selectedbitmap);   //////**************************************************************


            }


    public void SetFrameImage(int id){

        if(selectedbitmap!=null) {
            b = Bitmap.createBitmap(400, 550, Bitmap.Config.ARGB_8888);

            overlay = BitmapFactory.decodeResource(getResources(),id);
            overlay = Bitmap.createScaledBitmap(overlay, 400, 550, false);
            //create canvas with a clean bitmap
            Canvas canvas = new Canvas(b);
            //draw the snappedImage on the canvas
            canvas.drawBitmap(selectedbitmap, 0, 0, new Paint());
            //draw the overlay on the canvas
            canvas.drawBitmap(overlay, 0, 0, new Paint());

            frameimage.setImageBitmap(b);
            framedbitmap = ((BitmapDrawable) frameimage.getDrawable()).getBitmap();


        }
        else{
            Toast.makeText(this,"Error framing Photo",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.frame_image1:

                SetFrameImage(R.mipmap.frame);


                break;
            case R.id.frame_image2:

                SetFrameImage(R.mipmap.frame2);


                break;
            case R.id.frame_image3:

                SetFrameImage(R.mipmap.frame3);


                break;
            case R.id.frame_image4:

                SetFrameImage(R.mipmap.colorframe);


                break;
            case R.id.SaveButton:
//        Code for saving the image
                SaveFrameImage();


                break;
            default:
                break;
        }
    }

    //        Code for saving the image
    private void SaveFrameImage() {
        if(framedbitmap!=null){
            MediaStore.Images.Media.insertImage(getContentResolver(),framedbitmap,
                    "demo_image",
                    "demo_image");
            Toast.makeText(this,"Picture Saved",Toast.LENGTH_SHORT).show();
        }
    }


}





