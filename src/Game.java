import java.util.Random;
import java.util.function.Consumer;

public class Game {
    private Random rnd = new Random();
    public static final Game.GameCell EMPTY_CELL = new Game.GameCell(Game.CellState.EMPTY, 0);

    public enum CellState {
        FULL,
        EMPTY
    }

    public enum GameState {
        NOT_STARTED,
        PLAYING,
        WIN,
        FAIL
    }

    public static int maxValue = 0;


    public static int getMaxValue() {
        return maxValue;
    }

    public static void setMaxValue(int value) {
        maxValue = value;
    }

    public static class GameCell {
        private CellState state;
        private int value;

        public GameCell(CellState state, int value) {
            this.state = state;
            this.value = value;
        }

        public CellState getState() {
            return state;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int cellValue) {
            if (cellValue == 0) {
                value = 0;
                state = CellState.EMPTY;
            } else {
                value = cellValue;
                state = CellState.FULL;
            }

        }

        public void upValue() {
            setValue(++value);
        }

        public void setEmptyCell(){
            state = CellState.EMPTY;
            value = 0;
        }
        public boolean changeValue(GameCell cell) {
            if (cell.getState() == CellState.FULL && cell.getValue() == value) {
                upValue();
                if (value > getMaxValue()) {
                    setMaxValue(value);
                }
                cell.setEmptyCell();
                return true;
            } else if (getState() == CellState.EMPTY && cell.getState() == CellState.FULL) {
                setValue(cell.getValue());
                cell.setEmptyCell();
            }
            return false;
        }

    }

    private static GameCell[][] gameField;
    GameState state = GameState.NOT_STARTED;

    public Game() {
    }



    public int getRowCount() {
        return gameField == null ? 0 : gameField.length;
    }

    public int getColCount() {
        return gameField == null ? 0 : gameField[0].length;
    }

    public GameState getState(){
        return state;
    }
    public GameState currentGameState(){
        if(getMaxValue() >= 12){
            return GameState.WIN;
        } else if (!checkField()) {
            return GameState.FAIL;
        }
        else{
            return GameState.PLAYING;
        }
    }

    private boolean checkSides(int [][] check, int row, int col){
        int chk = check[row][col];
        boolean existsLeftSide = (col - 1 >= 0);
        boolean existsRightSide = (col + 1 < check[0].length);
        boolean existsTopSide = (row - 1 >= 0);
        boolean existsDownSide = (row + 1 < check.length);
        boolean ans = false;
        if(existsTopSide){
            ans = ans || chk == check[row-1][col];
        }
        if(existsDownSide){
            ans = ans || chk == check[row+1][col];
        }
        if(existsLeftSide){
            ans = ans || chk == check[row][col-1];
        }
        if(existsRightSide){
            ans = ans || chk == check[row][col+1];
        }

        return ans;

    }
    private int[][] intField(){
        int[][] ans = new int[getRowCount()][];
        for(int i = 0; i < gameField.length; i++){
            int[] tmp = new int[getColCount()];
            for(int j = 0; j < gameField[i].length; j++){
                tmp[j] = gameField[i][j].getValue();
            }
            ans[i] = tmp;
        }
        return ans;
    }
    private boolean checkField(){
        int[][] mask = blackAndWhiteMatrix(gameField);
        boolean emptyFlag = false;
        for(int i = 0; i < mask.length; i++){
            for(int j = 0; j < mask[i].length; j++){
                if(mask[i][j] != -1){
                    emptyFlag = true;
                    return true;
                }
            }
        }

        int[][] intCheckField = intField();
        boolean sidesFlag = false;
        for(int i = 0; i < intCheckField.length; i++){
            for(int j = 0; j < intCheckField[i].length; j++){
                if(checkSides(intCheckField, i, j)){
                    sidesFlag = true;
                    return true;
                }
            }
        }
        return false;
    }

    public void createRandomCell() {
        int rowRand;
        int colRand;

        if (maxValue == 0) {
            int cellCount = 0;

            while (cellCount < 2) {
                rowRand = rnd.nextInt(0, getRowCount());
                colRand = rnd.nextInt(0, getColCount());
                GameCell tmpCell = gameField[rowRand][colRand];
                if (tmpCell.getState() == CellState.EMPTY) {
                    tmpCell.setValue(1);
                    cellCount++;
                }
            }

        } else {
            rowRand = rnd.nextInt(0, getRowCount());
            colRand = rnd.nextInt(0, getColCount());
            GameCell tmpCell = gameField[rowRand][colRand];
            while(tmpCell.getState() == CellState.EMPTY) {
                if (maxValue == 2) {
                    tmpCell.setValue(2);
                } else {
                    int r = rnd.nextInt(1, maxValue / 2 + 1);
                    tmpCell.setValue(r);
                }

            }

        }
    }

        public void newGame(int colCount, int rowCount){
            gameField = new GameCell[colCount][rowCount];
            //create empty field
            for (int i = 0; i < colCount; i++) {
                for (int j = 0; j < rowCount; j++) {
                    gameField[i][j] = new GameCell(CellState.EMPTY, 0);
                }
            }

            //create 2 start cell
            createRandomCell();
            state = GameState.PLAYING;
        }
        public int[][] blackAndWhiteMatrix(GameCell[][] arr){
            int[][] ans = new int[arr.length][];
            for(int i = 0; i < arr.length; i++){
                int[] tmpArr = new int[arr[i].length];
                for(int j = 0; j < arr[i].length; j++){
                    tmpArr[j] = (arr[i][j].getValue() == 0 ? 0:-1);
                }
                ans[i] = tmpArr;
            }
            return ans;
        }

    private static void mazePathFinder(int[][] maze, int row, int col, int step) {
        int rowCount = maze.length;
        int colCount = maze[0].length;

        if (row < 0 || row >= rowCount ||
                col < 0 || col >= colCount) {
            return;
        }
        if (maze[row][col] != 0) {
            return;
        }
        maze[row][col] = step;

        mazePathFinder(maze, row - 1, col, step + 1);
        mazePathFinder(maze, row, col - 1, step + 1);
        mazePathFinder(maze, row, col + 1, step + 1);
        mazePathFinder(maze, row + 1, col, step + 1);
    }

        public boolean canCellChange(int row1, int col1, int row2, int col2){
            int [][] maze = blackAndWhiteMatrix(gameField);


            maze[row1][col1] = 0;
            maze[row2][col2] = 0;
            mazePathFinder(maze, row1, col1, 1);

//            for(int i = 0; i < maze.length; i++){
//                for(int j = 0; j < maze[i].length; j++){
//                    System.out.print(maze[i][j]);
//                }
//                System.out.println();
//            }

            return (maze[row2][col2] > 0);

        }

        public void changeCell(int rowIndex1, int colIndex1, int rowIndex2, int colIndex2){
            GameCell fromCell = gameField[rowIndex2][colIndex2];
            GameCell toCell = gameField[rowIndex1][colIndex1];
            fromCell.changeValue(toCell);
            createRandomCell();
        }
        public int getCellValue(int row, int col) {
            int rowCount = getRowCount(), colCount = getColCount();
            if (row < 0 || row >= rowCount || col < 0 || col >= colCount) {
                return 0;
            }

            return gameField[row][col].getValue();
        }
    }

