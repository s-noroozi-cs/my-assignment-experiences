package com.chess.tour.presentation;

import com.chess.tour.PieceTourSolution;
import com.chess.tour.model.Cell;
import com.chess.tour.model.PieceTourResponse;
import com.chess.tour.model.Position;
import com.chess.tour.validation.TourValidation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import static com.chess.tour.PieceTourSolution.BOARD_SIZE;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class Gui {
    private final JFrame frmMain;
    private final JTextField txtRowPosition, txtColPosition;
    private final JButton btnStart;
    private final JLabel[][] lblBoard;
    private final long waiting = 50;

    public Gui(PieceTourSolution pieceTourSolution) {

        frmMain = new JFrame("Piesr's Tour");
        frmMain.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frmMain.setLayout(new BorderLayout());

        txtRowPosition = new JTextField("0", 2);
        txtColPosition = new JTextField("0", 2);

        btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Optional<String> validation = TourValidation.validateInteger(txtRowPosition.getText());
                showValidationMessage(validation, "Row index error: ", txtRowPosition);

                if (!validation.isPresent()) {
                    validation = TourValidation.validateInteger(txtColPosition.getText());
                    showValidationMessage(validation, "Column index error: ", txtColPosition);
                }

                int rowIndex = Integer.parseInt(txtRowPosition.getText());
                int colIndex = Integer.parseInt(txtColPosition.getText());

                if (!validation.isPresent()) {
                    validation = TourValidation.validatePosition(rowIndex, colIndex, BOARD_SIZE);
                    showValidationMessage(validation, "Border size validation: ", txtRowPosition);
                }

                if (!validation.isPresent()) {
                    new Thread(() -> {
                        try {
                            initBoard();

                            PieceTourResponse response = pieceTourSolution.solve(new Position(rowIndex, colIndex));
                            Cell[] cells = response.sortByOrder();

                            for (int i = 0; i < cells.length; i++) {
                                Cell cell = cells[i];
                                if (cell != null) {
                                    JLabel lbl = lblBoard[cell.getPosition().getRow()][cell.getPosition().getCol()];
                                    lbl.setBackground(Color.GREEN);
                                    lbl.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                                    lbl.setText(cell.getVisitedOrder() + "");
                                    lbl.setForeground(Color.BLACK);
                                    presentationDelay();
                                }
                            }
                            if(!response.isSolved()){
                                JOptionPane.showMessageDialog(frmMain, "Could not find tour!", "ERROR", ERROR_MESSAGE);
                            }else{
                                JOptionPane.showMessageDialog(frmMain, "Found valid Tour.", "Info", INFORMATION_MESSAGE );
                            }
                        } catch (Throwable ex) {
                            ex.printStackTrace();
                        } finally {
                            btnStart.setEnabled(true);
                        }

                    }).start();
                    btnStart.setEnabled(false);
                }
            }
        });

        JPanel pnlUserParams = new JPanel(new FlowLayout());

        pnlUserParams.add(new JLabel("Start row index: "));
        pnlUserParams.add(txtRowPosition);

        pnlUserParams.add(new JLabel("Start column index: "));
        pnlUserParams.add(txtColPosition);

        pnlUserParams.add(btnStart);

        frmMain.add(pnlUserParams, BorderLayout.NORTH);

        lblBoard = new JLabel[BOARD_SIZE][BOARD_SIZE];
        initBoard();

        frmMain.pack();
        frmMain.setResizable(false);
        frmMain.setVisible(true);

    }

    private void initBoard() {
        JPanel pnlBoard = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        Color color = Color.WHITE;
        for (int i = 0; i < BOARD_SIZE; i++) {
            color = Color.WHITE.equals(color) ? Color.BLACK : Color.WHITE;
            for (int j = 0; j < BOARD_SIZE; j++) {
                lblBoard[i][j] = new JLabel(" ");
                lblBoard[i][j].setOpaque(true);
                lblBoard[i][j].setBackground(color);
                pnlBoard.add(lblBoard[i][j]);
                lblBoard[i][j].repaint();
                color = Color.WHITE.equals(color) ? Color.BLACK : Color.WHITE;
            }
        }
        frmMain.add(pnlBoard, BorderLayout.CENTER);
        frmMain.pack();

    }

    private void showValidationMessage(Optional<String> validation, String title, JComponent gotFocus) {
        validation.ifPresent(msg -> {
            JOptionPane.showMessageDialog(frmMain, title + msg, "Validation", ERROR_MESSAGE);
            gotFocus.requestFocus();
        });
    }

    private void presentationDelay() {
        try {
            Thread.sleep(waiting);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
