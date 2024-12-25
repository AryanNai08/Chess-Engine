package main;

import pieces.Piece;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Input extends MouseAdapter {
    Board board;

    public Input(Board board){
        this.board=board;
    }
    @Override
    public void mousePressed(MouseEvent e) {
    int col=e.getX() /  board.titlesize;
    int row=e.getY() /  board.titlesize;

    Piece pieceXY=board.getPiece(col,row);
    if(pieceXY != null){
        board.selectedPiece=pieceXY;
    }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    if(board.selectedPiece !=null){
        board.selectedPiece.xPos=e.getX()-board.titlesize / 2;
        board.selectedPiece.yPos=e.getY()-board.titlesize / 2;

        board.repaint();
    }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        int col=e.getX() /  board.titlesize;
        int row=e.getY() /  board.titlesize;

        if(board.selectedPiece !=null){
            Move move=new Move(board,board.selectedPiece,col,row);

            if(board.isValidMove(move)){
                board.makeMove(move);
            }else {
                board.selectedPiece.xPos=board.selectedPiece.col * board.titlesize;
                board.selectedPiece.yPos=board.selectedPiece.row * board.titlesize;
            }
        }
        board.selectedPiece=null;
        board.repaint();
    }



}
