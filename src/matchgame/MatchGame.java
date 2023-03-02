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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;


/**
 * Project: MatchGame
 * @author tsiga
 * Date: 02/21/2023
 * Description: Cat Matching Game
 */
public class MatchGame {
    private static final int CARDS = 20;
    private static final String MATCH = "match";
    private static final String OPENED = "opened";
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
    };

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
        play.addActionListener(e -> play());
        play.setFocusPainted(false);
        play.setContentAreaFilled(false);
        play.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        play.setForeground(Color.BLACK);
        play.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

        guessAgain.addActionListener(e -> guessAgain());
        guessAgain.setFocusPainted(false);
        guessAgain.setContentAreaFilled(false);
        guessAgain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        guessAgain.setForeground(Color.BLACK);
        guessAgain.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

        exit.addActionListener(e -> System.exit(0));
        exit.setFocusPainted(false);
        exit.setContentAreaFilled(false);
        exit.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        exit.setForeground(Color.BLACK);
        exit.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

        for (JLabel label : labels) {
            label.setForeground(Color.BLACK);
            label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            label.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
        }

        board.setLayout(new GridLayout(4, 4));
        board.setBackground(Color.PINK);
        board.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sidebar.setLayout(new GridLayout(4, 1));
        sidebar.setBackground(Color.PINK);
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebar.add(labels[2]);
        sidebar.add(play);
        sidebar.add(guessAgain);
        sidebar.add(exit);

        bottom.setLayout(new GridLayout(1, 1));
        bottom.setBackground(Color.PINK);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottom.add(labels[1]);

        title.setLayout(new GridLayout(1, 1));
        title.setBackground(Color.PINK);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        title.add(labels[0]);

        frame.add(board, BorderLayout.CENTER);
        frame.add(sidebar, BorderLayout.EAST);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.add(title, BorderLayout.NORTH);
        frame.setBackground(Color.PINK);
        frame.setTitle("Match Game");
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

    private void play() {
        active = true;
        score = 0;
        board.removeAll();
        labels[2].setText("SCORE: " + score);
        count.set(0);
        initButtons();
    }

    private void guessAgain() {
        if (!active) return;
        if (button1.get() == null || button2.get() == null) return;
        for (JButton button : buttons) {
            if (!button.getName().equals(MATCH)) {
                button.setIcon(cardBack);
                button.setEnabled(true);
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
        if (j < CARDS / 2) button.setIcon(new ImageIcon("resources/images/" + j + ".png"));
        else button.setIcon(new ImageIcon("resources/images/" + (j - CARDS / 2) + ".png"));
        button.setDisabledIcon(button.getIcon());
        button.setEnabled(false);
        checkWin(button);
    }

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
            for (JButton b : buttons) {
                if (!b.getName().equals(OPENED) && !b.getName().equals(MATCH)) {
                    b.setDisabledIcon(cardBack);
                    b.setEnabled(false);
                }
            }
            if (button1.get().getIcon().toString().equals(button2.get().getIcon().toString())) {
                button1.get().setName(MATCH);
                button1.get().setEnabled(false);
                button2.get().setName(MATCH);
                button2.get().setEnabled(false);
                score++;
                labels[2].setText("SCORE: " + score);
                if (score == CARDS / 2) {
                    JOptionPane.showMessageDialog(frame, null, "YOU WIN", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/images/party.png"));
                    score = 0;
                    play();
                }
                guessAgain();
            }
            count.set(0);
        }
    }
}