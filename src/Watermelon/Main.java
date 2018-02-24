package Watermelon;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
	public static String PlayerMode = "" ; //  PlayerMode = black(使用者要黑棋) or white  //白色先攻
	public static Node CorrentNode = new Node(); //目前盤面
	public static void main(String[] args){
		int[] intB = {1,1,1,1,1,0,1,0,0,0,0,0,0,0,2,0,2,2,2,2,2};//初始地圖
		CorrentNode.board = Arrays.copyOf(intB, intB.length);
				
		Game game = new Game();
    	while(true)
    	{
    	
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		if(!PlayerMode.equals("")) 
    		{
    			System.out.println("得到player mode = " + PlayerMode);
    			break;
    		}
    	}

		//(改)系統流程
		if(PlayerMode == "Black" ){
			while(true){
				CorrentNode = game.GetComputerInput(CorrentNode);				
				if(game.WinOrLose(CorrentNode)=="Win"||game.WinOrLose(CorrentNode)=="Lose")
				{
					game.ShowWinLose(game.WinOrLose(CorrentNode)); //傳入輸贏的參數值
					break;
				}
				CorrentNode = game.GetUserInput(CorrentNode); 
				if(game.WinOrLose(CorrentNode)=="Win"||game.WinOrLose(CorrentNode)=="Lose")
				{
					game.ShowWinLose(game.WinOrLose(CorrentNode));
					break;
				}
			}
		}
		if(PlayerMode == "White" ){
			while(true){
				CorrentNode = game.GetUserInput(CorrentNode); 
				if(game.WinOrLose(CorrentNode)=="Win"||game.WinOrLose(CorrentNode)=="Lose")
				{
					game.ShowWinLose(game.WinOrLose(CorrentNode)); //傳入輸贏的參數值
					break;
				}
				CorrentNode = game.GetComputerInput(CorrentNode);
				if(game.WinOrLose(CorrentNode)=="Win"||game.WinOrLose(CorrentNode)=="Lose")
				{
					game.ShowWinLose(game.WinOrLose(CorrentNode)); //傳入輸贏的參數值
					break;
				}
			}
		}
		
		//if(game.WinOrLose(CorrentNode)=="Win")
		
	}

}
