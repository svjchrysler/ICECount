package com.icescz.conteo.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.icescz.conteo.ControllersDB.DbLocal;
import com.icescz.conteo.Helper;
import com.icescz.conteo.Models.Person;
import com.icescz.conteo.R;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import rx.functions.Action1;

public class PeopleCountActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    @BindView(R.id.imgHombre)
    ImageView imgHombre;

    @BindView(R.id.imgNinia)
    ImageView imgNinia;

    @BindView(R.id.imgMujer)
    ImageView imgMujer;

    @BindView(R.id.imgAnciano)
    ImageView imgAnciano;

    @BindView(R.id.txtvCronometro)
    TextView txtvCronometro;

    @BindView(R.id.txthombre)
    TextView txtHombre;

    @BindView(R.id.txtninia)
    TextView txtNinia;

    @BindView(R.id.txtmujer)
    TextView txtMujer;

    @BindView(R.id.txtabuelo)
    TextView txtAnciano;

    private Realm realm;
    private CountDownTimer timer;
    private Validator validator;
    private Vibrator vibrator;
    private Integer countHombre, countNinia, countMujer, countAnciano;
    private long hora, minutos, segundos, segundosTotales;
    private boolean sw = false, swventana = false, swTerminado = false, swVentanaDatos = false;

    private Bitmap bitmapPhoto;
    private CameraPhoto cameraPhoto;
    private String pathPhoto = "";

    private Bitmap bitmapPhotoEstado;
    private String pathPhotoEstado = "";

    @NotEmpty(message = "El campo es obligatorio")
    EditText edtCalle1;

    @NotEmpty(message = "La calle de relevamiento es obligatorio")
    EditText edtCalle2;

    @NotEmpty(message = "La calle de relevamiento es obligatorio")
    EditText edtCalle3;

    @NotEmpty(message = "Temperatura obligatorio")
    EditText edtTemperatura;

    EditText edtnotafijo,edtcountambulante,edtnotaambulante,edtindigenah,edtindigenam,edtNota,edtcountfijo,edtComentarioAcera;

    Button btnGuardar, btnCancelar;

    Spinner spCondiciones, spinner;

    ImageButton imgCamera, imgCameraEstado;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_count);
        configInit();
    }

    @Override
    protected void onStop() {
        if (sw) {
            initCronometro((int) segundosTotales);
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu_conteo, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.opciones_people, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove:
                optionSelecionado();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void optionSelecionado() {
        switch (spinner.getSelectedItem().toString()) {
            case "Hombre":
                cargarInformacion(1, -1);
                break;
            case "Mujer":
                cargarInformacion(3, -1);
                break;
            case "Anciano":
                cargarInformacion(4, -1);
                break;
            case "Niño":
                cargarInformacion(2, -1);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("anciano", countAnciano);
        outState.putInt("ninia", countNinia);
        outState.putInt("mujer", countMujer);
        outState.putInt("hombre", countHombre);
        outState.putBoolean("sw", swventana);
        outState.putInt("seconds", (int) segundosTotales);
        outState.putBoolean("ventana", swVentanaDatos);
        if (sw)
            timer.cancel();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        countAnciano = savedInstanceState.getInt("anciano");
        countHombre = savedInstanceState.getInt("hombre");
        countMujer = savedInstanceState.getInt("mujer");
        countNinia = savedInstanceState.getInt("ninia");

        txtAnciano.setText("Total: " + countAnciano);
        txtHombre.setText("Total: " + countHombre);
        txtMujer.setText("Total: " + countMujer);
        txtNinia.setText("Total: " + countNinia);

        swventana = savedInstanceState.getBoolean("sw");
        initCronometro(savedInstanceState.getInt("seconds"));

        if (savedInstanceState.getBoolean("ventana"))
            ventanaDatos();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && swventana) {
            advertenciaClose();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void obtenerImagen(int id) {
        if (id == 1)
            cargarImagenMapeo();
        if (id == 2)
            cargarImagenAcera();
    }

    private void cargarImagenAcera() {
        RxImagePicker.with(this).requestImage(Sources.CAMERA).subscribe(new Action1<Uri>() {
            @Override
            public void call(Uri uri) {
                imgCameraEstado.setImageURI(uri);
            }
        });
    }

    private void cargarImagenMapeo() {
        RxImagePicker.with(this).requestImage(Sources.CAMERA).subscribe(new Action1<Uri>() {
            @Override
            public void call(Uri uri) {
                imgCamera.setImageURI(uri);
            }
        });
    }

    private void advertenciaClose() {
        final AlertDialog.Builder alertAdvertencia = new AlertDialog.Builder(this);
        alertAdvertencia.setTitle("Advertencia");
        alertAdvertencia.setMessage("Seguro que quieres salir se borrara tu información?");
        alertAdvertencia.setCancelable(false);

        alertAdvertencia.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                swVentanaDatos = true;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        alertAdvertencia.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertAdvertencia.show();
    }

    private void configInit() {
            configCreate();
        iniciarVariables();
    }

    private void iniciarVariables() {
        countHombre = 0;
        countNinia = 0;
        countMujer = 0;
        countAnciano = 0;
    }

    private void configCreate() {
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        realm = Realm.getDefaultInstance();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        this.setTitle("Bienvenido: " + Helper.nombre_encuestador);
    }

    private void initCronometro(int n) {
        sw = true;
        timer = new CountDownTimer(n * 1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                calculoCronometro(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                txtvCronometro.setText("Terminado");
                swTerminado = true;
                swVentanaDatos = true;
                if (dialog != null)
                    dialog.dismiss();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                //ventanaDatos();
            }
        };

        timer.start();
    }

    private void calculoCronometro(long millisUntilFinished) {
        segundosTotales = millisUntilFinished / 1000;
        hora = segundosTotales / 3600;
        minutos = (segundosTotales - (hora * 3600)) / 60;
        segundos = segundosTotales - ((hora * 3600) + (minutos * 60));
        if (minutos < 10 || segundos < 10) {
            String min = String.valueOf(minutos), seg = String.valueOf(segundos);
            if (minutos < 10)
                min = "0" + min;
            if (segundos < 10)
                seg = "0" + seg;
            txtvCronometro.setText(min + ":" + seg);
        } else {
            txtvCronometro.setText(minutos + ":" + segundos);
        }
    }

    private void vibracion() {
        vibrator.vibrate(50);
    }

    private void cargarInformacion(int param, int n) {
        swventana = true;
        vibracion();
        if (sw) {
            switch (param) {
                case 1:
                    if (countHombre > 0 || n > 0) {
                        countHombre += n;
                        txtHombre.setText("Total: " + countHombre);
                    }

                    break;
                case 2:
                    if (countNinia > 0 || n > 0) {
                        countNinia += n;
                        txtNinia.setText("Total: " + countNinia);
                    }
                    break;
                case 3:
                    if (countMujer > 0 || n > 0) {
                        countMujer += n;
                        txtMujer.setText("Total: " + countMujer);
                    }
                    break;
                case 4:
                    if (countAnciano > 0 || n > 0) {
                        countAnciano += n;
                        txtAnciano.setText("Total: " + countAnciano);
                    }
                    break;
            }
        } else {
            initCronometro(10);
            horafechainicio();
            cargarInformacion(param, n);
        }
    }

    private void horafechainicio() {
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Helper.horaActual = hourFormat.format(new java.util.Date()).toString();
        Helper.fechaActual = dateFormat.format(new java.util.Date()).toString();
    }

    private void ventanaDatos() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_nota);

        configComponents(dialog);
        cargarSpinner();

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && swventana && !swTerminado) {
                    dialog.dismiss();
                    swVentanaDatos = false;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                return false;
            }
        });

        dialog.show();
    }

    private void cargarDatosPerson() {

        Integer fijoCantidad = !edtcountfijo.getText().toString().equals("")  ? Integer.parseInt(edtcountfijo.getText().toString()) : 0;
        String fijonota = edtnotafijo.getText().toString();
        Integer ambulantecantidad = !edtcountambulante.getText().toString().equals("") ? Integer.parseInt(edtcountambulante.getText().toString()) : 0;
        String ambulantenota = edtnotaambulante.getText().toString();
        Integer indigenah = !edtindigenah.getText().toString().equals("") ? Integer.valueOf(edtindigenah.getText().toString()) : 0;
        Integer indigenam = !edtindigenam.getText().toString().equals("") ? Integer.valueOf(edtindigenam.getText().toString()) : 0;
        pathPhoto = !pathPhoto.equals("") && !pathPhoto.equals(null) ? pathPhoto : "";
        pathPhotoEstado = !pathPhotoEstado.equals("") && !pathPhotoEstado.equals(null) ? pathPhotoEstado : "";

        Person person = new Person();
        person.setNombreEncuestador(Helper.nombre_encuestador);
        person.setHombre(countHombre);
        person.setNinia(countNinia);
        person.setMujer(countMujer);
        person.setAnciano(countAnciano);
        person.setCalleRelevamiento(edtCalle1.getText().toString().trim());
        person.setCalleLateralA(edtCalle2.getText().toString().trim());
        person.setCalleLateralB(edtCalle3.getText().toString().trim());
        person.setTemperatura(Integer.parseInt(edtTemperatura.getText().toString()));
        person.setCondiciones(spCondiciones.getSelectedItem().toString());
        person.setHoraInicio(Helper.horaActual);
        person.setHoraFin(getHora());
        person.setFecha(Helper.fechaActual);
        person.setFijoCantidad(fijoCantidad);
        person.setFijoNota(fijonota);
        person.setAmbulanteCantidad(ambulantecantidad);
        person.setAmbulanteNota(ambulantenota);
        person.setIndigenaHombre(indigenah);
        person.setIndigenaMujer(indigenam);
        person.setImagenMapeo(pathPhoto);
        person.setImagenAcera(pathPhotoEstado);
        person.setCometarioAcera(edtComentarioAcera.getText().toString());
        person.setNota(edtNota.getText().toString());
        DbLocal dbLocal = new DbLocal(this);
        dbLocal.addPerson(realm, person);
    }

    private String getHora() {
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        return hourFormat.format(new java.util.Date()).toString();
    }

    private void iniciarActividad() {
        dialog.dismiss();
        startActivity(new Intent(PeopleCountActivity.this, PeopleCountActivity.class));
        finish();
    }

    private void cargarSpinner() {
        String[] condiciones = {"Parcialmente Soleado", "Soleado", "Parcialmente Nublado", "Totalmente Nublado",
                "Parcialmete Lluvioso", "Luvioso"};

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, condiciones);
        spCondiciones.setAdapter(adapter);
    }

    private void configComponents(Dialog dialogLayout) {

        edtCalle1 = (EditText) dialogLayout.findViewById(R.id.edtCalle1);
        edtCalle2 = (EditText) dialogLayout.findViewById(R.id.edtCalle2);
        edtCalle3 = (EditText) dialogLayout.findViewById(R.id.edtCalle3);
        edtTemperatura = (EditText) dialogLayout.findViewById(R.id.edtTemperatura);
        edtNota = (EditText) dialogLayout.findViewById(R.id.edtNota);
        spCondiciones = (Spinner) dialogLayout.findViewById(R.id.spCondicionesClimaticas);

        edtcountfijo = (EditText) dialogLayout.findViewById(R.id.edtcountfijo);
        edtnotafijo = (EditText)dialogLayout.findViewById(R.id.edtnotafijo);
        edtcountambulante = (EditText) dialogLayout.findViewById(R.id.edtcountambulante);
        edtnotaambulante = (EditText)dialogLayout.findViewById(R.id.edtnotaambulante);
        edtindigenah = (EditText)dialogLayout.findViewById(R.id.edtcountindigenah);
        edtindigenam = (EditText)dialogLayout.findViewById(R.id.edtcountindigenam);
        edtComentarioAcera = (EditText)dialogLayout.findViewById(R.id.edtComentarioAcera);

        btnGuardar = (Button)dialogLayout.findViewById(R.id.btnGuardar);
        btnCancelar = (Button)dialogLayout.findViewById(R.id.btnCacelar);

        btnGuardar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        if (swTerminado)
            btnCancelar.setEnabled(false);

        imgCameraEstado = (ImageButton)dialogLayout.findViewById(R.id.cameraestado);
        imgCamera = (ImageButton)dialogLayout.findViewById(R.id.camera);

        imgCamera.setOnClickListener(this);
        imgCameraEstado.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @OnClick(R.id.imgHombre)
    public void countHombre() {
        cargarInformacion(1, 1);
    }

    @OnClick(R.id.imgNinia)
    public void countNinia() {
        cargarInformacion(2, 1);
    }

    @OnClick(R.id.imgMujer)
    public void countMujer() {
        cargarInformacion(3, 1);
    }

    @OnClick(R.id.imgAnciano)
    public void countAbuelo() {
        cargarInformacion(4, 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGuardar:
                validator.validate();
                break;
            case R.id.btnCacelar:
                dialog.dismiss();
                swVentanaDatos = false;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case R.id.camera:
                obtenerImagen(1);
                break;
            case R.id.cameraestado:
                obtenerImagen(2);
                break;

        }
    }


    @Override
    public void onValidationSucceeded() {
        cargarDatosPerson();
        iniciarActividad();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
