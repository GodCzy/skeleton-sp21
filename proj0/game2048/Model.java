package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author C2Y
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed = false;

        // 设置视角为指定的方向，简化处理逻辑
        board.setViewingPerspective(side);

        int i , j , record ;
        for(i = 0; i < this.size(); i++ ){
            int bottom = this.size();
            for(j = this.size() - 2; j >= 0 ; j-- ){
                int object = 0;
                if(board.tile(i,j) != null ){
                    object = board.tile(i,j).value();
                }   //获取被操作格的值

                if(object != 0){   //如果被操作格为0，则不进行检测或移动操作
                    for(record = j+1; record < bottom; record++ ){  //遍历寻找目标格
                        if(board.tile(i,record) != null) {   //寻找有数字的格或为位于最顶端的空白格

                            if (board.tile(i, record).value() != object) {  //不相等则移动到前一个格子
                                board.move(i, record - 1, board.tile(i, j));
                                changed = true;
                                break;
                            } else{  //相等则合并，并计算分数
                                board.move(i, record, board.tile(i, j));
                                score += 2 * object;
                                bottom = record;
                                changed = true;
                                break;
                            }

                        }else if(record == bottom - 1){
                            board.move(i, record , board.tile(i, j));
                            changed = true;
                            break;
                        }
                    }
                }
            }
        }

        // 恢复视角到北方向，便于后续操作
        board.setViewingPerspective(Side.NORTH);

        // 更新游戏结束状态
        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }


    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        for(int col = 0; col < b.size(); col = col + 1){
            for(int row = 0; row < b.size(); row = row + 1)
                if(b.tile(col, row) == null){
                    return true;
                }

        }

        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        for (int col = 0; col < b.size(); col++) {
            for (int row = 0; row < b.size(); row++) {
                Tile t = b.tile(col, row);
                // 检查该位置的瓷砖是否存在，以及其值是否为 MAX_PIECE
                if (t != null && t.value() == MAX_PIECE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        for (int col = 0; col < b.size(); col++) {
            for (int row = 0; row < b.size(); row++) {
                Tile t1 = b.tile(col, row);

                // 如果有空位置，返回true，因为存在有效移动
                if (t1 == null) {
                    return true;
                }

                // 检查当前瓷砖的右侧（col + 1）是否有相同的值
                if (col + 1 < b.size()) {
                    Tile t2 = b.tile(col + 1, row);
                    if (t2 != null && t1.value() == t2.value()) {
                        return true;
                    }
                }

                // 检查当前瓷砖的下方（row + 1）是否有相同的值
                if (row + 1 < b.size()) {
                    Tile t3 = b.tile(col, row + 1);
                    if (t3 != null && t1.value() == t3.value()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}




