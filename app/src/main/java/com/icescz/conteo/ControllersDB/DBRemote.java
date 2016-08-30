package com.icescz.conteo.ControllersDB;

import android.content.Context;
import android.util.Log;
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
import com.icescz.conteo.Helper;
import com.icescz.conteo.Models.Car;
import com.icescz.conteo.Models.Person;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class DBRemote {

    Context context;
    Realm realm;
    Cloudinary cloudinary;

    public DBRemote(Context context, Realm realm) {
        this.context = context;
        this.realm = realm;
    }

    public void uploadPerson(final Person person, final Integer option) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Helper.URL_PERSON_STORE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Datos Subidos Correctamente", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (option == 1) {
                            DbLocal dbLocal = new DbLocal(context);
                            dbLocal.addPerson(realm, person);
                        }

                        if (option == 2) {
                            Toast.makeText(context, "Error en le servidor", Toast.LENGTH_SHORT).show();
                        }

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


                params.put("imageMapeo", person.getImagenMapeo());
                params.put("imageAcera", person.getImagenAcera());
                return params;
            }
        };

        RequestQueue request = Volley.newRequestQueue(context);
        request.add(stringRequest);
    }

    public void uploadCar(final Car car) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Helper.URL_CAR_STORE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Datos Subidos Correctamente", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DbLocal dbLocal = new DbLocal(context);
                        dbLocal.addCar(realm, car);
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
                return params;
            }
        };

        RequestQueue request = Volley.newRequestQueue(context);
        request.add(stringRequest);
    }

}
