package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Pawn extends Piece{
    public Pawn(Board board,int col,int row,boolean isWhite){
        super(board);
        this.col=col;
        this.row=row;
        this.xPos=col * board.titlesize;
        this.yPos=row * board.titlesize;

        this.isWhite=isWhite;
        this.name="Pawn";

        this.sprite= sheet.getSubimage(5 * sheetscale, isWhite?0 : sheetscale,sheetscale,sheetscale).getScaledInstance(board.titlesize,board.titlesize,BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col,int row){
        int colorIndex = isWhite? 1:-1;

        //push pawn 1
        if(this.col==col && row==this.row - colorIndex && board.getPiece(col,row)==null)
            return true;

        //push pawn 2
        if(this.row==(isWhite ? 6:1) && this.col==col && row==this.row - colorIndex * 2 && board.getPiece(col,row)==null &&  board.getPiece(col,row+colorIndex)==null)
            return true;

        //capture left
        if(col == this.col-1 && row==this.row-colorIndex && board.getPiece(col,row)!=null)
            return true;

        //capture right
        if(col == this.col+1 && row==this.row-colorIndex && board.getPiece(col,row)!=null)
            return true;

        // en passant left
        if(board.getTitleNum(col,row)==board.enPassantTile && col== this.col-1 && row ==this.row-colorIndex && board.getPiece(col,row+colorIndex) !=null)
            return true;
        // en passant right
        if(board.getTitleNum(col,row)==board.enPassantTile && col== this.col+1 && row ==this.row-colorIndex && board.getPiece(col,row+colorIndex) !=null)
            return true;

        return false;
    }
}
