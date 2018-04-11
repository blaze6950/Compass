package zna.online.compass.AuthorizationActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import zna.online.compass.MainActivity.MainMenuActivity;
import zna.online.compass.R;

public class SignInEmailActivity extends AppCompatActivity implements View.OnClickListener{

    private AutoCompleteTextView emailAutoCompleteTextView;
    private EditText passwordEditText;
    private Button signinButton;
    private ProgressDialog progressDialog;
    private TextView textViewSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_email);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        emailAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        signinButton = (Button) findViewById(R.id.email_sign_in_button);
        textViewSignIn = (TextView) findViewById(R.id.textViewSignIn);

        signinButton.setOnClickListener((View.OnClickListener) (this));
        textViewSignIn.setOnClickListener((View.OnClickListener) (this));
    }

    private void userLogin()
    {
        String email = emailAutoCompleteTextView.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            //email is empty
            Toast.makeText(this, "Please enter the correct email", Toast.LENGTH_SHORT).show();
            //stopping the function executing
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            //password is empty
            Toast.makeText(this, "Please enter the correct password", Toast.LENGTH_SHORT).show();
            //stopping the function executing
            return;
        }
        //if validating ok
        //we will first show a progressDialog
        progressDialog.setMessage("Sign in...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.cancel();
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Sign in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInEmailActivity.this, MainMenuActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == signinButton)
        {
            userLogin();
        }else if (v == textViewSignIn){
            startActivity(new Intent(SignInEmailActivity.this, SignUpEmailActivity.class));
        }

    }
}
