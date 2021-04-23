package com.AndroidTechCrew.technews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActvity extends AppCompatActivity  {
    private static final String TAG = "CreateAccountActvity";
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignUp;
    private FirebaseAuth mAuth;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText usernameEditText;
    private String lastNameInput;
    private String firstNameInput;
    private String usernameInput;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_actvity);
        editTextEmail = findViewById(R.id.etSignUpEmail);
        editTextPassword = findViewById(R.id.etSignUpPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
//        firstNameEditText = findViewById(R.id.fNameDialog);
//        lastNameEditText = findViewById(R.id.lNameDialog);
//        usernameEditText = findViewById(R.id.usernameDialog);

        mAuth = FirebaseAuth.getInstance();
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                if (email.isEmpty()) {
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    editTextPassword.setError("Password is required");
                    editTextPassword.requestFocus();
                    return;
                }
                if (!email.isEmpty() && !password.isEmpty()) {
                    createDiaglog();
                }
            }
        });
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            uploadUser(user);
                            updateUI(user);

                            Toast.makeText(CreateAccountActvity.this, "User Creation Success", Toast.LENGTH_SHORT).show();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActvity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //go to HomeActivity
            updateUI(currentUser);
        }
    }

    private void updateUI(FirebaseUser user) {
        //takes user to home activity
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
    }


    private void uploadUser(FirebaseUser user){
        Map<String, String> userDoc = new HashMap<>();
        userDoc.put("email", user.getEmail());
        userDoc.put("firstName",firstNameEditText.getText().toString());
        userDoc.put("lastName",lastNameEditText.getText().toString());
        userDoc.put("Username",usernameEditText.getText().toString());
        userDoc.put("uid", user.getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid())
                .set(userDoc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    public void createDiaglog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View accountPopup = getLayoutInflater().inflate(R.layout.dialog_sign_in,null);
        firstNameEditText = (EditText) accountPopup.findViewById(R.id.fNameDialog);
        lastNameEditText = (EditText) accountPopup.findViewById(R.id.lNameDialog);
        usernameEditText = (EditText) accountPopup.findViewById(R.id.usernameDialog);
        dialogBuilder.setView(accountPopup);
        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firstNameInput = firstNameEditText.getText().toString();
                lastNameInput = lastNameEditText.getText().toString();
                usernameInput = usernameEditText.getText().toString();
//                if(firstNameInput.isEmpty()) {
//                    firstNameEditText.setError("Name is required");
//                    firstNameEditText.requestFocus();
//                    return;
//                }
//                if(lastNameInput.isEmpty()) {
//                    lastNameEditText.setError("Name is required");
//                    lastNameEditText.requestFocus();
//                    return;
//                }
//
//
//                if (usernameInput.isEmpty()) {
//                    usernameEditText.setError("Username is required");
//                    usernameEditText.requestFocus();
//                    return;
//                }

                if(!firstNameInput.isEmpty() && !lastNameInput.isEmpty() && !usernameInput.isEmpty()){
                    String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();
                    createUser(email,password);
//                    Toast.makeText(CreateAccountActvity.this, "Name is " + nameInput + "Username is " + usernameInput, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(CreateAccountActvity.this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Toast.makeText(CreateAccountActvity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });

        dialog = dialogBuilder.create();
        dialog.show();

    }
}