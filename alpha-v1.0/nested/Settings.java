package nested;

public class Settings {
    String darkMode;
    String warning;
    String breakTime;
    String breakLength;
    String runTime;

    public void setDarkMode (String set) {
        this.darkMode = set;
    }

    public void setWarning (String set) {
        this.warning = set;
    }

    public void setBreakTime (String set) {
        this.breakTime = set;
    }

    public void setBreakLength (String set) {
        this.breakLength = set;
    }
 
    public void setRunTime (String set) {
        this.runTime = set;
    }

    public String getDarkMode() {
        return this.darkMode;
    }

    public String getWarning() {
        return this.warning;
    }

    public String getBreakTime() {
        return this.breakTime;
    }

    public String getBreakLength() {
        return this.breakLength;
    }

    public String getRunTime() {
        return this.runTime;
    }

}