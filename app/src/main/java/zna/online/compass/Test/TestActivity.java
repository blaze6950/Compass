package zna.online.compass.Test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.List;

import zna.online.compass.BestLocation;
import zna.online.compass.PlaceAndEvent;
import zna.online.compass.R;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textView, textViewUah;
    private BubbleSeekBar seekBar;
    private Button cancelButton, nextButton;
    private TestResult testResult;
    private EditText editText;
    private ImageView doneImageView;
    private TextView doneTextView;
    private ProgressBar progressBar;
    private Context context;
    private int stage;

    private List<PlaceAndEvent> resultsList;
    private List<PlaceAndEvent> smallResultsList;

    private BestLocation bestLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        seekBar = (BubbleSeekBar) findViewById(R.id.test_seekbar);
        cancelButton = (Button) findViewById(R.id.test_cancel_button);
        nextButton = (Button) findViewById(R.id.test_next_button);
        textView = (TextView) findViewById(R.id.test_textView);
        textViewUah =(TextView) findViewById(R.id.test_textViewUah);
        editText = (EditText) findViewById(R.id.test_editText);
        doneImageView = (ImageView) findViewById(R.id.testDone);
        doneTextView = (TextView) findViewById(R.id.testDoneText);
        progressBar = (ProgressBar) findViewById(R.id.test_progress_bar);

        context = this;

        testResult = new TestResult();
        stage = 1;

        resultsList = new ArrayList<>();
        smallResultsList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if(v == cancelButton){
            finish();
        }else if(v == nextButton){
            GoNextAction();
        }
    }

    private void GoNextAction() {
        switch(stage){
            case 1:
                testResult.setMoodLevel(seekBar.getProgress());
                textView.setText("Укажите количество людей");
                seekBar.getConfigBuilder().max(20).sectionCount(19).progress(1).build();
                stage++;
                break;
            case 2:
                testResult.setNumberOfPeople(seekBar.getProgress());
                textView.setText("Как долго вы собираетесь отдыхать?\n(Укажите значение в часах)");
                seekBar.getConfigBuilder().max(10).sectionCount(9).progress(1).build();
                stage++;
                break;
            case 3:
                testResult.setNumberOfHours(seekBar.getProgress());
                textView.setText("Сколько примерно денег\nвы готовы потратить?");
                seekBar.setVisibility(View.GONE);
                textViewUah.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
                stage++;
                break;
            case 4:
                if (editText.getText().length() != 0){
                    testResult.setMoney(Integer.parseInt(editText.getText().toString()));
                    LoadSuccessScreen();
                }
                break;
        }
    }

    private void LoadSuccessScreen() {
        textView.setVisibility(View.GONE);
        textViewUah.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);

        doneImageView.setVisibility(View.VISIBLE);
        doneTextView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        DelayProgressBar();
    }

    private void DelayProgressBar() {
        final long changeTime = 50L;
        progressBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(progressBar.getProgress() + 2);
                if (progressBar.getProgress() == 100){
                    Intent intent = new Intent(context, TestResultActivity.class);
                    intent.putExtra("testResult", testResult);
                    context.startActivity(intent);
                }else{
                    DelayProgressBar();
                }
            }
        }, changeTime);
    }

}
