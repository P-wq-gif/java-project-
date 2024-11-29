import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimedQuizApp {

    // Question Bank
    static String[] questions = {
            "What is the capital of France?",
            "Who developed Java?",
            "Which data type is used to store true or false values?",
            "What does JVM stand for?",
            "Which of these is not a programming language?"
    };

    static String[][] options = {
            {"Paris", "Berlin", "Madrid", "Rome"},
            {"Microsoft", "Sun Microsystems", "Apple", "Google"},
            {"int", "char", "boolean", "float"},
            {"Java Virtual Machine", "Java Value Mechanism", "Java Version Manager", "None"},
            {"Python", "HTML", "Java", "C++"}
    };

    static int[] correctAnswers = {0, 1, 2, 0, 1}; // Indexes of correct answers
    static int currentQuestionIndex = 0;
    static int score = 0;
    static String userName = "Student"; // Set user name (can be modified or input from user)

    static Timer questionTimer;
    static int timeRemaining = 60; // Time per question (in seconds)

    public static void main(String[] args) {
        // Launch the quiz window
        SwingUtilities.invokeLater(TimedQuizApp::createQuizWindow);
    }

    // Create the quiz window
    public static void createQuizWindow() {
        // Frame setup for full-screen mode
        JFrame quizFrame = new JFrame("Timed Quiz App");
        quizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        quizFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // Maximize the window
        quizFrame.setUndecorated(true);  // Remove window borders
        quizFrame.setLayout(new BorderLayout());

        // Background color set to black
        quizFrame.getContentPane().setBackground(Color.BLACK);

        // Display the first question
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BorderLayout());
        questionPanel.setBackground(Color.BLACK);

        // Display name and timer
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel userNameLabel = new JLabel("Welcome, " + userName, SwingConstants.LEFT);
        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        userNameLabel.setForeground(Color.WHITE);

        JLabel timerLabel = new JLabel("Time Left: " + timeRemaining + "s", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        timerLabel.setForeground(Color.WHITE);

        topPanel.add(userNameLabel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.EAST);

        // Set up the question label
        JLabel questionLabel = new JLabel(questions[currentQuestionIndex], SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        questionLabel.setForeground(Color.WHITE);
        questionPanel.add(questionLabel, BorderLayout.CENTER);

        // Button panel for answer options
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10)); // 2x2 grid for answers
        buttonPanel.setBackground(Color.BLACK);

        // Create enhanced buttons for options
        JButton optionA = createStyledButton(options[currentQuestionIndex][0]);
        JButton optionB = createStyledButton(options[currentQuestionIndex][1]);
        JButton optionC = createStyledButton(options[currentQuestionIndex][2]);
        JButton optionD = createStyledButton(options[currentQuestionIndex][3]);

        buttonPanel.add(optionA);
        buttonPanel.add(optionB);
        buttonPanel.add(optionC);
        buttonPanel.add(optionD);

        // Progress bar to show question number
        JProgressBar progressBar = new JProgressBar(0, questions.length);
        progressBar.setValue(currentQuestionIndex);
        progressBar.setStringPainted(true);

        // Action listeners for button clicks
        ActionListener answerListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton clickedButton = (JButton) e.getSource();
                int selectedAnswer = getAnswerIndex(clickedButton.getText());

                if (selectedAnswer == correctAnswers[currentQuestionIndex]) {
                    score++;
                }

                currentQuestionIndex++;

                if (currentQuestionIndex < questions.length) {
                    // Update the question and options
                    questionLabel.setText(questions[currentQuestionIndex]);
                    optionA.setText(options[currentQuestionIndex][0]);
                    optionB.setText(options[currentQuestionIndex][1]);
                    optionC.setText(options[currentQuestionIndex][2]);
                    optionD.setText(options[currentQuestionIndex][3]);
                    progressBar.setValue(currentQuestionIndex);
                    timeRemaining = 60; // Reset timer for the next question
                    timerLabel.setText("Time Left: " + timeRemaining + "s");
                    questionTimer.restart();  // Restart the timer
                } else {
                    // Show result
                    showResult(quizFrame);
                }
            }
        };

        // Add actions to the buttons
        optionA.addActionListener(answerListener);
        optionB.addActionListener(answerListener);
        optionC.addActionListener(answerListener);
        optionD.addActionListener(answerListener);

        // Timer for countdown
        questionTimer = new Timer(1000, e -> {
            timeRemaining--;
            timerLabel.setText("Time Left: " + timeRemaining + "s");
            if (timeRemaining <= 0) {
                // Time's up, move to next question
                questionTimer.stop();
                currentQuestionIndex++; // Move to next question if time runs out
                if (currentQuestionIndex < questions.length) {
                    questionLabel.setText(questions[currentQuestionIndex]);
                    optionA.setText(options[currentQuestionIndex][0]);
                    optionB.setText(options[currentQuestionIndex][1]);
                    optionC.setText(options[currentQuestionIndex][2]);
                    optionD.setText(options[currentQuestionIndex][3]);
                    progressBar.setValue(currentQuestionIndex);
                    timeRemaining = 60; // Reset timer
                    timerLabel.setText("Time Left: " + timeRemaining + "s");
                    questionTimer.start(); // Start the timer again for the next question
                } else {
                    // Show result if quiz ends
                    showResult(quizFrame);
                }
            }
        });
        questionTimer.start();

        // Add components to the frame
        quizFrame.add(topPanel, BorderLayout.NORTH);
        quizFrame.add(questionPanel, BorderLayout.CENTER);
        quizFrame.add(buttonPanel, BorderLayout.SOUTH);
        quizFrame.add(progressBar, BorderLayout.NORTH);

        // Show the quiz frame
        quizFrame.setVisible(true);
    }

    // Create a styled button with enhanced design
    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBackground(new Color(52, 152, 219)); // Blue color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185)); // Darker Blue on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219)); // Original color
            }
        });

        // Smooth animation effect on click
        button.addActionListener(e -> {
            button.setBackground(new Color(41, 128, 185)); // Darker Blue when clicked
            Timer timer = new Timer(150, ev -> button.setBackground(new Color(52, 152, 219))); // Reset color after delay
            timer.setRepeats(false);
            timer.start();
        });

        return button;
    }

    // Helper function to match the button text to the correct answer index
    public static int getAnswerIndex(String text) {
        for (int i = 0; i < options[currentQuestionIndex].length; i++) {
            if (options[currentQuestionIndex][i].equals(text)) {
                return i;
            }
        }
        return -1; // If not found, return an invalid index
    }

    // Show result screen at the end of the quiz
    public static void showResult(JFrame parentFrame) {
        // Close the current quiz frame
        parentFrame.dispose();

        // Show result in a new window
        JFrame resultFrame = new JFrame("Quiz Completed");
        resultFrame.setSize(400, 200);
        resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultFrame.setLocationRelativeTo(null);

        JLabel resultLabel = new JLabel("Your Score: " + score + "/" + questions.length, SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        resultLabel.setForeground(Color.WHITE);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 20));
        closeButton.setBackground(new Color(52, 152, 219)); // Blue color
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> System.exit(0));

        resultFrame.setLayout(new BorderLayout());
        resultFrame.add(resultLabel, BorderLayout.CENTER);
        resultFrame.add(closeButton, BorderLayout.SOUTH);
        resultFrame.getContentPane().setBackground(Color.BLACK); // Black background for result frame
        resultFrame.setVisible(true);
    }
}
