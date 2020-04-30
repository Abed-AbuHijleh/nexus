import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.*;

public class application extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    JPanel keyAspects, hoursSection, questions;
    JButton start;
    JLabel instructions, runningLength;
    JComboBox<String> breakTiming, hours;
    JRadioButton constant, timed;


    public application() {
        super("application");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(200,200, 250, 200);
        this.setTitle("Ergonomic");
        this.setResizable(false);
        String[] stringBreakTiming = {"15 Minutes", "30 Minutes", "45 Minutes"};
        breakTiming = new JComboBox<String>(stringBreakTiming);
        String[] stringHours = {"4.5 Hours", "6 Hours", "9 Hours"};
        hours = new JComboBox<String>(stringHours);
        instructions = new JLabel("<html><h3>Please fill out the following fields</h3><p>How long between breaks?</p></html>");
        runningLength = new JLabel("How long do you want this running?");

        start = new JButton("Start");
        ButtonGroup hoursSelectionGroup = new ButtonGroup();
        constant = new JRadioButton("Constant");
        timed = new JRadioButton("Timed");
        timed.setSelected(true);
        hoursSelectionGroup.add(constant);
        hoursSelectionGroup.add(timed);

        keyAspects = new JPanel();
        hoursSection = new JPanel();
        questions = new JPanel();
        keyAspects.setLayout(new BorderLayout());
        hoursSection.setLayout(new BorderLayout());
        questions.setLayout(new BorderLayout());

        hoursSection.setBackground(Color.white);
        hoursSection.add(runningLength, BorderLayout.NORTH);
        hoursSection.add(constant, BorderLayout.EAST);
        hoursSection.add(timed, BorderLayout.WEST);
        hoursSection.add(hours, BorderLayout.SOUTH);

        questions.setBackground(Color.white);
        questions.add(hoursSection, BorderLayout.CENTER);
        questions.add(breakTiming,BorderLayout.NORTH);

        keyAspects.setBackground(Color.white);
        keyAspects.add(instructions, BorderLayout.NORTH);
        keyAspects.add(questions, BorderLayout.CENTER);
        keyAspects.add(start, BorderLayout.SOUTH);

        timed.addActionListener(this);
        constant.addActionListener(this);
        start.addActionListener(this);

        this.add(keyAspects);
        this.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new application();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == constant){
            hours.setVisible(false);
        } else if (e.getSource() == timed) {
            hours.setVisible(true);
        } else if (e.getSource() == start) {
            // Save and close
            try {
                PrintWriter g = new PrintWriter("nested/save.txt");

                // Setup Break Timing
                if (breakTiming.getSelectedIndex() == 0) {
                    g.println("15");
                } else if (breakTiming.getSelectedIndex() == 1) {
                    g.println("30");
                } else if (breakTiming.getSelectedIndex() == 2) {
                    g.println("45");
                }

                // Setup Hours
                if (timed.isSelected()) {
                    if (hours.getSelectedIndex() == 0){
                        g.println("4.5");
                    } else if (hours.getSelectedIndex() == 1) {
                        g.println("6");
                    } else if (hours.getSelectedIndex() == 2){
                        g.println("9");
                    }
                } else if (constant.isSelected()){
                    g.println("c");
                }

                g.close();
            } catch (FileNotFoundException err){}
            // Open true application
            try {
                File window = new File("nested/window.java");
                Desktop.getDesktop().open(window);
            } catch (IOException err) {}
            // Close preferences application
            System.exit(0);
        }
    }

}