package com.example.root.bytecodetopdf;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

  String base64 = "TXkgTmFtZSBpcyB2YXN1cHVqeSBwYXRlbA==";

  @RequiresApi(api = Build.VERSION_CODES.KITKAT) @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    final TextView tv = (TextView) findViewById(R.id.textView);
    ViewTreeObserver vto = tv.getViewTreeObserver();
    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override public void onGlobalLayout() {
        try {
          File file1 = new File("/mnt/sdcard/test/");
          if (!file1.exists()) {
            file1.mkdirs();
          }

          File file =
              new File("/mnt/sdcard/test", "filename" + "vasupujy" + ".pdf");
          PrintAttributes printAttrs = new PrintAttributes.Builder().
              setColorMode(PrintAttributes.COLOR_MODE_COLOR).
              setMediaSize(PrintAttributes.MediaSize.ISO_A4).
              setResolution(new PrintAttributes.Resolution("zooey", PRINT_SERVICE, 450, 700)).
              setMinMargins(PrintAttributes.Margins.NO_MARGINS).
              build();
          PdfDocument document = new PrintedPdfDocument(MainActivity.this, printAttrs);
          PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(450, 700, 1).create();
          PdfDocument.Page page = document.startPage(pageInfo);
          byte[] data = Base64.decode(base64, Base64.DEFAULT);
          String text = new String(data, "UTF-8");
          tv.setText(text);
          if (page != null) {

            View view = findViewById(R.id.textView);//getContentView();
            view.layout(0, 0, view.getWidth(), view.getHeight());
            Log.i("draw view", " content size: " + view.getWidth() + " / " + view.getHeight());
            view.draw(page.getCanvas());
            // Move the canvas for the next view.
            page.getCanvas().translate(0, view.getHeight());
          }
          byte[] pdfAsBytes = Base64.decode(base64, Base64.NO_WRAP);
          document.finishPage(page);
          FileOutputStream os = new FileOutputStream(file);
          os.write(pdfAsBytes);
          document.writeTo(os);
          document.close();
          os.close();
          Log.i("done", file.getAbsolutePath().toString());
        } catch (IOException e) {
          throw new RuntimeException("Error generating file", e);
        }
      }
    });
  }
}
