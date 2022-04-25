package question5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import settingfile.SettingFile;

public class AllQuestion {
	
	private static String URL = SettingFile.URL;
	
	private static int size = 2;
	private static HashMap<String, Integer> resultHashMap = new HashMap<String, Integer>();
	
	private static int fileIndex;
	private static double denominator;
	
	public static void StartQ1(String input) {
//		모든 파일들을 하나의 리스트안에 저장
		List<SettingFile> list = new ArrayList<SettingFile>();
//		사용자가 지정한 폴더안에 존재하는 모든 파일리스트 반환
		String[] fileName = SettingFile.getFileName(URL);
		for(String name : fileName) {
			list.add(new SettingFile(name, URL));
		}
		
		String word = input.toUpperCase();
		
		if(word.length() > 1 || !SettingFile.getType(word)) {
			System.out.println("err : 1단어 이상 또는 한글/숫자가 입력되었습니다.");
			System.exit(0);
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
	
	public static void StartQ2(String input) {
//		모든 파일들을 하나의 리스트안에 저장
		List<SettingFile> list = new ArrayList<SettingFile>();
//		사용자가 지정한 폴더안에 존재하는 모든 파일리스트 반환
		String[] fileName = SettingFile.getFileName(URL);
		for(String name : fileName) {
			list.add(new SettingFile(name, URL));
		}
		
		String word = input.toUpperCase();
		
		if(word.length() > 1 || !SettingFile.getType(word)) {
			System.out.println("err : 1단어 이상 또는 한글/숫자가 입력되었습니다.");
			System.exit(0);
		}
		
		
		for(SettingFile data : list) {
//			가중치 값만큼 인덱스 값들을 구한다.
			List<Integer> arrIndex = getArrayIndex(data, word);
//			arrIndex의 값들을 HashMap에 삽입하면서 카운트
			getMapCnt(data, arrIndex);
//			System.out.println(data.getFileName());
		}	
		LinkedHashMap<String, Integer> result =  sortedMap(resultHashMap);
		System.out.print("모든 문서 = ");
		result.entrySet().stream().forEach(e -> System.out.print(e.getKey() + ":" + e.getValue() + " "));
	}
	
	public static void StartQ3(String input) {
//		모든 파일들을 하나의 리스트안에 저장
		List<SettingFile> list = new ArrayList<SettingFile>();
		List<HashMap<String, Integer>> listMap = new ArrayList<HashMap<String, Integer>>();
		HashMap<String, Double> result = new HashMap<String, Double>();
//		사용자가 지정한 폴더안에 존재하는 모든 파일리스트 반환
		String[] fileName = SettingFile.getFileName(URL);
		for(String name : fileName) {
			list.add(new SettingFile(name, URL));
		}
		
		String word = input;
		
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

//		파일별 모든 문자들을 hashMap 형태로 변환
		for(SettingFile data : list) {
			listMap.add(getMapCnt(data));
		}
		
		denominator = Denominator(listMap.get(fileIndex));
//		System.out.println("standard : " + denominator);
		for(int i = 0; i < listMap.size(); i++) {
			if(i == fileIndex) { continue; }
			
			double denominator2 = Denominator(listMap.get(i));
//			System.out.println(i + " : " + denominator2);
			double numerator = Numerator(listMap.get(fileIndex), listMap.get(i));
			
			result.put(fileName[i], numerator/(denominator*denominator2));
		}
		
		System.out.println(fileName[fileIndex] + " -> ");
		sortedMap2(result);	
	}
	//end question3
//	end main
	
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
//	end question1
	
	/*
	 * 매개변수 : HashMap<String, Integer>
	 * 설명 : HashMap에 저장된 Value (Integer)값들을 내림차순으로 정렬
	 * 반환 : LinkedHashMap<String, Integer>
	*/
	public static LinkedHashMap<String, Integer> sortedMap(HashMap<String, Integer> map) {
		LinkedHashMap<String, Integer> linkedMap = new LinkedHashMap<String, Integer>();
		List<String> listValueSet = new ArrayList<String>(map.keySet());
		Collections.sort(listValueSet, (value1, value2) -> (map.get(value2).compareTo(map.get(value1))));
		for(String key : listValueSet) {
			linkedMap.put(key, map.get(key));
        }
		
		return linkedMap;
	}
	
	/*
	 * 매개변수 : SettingFile, List<Integer>
	 * 설명 : List안에는 검색한 단어의 가중치만큼의 단어들의 Index번호들이 들어가 있다.
	 * 이 Index값들에 해당하는 값들을 SettingFile에서 얻는다.
	 * 얻은 값들을 HashMap에 삽입하면서 등장한 횟수를 카운트한다.
	 * 반환 : x	
	*/
	public static void getMapCnt(SettingFile data, List<Integer> list) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(Integer index : list) {
			if(map.containsKey(data.getFileData()[index])) {
				map.put(data.getFileData()[index], map.get(data.getFileData()[index]) + 1);
			} else {
				map.put(data.getFileData()[index], 1);
			}
			
			if(resultHashMap.containsKey(data.getFileData()[index])) {
				resultHashMap.put(data.getFileData()[index], resultHashMap.get(data.getFileData()[index]) + 1);
			} else {
				resultHashMap.put(data.getFileData()[index], 1);
			}
		}
		LinkedHashMap<String, Integer> linkedMap = sortedMap(map);
		System.out.print(data.getFileName() + " = ");
		linkedMap.entrySet().stream().forEach(e -> System.out.print(e.getKey() + ":" + e.getValue() + " "));
		System.out.println();
	}
	
	/*
	 * 매개변수 : SettingFile, String
	 * 설명 : 입력한 단어의 인덱스 기준으로 가중치 값들의 인덱스값들을 구한다.
	 * 반환 : List<Integer>
	*/
	public static List<Integer> getArrayIndex(SettingFile data, String word) {
		List<Integer> list = new ArrayList<Integer>();
//		해당 단어가 있는 배열의 인덱스를 구한다.
		for(int i = 0; i < data.getFileData().length; i++) {
//			System.out.println(data.getFileData().length);
//			사이즈값 크기만큼 단어 인덱스 가져오기
			if(data.getFileData()[i].equals(word)) {
				int start = i - size;
				int end = i + size;
				if(start < 0) { start = 0; }
				if(end > data.getFileData().length - 1) { end = data.getFileData().length - 1; }
				for(int cnt = start; cnt < i; cnt++) {
					list.add(cnt);
				}
				for(int cnt = end; cnt > i; cnt--) {
					list.add(cnt);
				}
//				System.out.println("start : " + start + ", end : " + end);
				
			}
		}
		return list;
	}
//	end question 2
	
	/*
	 * 매개변수 : HashMap<String, Integer>
	 * 설명 : HashMap에 저장된 Value (Integer)값들을 내림차순으로 정렬
	 * 반환 : LinkedHashMap<String, Integer>
	*/
	public static void sortedMap2(HashMap<String, Double> map) {
		LinkedHashMap<String, Double> linkedMap = new LinkedHashMap<String, Double>();
		List<String> listValueSet = new ArrayList<String>(map.keySet());
		Collections.sort(listValueSet, (value1, value2) -> (map.get(value2).compareTo(map.get(value1))));
		for(String key : listValueSet) {
			linkedMap.put(key, map.get(key));
        }
		
		for(Entry<String, Double> elem : linkedMap.entrySet()){ 
			System.out.println(elem.getKey() + " : " + elem.getValue() + " ");
		}
	}
	
	/*
	 * 매개변수 : HashMap<String Integer>
	 * 설명 : 분모 계산
	 * 반환 : double
	*/
	public static double Denominator(HashMap<String, Integer> map) {
		double result = 0;
		for(Entry<String, Integer> elem : map.entrySet()){ 
			result = result + (elem.getValue() * elem.getValue());
		}

		return Math.sqrt(result);
	}
	
	/*
	 * 매개변수 : HashMap<String, Integer>, HashMap<String, Integer>
	 * 설명 : 분자 계산
	 * 반환 : double
	*/
//	분자 계산
	public static double Numerator(HashMap<String, Integer> map1, HashMap<String, Integer> map2) {
		double result = 0;
		for(Entry<String, Integer> elem : map1.entrySet()){ 
			if(map2.containsKey(elem.getKey())) {
				result = result + (elem.getValue() * map2.get(elem.getKey()));
			}
		}
		
		return result;
	}
	
	
	/*
	 * 매개변수 : SettingFile, List<Integer>
	 * 설명 : List안에는 검색한 단어의 가중치만큼의 단어들의 Index번호들이 들어가 있다.
	 * 이 Index값들에 해당하는 값들을 SettingFile에서 얻는다.
	 * 얻은 값들을 HashMap에 삽입하면서 등장한 횟수를 카운트한다.
	 * 반환 : x	
	*/
	public static HashMap<String, Integer> getMapCnt(SettingFile data) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		for(int i = 0; i < data.getFileData().length; i++) {
			if(map.containsKey(data.getFileData()[i])) {
				map.put(data.getFileData()[i], map.get(data.getFileData()[i]) + 1);
			} else {
				map.put(data.getFileData()[i], 1);
			}
		}
		
//		System.out.print(data.getFileName() + " = ");
//		map.entrySet().stream().forEach(e -> System.out.print(e.getKey() + ":" + e.getValue() + " "));
//		System.out.println();
		return map;
	}
//	end question3

}

