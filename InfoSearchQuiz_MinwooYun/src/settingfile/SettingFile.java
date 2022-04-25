package settingfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Pattern;

public class SettingFile {
//	파일 이름
	private String fileName;
//	파일 데이터
	private String[] fileData;
//	파일 경로
	public static String URL = "C:\\Users\\yun\\git\\InfoSearchQuiz\\첨부파일1";
	
	/*
	 * 매개변수 : fileName(파일 이름), URL(파일 URL)
	 * 설명 : 사용자가 지정한 파일의 내용과 파일이름, URL을 생성자를 통해 객체를 생성한다.
	 * 반환 : x
	*/
	public SettingFile(String fileName, String URL) {
		this.fileName = fileName;
		this.fileData = setFileData();
	}

	/*
	 * 매개변수 : x
	 * 설명 : 생성자를통해 호출받은 함수 
	 * 이 함수를 통해 해당 파일이 가지고 있는 데이터를 배열을 통해 반환
	 * 반환 : String[]
	*/
	private String[] setFileData() {	
		try {
			String str = "";
			File file = new File(URL + "\\" + fileName);
			BufferedReader BFreader = new BufferedReader(new FileReader(file));
		    String line;
		    while ((line = BFreader.readLine()) != null){
		      str = str.concat(line);
		    }
		    String[] strData = str.split(" "); 
		    BFreader.close();
			
		    return strData;
		    
		}catch (Exception e) {
			return new String[0];
		}
	}
	
	/*
	 * 매개변수 : 파일 경로 (파일 포함 x)
	 * 설명 : 사용자가 지정한 경로안에 존재하는 모든 파일 리스트를 반환
	 * 반환 : String[]
	*/
	public static String[] getFileName(String URL) {
//		URL 경로 안에있는 파일들의 목록을 담는 배열 생성
		File dir = new File(URL);
		String[] dataName = dir.list();
		
		return dataName;
	}

	/*
	 * 매개변수 : word (사용자가 검색한 단어)
	 * 설명 : 사용자가 검색한 단어가 조건에 만족한지 검사
	 * 반환 : boolean
	*/
	public static boolean getType(String word) {
        return Pattern.matches("^[a-zA-Z]*$", word);
    }
	
	/*
	 * Getter
	*/
	public String getFileName() { return this.fileName; }
	public String[] getFileData() { return this.fileData; }
	public String getURL() { return SettingFile.URL; }
}
