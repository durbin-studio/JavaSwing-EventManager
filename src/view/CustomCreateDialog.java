package view; /**
 * Created by arup3 on 5/11/2017.
 */

import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.optionalusertools.TimeChangeListener;
import com.github.lgooddatepicker.zinternaltools.TimeChangeEvent;
import listeners.NewEventCreateListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CustomCreateDialog extends JDialog implements ActionListener {
    private JPanel myPanel = null;
    private JButton yesButton = null;
    private JButton noButton = null;
    private HintTextField titleField = null;
    static TimePicker timePickerStart, timerPickerEnd;
    NewEventCreateListener listener;
    private boolean answer = false;
    String stDate = "",edDate = "";

    public boolean getAnswer() { return answer; }

    public CustomCreateDialog(JFrame frame, boolean modal, String myMessage, NewEventCreateListener _listener) {
        super(frame, modal);

        listener = _listener;
        this.setTitle(myMessage);
        this.setResizable(false);
        myPanel = new JPanel();
        myPanel.setBorder(BorderFactory.createEmptyBorder(55, 55, 55, 55));
        myPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1;

        titleField = new HintTextField("Enter Event Title");
        gbc.fill = GridBagConstraints.BOTH;
        myPanel.add(titleField, gbc);

        timePickerStart = new TimePicker();
        timePickerStart.setText("Start Time");
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        myPanel.add(timePickerStart, gbc);

        timerPickerEnd = new TimePicker();
        timerPickerEnd.setText("End Time");
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        myPanel.add(timerPickerEnd, gbc);

        yesButton = new JButton("Save");
        yesButton.addActionListener(this);
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        myPanel.add(yesButton, gbc);

        noButton = new JButton("Cancel");
        noButton.addActionListener(this);
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        myPanel.add(noButton, gbc);


        Dimension dims = new Dimension(400,250);
        myPanel.setPreferredSize(dims);


        timePickerStart.addTimeChangeListener(new TimeChangeListener() {
            @Override
            public void timeChanged(TimeChangeEvent timeChangeEvent) {
                System.out.println("Start time selected: "+timeChangeEvent.getNewTime());
                stDate =""+ timeChangeEvent.getNewTime();
            }
        });

        timerPickerEnd.addTimeChangeListener(new TimeChangeListener() {
            @Override
            public void timeChanged(TimeChangeEvent timeChangeEvent) {
                System.out.println("End time selected: "+timeChangeEvent.getNewTime());
                edDate = ""+timeChangeEvent.getNewTime();
            }
        });


        myPanel.setVisible(true);
        this.setContentPane(myPanel);

        pack();
        setLocationRelativeTo(frame);
        setLocationByPlatform(true);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (yesButton == e.getSource()) {
            System.err.println("User chose yes.");
            answer = true;
            setVisible(false);
            System.out.println("message: " + titleField.getText());
            listener.onEventCreated(titleField.getText(),stDate,edDate);
        } else if (noButton == e.getSource()) {
            System.err.println("User chose no.");
            answer = false;
            setVisible(false);
        }
    }
}