package main;

import javax.swing.*;
import java.awt.*;

public class ResultPopup {

    public static void showResult(String resultMessage, Board board) {
        // Create a dialog box
        JDialog dialog = new JDialog();
        dialog.setTitle("Game Result");

        // Set layout and size
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null); // Center on screen

        // Add styled label
        JLabel messageLabel = new JLabel(resultMessage, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setForeground(new Color(34, 139, 34)); // Green for win
        dialog.add(messageLabel, BorderLayout.CENTER);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Add "OK" button to close the dialog
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(okButton);

        // Add "Play Again" button to reset the game
        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(e -> {
            board.resetGame(); // Reset the board to its initial state
            dialog.dispose();  // Close the dialog
        });
        buttonPanel.add(playAgainButton);

        // Add button panel to the dialog
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Make dialog modal and visible
        dialog.setModal(true);
        dialog.setVisible(true);
    }
}
