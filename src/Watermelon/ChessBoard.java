package Watermelon;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/*****
API列表:
架構: 
Point BoardPoint[] //棋盤的0~20位，每個位置都用point表示
JLabel ChessLabel[] //棋子的0~11位，每個位置都用Label表示 獲取坐標方法為GetChessPosition_X

MoveChess(int ChessLabelNumber ,double TargetX,double TargetY) //移動棋子到(X,Y)點

GetChessPosition_X(int ChessLabelNumber) //回傳棋子Label的X坐標
GetChessPosition_Y(int ChessLabelNumber) //回傳棋子Label的Y坐標

******/

public class ChessBoard extends MouseAdapter implements Runnable { //繼承至控制滑鼠的Class
	JFrame ButtonFrame = new JFrame();//定義開始主框架	
	JFrame MainFrame = new JFrame();//定義棋盤主框架
	JFrame EndFrame = new JFrame();//定義顯示勝負主框架
	
	GUIDefaultValue Default = new GUIDefaultValue(); //宣告關於參數的class
	
	
	private Point[] BoardPoint =  Default.GetBoardPoint();//定義棋盤的位置
	private String[] ChessPicture = Default.GetChessPicture(); //連接至每個棋子的圖片
	
    JPanel contentPanel = null, imagePanel = null;
    JLabel wordLabel = null;
    JLabel bgLabel = null;
    
    JLabel ChessLabel[] = new JLabel[12]; //主要控制棋子的Label
    int[] ChessPosition = {0,1,2,3,4,6,14,16,17,18,19,20};//記錄棋子在棋盤的哪個位置上，被吃掉則值為-1
        
    ImageIcon background = null;
    
    int[] CurrentBoardArray = {1,1,1,1,1,0,1,0,0,0,0,0,0,0,2,0,2,2,2,2,2};//與後端溝通棋盤用
    String BlackOrWhite = "Non"; //設定玩家為黑色或白色
    boolean RefleshSignal = false; //設定盤面是否有更動，需要更新後端資料
    
	JPanel Mousepanel; //滑鼠控制的背景panel
	boolean MouseLock = false; //設定滑鼠有沒有被禁止點擊
    
    ChessBoard(){    	
    	SelectBlackOrWhite(); //選擇先攻後攻
    	
        // 1.設置frame title及Layout之類型
    	MainFrame.setTitle("人工智慧西瓜棋");
    	MainFrame.setLayout(new BorderLayout());
    	
        imagePanel = (JPanel) MainFrame.getContentPane();// 把内容視窗轉為JPanel，否則不能使用setOpaque()來使視窗變成透明
        imagePanel.setOpaque(false);//使得圖片能顯示在視窗後
        
        
        SetBackgroundImage(); //設定背景圖片
        
        InitializeChessImage();
                
        //設置frame之基本設定
        MainFrame.setMinimumSize(new java.awt.Dimension(Default.GetBackgroundSize_X(),Default.GetBackgroundSize_Y()));
        MainFrame.setLocationRelativeTo(null);
        MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void run() { // implements Runnable run()
    	while(true)
    	{
	    	MainFrame.repaint();
	    	//System.out.println("執行");
    	}
    }
    
    public void SetBackgroundImage() //設定背景圖片
    {
    	background = new ImageIcon(getClass().getResource("./res/chessboard.png"));       // 背景圖片
        bgLabel = new JLabel(background);      // 把背景圖顯示在Label中
        bgLabel.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());    // 把含有背景圖之Label位置設置為圖片剛好填充整個版面
        
        //imagePanel = (JPanel) this.getContentPane();// 把内容視窗轉為JPanel，否則不能使用setOpaque()來使視窗變成透明
        //imagePanel.setOpaque(false);
        MainFrame.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));     // 把背景圖添加到分層窗格的最底層以作為背景
    }
    
    public void InitializeChessImage() //設置一開始的棋子位置
    {
    	//ChessPosition已在上面被初始化過
    	
    	//0~5號為AI 6~11為player 
    	//0~4號棋
    	for(int i=0 ; i<5 ; i++)//0~4號棋放0~4號位
    	{
    		SetChessOnBoard(i,i);
    	}
    	
    	//5 放6號位
    	SetChessOnBoard(5,6);
	    
	    //6 放14號位
    	SetChessOnBoard(6,14);
	    
	    //7~11 放16~20位
    	for(int i=7 ; i<12 ; i++)
    	{
    		SetChessOnBoard(i,i+9);
    	}
    }
    
    
    /* 此版MoveChess為動畫移動版，但是不知道為什麼副程式呼叫只能瞬間移動，只能在main使用
    public void MoveChess(int ChessLabelNumber ,double TargetX,double TargetY) //直線移動棋子
    {
    	int MoveTimes =50;//要讓棋子小位移幾次
        double NowX = GetChessPosition_X(ChessLabelNumber);//取得目前棋子位置
        double NowY = GetChessPosition_Y(ChessLabelNumber);
        
        ///計算棋子每次的位移大小///
        double Displace_X = (TargetX - NowX)/MoveTimes;
        double Displace_Y = (TargetY - NowY)/MoveTimes;       
        
        ImageIcon NowChessImage =(ImageIcon) ChessLabel[ChessLabelNumber].getIcon(); //取得棋子的圖示
        
        //this.getLayeredPane().remove(ChessLabel[ChessLabelNumber]);//移除要移動的物件
    	for(int i=0; i<MoveTimes ;i++)
    	{
    		MainFrame.getLayeredPane().remove(ChessLabel[ChessLabelNumber]); //把舊的棋子圖移除
	        
	        ///計算棋子每次的位移大小///
	        NowX = NowX + Displace_X; //下一個到的位置
	        NowY = NowY + Displace_Y; //下一個到的位置
	        System.out.println(NowX);
	        System.out.println(NowY);
	    
	        ChessLabel[ChessLabelNumber] = new JLabel(NowChessImage);      // 把棋子圖顯示在Label中
	        ChessLabel[ChessLabelNumber].setBounds((int)NowX, (int)NowY, NowChessImage.getIconWidth(), NowChessImage.getIconHeight()); // 把含有棋子圖之Label位置設置為圖片剛好填充整個版面
	        
	        MainFrame.getLayeredPane().add(ChessLabel[ChessLabelNumber], new Integer(Integer.MIN_VALUE+1));     // 把背景圖添加到分層窗格的最底層以作為背景

	        //MainFrame.repaint(); //如不使用會有殘影  
	        
	        try {
	            Thread.sleep(50);//移動後暫停某一秒數，控制移動速度
	        }
	            catch(InterruptedException e) {
	        }     
    	}
    	
    }
    */
    
    public void MoveChess(int ChessNumber ,int BoardNumber) //直線移動棋子
    {                
    	RemoveChessOnBoard(ChessNumber);
    	SetChessOnBoard(ChessNumber,BoardNumber); //新增新的棋子到棋盤
    }
    
    public void RemoveChessOnBoard(int ChessNumber)
    {
    	MainFrame.getLayeredPane().remove(ChessLabel[ChessNumber]); //把舊的棋子圖移除
    	int RemovedChessPosition = ChessPosition[ChessNumber] ;
    	CurrentBoardArray[RemovedChessPosition] = 0; 
    	ChessPosition[ChessNumber] = -1; //移除棋子的位置資訊
    }
    
    public void SetChessOnBoard(int ChessNumber,int BoardNumber) //新增新的棋子到棋盤
    {
    	String Index;
    	ImageIcon ChessImage;
    	
    	Index = "./res/" + ChessPicture[ChessNumber]  + ".png"; //設製圖片路徑
    	ChessImage = new ImageIcon(getClass().getResource(Index));// 設置棋子圖片
        ChessLabel[ChessNumber] = new JLabel(ChessImage);      // 把背景圖顯示在Label中
	    ChessLabel[ChessNumber].setBounds(BoardPoint[BoardNumber].x, BoardPoint[BoardNumber].y, ChessImage.getIconWidth(), ChessImage.getIconHeight());    // 設置棋子圖之Label位置
	    MainFrame.getLayeredPane().add(ChessLabel[ChessNumber], new Integer(Integer.MIN_VALUE+1));     // 把背景圖添加到分層窗格的最底層以作為背景
	    
	    ChessPosition[ChessNumber] = BoardNumber; //新增棋子的位置資訊
	    if(ChessNumber>5) //黑棋
	    {
	    	CurrentBoardArray[BoardNumber] = 2;
	    }
	    else //白棋
	    {
	    	CurrentBoardArray[BoardNumber] = 1;
	    }
    }
    
    public Point GetBoardPoint(int BoardNumber) //得到格子的位置 參數為編號
    {
		return BoardPoint[BoardNumber];
    }
    
    public double GetChessPosition_X(int ChessLabelNumber)//取得目前棋子的圖片位置
    {
    	if(ChessPosition[ChessLabelNumber]== -1) //假如棋子已經被吃了
    	{
    		return -1;
    	}
    	else
    	{
	        double NowX = BoardPoint[ChessPosition[ChessLabelNumber]].x;
	        return NowX;
    	}
    }
    
    public double GetChessPosition_Y(int ChessLabelNumber)//取得目前棋子的圖片位置
    {
    	if(ChessPosition[ChessLabelNumber]== -1) //假如棋子已經被吃了
    	{
    		return -1;
    	}
    	else
    	{
	        double NowY = BoardPoint[ChessPosition[ChessLabelNumber]].y;
	        return NowY;
    	}
    }
    
    /////////////////////設定開始按鈕及結束的方法/////////////////////
    private void SelectBlackOrWhite()
    {
    	//設置frame之基本設定
    	ButtonFrame.setTitle("人工智慧 - 西瓜棋");
    	ButtonFrame.setLayout(new BorderLayout());
    	ButtonFrame.setMinimumSize(new java.awt.Dimension(300,80));
    	ButtonFrame.setLocationRelativeTo(null);
    	ButtonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	ButtonFrame.setVisible(true);
        
    	JButton Black = new JButton("AI為黑色(先攻)");
    	JButton White = new JButton("AI為白色(後攻)");
    	
    	JPanel panel=new JPanel();
    	panel.add(Black);
    	panel.add(White);
    	ButtonFrame.add(panel);

    	Black.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) 
    	    {
    	        // 按下按鈕之後執行的動作
    	    	System.out.println("選擇AI黑色(先攻)");
    	    	BlackOrWhite = "Black";
    	    	Main.PlayerMode = "Black";
    	        MainFrame.setVisible(true);
    	        IntillizeMouseControl();
    	    	ButtonFrame.setVisible(false);
    	    	MouseLock = true; //一開始先鎖住滑鼠
    	    	MainFrame.setTitle("人工智慧西瓜棋 - AI思考中");
    	    }
    	});
    	
    	White.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) 
    	    {
    	        // 按下按鈕之後執行的動作
    	    	System.out.println("選擇AI白色(後攻)");
    	    	BlackOrWhite = "White";
    	    	Main.PlayerMode = "White";
    	        MainFrame.setVisible(true);
    	        IntillizeMouseControl();
    	    	ButtonFrame.setVisible(false);
    	    	MouseLock = false; //一開始不鎖住滑鼠
    	    	MainFrame.setTitle("人工智慧西瓜棋 - 玩家回合");
    	    }
    	});
    }
    public String GetBlackOrWhite()
    {
    	ButtonFrame.setTitle("人工智慧 - 西瓜棋");
    	ButtonFrame.setLayout(new BorderLayout());
    	ButtonFrame.setMinimumSize(new java.awt.Dimension(300,80));
    	ButtonFrame.setLocationRelativeTo(null);
    	ButtonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	ButtonFrame.setVisible(true);
    	
    	return BlackOrWhite;
    }
    
    public void ShowWinORLose(String state)
    {
    	if(state == "Win") //AI勝
    	{	
    		MainFrame.setTitle("人工智慧 - 西瓜棋 - AI勝利");
    		MouseLock = true;
    	}
    	else if(state == "Lose") //AI輸
    	{
    		MainFrame.setTitle("人工智慧 - 西瓜棋 - 玩家勝利");
    		MouseLock = true;
    	}
    }
    /////////////////////////////////////////////////////////
    
    ////////////////設定滑鼠控制的選項//////////////////////////////
    private void IntillizeMouseControl(){
    	MainFrame.addWindowListener(new Adapter());
    	MainFrame.setSize(Default.GetBackgroundSize_X(),Default.GetBackgroundSize_Y()); //設定監聽滑鼠的尺寸

        Mousepanel = new JPanel();
        Mousepanel.addMouseListener(this);
        Mousepanel.addMouseWheelListener(this);
        Mousepanel.addMouseMotionListener(this);
        
        Mousepanel.setOpaque(false);//使得控制的panel物件透明化
        
        MainFrame.add(Mousepanel, BorderLayout.CENTER);
        MainFrame.setVisible(true);
    }
    
    JLabel LockLabel;
    public void SetLockPicture(double x,double y) //設置鎖定圖示
    {
    	String Index = "./res/" + "ChooseLock"  + ".png"; //設製圖片路徑
    	ImageIcon ChessImage = new ImageIcon(getClass().getResource(Index));// 設置棋子圖片
    	LockLabel = new JLabel(ChessImage);      // 把背景圖顯示在Label中
    	LockLabel.setBounds((int)x, (int)y, ChessImage.getIconWidth(), ChessImage.getIconHeight());    // 設置棋子圖之Label位置
	    MainFrame.getLayeredPane().add(LockLabel, new Integer(Integer.MIN_VALUE+2));    
    }
    
    public void RemoveLockPicture() //移除現有鎖定圖示
    {
    	MainFrame.getLayeredPane().remove(LockLabel); //取消上一個鎖定圖示
    }
    
    int FirstChooseChess = -1;
    int SecondChooseChess = -1;
    public void mouseClicked(MouseEvent e) {
        int ClickPoint_X = e.getX();
        int ClickPoint_Y = e.getY();
        int ClickChess = -1; //點到的棋子編號
        int ClickBoardPoint = -1; //點到的版子編號
        
        if(MouseLock == false) //假如滑鼠點擊沒有被鎖住
        {
        
	        for(int i=0 ;i<12;i++)
	        {
	        	if(GetChessPosition_X(i) != -1) //跳過被吃子的判斷
	        	{
		        	if(GetChessPosition_X(i)-10<ClickPoint_X   &&     //判斷點擊點有沒有落在棋子上
		        		ClickPoint_X < GetChessPosition_X(i)+70 &&
		        		 GetChessPosition_Y(i)-10<ClickPoint_Y   &&
		        		  ClickPoint_Y < GetChessPosition_Y(i)+70)
		        	{
		        		ClickChess = i ;
		        		break;
		        	}
	        	}
	        }
	        
	        for(int i=0 ;i<21;i++)
	        {
	        	if(BoardPoint[i].x-10<ClickPoint_X   &&     //判斷點擊點有沒有落在棋盤格子上
	        		ClickPoint_X < BoardPoint[i].x+70 &&
	        		BoardPoint[i].y-10<ClickPoint_Y   &&
	        		  ClickPoint_Y < BoardPoint[i].y+70)
	        	{
	        		ClickBoardPoint = i ;
	        		System.out.println("點到棋盤"+i);
	        		break;
	        	}
	        }
	   ///////////////////////////////////////////////////////////////////////////
	        
	////////////////////////*盤面判斷*/////////////////////////////////////////////
	        if(ClickChess != -1) //假如有點到棋子
	        {
	            if(FirstChooseChess == -1) //假如沒有第一個點的記錄
	            {
	            	FirstChooseChess = ClickChess;
	            	String string = "滑鼠點到第一個點的位置:(" + ClickPoint_X + "," + ClickPoint_Y +")";  
	            	System.out.println(string);
	            	SetLockPicture( GetChessPosition_X(ClickChess), GetChessPosition_Y(ClickChess) ); //設置鎖定圖示
	            }
	            else //有第一個點的記錄
	            {
	                if (ClickChess != -1) //第二次點到棋子
	                {
	                	FirstChooseChess = ClickChess; //更換棋子選擇
	                	String string = "滑鼠點到第二個點的位置，更換棋子選擇:(" + ClickPoint_X + "," + ClickPoint_Y +")";  
	                	System.out.println(string);
	                	RemoveLockPicture(); //取消上一個鎖定圖示
	                	SetLockPicture( GetChessPosition_X(ClickChess), GetChessPosition_Y(ClickChess) );
	                }
	                else if (ClickChess == -1 && ClickBoardPoint == -1) //點到其他地方
	                {
	                	FirstChooseChess = -1 ;
	                	String string = "滑鼠點到其他位置，取消所有選擇:(" + ClickPoint_X + "," + ClickPoint_Y +")";  
	                	System.out.println(string);
	                }
	            }
	        }
	        else //沒點到棋子
	        {
	            if(ClickBoardPoint != -1 &&ClickChess == -1 && FirstChooseChess != -1) //假如第二次有點到棋盤位置，上面沒有棋子且有點擊記錄
	            {
	            	SecondChooseChess = ClickChess;	
	            	String string = "滑鼠點到第二個點的位置，更換位置:(" + ClickPoint_X + "," + ClickPoint_Y +")";  
	            	System.out.println(string);
	            	
	            	/////////換位////////////
	            	MoveChess(FirstChooseChess,ClickBoardPoint);
	            	RefleshSignal = true; //使用者輸入後更新後端資料
	            	MouseLock = true; //使用者下子後暫停滑鼠事件，直到電腦下完
	            	MainFrame.setTitle("人工智慧西瓜棋 - AI思考中");
	            	/////////////////////////
	            	
	            	FirstChooseChess = -1;
	            	SecondChooseChess = -1;
	            	RemoveLockPicture(); //取消上一個鎖定圖示
	            }
	            else if(FirstChooseChess != -1) //假如有第一個點的記錄但點到地板
	            {
	            	FirstChooseChess = ClickChess;
	            	RemoveLockPicture(); //取消上一個鎖定圖示
	            	String string = "滑鼠點到其他位置，取消第一個點:(" + ClickPoint_X + "," + ClickPoint_Y +")";  
	            	System.out.println(string);
	            }
	            else //沒有記錄第一點
	            {
		        	FirstChooseChess = -1 ;
		        	String string = "滑鼠點到其他位置，且無記錄:(" + ClickPoint_X + "," + ClickPoint_Y +")";  
		        	System.out.println(string);
	            }
	        }      
        } //MouseLock 
    }
    ///////////////////////////////////////////////////////////////////////
    
    ////////////////以下是設定傳給後端api會用到的程式///////////////////////
    public void RefleshBoardArray()
    {
    	int[] BoardArray = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};//初始地圖
    	
    	for(int i = 0 ; i<12 ; i++) //製造BoardArray
    	{
			double ChessPosition_X = GetChessPosition_X(i); //第i個棋子的位置
			double ChessPosition_Y = GetChessPosition_Y(i);
    		for(int j = 0 ; j<21 ; j++)
    		{
    			if (ChessPosition_X == BoardPoint[j].x && ChessPosition_Y == BoardPoint[j].y) //判斷棋子在棋盤的哪個位置
    			{
    				if(i<6) //玩家的棋子
    				{
    					BoardArray[j] = 1;
    				}
    				else //AI的棋子
    				{
    					BoardArray[j] = 2;
    				}
    				break;
    			}
    		}
    	}
        CurrentBoardArray = BoardArray;
    }
    
    public int[] GetBoardArray()//傳給後端現在的
    {    	
    	return CurrentBoardArray;
    	
    }
    
    public void PlayerEatBoardArray(int[] BoardArrayFromBack) //吃子後呼叫此含式
    {
    	int Minor;
    	
    	int EatChessPosition = -1;
    	int MoveChessNumber = -1;

    	for(int i=0 ; i<21 ; i++) //找出新的跟舊的差在哪裡
    	{
    		Minor = CurrentBoardArray[i] - BoardArrayFromBack[i]; 
        	if(Minor == 1 || Minor == 2) //黑棋或白棋被吃掉
        	{
        		EatChessPosition = i;
        		System.out.println(EatChessPosition+"是被吃的地點");
        		for(int j=0 ; j<12 ; j++) //去找這個點擺的是什麼棋子
        		{
        			if(ChessPosition[j] == EatChessPosition) //找出棋子位置等於棋子消失的位置
        			{
        				MoveChessNumber = j;//找出被吃的棋子編號
        				System.out.println(MoveChessNumber+"是被吃的棋子編號");
        			}
        		}
       		
        		//拿掉前端棋子//
        		RemoveChessOnBoard(MoveChessNumber);
        		
        	}
    	}

    }
    
    public void ComputerSetBoardArray(int[] BoardArrayFromBack)
    {
    	int Minor;
    	
    	int MoveChessOldPosition = -1;
    	int MoveChessNumber = -1;
    	int MoveChessNewPosition = -1;
    	
    	int EatChessPosition = -1;
    	int EatChessNumber = -1;
    	
    	/*
    	System.out.print("CurrentBoardArray(前端) : ");
		for(int j=0;j<21;j++){
			System.out.print(CurrentBoardArray[j]+",");
		}
		System.out.println();
		
    	System.out.print("BoardArrayFromBack(後端) : ");
		for(int j=0;j<21;j++){
			System.out.print(BoardArrayFromBack[j]+",");
		}
	    System.out.println();
	    */
	    
    	for(int i=0 ; i<21 ; i++) //找出新的跟舊的差在哪裡
    	{
    		Minor = CurrentBoardArray[i] - BoardArrayFromBack[i]; 
    		if(BlackOrWhite=="White") //AI是白色棋子 //找1變0 0變1的點
    		{
        		if(Minor == -1) //0變1 代表電腦下的點
        		{
        			MoveChessNewPosition = i; //移除棋子的位置
        		}
        		else if(Minor == 1) //1變0 代表電腦拿開的點
        		{
        			MoveChessOldPosition = i;
        			for(int j=0 ; j<12 ; j++) //去找這個點擺的是什麼棋子
        			{
        				if(ChessPosition[j] == MoveChessOldPosition) //找出棋子位置等於棋子消失的位置
        				{
        					MoveChessNumber = j;//找出移動的棋子編號
        				}
        			}
        		}
        		else if(Minor == 2) //2變0 代表電腦吃掉的子
        		{
        			EatChessPosition = i; //被吃掉的位置
        			for(int j=0 ; j<12 ; j++) //去找這個點擺的是什麼棋子
        			{
        				if(ChessPosition[j] == EatChessPosition) //找出棋子位置等於棋子被吃的位置
        				{
        					EatChessNumber = j;//找出被吃的的棋子編號
        				}
        			}
        			RemoveChessOnBoard(EatChessNumber); //移除被吃的棋子
        		}
    		}
    		else if(BlackOrWhite=="Black")	//AI是黑色棋子 //找2變0 0變2的點
    		{
        		if(Minor == -2) //0變2 代表電腦下的點
        		{
        			MoveChessNewPosition = i; //移除棋子的位置
        		}
        		else if(Minor == 2) //2變0 代表電腦拿開的點
        		{
        			MoveChessOldPosition = i;
        			for(int j=0 ; j<12 ; j++) //去找這個點擺的是什麼棋子
        			{
        				if(ChessPosition[j] == MoveChessOldPosition) //找出棋子位置等於棋子消失的位置
        				{
        					MoveChessNumber = j;//找出移動的棋子編號
        				}
        			}
        		}
        		else if(Minor == 1) //1變0 代表電腦吃掉的子
        		{
        			EatChessPosition = i; //被吃掉的位置
        			for(int j=0 ; j<12 ; j++) //去找這個點擺的是什麼棋子
        			{
        				if(ChessPosition[j] == EatChessPosition) //找出棋子位置等於棋子被吃的位置
        				{
        					EatChessNumber = j;//找出被吃的的棋子編號
        				}
        			}
        			RemoveChessOnBoard(EatChessNumber); //移除被吃的棋子
        		}
    		}
    	}
    	
    	//找出移動的兩個點後
		System.out.println("電腦移動的棋子為"+MoveChessNumber+"號");
		System.out.println("移動到棋盤"+MoveChessNewPosition);
		
		//////前端移動棋盤/////
		SetLockPicture(BoardPoint[MoveChessOldPosition].x,BoardPoint[MoveChessOldPosition].y);//設定電腦移動前鎖定圖示
		
        try {//鎖定後暫停某一秒數
            Thread.sleep(500);
        }
            catch(InterruptedException e) {
        }     
		
		RemoveLockPicture();//移除鎖定圖示
		MoveChess(MoveChessNumber,MoveChessNewPosition);
    	CurrentBoardArray = BoardArrayFromBack;
    	MouseLock = false;//電腦下子後解鎖滑鼠事件，直到使用者下完
    	MainFrame.setTitle("人工智慧西瓜棋 - 玩家回合");
    }
    
    public void SetRefleshSignal(boolean tf) //設定盤面是否有更動，需要更新後端資料
    {
    	RefleshSignal =tf;
    }
    
    public boolean GetRefleshSignal() //設定盤面是否有更動，需要更新後端資料
    {
    	return RefleshSignal;
    }
    //////////////////////////////////////////////////////////
}

class Adapter extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
}
 