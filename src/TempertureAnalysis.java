import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CityTemperature {
    public static void main(String[] args) {
        String csvFile = "path_to_your_csv_file.csv";
        String line;
        String cvsSplitBy = ",";
        int maxLines = 50;
        
        Map<String, double[]> cityTemperatures = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            int lineCount = 0;
            while ((line = br.readLine()) != null && lineCount < maxLines) {
                String[] data = line.split(cvsSplitBy);
                String city = data[1];
                double temperature = Double.parseDouble(data[2]);
