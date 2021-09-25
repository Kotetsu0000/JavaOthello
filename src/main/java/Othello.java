public class Othello {
    double[][] board;
    int player;
    int passFlag;
    Othello(){
        reset();
    }

    void reset(){
        this.board = new double[][]{
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 1, 0, 0, 0},
                {0, 0, 0, 1, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        this.player = 1;
        this.passFlag = 0;
    }

    //盤面のコピーをする関数
    double[][] boardCopy(double[][] board){
        double[][] copy_board = new double[8][8];
        for (int i = 0; i < board.length; i++){
            System.arraycopy(board[i], 0, copy_board[i], 0, board.length);
        }
        return copy_board;
    }

    int[] changeDirection(int[] xy, int direction){
        if (direction == 0){return new int[]{xy[0]-1, xy[1]-1};}
        else if (direction == 1){return new int[]{xy[0], xy[1]-1};}
        else if (direction == 2){return new int[]{xy[0]+1, xy[1]-1};}
        else if (direction == 3){return new int[]{xy[0]-1, xy[1]};}
        else if (direction == 4){return new int[]{xy[0]+1, xy[1]};}
        else if (direction == 5){return new int[]{xy[0]-1, xy[1]+1};}
        else if (direction == 6){return new int[]{xy[0], xy[1]+1};}
        else if (direction == 7){return new int[]{xy[0]+1, xy[1]+1};}
        return xy;
    }

    //石が置けるかどうかの判定用
    int putCheck(double[][] board, int player, int[] xy, int direction, int number){
        xy = changeDirection(xy, direction);
        /*
         * 0 : 置ける
         * 1 : 置けない
         * 2 : 石がない又は盤面外検出
         * 3 : 異常終了
         */
        if (xy[0]>=0 & xy[0]< board.length & xy[1] >= 0 & xy[1] < board.length){
            if (board[xy[0]][xy[1]] == 0){
                return 2;
            }
            else if (!(board[xy[0]][xy[1]] == player)){
                return putCheck(board,player,xy,direction,number + 1);
            }
            else if (board[xy[0]][xy[1]] == player){
                if (number == 0){
                    return 1;
                }
                else if(!(number ==0)){
                    return 0;
                }
                else{
                    System.out.println("異常終了");
                    return 3;
                }
            }
        }
        else{
            return 2;
        }
        System.out.println("異常終了");
        return 3;
    }

    //石が置ける場所のリストを返す
    int[][] putPlace(double[][] board, int player){
        double[][] putPlaceBoard = new double[][]{{0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}};
        int numberOfStonePlace = 0;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                if (board[i][j] == 0){
                    for (int k = 0; k < 8; k++){
                        //System.out.println(putCheck(board, player, new int[]{i,j}, k, 0));
                        if (putCheck(board, player, new int[]{i,j}, k, 0) == 0){
                            putPlaceBoard[i][j] = 1;
                            numberOfStonePlace++;
                            break;
                        }
                    }
                }
            }
        }
        int[][] returnArray = new int[numberOfStonePlace][2];
        int k = 0;
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (putPlaceBoard[i][j] == 1){
                    returnArray[k][0] = i;
                    returnArray[k][1] = j;
                    k++;
                }
            }
        }
        return returnArray;
    }

    //ひっくり返す石のリストを返す
    int[][] turnStone(double[][] board, int player, int[] xy, int direction){
        double[][] turnPlaceBoard = new double[][]{{0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}};
        int numberOfStonePlace = 0;
        int[] xy_ = new int[2];
        xy_[0] = xy[0];
        xy_[1] = xy[1];
        while (true){
            xy_ = changeDirection(xy_, direction);
            //System.out.println(xy_[0] + "," + xy_[1]);
            if (xy_[0]>=0 & xy_[0]< board.length & xy_[1] >= 0 & xy_[1] < board.length){
                //System.out.println(board[xy_[0]][xy_[1]] );
                if (board[xy_[0]][xy_[1]] == 0){
                    numberOfStonePlace = 0;
                    break;
                }
                else if(!(board[xy_[0]][xy_[1]] ==player)){
                    turnPlaceBoard[xy_[0]][xy_[1]] = 1;
                    numberOfStonePlace++;
                }
                else if(board[xy_[0]][xy_[1]] ==player){
                    break;
                }
            }
            else{
                numberOfStonePlace = 0;
                break;
            }
        }
        if (numberOfStonePlace == 0){
            return new int[][]{{-1,-1}};
        }
        int[][] returnArray = new int[numberOfStonePlace][2];
        int k = 0;
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (turnPlaceBoard[i][j] == 1){
                    returnArray[k][0] = i;
                    returnArray[k][1] = j;
                    k++;
                }
            }
        }
        return returnArray;
    }

    //石を置く(置けないところに実行するとバグる可能性あり)
    double[][] put(double[][] board, int[] xy, int player){
        double[][] board_ = boardCopy(board);
        int[][] putPlaceList =  putPlace(board, player);
        for (int i = 0, putPlaceListLength = putPlaceList.length; i < putPlaceListLength; i++) {
            int[] ints = putPlaceList[i];
            if (ints[0] == xy[0] & ints[1] == xy[1]) {
                //System.out.println("Hellow!!!");
                board_[xy[0]][xy[1]] = player;
                for (int j = 0; j < 8; j++) {
                    int[][] turnStoneList = turnStone(board, player, xy, j);
                    for (int[] value : turnStoneList) {
                        //System.out.println(turnStoneList[k][0]+","+turnStoneList[k][1]);
                        if (value[0] != -1 & value[1] != -1) {
                            board_[value[0]][value[1]] = player;
                        }
                    }
                }

            }
        }
        return board_;
    }

    //石の数を数える
    int[] result(double[][] board){
        int black = 0;
        int white = 0;
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 1){
                    black += 1;
                }
                else if (board[i][j] == 2){
                    white += 1;
                }
            }
        }
        return new int[]{black,white};
    }
}
