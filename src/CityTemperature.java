import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CityTemperature {
    public static void main(String[] args) {
        String csvFile = "merged_data[1].csv";
        String line;
        String cvsSplitBy = ",";
        
        Map<String, double[]> cityTemperatures = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Lire et ignorer la première ligne (en-têtes)
            String headerLine = br.readLine();
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                
                // Vérifiez si la ligne contient le nombre attendu de colonnes
                if (data.length < 3) {
                    System.err.println("Ligne mal formée: " + line);
                    continue;
                }

                String city = data[1];
                String temperatureStr = data[2];

                // Vérifiez si la donnée de température est un nombre valide
                double temperature;
                if (isNumeric(temperatureStr)) {
                    temperature = Double.parseDouble(temperatureStr);
                } else {
                    // Ignorez cette ligne si la température n'est pas valide
                    continue;
                }

                if (cityTemperatures.containsKey(city)) {
                    double[] temps = cityTemperatures.get(city);
                    temps[0] = Math.max(temps[0], temperature); // Température maximale
                    temps[1] = Math.min(temps[1], temperature); // Température minimale
                    temps[2] += temperature; // Somme des températures
                    temps[3]++; // Nombre de températures
                } else {
                    double[] temps = new double[4];
                    temps[0] = temperature; // max temp
                    temps[1] = temperature; // min temp
                    temps[2] = temperature; // sum of temps
                    temps[3] = 1;           // count of temps
                    cityTemperatures.put(city, temps);
                }
            }

            // Calculer la température moyenne
            for (Map.Entry<String, double[]> entry : cityTemperatures.entrySet()) {
                double[] temps = entry.getValue();
                temps[2] = temps[2] / temps[3];
            }

            // Afficher les résultats
            for (Map.Entry<String, double[]> entry : cityTemperatures.entrySet()) {
                double[] temps = entry.getValue();
                System.out.println(entry.getKey() + ": [Max: " + temps[0] + ", Min: " + temps[1] + ", Avg: " + temps[2] + "]");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour vérifier si une chaîne est un nombre valide
    private static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
