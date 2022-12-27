package com.example.exemplededatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText email,pass,repass;
    Button creation;
     ProgressBar progress;
    TextView logintext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recuperation

        email=findViewById(R.id.username);
        pass=findViewById(R.id.password);
        repass=findViewById(R.id.repassword);
        creation=findViewById(R.id.btnsign);
        progress = findViewById(R.id.progress_bar);
        logintext =findViewById(R.id.login_text);
        ////////////////fermeture si ok
        creation.setOnClickListener(v -> CreateAccount());
        logintext.setOnClickListener(v-> finish());

    }

    ///////
    void CreateAccount (){
        String email_edit = email.getText().toString();
        String pass_edit = pass.getText().toString();
        String repasse_dit= repass.getText().toString();

     boolean  isvalidated = validationData (email_edit ,pass_edit,repasse_dit);
     if(!isvalidated ){
         return;
     }


         ///partiefirebase

        CreateAccountInFirebase(email_edit,pass_edit );


    }
    void  CreateAccountInFirebase(String email_edit,String pass_edit ){
        changeInProgress (true);

        FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email_edit,pass_edit).addOnCompleteListener(MainActivity.this,

                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress (false);
                     if (task.isSuccessful()) {
                         //////////////creation du compte est fait

                         Toast.makeText(MainActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                         firebaseAuth.getCurrentUser().sendEmailVerification();
                         firebaseAuth.signOut();
                         finish();
                     } else{
                         /////////echec faire

                         Toast.makeText(MainActivity.this,"vous avez un compte",Toast.LENGTH_SHORT).show();

                     }


                    }
                }

        );



    }
    ///////////////// progr

    void changeInProgress (boolean inProgress){

        if(inProgress){
            progress.setVisibility(View.VISIBLE);
            creation.setVisibility(View.GONE);
        } else{
            progress.setVisibility(View.GONE);
            creation.setVisibility(View.VISIBLE);
        }
    }


    ////////////////////////////////
    boolean validationData(String email_edit,String pass_edit,String repasse_dit)
    {     //validation
       if(!Patterns.EMAIL_ADDRESS.matcher(email_edit).matches()) {
           email.setError("email invalide");
           return false ;
    }//////////////////////
    if(pass_edit.length() <6){
        pass.setError("la longueur du mot de passe  est invalidée ");
        return false;
    }///////////////////////

    if(pass_edit.equals(repasse_dit))
    {
        repass.setError("le mot de pass n'est pas correct");
        return false ;


    }
    ///////////////////confirmation

    return true ;
    }
}