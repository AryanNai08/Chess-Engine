//package pieces;
//
//import main.Board;
//
//import java.awt.image.BufferedImage;
//
//public class Rook extends Piece{
//    public Rook(Board board,int col,int row,boolean isWhite){
//        super(board);
//        this.col=col;
//        this.row=row;
//        this.xPos=col * board.titlesize;
//        this.yPos=row * board.titlesize;
//
//        this.isWhite=isWhite;
//        this.name="Rook";
//
//        this.sprite= sheet.getSubimage(4 * sheetscale, isWhite?0 : sheetscale,sheetscale,sheetscale).getScaledInstance(board.titlesize,board.titlesize,BufferedImage.SCALE_SMOOTH);
//    }
//
//    public boolean isValidMovement(int col,int row){
//        return this.col == col || this.row==row;
//    }
//
//    public boolean moveCollidesWithPiece(int col,int row){
//        //        left
//        if(this.col>col)
//            for(int c=this.col-1;c>col;c--)
//                if(board.getPiece(c,this.row)!= null)
//                    return true;
//
//        //        Right
//        if(this.col<col)
//            for(int c=this.col+1;c<col;c++)
//                if(board.getPiece(c,this.row)!= null)
//                    return true;
//
//        //        Up
//        if(this.row>row)
//            for(int r=this.row-1;r>row;r--)
//                if(board.getPiece(this.col,r)!= null)
//                    return true;
//
//        //        down
//        if(this.row<row)
//            for(int r=this.row+1;r<row;r--)
//                if(board.getPiece(this.col,r)!= null)
//                    return true;
//
//        return false;
//
//    }
//}

package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Rook extends Piece {
    public Rook(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.titlesize;
        this.yPos = row * board.titlesize;

        this.isWhite = isWhite;
        this.name = "Rook";

        this.sprite = sheet.getSubimage(4 * sheetscale, isWhite ? 0 : sheetscale, sheetscale, sheetscale)
                .getScaledInstance(board.titlesize, board.titlesize, BufferedImage.SCALE_SMOOTH);
    }

    /**
     * Checks if the target position is a valid movement for the rook.
     * A valid move must be along a straight line and not blocked by any piece.
     */
    public boolean isValidMovement(int col, int row) {
        // Rook moves in a straight line: either the same row or the same column
        if (this.col == col || this.row == row) {
            // Ensure the move does not collide with any piece
            return !moveCollidesWithPiece(col, row);
        }
        return false;
    }

    /**
     * Checks if the rook's path to the target position collides with any piece.
     */
    public boolean moveCollidesWithPiece(int col, int row) {
        if (this.col == col && this.row == row) {
            // No movement, no collision
            return true;
        }

        // Straight-line movement (horizontal or vertical)
        if (this.col > col) {
            // Moving left
            for (int c = this.col - 1; c > col; c--) {
                if (board.getPiece(c, this.row) != null) {
                    return true;
                }
            }
        } else if (this.col < col) {
            // Moving right
            for (int c = this.col + 1; c < col; c++) {
                if (board.getPiece(c, this.row) != null) {
                    return true;
                }
            }
        } else if (this.row > row) {
            // Moving up
            for (int r = this.row - 1; r > row; r--) {
                if (board.getPiece(this.col, r) != null) {
                    return true;
                }
            }
        } else if (this.row < row) {
            // Moving down
            for (int r = this.row + 1; r < row; r++) {
                if (board.getPiece(this.col, r) != null) {
                    return true;
                }
            }
        }

        // Ensure the target square is not occupied by a friendly piece
        Piece targetPiece = board.getPiece(col, row);
        if (targetPiece != null && targetPiece.isWhite == this.isWhite) {
            return true; // Can't capture own piece
        }

        return false;
    }
}

