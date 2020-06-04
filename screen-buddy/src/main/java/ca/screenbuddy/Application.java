package ca.screenbuddy;

// Imports
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import ca.screenbuddy.nested.Algo;
import ca.screenbuddy.nested.swapFix;
import ca.screenbuddy.nested.Settings;
import ca.screenbuddy.nested.Window;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import java.util.concurrent.TimeUnit;

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
    loggedInPanel, logoutPanel, loggedInEastPanel, directoryLogoPanel, actionHeader, actionContent, actionHeaderEast, actionContentPassive, actionContentActive, 
    settingsHeader, settingsContent, settingsHeaderWest, settingsSection0, settingsSection1, settingsSection0Panel0, settingsSection0Panel1, settingsSection1Panel0,
    settingsSection1Panel1, settingsSection1Panel2, surveyHeader, surveyContent, surveySurvey, surveyHistory, surveyAnalytics;
    JButton loginLoginButton, directoryStart, directorySurvey, directorySettings, directoryLogout, settingsCancel, settingsApply, settingsRestore, actionStop, 
    actionPause, actionStart, surveyStart, surveyData;
    JTextField loginUsername;
    JPasswordField loginPassword;
    JCheckBox loginRemember, settingsWarning, settingsDarkMode, loginPasswordView;
    JComboBox<String> settingsWorkTime, settingsRunTime, settingsBreakLength;
    JLabel actionTimer, loginLogoLabel, loginSignupLabel, loginErrorDisplay, loginPasswordForget, directoryLogo, directoryLogoutImage, actionMessage, settingsSection0Label0,
    settingsSection0Label1, settingsSection1Label0, settingsSection1Label1, settingsSection1Label2, settingsSection0Label, settingsSection1Label, actionExcersizes, actionMessageMini;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    Color black = Color.BLACK;
    Color white = Color.WHITE;
    Color lightGray = new java.awt.Color(241, 239, 240);
    Color gray = new java.awt.Color(221, 219, 220);
    Color blue = new java.awt.Color(102, 255, 255);
    Color salmon = new java.awt.Color(255, 102, 102);
    Color orange = new java.awt.Color(255, 128, 0);

    String[] excersizeData = {"Wrist Flexion", "Wrist Extension", "Neck Flexion", "Neck Extension", "Neck Roll", "Shoulder Shrugs", "Hamstring Stretch", "Calf Raises", "Back Lateral Extension", "Go For a Walk"};



    // App run ints
    int secondsRemaining, runType;

    public Application() {
        super("Application");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(200,200, keyPanelSizeWidth, keyPanelSizeHeight);
        this.setTitle("Screen Buddy");
        try {
            final Image img = ImageIO.read(new File("./assets/icon.png"));
            this.setIconImage(img);
        } catch (final IOException err) {}

        this.setResizable(false);

        // Call startup functrions
        createPanels();
        checkCredentials();

        // Add Listeners
        loginLoginButton.addActionListener(this);
        loginRemember.addItemListener(this);
        loginPasswordView.addItemListener(this);
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

        private String[] appFileReader(final String filename) {
            // Check file line size
            int count = 0;
            try {
                final InputStream is = new BufferedInputStream(new FileInputStream(filename));
                try {

                    final byte[] c = new byte[1024];
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
                } catch (final NumberFormatException err) {} 
                catch (final IOException err) {}
            } catch (final FileNotFoundException err) {}
            final String[] lines = new String[count];

            // input lines into an array
            try {
                final BufferedReader read = new BufferedReader(new FileReader(filename));
                try{
                    for (int k = 0; k < count; k++) {
                        lines[k] = read.readLine();
                    }
                    read.close();
                } catch(final IOException err) {} 
                catch(final NumberFormatException err){}
            } catch(final FileNotFoundException err){}

            // Return Array
            return lines;
        }


        // File Writer

        private void appFileWriter(final String filename, final String[] lines) {
            try {
                final PrintWriter writer = new PrintWriter(filename);
                for (int l = 0; l < lines.length; l++) {
                    if (l == (lines.length - 1)) {
                        writer.print(lines[l]);
                    } else {
                        writer.println(lines[l]);
                    }
                }
                writer.close();
            } catch (final FileNotFoundException err) {}
        }

        // Setup app coloring (light or dark)

        private void appColourer(final int type) {
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
                actionContentActive.setBackground(white);
                actionHeader.setBorder(new CompoundBorder(new EmptyBorder(5,0,10,5), new MatteBorder(0, 0, 5, 0, lightGray)));

                survey.setBackground(white);
                surveyHeader.setBackground(white);
                surveyHeader.setBorder(new CompoundBorder(new EmptyBorder(5,0,10,5), new MatteBorder(0, 0, 5, 0, lightGray)));
                surveyStart.setBackground(lightGray);
                surveyData.setBackground(lightGray);
                surveyContent.setBackground(white);

                settings.setBackground(white);
                settingsHeader.setBackground(white);
                settingsHeaderWest.setBackground(white);
                settingsApply.setBackground(lightGray);
                settingsCancel.setBackground(lightGray);
                settingsRestore.setBackground(lightGray);
                settingsContent.setBackground(white);
                settingsHeader.setBorder(new CompoundBorder(new EmptyBorder(5,0,10,5), new MatteBorder(0, 0, 5, 0, lightGray)));
                settingsSection0.setBackground(white);
                settingsSection0Panel0.setBackground(white);
                settingsSection0Panel1.setBackground(white);
                settingsSection1.setBackground(white);
                settingsSection1Panel0.setBackground(white);
                settingsSection1Panel1.setBackground(white);
                settingsSection1Panel2.setBackground(white);
                settingsDarkMode.setBackground(white);
                settingsWarning.setBackground(white);

                directory.setBackground(gray);
                directoryLogoPanel.setBackground(gray);
                logoutPanel.setBackground(gray);
                directoryStart.setBackground(gray);
                directorySurvey.setBackground(gray);
                directorySettings.setBackground(gray);
                directoryLogout.setBackground(gray);

                login.setBackground(white);
                loginRemember.setBackground(white);
                loginWestPanel.setBackground(lightGray);
                loginLogoLabel.setBackground(white);
                loginEastPanel.setBackground(white);
                loginUsernamePanel.setBackground(white);
                loginPasswordPanel.setBackground(white);
                loginErrorPanel.setBackground(white);
                loginPasswordView.setBackground(white);
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
        motherPanel.setBorder(new EmptyBorder(-10,0,0,0));

        //
        // Login
        loginWestPanel = new JPanel(new GridBagLayout());
        loginEastPanel = new JPanel(new GridBagLayout());
        loginLogoLabel = new JLabel();
        try {
            Image img = ImageIO.read(new File("./assets/Logo.png"));
            img = img.getScaledInstance( 350, 140,  java.awt.Image.SCALE_SMOOTH ) ;  
            loginLogoLabel.setIcon(new ImageIcon(img));
        } catch (final IOException err) {}
        loginSignupLabel = new JLabel("<html><h3>Don't have an account? <a style='color: #50E6E6;' href='google.com'>Sign Up Here</a></h3></html>");

        login = new JPanel(new GridLayout(0,2));
        login.setBorder(new EmptyBorder(10,0,0,0));
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
        } catch (final IOException err) {}
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
        } catch (final IOException err) {}
        loginUsername = new JTextField("");
        loginUsername.setPreferredSize(new Dimension((int) keyPanelSizeWidth / 5, 15));
        loginPassword = new JPasswordField("");
        loginPassword.setPreferredSize(new Dimension((int) (keyPanelSizeWidth / 5) - 15, 15));

        loginPasswordView = new JCheckBox();
        loginPasswordView.setFocusPainted(false);
        try {
            Image img = ImageIO.read(new File("./assets/hiddenIcon.png"));
            img = img.getScaledInstance( 15, 15,  java.awt.Image.SCALE_SMOOTH ) ;  
            loginPasswordView.setIcon(new ImageIcon(img));

            img = ImageIO.read(new File("./assets/viewIcon.png"));
            img = img.getScaledInstance( 15, 15,  java.awt.Image.SCALE_SMOOTH ) ; 
            loginPasswordView.setSelectedIcon(new ImageIcon(img));
        } catch (final IOException err) {}

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

        loginPasswordPanel = new JPanel(new BorderLayout());
        loginPasswordPanel.add(loginPassword, BorderLayout.WEST);
        loginPasswordPanel.add(loginPasswordView, BorderLayout.EAST);
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
        loggedInPanel.setVisible(false);
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
        } catch (final IOException err) {}
        directoryLogoutImage = new JLabel();
        try {
            Image img = ImageIO.read(new File("./assets/logout.png"));
            img = img.getScaledInstance( 15, 15,  java.awt.Image.SCALE_SMOOTH ) ;  
            directoryLogoutImage.setIcon(new ImageIcon(img));
        } catch (final IOException err) {}

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
        survey = new JPanel(new GridBagLayout()); 
        survey.setVisible(false);

        surveyHeader = new JPanel(new BorderLayout());
        surveyHeader.setPreferredSize(new Dimension(keyPanelSizeWidth-110, 45));

        surveyStart = new JButton("Start");
        surveyStart.setPreferredSize(new Dimension(100, 25));
        surveyStart.setFocusPainted(false);
        surveyStart.setBorder(null);
        
        surveyData = new JButton("Analytics");
        surveyData.setPreferredSize(new Dimension(100, 25));
        surveyData.setFocusPainted(false);
        surveyData.setBorder(null);

        surveyHeader.add(surveyStart, BorderLayout.WEST);
        surveyHeader.add(surveyData, BorderLayout.EAST);

        //

        surveyContent = new JPanel();


        c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        survey.add(surveyHeader, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        survey.add(surveyContent, c);

        loggedInEastPanel.add(survey);

        //
        // Settings
        settings = new JPanel(new GridBagLayout());
        settings.setVisible(false);
        settingsHeader = new JPanel(new BorderLayout());
        settingsHeaderWest = new JPanel(new BorderLayout());
        settingsHeaderWest.setBorder(new EmptyBorder(0, 0, 0, keyPanelSizeWidth-400));
        settingsContent = new JPanel(new GridBagLayout());

        settingsApply = new JButton("Apply");
        settingsApply.setPreferredSize(new Dimension(80, 25));
        settingsApply.setFocusPainted(false);
        settingsApply.setBorder(null);

        settingsCancel = new JButton("Cancel");
        settingsCancel.setPreferredSize(new Dimension(80, 25));
        settingsCancel.setFocusPainted(false);
        settingsCancel.setBorder(null);

        settingsRestore = new JButton("Restore Defaults");
        settingsRestore.setPreferredSize(new Dimension(130, 25));
        settingsRestore.setFocusPainted(false);
        settingsRestore.setBorder(null);

        settingsHeaderWest.add(settingsApply, BorderLayout.EAST);
        settingsHeaderWest.add(settingsCancel, BorderLayout.WEST);

        settingsHeader.add(settingsHeaderWest, BorderLayout.WEST);
        settingsHeader.add(settingsRestore, BorderLayout.EAST);

        //

        settingsSection0 = new JPanel(new GridBagLayout());
        settingsSection0.setPreferredSize(new Dimension(keyPanelSizeWidth-110, 150));
        settingsSection0Label = new JLabel("<html><h2><span style='text-decoration: underline; color: #ff8000;''>UI Preferences</span></h2></html>");
        settingsSection0Panel0 = new JPanel(new BorderLayout());
        settingsSection0Panel0.setPreferredSize(new Dimension(keyPanelSizeWidth-180,20));
        settingsSection0Label0 = new JLabel("Dark Mode");
        settingsSection0Panel1 = new JPanel(new BorderLayout());
        settingsSection0Label1 = new JLabel("30 Minute Headsup");

        settingsSection1 = new JPanel(new GridBagLayout());
        settingsSection1.setPreferredSize(new Dimension(keyPanelSizeWidth-110, 150));
        settingsSection1Label = new JLabel("<html><h2><span style='text-decoration: underline; color: #ff8000;''>Break Preferences</span></h2></html>");
        settingsSection1Panel0 = new JPanel(new BorderLayout());
        settingsSection1Panel0.setPreferredSize(new Dimension(keyPanelSizeWidth-180, 25));
        settingsSection1Label0 = new JLabel("Time Between Breaks");
        settingsSection1Panel1 = new JPanel(new BorderLayout());
        settingsSection1Label1 = new JLabel("App Run Length");
        settingsSection1Panel2 = new JPanel(new BorderLayout());
        settingsSection1Label2 = new JLabel("Break Length");


        settingsDarkMode = new JCheckBox();
        try {
            Image img = ImageIO.read(new File("./assets/switchOff.png"));
            img = img.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH ) ;  
            settingsDarkMode.setIcon(new ImageIcon(img));

            img = ImageIO.read(new File("./assets/switchOn.png"));
            img = img.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH ) ; 
            settingsDarkMode.setSelectedIcon(new ImageIcon(img));
        } catch (final IOException err) {}
        settingsWarning = new JCheckBox();
        try {
            Image img = ImageIO.read(new File("./assets/switchOff.png"));
            img = img.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH ) ;  
            settingsWarning.setIcon(new ImageIcon(img));

            img = ImageIO.read(new File("./assets/switchOn.png"));
            img = img.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH ) ; 
            settingsWarning.setSelectedIcon(new ImageIcon(img));
        } catch (final IOException err) {}
        final String[] WorkTime = {"30 Minutes", "45 Minutes", "1 Hour (Recommended)", "75 Minutes", "90 Minutes"};
        settingsWorkTime = new JComboBox<String>(WorkTime);
        final String[] RunTime = {"4.5 Hours", "6 Hours", "9 Hours", "Constant"};
        settingsRunTime = new JComboBox<String>(RunTime);
        final String[] BreakLength = {"2.5 Minutes", "5 Minutes", "CVB (Recommended)"};
        settingsBreakLength = new JComboBox<String>(BreakLength);

        c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        settingsSection0.add(settingsSection0Label, c);

        settingsSection0Panel0.add(settingsSection0Label0, BorderLayout.WEST);
        settingsSection0Panel0.add(settingsDarkMode, BorderLayout.EAST);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        settingsSection0.add(settingsSection0Panel0, c);

        settingsSection0Panel1.add(settingsSection0Label1, BorderLayout.WEST);
        settingsSection0Panel1.add(settingsWarning, BorderLayout.EAST);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        settingsSection0.add(settingsSection0Panel1, c);
        
        c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        settingsSection1.add(settingsSection1Label, c);

        settingsSection1Panel0.add(settingsSection1Label0, BorderLayout.WEST);
        settingsSection1Panel0.add(settingsWorkTime, BorderLayout.EAST);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        settingsSection1.add(settingsSection1Panel0, c);

        settingsSection1Panel1.add(settingsSection1Label1, BorderLayout.WEST);
        settingsSection1Panel1.add(settingsRunTime, BorderLayout.EAST);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        settingsSection1.add(settingsSection1Panel1, c);

        settingsSection1Panel2.add(settingsSection1Label2, BorderLayout.WEST);
        settingsSection1Panel2.add(settingsBreakLength, BorderLayout.EAST);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 3;
        settingsSection1.add(settingsSection1Panel2, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        settingsContent.add(settingsSection0, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        settingsContent.add(settingsSection1, c);

        c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        settings.add(settingsHeader, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        settings.add(settingsContent, c);

        loggedInEastPanel.add(settings);

        //
        // Begin Tab
        action = new JPanel(new GridBagLayout());
        action.setVisible(true);
        actionHeader = new JPanel(new BorderLayout());
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
            final String[] test = appFileReader("./db/distrubution.txt");
            test[5] = test[5];
            actionMessage.setText("<html><h1 style='color:gray'>Press Start to Begin</h1></html>");
            actionStart.setEnabled(true);
        } catch (final ArrayIndexOutOfBoundsException err) {
            // No data yet
            actionMessage.setText("<html><h2 style='color:gray'>Please fill in some info in the data tab before beginning</h2></html>");
            actionStart.setEnabled(false);
        }

        actionMessageMini = new JLabel("This tab can now be minimized");
        actionExcersizes = new JLabel();

        actionContentPassive = new JPanel(new BorderLayout());
        actionContentPassive.setBorder(new EmptyBorder(80, 0, 0, 0));
        actionContentPassive.add(actionMessage, BorderLayout.CENTER);

        loggedInEastPanel.add(action);


        actionContentActive = new JPanel(new GridBagLayout());
        actionContentActive.setPreferredSize(new Dimension(keyPanelSizeWidth-110, keyPanelSizeHeight-35));
        actionContentActive.setBorder(new EmptyBorder(-40, 80, 0, 0));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        actionContentActive.add(actionTimer, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 0;
        actionContentActive.add(actionExcersizes, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 1;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        actionContentActive.add(actionMessageMini, c);

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
                directoryStart.setBackground(white);
            } else if (settingPreferences[0].equals("1")) {
                appColourer(1);
                directoryStart.setBackground(black);
            }
        } catch (final ArrayIndexOutOfBoundsException err) {
           // Default Settings 
           appColourer(0);
           directoryStart.setBackground(white);
           settingPreferences = new String[5];
           settingPreferences[0] = "0";
           settingPreferences[1] = "1";
           settingPreferences[2] = "60";
           settingPreferences[3] = "6";
           settingPreferences[4] = "c";
           appFileWriter("db/preferences.txt", settingPreferences);
        } catch (final NullPointerException err) {
            appColourer(0);
            directoryStart.setBackground(white);
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
        final String[] credentials = appFileReader("/db/userSave.txt");
        try{
            if (credentials[0].equals("abed") && credentials[1].equals("password")) {
                System.out.println("we in");
                // Login Authorized
                login.setVisible(false);
                loggedInPanel.setVisible(true);
                loginErrorDisplay.setVisible(false);
            } else {
                // Login Unauthorized
                login.setVisible(true);
                loginErrorDisplay.setVisible(true);
            }
        } catch (final ArrayIndexOutOfBoundsException err) {
            //Save file unpopulate
            login.setVisible(true);
            loggedInPanel.setVisible(false);
        } catch (final NullPointerException err) {
            login.setVisible(true);
            loggedInPanel.setVisible(false);
        }
    }


    // Recursive timer function
    private void timerFunction(final String[][] excersizeMatrix) {
        try {
            // Update JLabel
            final int minute = (int) Math.floor(secondsRemaining / 60);
            final int secondCalc = secondsRemaining - (minute * 60);
            String second;
            if (secondCalc < 10) {
                second = "0" + secondCalc;
            } else {
                second = "" + secondCalc;
            }
            actionTimer.setText("<html><p><span style='color: #66ffff; font-size:50px;'><strong>" + minute + ":" + second + "</strong></span></p></html>");
            TimeUnit.SECONDS.sleep(1);
            secondsRemaining--;
        } catch (final InterruptedException err) {}
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
                if (setsDone < setCount) {
                    setsDone++;
                    String nextExcersizes = "<html><h3>Next Excersizes: </h3>";
                    for (int ab = 0; ab < 20; ab++) {
                        try {
                            nextExcersizes += ("<p>" + excersizeData[Integer.parseInt(excersizeMatrix[setsDone][ab])] + "</p>");
                        } catch (ArrayIndexOutOfBoundsException err) {
                            ab = 20;
                        }
                    }
                    nextExcersizes += "</html>";
                    actionExcersizes.setText(nextExcersizes);
                    secondsRemaining = (int) (Integer.parseInt(settingPreferences[2]) * 60);
                    timerFunction(excersizeMatrix);
                    return;
                } else {
                    if (repeat == 1) {
                        setsDone = 0;
                        startRun();
                        return;
                    } else {
                        // Clear appdb.txt
                        final String[] clear = {""};
                        appFileWriter("db/appdb.txt", clear);
                        //
                        actionContentActive.setVisible(false);
                        actionContentPassive.setVisible(true);
                        actionStart.setEnabled(true);
                        actionStop.setEnabled(false);
                        actionPause.setEnabled(false);
                        return;
                    }
                }
            } else {
                timerFunction(excersizeMatrix);
                return;
            }
        }
    }

    private void startWindow(final String[][] excersizeMatrix) {
        // Populate appdb.txt
        final String[] currentSet = new String[4];
        for (int g = 0; g < 20; g++) {
            try {
                currentSet[g] = excersizeMatrix[setsDone][g];
            } catch (ArrayIndexOutOfBoundsException err) {
                g = 20;
            }
        }
        appFileWriter("db/appdb.txt", currentSet);
        // Call window application with the appropriate information
        new Window();
        return;
    }


    private void startRun() {

        // Insure that the distrubution has been populated
        final String[] values = appFileReader("db/distrubution.txt");
        settingPreferences = appFileReader("db/preferences.txt");

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
            final Integer[] rates = new Integer[values.length];
            for (int i = 0; i < values.length; i++) {
                rates[i] = (int) Math.floor(Double.parseDouble(values[i]) * setCount * 4);
            }
            final String[] excersizes = new String[values.length];
            for (int j = 0; j < values.length; j++){
                excersizes[j] = j + "";
            }
            final Algo swap = new swapFix();
            excersizeMatrix = swap.calc(setCount, 4, rates, excersizes);
        } else {
            // Use break timing based algoirhtm
        }
        // Call Recursive timing algorithm
        // For loop for every set
        secondsRemaining = (int) (Integer.parseInt(settingPreferences[2]) * 60);
        String nextExcersizes = "<html><h3>Next Excersizes: </h3>";
        for (int ab = 0; ab < 20; ab++) {
            try {
                nextExcersizes += ("<p>" + excersizeData[Integer.parseInt(excersizeMatrix[setsDone][ab])] + "</p>");
            } catch (ArrayIndexOutOfBoundsException err) {
                ab = 20;
            }
        }
        nextExcersizes += "</html>";
        actionExcersizes.setText(nextExcersizes);
        timerFunction(excersizeMatrix);
        return;
    }

    public static void main(final String[] args) throws Exception {
        new Application();
    }

    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == loginLoginButton) {
            final String[] userInformation = new String[2];
            userInformation[0] = loginUsername.getText();
            userInformation[1] = loginPassword.getText();
            appFileWriter("db/userSave.txt", userInformation);
            checkCredentials();
            if (loginRem == 1) {} 
            else {
                final String[] blank = {""};
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
            final String[] blank = {""};
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
            final String[] settingPreferences = new String[5];
            if (settingsDarkMode.isSelected() == false) {
                settingPreferences[0] = "0";
            } else {
                settingPreferences[0] = "1";
            }
            appColourer(Integer.parseInt(settingPreferences[0]));
            if (settingPreferences[0].equals("0")) {
                directorySettings.setBackground(white);
            } else {
                directorySettings.setBackground(black);
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
            } catch(final ArrayIndexOutOfBoundsException err) { 
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
            final SwingWorker optionWindow= new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    final int result = JOptionPane.showConfirmDialog(null, "Are you sure you would like to restore defaults?", "WARNING", JOptionPane.YES_NO_OPTION);
                    switch (result) {
                       case JOptionPane.YES_OPTION:
                       final String[] empty = {"0","1","60","6","c"};
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
                final SwingWorker extraThread= new SwingWorker<String, Void>() {
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
            final String[] clear = {""};
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
            final SwingWorker extraThread= new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    startRun();
                    return null;
                }
            };
            extraThread.execute();
        }
    }

    public void itemStateChanged (final ItemEvent e) {
        if (e.getSource() == loginRemember) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                loginRem = 1;
            } else {
                loginRem = 0;
            }
        } else if (e.getSource() == loginPasswordView) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                loginPassword.setEchoChar((char) 0);
            } else {
                loginPassword.setEchoChar('•');
            }
        }
    }


}