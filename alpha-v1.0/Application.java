
// Imports
import javax.swing.*;

import nested.Algo;
import nested.swapFix;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

import nested.Window;

public class Application extends JFrame implements ActionListener, ItemListener {
    // Serial Version UID
    private static final long serialVersionUID = 1L;
    // Variables
    private int loginRem = 0;
    String[] settingPreferences;
    int keyPanelSizeWidth = 800;
    int keyPanelSizeHeight = 450;
    int repeat;
    String[][] excersizeMatrix;
    // Get number of sets
         int setCount;
         int setsDone;
        // Key Panels
    JPanel motherPanel, login, survey, settings, action, directory;
    JButton loginLoginButton, directoryStart, directorySurvey, directorySettings, settingsLogout, settingsCancel, settingsApply, settingsRestore, settingsBack, actionStop, actionPause;
    JTextField loginUsername, loginPassword;
    JCheckBox loginRemember, settingsWarning, settingsDarkMode;
    JComboBox<String> settingsWorkTime, settingsRunTime, settingsBreakLength;
    JLabel actionTimer;

    Color black = Color.BLACK;
    Color white = Color.WHITE;
    Color gray = new java.awt.Color(128, 128, 128);
    Color blue = new java.awt.Color(102, 255, 255);
    Color salmon = new java.awt.Color(255, 102, 102);
    Color orange = new java.awt.Color(255, 128, 0);

    // App run ints
    int secondsRemaining, runType;

    public Application() {
        super("Application");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(200,200, keyPanelSizeWidth, keyPanelSizeHeight);
        this.setTitle("Ergonomic");
        this.setResizable(false);

        // Call startup functrions
        createPanels();
        checkCredentials();

        // Add Listeners
        loginLoginButton.addActionListener(this);
        loginRemember.addItemListener(this);
        settingsLogout.addActionListener(this);
        directorySettings.addActionListener(this);
        directoryStart.addActionListener(this);
        directorySurvey.addActionListener(this);
        settingsApply.addActionListener(this);
        settingsCancel.addActionListener(this);
        settingsRestore.addActionListener(this);
        settingsBack.addActionListener(this);
        actionPause.addActionListener(this);
        actionStop.addActionListener(this);

        // Add Panels
        motherPanel.add(login);
        motherPanel.add(survey);
        motherPanel.add(settings);
        motherPanel.add(action);
        motherPanel.add(directory);
        this.add(motherPanel);

        // Set visible
        this.setVisible(true);
    }

    //
    // Utility Functions
    //

        // File reader

        private String[] appFileReader(String filename) {
            // Check file line size
            int count = 0;
            try {
                InputStream is = new BufferedInputStream(new FileInputStream(filename));
                try {

                    byte[] c = new byte[1024];
                    count = 1;
                    int readChars = 0;
                    //boolean empty = true;
                    while ((readChars = is.read(c)) != -1) {
                        //empty = false;
                        for (int i = 0; i < readChars; ++i) {
                            if (c[i] == '\n') {
                                count++;
                            }
                        }
                    }
                    is.close();
                } catch (NumberFormatException err) {} 
                catch (IOException err) {}
            } catch (FileNotFoundException err) {}
            String[] lines = new String[count];

            // input lines into an array
            try {
                BufferedReader read = new BufferedReader(new FileReader(filename));
                try{
                    for (int k = 0; k < count; k++) {
                        lines[k] = read.readLine();
                    }
                    read.close();
                } catch(IOException err) {} 
                catch(NumberFormatException err){}
            } catch(FileNotFoundException err){}

            // Return Array
            return lines;
        }


        // File Writer

        private void appFileWriter(String filename, String[] lines) {
            try {
                PrintWriter writer = new PrintWriter(filename);
                for (int l = 0; l < lines.length; l++) {
                    if (l == (lines.length - 1)) {
                        writer.print(lines[l]);
                    } else {
                        writer.println(lines[l]);
                    }
                }
                writer.close();
            } catch (FileNotFoundException err) {}
        }

        // Setup app coloring (light or dark)

        private void appColourer(int type) {
            Color sysCol;
            if (type == 0) {
                // Light mode
                sysCol = white;
            } else if (type == 1){
                // Dark mode
                sysCol = gray;
            } else {
                sysCol = white;
            }
            motherPanel.setBackground(sysCol);
            login.setBackground(sysCol);
            settings.setBackground(sysCol);
            directory.setBackground(sysCol);
        }



    //
    // Lgoin Procedure Functions
    //

        // Create Panels
    
    private void createPanels() {
        
        motherPanel = new JPanel();

        //
        // Login
        login = new JPanel();
        login.setBounds(0, 0, keyPanelSizeWidth, keyPanelSizeHeight);
        login.setVisible(false);

        loginLoginButton = new JButton("Login");
        loginRemember = new JCheckBox("Remember Me");
        loginUsername = new JTextField("username");
        loginPassword = new JTextField("password");

        login.add(loginLoginButton);
        login.add(loginRemember);
        login.add(loginUsername);
        login.add(loginPassword);

        //
        // Directory
        directory = new JPanel();
        directory.setVisible(false);

        directorySettings = new JButton("Settings");
        directoryStart = new JButton("Begin");
        directorySurvey = new JButton("Survey");

        directory.add(directoryStart);
        directory.add(directorySurvey);
        directory.add(directorySettings);

        //
        // Survey
        survey = new JPanel(); 
        survey.setBounds(0, 0, keyPanelSizeWidth, keyPanelSizeHeight);
        survey.setVisible(false);

        //
        // Settings
        settings = new JPanel();
        settings.setBounds(0, 0, keyPanelSizeWidth, keyPanelSizeHeight);
        settings.setVisible(false);

        settingsLogout = new JButton("Logout");
        settingsApply = new JButton("Apply");
        settingsCancel = new JButton("Cancel");
        settingsRestore = new JButton("Restore Defaults");
        settingsBack = new JButton("Back");
        settingsDarkMode = new JCheckBox();
        settingsWarning = new JCheckBox();
        String[] WorkTime = {"30 Minutes", "45 Minutes", "1 Hour (Recommended)", "75 Minutes", "90 Minutes"};
        settingsWorkTime = new JComboBox<String>(WorkTime);
        String[] RunTime = {"4.5 Hours", "6 Hours", "9 Hours", "Constant"};
        settingsRunTime = new JComboBox<String>(RunTime);
        String[] BreakLength = {"2.5 Minutes", "5 Minutes", "CVB (Recommended)"};
        settingsBreakLength = new JComboBox<String>(BreakLength);

        settings.add(settingsApply);
        settings.add(settingsCancel);
        settings.add(settingsRestore);
        settings.add(settingsBack);
        settings.add(settingsDarkMode);
        settings.add(settingsWarning);
        settings.add(settingsWorkTime);
        settings.add(settingsRunTime);
        settings.add(settingsBreakLength);
        settings.add(settingsLogout);

        // Read file and setup the choices accordingly

        try {
            settingPreferences = appFileReader("db/preferences.txt");
            // Setup system darkmode or lightmode

            if (settingPreferences[0].equals("0")) {
                appColourer(0);
            } else if (settingPreferences[0].equals("1")) {
                appColourer(1);
            }
        } catch(ArrayIndexOutOfBoundsException err) {
           // Default Settings 
           appColourer(0);
           settingPreferences = new String[5];
           settingPreferences[0] = "0";
           settingPreferences[1] = "1";
           settingPreferences[2] = "60";
           settingPreferences[3] = "6";
           settingPreferences[4] = "c";
           appFileWriter("db/preferences.txt", settingPreferences);
        }

        // Set all swing components into the proper position
        // Dark Mode
        if (settingPreferences[0].equals("0")){
            settingsDarkMode.setSelected(false);
        } else {
            settingsDarkMode.setSelected(true);
        }
        // 30 Second Warning Gui
        if (settingPreferences[1].equals("0")){
            settingsWarning.setSelected(false);
        } else {
            settingsWarning.setSelected(true);
        }
        // Length in between breaks
        if (settingPreferences[2].equals("30")) {
            settingsWorkTime.setSelectedIndex(0);
        } else if (settingPreferences[2].equals("45")) {
            settingsWorkTime.setSelectedIndex(1);
        } else if (settingPreferences[2].equals("60")) {
            settingsWorkTime.setSelectedIndex(2);
        } else if (settingPreferences[2].equals("75")) {
            settingsWorkTime.setSelectedIndex(3);
        } else {
            settingsWorkTime.setSelectedIndex(4);
        }
        // Run Time
        if (settingPreferences[3].equals("4.5")) {
            settingsRunTime.setSelectedIndex(0);
        } else if (settingPreferences[3].equals("6")) {
            settingsRunTime.setSelectedIndex(1);
        } else if (settingPreferences[3].equals("9")) {
            settingsRunTime.setSelectedIndex(2);
        } else {
            settingsRunTime.setSelectedIndex(3);
        }
        // Break Length
        if (settingPreferences[4].equals("2.5")) {
            settingsBreakLength.setSelectedIndex(0);
        } else if (settingPreferences[4].equals("5")) {
            settingsBreakLength.setSelectedIndex(1);
        } else {
            settingsBreakLength.setSelectedIndex(2);
        }


        //
        // Begin Tab
        action = new JPanel();
        action.setBounds(0, 0, keyPanelSizeWidth, keyPanelSizeHeight);
        action.setVisible(false);

        actionTimer = new JLabel("xx:xx");
        actionPause = new JButton("Pause");
        actionStop = new JButton("Stop");

        action.add(actionTimer);
        action.add(actionPause);
        action.add(actionStop);

    }

        // Check bootup succeeded and call from there

    private void checkCredentials() {
        String[] credentials = appFileReader("db/userSave.txt");
        try{
            if (credentials[0].equals("abed") && credentials[1].equals("password")) {
                // Login Authorized
                login.setVisible(false);
                directory.setVisible(true);
            } else {
                // Login Unauthorized
                login.setVisible(true);
            }
        } catch (ArrayIndexOutOfBoundsException err) {
            //Save file unpopulate
            login.setVisible(true);
        }
    }


    // Recursive timer function
    private void timerFunction(String[][] excersizeMatrix) {
        try {
            // Update JLabel
            int minute = (int) Math.floor(secondsRemaining / 60);
            int secondCalc = secondsRemaining - (minute * 60);
            String second;
            if (secondCalc < 10) {
                second = "0" + secondCalc;
            } else {
                second = "" + secondCalc;
            }
            actionTimer.setText(minute + ":" + second);
            TimeUnit.SECONDS.sleep(1);
            secondsRemaining--;
        } catch (InterruptedException err) {}
        // Stop Function
        if (runType == 1) {
            directory.setVisible(true);
            action.setVisible(false);
            return;
        }
        // Pause Function
        else if (runType == 2) {
            return;
        } else {
            if (secondsRemaining == 0) {
                // Start the window
                startWindow(excersizeMatrix);
                // Recall next set
                if (setsDone<setCount) {
                    secondsRemaining = (int) (Integer.parseInt(settingPreferences[2]) * 60);
                    setsDone++;
                    timerFunction(excersizeMatrix);
                    return;
                } else {
                    if (repeat == 1) {
                        setsDone = 0;
                        startRun();
                        return;
                    } else {
                        // Clear appdb.txt
                        String[] clear = {""};
                        appFileWriter("db/appdb.txt", clear);
                        // Close tab
                        action.setVisible(false);
                        directory.setVisible(true);
                        return;
                    }
                }
            } else {
                timerFunction(excersizeMatrix);
            }
            return;
        }
    }

    private void startWindow(String[][] excersizeMatrix) {
        // Populate appdb.txt
        String[] currentSet = new String[4];
        for (int g = 0; g < 4; g++) {
            currentSet[g] = excersizeMatrix[setsDone][g];
        }
        appFileWriter("db/appdb.txt", currentSet);
        // Call window application with the appropriate information
        new Window();
        return;
    }


    private void startRun() {

        // Insure that the distrubution has been populated
        String[] values = appFileReader("db/distrubution.txt");
        try {
            values[4] = values[4];
        } catch (ArrayIndexOutOfBoundsException err) {
            // Leave tab
            action.setVisible(false);
            directory.setVisible(true);
            // Pop up to insure that the user goes to survey
            SwingWorker optionWindow= new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    int result = JOptionPane.showConfirmDialog(null, "We need some info on you, you must complete the survey.\n Go there now?", "Alert", JOptionPane.YES_NO_OPTION);
                    switch (result) {
                       case JOptionPane.YES_OPTION:
                       directory.setVisible(false);
                       survey.setVisible(true);
                       break;
                       case JOptionPane.NO_OPTION:
                       break;
                       case JOptionPane.CLOSED_OPTION:
                       break;
                    }
                    return null;
                }
            };
            optionWindow.execute();
            return;
        }


        // Test for constnat repition
        if (settingPreferences[3].equals("c")) {
            repeat = 1;
            setCount = (int) Math.floor(1440 / Integer.parseInt(settingPreferences[2], 10));
        } else {
            repeat = 0;
            setCount = (int) (Integer.parseInt(settingPreferences[3], 10) * 60) / Integer.parseInt(settingPreferences[2], 10);
        }
        // Figure out algorithm type that will be used and use it
        excersizeMatrix = new String[setCount][4];
        if (settingPreferences[4].equals("c")) {
            // Use swapping algorithm with 4 objects per set

            // First calculate rates based on float values
            Integer[] rates = new Integer[values.length];
            for (int i = 0; i < values.length; i++) {
                rates[i] = (int) Math.floor(Double.parseDouble(values[i]) * setCount * 4);
            }
            String[] excersizes = new String[values.length];
            for (int j = 0; j < values.length; j++){
                excersizes[j] = j + "";
            }
            Algo swap = new swapFix();
            excersizeMatrix = swap.calc(setCount, 4, rates, excersizes);
        } else {
            // Use break timing based algoirhtm
        }
        // Call Recursive timing algorithm
        // For loop for every set
        secondsRemaining = (int) (Integer.parseInt(settingPreferences[2]) * 60);
        timerFunction(excersizeMatrix);
        return;
    }

    public static void main(String[] args) throws Exception {
        new Application();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginLoginButton) {
            String[] userInformation = new String[2];
            userInformation[0] = loginUsername.getText();
            userInformation[1] = loginPassword.getText();
            appFileWriter("db/userSave.txt", userInformation);
            checkCredentials();
            if (loginRem == 1) {} 
            else {
                userInformation[0] = "";
                userInformation[1] = "";
                appFileWriter("db/userSave.txt", userInformation);
            }
        } else if (e.getSource() == settingsLogout) {
            // Clear save file
            String[] userInformation = new String[2];
            userInformation[0] = "";
            userInformation[1] = "";
            appFileWriter("db/userSave.txt", userInformation);
            settings.setVisible(false);
            login.setVisible(true);
        } else if (e.getSource() == directoryStart) {
            directory.setVisible(false);
            action.setVisible(true);
            SwingWorker extraThread= new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    startRun();
                    return null;
                }
            };
            extraThread.execute();
        } else if (e.getSource() == directorySettings) {
            directory.setVisible(false);
            settings.setVisible(true);
        } else if (e.getSource() == directorySurvey) {
            directory.setVisible(false);
        } else if (e.getSource() == settingsApply) {
            String[] settingPreferences = new String[5];
            if (settingsDarkMode.isSelected() == false) {
                settingPreferences[0] = "0";
            } else {
                settingPreferences[0] = "1";
            }
            if (settingsWarning.isSelected() == false) {
                settingPreferences[1] = "0";
            } else {
                settingPreferences[1] = "1";
            }
            if (settingsWorkTime.getSelectedIndex() == 0){
                settingPreferences[2] = "30";
            } else if (settingsWorkTime.getSelectedIndex() == 1){
                settingPreferences[2] = "45";
            }  else if (settingsWorkTime.getSelectedIndex() == 2){
                settingPreferences[2] = "60";
            }  else if (settingsWorkTime.getSelectedIndex() == 3){
                settingPreferences[2] = "75";
            } else {
                settingPreferences[2] = "90";
            }
            if (settingsRunTime.getSelectedIndex() == 0) {
                settingPreferences[3] = "4.5";
            } else if (settingsRunTime.getSelectedIndex() == 1) {
                settingPreferences[3] = "6";
            } else if (settingsRunTime.getSelectedIndex() == 2) {
                settingPreferences[3] = "9";
            } else {
                settingPreferences[3] = "c";
            }
            if (settingsBreakLength.getSelectedIndex() == 0) {
                settingPreferences[4] = "2.5";
            } else if (settingsBreakLength.getSelectedIndex() == 1) {
                settingPreferences[4] = "5";
            } else {
                settingPreferences[4] = "c";
            }
            appFileWriter("db/preferences.txt", settingPreferences);
        } else if (e.getSource() == settingsCancel) {
            String[] settingPreferences;
            try {
                settingPreferences = appFileReader("db/preferences.txt");
            } catch(ArrayIndexOutOfBoundsException err) { 
                settingPreferences = new String[5];
            }
            // Set all swing components into the proper position
            // Dark Mode
            if (settingPreferences[0].equals("0")){
                settingsDarkMode.setSelected(false);
            } else {
                settingsDarkMode.setSelected(true);
            }
            // 30 Second Warning Gui
            if (settingPreferences[1].equals("0")){
                settingsWarning.setSelected(false);
            } else {
                settingsWarning.setSelected(true);
            }
            // Length in between breaks
            if (settingPreferences[2].equals("30")) {
                settingsWorkTime.setSelectedIndex(0);
            } else if (settingPreferences[2].equals("45")) {
                settingsWorkTime.setSelectedIndex(1);
            } else if (settingPreferences[2].equals("60")) {
                settingsWorkTime.setSelectedIndex(2);
            } else if (settingPreferences[2].equals("75")) {
                settingsWorkTime.setSelectedIndex(3);
            } else {
                settingsWorkTime.setSelectedIndex(4);
            }
            // Run Time
            if (settingPreferences[3].equals("4.5")) {
                settingsRunTime.setSelectedIndex(0);
            } else if (settingPreferences[3].equals("6")) {
                settingsRunTime.setSelectedIndex(1);
            } else if (settingPreferences[3].equals("9")) {
                settingsRunTime.setSelectedIndex(2);
            } else {
                settingsRunTime.setSelectedIndex(3);
            }
            // Break Length
            if (settingPreferences[4].equals("2.5")) {
                settingsBreakLength.setSelectedIndex(0);
            } else if (settingPreferences[4].equals("5")) {
                settingsBreakLength.setSelectedIndex(1);
            } else {
                settingsBreakLength.setSelectedIndex(2);
            }
        } else if (e.getSource() == settingsRestore) {
            SwingWorker optionWindow= new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    int result = JOptionPane.showConfirmDialog(null, "Are you sure you would like to restore defaults?", "WARNING", JOptionPane.YES_NO_OPTION);
                    switch (result) {
                       case JOptionPane.YES_OPTION:
                       String[] empty = {"0","1","60","6","c"};
                       appFileWriter("db/preferences.txt", empty);
                       settingsDarkMode.setSelected(false);
                       settingsWarning.setSelected(true);
                       settingsWorkTime.setSelectedIndex(2);
                       settingsRunTime.setSelectedIndex(1);
                       settingsBreakLength.setSelectedIndex(2);
                       break;
                       case JOptionPane.NO_OPTION:
                       break;
                       case JOptionPane.CLOSED_OPTION:
                       break;
                    }
                    return null;
                }
            };
            optionWindow.execute();
        } else if (e.getSource() == settingsBack) {
            settings.setVisible(false);
            directory.setVisible(true);
        } else if (e.getSource() == actionPause) {
            // Unpausing
            if (runType == 2) {
                actionPause.setText("Pause");
                runType = 0;
                SwingWorker extraThread= new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        timerFunction(excersizeMatrix);
                        return null;
                    }
                };
                extraThread.execute();
            }
            // Pause
            else {
                runType = 2;
                actionPause.setText("Resume");
            }

        } else if (e.getSource() == actionStop) {
            secondsRemaining = 0;
            // Clear appdb.txt
            String[] clear = {""};
            appFileWriter("db/appdb.txt", clear);
            action.setVisible(false);
            directory.setVisible(true);
        }
    }

    public void itemStateChanged (ItemEvent e) {
        if (e.getSource() == loginRemember) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                loginRem = 1;
            } else {
                loginRem = 0;
            }
        }
    }


}