package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Bishop extends Piece {
    public Bishop(Board board,int col,int row,boolean isWhite){
        super(board);
        this.col=col;
        this.row=row;
        this.xPos=col * board.titlesize;
        this.yPos=row * board.titlesize;

        this.isWhite=isWhite;
        this.name="Bishop";

        this.sprite= sheet.getSubimage(2 * sheetscale, isWhite?0 : sheetscale,sheetscale,sheetscale).getScaledInstance(board.titlesize,board.titlesize,BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col,int row){
        return Math.abs(this.col-col) == Math.abs(this.row-row);  // current.col - targeted col
    }

    public boolean moveCollidesWithPiece(int col,int row){
        //   up left
        if(this.col>col && this.row>row)
            for(int i=1;i<Math.abs(this.col-col);i++)
                if(board.getPiece(this.col-i,this.row-i) !=null)
                    return true;

        //   up right
        if(this.col<col && this.row>row)
            for(int i=1;i<Math.abs(this.col-col);i++)
                if(board.getPiece(this.col+i,this.row-i) !=null)
                    return true;

        //   down left
        if(this.col>col && this.row<row)
            for(int i=1;i<Math.abs(this.col-col);i++)
                if(board.getPiece(this.col-i,this.row+i) !=null)
                    return true;

        //   down right
        if(this.col<col && this.row<row)
            for(int i=1;i<Math.abs(this.col-col);i++)
                if(board.getPiece(this.col+i,this.row+i) !=null)
                    return true;
    return  false;
    }
}
