package com.example.yas_birouk.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int PICK_IMAGE=1;
    private int PICK_IMAGE_2=99;
    private static final String Tag="main_activty";
    private int v=0;
    private org.opencv.android.CameraBridgeViewBase mOpenCvCameraView;
    static{
        if(!OpenCVLoader.initDebug()){
            Log.d(Tag,"openCV not loded");
        }else{
            Log.d(Tag,"openCV is successfuly loded");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View view) {
//              Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //**************
                Bitmap textBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_with_text_2);
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!textRecognizer.isOperational()) {
                    new AlertDialog.Builder(getApplicationContext()).setMessage("Text recognizer could not be set up on your device :(").show();
                    return;
                }
                else{
                    String detectedText="this is the text : ";

                    TextView detectedTextView=findViewById(R.id.results);
                    Frame frame = new Frame.Builder().setBitmap(textBitmap).build();
                    SparseArray<TextBlock> text = textRecognizer.detect(frame);
                    for (int i = 0; i < text.size(); i++) {
                        TextBlock textBlock = text.valueAt(i);
                        if (textBlock != null && textBlock.getValue() != null) {
                            detectedText += textBlock.getValue();
                        }
                    }
                    detectedTextView.setText(detectedText);
                    textRecognizer.release();
                }
                //***************
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

        } else if (id == R.id.nav_slideshow) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture for text detection"), PICK_IMAGE_2);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    ///onActivityReasult  added by me

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                if(v==1){
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    // Log.d(TAG, String.valueOf(bitmap));


                    ImageView imageView = (ImageView) findViewById(R.id.imageView0);
                    imageView.setImageBitmap(bitmap);
                }
                else{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);


                    bitmap=createContrast(bitmap,100);
                    ImageView imageView = (ImageView) findViewById(R.id.imageView1);
                    imageView.setImageBitmap(bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_IMAGE_2 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                if(v==1){
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    // Log.d(TAG, String.valueOf(bitmap));


                    ImageView imageView = (ImageView) findViewById(R.id.imageView0);
                    imageView.setImageBitmap(bitmap);
                }
                else{
                    //********************************************_______________________________________________________
                    Bitmap textBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                    Bitmap textBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_with_text_2);
                    TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
                    if (!textRecognizer.isOperational()) {
                        new AlertDialog.Builder(this).setMessage("Text recognizer could not be set up on your device :(").show();
                        return;
                    }
                    else{
                        String detectedText="this is the text : ";

                        TextView detectedTextView=findViewById(R.id.results);
                        Frame frame = new Frame.Builder().setBitmap(textBitmap).build();
                        SparseArray<TextBlock> text = textRecognizer.detect(frame);
                        for (int i = 0; i < text.size(); i++) {
                            TextBlock textBlock = text.valueAt(i);
                            if (textBlock != null && textBlock.getValue() != null) {
                                detectedText += textBlock.getValue();
                            }
                        }
                        detectedTextView.setText(detectedText);
                        textRecognizer.release();
                    }

                    //****__________________________________________________________________________________________________
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //les fonction ajoutées
    //******************************************************* -----les fonction ajoutées----- ************************************************************

    public static Bitmap doColorFilter(Bitmap src, double red, double green, double blue) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                // apply filtering on each channel R, G, B
                A = Color.alpha(pixel);
                R = (int)(Color.red(pixel) * red);
                G = (int)(Color.green(pixel) * green);
                B = (int)(Color.blue(pixel) * blue);
                // set new color pixel to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }
    //********************************************************  invert image on the fly ************************************************
    public static Bitmap doInvert(Bitmap src) {
        // create new bitmap with the same settings as source bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // color info
        int A, R, G, B;
        int pixelColor;
        // image size
        int height = src.getHeight();
        int width = src.getWidth();

        // scan through every pixel
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                // get one pixel
                pixelColor = src.getPixel(x, y);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);
                // set newly-inverted pixel to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final bitmap
        return bmOut;
    }
    //********************************contrast image on the fly ****************
    public static Bitmap createContrast(Bitmap src, double value) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.green(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.blue(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }

    public  static Bitmap toGray(Bitmap src){
        Mat tmp = new Mat (src.getWidth(), src.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(src, tmp);
        Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
//there could be some processing
        //Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_GRAY2RGB, 4);
        Utils.matToBitmap(tmp, src);
        return src;
    }

//********************************************** ajoute men 3and  driss *********************************

//
//    @Override
//    public void onCameraViewStarted(int width, int height) {
//
//    }
//
//    @Override
//    public void onCameraViewStopped() {
//
//    }
//
//    @Override
//    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//        //We're returning the colored frame as is to be rendered on
//        //thescreen.
//        Mat rgba = inputFrame.rgba();
//        Size sizeRgba = rgba.size();
//        Mat grayInnerWindow1;
//        Mat rgbaInnerWindow;
//
//        int rows = (int) sizeRgba.height;
//        int cols = (int) sizeRgba.width;
//
//        int left = cols / 8;
//        int top = rows / 8;
//
//        int width = cols * 3 / 4;
//        int height = rows * 3 / 4;
//
//        rgbaInnerWindow = rgba.submat(top, top + height, left, left + width);
//
//        ArrayList<Mat> channels = new ArrayList<Mat>(3);
//
//        Mat src1= Mat.zeros(rgbaInnerWindow.size(), CvType.CV_8UC3);
//
//        Core.split(rgbaInnerWindow, channels);
//
//        Mat b = channels.get(0);
//        Imgproc.threshold(b, b, 0, 70, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
//        Mat g = channels.get(1);
//        Imgproc.threshold(g, g, 0, 70, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
//        Mat r = channels.get(2);
//        Imgproc.threshold(r, r, 90, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
//
//        Core.merge(channels, src1);
//        Imgproc.medianBlur(src1, src1, 3);
//
//        Imgproc.threshold(src1,rgbaInnerWindow,0, 255, Imgproc.THRESH_BINARY);
//        Imgproc.cvtColor(rgbaInnerWindow,src1, Imgproc.COLOR_BGR2GRAY);
//
//        rgbaInnerWindow.release();
//
//        return rgba;
//
//
//        //return inputFrame.rgba();
//    }

    ///************************************************text regonition by google text recognizer ************************************************************************



}



