package com.centinela.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;


import com.centinela.model.Alert;
import com.centinela.model.Evidences;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AlertService {
    public void save(final ArrayList<File> evidence_list, final Context context, String description) throws FileNotFoundException {
        SharedPreferences prefs =
                context.getSharedPreferences("settings", Context.MODE_MULTI_PROCESS);
        final Alert alert =new Alert();

        final ProgressDialog dialog = ProgressDialog.show(context, "",
                "Enviando alerta... espere un momento", true);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        alert.setCreation_date(dateFormat.format(date));
        alert.setCreation_time(timeFormat.format(date));
        alert.setDevice_name(getDeviceName());
        alert.setDevice_version(Build.VERSION.CODENAME);
        alert.setUser_id(prefs.getString("user","0"));
        alert.setUser_avatar( prefs.getString( "avatar","" ) );
        alert.setUser_name( prefs.getString( "name","" ) );
        alert.setDescription(description);
        alert.setLat("3838833");
        alert.setLng("93873");
        alert.setDirection("Av Alameda San Marcos, Distrito de Chorrillos Lima Perú");
        alert.setShow( false );
        alert.setState( false );
        alert.setEvidences_size( evidence_list.size() );
        final ArrayList<Evidences> evidences1=new ArrayList<>();
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("/alerts");
        String key=mDatabase.push().getKey();
         alert.setId(key);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference reference=storage.getReference().child("alerts/"+alert.getId()+".png");
        for (int i=0;i<evidence_list.size();i++){

            Bitmap bitmap = BitmapFactory.decodeFile(evidence_list.get(i).getAbsolutePath());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, stream);
            byte[] byteArray = stream.toByteArray();
            bitmap.recycle();
            /* File file = new File("/temp/abc.txt");
  //init array with file length
  byte[] bytesArray = new byte[(int) file.length()];

  FileInputStream fis = new FileInputStream(file);
  fis.read(bytesArray); //read file into bytes[]
  fis.close();
			*/

            UploadTask uploadTask = reference.putBytes(byteArray);
            final int j=i;
            Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    dialog.dismiss();
                    if (!task.isSuccessful()) {
                        throw task.getException();

                    }
                    // Continue with the task to get the download URL
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Evidences e=new Evidences();
                        e.setPath(downloadUri.toString());
                        e.setTitle("evidencia 1");
                        evidences1.add(e);

                       if(j==evidence_list.size()-1){
                           mDatabase.child(alert.getId()).setValue(alert);

                           for (int k=0;k<evidences1.size();k++){
                               String file_key=mDatabase.child(alert.getId()).child("evidences").push().getKey();
                               mDatabase.child(alert.getId()).child("evidences").child(file_key).setValue(evidences1.get(k));
                           }
                           dialog.dismiss();
                           Toast.makeText(context,"Alerta enviada correctamente!", Toast.LENGTH_LONG).show();
                       }

                    } else {
                        Log.d("FIREBASE","Error al subir el archivo");
                    }
                }
            });
        }


    }
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
