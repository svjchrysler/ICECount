package com.icescz.conteo.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.cloudinary.utils.ObjectUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.icescz.conteo.ControllersDB.DBRemote;
import com.icescz.conteo.ControllersDB.DbLocal;
import com.icescz.conteo.Helper;
import com.icescz.conteo.Models.Car;
import com.icescz.conteo.Models.Person;
import com.icescz.conteo.R;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class OptionActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    @BindView(R.id.edtNombre)
    EditText edtNombre;

    @BindView(R.id.imgPeople)
    ImageView imgPeople;

    @BindView(R.id.imgCars)
    ImageView imgCars;

    private Validator validator;
    private int activity = 0;

    private Vibrator vibrator;
    Cloudinary cloudinary;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        configInitial();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sincronizar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sincronizar:
                sincronizar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Person cargarPerson(Person objPerson) {
        Person person = new Person();
        person.setId(objPerson.getId());
        person.setNombreEncuestador(objPerson.getNombreEncuestador());
        person.setHombre(objPerson.getHombre());
        person.setNinia(objPerson.getNinia());
        person.setMujer(objPerson.getMujer());
        person.setAnciano(objPerson.getAnciano());
        person.setCalleRelevamiento(objPerson.getCalleRelevamiento());
        person.setCalleLateralA(objPerson.getCalleLateralA());
        person.setCalleLateralB(objPerson.getCalleLateralB());
        person.setTemperatura(objPerson.getTemperatura());
        person.setCondiciones(objPerson.getCondiciones());
        person.setHoraInicio(objPerson.getHoraInicio());
        person.setHoraFin(objPerson.getHoraFin());
        person.setFecha(objPerson.getFecha());
        person.setFijoCantidad(objPerson.getFijoCantidad());
        person.setFijoNota(objPerson.getFijoNota());
        person.setAmbulanteCantidad(objPerson.getAmbulanteCantidad());
        person.setAmbulanteNota(objPerson.getAmbulanteNota());
        person.setIndigenaHombre(objPerson.getIndigenaHombre());
        person.setIndigenaMujer(objPerson.getIndigenaMujer());
        person.setImagenMapeo(objPerson.getImagenMapeo());
        person.setImagenAcera(objPerson.getImagenAcera());
        person.setCometarioAcera(objPerson.getCometarioAcera());
        person.setNota(objPerson.getNota());

        return person;
    }

    private Car cargarCar(Car objCar) {
        Car car = new Car();
        car.setId(objCar.getId());
        car.setNombreEncuestador(objCar.getNombreEncuestador());
        car.setParticular(objCar.getParticular());
        car.setBicicleta(objCar.getBicicleta());
        car.setMotocicleta(objCar.getMotocicleta());
        car.setTaxi(objCar.getTaxi());
        car.setPublico(objCar.getPublico());
        car.setRepartidor(objCar.getRepartidor());
        car.setCalleRelevamiento(objCar.getCalleRelevamiento());
        car.setCalleLateralA(objCar.getCalleLateralA());
        car.setCalleLateralB(objCar.getCalleLateralB());
        car.setTemperatura(objCar.getTemperatura());
        car.setCondiciones(objCar.getCondiciones());
        car.setHoraInicio(objCar.getHoraInicio());
        car.setHoraFin(objCar.getHoraFin());
        car.setFecha(objCar.getFecha());
        car.setFijoCantidad(objCar.getFijoCantidad());
        car.setFijoNota(objCar.getFijoNota());
        car.setAmbulanteCantidad(objCar.getAmbulanteCantidad());
        car.setAmbulanteNota(objCar.getAmbulanteNota());
        car.setIndigenaHombre(objCar.getIndigenaHombre());
        car.setIndigenaMujer(objCar.getIndigenaMujer());
        car.setImagenMapeo(objCar.getImagenMapeo());
        car.setImagenAcera(objCar.getImagenAcera());
        car.setCometarioAcera(objCar.getCometarioAcera());
        car.setNota(objCar.getNota());
        return car;
    }

    public void uploadCar(final Car car) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Helper.URL_CAR_STORE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DbLocal dbLocal = new DbLocal(OptionActivity.this);
                        dbLocal.eliminarCar(realm, car);
                        Toast.makeText(OptionActivity.this, "Datos Subidos Correctamente", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OptionActivity.this, "Error en el servidor", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nombre", car.getNombreEncuestador());
                params.put("particular", String.valueOf(car.getParticular()));
                params.put("bicicleta", String.valueOf(car.getBicicleta()));
                params.put("motocicleta", String.valueOf(car.getMotocicleta()));
                params.put("taxi", String.valueOf(car.getTaxi()));
                params.put("publico", String.valueOf(car.getPublico()));
                params.put("repartidor", String.valueOf(car.getRepartidor()));
                params.put("relevamiento", car.getCalleRelevamiento());
                params.put("lateral_a", car.getCalleLateralA());
                params.put("lateral_b", car.getCalleLateralB());
                params.put("temperatura", String.valueOf(car.getTemperatura()));
                params.put("condiciones", car.getCondiciones());
                params.put("inicio", car.getHoraInicio());
                params.put("fin", car.getHoraFin());
                params.put("fecha", car.getFecha());
                params.put("nota", car.getNota());
                params.put("fijoc", String.valueOf(car.getFijoCantidad()));
                params.put("fijon", car.getFijoNota());
                params.put("ambulantec", String.valueOf(car.getAmbulanteCantidad()));
                params.put("ambulanten", car.getAmbulanteNota());
                params.put("indigenah", String.valueOf(car.getIndigenaHombre()));
                params.put("indigenam", String.valueOf(car.getIndigenaMujer()));
                params.put("comentario", car.getCometarioAcera());
                params.put("imageMapeo", car.getImagenMapeo());
                params.put("imageAcera", car.getImagenAcera());

                if (!car.getImagenMapeo().equals("")) {
                    params.put("imageAcera", obtenerImagenBase64(car.getImagenMapeo()));
                } else {
                    params.put("imageAcera", car.getImagenAcera());
                }

                if (!car.getImagenAcera().equals("")) {
                    params.put("imageMapeo", obtenerImagenBase64(car.getImagenMapeo()));
                } else {
                    params.put("imageMapeo", car.getImagenMapeo());
                }
                return params;
            }
        };

        RequestQueue request = Volley.newRequestQueue(OptionActivity.this);
        request.add(stringRequest);
    }

    public void uploadPerson(final Person person) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Helper.URL_PERSON_STORE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(OptionActivity.this, "Datos Subidos Correctamente", Toast.LENGTH_SHORT).show();
                        DbLocal dbLocal = new DbLocal(OptionActivity.this);
                        dbLocal.eliminarPerson(realm ,person);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OptionActivity.this, "Error en le servidor", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nombre", person.getNombreEncuestador());
                params.put("hombre", String.valueOf(person.getHombre()));
                params.put("ninia", String.valueOf(person.getNinia()));
                params.put("mujer", String.valueOf(person.getMujer()));
                params.put("anciano", String.valueOf(person.getAnciano()));
                params.put("relevamiento", person.getCalleRelevamiento());
                params.put("lateral_a", person.getCalleLateralA());
                params.put("lateral_b", person.getCalleLateralB());
                params.put("temperatura", String.valueOf(person.getTemperatura()));
                params.put("condiciones", person.getCondiciones());
                params.put("inicio", person.getHoraInicio());
                params.put("fin", person.getHoraFin());
                params.put("fecha", person.getFecha());
                params.put("nota", person.getNota());
                params.put("fijoc", String.valueOf(person.getFijoCantidad()));
                params.put("fijon", person.getFijoNota());
                params.put("ambulantec", String.valueOf(person.getFijoCantidad()));
                params.put("ambulanten", person.getAmbulanteNota());
                params.put("indigenah", String.valueOf(person.getIndigenaHombre()));
                params.put("indigenam", String.valueOf(person.getIndigenaMujer()));
                params.put("comentario", person.getCometarioAcera());

                if (!person.getImagenMapeo().equals("")) {
                    params.put("imageAcera", obtenerImagenBase64(person.getImagenMapeo()));
                } else {
                    params.put("imageAcera", person.getImagenAcera());
                }

                if (!person.getImagenAcera().equals("")) {
                    params.put("imageMapeo", obtenerImagenBase64(person.getImagenMapeo()));
                } else {
                    params.put("imageMapeo", person.getImagenMapeo());
                }

                return params;
            }
        };

        RequestQueue request = Volley.newRequestQueue(OptionActivity.this);
        request.add(stringRequest);
    }


    public boolean isConnectedWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public boolean isConnectedMobile(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    private String obtenerImagenBase64(String photoPath) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap imageBitmap = null;
        try {
            imageBitmap = ImageLoader.init().from(photoPath).getBitmap();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void cargarImagenCloud(final String nameImage) {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                cloudinary = new Cloudinary(Utils.cloudinaryUrlFromContext(OptionActivity.this));
                String imagen = cloudinary.url().generate(nameImage);

                try {
                    Map data = cloudinary.uploader().uploadLarge(imagen, ObjectUtils.asMap("public_id", "sample_remote", "url"));
                    Log.e("iformacion imagen", data.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        task.execute();
    }

    private void sincronizar() {
        RealmResults<Person> personRealmResults = realm.where(Person.class).findAll();
        RealmResults<Car> carRealmResults = realm.where(Car.class).findAll();
        if (isConnectedMobile(this) || isConnectedWifi(this)) {
            if (isOnline(this)) {
                if (personRealmResults.size() > 0 || carRealmResults.size() > 0) {
                    for (Person person : personRealmResults) {
                        uploadPerson(cargarPerson(person));
                    }

                    for (Car car : carRealmResults) {
                        uploadCar(cargarCar(car));
                    }
                } else {
                    Toast.makeText(OptionActivity.this, "Ya no tiene informacion muchas gracias", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(OptionActivity.this, "Porfavor conectarse a Internet", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(OptionActivity.this, "Porfavor conectarse a Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void configInitial() {
        configInitCreate();
    }

    private void configInitCreate() {
        ButterKnife.bind(this);
        validator = new Validator(this);
        realm = Realm.getDefaultInstance();
        validator.setValidationListener(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @OnClick(R.id.imgPeople)
    public void startActivityA() {
        optionSelection(1);
    }

    @OnClick(R.id.imgCars)
    public void startActivityB() {
        optionSelection(2);
    }

    private void optionSelection(int activity) {
        this.activity = activity;
        vibracion();
        validator.validate();
    }

    private void iniciarActividad() {
        Helper.nombre_encuestador = edtNombre.getText().toString().trim();
        if (activity == 1)
            startActivity(new Intent(OptionActivity.this, PeopleCountActivity.class));
        if (activity == 2)
            startActivity(new Intent(OptionActivity.this, CarsCountActivity.class));
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    private void vibracion() {
        vibrator.vibrate(100);
    }


    @Override
    public void onValidationSucceeded() {
        iniciarActividad();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = "Escriba su nombre porfavor";

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
