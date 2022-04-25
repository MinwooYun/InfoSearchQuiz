package question5;

import java.util.Scanner;

import question1.Question1;
import question2.Question2;
import question3.Question3;
import question4.Question4;

public class Question5 {
	
	private static boolean flag = true;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(flag) {
			
			System.out.println("1. TF-IDF | 2. Word Co-occurrence Matrix | 3. Cosine Similarity | 4. 1번, 3번 응용 | 5. 종료");
			System.out.println("입력 방법 : 1 (단어) | 2 (단어) | 3 문서이름 | 4 경로 | 5");
			System.out.print("입력하세요 : ");

			String input = sc.nextLine();
			String number = input.substring(0,1);
			
			switch(number) {
				case "1":
					Question1.progress(input.substring(2));
					System.out.println("\n");
					break;
				case "2":
					Question2.progress(input.substring(2));
					System.out.println("\n");
					break;
				case "3":
					Question3.progress(input.substring(2));
					System.out.println("\n");
					break;
				case "4":
					Question4.progress(input.substring(2));
					System.out.println("\n");
					break;
				case "5":
					System.out.println("시스템을 종료합니다.");
					System.exit(0);
				default:
					System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
					
			}
			
		}
		
		sc.close();
	}

}
