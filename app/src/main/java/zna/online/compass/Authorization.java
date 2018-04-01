package zna.online.compass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Authorization extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        //startActivity(new Intent(this, SignInEmailActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button:
                startActivity(new Intent(Authorization.this, SignInEmailActivity.class));
                break;
            case R.id.sign_up_button:
                startActivity(new Intent(Authorization.this, SignUpEmailActivity.class));
                break;
        }
    }
}
