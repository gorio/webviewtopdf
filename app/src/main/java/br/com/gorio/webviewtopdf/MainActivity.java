package br.com.gorio.webviewtopdf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private WebView myWebView;
    Bitmap bmp;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You PDF was Saved Successfully", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                myWebView.capturePicture();
            }
        });

        myWebView = (WebView) findViewById(R.id.mywebview);

//        myWebView.setPictureListener(new WebView.PictureListener() {
//
//            public void onNewPicture(WebView view, Picture picture) {
//                if (picture != null) {
//                    try {
//                        Bitmap bmp = pictureDrawable2Bitmap(new PictureDrawable(picture));
//                        FileOutputStream out = new FileOutputStream(filename);
//                        bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
//                        out.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

        myWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

//                Picture picture = myWebView.capturePicture();

                Picture picture = view.capturePicture();
                Bitmap b = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(b);
                picture.draw(c);

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream( "/sdcard/"  + "page.jpg" );
                    if ( fos != null ) {
                        b.compress(Bitmap.CompressFormat.JPEG, 100, fos );
                        fos.close();
                    }
                }
                catch( Exception e ) {
                    System.out.println("-----error--"+e);
                }

            }
        });

        myWebView.getSettings().setJavaScriptEnabled(true);

        myWebView.loadUrl("http://www.globo.com");
    }

    private static Bitmap pictureDrawable2Bitmap(PictureDrawable pictureDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth()
                , pictureDrawable.getIntrinsicHeight()
                , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPicture(pictureDrawable.getPicture());
        return bitmap;
    }
}
