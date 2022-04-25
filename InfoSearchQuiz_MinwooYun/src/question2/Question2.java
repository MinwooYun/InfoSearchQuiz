package question2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import settingfile.*;

public class Question2 {
	private static int size = 2;
	private static String URL = SettingFile.URL;
	private static HashMap<String, Integer> resultHashMap = new HashMap<String, Integer>();


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
	
	/*
	 * 매개변수 : HashMap<String, Integer>
	 * 설명 : HashMap에 저장된 Value (Integer)값들을 내림차순으로 정렬
	 * 매개변수 T는 숫자만 받는다 (Integer, Float, Double)
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
	
	
}
