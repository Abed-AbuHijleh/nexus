package nested;

// Imports
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class Window extends JFrame {
    private static final long serialVersionUID = 1L;
    // Variables
    int sideType, secondsLeft;
    JPanel mainPanel, northPanel, midPanel, midSouthPanel, imagePanel;
    JLabel imageLabel, timerLabel, titleLabel, descLabel;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Color black = Color.BLACK;
    Color white = Color.WHITE;
    Color gray = new java.awt.Color(128, 128, 128);
    Color blue = new java.awt.Color(102, 255, 255);
    Color salmon = new java.awt.Color(255, 102, 102);
    Color orange = new java.awt.Color(255, 128, 0);

    // imageDir, name, time, sides (0 for no 1 for yea)
    int seconds = 0;
    String[][] excersizeData = {
        {"sample.png", "Wrist Flexion", "15", "1"}, 
        {"sample.png", "Wrist Extension", "15", "1"}, 
        {"sample.png", "Neck Flexion", "30", "0"}, 
        {"sample.png", "Neck Extension", "30", "0"}, 
        {"sample.png", "Neck Roll", "45", "0"},
        {"sample.png", "Shoulder Shrugs", "30", "0"},
        {"sample.png", "Hamstring Stretch", "20", "1"},
        {"sample.png", "Calf Raises", "30", "0"},
        {"sample.png", "Back Lateral Extension", "15", "0"},
        {"sample.png", "Go For a Walk", "90", "0"}
    };

    public Window() {
        // Setup window
        super("Window");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0,0, 250, 200);
        this.setTitle("Ergonomic");
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);

        // Read daata
        String[] colorSet = appFileReader("./db/preferences.txt");
        String[] excersizes = appFileReader("./db/appdb.txt");

        // Create frame
        createPanels();
        this.add(mainPanel);
        color(Integer.parseInt(colorSet[0], 10));

        // Display the excersizes and form window
        SwingWorker optionWindow= new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                displayExcersizes(excersizes, Integer.parseInt(colorSet[0], 10));
                return null;
            }
        };
        optionWindow.execute();

        // Load frame into view
        this.setVisible(true);
        for (float i = 0; i <= 30; i++) {
            this.setOpacity((float) (i/30));
            try {
                Thread.sleep(10);
            } catch (InterruptedException err) {}
        }
    }


    public static void main(String[] args) throws Exception {
        new Window();
    }

    private void createPanels() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        northPanel = new JPanel(new BorderLayout());
        midPanel = new JPanel(new GridLayout(2,1));
        midPanel.setBorder(new EmptyBorder(0,(int) Math.floor(screenSize.getWidth()/3),0,0));
        midSouthPanel = new JPanel(new BorderLayout());  
        imagePanel = new JPanel(new BorderLayout());     
        imagePanel.setSize(100,100);   

        timerLabel = new JLabel("<html><h2>xx:xx</h2></html>");
        titleLabel = new JLabel("<html><h1>Excersize</h1></html>");
        descLabel = new JLabel("<html><h2>This side</h2></html>");
        imageLabel = new JLabel();

        imagePanel.add(imageLabel, BorderLayout.WEST);
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(midPanel, BorderLayout.CENTER);

        midPanel.add(imagePanel);
        midPanel.add(midSouthPanel);
        midSouthPanel.add(descLabel, BorderLayout.EAST);
        northPanel.add(titleLabel, BorderLayout.WEST);
        northPanel.add(timerLabel, BorderLayout.EAST);
    }

    private void color(Integer type) {
        if (type == 0) {
            // Lightmode
            mainPanel.setBackground(white);
            northPanel.setBackground(white);
            titleLabel.setForeground(black);
            timerLabel.setForeground(black);
            midSouthPanel.setBackground(white);
            descLabel.setForeground(black);
            midPanel.setBackground(white);
            imagePanel.setBackground(white);
        } else {
            // Darkmode
            mainPanel.setBackground(black);
            northPanel.setBackground(black);
            titleLabel.setForeground(white);
            timerLabel.setForeground(white);
            midSouthPanel.setBackground(black);
            descLabel.setForeground(white);
            midPanel.setBackground(black);
            imagePanel.setBackground(white);
        }
    }

    private void displayExcersizes(String[] excersizes, Integer imageColor) {
        for (int i = 0; i < excersizes.length; i++) {
            // Update Labels
            if (imageColor == 0) {
                imageLabel.setIcon(new ImageIcon("./resources/" + excersizeData[Integer.parseInt(excersizes[i], 10)][0]));
            } else {
                imageLabel.setIcon(invertImage("./resources/" + excersizeData[Integer.parseInt(excersizes[i], 10)][0]));
            }
            titleLabel.setText("<html><h1>" + excersizeData[Integer.parseInt(excersizes[i], 10)][1] + "</h1></html>");

            if (excersizeData[Integer.parseInt(excersizes[i])][3].equals("0")) {
                // If there is no side switching

                descLabel.setText("<html><h2>Do it</h2></html>");

                // Update time
                secondsLeft = Integer.parseInt(excersizeData[Integer.parseInt(excersizes[i], 10)][2]);
                while (true) {
                    if (secondsLeft == 0) {
                        break;
                    } else {
                        timerFunction();
                    }
                }
            } else {
                // There is side switching

                for (int sides = 0; sides < 2; sides++) {

                    if (sides == 0) {
                        descLabel.setText("<html><h2>Right Side First</h2></html>");
                    } else {
                        descLabel.setText("<html><h2>Now The Left Side</h2></html>");
                    }

                    secondsLeft = Integer.parseInt(excersizeData[Integer.parseInt(excersizes[i], 10)][2]);
                    while (true) {
                        if (secondsLeft == 0) {
                            break;
                        } else {
                            timerFunction();
                        }
                    }
                }
            }
        }
        System.exit(0);
    }

    private void timerFunction() {
        int minute = (int) Math.floor(secondsLeft / 60);
        int secondCalc = secondsLeft - (minute * 60);
        String second;
        if (secondCalc < 10) {
            second = "0" + secondCalc;
        } else {
            second = "" + secondCalc;
        }
        timerLabel.setText("<html><h2>"+minute+":"+second+"</h2></html>");
        secondsLeft--;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException err) {}
    }

    public static ImageIcon invertImage(String imageName) {
        // Get Image
        BufferedImage inputFile = null;
        try {
            inputFile = ImageIO.read(new File(imageName));
        } catch (IOException err) {}

        // Invert Image Colors
        for (int x = 0; x < inputFile.getWidth(); x++) {
            for (int y = 0; y < inputFile.getHeight(); y++) {
                int rgba = inputFile.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(), 255 - col.getGreen(), 255 - col.getBlue());
                inputFile.setRGB(x, y, col.getRGB());
            }
        }

        // Return final product
        ImageIcon image = new ImageIcon(inputFile);
        return image;
    }

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
}