package connectEngine;
import swingConnect.*;
import java.util.Scanner;

public class mainEngine {
	static LList list = new LList();
	public static int[][] grid = new int[6][7];
	public static int currenty = 0;

	public static boolean checkM(int[][] val) {
		for(int i = 0; i< val.length; i++) {
			for(int j = 0; j< val[0].length; j++) {
				list.newNode(val[i][j]);
			}
			if(list.checkWin()) {
				list.destroyLink();
				return true;
			}
			list.destroyLink();
		}
		return false;
	}
	public static boolean fullRow(int[][] val) {
		for(int j = 0; j < val[0].length; j++) {
			if(val[0][j] == 0) {
				return false;
			}
		}
		return true;
	}
	
	public static void printM(int[][] val) {
		for(int i = 0; i< val.length; i++) {
			for(int j = 0; j< val[0].length; j++) {
				System.out.print(" " + val[i][j]);
			}
			System.out.println();
		}
	}
	
	public static int[][] transP(int[][] val) {
		int[][] transpose = new int[val[0].length][val.length];
		for(int i = 0; i< val.length; i++) {
			for(int j = 0; j< val[0].length; j++) {
				transpose[j][i] = val[i][j];
			}
		}
		return transpose;
	}

	public static boolean diagP(int[][] val) {
//		LList list = new LList();
		for (int i = val.length-1; i >= 0; i--) {
			for(int m = i, j =0; m >= 0 && j < val[0].length; m--, j++) {
//				System.out.print(" " + val[m][j]);
				list.newNode(val[m][j]);
			}
			if(list.checkWin()) {
				list.destroyLink();
				return true;
			}
			list.destroyLink();
		}
		
		for (int j = val[0].length-1; j >= 1; j--) {
			for(int m = j, i = val.length-1; m <=val[0].length-1 && i >=0; m++, i--) {
//				System.out.print(" " + val[i][m]);
				list.newNode(val[i][m]);
			}
			if(list.checkWin()) {
				list.destroyLink();
				return true;
			}
			list.destroyLink();
		}
		return false;
	}
	
	public static boolean invDiagP(int[][] val) {
//		LList list = new LList();
		for (int i = val.length-1; i >=0; i--) {
			for(int m = i, j = val[0].length-1; j>=0 && m>=0; j--, m--) {
//				System.out.print(" " + val[m][j]);
				list.newNode(val[m][j]);
			}
			if(list.checkWin()) {
				list.destroyLink();
				return true;
			}
			list.destroyLink();
		}
		for (int j = val[0].length-2; j>=0; j--) {
			for (int m = j, i = val.length-1; m>=0 && i>=0; m--,i--) {
//				System.out.print(" " + val[i][m]);
				list.newNode(val[i][m]);
			}
			if(list.checkWin()) {
				list.destroyLink();
				return true;
			}
			list.destroyLink();
		}
		return false;
	}
	
	public static int alternateUser(int playerNum) {
		if(playerNum == 1) 
			return 2;
		else
			return 1;
	}
	
	public static int[] placePiece(int userinp, int[][] val, int user) {
		if(0 > userinp || userinp >= val[0].length || val[0][userinp] != 0) {
			int[] lost = {user,-1};
			return lost;
		}
		else {
			for (int i = val.length-1, j = userinp; i >= 0; i--) {
				if (val[i][j] == 0) {
					val[i][j] = user;
					int[] lost = {alternateUser(user),i};
					return lost;
				}
			}
		}
		return null;
	}
	public static boolean conditions(int[][] val) {
		boolean[] checker = {checkM(transP(val)),invDiagP(val),diagP(val),fullRow(val),checkM(val)};
		for(int i = 0;i < checker.length; i++) {
			if(checker[i] == true) {
				return false;
			}
		}
		return true;
	}
	static Scanner userInput = new Scanner(System.in);
	
	public static void gameState(int[][] val, int player) {
		while(conditions(val)) {
			System.out.println("Please choose a column for player: "+ player);
//			player = placePiece(2, val, player);
			printM(val);
		}
	}
	

}