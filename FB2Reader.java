import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class FB2Reader{
    double[] zoneWeigh = {0.2,0.3,0.5};
    File[] listOfFiles;
    int collectionSize;
    private static final String CHARSET = "cp866";
    Map<String, Map<Integer, Integer[]>> coordinateIndex = new HashMap<>();

    FB2Reader(String path) throws IOException {
        File folder = new File(path);
        this.listOfFiles = folder.listFiles();
        this.collectionSize = listOfFiles.length;
        StringTokenizer st;
        String line = "";
        String charset = "UTF-8";
        for (int i = 0; i < collectionSize; i++) {
            int zone = 0;
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("C:/AudioFindProject/books/"+listOfFiles[i].getName()), charset));
            while ((line = reader.readLine()) != null) {
                if(!line.contains("<")){
                    st = new StringTokenizer(line, " \t\n\r\",.?!");
                    while (st.hasMoreTokens()) {
                        String word = st.nextToken().toLowerCase();
                        if(coordinateIndex.containsKey(word)){
                            if (coordinateIndex.get(word).containsKey(i)) {
                                coordinateIndex.get(word).get(i)[zone]++;
                            }else{
                                Integer[] arr = new Integer[3];
                                arr[0] = 0;
                                arr[1] = 0;
                                arr[2] = 0;
                                arr[zone]++;
                                coordinateIndex.get(word).put(i,arr);
                            }
                        }else{
                            Map<Integer,Integer[]> map = new HashMap<>();
                            Integer[] arr = new Integer[3];
                            arr[0] = 0;
                            arr[1] = 0;
                            arr[2] = 0;
                            arr[zone]++;
                            map.put(i,arr);
                            coordinateIndex.put(word, map);
                        }
                    }
                }
                else if(line.contains("/")){
                    zone++;
                }
            }
            reader.close();
        }
    }
    public double weight(String q){
       if(!coordinateIndex.containsKey(q))
           return -1;
       double n,df, idf;
       n = collectionSize;
       df = coordinateIndex.get(q).keySet().size();
       idf = Math.log(n/df);
        return idf;
    }
    public int query(String q){
        String[] queryWords = q.split(" ");
        for (int i = 0; i < queryWords.length; i++) {
            if(!coordinateIndex.containsKey(queryWords[i]))
                return -1;
        }
        int res = 0;
        double temp = 0;
        for (int j = 0; j < collectionSize; j++) {
            double mark = 0;
            for (int i = 0; i < queryWords.length; i++) {
                if(coordinateIndex.get(queryWords[i]).containsKey(j)){
                    int tf = 0;
                    for (int k = 0; k < 3; k++) {
                        tf = coordinateIndex.get(queryWords[i]).get(j)[k];
                        mark += tf*weight(queryWords[i]) * zoneWeigh[k];
                    }

                }
            }
            if(mark > temp){
                temp = mark;
                res = j;
            }

        }
        return res;
    }

    public File[] getListOfFiles() {
        return listOfFiles;
    }

    public static void main(String[] args) throws IOException {
        FB2Reader iv = new FB2Reader("C:/AudioFindProject/books");
        String input = "";
        String request = "";
        String fileName = "C:/AudioFindProject/query.txt";
        String charset = "UTF-8";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileName), charset));

        String line;
        while ((line = reader.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, " ");
            while (st.hasMoreTokens()) {
                input += st.nextToken().toLowerCase()+" ";

            }
        }
        reader.close();
        request = iv.getListOfFiles()[iv.query(input)].getName();
        System.out.println(request);
        File ff = new File("C:/AudioFindProject/request.txt");
        FileWriter nFile = new FileWriter(ff);
        nFile.write(request);
        nFile.close();
        /*
            //Шекспір
            //1 - 12.txt
            //2 - 1.txt
            //1 2 - 11.txt
            //0 - 1.txt

            //під
            //3 - 7.txt
            //2 - 10.txt
            //0 - 10.txt

        }*/
    }
}
