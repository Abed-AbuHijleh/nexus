// Imports
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Application extends JFrame implements ActionListener {
    // Serial Version UID
    private static final long serialVersionUID = 1L;
    // Variables
        // Key Panels
    JPanel login, survey, settings, action, directory, twoFA;

    public Application() {
        super("Application");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(200,200, 800, 450);
        this.setTitle("Ergonomic");
        this.setResizable(false);

        // Call startup functrions
        createPanels();
        
        // Add panels to paane
        this.add(login);
        login.setVisible(false);
        this.add(survey);
        survey.setVisible(false);
        this.add(settings);
        settings.setVisible(false);
        this.add(action);
        action.setVisible(false);
        this.add(directory);
        directory.setVisible(false);
        this.add(twoFA);
        twoFA.setVisible(false);

        // Test credentials and open correct panel
        checkCredentials();

        // Set visible
        this.setVisible(true);
    }

    //
    // Utility Functions
    //

        // Encrypter


        
        // Decrypter



        // File reader





    //
    // Lgoin Procedure Functions
    //

        // Create Panels
    
    private void createPanels() {

    }

        // Check bootup succeeded and call from there

    private void checkCredentials() {

    }


    public static void main(String[] args) throws Exception {
        new Application();
    }

    public void actionPerformed(ActionEvent e) {
    }


}