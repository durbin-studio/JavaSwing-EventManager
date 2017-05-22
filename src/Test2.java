/**
 * Created by arup3 on 5/11/2017.
 */

import javax.swing.*;
import java.awt.*;


public class Test2 {
    private void displayGUI()
    {
        JFrame frame = new JFrame("GridBagLayout Example");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        CustomPanel topPanel = new CustomPanel(Color.BLUE.darker().darker());
        CustomPanel middlePanel = new CustomPanel(Color.CYAN.darker().darker());
        CustomPanel bottomPanel = new CustomPanel(Color.DARK_GRAY);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;

        contentPane.add(topPanel, gbc);

        gbc.gridy = 1;
        contentPane.add(middlePanel, gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(bottomPanel, gbc);

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String... args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new Test2().displayGUI();
            }
        });
    }
}

class CustomPanel extends JPanel
{
    public CustomPanel(Color backGroundColour)
    {
        setOpaque(true);
        setBackground(backGroundColour);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return (new Dimension(200, 150));
    }
}