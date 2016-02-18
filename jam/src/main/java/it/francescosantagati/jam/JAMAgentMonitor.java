package it.francescosantagati.jam;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Graphic interface to easily manage a it.francescosantagati.jam.JAMAgent instance.
 *
 * @author Francesco Santagati
 */
public class JAMAgentMonitor extends JPanel implements ActionListener, Observer {

    // GUI strings
    private static final String STRING_INIT = "Init";
    private static final String STRING_START = "Start";
    private static final String STRING_DESTROY = "Destroy";
    private static final String STRING_EXIT = "Exit";
    private static final String STRING_CONNECTION_CONSOLE = "<html>Connection<br/>console:</html>";
    private static final int GAP = 10;

    // GUI component
    private JButton initButton;
    private JButton startButton;
    private JButton destroyButton;
    private JButton exitButton;
    private JLabel consoleLabel;
    private JTextArea consoleArea;
    private JScrollPane scrollConsolePane;

    private JAMAgent agent;

    /**
     * Construct a JAM GUI
     *
     * @param agent JAM Agent
     */
    public JAMAgentMonitor(JAMAgent agent) {
        super(new BorderLayout(GAP, GAP));
        this.agent = agent;
        agent.addObserver(this);
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
        frame.setTitle("JAM Monitor: " + agent.getMyID());
        frame.pack();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        frame.setVisible(true);
    }

    /**
     * Initialize it.francescosantagati.jam.JAMAgent through GUI
     *
     * @throws JAMADSLException when fail to init
     */
    public void initJAMAgent() throws JAMADSLException {
        agent.init();
    }

    /**
     * Start it.francescosantagati.jam.JAMAgent through GUI
     */
    public void startJAMAgent() {
        agent.start();
    }

    /**
     * Destroy it.francescosantagati.jam.JAMAgent through GUI
     *
     * @throws JAMADSLException when fail to destroy
     */
    public void destroyJAMAgent() throws JAMADSLException {
        agent.destroy();
    }

    /**
     * Display a message on console area.
     *
     * @param o          it.francescosantagati.jam.JAMAgent instance
     * @param logMessage it.francescosantagati.jam.Message to display
     */
    @Override
    public void update(Observable o, Object logMessage) {
        consoleArea.append(logMessage.toString() + "\n");
        consoleArea.repaint();
    }

    /**
     * Invoked when a button is clicked.
     *
     * @param event Action event
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        JButton source = (JButton) event.getSource();
        if (source.getText().equals(JAMAgentMonitor.STRING_INIT)) {
            try {
                initJAMAgent();
            } catch (JAMADSLException e) {
                Log.e(this, e.getMessage());
            }

        } else if (source.getText().equals(JAMAgentMonitor.STRING_START)) {
            startJAMAgent();

        } else if (source.getText().equals(JAMAgentMonitor.STRING_DESTROY)) {
            try {
                destroyJAMAgent();
            } catch (JAMADSLException e) {
                Log.e(this, e.getMessage());
            }

        } else if (source.getText().equals(JAMAgentMonitor.STRING_EXIT)) {
            System.exit(0);
        }
    }

    private void initComponent() {
        consoleLabel = new JLabel(STRING_CONNECTION_CONSOLE);
        consoleArea = new JTextArea(5, 40);
        scrollConsolePane = new JScrollPane(consoleArea);
        consoleArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret) consoleArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        initButton = new JButton(STRING_INIT);
        startButton = new JButton(STRING_START);
        destroyButton = new JButton(STRING_DESTROY);
        exitButton = new JButton(STRING_EXIT);
    }

    private void setupListeners() {
        initButton.addActionListener(this);
        startButton.addActionListener(this);
        destroyButton.addActionListener(this);
        exitButton.addActionListener(this);
    }

    private void buildPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout(GAP, GAP));
        buttonPanel.add(initButton, BorderLayout.NORTH);
        buttonPanel.add(startButton, BorderLayout.CENTER);
        buttonPanel.add(destroyButton, BorderLayout.SOUTH);

        add(buttonPanel, BorderLayout.EAST);
        add(scrollConsolePane, BorderLayout.CENTER);
        add(consoleLabel, BorderLayout.WEST);
        add(exitButton, BorderLayout.SOUTH);
    }
}
