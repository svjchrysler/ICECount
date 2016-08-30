package com.icescz.conteo.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.icescz.conteo.ControllersDB.DbLocal;
import com.icescz.conteo.Helper;
import com.icescz.conteo.Models.Car;
import com.icescz.conteo.Models.Person;
import com.icescz.conteo.R;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class CarsCountActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    @BindView(R.id.txtvCronometro)
    TextView txtvcronometro;

    @BindView(R.id.imgParticular)
    ImageView imgParticular;

    @BindView(R.id.imgBicicleta)
    ImageView imgBicicleta;

    @BindView(R.id.imgPublico)
    ImageView imgPublico;

    @BindView(R.id.imgRepartidor)
    ImageView imgRepartidor;

    @BindView(R.id.imgMotoclicleta)
    ImageView imgMotocicleta;

    @BindView(R.id.imgTaxi)
    ImageView imgTaxi;

    @BindView(R.id.txtparticular)
    TextView txtParticular;

    @BindView(R.id.txtbicicleta)
    TextView txtBicicleta;

    @BindView(R.id.txtpublico)
    TextView txtPublico;

    @BindView(R.id.txtrepartidor)
    TextView txtRepartidor;

    @BindView(R.id.txtmotoclicleta)
    TextView txtMotocicleta;

    @BindView(R.id.txttaxi)
    TextView txtTaxi;

    private Realm realm;
    private CountDownTimer timer;
    private Validator validator;
    private Vibrator vibrator;

    private Integer countParticular, countBicicleta, countPublico, countRepartidor, countMotocicleta, countTaxi;
    private boolean sw = false, swventana = false, swTerminado = false, swVentanaDatos = false;
    long hora, minutos, segundos, segundosTotales;

    private Bitmap bitmapPhoto;
    private CameraPhoto cameraPhoto;
    private String pathPhoto = "";

    private Bitmap bitmapPhotoEstado;
    private CameraPhoto cameraPhotoEstado;
    private String pathPhotoEstado = "";

    @NotEmpty(message = "El campo es obligatorio")
    EditText edtCalle1;

    @NotEmpty(message = "La calle de relevamiento es obligatorio")
    EditText edtCalle2;

    @NotEmpty(message = "La calle de relevamiento es obligatorio")
    EditText edtCalle3;

    @NotEmpty(message = "Temperatura obligatorio")
    EditText edtTemperatura;

    EditText edtNota, edtcountfijo, edtnotafijo, edtcountambulante,edtnotaambulante,edtindigenah,edtindigenam,edtComentarioAcera;

    Spinner spCondiciones, spinner;

    Button btnGuardar, btnCancelar;

    ImageButton imgCamera, imgCameraEstado;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_count);
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.opciones_cars, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK && swventana) {
            advertenciaClose();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void optionSelecionado() {
        switch (spinner.getSelectedItem().toString()) {
            case "Bicicleta":
                inicializar(1, -1);
                break;
            case "Taxi":
                inicializar(2, -1);
                break;
            case "Motocicleta":
                inicializar(3, -1);
                break;
            case "Particular":
                inicializar(4, -1);
                break;
            case "Publico":
                inicializar(5, -1);
                break;
            case "Repartidor":
                inicializar(6, -1);
                break;

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("bicicleta", countBicicleta);
        outState.putInt("taxi", countTaxi);
        outState.putInt("motocicleta", countMotocicleta);
        outState.putInt("particular", countParticular);
        outState.putInt("publico", countPublico);
        outState.putInt("repartidor", countRepartidor);
        outState.putInt("seconds", (int) segundosTotales);
        outState.putBoolean("sw", swventana);
        outState.putBoolean("ventana", swVentanaDatos);
        if (sw)
            timer.cancel();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        countBicicleta = savedInstanceState.getInt("bicicleta");
        countTaxi = savedInstanceState.getInt("taxi");
        countMotocicleta = savedInstanceState.getInt("motocicleta");
        countParticular = savedInstanceState.getInt("particular");
        countPublico = savedInstanceState.getInt("publico");
        countRepartidor = savedInstanceState.getInt("repartidor");

        txtBicicleta.setText("Total: " + countBicicleta);
        txtTaxi.setText("Total: " + countTaxi);
        txtMotocicleta.setText("Total: " + countMotocicleta);
        txtParticular.setText("Total: " + countParticular);
        txtPublico.setText("Total: " + countPublico);
        txtRepartidor.setText("Total: " + countRepartidor);
        swventana = savedInstanceState.getBoolean("sw");
        initCronometro(savedInstanceState.getInt("seconds"));

        if (savedInstanceState.getBoolean("ventana"))
            ventanaDatos();
    }

    private void configInit() {
        configCreate();
        inicializarVariables();
    }

    private void inicializarVariables() {
        countBicicleta = 0;
        countMotocicleta = 0;
        countParticular = 0;
        countPublico = 0;
        countRepartidor = 0;
        countTaxi = 0;
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
                txtvcronometro.setText("Terminado");
                swTerminado = true;
                ventanaDatos();
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
            txtvcronometro.setText(min + ":" + seg);
        } else {
            txtvcronometro.setText(minutos + ":" + segundos);
        }
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
                if (i == KeyEvent.KEYCODE_BACK && swventana) {
                    dialog.dismiss();
                    swVentanaDatos = false;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                return false;
            }
        });

        dialog.show();
    }

    private void iniciarActividad() {
        dialog.dismiss();
        startActivity(new Intent(CarsCountActivity.this, CarsCountActivity.class));
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

    private void inicializar(int param, int n) {
        swventana = true;
        vibracion();
        if (sw) {
            switch (param) {
                case 1:
                    if (countBicicleta > 0 || n > 0) {
                        countBicicleta += n;
                        txtBicicleta.setText("Total: " + countBicicleta.toString());
                    }

                    break;
                case 2:
                    if (countTaxi > 0 || n > 0) {
                        countTaxi += n;
                        txtTaxi.setText("Total: " + countTaxi.toString());
                    }
                    break;
                case 3:
                    if (countMotocicleta > 0 || n > 0) {
                        countMotocicleta += n;
                        txtMotocicleta.setText("Total: " + countMotocicleta.toString());
                    }
                    break;
                case 4:
                    if (countParticular > 0 || n > 0) {
                        countParticular += n;
                        txtParticular.setText("Total: " + countParticular.toString());
                    }
                    break;
                case 5:
                    if (countPublico > 0 || n > 0) {
                        countPublico += n;
                        txtPublico.setText("Total: " + countPublico.toString());
                    }
                    break;
                case 6:
                    if (countRepartidor > 0 || n > 0) {
                        countRepartidor += n;
                        txtRepartidor.setText("Total: " + countRepartidor.toString());
                    }
                    break;
            }
        } else {
            initCronometro(600);
            horafechainicio();
            inicializar(param, n);
        }
    }

    private void horafechainicio() {
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Helper.horaActual = hourFormat.format(new java.util.Date()).toString();
        Helper.fechaActual = dateFormat.format(new java.util.Date()).toString();
    }

    private void vibracion() {
        vibrator.vibrate(50);
    }

    private void obtenerImagen(int id) {
        imagenCamera(id);
    }

    private void imagenCamera(int id) {
        if (id==1)
            cameraPhoto = new CameraPhoto(CarsCountActivity.this);
        if (id == 2)
            cameraPhotoEstado = new CameraPhoto(CarsCountActivity.this);

        try {
            if (id == 1) {
                startActivityForResult(cameraPhoto.takePhotoIntent(), 1);
                cameraPhoto.addToGallery();
            }
            if (id == 2) {
                startActivityForResult(cameraPhotoEstado.takePhotoIntent(), 2);
                cameraPhotoEstado.addToGallery();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String photoPath = cameraPhoto.getPhotoPath();
            cargarImagen(photoPath, 1);
        }

        if (requestCode == 2) {
            String photoPath = cameraPhotoEstado.getPhotoPath();
            cargarImagen(photoPath, 2);
        }

    }

    protected void cargarImagen(String photoPath, int id) {
        if (id == 1)
            pathPhoto = photoPath;
        if (id == 2)
            pathPhotoEstado = photoPath;
        try {
            Bitmap imageBitmap = ImageLoader.init().from(photoPath).getBitmap();
            if (id == 1) {
                bitmapPhoto = imageBitmap;
                imgCamera.setImageBitmap(imageBitmap);
            }
            if (id == 2) {
                bitmapPhotoEstado = imageBitmap;
                imgCameraEstado.setImageBitmap(imageBitmap);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void advertenciaClose() {
        final AlertDialog.Builder alertAdvertencia = new AlertDialog.Builder(this);
        alertAdvertencia.setTitle("Advertencia");
        alertAdvertencia.setMessage("Seguro que quieres salir?");
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

    private void cargarDatosCar() {
        Integer fijoCantidad = !edtcountfijo.getText().toString().equals("")  ? Integer.parseInt(edtcountfijo.getText().toString()) : 0;
        String fijonota = edtnotafijo.getText().toString();
        Integer ambulantecantidad = !edtcountambulante.getText().toString().equals("") ? Integer.parseInt(edtcountambulante.getText().toString()) : 0;
        String ambulantenota = edtnotaambulante.getText().toString();
        Integer indigenah = !edtindigenah.getText().toString().equals("") ? Integer.valueOf(edtindigenah.getText().toString()) : 0;
        Integer indigenam = !edtindigenam.getText().toString().equals("") ? Integer.valueOf(edtindigenam.getText().toString()) : 0;
        pathPhoto = !pathPhoto.equals("") && !pathPhoto.equals(null) ? pathPhoto : "";
        pathPhotoEstado = !pathPhotoEstado.equals("") && !pathPhotoEstado.equals(null) ? pathPhotoEstado : "";

        Car car = new Car();
        car.setNombreEncuestador(Helper.nombre_encuestador);
        car.setParticular(countParticular);
        car.setBicicleta(countBicicleta);
        car.setMotocicleta(countMotocicleta);
        car.setTaxi(countTaxi);
        car.setRepartidor(countRepartidor);
        car.setPublico(countPublico);
        car.setCalleRelevamiento(edtCalle1.getText().toString().trim());
        car.setCalleLateralA(edtCalle2.getText().toString().trim());
        car.setCalleLateralB(edtCalle3.getText().toString().trim());
        car.setTemperatura(Integer.parseInt(edtTemperatura.getText().toString()));
        car.setCondiciones(spCondiciones.getSelectedItem().toString());
        car.setHoraInicio(Helper.horaActual);
        car.setHoraFin(getHora());
        car.setFecha(Helper.fechaActual);
        car.setFijoCantidad(fijoCantidad);
        car.setFijoNota(fijonota);
        car.setAmbulanteCantidad(ambulantecantidad);
        car.setAmbulanteNota(ambulantenota);
        car.setIndigenaHombre(indigenah);
        car.setIndigenaMujer(indigenam);
        car.setImagenMapeo(pathPhoto);
        car.setImagenAcera(pathPhotoEstado);
        car.setCometarioAcera(edtComentarioAcera.getText().toString());
        car.setNota(edtNota.getText().toString());
        DbLocal dbLocal = new DbLocal(this);
        dbLocal.addCar(realm, car);
    }

    private String getHora() {
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        return hourFormat.format(new java.util.Date()).toString();
    }

    @Override
    public void onValidationSucceeded() {
        cargarDatosCar();
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

    @OnClick(R.id.imgBicicleta)
    public void countBicicleta() {
        inicializar(1, 1);
    }

    @OnClick(R.id.imgTaxi)
    public void countTaxi() {
        inicializar(2, 1);
    }

    @OnClick(R.id.imgMotoclicleta)
    public void countMotocicleta() {
        inicializar(3, 1);
    }

    @OnClick(R.id.imgParticular)
    public void countParticular() {
        inicializar(4, 1);
    }

    @OnClick(R.id.imgPublico)
    public void countPublico() {
        inicializar(5, 1);
    }

    @OnClick(R.id.imgRepartidor)
    public void countRepartidor() {
        inicializar(6, 1);
    }
}
