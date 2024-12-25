package main;

import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class Board extends JPanel{

    public String fenStringPostion="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";


    public int titlesize=85;

    int cols=8;
    int rows=8;

    private Piece checkedKing = null;


    private int checkedKingCol = -1; // Column of the checked king
    private int checkedKingRow = -1; // Row of the checked king

    ArrayList<Piece> pieceList = new ArrayList<>();

    public Piece selectedPiece;

    Input input=new Input(this);

    public CheckScanner checkSacnner=new CheckScanner(this);

    public int enPassantTile = -1;

    private boolean isWhitetoMove =true;
    private boolean isGameOver=false;

    public Board(){
        this.setPreferredSize(new Dimension(cols*titlesize,rows*titlesize));
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        loadPostionFromFEN(fenStringPostion);

    }

    public Piece getPiece(int col,int row){
        for(Piece piece:pieceList){
            if(piece.col==col && piece.row==row){
                return piece;
            }
        }
        return null;
    }

    public void makeMove(Move move){

        if(move.piece.name.equals("Pawn")){
            movePawn(move);
        }else {
            enPassantTile= -1;
        }

        if(move.piece.name.equals("King")){
            moveKing((move));
        }
            move.piece.col=move.newCol;
            move.piece.row=move.newRow;
            move.piece.xPos=move.newCol * titlesize;
            move.piece.yPos=move.newRow * titlesize;

            move.piece.isFirstMove=false;

            capture(move.capture);

            isWhitetoMove =!isWhitetoMove;

            updateGameState();

    }

    private void moveKing(Move move) {

        if (Math.abs(move.piece.col - move.newCol) == 2) {
            Piece rook;
            if(move.piece.col<move.newCol){
                rook = getPiece(7,move.piece.row);
                rook.col=5;
        }else {
                rook = getPiece(0,move.piece.row);
                rook.col=3;
            }
            rook.xPos=rook.col * titlesize;
    }
}
    private void movePawn(Move move){

        // enpassant
        int colorIndex=move.piece.isWhite ? 1:-1;

        if(getTitleNum(move.newCol,move.newRow)==enPassantTile){
            move.capture=getPiece(move.newCol,move.newRow + colorIndex);
        }

        if(Math.abs(move.piece.row-move.newRow)==2){
            enPassantTile=getTitleNum(move.newCol,move.newRow + colorIndex);
        }else {
            enPassantTile= -1;
        }

        //promotions
        colorIndex=move.piece.isWhite ? 0:7;
        if(move.newRow == colorIndex){
            promotePawn(move);
        }

    }

    private void promotePawn(Move move) {

            String[] options = {"Queen", "Rook", "Bishop", "Knight"};

            // Show a dialog to the user to select the promotion piece
            String choice = (String) JOptionPane.showInputDialog(
                    null,
                    "Choose promotion piece:",
                    "Pawn Promotion",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0] // Default selection is Queen
            );

            if (choice != null) {
                switch (choice) {
                    case "Queen":
                        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
                        break;
                    case "Rook":
                        pieceList.add(new Rook(this, move.newCol, move.newRow, move.piece.isWhite));
                        break;
                    case "Bishop":
                        pieceList.add(new Bishop(this, move.newCol, move.newRow, move.piece.isWhite));
                        break;
                    case "Knight":
                        pieceList.add(new Knight(this, move.newCol, move.newRow, move.piece.isWhite));
                        break;
                }
            } else {
                // Default to Queen if no choice is made
                pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
            }

            capture(move.piece); // Remove the pawn

    }

    public void capture(Piece piece){
        pieceList.remove(piece);
    }
    public boolean isValidMove(Move move) {

        if (isGameOver) {
            return false;
        }

        // Ensure the player is moving their own piece
        if (move.piece.isWhite != isWhitetoMove) {
            return false;
        }

        // Prevent capturing a piece of the same color
        if (sameTeam(move.piece, move.capture)) {
            return false;
        }

        // Check if the piece's movement is valid for its type
        if (!move.piece.isValidMovement(move.newCol, move.newRow)) {
            return false;
        }

        // Ensure there are no collisions along the path of movement
        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)) {
            return false;
        }

        // Check if the king would be in check after this move
        if (checkSacnner.isKingChecked(move)) {
            return false;
        }

        return true;
    }


    public boolean sameTeam(Piece p1,Piece p2){
        if(p1==null || p2==null){
            return  false;
        }
        return p1.isWhite==p2.isWhite;
    }

    public int getTitleNum(int col,int row){
        return row * rows + col;
    }

    Piece findKing(boolean isWhite) {
        for (Piece piece : pieceList) {
            if (piece instanceof King && piece.isWhite == isWhite) {
                return piece;
            }
        }
        return null; // This should never happen if the board is correctly initialized
    }



    //"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    public void loadPostionFromFEN(String fenString){
        pieceList.clear();
        String[] parts=fenString.split(" ");

        //setup pieces
        String postion=parts[0];
        int row=0;
        int col=0;
        for(int i=0;i<postion.length();i++){
            char ch=postion.charAt(i);
            if(ch=='/'){
                row++;
                col=0;
            } else if (Character.isDigit(ch)) {
                col +=Character.getNumericValue(ch);
            }else {
                boolean isWhite=Character.isUpperCase(ch);
                char pieceChar=Character.toLowerCase(ch);

                switch (pieceChar){
                    case 'r':
                        pieceList.add(new Rook(this,col,row,isWhite));
                        break;
                    case 'n':
                        pieceList.add(new Knight(this,col,row,isWhite));
                        break;
                    case 'b':
                        pieceList.add(new Bishop(this,col,row,isWhite));
                        break;
                    case 'q':
                        pieceList.add(new Queen(this,col,row,isWhite));
                        break;
                    case 'k':
                        pieceList.add(new King(this,col,row,isWhite));
                        break;
                    case 'p':
                        pieceList.add(new Pawn(this,col,row,isWhite));
                        break;
                }
                col++;
            }
        }
        //color to move
        isWhitetoMove=parts[1].equals("w");

        //castling
        Piece bqr=getPiece(0,0);
        if(bqr instanceof Rook){
            bqr.isFirstMove=parts[2].contains("q");
        }

        Piece bkr=getPiece(7,0);
        if(bqr instanceof Rook){
            bqr.isFirstMove=parts[2].contains("K");
        }

        Piece wqr=getPiece(0,7);
        if(bqr instanceof Rook){
            bqr.isFirstMove=parts[2].contains("Q");
        }

        Piece wkr=getPiece(7,7);
        if(bqr instanceof Rook){
            bqr.isFirstMove=parts[2].contains("K");
        }

        //en passant square
        if(parts[3].equals("-")){
            enPassantTile=-1;
        }else {
            enPassantTile=(7-(parts[3].charAt(1)-'1')) * 8 + (parts[3].charAt(0)-'a');
        }
    }


    private void updateGameState() {
        Piece king = findKing(isWhitetoMove);

        // Check if the game is over
        if (checkSacnner.isGameOver(king)) {
            if (checkSacnner.isKingChecked(new Move(this, king, king.col, king.row))) {
                // Highlight the king's position when checked
                checkedKingCol = king.col;
                checkedKingRow = king.row;

                // Show result popup for a win with "Play Again" functionality
                ResultPopup.showResult(isWhitetoMove ? "Black Wins!" : "White Wins!", this);
            } else {
                // Clear the checked king position
                checkedKingCol = -1;
                checkedKingRow = -1;

                // Show result popup for a stalemate with "Play Again" functionality
                ResultPopup.showResult("Stalemate!", this);
            }
        } else if (insufficentMaterial(true) && insufficentMaterial(false)) {
            // Clear the checked king position
            checkedKingCol = -1;
            checkedKingRow = -1;

            // Show result popup for insufficient material with "Play Again" functionality
            ResultPopup.showResult("Insufficient Material - Game Over!", this);
            isGameOver = true;
        } else {
            // Check if the king is under check
            if (checkSacnner.isKingChecked(new Move(this, king, king.col, king.row))) {
                // Mark the king's position if it's checked
                checkedKingCol = king.col;
                checkedKingRow = king.row;
            } else {
                // Clear the checked king position if not under check
                checkedKingCol = -1;
                checkedKingRow = -1;
            }
        }

        // Request board repaint after update
        repaint();
    }



    public void resetGame() {
        // Reset FEN string to the default position
        fenStringPostion = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        // Reload the position
        loadPostionFromFEN(fenStringPostion);

        // Reset other game variables
        selectedPiece = null;
        isWhitetoMove = true;
        isGameOver = false;
        checkedKingCol = -1;
        checkedKingRow = -1;
        enPassantTile = -1;

        // Repaint the board to reflect the reset state
        repaint();
    }



    private boolean insufficentMaterial(boolean isWhite){
        ArrayList<String> names=pieceList.stream()
                .filter(piece -> piece.isWhite==isWhite)
                .map(piece -> piece.name)
                .collect(Collectors.toCollection(ArrayList::new));

        if(names.contains("Queen")||names.contains("Rook")||names.contains("Pawn")){
            return false;
        }
        return names.size()<3;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Paint the chessboard
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Highlight the checked king's square in red
                if (r == checkedKingRow && c == checkedKingCol) {
                    g2d.setColor(new Color(255, 0, 0, 128)); // Semi-transparent red
                } else {
                    // Alternate square colors
                    boolean isLightSquare = (r + c) % 2 == 0;
                    g2d.setColor(isLightSquare ? new Color(227, 198, 181) : new Color(157, 105, 53));
                }
                g2d.fillRect(c * titlesize, r * titlesize, titlesize, titlesize);
                boolean isLightSquare = (r + c) % 2 == 0;
                // Determine text color based on square color
                Color textColor = isLightSquare ? Color.BLACK : Color.WHITE;

                // Add row numbers (on the left side)
                if (c == 0) { // Only add numbers on the first column
                    g2d.setColor(textColor);
                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                    String rowNumber = String.valueOf(8 - r);
                    g2d.drawString(rowNumber, c * titlesize + 5, r * titlesize + 15); // Adjusted position
                }

                // Add column letters (on the bottom side)
                if (r == rows - 1) { // Only add letters on the last row
                    g2d.setColor(textColor);
                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                    String colLetter = String.valueOf((char) ('a' + c));
                    g2d.drawString(colLetter, c * titlesize + titlesize - 12, r * titlesize + titlesize - 5); // Adjusted position
                }
            }
        }



        // Paint highlight for valid moves
        if (selectedPiece != null) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (isValidMove(new Move(this, selectedPiece, c, r))) {
                        g2d.setColor(new Color(68, 180, 57, 190)); // Green highlight with transparency
                        g2d.fillRect(c * titlesize, r * titlesize, titlesize, titlesize);
                    }
                }
            }
        }



//        paint pieces
        for(Piece piece : pieceList){
            piece.paint(g2d);
        }
    }

}
