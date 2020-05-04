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
    JButton loginLoginButton;
    JTextField loginUsername, loginPassword;
    JCheckBox loginRemember;

    public Application() {
        super("Application");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(200,200, 800, 450);
        this.setTitle("Ergonomic");
        this.setResizable(false);

        // Call startup functrions
        createPanels();
        
        // Add panels to paane
        login = new JPanel();
        this.add(login);
        login.setVisible(false);

        survey = new JPanel();
        this.add(survey);
        survey.setVisible(false);

        settings = new JPanel();
        this.add(settings);
        settings.setVisible(false);

        action = new JPanel();
        this.add(action);
        action.setVisible(false);

        directory = new JPanel();
        this.add(directory);
        directory.setVisible(false);

        twoFA = new JPanel();
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

        private String[] appFileReader(String filename) {
            // Check file line size
            int count = 0;
            try {
                InputStream is = new BufferedInputStream(new FileInputStream(filename));
                try {

                    byte[] c = new byte[1024];
                    count = 0;
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
            } catch(FileNotFoundException err){
            }

            // Return Array
            return lines;
        }



    //
    // Lgoin Procedure Functions
    //

        // Create Panels
    
    private void createPanels() {
        //
        // Login

        //
        // Survey

        //
        // Directory

        //
        // Settings

        //
        // Action
    }

        // Check bootup succeeded and call from there

    private void checkCredentials() {
        String[] credentials = appFileReader("save/save.txt");
        try{
            if (credentials[0] == "abed.a.h@live.com" && credentials[1] == "password") {
                // Login Authorized
            } else {
                // Login Unauthorized
            }
        } catch (ArrayIndexOutOfBoundsException err) {
            //Save file unpopulated
            System.out.println("we got nothing");
        }
    }


    public static void main(String[] args) throws Exception {
        new Application();
    }

    public void actionPerformed(ActionEvent e) {
    }


}