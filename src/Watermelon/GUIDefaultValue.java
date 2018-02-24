package Watermelon;

import java.awt.Point;
/*
��class�O�Ψөw�q�ѽL�P�Ѥl���ѼƥΪ�
*/

import javax.swing.ImageIcon;

public class GUIDefaultValue {
	private Point[] BoardPoint = { //�w�q�ѽL����m
			  new Point(258, 45),//0
			  new Point(160, 70),//1
			  new Point(258, 125),//2
			  new Point(365, 70),//3
			  new Point(10, 220),//4
			  new Point(258, 200),//5
			  new Point(510, 220),//6
			  new Point(-10, 318),//7
			  new Point(60, 318),//8
			  new Point(140, 318),//9
			  new Point(258, 318),//10
			  new Point(375, 318),//11
			  new Point(456, 318),//12
			  new Point(530, 318),//13
			  new Point(10, 420),//14
			  new Point(258, 440),//15
			  new Point(510, 420),//16
			  new Point(160, 575),//17
			  new Point(258, 520),//18
			  new Point(365, 575),//19
			  new Point(258, 590)//20
			};
	
	private String[] ChessPicture = { //�w�q�Ѥl���Ϥ�
			  "White0",//0
			  "White1",//1
			  "White2",//2
			  "White3",//3
			  "White4",//4
			  "White5",//5
			  "Black6",//6
			  "Black7",//7
			  "Black8",//8
			  "Black9",//9
			  "Black10",//10
			  "Black11"//11
	};
	
	ImageIcon background = new ImageIcon(getClass().getResource("./res/chessboard.png"));  //Ū���I���Ϥ�
	private int BackgroundSize_X = background.getIconWidth()+18;//�������j�p(���P�I���Ϥ�)
	private int BackgroundSize_Y = background.getIconHeight()+45;//�������j�p(���P�I���Ϥ�)
	
	public Point[] GetBoardPoint()
	{
		return BoardPoint;
	}
	
	public String[] GetChessPicture()
	{
		return ChessPicture;
	}
	
	public int GetBackgroundSize_X()
	{
		return BackgroundSize_X;
	}
	
	public int GetBackgroundSize_Y()
	{
		return BackgroundSize_Y;
	}
}
