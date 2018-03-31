package zna.online.compass;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInEmailActivity extends AppCompatActivity implements View.OnClickListener{

    private AutoCompleteTextView emailAutoCompleteTextView;
    private EditText passwordEditText;
    private Button signupButton;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_email);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        emailAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        signupButton = (Button) findViewById(R.id.email_sign_up_button);

        signupButton.setOnClickListener(this);
    }

    private void registerUser()
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
        progressDialog.setMessage("Sign up...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            //user is successfully registered and logged in
                            // we will start the profile activity here
                            // right now lets display a toast only
                            Toast.makeText(SignInEmailActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SignInEmailActivity.this, "Could not register. Please, try again", Toast.LENGTH_SHORT).show();
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
