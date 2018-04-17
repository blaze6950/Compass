package zna.online.compass.Test;

import android.os.Parcel;
import android.os.Parcelable;

public class TestResult implements Parcelable {
    public int mood;
    public int numberOfPeople;
    public int numberOfHours;
    public int averageCheck;

    public TestResult(int mood, int numberOfPeople, int numberOfHours, int averageCheck) {
        this.mood = mood;
        this.numberOfPeople = numberOfPeople;
        this.numberOfHours = numberOfHours;
        this.averageCheck = averageCheck;
    }

    public TestResult() {

    }

    protected TestResult(Parcel in) {
        int[] data = new int[4];
        in.readIntArray(data);

        mood = data[0];
        numberOfPeople = data[1];
        numberOfHours = data[2];
        averageCheck = data[3];
    }

    public static final Creator<TestResult> CREATOR = new Creator<TestResult>() {
        @Override
        public TestResult createFromParcel(Parcel in) {
            return new TestResult(in);
        }

        @Override
        public TestResult[] newArray(int size) {
            return new TestResult[size];
        }
    };

    public int getMoodLevel() {
        return mood;
    }

    public void setMoodLevel(int moodLevel) {
        this.mood = mood;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public int getMoney() {
        return averageCheck;
    }

    public void setMoney(int money) {
        this.averageCheck = money;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(new int[] {mood, numberOfPeople, numberOfHours, averageCheck});
    }

    public boolean CompareTestResults(TestResult testResult){
        if (CheckMood(testResult) && CheckNumberOfPeople(testResult) && CheckNumberOfHours(testResult) && CheckAverageCheck(testResult)){
            return true;
        }else{
            return false;
        }
    }

    private boolean CheckAverageCheck(TestResult testResult) {
        int buf = numberOfPeople - testResult.numberOfPeople;
        if (buf < 0){
            buf = buf * (-1);
        }
        if (buf <= 150){
            return true;
        }else{
            return false;
        }
    }

    private boolean CheckNumberOfHours(TestResult testResult) {
        int buf = numberOfPeople - testResult.numberOfPeople;
        if (buf < 0){
            buf = buf * (-1);
        }
        if (buf <= 1){
            return true;
        }else{
            return false;
        }
    }

    private boolean CheckNumberOfPeople(TestResult testResult) {
        int buf = numberOfPeople - testResult.numberOfPeople;
        if (buf < 0){
            buf = buf * (-1);
        }
        if (buf <= 4){
            return true;
        }else{
            return false;
        }
    }

    private boolean CheckMood(TestResult testResult) {
        if (mood < 4){
            if (testResult.mood < 4){
                return true;
            }else{
                return false;
            }
        }else if(mood > 7){
            if (testResult.mood > 7){
                return true;
            }else{
                return false;
            }
        }else{
            if (testResult.mood <= 7 && testResult.mood >= 4){
                return true;
            }else{
                return false;
            }
        }
    }
}
