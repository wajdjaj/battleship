package utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
	static public char intToChar(int in) {
		return (char) ((int) 'a' + in);
	}
	public static int[] getCoords(String placement){
		int p[] = new int[4];
		if (placement.length() > 4){
			for (int i = 0; i < 2; i++){
				int tmp[];
				if ((tmp = stringToPosition(placement.substring(i*2))) == null)
					return null;
				p[i*2] = tmp[0]; 
				p[i*2+1] = tmp[1];
			}
			return p;
		}
		return null;
	}
	
	public static int[] stringToPosition(String in){
		System.out.println("stringtopos: " + in);
		if (in == null)
			System.exit(0);
		Pattern pattern = Pattern.compile("(\\w\\d+)");
		Matcher matcher = pattern.matcher(in);
		if (matcher.find()){			
			int p[] = new int[2];
			p[0] = Integer.parseInt(matcher.group(1).substring(1))-1;			
			p[1] = charToInt(matcher.group(1).charAt(0));
			return p;
		}
		return null;
	}
	static int charToInt(char in){		
		return Character.toLowerCase(in) - 'a';
	}
}
