package question5;

import java.util.Scanner;
import question5.Q1;

public class Question5 {
	
	private static boolean flag = true;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(flag) {
			System.out.print("입력하세요 : ");
			String input = sc.nextLine();
			String number = input.substring(0,1);
			
			switch(number) {
				case "1":
					Q1.StartQ1(input.substring(2));
					break;
				case "2":
					String word2 = input.substring(2);
					break;
				case "3":
					String word3 = input.substring(2);
					break;
				case "4":
					System.out.println();
					break;
				case "5":
					System.out.println("시스템을 종료합니다.");
					System.exit(0);
			}
			sc.nextLine();
			
		}

	}

}
