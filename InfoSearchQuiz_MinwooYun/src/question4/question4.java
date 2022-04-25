package question4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Map.Entry;

import question1.Question1;
import settingfile.SettingFile;

public class question4 {
	private static String URL = SettingFile.URL;
	private static int fileIndex;
//	사용할 상위 스코어 수
	private static int topScore = 5;
	
	public static void main(String[] args) {
//		모든 파일들을 하나의 리스트안에 저장
		List<SettingFile> list = new ArrayList<SettingFile>();
		List<HashMap<String, Integer>> listMap = new ArrayList<HashMap<String, Integer>>();
		HashMap<String, Double> resultDATA = new HashMap<String, Double>();
//		사용자가 지정한 폴더안에 존재하는 모든 파일리스트 반환
		String[] fileName = SettingFile.getFileName(URL);
		for(String name : fileName) {
			list.add(new SettingFile(name, URL));
		}
		
//		사용자에게 알파벳값을 입력받는다.
		Scanner sc = new Scanner(System.in);
		System.out.print("문서 이름을 입력하세요 : ");
		String word = sc.next();
		
//		파일 존재여부 검사
		boolean flag = true;
		for(int i = 0; i < fileName.length; i++) {
			if(word.equals(fileName[i])) {
				flag = false;
				fileIndex = i;
			}
		}
		if(flag) {
			System.out.println("존재하는 파일이 없습니다. \n프로그램을 종료합니다.");
			System.exit(0);
		}
		

		List<TFIDF> result = new ArrayList<TFIDF>();
		for(SettingFile data1 : list) {
			TFIDF tfidf = new TFIDF(data1.getFileName());
			
			for(String src : data1.getFileData()) {
//				TF 값들을 담을 리스트
				List<Float> TFresult = new ArrayList<Float>();
				HashMap<String, Float> mapping = new HashMap<String, Float>();
				
				for(SettingFile data2 : list) {
					float tf = Question1.getTF(data2, src);
					TFresult.add(tf);
					mapping.put(data2.getFileName(), tf);
				}
				
//				IDF 값 저장
				Float IDF = Question1.getIDF(TFresult);
				if(IDF.isInfinite()) { IDF = (float) 0; }
				
				tfidf.putMap(src, mapping.get(data1.getFileName()) * IDF );
//				System.out.print(src + " : " + mapping.get(data1.getFileName()) * IDF + ", ");
			}
//			System.out.println();
			result.add(tfidf);
			tfidf.MapSort();
		}		
		
//		for(TFIDF data : result) {
//			for(Entry<String, Float> elem : data.getLinkedMap().entrySet()){ 
//				System.out.print(elem.getKey() + ":" + elem.getValue() + " ");
//			}
//			System.out.println();
//		}
		
//		기준이 되는 문서의 분모 계산
		double denominator = Denominator(result.get(fileIndex).getLinkedMap());
		

		for(int i = 0; i < result.size(); i++) {
//				이미 앞에서 구한 기준문서는 skip
			if(i == fileIndex) { continue; }		
			
			double denominator2 = Denominator(result.get(i).getLinkedMap());
//				System.out.println(i + " : " + denominator2);
			double numerator = Numerator(result.get(fileIndex).getLinkedMap(), result.get(i).getLinkedMap());
			resultDATA.put(fileName[i], numerator/(denominator*denominator2));
		}
		
		
		for(Entry<String, Double> elem : resultDATA.entrySet()){ 
			System.out.println(elem.getKey() + " : " + elem.getValue());
		}

		sc.close();
	}
	
	/*
	 * 매개변수 : HashMap<String Integer>
	 * 설명 : 분모 계산
	 * 반환 : double
	*/
	public static double Denominator(LinkedHashMap<String, Float> map) {
		double result = 0;
		int cnt = 0;
		
		for(Entry<String, Float> elem : map.entrySet()){ 
			result = result + (elem.getValue() * elem.getValue());
			cnt++;
			if(cnt == topScore) {return Math.sqrt(result);}
		}

		return Math.sqrt(result);
	}

	
	/*
	 * 매개변수 : HashMap<String, Integer>, HashMap<String, Integer>
	 * 설명 : 분자 계산
	 * 반환 : double
	*/
	public static double Numerator(LinkedHashMap<String, Float> map1, LinkedHashMap<String, Float> map2) {
		double result = 0;
		int cnt = 0;
		for(Entry<String, Float> elem : map1.entrySet()){ 
			if(map2.containsKey(elem.getKey())) {
				result = result + (elem.getValue() * map2.get(elem.getKey()));
			}
			cnt++;
			if(cnt == topScore) { return result; }
		}
		
		return result;
	}

}

/*
 * TFIDF값들을 담을 클래스
*/
class TFIDF{
	private String fileName;
	private HashMap<String, Float> wordANDtfIdf;
	private LinkedHashMap<String, Float> sortedLinkedMap = new LinkedHashMap<String, Float>();
	
	public TFIDF(String fileName) {
		this.fileName = fileName;
		this.wordANDtfIdf  = new HashMap<String, Float>();
	}

	public String setFileName() { return this.fileName; }
	public void putMap(String word, Float tfidf) { wordANDtfIdf.put(word, tfidf); }
	public HashMap<String, Float> getMap() { return this.wordANDtfIdf; }
	public LinkedHashMap<String, Float> getLinkedMap() { return this.sortedLinkedMap; }
	
	public void MapSort () {
		sortedLinkedMap = sortedMap(wordANDtfIdf);
	}
	
	/*
	 * 매개변수 : HashMap<String, Integer>
	 * 설명 : HashMap에 저장된 Value (Integer)값들을 내림차순으로 정렬
	 * 매개변수 T는 숫자만 받는다 (Integer, Float, Double)
	 * 반환 : LinkedHashMap<String, Integer>
	*/
	public static LinkedHashMap<String, Float> sortedMap(HashMap<String, Float> map) {
		LinkedHashMap<String, Float> linkedMap = new LinkedHashMap<String, Float>();
		List<String> listValueSet = new ArrayList<String>(map.keySet());
		Collections.sort(listValueSet, (value1, value2) -> (map.get(value2).compareTo(map.get(value1))));
		for(String key : listValueSet) {
			linkedMap.put(key, map.get(key));
        }
		
		return linkedMap;
	}
}
