
// Imports
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.DimensionUIResource;

import nested.Algo;
import nested.swapFix;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
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
    JPanel motherPanel, login, survey, settings, action, directory, loginWestPanel, loginEastPanel, loginUsernamePanel, loginPasswordPanel, loginErrorPanel,
    loggedInPanel, logoutPanel, loggedInEastPanel, directoryLogoPanel, actionHeader, actionContent, actionHeaderEast, actionContentPassive, actionContentActive;
    JButton loginLoginButton, directoryStart, directorySurvey, directorySettings, directoryLogout, settingsCancel, settingsApply, settingsRestore, actionStop, actionPause, actionStart;
    JTextField loginUsername, loginPassword;
    JCheckBox loginRemember, settingsWarning, settingsDarkMode;
    JComboBox<String> settingsWorkTime, settingsRunTime, settingsBreakLength;
    JLabel actionTimer, loginLogoLabel, loginSignupLabel, loginErrorDisplay, loginPasswordForget, directoryLogo, directoryLogoutImage, actionMessage;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    Color black = Color.BLACK;
    Color white = Color.WHITE;
    Color lightGray = new java.awt.Color(241, 239, 240);
    Color gray = new java.awt.Color(221, 219, 220);
    Color blue = new java.awt.Color(102, 255, 255);
    Color salmon = new java.awt.Color(255, 102, 102);
    Color orange = new java.awt.Color(255, 128, 0);

    // App run ints
    int secondsRemaining, runType;

    public Application() {
        super("Application");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(200,200, keyPanelSizeWidth, keyPanelSizeHeight);
        this.setTitle("Screen Buddy");
        try {
            Image img = ImageIO.read(new File("./assets/icon.png"));
            this.setIconImage(img);
        } catch (IOException err) {}

        this.setResizable(false);

        // Call startup functrions
        createPanels();
        checkCredentials();

        // Add Listeners
        loginLoginButton.addActionListener(this);
        loginRemember.addItemListener(this);
        directoryLogout.addActionListener(this);
        directorySettings.addActionListener(this);
        directoryStart.addActionListener(this);
        directorySurvey.addActionListener(this);
        settingsApply.addActionListener(this);
        settingsCancel.addActionListener(this);
        settingsRestore.addActionListener(this);
        actionPause.addActionListener(this);
        actionStop.addActionListener(this);
        actionStart.addActionListener(this);

        // Add Panels
        motherPanel.add(login);
        motherPanel.add(loggedInPanel);
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
            if (type == 0) {
                // Light mode
                motherPanel.setBackground(white);
                loggedInPanel.setBackground(white);
                loggedInEastPanel.setBackground(white);

                action.setBackground(white);
                actionTimer.setBackground(white);
                actionTimer.setForeground(black);
                actionHeader.setBackground(white);
                actionHeaderEast.setBackground(white);
                actionContent.setBackground(white);
                actionStart.setBackground(lightGray);
                actionPause.setBackground(lightGray);
                actionStop.setBackground(lightGray);
                actionContentPassive.setBackground(white);

                settings.setBackground(white);

                directory.setBackground(gray);
                directoryLogoPanel.setBackground(gray);
                logoutPanel.setBackground(gray);
                directoryStart.setBackground(white);
                directorySurvey.setBackground(gray);
                directorySettings.setBackground(gray);
                directoryLogout.setBackground(gray);

                login.setBackground(white);
                loginRemember.setBackground(white);
                loginWestPanel.setBackground(white);
                loginLogoLabel.setBackground(white);
                loginEastPanel.setBackground(white);
                loginUsernamePanel.setBackground(white);
                loginPasswordPanel.setBackground(white);
                loginErrorPanel.setBackground(white);
            } else if (type == 1){
                // Dark mode
                motherPanel.setBackground(black);

                settings.setBackground(black);

                directory.setBackground(black);

                login.setBackground(black);
                loginRemember.setBackground(black);
            }
        }



    //
    // Lgoin Procedure Functions
    //

        // Create Panels
    
    private void createPanels() {
        
        motherPanel = new JPanel();

        //
        // Login
        loginWestPanel = new JPanel(new GridBagLayout());
        loginEastPanel = new JPanel(new GridBagLayout());
        loginLogoLabel = new JLabel();
        try {
            Image img = ImageIO.read(new File("./assets/Logo.png"));
            img = img.getScaledInstance( 350, 140,  java.awt.Image.SCALE_SMOOTH ) ;  
            loginLogoLabel.setIcon(new ImageIcon(img));
        } catch (IOException err) {}
        loginSignupLabel = new JLabel("<html><h3>Don't have an account? <a style='color: #50E6E6;' href='google.com'>Sign Up Here</a></h3></html>");

        login = new JPanel(new GridLayout(0,2));
        login.setBounds(0, 0, keyPanelSizeWidth, keyPanelSizeHeight);
        login.setVisible(false);

        loginLoginButton = new JButton();
        loginLoginButton.setFocusPainted(false);
        try {
            Image img = ImageIO.read(new File("./assets/Login.png"));
            img = img.getScaledInstance( 81, 81,  java.awt.Image.SCALE_SMOOTH ) ;  
            loginLoginButton.setIcon(new ImageIcon(img));
            loginLoginButton.setOpaque(false);
            loginLoginButton.setContentAreaFilled(false);
            loginLoginButton.setBorderPainted(false);
        } catch (IOException err) {}
        loginRemember = new JCheckBox("<html><h4>Remember Me</h4><html>");
        loginRemember.setFocusPainted(false);
        loginRemember.setHorizontalTextPosition(SwingConstants.LEFT);
        try {
            Image img = ImageIO.read(new File("./assets/switchOff.png"));
            img = img.getScaledInstance( 40, 40,  java.awt.Image.SCALE_SMOOTH ) ;  
            loginRemember.setIcon(new ImageIcon(img));

            img = ImageIO.read(new File("./assets/switchOn.png"));
            img = img.getScaledInstance( 40, 40,  java.awt.Image.SCALE_SMOOTH ) ; 
            loginRemember.setSelectedIcon(new ImageIcon(img));
        } catch (IOException err) {}
        loginUsername = new JTextField("");
        loginUsername.setPreferredSize(new Dimension((int) keyPanelSizeWidth / 5, 15));
        loginPassword = new JTextField("");
        loginPassword.setPreferredSize(new Dimension((int) keyPanelSizeWidth / 5, 15));

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 2;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        loginWestPanel.add(loginLogoLabel, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 4;
        loginWestPanel.add(loginSignupLabel, c);

        loginWestPanel.setBorder(new EmptyBorder(keyPanelSizeHeight/20, 20, keyPanelSizeHeight/4, 20));

        loginPasswordForget = new JLabel("<html><h3><a  style='color: #50e6e6; text-decoration: none;' href='youtube.com'>Forgot Password?</a></h3></html>");
        loginErrorDisplay = new JLabel("<html><p><strong><span style='color: #ff6666;'>Invalid login credentials</span></strong></p></html>");
        loginErrorDisplay.setVisible(false);

        loginErrorPanel = new JPanel(new BorderLayout());

        loginUsernamePanel = new JPanel();
        loginUsernamePanel.setBorder(new TitledBorder(new LineBorder(black), "Email"));
        loginUsernamePanel.add(loginUsername);
        loginUsername.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        loginPasswordPanel = new JPanel();
        loginPasswordPanel.add(loginPassword);
        loginPasswordPanel.setBorder(new TitledBorder(new LineBorder(black), "Password"));
        loginPassword.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        loginEastPanel.add(loginUsernamePanel);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        loginEastPanel.add(loginRemember, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        loginEastPanel.add(loginPasswordPanel, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        loginEastPanel.add(loginPasswordForget, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 4;
        loginErrorPanel.add(loginLoginButton, BorderLayout.EAST);
        loginErrorPanel.add(loginErrorDisplay, BorderLayout.WEST);
        loginErrorPanel.setBorder(new EmptyBorder(40, 0, 0, 0));
        loginEastPanel.add(loginErrorPanel, c);
        loginEastPanel.setBorder(new EmptyBorder(130, 0, 0, 0));

        login.add(loginWestPanel);
        login.add(loginEastPanel);

        //
        // Directory

        loggedInPanel = new JPanel(new BorderLayout());
        loggedInPanel.setVisible(true);
        loggedInEastPanel = new JPanel();
        loggedInPanel.add(loggedInEastPanel, BorderLayout.EAST);
        loggedInEastPanel.setPreferredSize(new Dimension(keyPanelSizeWidth-110, keyPanelSizeHeight));

        directory = new JPanel(new GridBagLayout());
        directoryLogoPanel = new JPanel();
        directoryLogoPanel.setBorder(new EmptyBorder(5,10,10,0));

        directoryLogo = new JLabel();
        try {
            Image img = ImageIO.read(new File("./assets/Logo.png"));
            img = img.getScaledInstance( 85, 35,  java.awt.Image.SCALE_SMOOTH ) ;  
            directoryLogo.setIcon(new ImageIcon(img));
        } catch (IOException err) {}
        directoryLogoutImage = new JLabel();
        try {
            Image img = ImageIO.read(new File("./assets/logout.png"));
            img = img.getScaledInstance( 15, 15,  java.awt.Image.SCALE_SMOOTH ) ;  
            directoryLogoutImage.setIcon(new ImageIcon(img));
        } catch (IOException err) {}

        directoryStart = new JButton("<html><h3>Begin</h3></html>");
        directoryStart.setFocusPainted(false);
        directoryStart.setBorder(null);
        directoryStart.setPreferredSize(new Dimension(110, 40));

        directorySurvey = new JButton("<html><h3>Data</h3></html>");
        directorySurvey.setFocusPainted(false);
        directorySurvey.setPreferredSize(new Dimension(110, 40));
        directorySurvey.setBorder(null);

        directorySettings = new JButton("<html><h3>Settings</h3></html>");
        directorySettings.setFocusPainted(false);
        directorySettings.setPreferredSize(new Dimension(110, 40));
        directorySettings.setBorder(null);

        directoryLogout = new JButton("Logout");
        directoryLogout.setFocusPainted(false);
        directoryLogout.setBorder(null);


        c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        directoryLogoPanel.add(directoryLogo);
        directory.add(directoryLogoPanel, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        directory.add(directoryStart, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        directory.add(directorySurvey, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 3;
        directory.add(directorySettings, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 100;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 4;
        logoutPanel = new JPanel();
        logoutPanel.add(directoryLogout);
        logoutPanel.add(directoryLogoutImage);
        logoutPanel.setBorder(new EmptyBorder(180, 0, 20, 0));
        directory.add(logoutPanel, c);

        loggedInPanel.add(directory, BorderLayout.WEST);

        //
        // Survey
        survey = new JPanel(); 
        survey.setVisible(false);

        loggedInEastPanel.add(survey);

        //
        // Settings
        settings = new JPanel();
        settings.setVisible(false);

        settingsApply = new JButton("Apply");
        settingsCancel = new JButton("Cancel");
        settingsRestore = new JButton("Restore Defaults");
        settingsDarkMode = new JCheckBox("Darkmode");
        settingsWarning = new JCheckBox("30 Second Headsup");
        String[] WorkTime = {"30 Minutes", "45 Minutes", "1 Hour (Recommended)", "75 Minutes", "90 Minutes"};
        settingsWorkTime = new JComboBox<String>(WorkTime);
        String[] RunTime = {"4.5 Hours", "6 Hours", "9 Hours", "Constant"};
        settingsRunTime = new JComboBox<String>(RunTime);
        String[] BreakLength = {"2.5 Minutes", "5 Minutes", "CVB (Recommended)"};
        settingsBreakLength = new JComboBox<String>(BreakLength);

        settings.add(settingsApply);
        settings.add(settingsCancel);
        settings.add(settingsRestore);
        settings.add(settingsDarkMode);
        settings.add(settingsWarning);
        settings.add(settingsWorkTime);
        settings.add(settingsRunTime);
        settings.add(settingsBreakLength);

        loggedInEastPanel.add(settings);

        //
        // Begin Tab
        action = new JPanel(new GridBagLayout());
        action.setVisible(true);
        actionHeader = new JPanel(new BorderLayout());
        actionHeader.setBorder(new CompoundBorder(new EmptyBorder(10,0,5,5), new MatteBorder(0, 0, 5, 0, lightGray)));
        actionHeaderEast = new JPanel(new BorderLayout());
        actionHeaderEast.setBorder(new EmptyBorder(0, keyPanelSizeWidth-400, 0, 0));
        actionContent = new JPanel();
        actionContent.setPreferredSize(new Dimension(keyPanelSizeWidth-110, keyPanelSizeHeight-35));

        actionTimer = new JLabel("xx:xx");

        actionPause = new JButton("Pause");
        actionPause.setEnabled(false);
        actionPause.setPreferredSize(new Dimension(80, 25));
        actionPause.setFocusPainted(false);
        actionPause.setBorder(null);

        actionStop = new JButton("Stop");
        actionStop.setEnabled(false);
        actionStop.setPreferredSize(new Dimension(80, 25));
        actionStop.setFocusPainted(false);
        actionStop.setBorder(null);

        actionStart = new JButton("Start");
        actionStart.setPreferredSize(new Dimension(100, 25));
        actionStart.setFocusPainted(false);
        actionStart.setBorder(null);

        actionHeader.add(actionStart, BorderLayout.WEST);
        actionHeaderEast.add(actionPause, BorderLayout.WEST);
        actionHeaderEast.add(actionStop, BorderLayout.EAST);
        actionHeader.add(actionHeaderEast, BorderLayout.EAST);

        c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        action.add(actionHeader, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        action.add(actionContent, c);


        actionMessage = new JLabel();
        try {
            String[] test = appFileReader("./db/distrubution.txt");
            test[5] = test[5];
            actionMessage.setText("<html><h1 style='color:gray'>Press Start to Begin</h1></html>");
            actionStart.setEnabled(true);
        } catch (ArrayIndexOutOfBoundsException err) {
            // No data yet
            actionMessage.setText("<html><h2 style='color:gray'>Please fill in some info in the data tab before beginning</h2></html>");
            actionStart.setEnabled(false);
        }

        actionContentPassive = new JPanel(new BorderLayout());
        actionContentPassive.setBorder(new EmptyBorder(80, 0, 0, 0));
        actionContentPassive.add(actionMessage, BorderLayout.CENTER);

        loggedInEastPanel.add(action);


        actionContentActive = new JPanel(new GridBagLayout());

        actionContentActive.add(actionTimer);
        actionContentActive.setVisible(false);

        actionContent.add(actionContentActive);
        actionContent.add(actionContentPassive);




        // Settings Pt 2
        // Read file and setup the choices accordingly
        try {
            settingPreferences = appFileReader("db/preferences.txt");
            // Setup system darkmode or lightmode

            if (settingPreferences[0].equals("0")) {
                appColourer(0);
            } else if (settingPreferences[0].equals("1")) {
                appColourer(1);
            }
        } catch (ArrayIndexOutOfBoundsException err) {
           // Default Settings 
           appColourer(0);
           settingPreferences = new String[5];
           settingPreferences[0] = "0";
           settingPreferences[1] = "1";
           settingPreferences[2] = "60";
           settingPreferences[3] = "6";
           settingPreferences[4] = "c";
           appFileWriter("db/preferences.txt", settingPreferences);
        } catch (NullPointerException err) {
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
    }

        // Check bootup succeeded and call from there

    private void checkCredentials() {
        String[] credentials = appFileReader("db/userSave.txt");
        try{
            if (credentials[0].equals("abed") && credentials[1].equals("password")) {
                // Login Authorized
                login.setVisible(false);
                directory.setVisible(true);
                loginErrorDisplay.setVisible(false);
            } else {
                // Login Unauthorized
                login.setVisible(true);
                loginErrorDisplay.setVisible(true);
            }
        } catch (ArrayIndexOutOfBoundsException err) {
            //Save file unpopulate
            login.setVisible(true);
        } catch (NullPointerException err) {
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
                String[] blank = {""};
                appFileWriter("db/userSave.txt", blank);
            }
            settingPreferences = new String[5];
            settingPreferences[0] = "0";
            settingPreferences[1] = "1";
            settingPreferences[2] = "60";
            settingPreferences[3] = "6";
            settingPreferences[4] = "c";
            appFileWriter("db/preferences.txt", settingPreferences); 
        } else if (e.getSource() == directoryLogout) {
            // Clear save file
            String[] blank = {""};
            appFileWriter("db/userSave.txt", blank);
            appFileWriter("db/preferences.txt", blank);
            loginUsername.setText("");
            loginPassword.setText("");
            settings.setVisible(false);
            login.setVisible(true);
        } else if (e.getSource() == directoryStart) {
            survey.setVisible(false);
            directorySurvey.setBackground(gray);
            settings.setVisible(false);
            directorySettings.setBackground(gray);
            action.setVisible(true);
            directoryStart.setBackground(white);
        } else if (e.getSource() == directorySettings) {
            survey.setVisible(false);
            directorySurvey.setBackground(gray);
            action.setVisible(false);
            directoryStart.setBackground(gray);
            settings.setVisible(true);
            directorySettings.setBackground(white);
        } else if (e.getSource() == directorySurvey) {
            action.setVisible(false);
            directoryStart.setBackground(gray);
            settings.setVisible(false);
            directorySettings.setBackground(gray);
            survey.setVisible(true);
            directorySurvey.setBackground(white);
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
            runType = 1;
            // Clear appdb.txt
            String[] clear = {""};
            appFileWriter("db/appdb.txt", clear);
            //
            actionContentActive.setVisible(false);
            actionContentPassive.setVisible(true);
            actionStart.setEnabled(true);
            actionStop.setEnabled(false);
            actionPause.setEnabled(false);

            // Reset panel
        } else if (e.getSource() == actionStart) {
            actionContentActive.setVisible(true);
            actionContentPassive.setVisible(false);
            actionStart.setEnabled(false);
            actionStop.setEnabled(true);
            actionPause.setEnabled(true);
            runType = 0;
            SwingWorker extraThread= new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    startRun();
                    return null;
                }
            };
            extraThread.execute();
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