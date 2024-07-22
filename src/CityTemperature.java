import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CityTemperature {
    public static void main(String[] args) {
        String csvFile = "merged_data[1].csv"; // Remplacez par le chemin réel de votre fichier CSV
        String line;
        String cvsSplitBy = ",";
        int maxLines = 50; // Limiter le nombre de lignes lues

        Map<String, double[]> cityTemperatures = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            int lineCount = 0;

            while ((line = br.readLine()) != null && lineCount < maxLines) {
                // Ignore la ligne d'en-tête si présente
                if (lineCount == 0 && line.contains("city,temperature")) {
                    lineCount++;
                    continue;
                }

                String[] data = line.split(cvsSplitBy);

                // Vérifiez que les données sont valides
                if (data.length >= 3) {
                    String city = data[1].trim(); // Index 1 pour la ville
                    try {
                        double temperature = Double.parseDouble(data[2].trim()); // Index 2 pour la température

                        if (cityTemperatures.containsKey(city)) {
                            double[] temps = cityTemperatures.get(city);
                            temps[0] = Math.max(temps[0], temperature); // Température maximale
                            temps[1] = Math.min(temps[1], temperature); // Température minimale
                            temps[2] += temperature; // Somme des températures
                            temps[3]++; // Nombre de températures
                        } else {
                            double[] temps = new double[4];
                            temps[0] = temperature; // Température maximale
                            temps[1] = temperature; // Température minimale
                            temps[2] = temperature; // Somme des températures
                            temps[3] = 1; // Nombre de températures
                            cityTemperatures.put(city, temps);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid temperature format at line " + (lineCount + 1) + ": " + data[2]);
                    }
                } else {
                    System.err.println("Invalid data line at line " + (lineCount + 1) + ": " + line);
                }

                lineCount++;
            }

            // Calculer la température moyenne
            for (Map.Entry<String, double[]> entry : cityTemperatures.entrySet()) {
                double[] temps = entry.getValue();
                temps[2] = temps[2] / temps[3]; // Calcul de la température moyenne
            }

            // Afficher les résultats
            for (Map.Entry<String, double[]> entry : cityTemperatures.entrySet()) {
                double[] temps = entry.getValue();
                System.out.println(entry.getKey() + ": [" + temps[0] + ", " + temps[1] + ", " + temps[2] + "]");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
