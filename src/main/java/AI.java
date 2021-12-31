import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.util.Arrays;

public class AI extends Thread{
    //public MultiLayerNetwork model;
    public ComputationGraph PVModel, PModel;
    public Othello othello;
    int[] AIPlace = null;
    boolean thinkFlag=true;
    int thinkNumber=0;

    MiniMax miniMax;
    BoardPred boardPred;

    double[][] predBoard;
    int predPlayer;
    int predNumber;
    int predAIMode;
    int predMode;
    //int maxDepth;
    double maxTime = 30000;
    boolean predStart=false;

    AI() throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
        System.out.println("AI_class start");
        // モデルの読み込み
        this.PVModel = this.loadModel("model/model_policy_01.h5");
        this.PModel = this.loadModel("model/model_sub_01.h5");

        this.boardPred = new BoardPred();
        this.miniMax = new MiniMax();

        this.othello = new Othello();
        this.othello.reset();
        this.predBoard = this.othello.board;

        System.out.println("AI_class ok");
    }

    public void run() {
        while (true){
            //System.out.println("CPU 待機中 !!");
            boardPred.predict(predBoard);
            if(predStart){
                System.out.println("思考開始");
                this.thinkNumber=0;
                this.AIPlace = this.selectAI();
                predStart=false;
                System.out.println("End");
            }
        }
    }

    ComputationGraph loadModel(String filePath) throws IOException, UnsupportedKerasConfigurationException, InvalidKerasConfigurationException {
        String simpleMlp = new ClassPathResource(filePath).getFile().getPath();
        return KerasModelImport.importKerasModelAndWeights(simpleMlp);
    }

    int[] selectAI(){
        /*
        * double[][] predBoard;
        * int predPlayer;
        * int predNumber;
        * int predAIMode;
        * int predMode;
        */
        //Value-MiniMax
        if(predAIMode==1){
            if(predMode==0){
                double[] pred = this.miniMax.selectLose(predBoard,predPlayer,predNumber,0);
                return new int[]{(int)pred[0],(int)pred[1]};
            }
            else if(predMode==1){
                double[] pred = this.miniMax.selectDraw(predBoard,predPlayer,predNumber,0);
                return new int[]{(int)pred[0],(int)pred[1]};
            }
            else if(predMode==2){
                double[] pred = this.miniMax.selectWin(predBoard,predPlayer,predNumber,0);
                return new int[]{(int)pred[0],(int)pred[1]};
            }
            else return null;
        }
        //Policy-MCTS
        else if(predAIMode==2){
            return new PolicyMCTS(this.PModel, this.predBoard, this.predPlayer, 0, this.predNumber, this.maxTime).select();
        }
        //PolicyValue-MCTS
        else if(predAIMode==3){
            return new PolicyValueMCTS(this.PVModel,this.PModel, this.predBoard, this.predPlayer, 0, this.predNumber, this.maxTime).select();
        }
        else return null;

    }

    public static class BoardPred{
        public MultiLayerNetwork model;
        BoardPred() throws IOException, UnsupportedKerasConfigurationException, InvalidKerasConfigurationException {
            String simpleMlp = new ClassPathResource("model/othello_model_var_9_06.h5").getFile().getPath();
            this.model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMlp);
        }
        //配列の形式をAIで推論できる形に変形する関数
        INDArray deformation(double[][] board){
            double[][][] temp_board = new double[2][8][8];
            for (int i = 0; i < board.length; i++){
                for (int j = 0; j < board.length; j++){
                    if (board[i][j] == 1){
                        temp_board[0][i][j] = 1;
                    }
                    else if (board[i][j] == 2){
                        temp_board[1][i][j] = 1;
                    }
                }
            }
            return Nd4j.create(new double[][][][]{temp_board});
        }

        double[] predict(double[][] board){
            return model.output(this.deformation(board)).toDoubleMatrix()[0];
        }
    }

    public class MiniMax{
        public MultiLayerNetwork model;
        public Othello othello;
        MiniMax() throws IOException, UnsupportedKerasConfigurationException, InvalidKerasConfigurationException {
            String simpleMlp = new ClassPathResource("model/othello_model_var_9_06.h5").getFile().getPath();
            this.model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMlp);
            this.othello = new Othello();
        }

        //配列の形式をAIで推論できる形に変形する関数
        INDArray deformation(double[][] board){
            double[][][] temp_board = new double[2][8][8];
            for (int i = 0; i < board.length; i++){
                for (int j = 0; j < board.length; j++){
                    if (board[i][j] == 1){
                        temp_board[0][i][j] = 1;
                    }
                    else if (board[i][j] == 2){
                        temp_board[1][i][j] = 1;
                    }
                }
            }
            return Nd4j.create(new double[][][][]{temp_board});
        }

        double[] predict(double[][] board){
            return model.output(this.deformation(board)).toDoubleMatrix()[0];
        }

        //盤面を選択する
        double[] selectWin(double[][] board, int player, int number, int passFlag){
            if(predStart){
                int[][] place = othello.putPlace(board, player);
                //試合が継続する場合
                if (passFlag < 2){
                    //予測手数が1手先の場合
                    if (number == 1){
                        //手がない場合
                        if (place.length == 0){
                            double[] pred = this.predict(board);
                            thinkNumber++;
                            return new double[] {-1,-1, pred[0], pred[1], pred[2]};
                        }
                        //手がある場合
                        else{
                            double[][] predList = new double[place.length][3];
                            double[] predPlayerList = new double[place.length];
                            for (int i = 0; i < place.length; i++){
                                //System.out.println("X : "+place[i][0]+ ", Y : " + place[i][0]);
                                predList[i] = this.predict(othello.put(board, place[i], player));
                                thinkNumber++;
                                predPlayerList[i] = predList[i][player-1];
                                //System.out.println("0:"+place[i][0]+", 1:"+place[i][1]+", 2:"+predList[i][0]+", 3:"+predList[i][1]+", 4:"+ predList[i][2]);
                            }
                            int index = maxIndex(predPlayerList);
                            return new double[] {place[index][0],place[index][1], predList[index][0], predList[index][1], predList[index][2]};
                        }
                    }
                    //予測手数が1手先でない場合
                    else{
                        if (place.length == 0){
                            double[] pred = selectWin(board, player%2+1, number - 1, passFlag+1);
                            return new double[] {-1,-1, pred[0], pred[1], pred[2]};
                        }
                        else{
                            double[][] predList = new double[place.length][3];
                            double[] predPlayerList = new double[place.length];
                            for (int i = 0; i < place.length; i++){
                                predList[i] = selectWin(othello.put(board, place[i], player), player%2+1, number - 1, 0);
                                predPlayerList[i] = predList[i][player+1];
                            }
                            int index = maxIndex(predPlayerList);
                            return new double[] {place[index][0],place[index][1],predList[index][2],predList[index][3],predList[index][4]};
                        }
                    }
                }
                //試合が終了する場合
                else{
                    int[] stone = othello.result(othello.board);
                    thinkNumber++;
                    //黒番(先手)の勝ち
                    if (stone[0]>stone[1]){
                        return new double[] {-1,-1,1,0,0};
                    }
                    //白番(後手)の勝ち
                    else if (stone[0]<stone[1]){
                        return new double[] {-1,-1,0,1,0};
                    }
                    //引き分け
                    else{
                        return new double[] {-1,-1,0,0,1};
                    }
                }
            }
            else{
                return new double[] {-1,-1,0,0,0};
            }
        }

        //盤面を選択する
        double[] selectLose(double[][] board, int player, int number, int passFlag){
            if(predStart){
                int[][] place = othello.putPlace(board, player);
                //試合が継続する場合
                if (passFlag < 2){
                    //予測手数が1手先の場合
                    if (number == 1){
                        //手がない場合
                        if (place.length == 0){
                            double[] pred = this.predict(board);
                            thinkNumber++;
                            return new double[] {-1,-1, pred[0], pred[1], pred[2]};
                        }
                        //手がある場合
                        else{
                            double[][] predList = new double[place.length][3];
                            double[] predPlayerList = new double[place.length];
                            for (int i = 0; i < place.length; i++){
                                //System.out.println("X : "+place[i][0]+ ", Y : " + place[i][0]);
                                predList[i] = this.predict(othello.put(board, place[i], player));
                                thinkNumber++;
                                predPlayerList[i] = predList[i][player%2];
                                //System.out.println("0:"+place[i][0]+", 1:"+place[i][1]+", 2:"+predList[i][0]+", 3:"+predList[i][1]+", 4:"+ predList[i][2]);
                            }
                            int index = maxIndex(predPlayerList);
                            return new double[] {place[index][0],place[index][1], predList[index][0], predList[index][1], predList[index][2]};
                        }
                    }
                    //予測手数が1手先でない場合
                    else{
                        if (place.length == 0){
                            double[] pred = selectLose(board, player%2+1, number - 1, passFlag+1);
                            return new double[] {-1,-1, pred[0], pred[1], pred[2]};
                        }
                        else{
                            double[][] predList = new double[place.length][3];
                            double[] predPlayerList = new double[place.length];
                            for (int i = 0; i < place.length; i++){
                                predList[i] = selectLose(othello.put(board, place[i], player), player%2+1, number - 1, 0);
                                predPlayerList[i] = predList[i][player%2];
                            }
                            int index = maxIndex(predPlayerList);
                            return new double[] {place[index][0],place[index][1],predList[index][2],predList[index][3],predList[index][4]};
                        }
                    }
                }
                //試合が終了する場合
                else{
                    int[] stone = othello.result(othello.board);
                    thinkNumber++;
                    //黒番(先手)の勝ち
                    if (stone[0]>stone[1]){
                        return new double[] {-1,-1,1,0,0};
                    }
                    //白番(後手)の勝ち
                    else if (stone[0]<stone[1]){
                        return new double[] {-1,-1,0,1,0};
                    }
                    //引き分け
                    else{
                        return new double[] {-1,-1,0,0,1};
                    }
                }
            }
            else{
                return new double[] {-1,-1,0,0,0};
            }
        }

        //盤面を選択する
        double[] selectDraw(double[][] board, int player, int number, int passFlag){
            if(predStart){
                int[][] place = othello.putPlace(board, player);
                //試合が継続する場合
                if (passFlag < 2){
                    //予測手数が1手先の場合
                    if (number == 1){
                        //手がない場合
                        if (place.length == 0){
                            double[] pred = this.predict(board);
                            thinkNumber++;
                            return new double[] {-1,-1, pred[0], pred[1], pred[2]};
                        }
                        //手がある場合
                        else{
                            double[][] predList = new double[place.length][3];
                            double[] predPlayerList = new double[place.length];
                            for (int i = 0; i < place.length; i++){
                                //System.out.println("X : "+place[i][0]+ ", Y : " + place[i][0]);
                                predList[i] = this.predict(othello.put(board, place[i], player));
                                thinkNumber++;
                                predPlayerList[i] = predList[i][2];
                                //System.out.println("0:"+place[i][0]+", 1:"+place[i][1]+", 2:"+predList[i][0]+", 3:"+predList[i][1]+", 4:"+ predList[i][2]);
                            }
                            int index = maxIndex(predPlayerList);
                            return new double[] {place[index][0],place[index][1], predList[index][0], predList[index][1], predList[index][2]};
                        }
                    }
                    //予測手数が1手先でない場合
                    else{
                        if (place.length == 0){
                            double[] pred = selectDraw(board, player%2+1, number - 1, passFlag+1);
                            return new double[] {-1,-1, pred[0], pred[1], pred[2]};
                        }
                        else{
                            double[][] predList = new double[place.length][3];
                            double[] predPlayerList = new double[place.length];
                            for (int i = 0; i < place.length; i++){
                                predList[i] = selectDraw(othello.put(board, place[i], player), player%2+1, number - 1, 0);
                                predPlayerList[i] = predList[i][2];
                            }
                            int index = maxIndex(predPlayerList);
                            return new double[] {place[index][0],place[index][1],predList[index][2],predList[index][3],predList[index][4]};
                        }
                    }
                }
                //試合が終了する場合
                else{
                    int[] stone = othello.result(othello.board);
                    thinkNumber++;
                    //黒番(先手)の勝ち
                    if (stone[0]>stone[1]){
                        return new double[] {-1,-1,1,0,0};
                    }
                    //白番(後手)の勝ち
                    else if (stone[0]<stone[1]){
                        return new double[] {-1,-1,0,1,0};
                    }
                    //引き分け
                    else{
                        return new double[] {-1,-1,0,0,1};
                    }
                }
            }
            else{
                return new double[] {-1,-1,0,0,0};
            }
        }

        int maxIndex(double[] list){
            double max = 0;
            int maxIndex = 0;
            for (int i = 0; i < list.length; i++){
                if (max<list[i]){
                    max = list[i];
                    maxIndex = i;
                }
            }
            return maxIndex;
        }
    }

    public class PolicyMCTS{
        Othello o;
        public ComputationGraph PModel;
        double[][] board;
        double maxTime, UCTScore;
        int[][] places;
        int[] win;
        int player, passFlag, maxDepth, visit, threshold;
        PolicyMCTS[] childTree = null;
        PolicyMCTS(ComputationGraph PModel, double[][] board, int player, int passFlag, int maxDepth, double maxTime){
            this.o = new Othello();
            this.PModel = PModel;
            this.board = this.o.boardCopy(board);
            this.player = player;
            this.passFlag = passFlag;
            this.maxDepth = maxDepth;
            this.maxTime = maxTime;
            this.places = this.o.putPlace(this.board, this.player);
            this.win = new int[]{0,0,0};
            this.visit = 0;

            this.threshold = 10;
        }

        //配列の形式をAIで推論できる形に変形する関数
        INDArray deformation(double[][] board) {
            double[][][] temp_board = new double[8][8][2];
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == 1) {
                        temp_board[i][j][0] = 1;
                    } else if (board[i][j] == 2) {
                        temp_board[i][j][1] = 1;
                    }
                }
            }
            return Nd4j.create(new double[][][][]{temp_board});
        }

        //double配列の合計値
        double sumDouble(double[] doubleList){
            double sum = 0.0;
            for(int i = 0; i < doubleList.length; i++){
                sum += doubleList[i];
            }
            return sum;
        }

        //ポリシーネットワークによる手の確率的選択
        int[] selectPolicy(double[][] board, int player){
            int[][] places = this.o.putPlace(board, player);
            double[] policy = this.PModel.output(this.deformation(this.board))[0].toDoubleMatrix()[0];
            double[] placesPolicy = new double[places.length];
            for(int i = 0; i < places.length; i++){
                int index = places[i][0] + places[i][0] * board.length;
                placesPolicy[i] = policy[index];
            }
            double sum = sumDouble(placesPolicy);
            for(int i = 0; i < places.length; i++){
                placesPolicy[i] = placesPolicy[i] / sum;
            }
            double rand = Math.random();
            int selectIndex = -1;
            while (rand>0){
                selectIndex += 1;
                rand -= placesPolicy[selectIndex];
            }
            return places[selectIndex];
        }

        //その地点からのゲームの結果をシミュレートして出力する
        int getWinPlayer(){
            int passFlag = this.passFlag;
            int player = this.player;
            double[][] board = this.o.boardCopy(this.board);
            while (passFlag<2){
                if(this.o.putPlace(board, player).length==0){
                    passFlag+=1;
                }
                else {
                    int[] place = selectPolicy(board, player);
                    board = this.o.put(board, place, player);
                }
                player = player%2+1;
            }
            int[] stone = this.o.result(board);
            if (stone[0]>stone[1]){
                return 0;
            }
            else if (stone[0]<stone[1]){
                return 1;
            }
            else{
                return 2;
            }
        }

        //枝のUCTScoreを計算する
        void updateChildUCTScore(){
            for (int i=0;i<childTree.length;i++){
                childTree[i].UCTScore = childTree[i].win[childTree[i].player-1]/childTree[i].visit + Math.sqrt(Math.log(this.visit)/childTree[i].visit);
            }
        }

        //一番UCTの高い枝を選ぶ
        int selectUCTIndex(){
            int returnIndex = 0;
            double bestScore = 0;
            for (int i=0;i<childTree.length;i++){
                if (bestScore < childTree[i].UCTScore){
                    bestScore = childTree[i].UCTScore;
                    returnIndex = i;
                }
            }
            return returnIndex;
        }

        //枝を伸ばす
        int[] makeChildTree(){
            int winIndex;
            int[] returnWin = new int[] {0,0,0};
            if (this.places.length==0){
                childTree = new PolicyMCTS[1];
                childTree[1] = new PolicyMCTS(this.PModel, this.board, this.player%2+1, this.passFlag + 1, this.maxDepth, this.maxTime);
                this.visit += 1;
                childTree[1].visit += 1;
                winIndex = childTree[1].getWinPlayer();
                this.win[winIndex] += 1;
                childTree[1].win[winIndex] += 1;
                returnWin[winIndex] += 1;
            }
            else {
                childTree = new PolicyMCTS[this.places.length];
                for (int i=0;i<this.places.length;i++){
                    childTree[i] = new PolicyMCTS(this.PModel, this.o.put(this.board, this.places[i], this.player), this.player%2+1, 0, this.maxDepth, this.maxTime);
                    this.visit += 1;
                    childTree[i].visit += 1;
                    winIndex = childTree[i].getWinPlayer();
                    this.win[winIndex] += 1;
                    childTree[i].win[winIndex] += 1;
                    returnWin[winIndex] += 1;
                }
            }
            return returnWin;
        }

        //プレイアウトをする関数
        int[] playout(){
            int[] returnWin = new int[]{0,0,0};
            int[] tempWin;
            if (threshold < this.visit){
                if(childTree == null){
                    tempWin = this.makeChildTree();
                    for (int i=0;i<3;i++){
                        returnWin[i]+=tempWin[i];
                    }
                }
                else {
                    updateChildUCTScore();
                    childTree[selectUCTIndex()].playout();
                }
            }
            else {
                returnWin[getWinPlayer()] += 1;
            }
            return returnWin;
        }

        //選択時に実行される関数
        int[] select(){
            double startTime = System.currentTimeMillis();
            int returnIndex = 0;
            int bestVisit = 0;
            int[] tempWin;
            this.win = this.makeChildTree();

            while (System.currentTimeMillis() - startTime < this.maxTime && predStart){
                updateChildUCTScore();
                tempWin = childTree[selectUCTIndex()].playout();
                for (int i=0;i<3;i++){
                    this.win[i]+=tempWin[i];
                }
            }

            for (int i=0;i<childTree.length;i++){
                if (bestVisit < childTree[i].win[childTree[i].player- 1]){
                    returnIndex = i;
                    bestVisit = childTree[i].win[childTree[i].player- 1];
                }
            }
            System.out.println(100.0 * this.win[this.player- 1] / (this.win[0]+this.win[1]+this.win[2]));
            System.out.println(Arrays.toString(this.win));

            return this.places[returnIndex];
        }

    }

    public static class PolicyValueMCTS{
        Othello o;
        ComputationGraph PVModel, PModel;
        double[][] board;
        double[] Policy;
        double maxTime, Value;
        int[][] places;
        int player, passFlag, maxDepth;
        PolicyValueMCTS[] childTree;

        PolicyValueMCTS(ComputationGraph PVModel,ComputationGraph PModel, double[][] board, int player, int passFlag, int maxDepth, double maxTime){
            this.o = new Othello();
            this.PVModel = PVModel;
            this.PModel = PModel;
            this.board = this.o.boardCopy(board);
            this.player = player;
            this.passFlag = passFlag;
            this.maxDepth = maxDepth;
            this.maxTime = maxTime;
            this.places = this.o.putPlace(this.board, this.player);

            this.predBoard();
        }

        //配列の形式をAIで推論できる形に変形する関数
        INDArray deformation(double[][] board) {
            double[][][] temp_board = new double[8][8][2];
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == 1) {
                        temp_board[i][j][0] = 1;
                    } else if (board[i][j] == 2) {
                        temp_board[i][j][1] = 1;
                    }
                }
            }
            return Nd4j.create(new double[][][][]{temp_board});
        }

        //盤面の評価値作成
        void predBoard(){
            INDArray[] pred = this.PVModel.output(this.deformation(this.board));
            this.Value = pred[0].toDoubleMatrix()[0][this.player - 1];
            this.Policy = pred[1].toDoubleMatrix()[0];
        }

        //double配列の合計値
        double sumDouble(double[] doubleList){
            double sum = 0.0;
            for(int i = 0; i < doubleList.length; i++){
                sum += doubleList[i];
            }
            return sum;
        }

        //ポリシーネットワークによる手の確率的選択
        int[] selectPolicy(double[][] board, int player){
            int[][] places = this.o.putPlace(board, player);
            double[] policy = this.PModel.output(this.deformation(this.board))[0].toDoubleMatrix()[0];
            double[] placesPolicy = new double[places.length];
            for(int i = 0; i < places.length; i++){
                int index = places[i][0] + places[i][0] * board.length;
                placesPolicy[i] = policy[index];
            }
            double sum = sumDouble(placesPolicy);
            for(int i = 0; i < places.length; i++){
                placesPolicy[i] = placesPolicy[i] / sum;
            }
            double rand = Math.random();
            int selectIndex = -1;
            while (rand>0){
                selectIndex += 1;
                rand -= placesPolicy[selectIndex];
            }
            return places[selectIndex];
        }

        //その地点からのゲームの結果をシミュレートして出力する
        int getWinPlayer(){
            int passFlag = this.passFlag;
            int player = this.player;
            double[][] board = this.o.boardCopy(this.board);
            while (passFlag<2){
                if(this.o.putPlace(board, player).length==0){
                    passFlag+=1;
                }
                else {
                    int[] place = selectPolicy(board, player);
                    board = this.o.put(board, place, player);
                }
                player = player%2+1;
            }
            int[] stone = this.o.result(board);
            if (stone[0]>stone[1]){
                return 0;
            }
            else if (stone[0]<stone[1]){
                return 1;
            }
            else{
                return 2;
            }
        }

        //木を先に掘っていく関数
        void makeChildTree(){
            if (this.places.length == 0){
                this.childTree = new PolicyValueMCTS[1];
            }
            else{
                this.childTree = new PolicyValueMCTS[this.places.length];
            }
        }

        //選択時に実行される関数
        int[] select(){
            return new int[] {0,0};
        }

    }
}
