package nested;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.util.*;

public class testing extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    //changeable variables
    public int intervalTotal = 10;
    public int perSet = 4;
    //variables
    int setCount = 15;
    int current = 0;
    String excersizes[] = new String[intervalTotal];
    private Integer[] counts = new Integer[intervalTotal];

    //elementsvisu
    JTextField excersizeRate;
    JButton next;
    JButton launch;
    JLabel keyLabel;
    JTextArea res;
    JPanel setting;
    JPanel preferences;
    BufferedReader read;


    public testing() {
        super("testing");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(200,200, 750, 400);
        this.setTitle("Algorithm Testing");

        next = new JButton("Next");
        launch = new JButton("Launch");
        next.addActionListener(this);
        launch.addActionListener(this);
        excersizeRate = new JTextField(10);
        keyLabel = new JLabel("excersize");
        setting = new JPanel();
        preferences = new JPanel();
        res = new JTextArea("Set 1:\nSet 2:\nSet 3:\nSet 4:\nSet 5:\nSet 6:\nSet 7:\nSet 8:\nSet 9:\nSet 10:");

        preferences.add(excersizeRate);
        preferences.add(keyLabel);
        preferences.add(next);

        setting.setLayout(new BorderLayout());
        setting.add(preferences, BorderLayout.WEST);
        setting.add(launch, BorderLayout.EAST);
        setting.add(res, BorderLayout.SOUTH);

        this.add(setting);

        updateExcersizes(0);

        this.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new testing();
    }

    public void updateExcersizes(int selection){
        // Init Call Function
        if (selection==0){
            try{
                read = new BufferedReader(new FileReader("nested/stretches.txt"));
                try{
                    for(int i = 0; i<intervalTotal; i++) {
                        excersizes[i] =  read.readLine();
                    }
                
                read.close();
                    } catch(IOException e) {
                    }
            } catch(FileNotFoundException e){
            }
            keyLabel.setText(excersizes[0]);
        } 
        // Function after next is pushed
        else{
            try {

                if(current < (intervalTotal)){
                    counts[current] = Integer.parseInt(excersizeRate.getText(),10);
                    if (current+1 == intervalTotal){
                        keyLabel.setText("Press Launch");
                    } else {
                        keyLabel.setText(excersizes[current+1]);
                    }
                }
            } catch (NumberFormatException n) {
                System.out.println("error");
            }
            current++;
        }
    }

    public double timer(Algo x) {
        String[][] response;
        long startTime;
        startTime = Calendar.getInstance().getTimeInMillis();
        response = x.calc(setCount, perSet, counts, excersizes);
        startTime = Calendar.getInstance().getTimeInMillis() - startTime;
        reportRes(response);
        System.out.println("Time in ms");
        return startTime;
    }

    public void reportRes(String[][] displayResponse) {
        String outputString = new String("");
        // for number of sets
        for(int j = 0; j<setCount;j++){
            // Write down sets
            outputString += "Set "+ (j+1) +": | ";
            // for number of objects per set
            for (int k = 0;k<perSet; k++){
                outputString += displayResponse[j][k]+" | ";
            }
            // next line
            outputString += "\n";
        }
        res.setText(outputString);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == next) {
            updateExcersizes(1);
        } else if (e.getSource() == launch){
            System.out.println(timer(new swapFix()));
        }
    }
}