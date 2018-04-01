package zna.online.compass;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpEmailActivity extends AppCompatActivity implements View.OnClickListener{

    private AutoCompleteTextView emailAutoCompleteTextView;
    private EditText passwordEditText;
    private Button signupButton;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_email);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        emailAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        signupButton = (Button) findViewById(R.id.email_sign_up_button);

        signupButton.setOnClickListener((View.OnClickListener)(this));
    }

    private void registerUser()
    {
        String email = emailAutoCompleteTextView.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty())
        {
            //email is empty
            emailAutoCompleteTextView.setError("Email is required");
            emailAutoCompleteTextView.requestFocus();
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            //stopping the function executing
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailAutoCompleteTextView.setError("Please enter a valid email");
            emailAutoCompleteTextView.requestFocus();
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            //stopping the function executing
            return;
        }

        if (password.isEmpty())
        {
            //password is empty
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            //stopping the function executing
            return;
        }

        if (password.length() < 6)
        {
            passwordEditText.setError("Minimum length of password should be six");
            passwordEditText.requestFocus();
            Toast.makeText(this, "Minimum length of password should be six", Toast.LENGTH_SHORT).show();
            //stopping the function executing
            return;
        }
        //if validating ok
        //we will first show a progressDialog
        progressDialog.setMessage("Sign up...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.cancel();
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "User registered error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == signupButton)
        {
            registerUser();
        }
    }
}
