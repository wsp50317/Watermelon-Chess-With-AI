package Watermelon;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
	public static String PlayerMode = "" ; //  PlayerMode = black(�ϥΪ̭n�´�) or white  //�զ����
	public static Node CorrentNode = new Node(); //�ثe�L��
	public static void main(String[] args){
		int[] intB = {1,1,1,1,1,0,1,0,0,0,0,0,0,0,2,0,2,2,2,2,2};//��l�a��
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
    			System.out.println("�o��player mode = " + PlayerMode);
    			break;
    		}
    	}

		//(��)�t�άy�{
		if(PlayerMode == "Black" ){
			while(true){
				CorrentNode = game.GetComputerInput(CorrentNode);				
				if(game.WinOrLose(CorrentNode)=="Win"||game.WinOrLose(CorrentNode)=="Lose")
				{
					game.ShowWinLose(game.WinOrLose(CorrentNode)); //�ǤJ��Ĺ���Ѽƭ�
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
					game.ShowWinLose(game.WinOrLose(CorrentNode)); //�ǤJ��Ĺ���Ѽƭ�
					break;
				}
				CorrentNode = game.GetComputerInput(CorrentNode);
				if(game.WinOrLose(CorrentNode)=="Win"||game.WinOrLose(CorrentNode)=="Lose")
				{
					game.ShowWinLose(game.WinOrLose(CorrentNode)); //�ǤJ��Ĺ���Ѽƭ�
					break;
				}
			}
		}
		
		//if(game.WinOrLose(CorrentNode)=="Win")
		
	}

}
