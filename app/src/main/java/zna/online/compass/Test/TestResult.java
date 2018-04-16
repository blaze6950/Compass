package zna.online.compass.Test;

public class TestResult {
    private int moodLevel;
    private int numberOfPeople;
    private int numberOfHours;
    private int money;

    public TestResult(int moodLevel, int numberOfPeople, int numberOfHours, int money) {
        this.moodLevel = moodLevel;
        this.numberOfPeople = numberOfPeople;
        this.numberOfHours = numberOfHours;
        this.money = money;
    }

    public TestResult() {

    }

    public int getMoodLevel() {
        return moodLevel;
    }

    public void setMoodLevel(int moodLevel) {
        this.moodLevel = moodLevel;
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
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
