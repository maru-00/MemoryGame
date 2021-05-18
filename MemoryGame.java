package java1_test;

import java.util.Scanner;
import java.util.Random;

public class MemoryGame {
	static int[] cards; //カードの箱
	static int[] status; //カードの状態
	static int[] memory; //めくったカードの記憶
	static int[] markdata; //トランプの柄データ
	static Scanner scan = new Scanner(System.in);
	static int mood; //ゲームのモード
	static int cardsNum; //カードの枚数
	static Random rand = new Random();
	static int level, chance; // computerのレベル
	static boolean turn = true;
	static int mypoint; //プレイヤーの得点
	static int compoint; //computerの得点
	static String mark;
	
	public static void main(String[] args) {
		for (int h = 0;; h++) {
			while (true) {
				System.out.println("モードを選んでね。");
				System.out.println("1:ソロプレイ　2:Computerとバトル");
				int selectmood = scan.nextInt();
				if (selectmood != 1 && selectmood != 2) {
					System.out.println("そのモードはないよ…");
				} else {
					mood = selectmood;
					break;
				}
			}
			while(true) {
				if(mood == 1) {
					break;
				}else {
					System.out.println("レベルを1~10から選んでね");
					level = scan.nextInt();
					if(level < 1 || level > 10) {
						System.out.println("そのレベルの相手がいないよ…");
					}else {
						break;
					}
				}
			}
			while (true) {
				System.out.println("カード枚数を決めてね");
				System.out.println("※枚数は16~52枚までの4の倍数にしてね");
				cardsNum = scan.nextInt();

				if (cardsNum % 4 != 0 || cardsNum < 16) {
					System.out.println("入力された枚数では開始できないよ。");
				} else {
					break;
				}
			}

			System.out.println(cardsNum + "枚の神経衰弱スタート！");
			initCards(cardsNum);
			for (int i = 1;; ++i) {
				System.out.println("ターン：" + i);
				output();

				if (mood == 2 && i % 2 == 0) {
					System.out.println("コンピューターの番だよ。");
					turn = false;
					breaktime();   
					computer();
				} else {
					System.out.println("君の番だよ。");
					turn = true;
					player();
				}
				
				if (Pair()) {
					changeStatus(1, 2);
					System.out.println("ペアができたよ");
					if(mood == 2) {
						if(turn) {
							mypoint++;
						}else{
							compoint++;
						}
					}
					System.out.println();
					breaktime(); 
				} else {
					changeStatus(1, 0);
				}
				if (cardsNum == getStatusNum(2)) {
					break;
				}
			}
			System.out.println("最終結果");
			for(int i = 0; i < cards.length; ++i) {
				status[i] = 1;
			}
			output();
			if(mood == 2) {
				System.out.print(mypoint+" : "+compoint+"で");
				if(mypoint == compoint) {
					System.out.println("引き分け！");
				}else if(mypoint > compoint) {
					System.out.println("君の勝ち！");
				}else {
					System.out.println("Computerの勝ち！");
				}
			}
			break;
		}
	}

	private static void player() {
		int pos1 = 0, pos2 = 0;
		for (int j = 0;; j++) {
			System.out.println("１枚目にめくるカードを選んでね。");
			System.out.println("上から何行目？");
			int row1 = scan.nextInt();
			System.out.println("左から何列目？");
			int col1 = scan.nextInt();
			pos1 = (row1 - 1) * (cardsNum/4) + (col1 - 1);
			if (status[pos1] != 0) {
				System.out.println("既にめくったカードだよ。選びなおそう。");				
			}else {
				break;
			}
		}
		int cardnum1 = openCard(pos1);
		output();

		for (int j = 0;; j++) {
			System.out.println("２枚目にめくるカードを選んでね。");
			System.out.println("上から何行目？");
			int row2 = scan.nextInt();
			System.out.println("左から何列目？");
			int col2 = scan.nextInt();
			pos2 = (row2 - 1) * (cardsNum/4) + (col2 - 1);
			if (pos1 == pos2 || status[pos2] != 0) {
				System.out.println("既にめくったカードだよ。選びなおそう。");
			} else {
				break;
			}
		}
		int cardnum2 = openCard(pos2);
		output();
	}

	private static void computer() {
		//乱数でレベル指定
		chance  = rand.nextInt(10);
		int cardnum1 = openCard();
		System.out.println("1枚目");
		output();
		breaktime();
		if (!openMemoryCard(cardnum1)) {
			int cardnum2 = openCard();
		} else if(chance < level){
			System.out.println("記憶からカードをオープン！");
		}else {
			int cardnum2 = openCard();
		}
		System.out.println("2枚目");
		output();
		breaktime(); 
	}

	private static void initCards(int cardsNum) {//カードの初期化
		cards = new int[cardsNum];
		status = new int[cardsNum];
		memory = new int[cardsNum];
		markdata = new int[cardsNum];
		for (int i = 0; i < cards.length; i++) {
			cards[i] = (i / 4) + 1;
			status[i] = 0; // 伏せられている状態
			memory[i] = 0;
			markdata[i] = i % 4;
		}
		for (int i = 0; i < cards.length; ++i) {
			int rnd = (int) (Math.random() * (double) cards.length);
			int num = cards[i];
			cards[i] = cards[rnd];
			cards[rnd] = num;
			int data = markdata[i];
			markdata[i] = markdata[rnd];
			markdata[rnd] = data;
		}
	}

	private static void output() {//カードの出力
		for (int i = 0; i < cards.length; ++i) {
			switch (status[i]) {
			case 0:
				System.out.print("*   ");
				break;

			case 1:
				if(markdata[i] == 0) {
					mark = "♡";
				}else if(markdata[i] == 1) {
					mark = "♧";
				}else if(markdata[i] == 2) {
					mark = "♢";
				}else {
					mark = "♤";
				}
				
				switch (cards[i]) {
				case 1:
					System.out.print(mark + "A  ");
					break;
				case 10:
					System.out.print(mark + cards[i] + " ");
					break;
				case 11:
					System.out.print(mark + "J  ");
					break;
				case 12:
					System.out.print(mark + "Q  ");
					break;
				case 13:
					System.out.print(mark + "K  ");
					break;
				default:
					System.out.print(mark + cards[i] + "  ");
					break;
				}
				break;
				
			case 2:
				System.out.print("    ");
				break;
			}

			if (((i + 1) % (cards.length / 4)) == 0) {
				System.out.println();
			}
		}
		System.out.println();
	}

	private static int getStatusNum(int st) {//カードの状態取得
		int num = 0;
		for (int i = 0; i < status.length; i++) {
			if (status[i] == st) {
				++num;
			}
		}
		return num;
	}

	private static int openCard() {//computer用
		int hideNum = getStatusNum(0);
		if (1 > hideNum) {
			return 0;
		}
		int rand = (int) (Math.random() * (double) hideNum) + 1;
		int cardnum = 0;
		for (int i = 0; i < status.length; i++) {
			if (status[i] == 0) {
				--rand;
				if (0 == rand) {
					status[i] = 1;// オープン
					cardnum = cards[i];
					memory[i] = cardnum;
					break;
				}
			}
		}
		return cardnum;
	}

	private static int openCard(int pos) {//player用
		int hideNum = getStatusNum(0);
		if (1 > hideNum) {
			return 0;
		}
		int cardnum = 0;
		for (int i = 0; i < status.length; i++) {
			if (status[i] == 0) {
				if (i == pos) {
					status[i] = 1;// オープン
					cardnum = cards[i];
					memory[i] = cardnum;
					break;
				}
			}
		}
		return cardnum;
	}

	private static boolean openMemoryCard(int cardnum) {//めくったカードの記憶
		for (int i = 0; i < status.length; i++) {
			if (status[i] == 0) {
				if (cardnum == memory[i]) {
					if(chance < level) {
						status[i] = 1;// オープン
					}
					return true;
				}
			}
		}
		return false;
	}

	private static boolean Pair() {//ペアの確認
		int num = 0, cardnum = 0;
		for (int i = 0; i < status.length; i++) {
			if (1 == status[i]) {
				++num;
				if (1 == num) {
					cardnum = cards[i];
				} else {
					if (cardnum == cards[i]) {
						return true;
					}
					break;
				}
			}
		}
		return false;
	}

	private static void changeStatus(int oldStatus, int newStatus) {//カードの状態変更
		for (int i = 0; i < status.length; i++) {
			if (oldStatus == status[i]) {
				status[i] = newStatus;
			}
		}
	}
	
	private static void breaktime() {//動作停止
		try {
            Thread.sleep(2000);
        } catch(InterruptedException e){
            e.printStackTrace();
        } 
	}
}
