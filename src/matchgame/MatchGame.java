package matchgame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * Project: MatchGame
 * @author t
 * Date: 02/21/2023
 * Description: Cat-themed matching game.
 */
public class MatchGame {
    private static final int CARDS = 20;
    private static final String MATCH = "match"; // State that a match was found
    private static final String OPENED = "opened"; // State that a card was flipped
    private final Random random = new Random();
    private final ImageIcon cardBack = new ImageIcon("resources/images/card_back.png");

    private final JFrame frame = new JFrame();
    private final JPanel board = new JPanel();
    private final JPanel sidebar = new JPanel();
    private final JPanel bottom = new JPanel();
    private final JPanel title = new JPanel();

    private final JButton play = new JButton("PLAY");
    private final JButton guessAgain = new JButton("GUESS AGAIN");
    private final JButton exit = new JButton("EXIT");

    private final JLabel[] labels = new JLabel[]{
        new JLabel("MATCHING GAME"),
        new JLabel("CLICK TWO CARDS TO SEE IF THEY MATCH"),
        new JLabel("SCORE: 0")
    }; // Labels are created like this to be called in an enhanced for loop — better for a consistent GUI

    // Redefine variables for clarity
    private final JLabel titleLabel = labels[0];
    private final JLabel descLabel = labels[1];
    private final JLabel scoreLabel = labels[2];

    private final AtomicInteger count = new AtomicInteger();
    private final AtomicReference<JButton> button1 = new AtomicReference<>();
    private final AtomicReference<JButton> button2 = new AtomicReference<>();

    private boolean active = false;
    private JButton[] buttons;
    private String name1;
    private String name2;
    private int score;

    public static void main(String[] args) {
        MatchGame matchGame = new MatchGame();
        matchGame.init();
        matchGame.initButtons();
    }

    public void init() {
        play.addActionListener(e -> playAction());
        play.setFocusPainted(false);
        play.setContentAreaFilled(false);
        play.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        play.setForeground(Color.BLACK);
        play.setFont(new Font("Segoe UI", Font.BOLD, 20));

        guessAgain.addActionListener(e -> guessAgainAction());
        guessAgain.setFocusPainted(false);
        guessAgain.setContentAreaFilled(false);
        guessAgain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        guessAgain.setForeground(Color.BLACK);
        guessAgain.setFont(new Font("Segoe UI", Font.BOLD, 20));

        exit.addActionListener(e -> System.exit(0));
        exit.setFocusPainted(false);
        exit.setContentAreaFilled(false);
        exit.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        exit.setForeground(Color.BLACK);
        exit.setFont(new Font("Segoe UI", Font.BOLD, 20));

        for (JLabel lbl : labels) {
            lbl.setForeground(Color.BLACK);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lbl.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setVerticalAlignment(SwingConstants.CENTER);
        }

        board.setLayout(new GridLayout(4, 4));
        board.setBackground(Color.PINK);
        board.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sidebar.setLayout(new GridLayout(4, 1));
        sidebar.setBackground(Color.PINK);
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebar.add(scoreLabel);
        sidebar.add(play);
        sidebar.add(guessAgain);
        sidebar.add(exit);

        bottom.setLayout(new GridLayout(1, 1));
        bottom.setBackground(Color.PINK);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottom.add(descLabel);

        title.setLayout(new GridLayout(1, 1));
        title.setBackground(Color.PINK);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        title.add(titleLabel);

        frame.add(board, BorderLayout.CENTER);
        frame.add(sidebar, BorderLayout.EAST);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.add(title, BorderLayout.NORTH);
        frame.setBackground(Color.PINK);
        frame.setTitle("Cat Matching Game");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
    }

    private void initButtons() {
        button1.set(null);
        button2.set(null);
        buttons = new JButton[CARDS];

        for (int i = 0; i < CARDS; i++) {
            buttons[i] = new JButton(cardBack);
            buttons[i].setPreferredSize(new Dimension(100, 100));
            buttons[i].setName(String.valueOf(i));
            buttons[i].setFocusPainted(false);
            buttons[i].setContentAreaFilled(false);
            buttons[i].setBorder(BorderFactory.createEmptyBorder());
            final int j = i;
            buttons[i].addActionListener(e -> buttonAction(e, j));
        }

        for (int i = 0; i < CARDS; i++) {
            int randomIndex = i + random.nextInt(CARDS - i);
            JButton temp = buttons[i];
            buttons[i] = buttons[randomIndex];
            buttons[randomIndex] = temp;
            board.add(buttons[i]);
        }
        frame.pack();
    }

    /**
     * Play action method.
     */
    private void playAction() {
        active = true;
        score = 0;
        board.removeAll();
        descLabel.setText("CLICK TWO CARDS TO SEE IF THEY MATCH");
        scoreLabel.setText("SCORE: " + score);
        count.set(0);
        initButtons();
    }

    /**
     * Guess Again action method.
     */
    private void guessAgainAction() {
        if (!active) return;
        if (button1.get() == null || button2.get() == null) return;

        for (JButton btn : buttons) {
            if (!btn.getName().equals(MATCH)) {
                btn.setIcon(cardBack);
                btn.setEnabled(true);
            }
        }

        if (!button1.get().getName().equals(MATCH) && !button2.get().getName().equals(MATCH)) {
            button1.get().setName(name1);
            button2.get().setName(name2);
        }
        button1.set(null);
        button2.set(null);
        count.set(0);
    }

    private void buttonAction(ActionEvent e, int j) {
        if (!active) return;
        JButton button = (JButton) e.getSource();
        if (j < CARDS / 2)
            button.setIcon(new ImageIcon("resources/images/" + j + ".png"));
        else 
            button.setIcon(new ImageIcon("resources/images/" + (j - CARDS / 2) + ".png"));

        button.setDisabledIcon(button.getIcon());
        button.setEnabled(false);
        checkWin(button);
    }

    /**
     * Checks if the player has won the game and blits the win screen.
     * @param button 
     */
    private void checkWin(JButton button) {
        count.getAndIncrement();
        if (count.get() == 1) {
            button1.set(button);
            name1 = button1.get().getName();
            button1.get().setName(OPENED);

        } else if (count.get() == 2) {
            button2.set(button);
            name2 = button2.get().getName();
            button2.get().setName(OPENED);

            // Disables all cards that are not OPENED or MATCHED until "Guess Again" is pressed
            for (JButton btn : buttons) {
                if (!btn.getName().equals(OPENED) && !btn.getName().equals(MATCH)) {
                    btn.setDisabledIcon(cardBack);
                    btn.setEnabled(false);
                }
            }

            // If selection1 icon is the same as selection2 icon
            if (button1.get().getIcon().toString().equals(button2.get().getIcon().toString())) {
                button1.get().setName(MATCH);
                button1.get().setEnabled(false);
                button2.get().setName(MATCH);
                button2.get().setEnabled(false);
                score++;
                scoreLabel.setText("SCORE: " + score);

                // When player has won the game
                if (score == CARDS / 2) {
                    descLabel.setText("CONGRATULATIONS!");

                    // Create the win screen
                    final JOptionPane winPane = new JOptionPane(new JLabel(new ImageIcon("resources/images/party.png")));
                    JDialog dialog = winPane.createDialog("YOU WIN!");
                    dialog.setModal(true);
                    dialog.setVisible(true);

                    // Resets the game
                    score = 0;
                    playAction();
                }
                guessAgainAction();
            }
            count.set(0);
        }
    }
}
