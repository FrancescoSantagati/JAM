package it.francescosantagati.jam;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * Graphic interface to easily manage an it.francescosantagati.jam.ADSL instance.
 *
 * @author Francesco Santagati
 */
public class ADSLMonitor extends JPanel implements ActionListener, LogListener {

    // String
    private static final String STRING_STARTREG = "Start reg.";
    private static final String STRING_STARTUP = "Start up";
    private static final String STRING_SHUTDOWN = "Shutdown";
    private static final String STRING_EXIT = "Exit";
    private static final String STRING_PORT = "Port:";
    private static final String STRING_CONSOLE = "<html>Connection<br/>console:</html>";
    private static final int GAP = 10;

    // Components
    private JTextField portField;
    private JTextArea consoleArea;
    private JScrollPane scrollPane;
    private JLabel portLabel;
    private JLabel consoleLabel;
    private JSeparator separator;
    private JButton startRegButton;
    private JButton startUpButton;
    private JButton shutdownButton;
    private JButton exitButton;

    private ADSL adsl;

    /**
     * Construct a monitor for an it.francescosantagati.jam.ADSL instance.
     *
     * @param adsl Adsl to monitor
     */
    public ADSLMonitor(ADSL adsl) {
        super(new BorderLayout(GAP, GAP));

        this.adsl = adsl;
        initComponent();
        setupListeners();
        buildPanel();
    }

    /**
     * Show the monitor window on the screen.
     */
    public void showFrame() {
        JFrame frame = new JFrame();
        Container cp = frame.getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(this, BorderLayout.CENTER);
        frame.setTitle("Agent Directory Service Layer Monitor");
        frame.pack();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        frame.setVisible(true);
    }

    /**
     * Write log message on console area.
     *
     * @param logMessage it.francescosantagati.jam.Log message
     */
    @Override
    public void onLogMessage(String logMessage) {
        if (logMessage != null) {
            consoleArea.append(logMessage + "\n");
            consoleArea.repaint();
        }
    }

    /**
     * Invoked when a button is clicked.
     *
     * @param event Action event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        JButton source = (JButton) event.getSource();
        try {
            if (source.getText().equals(ADSLMonitor.STRING_STARTREG)) {
                ((ADSLImpl) adsl).PORT = Integer.parseInt(portField.getText());
                adsl.startRMIRegistry();

            } else if (source.getText().equals(ADSLMonitor.STRING_STARTUP)) {

                adsl.addListener(this);
                adsl.startADSL();
                portField.setEditable(false);

            } else if (source.getText().equals(ADSLMonitor.STRING_SHUTDOWN)) {

                adsl.removeListener(this);
                adsl.stopADSL();
                portField.setEditable(true);

            } else if (source.getText().equals(ADSLMonitor.STRING_EXIT)) {
                System.exit(0);
            }
        } catch (RemoteException e) {

            Log.e(this, e.getMessage());
        }
    }

    private void initComponent() {
        portLabel = new JLabel(STRING_PORT);
        consoleLabel = new JLabel(STRING_CONSOLE);
        portField = new JTextField();
        portField.setText((Integer.toString(adsl.PORT)));

        consoleArea = new JTextArea(8, 30);
        scrollPane = new JScrollPane(consoleArea);
        consoleArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret) consoleArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        separator = new JSeparator();
        startRegButton = new JButton(STRING_STARTREG);
        startUpButton = new JButton(STRING_STARTUP);
        shutdownButton = new JButton(STRING_SHUTDOWN);
        exitButton = new JButton(STRING_EXIT);
    }

    private void setupListeners() {
        startRegButton.addActionListener(this);
        startUpButton.addActionListener(this);
        shutdownButton.addActionListener(this);
        exitButton.addActionListener(this);
    }

    private void buildPanel() {
        JPanel panelNorth = new JPanel(new BorderLayout(GAP, GAP));
        JPanel panelCenter = new JPanel(new BorderLayout(GAP, GAP));

        JPanel startStopPanel = new JPanel();
        startStopPanel.setLayout(new BoxLayout(startStopPanel, BoxLayout.PAGE_AXIS));
        startStopPanel.add(startUpButton);
        startStopPanel.add(shutdownButton);

        panelNorth.add(portLabel, BorderLayout.WEST);
        panelNorth.add(portField, BorderLayout.CENTER);
        panelNorth.add(startRegButton, BorderLayout.EAST);
        panelNorth.add(separator, BorderLayout.SOUTH);

        panelCenter.add(consoleLabel, BorderLayout.WEST);
        panelCenter.add(scrollPane, BorderLayout.CENTER);
        panelCenter.add(startStopPanel, BorderLayout.EAST);

        add(panelNorth, BorderLayout.NORTH);
        add(panelCenter, BorderLayout.CENTER);
        add(exitButton, BorderLayout.SOUTH);
    }
}
