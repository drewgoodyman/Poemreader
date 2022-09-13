import java.io.BufferedReader; 
import java.io.DataInputStream;
import java.io.FileInputStream; 
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import java.util.Map.Entry; 
import java.util.Set;
import java.util.regex.Pattern;

public class PoemReader {

	public static void main(String args[]) { 
		Map<String, Integer> wordMap = poemMap("src/testingRaven.html"); 
		List<Entry<String, Integer>> list = sortByValueInDecreasingOrder(wordMap); 
		System.out.println("list of the top 20 most frequently used words");
		System.out.println("*********************************************");
		for (Map.Entry<String, Integer> entry : list) {
			if (entry.getValue() > 1) { 
				System.out.println(entry.getKey() + " \t\t ***  " + entry.getValue());
			}
		} 
	} 
	public static Map<String, Integer> poemMap(String fileName) {
		
		Map<String, Integer> wordMap = new HashMap<>();
		try (FileInputStream fis = new FileInputStream(fileName);
				DataInputStream dis = new DataInputStream(fis);
				BufferedReader br = new BufferedReader( new InputStreamReader(dis))) {	
			Pattern pattern = Pattern.compile("[^a-zA-Z]");
			String start = "<div class=\"chapter\">";
			String end = "</div><!--end chapter-->";
			String line = null;
            while ((line = br.readLine()) != null) {
                if (line.contains(start))
                    while (!line.contains(end)) {
                        line = line.toLowerCase();
                        line = line.replaceAll("<[^>]*>", "");
                        line = line.replaceAll("\\!@#$%^&*()<.*?>", "");
                        line = line.replaceAll("&.\"'*?;", "");
                        String[] words = pattern.split(line);
                        for (String word : words) {
                            if (wordMap.containsKey(word)) {
                                wordMap.put(word, (wordMap.get(word) + 1));
                            } else {
                                wordMap.put(word, 1);
                            }
                        }
                        line = br.readLine();
                    }
            }
			
		} catch (IOException ioex) { 
			ioex.printStackTrace(); } 
		return wordMap; 
	}
	public static List<Entry<String, Integer>> sortByValueInDecreasingOrder( Map<String, Integer> wordMap) {
		Set<Entry<String, Integer>> entries = wordMap.entrySet();
		List<Entry<String, Integer>> list = new ArrayList<>(entries);
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			@Override 
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
				}
			});
		return list.subList(1, 21);
		}
	}
