package com.example.bloggerstictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class Register extends AppCompatActivity {
    EditText user,pass,name,mobilenumber;
    Button registerbutton;
    FirebaseAuth Auth;
    TextView loginbutton;
    GifImageView image;
    FirebaseFirestore fstore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        name = findViewById(R.id.name);
        mobilenumber = findViewById(R.id.number);
        user = findViewById(R.id.user1);
        pass = findViewById(R.id.pass1);
        registerbutton = findViewById(R.id.butregis);
        loginbutton = findViewById(R.id.logintext);
        image = findViewById(R.id.gif);

        Auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        if(Auth.getCurrentUser() != null ){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();}

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user.getText().toString().trim();
                String password = pass.getText().toString().trim();
                String fullname = name.getText().toString();
                String number = mobilenumber.getText().toString();



                if (TextUtils.isEmpty(email)){
                    user.setError("Email is required!");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    pass.setError("Password is required!!");
                    return;
                }
                if(password.length() < 6){
                    pass.setError("Password must be greater than or equal to 6 character long!");
                    return;
                }
                image.setVisibility(View.VISIBLE);


                //registraring the User

                Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "User Registered Sussessfully", Toast.LENGTH_SHORT).show();
                            userID = Auth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("Name",fullname);
                            user.put("email",email);
                            user.put("mobile",number);
                            user.put("userUid",userID);
                            user.put("status",null);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: user profile is created for "+ userID);
                                }
                            });
                           fstore.collection("users").document(userID).update("wins", FieldValue.increment(1));
                            fstore.collection("users").document(userID).update("wins", FieldValue.increment(-1));
                            fstore.collection("users").document(userID).update("played", FieldValue.increment(1));
                            fstore.collection("users").document(userID).update("played", FieldValue.increment(-1));
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));}

                        else{
                            Toast.makeText(Register.this, "Error! "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            image.setVisibility(View.INVISIBLE);
                        }

                    }
                });

            }
        });


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });



    }
}