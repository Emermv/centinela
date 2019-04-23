package com.centinela;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.centinela.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

   private LinearLayout home;
   private LinearLayout advanced;
   private EditText user,name,last_name,password;
    BottomNavigationView navigation;
   private RadioButton female,male;
    public static final int RESULT_GALLERY = 0;
    ImageView imageView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   home.setVisibility(View.VISIBLE);
                    advanced.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_advanced:
                    home.setVisibility(View.GONE);
                    advanced.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        home=findViewById(R.id.home_register);
        advanced=findViewById(R.id.advanced_register);
        user=findViewById(R.id.user);
        name=findViewById(R.id.name);
        password=findViewById(R.id.password);
        last_name=findViewById(R.id.last_name);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        FloatingActionButton save_btn=findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            save(v);
            }
        });
        imageView=findViewById(R.id.avatar_preview);
        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent , RESULT_GALLERY );
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageView.setImageURI(data.getData());
    }
  private void show_tab(int id){
      if(id==1){
          advanced.setVisibility(View.VISIBLE);
          home.setVisibility(View.GONE);
          navigation.setSelectedItemId(R.id.navigation_advanced);
      }else{
          advanced.setVisibility(View.GONE);
          home.setVisibility(View.VISIBLE);
          navigation.setSelectedItemId(R.id.navigation_home);
      }
  }
    public void save(View view){
    if(name.getText().toString().isEmpty()){
        name.requestFocus();
        name.setError("Por favor ingrese su nombre!");
     }else{
       if(last_name.getText().toString().isEmpty()){
           last_name.requestFocus();
           last_name.setError("Por favor ingrese sus apellidos!");
       }else{
           if(user.getText().toString().isEmpty()){
               user.requestFocus();
               user.setError("Por favor ingrese su usuario");
                 show_tab(1);

           }else{
               if(user.getText().toString().length()<4){
                   user.requestFocus();
                   user.setError("El usuario debe tener por lo minimo 4 caractres");
               }else{
                   if(password.getText().toString().length()==0 ||  password.getText().toString().isEmpty()){
                     password.requestFocus();
                     password.setError("Por favor ingrese  una contraseña");
                   }else{
                       final User user_val=new User();
                       user_val.setName(name.getText().toString()+" "+last_name.getText().toString());
                       user_val.setUser(user.getText().toString());
                       user_val.setPassword(password.getText().toString());
                       user_val.setGender(male.isChecked()?"M":"F");
                       user_val.setRole(2);
                       DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                       Date date = new Date();
                       user_val.setCreation_date(dateFormat.format(date));
                        final DatabaseReference mDatabase;
                       mDatabase = FirebaseDatabase.getInstance().getReference("/users");
                       String key=mDatabase.push().getKey();
                       user_val.setId(key);
                       imageView.setDrawingCacheEnabled(true);
                       imageView.buildDrawingCache();
                       Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                       ByteArrayOutputStream baos = new ByteArrayOutputStream();
                       bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                       byte[] data = baos.toByteArray();
                       FirebaseStorage storage = FirebaseStorage.getInstance();
                       final StorageReference reference=storage.getReference().child("avatars/"+user_val.getUser()+".png");
                       UploadTask uploadTask = reference.putBytes(data);
                       Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                           @Override
                           public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
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
                                   user_val.setAvatar(downloadUri.toString());
                                   mDatabase.child(user_val.getUser()).setValue(user_val);
                                   Toast.makeText(RegisterActivity.this,"Usuario registrado correctamente",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                               } else {
                                  Log.d("FIREBASE","Error al subir el archivo");
                               }
                           }
                       });



                   }
               }
           }
       }
     }
    }

}
