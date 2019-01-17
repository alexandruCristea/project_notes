package com.example.alexandrucristea.memories;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private Button buttonRegister;
  private EditText editTextEmail;
  private EditText editTextPassword;
  private TextView textViewSingin;

  private ProgressDialog progressDialog;

  private FirebaseAuth firebaseAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    firebaseAuth = FirebaseAuth.getInstance();

    if (firebaseAuth.getCurrentUser() != null) {
      finish();
      startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    progressDialog = new ProgressDialog(this);

    buttonRegister = (Button) findViewById(R.id.buttonRegister);

    editTextEmail = (EditText) findViewById(R.id.editTextEmail);
    editTextPassword = (EditText) findViewById(R.id.editTextPassword);

    textViewSingin = (TextView) findViewById(R.id.textViewSignIn);

    buttonRegister.setOnClickListener(this);
    textViewSingin.setOnClickListener(this);


  }

  private void registerUser () {
    String email = editTextEmail.getText().toString().trim();
    String password = editTextPassword.getText().toString().trim();

    if(TextUtils.isEmpty(email)){
      Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
      return;
    }

    if(TextUtils.isEmpty(password)){
      Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
      return;
    }

    progressDialog.setMessage("Registering User...");
    progressDialog.show();

    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
          Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
          progressDialog.hide();

            //home activity
            finish();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

        }

        else {
          Toast.makeText(MainActivity.this, "Could not register...please try again", Toast.LENGTH_SHORT).show();
          progressDialog.hide();
        }
      }
    });

  }
  @Override
  public void onClick(View view) {
    if(view == buttonRegister) {
      registerUser();
    }

    if(view == textViewSingin) {
      //will open login activity here
      startActivity(new Intent(this, LoginActivity.class));
    }
  }
}
