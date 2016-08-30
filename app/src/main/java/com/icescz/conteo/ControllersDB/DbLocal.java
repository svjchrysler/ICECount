package com.icescz.conteo.ControllersDB;


import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.icescz.conteo.Activities.PeopleCountActivity;
import com.icescz.conteo.Helper;
import com.icescz.conteo.Models.Car;
import com.icescz.conteo.Models.Person;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

public class DbLocal {

    public Context context;

    public DbLocal(Context context) {
        this.context = context;
    }

    public void addPerson(Realm realm, final Person objPerson) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Person person = bgRealm.createObject(Person.class);
                int id;
                try {
                    id = bgRealm.where(Person.class).max("id").intValue() + 1;
                }catch (ArrayIndexOutOfBoundsException e) {
                    id = 1;
                }
                cargarObjetoPersona(objPerson, person, id);
            }

        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "Datos Guardados Correctamente", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(context, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addCar(Realm realm, final Car objCar) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                Car car = bgRealm.createObject(Car.class);
                int id;
                try {
                    id = bgRealm.where(Car.class).max("id").intValue() + 1;
                }catch (ArrayIndexOutOfBoundsException e) {
                    id = 1;
                }
                cargarObjetoAuto(objCar, car, id);
            }

        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "Datos Guardados Correctamente", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(context, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void eliminarPerson(Realm realm, final Person person) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Person objPerson = realm.where(Person.class).equalTo("id", person.getId()).findFirst();
                objPerson.deleteFromRealm();
            }
        });
    }

    public void eliminarCar(Realm realm, final Car car) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Car objCar = realm.where(Car.class).equalTo("id", car.getId()).findFirst();
                objCar.deleteFromRealm();
            }
        });
    }

    private void cargarObjetoPersona(Person objPerson, Person person, int id) {
        person.setId(id);
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
    }

    private void cargarObjetoAuto(Car objCar, Car car, int id) {
        car.setId(id);
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
        car.setHoraFin(getHora());
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
    }

    public void refres_views(Realm realm) {
        RealmResults<Person> personRealmResults = realm.where(Person.class).findAll();
        String output = "";
        for (Person person: personRealmResults) {
            output+=person.toString();
        }
    }

    private String getHora() {
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        return hourFormat.format(new java.util.Date()).toString();
    }

}
