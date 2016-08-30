package com.icescz.conteo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.icescz.conteo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    //Cloudinary cloudinary;

    @BindView(R.id.imgFondo)
    ImageView imgFondo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //cargarImagen();
    }

    @OnClick(R.id.imgFondo)
    public void startMain() {
        Intent intent = new Intent(MainActivity.this, OptionActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
    }

    /*
    private void cargarImagen() {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                cloudinary = new Cloudinary(Utils.cloudinaryUrlFromContext(MainActivity.this));
                String imagen = cloudinary.url().generate("http://res.cloudinary.com/demo/image/upload/sample.jpg");
                try {
                    Map data = cloudinary.uploader().upload(imagen, ObjectUtils.asMap("public_id", "sample_remote"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

        };

        task.execute();
    }*/
}
