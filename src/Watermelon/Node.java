package Watermelon;
//(改)負責記錄節點屬性  
public class Node {
	public  int[] board = new int[21];//資料型態為陣列  (改)1為白棋  2為黑棋
	public   int score = -1; //Node 分數
	public Node parent; //記錄父點
	public   int level = 0;

}
