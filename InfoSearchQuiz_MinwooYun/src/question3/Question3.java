package question3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import settingfile.SettingFile;

public class Question3 {
	
	private static String URL = SettingFile.URL;
	private static int fileIndex;
	private static double denominator;
	
	public static void main(String[] args) {
//		모든 파일들을 하나의 리스트안에 저장
		List<SettingFile> list = new ArrayList<SettingFile>();
		List<HashMap<String, Integer>> listMap = new ArrayList<HashMap<String, Integer>>();
//		문서별 코사인 유사도를 저장할 HashMap
		HashMap<String, Double> result = new HashMap<String, Double>();
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

//		파일별 모든 문자들을 hashMap 형태로 변환
		for(SettingFile data : list) {
			listMap.add(getMapCnt(data));
		}
		
//		기준이 되는 문서의 분모 계산
		denominator = Denominator(listMap.get(fileIndex));
//		System.out.println("standard : " + denominator);
		for(int i = 0; i < listMap.size(); i++) {
//			이미 앞에서 구한 기준문서는 skip
			if(i == fileIndex) { continue; }		
			
			double denominator2 = Denominator(listMap.get(i));
//			System.out.println(i + " : " + denominator2);
			double numerator = Numerator(listMap.get(fileIndex), listMap.get(i));
			
			result.put(fileName[i], numerator/(denominator*denominator2));
		}
		
		System.out.println(fileName[fileIndex] + " -> ");
		sortedMap(result);	
		
		sc.close();
	}
	
	/*
	 * 매개변수 : HashMap<String, Integer>
	 * 설명 : HashMap에 저장된 Value (Integer)값들을 내림차순으로 정렬
	 * 반환 : LinkedHashMap<String, Integer>
	*/
	public static void sortedMap(HashMap<String, Double> map) {
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
//				map.put(data.getFileData()[i], 1);
			} else {
				map.put(data.getFileData()[i], 1);
			}
		}
		
//		System.out.print(data.getFileName() + " = ");
//		map.entrySet().stream().forEach(e -> System.out.print(e.getKey() + ":" + e.getValue() + " "));
//		System.out.println();
		return map;
	}

}