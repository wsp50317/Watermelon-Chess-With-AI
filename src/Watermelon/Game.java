package Watermelon;
import java.util.Arrays;

//(改)  為了方便控制遊戲流程我加了這個class負責遊戲流程有關的功能
public class Game {

	public ChessBoard t =new ChessBoard(); //使用視窗介面的class
	
	public  Game (){
    	Thread thread = new Thread(t); //這個thread是讓視窗畫面一直刷新用的
    	thread.start();
	}	
	
	//*** 由介面獲得使用者輸入
	public Node GetUserInput(Node CorrentNode){
		int[] NewBoard = new int[21];	//新盤面
		Node nextStep = new Node();		//記錄下一步盤面資料的節點
		while(true) //等待使用者輸入
		{
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(t.GetRefleshSignal()) //使用者輸入了
			{
				System.out.println("使用者輸入了");
				NewBoard = t.GetBoardArray();//< = 取得使用者輸入的新盤面 
				
				System.out.println("使用者輸入的新盤面:");				
				for(int j=0;j<21;j++){
					System.out.print(NewBoard[j]+",");
				}
			    System.out.println();
			    
				break;
			}
		} 
		t.SetRefleshSignal(false);
		nextStep.board = Eat(NewBoard); //判斷是否有吃子	
		t.PlayerEatBoardArray(nextStep.board);//把吃子後的board寫到前端
		
		return nextStep; 
	}
	//*** 將電腦的輸入show到螢幕上
	public void ShowComputer(){

	}
	//*** 將最終的輸贏show到螢幕上
	public void ShowWinLose(String WinORLose){
		t.ShowWinORLose(WinORLose);
	}

	public static String WinOrLose(Node n){
		String WinOrLose = "Non";
		
		//算出黑白子的數量
		int w=0,b=0;
		for(int j=0;j<21;j++){
			if(n.board[j]==1) w++;
			else if(n.board[j]==2) b++;
		}
		//判斷是否由戲結束  並輸出使用者輸贏的結果  Win / Lose / Non(還沒分出勝負)
		if(Main.PlayerMode.equals("Black")){
			if(w<=2) 
			{
				WinOrLose = "Win";
			}
			else if(b<=2) 
			{
				WinOrLose = "Lose";
			}
		}
		if(Main.PlayerMode.equals("White")){
			if(w<=2)
			{
				WinOrLose = "Lose";
			}
			else if(b<=2)
			{
				WinOrLose = "Win";
			}
		}
		return WinOrLose;
	}
	
	public Node GetComputerInput(Node CorrentNode){
		Node nextStep = new Node();
		AlphaBetaAlgorithm AB = new AlphaBetaAlgorithm(Main.PlayerMode,CorrentNode);
		
		
		AB.alphaBeta(CorrentNode, 8, -java.lang.Integer.MAX_VALUE, java.lang.Integer.MAX_VALUE);//電腦算出下一步
		
		nextStep = AB.B;
		//限制不能來回走4次
				if(Limit(nextStep.board)) {System.out.println("123");nextStep = AB.NB;}  //如果將要來回走4次  走次佳點
		//

		System.out.print("電腦算出的nextStep : ");
		for(int j=0;j<21;j++){
			System.out.print(AB.B.board[j]+",");
		}
	    System.out.println();
		t.ComputerSetBoardArray(nextStep.board);//放入電腦下一步的值
		System.out.println("電腦計算完成!");
		return nextStep; 
	}
	
	//限制不能來回走4次
		int[] l_4 = new int[4];
		int len = 0;
		public boolean Limit(int[] BoardArrayFromBack){
			int MoveChessNewPosition =-1;
			int Minor =-1;
		
			for(int i=0 ; i<21 ; i++) //找出新的跟舊的差在哪裡
	    	{
	    		Minor = t.CurrentBoardArray[i] - BoardArrayFromBack[i]; 
	    		if(Main.PlayerMode=="White") //AI是白色棋子 //找1變0 0變1的點
	    		{
	        		if(Minor == -1) //0變1 代表電腦下的點
	        		{
	        			MoveChessNewPosition = i; //移除棋子的位置
	        			break;
	        		}
	    		}
	    		else if(Main.PlayerMode=="Black")	//AI是黑色棋子 //找2變0 0變2的點
	    		{
	        		if(Minor == -2) //0變2 代表電腦下的點
	        		{
	        			MoveChessNewPosition = i; //移除棋子的位置
	        			break;
	        		}
	    		}
	    	}
			System.out.println("MoveChessNewPosition "+MoveChessNewPosition);
			
			//判斷有沒有重複走
			if(len==0){
				l_4[len]=MoveChessNewPosition;   //記錄走的位置
				len++;
			}
			else if(len==1){
				l_4[len]=MoveChessNewPosition;   //記錄走的位置
				len++;
			}
			else if(len==2){
				if(l_4[0]==MoveChessNewPosition) {		//如果第3步等於第1步
					l_4[len]=MoveChessNewPosition;   //記錄走的位置
					len++;
				}
				else {										//如果第3步不等於第1步
					Arrays.fill(l_4, 0);		 //清除記錄
					len=0;
					l_4[len]=MoveChessNewPosition;   //將這次記錄為第一次
					len++;
				}
			}
			else if(len==3){
				if(l_4[1]==MoveChessNewPosition) {		//如果第4步等於第2步
					Arrays.fill(l_4, 0);		 //清除記錄
					len=0;
					return true;                //重複走4次回傳錯誤
				}
				else {										//如果第4步不等於第2步
					Arrays.fill(l_4, 0);		 //清除記錄
					len=0;
					l_4[len]=MoveChessNewPosition;   //將這次記錄為第一次
					len++;
				}
			}
			//
			for(int i=0;i<len;i++) System.out.print(l_4[i]+" ");
			System.out.println();
			
			//
			return false;		
		}

    //-------------------------吃子--------------------------------	
	static int[] block ;
	static int blockLength ;   
	static int[] map;
	static public int[] Eat(int[] board){
		map = Arrays.copyOf(board, board.length);
		//用Board分析目前盤面判斷是否可以吃子 
	        for(int i = 0;i < 21; i++){
	     
	                if(map[i] == 0)  
	                   continue;
	                else{
	                	block = new int[21];
	                	blockLength = 1;                   
	                   	block[0] = i; 
	                   	//System.out.println("map :"+ i);
	                    recursion(i); 
	                    //System.out.println(i);
	                    if(hasQi()) 
	                    	continue;            
	                    else {
	                        for(int t = 0;t < blockLength; t++){
	                        	//System.out.println("block :"+ block[t]);
	                        	map[block[t]] = 0; 
	                        }
	                    }
	                }
	            
	        }
	   
		return map; 
	}
	
	public static boolean isInBlock(int x){
		for(int i=0; i<blockLength ; i++){
			if(x==block[i]) return false;
		}		
		return true; 
	}
	
    public static void recursion(int index){   
    	if(index == 0 ){ 	
			if(map[1]==map[index]&& isInBlock(1)){block[blockLength] = 1;   blockLength++;   recursion(1);} 
			if(map[2]==map[index]&& isInBlock(2)){block[blockLength] = 2;   blockLength++;   recursion(2);} 
			if(map[3]==map[index]&& isInBlock(3)){block[blockLength] = 3;   blockLength++;   recursion(3);} 			
		}
		else if(index==1){
			if(map[0]==map[index]&& isInBlock(0)){block[blockLength] = 0;   blockLength++;   recursion(0);} 
			if(map[2]==map[index]&& isInBlock(2)){block[blockLength] = 2;   blockLength++;   recursion(2);} 
			if(map[4]==map[index]&& isInBlock(4)){block[blockLength] = 4;   blockLength++;   recursion(4);} 
		}
		else if(index==2){
			if(map[0]==map[index]&& isInBlock(0)){block[blockLength] = 0;   blockLength++;   recursion(0);} 
			if(map[1]==map[index]&& isInBlock(1)){block[blockLength] = 1;   blockLength++;   recursion(1);} 
			if(map[3]==map[index]&& isInBlock(3)){block[blockLength] = 3;   blockLength++;   recursion(3);} 
			if(map[5]==map[index]&& isInBlock(5)){block[blockLength] = 5;   blockLength++;   recursion(5);} 
		}
		else if(index==3){
			if(map[0]==map[index]&& isInBlock(0)){block[blockLength] = 0;   blockLength++;   recursion(0);} 
			if(map[2]==map[index]&& isInBlock(2)){block[blockLength] = 2;   blockLength++;   recursion(2);} 
			if(map[6]==map[index]&& isInBlock(6)){block[blockLength] = 6;   blockLength++;   recursion(6);} 
		}
		else if(index==4){
			if(map[1]==map[index]&& isInBlock(1)){block[blockLength] = 1;   blockLength++;   recursion(1);} 
			if(map[7]==map[index]&& isInBlock(7)){block[blockLength] = 7;   blockLength++;   recursion(7);} 
			if(map[8]==map[index]&& isInBlock(8)){block[blockLength] = 8;   blockLength++;   recursion(8);} 
		}
		else if(index==5){
			if(map[2]==map[index]&& isInBlock(2)){block[blockLength] = 2;   blockLength++;   recursion(2);} 
			if(map[9]==map[index]&& isInBlock(9)){block[blockLength] = 9;   blockLength++;   recursion(9);} 
			if(map[10]==map[index]&& isInBlock(10)){block[blockLength] = 10;   blockLength++;   recursion(10);} 
			if(map[11]==map[index]&& isInBlock(11)){block[blockLength] = 11;   blockLength++;   recursion(11);} 
		}
		else if(index==6){
			if(map[3]==map[index]&& isInBlock(3)){block[blockLength] = 3;   blockLength++;   recursion(3);} 
			if(map[12]==map[index]&& isInBlock(12)){block[blockLength] = 12;   blockLength++;   recursion(12);} 
			if(map[13]==map[index]&& isInBlock(13)){block[blockLength] = 13;   blockLength++;   recursion(13);} 
		}
		else if(index==7){
			if(map[4]==map[index]&& isInBlock(4)){block[blockLength] = 4;   blockLength++;   recursion(4);} 
			if(map[8]==map[index]&& isInBlock(8)){block[blockLength] = 8;   blockLength++;   recursion(8);} 
			if(map[14]==map[index]&& isInBlock(14)){block[blockLength] = 14;   blockLength++;   recursion(14);} 
		}
		else if(index==8){
			if(map[4]==map[index]&& isInBlock(4)){block[blockLength] = 4;   blockLength++;   recursion(4);} 
			if(map[7]==map[index]&& isInBlock(7)){block[blockLength] = 7;   blockLength++;   recursion(7);} 
			if(map[9]==map[index]&& isInBlock(8)){block[blockLength] = 9;   blockLength++;   recursion(9);} 
			if(map[14]==map[index]&& isInBlock(14)){block[blockLength] = 14;   blockLength++;   recursion(14);} 
		}
		else if(index==9){
			if(map[5]==map[index]&& isInBlock(5)){block[blockLength] = 5;   blockLength++;   recursion(5);} 
			if(map[8]==map[index]&& isInBlock(8)){block[blockLength] = 8;   blockLength++;   recursion(8);} 
			if(map[10]==map[index]&& isInBlock(10)){block[blockLength] = 10;   blockLength++;   recursion(10);} 
			if(map[15]==map[index]&& isInBlock(15)){block[blockLength] = 15;   blockLength++;   recursion(15);} 
		}
		else if(index==10){
			if(map[5]==map[index]&& isInBlock(5)){block[blockLength] = 5;   blockLength++;   recursion(5);} 
			if(map[9]==map[index]&& isInBlock(9)){block[blockLength] = 9;   blockLength++;   recursion(9);} 
			if(map[11]==map[index]&& isInBlock(11)){block[blockLength] = 11;   blockLength++;   recursion(11);} 
			if(map[15]==map[index]&& isInBlock(15)){block[blockLength] = 15;   blockLength++;   recursion(15);} 
		}
		else if(index==11){
			if(map[5]==map[index]&& isInBlock(5)){block[blockLength] = 5;   blockLength++;   recursion(5);} 
			if(map[10]==map[index]&& isInBlock(10)){block[blockLength] = 10;   blockLength++;   recursion(10);} 			
			if(map[12]==map[index]&& isInBlock(11)){block[blockLength] = 12;   blockLength++;   recursion(12);} 
			if(map[15]==map[index]&& isInBlock(15)){block[blockLength] = 15;   blockLength++;   recursion(15);} 
		}
		else if(index==12){
			if(map[6]==map[index]&& isInBlock(6)){block[blockLength] = 6;   blockLength++;   recursion(6);} 
			if(map[11]==map[index]&& isInBlock(11)){block[blockLength] = 11;   blockLength++;   recursion(11);} 
			if(map[13]==map[index]&& isInBlock(13)){block[blockLength] = 13;   blockLength++;   recursion(13);} 
			if(map[16]==map[index]&& isInBlock(16)){block[blockLength] = 16;   blockLength++;   recursion(16);}  
		}
		else if(index==13){
			if(map[6]==map[index]&& isInBlock(6)){block[blockLength] = 6;   blockLength++;   recursion(6);} 
			if(map[12]==map[index]&& isInBlock(12)){block[blockLength] = 12;   blockLength++;   recursion(12);} 
			if(map[16]==map[index]&& isInBlock(16)){block[blockLength] = 16;   blockLength++;   recursion(16);} 
		}
		else if(index==14){
			if(map[7]==map[index]&& isInBlock(7)){block[blockLength] = 7;   blockLength++;   recursion(7);} 
			if(map[8]==map[index]&& isInBlock(8)){block[blockLength] = 8;   blockLength++;   recursion(8);} 
			if(map[17]==map[index]&& isInBlock(17)){block[blockLength] = 17;   blockLength++;   recursion(17);} 
		}
		else if(index==15){
			if(map[9]==map[index]&& isInBlock(9)){block[blockLength] = 8;   blockLength++;   recursion(9);} 
			if(map[10]==map[index]&& isInBlock(10)){block[blockLength] = 10;   blockLength++;   recursion(10);} 
			if(map[11]==map[index]&& isInBlock(11)){block[blockLength] = 11;   blockLength++;   recursion(11);} 
			if(map[18]==map[index]&& isInBlock(18)){block[blockLength] = 18;   blockLength++;   recursion(18);} 
		}
		else if(index==16){
			if(map[12]==map[index]&& isInBlock(12)){block[blockLength] = 12;   blockLength++;   recursion(12);} 
			if(map[13]==map[index]&& isInBlock(13)){block[blockLength] = 13;   blockLength++;   recursion(13);} 
			if(map[19]==map[index]&& isInBlock(19)){block[blockLength] = 19;   blockLength++;   recursion(19);} 
		}
		else if(index==17){
			if(map[14]==map[index]&& isInBlock(14)){block[blockLength] = 14;   blockLength++;   recursion(14);} 
			if(map[18]==map[index]&& isInBlock(18)){block[blockLength] = 18;   blockLength++;   recursion(18);} 
			if(map[20]==map[index]&& isInBlock(20)){block[blockLength] = 20;   blockLength++;   recursion(20);} 
		}
		else if(index==18){
			if(map[15]==map[index]&& isInBlock(15)){block[blockLength] = 15;   blockLength++;   recursion(15);} 
			if(map[17]==map[index]&& isInBlock(17)){block[blockLength] = 17;   blockLength++;   recursion(17);} 
			if(map[19]==map[index]&& isInBlock(19)){block[blockLength] = 19;   blockLength++;   recursion(19);} 
			if(map[20]==map[index]&& isInBlock(20)){block[blockLength] = 20;   blockLength++;   recursion(20);} 
		}
		else if(index==19){
			if(map[16]==map[index]&& isInBlock(16)){block[blockLength] = 16;   blockLength++;   recursion(16);} 
			if(map[18]==map[index]&& isInBlock(18)){block[blockLength] = 18;   blockLength++;   recursion(18);} 
			if(map[20]==map[index]&& isInBlock(20)){block[blockLength] = 20;   blockLength++;   recursion(20);} 
		}
		else if(index==20){
			if(map[17]==map[index]&& isInBlock(17)){block[blockLength] = 17;   blockLength++;   recursion(17);} 
			if(map[18]==map[index]&& isInBlock(18)){block[blockLength] = 18;   blockLength++;   recursion(18);} 
			if(map[19]==map[index]&& isInBlock(19)){block[blockLength] = 19;   blockLength++;   recursion(19);} 
		}	      
    }

    public static boolean hasQi(){
        for(int index = 0;index < blockLength; index++){
        	if(block[index] == 0 ){ 	
    			if(map[1]==0){return true;} 
    			if(map[2]==0){return true;} 
    			if(map[3]==0){return true;} 			
    		}
    		else if(block[index]==1){
    			if(map[0]==0){return true;} 
    			if(map[2]==0){return true;} 
    			if(map[4]==0){return true;} 
    		}
    		else if(block[index]==2){
    			if(map[0]==0){return true;} 
    			if(map[1]==0){return true;} 
    			if(map[3]==0){return true;} 
    			if(map[5]==0){return true;} 
    		}
    		else if(block[index]==3){
    			if(map[0]==0){return true;} 
    			if(map[2]==0){return true;} 
    			if(map[6]==0){return true;} 
    		}
    		else if(block[index]==4){
    			if(map[1]==0){return true;} 
    			if(map[7]==0){return true;} 
    			if(map[8]==0){return true;} 
    		}
    		else if(block[index]==5){
    			if(map[2]==0){return true;} 
    			if(map[9]==0){return true;} 
    			if(map[10]==0){return true;} 
    			if(map[11]==0){return true;} 
    		}
    		else if(block[index]==6){
    			if(map[3]==0){return true;} 
    			if(map[12]==0){return true;} 
    			if(map[13]==0){return true;} 
    		}
    		else if(block[index]==7){
    			if(map[4]==0){return true;} 
    			if(map[8]==0){return true;} 
    			if(map[14]==0){return true;} 
    		}
    		else if(block[index]==8){
    			if(map[4]==0){return true;} 
    			if(map[7]==0){return true;} 
    			if(map[9]==0){return true;} 
    			if(map[14]==0){return true;} 
    		}
    		else if(block[index]==9){
    			if(map[5]==0){return true;} 
    			if(map[8]==0){return true;} 
    			if(map[10]==0){return true;} 
    			if(map[15]==0){return true;} 
    		}
    		else if(block[index]==10){
    			if(map[5]==0){return true;} 
    			if(map[9]==0){return true;} 
    			if(map[11]==0){return true;} 
    			if(map[15]==0){return true;} 
    		}
    		else if(block[index]==11){
    			if(map[5]==0){return true;} 
    			if(map[10]==0){return true;} 
    			if(map[12]==0){return true;} 
    			if(map[15]==0){return true;} 
    		}
    		else if(block[index]==12){
    			if(map[6]==0){return true;} 
    			if(map[11]==0){return true;} 
    			if(map[13]==0){return true;} 
    			if(map[16]==0){return true;} 
    		}
    		else if(block[index]==13){
    			if(map[6]==0){return true;} 
    			if(map[12]==0){return true;} 
    			if(map[16]==0){return true;} 
    		}
    		else if(block[index]==14){
    			if(map[7]==0){return true;} 
    			if(map[8]==0){return true;} 
    			if(map[17]==0){return true;} 
    		}
    		else if(block[index]==15){
    			if(map[9]==0){return true;} 
    			if(map[10]==0){return true;} 
    			if(map[11]==0){return true;} 
    			if(map[18]==0){return true;} 
    		}
    		else if(block[index]==16){
    			if(map[12]==0){return true;} 
    			if(map[13]==0){return true;} 
    			if(map[19]==0){return true;} 
    		}
    		else if(block[index]==17){
    			if(map[14]==0){return true;} 
    			if(map[18]==0){return true;} 
    			if(map[20]==0){return true;} 
    		}
    		else if(block[index]==18){
    			if(map[15]==0){return true;} 
    			if(map[17]==0){return true;} 
    			if(map[19]==0){return true;} 
    			if(map[20]==0){return true;} 
    		}
    		else if(block[index]==19){
    			if(map[16]==0){return true;} 
    			if(map[18]==0){return true;} 
    			if(map[20]==0){return true;}  
    		}
    		else if(block[index]==20){
    			if(map[17]==0){return true;} 
    			if(map[18]==0){return true;} 
    			if(map[19]==0){return true;} 
    		}	              
        }
        return false;
    }
}
