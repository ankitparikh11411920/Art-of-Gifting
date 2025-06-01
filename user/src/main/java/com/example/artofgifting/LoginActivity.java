package com.example.artofgifting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 1;
    CircleImageView google;
    CircleImageView Phoneotp;
    Button login;
    //    EditText username;
//    EditText password;
    private TextInputLayout username;
    private TextInputLayout password;
    TextView txtreg;
    ProgressBar progressBar;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    FirebaseUser user = mAuth.getCurrentUser();
    //vars
    String contact;

    private GoogleSignInClient mGoogleSignInClient;
    private TextView forgot;
    private Button submit;
    private ProgressDialog dialog;
    private EditText forgotemail;

    @Override
    protected void onStart() {
        super.onStart();
        dialog = new ProgressDialog(this);


        if (isNetworkConnectionAvailable()) {
            final ScrollView scrollView = findViewById(R.id.login_home);

            if (user != null && user.isEmailVerified()) {
                scrollView.setVisibility(View.INVISIBLE);
                dialog.setMessage("Loading...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            if (dataSnapshot.child(user.getUid()).hasChild("fullname")) {
                                if (dataSnapshot.child(user.getUid()).hasChild("image_url")) {
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                    dialog.dismiss();
                                } else {
                                    startActivity(new Intent(LoginActivity.this, Upload_Image.class));
                                    finish();
                                    dialog.dismiss();
                                }
                            } else {
                                startActivity(new Intent(LoginActivity.this, Enter_register_data.class));
                                finish();
                                dialog.dismiss();
                            }
                        } else {
                            dialog.dismiss();
                            scrollView.setVisibility(View.VISIBLE);
                            onResume();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                    }
                });
            }
            else if(user != null && user.getPhoneNumber()!=null){
                scrollView.setVisibility(View.INVISIBLE);
                dialog.setMessage("Loading...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            if (dataSnapshot.child(user.getUid()).hasChild("fullname")) {
                                if (dataSnapshot.child(user.getUid()).hasChild("image_url")) {
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                    dialog.dismiss();
                                } else {
                                    startActivity(new Intent(LoginActivity.this, Upload_Image.class));
                                    finish();
                                    dialog.dismiss();
                                }
                            } else {
                                startActivity(new Intent(LoginActivity.this, Enter_register_data.class));
                                finish();
                                dialog.dismiss();
                            }
                        } else {
                            dialog.dismiss();
                            scrollView.setVisibility(View.VISIBLE);
                            onResume();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                    }
                });
            }else {
                dialog.dismiss();
                scrollView.setVisibility(View.VISIBLE);
                onResume();
            }
        } else {
            dialog.dismiss();
            startActivity(new Intent(LoginActivity.this, InternetConnectionActivity.class));
        }

    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            return true;
        } else {
            return false;
        }
    }


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        google = findViewById(R.id.user_googlesignin_login);
        username = findViewById(R.id.user_edt_email_login);
        password = findViewById(R.id.user_edt_password_login);
        login = findViewById(R.id.user_btn_sign_login);
        progressBar = findViewById(R.id.user_login_progressbar);
        login.setOnClickListener(this);
        forgot = findViewById(R.id.user_tv_forgotpassword_login);
        Phoneotp = findViewById(R.id.user_phoneotpsignin_login);
        txtreg = findViewById(R.id.user_tv_register_login);


        Phoneotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(LoginActivity.this, Phoneauth.class);
                startActivity(intent);
                progressBar.setVisibility(View.INVISIBLE);

            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        txtreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent i = new Intent(LoginActivity.this, Register.class);
                startActivity(i);
                progressBar.setVisibility(View.INVISIBLE);
            }

        });


        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotdialog();
            }
        });
    }

    private void forgotdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View rootview = LayoutInflater.from(LoginActivity.this)
                .inflate(R.layout.raw_dialog_password, null);
        builder.setView(rootview);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        submit = rootview.findViewById(R.id.btn_forgot);
        forgotemail = rootview.findViewById(R.id.edt_forgotpasswordemail);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = forgotemail.getText().toString().trim();
                if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//                    StyleableToast.makeText(LoginActivity.this,"Email is required",R.style.exampletoast).show();
                    forgotemail.setError("Enter a valid Email address");
                    forgotemail.requestFocus();
                }else {
                    forgotpassword(email);
                    alertDialog.dismiss();
                }
            }
        });


    }

    private void forgotpassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(LoginActivity.this, "Check email", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChildren()) {
                                        if (dataSnapshot.child(user.getUid()).hasChild("fullname")) {
                                            if (dataSnapshot.child(user.getUid()).hasChild("image_url")) {
                                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                finish();
                                                dialog.dismiss();
                                            } else {
                                                startActivity(new Intent(LoginActivity.this, Upload_Image.class));
                                                finish();
                                                dialog.dismiss();
                                            }
                                        } else {
                                            startActivity(new Intent(LoginActivity.this, Enter_register_data.class));
                                            finish();
                                            dialog.dismiss();
                                        }
                                    } else {
                                        startActivity(new Intent(LoginActivity.this, Enter_register_data.class));
                                        finish();
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    dialog.dismiss();
                                    Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Login failed with google", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_btn_sign_login:
                String email = username.getEditText().getText().toString().trim();
                String password = this.password.getEditText().getText().toString().trim();
                if (email.isEmpty()) {
                    username.setError("Email is required");
                }
                if (password.isEmpty()) {
                    this.password.setError("Password is required");
                    return;
                }
                if (password.length() < 6) {
                    this.password.setError("Password must have 6 characters");
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        final FirebaseUser user = mAuth.getCurrentUser();

                                        if (user.isEmailVerified()) {
                                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.hasChildren()) {
                                                        if (dataSnapshot.child(user.getUid()).hasChild("fullname")) {
                                                            if (dataSnapshot.child(user.getUid()).hasChild("image_url")) {
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                                finish();
                                                                dialog.dismiss();
                                                            } else {
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                startActivity(new Intent(LoginActivity.this, Upload_Image.class));
                                                                finish();
                                                                dialog.dismiss();
                                                            }
                                                        } else {
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            startActivity(new Intent(LoginActivity.this, Enter_register_data.class));
                                                            finish();
                                                        }
                                                    } else {
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        startActivity(new Intent(LoginActivity.this, Enter_register_data.class));
                                                        finish();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    dialog.dismiss();
                                                }
                                            });

//                                            checkimage();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Verify your email to sign in", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);

                                    }
                                }

                            });
                }
                break;
            case R.id.user_tv_forgotpassword_login:
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                View mview = getLayoutInflater().inflate(R.layout.raw_dialog_password, null);
                final EditText txt_inputtext = mview.findViewById(R.id.edt_forgotpasswordemail);
                Button forgotpass = mview.findViewById(R.id.btn_forgot);
                alert.setView(mview);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                break;
        }

    }

}

