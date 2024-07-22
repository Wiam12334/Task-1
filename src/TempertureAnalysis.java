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
                if (cityTemperatures.containsKey(city)) {
                    double[] temps = cityTemperatures.get(city);
                    temps[0] = Math.max(temps[0], temperature);
                    temps[1] = Math.min(temps[1], temperature);
                    temps[2] += temperature;
                    temps[3]++;
                } else {
                    double[] temps = new double[4];
                    temps[0] = temperature; // max temp
                    temps[1] = temperature; // min temp
                    temps[2] = temperature; // sum of temps
                    temps[3] = 1;           // count of temps
                    cityTemperatures.put(city, temps);
                }

                lineCount++;
            }

            // Calculer la température moyenne
            for (Map.Entry<String, double[]> entry : cityTemperatures.entrySet()) {
                double[] temps = entry.getValue();
                temps[2] = temps[2] / temps[3];
            }
