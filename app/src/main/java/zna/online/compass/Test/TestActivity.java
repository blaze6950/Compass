package zna.online.compass.Test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import zna.online.compass.R;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textView, textViewUah;
    private BubbleSeekBar seekBar;
    private Button cancelButton, nextButton;
    private TestResult testResult;
    private EditText editText;
    private int stage;

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
        testResult = new TestResult();
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
                seekBar.getConfigBuilder().max(20).sectionCount(19).progress(1);
                stage++;
                break;
            case 2:
                testResult.setNumberOfPeople(seekBar.getProgress());
                textView.setText("Как долго вы собираетесь отдыхать?\n(Укажите значение в часах)");
                seekBar.getConfigBuilder().max(10).sectionCount(9).progress(1);
                stage++;
                break;
            case 3:
                testResult.setNumberOfHours(seekBar.getProgress());
                textView.setText("Сколько примерно денег вы готовы потратить?");
                seekBar.setVisibility(View.GONE);
                textViewUah.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
                stage++;
                break;
            case 4:
                testResult.setMoney(seekBar.getProgress());
                //start activity testResult;
                break;
        }
    }
}
