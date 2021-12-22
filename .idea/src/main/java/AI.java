import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.security.Policy;

public class AI extends Thread{
    public MultiLayerNetwork model;
    public ComputationGraph PVModel, PModel;
    public Othello othello;
    double[] AIPlace = null;
    boolean thinkFlag=true;
    int thinkNumber=0;

    MiniMax miniMax;
    BoardPred boardPred;

    AI() throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
        System.out.println("AI_class start");
        // モデルの読み込み
        this.PVModel = this.loadModel("model/model_policy_01.h5");
        this.PModel = this.loadModel("model/model_sub_01.h5");

        boardPred = new BoardPred();
        this.miniMax = new MiniMax();

        System.out.println("AI_class ok");
    }

    double[][] predBoard;
    int predPlayer;
    int predNumber;
    int predAIMode;
    int predMode;
    boolean predStart=false;

    public void run() {
        while (true){
            System.out.println("CPU 待機中 !!");
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

    double[] selectAI(){
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
                return this.miniMax.selectLose(predBoard,predPlayer,predNumber,0);
            }
            else if(predMode==1){
                return this.miniMax.selectDraw(predBoard,predPlayer,predNumber,0);
            }
            else if(predMode==2){
                return this.miniMax.selectWin(predBoard,predPlayer,predNumber,0);
            }
            else return null;
        }
        //Policy-MCTS
        else if(predAIMode==2){
            return null;
        }
        //PolicyValue-MCTS
        else if(predAIMode==3){
            return null;
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
        MiniMax() throws IOException, UnsupportedKerasConfigurationException, InvalidKerasConfigurationException {
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
        public ComputationGraph model;
        PolicyMCTS(ComputationGraph model){
            this.model = model;
        }
    }
}
