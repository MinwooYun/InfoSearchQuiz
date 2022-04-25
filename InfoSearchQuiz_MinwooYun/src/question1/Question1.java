package question1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import settingfile.SettingFile;

public class Question1 {
	private static String URL = SettingFile.URL;

	public static void main(String[] args) {
//		사용자에게 알파벳값을 입력받는다.
		Scanner sc = new Scanner(System.in);
		System.out.print("알파벳을 입력하세요 : ");
		String word = sc.next();
		if(!SettingFile.getType(word)) {
			System.out.println("err : 한글/숫자가 입력되었습니다.");
			System.exit(0);
		}
		
		progress(word);

		sc.close();
	}
	
	public static void progress(String word) {
//		모든 파일들을 하나의 리스트안에 저장
		List<SettingFile> list = new ArrayList<SettingFile>();
//		사용자가 지정한 폴더안에 존재하는 모든 파일리스트 반환
		String[] fileName = SettingFile.getFileName(URL);
		for(String name : fileName) {
			list.add(new SettingFile(name, URL));
		}
		
//		TF 값들을 담을 리스트
		List<Float> TFresult = new ArrayList<Float>();
		
		
//		모든 문서의 TF값을 배열에 저장
		list.stream()
			.forEach(w -> TFresult.add(getTF(w, word)));
		
//		IDF 값 저장
		Float IDF = getIDF(TFresult);
		if(IDF.isInfinite()) { IDF = (float) 0; }
	
//		결과값 출력
		for(int i = 0; i < fileName.length; i++) {
			System.out.println(fileName[i] + " : " + TFresult.get(i) * IDF);
		}
	}
	
	/*
	 * 매개변수 : SettingFile, String
	 * 설명 : TF값 계산 (문서 내용, 사용자가 입력한 단어) 
	 * 수식 : 문서내 단어수 / 문서내 모든 단어수
	 * 반환 : float
	*/
	public static float getTF(SettingFile data, String word) {	
//		문서의 모든 단어수
		float allCount = data.getFileData().length;
//		사용자가 입력한 단어의 수
		float wordCount = (int) Arrays.asList(data.getFileData()).stream()
																 .filter(w -> w.equals(word))
																 .count();
		if(wordCount == 0) {return 0;}
		return wordCount/allCount;
	}
	
	/*
	 * 매개변수 : List<Float>
	 * 설명 : IDF값 계산 (매개변수는 TF값이 저장된 list)
	 * 수식 : log(1 + (문서 전체수 / 단어를 포함한 문서수)
	 * 반환 : float
	*/
	public static float getIDF(List<Float> TFlist) {
		
//		TF값이 0이면 단어가 없다는 뜻이므로 +1을 안한다.
		float countDoc = 0;
		for(Float list : TFlist) {
			if(list > 0) { ++countDoc; }
		}
		return (float) Math.log(1 + (float)TFlist.size()/countDoc);
	}

}

