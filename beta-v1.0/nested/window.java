package nested;

import javax.swing.*;
// import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class window extends JFrame {
    private static final long serialVersionUID = 1L;
    BufferedReader read;
    int excersizeCount = 10;
    int excersizePerSet = 4;
    String[] excersizes = new String[excersizeCount];
    Integer[] counts = new Integer[excersizeCount];

    JLabel displayLabel;
    JPanel panel;

    int timing, rep, set, position;
    String[][] excersizeMatrix;


    public window() {
        super("window");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0,0, 250, 200);
        this.setTitle("Ergonomic");
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        readData(new swapFix());
        displayLabel = new JLabel("please wait for further instruction");
        panel.add(displayLabel);
        this.add(panel);
    }

    public static void main(String[] args) throws Exception {
        new window();
    }

    private void readData(Algo x) {
        int breakTiming = 0;
        String runTime = new String();
        int runCount = 0;
        try {
            read = new BufferedReader(new FileReader("nested/save.txt"));
            try{
                breakTiming = (int) Integer.parseInt(read.readLine(), 10);
                runTime = read.readLine();
                try {
                    runCount = (int) (Integer.parseInt(runTime) * (60/breakTiming));
                } catch (NumberFormatException e) {
                    runCount = (int) (24 * (60/breakTiming));
                }
            } catch(IOException e) {} 
            catch(NumberFormatException e){}
        } catch(FileNotFoundException e){
        }
        // Calculate count for each excersize set
        int excersizeCountInSet = (int) (((runCount * excersizePerSet) / excersizeCount) * 1.25);
        // Read save.txt
                try{
                    read = new BufferedReader(new FileReader("nested/stretches.txt"));
                    try{
                        for(int i = 0; i<excersizeCount; i++) {
                            excersizes[i] =  read.readLine();
                            counts[i] = excersizeCountInSet;
                        }
                    
                        read.close();
                    } catch(IOException e) {}
                } catch(FileNotFoundException e){
                }

        // Get Algorithm Responses
        excersizeMatrix = new String[runCount][excersizePerSet];
        excersizeMatrix = x.calc(runCount, excersizePerSet, counts, excersizes);

        // Start Timers
        for (int j = 0; j<runCount; j++){
            // Starts timer
            position = j;
            //Sleep
            try {
                TimeUnit.SECONDS.sleep(breakTiming);
            } catch (InterruptedException e) {
            }   
            setTimers();
        }

        // Restart if c, close if done
        if (runTime == "c"){
            readData(new swapFix());
        } else {
            try {
                File window = new File("./application.java");
                Desktop.getDesktop().open(window);
            } catch (IOException err) {}
            System.exit(0);
        }
    }

    private void setTimers() {
        // Called before every set of excersizes starts
        System.out.println("set Timer");
        this.setVisible(true);
        rep = 0;
        set = 0;
        displayExcersizes();
    }

    private void displayExcersizes() {
        //
        // excersizeMatrix[][] -> excersizes
        // timing -> time left of excersize
        // position -> [h][] set of objects
        // set -> [][h] excersoze in set
        // rep -> repition (for each side)
        //
        //
        // Update GUI Interface
        try {
           TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }    

        if(timing > 1) {
            timing--;
            if (rep == 0) {
                // First Side
                //displayLabel.setText(timing + "   Left Side    " + excersizeMatrix[position][set]);
                System.out.println("First Side");
    
            } else if (rep == 1) {
                // Second Side
                //displayLabel.setText(timing + "   Right Side    " + excersizeMatrix[position][set]);
                System.out.println("Other Side");
    
            } else if (rep == 2) {
                // No Side Switch
                //displayLabel.setText(timing + "    " + excersizeMatrix[position][set]);
                System.out.println("Walk");
    
            }
            displayExcersizes();
        } else {
            updateExcersizePos();
        }
    }

    private void updateExcersizePos() {
        System.out.println("Updating Excersize");
        if (set == (excersizeCount-1)){
            // Done set
            position++;
            // Finish
            return;
        } else {
            if (excersizeMatrix[position][rep] == "go walk") {
                set++;
                rep = 2;
                timing = 30;
            } else {
                if (rep == 0) {
                    rep = 1;
                    timing = 15;
                } else if (rep == 1) {
                    rep = 0;
                    timing = 15;
                    set++;
                }
            }
            // Setup GUI
            //displayLabel.setText(timing + "    " + excersizeMatrix[position][set]);
            displayExcersizes();
        }
    }
}