package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Queen extends Piece {
    public Queen(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.titlesize;
        this.yPos = row * board.titlesize;

        this.isWhite = isWhite;
        this.name = "Queen";

        this.sprite = sheet.getSubimage(1 * sheetscale, isWhite ? 0 : sheetscale, sheetscale, sheetscale)
                .getScaledInstance(board.titlesize, board.titlesize, BufferedImage.SCALE_SMOOTH);
    }

    /**
     * Checks if the target position is a valid movement for the queen.
     * A valid move must be along a straight line or diagonal and not blocked by any piece.
     */
    public boolean isValidMovement(int col, int row) {
        if (this.col == col || this.row == row || Math.abs(this.col - col) == Math.abs(this.row - row)) {
            // Ensure the move does not collide with any piece
            return !moveCollidesWithPiece(col, row);
        }
        return false;
    }

    /**
     * Checks if the queen's path to the target position collides with any piece.
     */
    public boolean moveCollidesWithPiece(int col, int row) {
        if (this.col == col && this.row == row) {
            // No movement, no collision
            return true;
        }

        if (this.col == col || this.row == row) {
            // Straight-line movement (rook-like)
            if (this.col > col) {
                // Left
                for (int c = this.col - 1; c > col; c--) {
                    if (board.getPiece(c, this.row) != null) {
                        return true;
                    }
                }
            } else if (this.col < col) {
                // Right
                for (int c = this.col + 1; c < col; c++) {
                    if (board.getPiece(c, this.row) != null) {
                        return true;
                    }
                }
            } else if (this.row > row) {
                // Up
                for (int r = this.row - 1; r > row; r--) {
                    if (board.getPiece(this.col, r) != null) {
                        return true;
                    }
                }
            } else if (this.row < row) {
                // Down
                for (int r = this.row + 1; r < row; r++) {
                    if (board.getPiece(this.col, r) != null) {
                        return true;
                    }
                }
            }
        } else {
            // Diagonal movement (bishop-like)
            int deltaCol = col > this.col ? 1 : -1;
            int deltaRow = row > this.row ? 1 : -1;

            int c = this.col + deltaCol;
            int r = this.row + deltaRow;
            while (c != col && r != row) {
                if (board.getPiece(c, r) != null) {
                    return true;
                }
                c += deltaCol;
                r += deltaRow;
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
