import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CityTemperature {
    public static void main(String[] args) {
        String csvFile = "merged_data[1].csv"; // Assurez-vous que ce chemin est correct
        String line;
        String cvsSplitBy = ",";

        // Utiliser une Map pour stocker les données de température
        Map<String, double[]> cityTemperatures = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Lire la ligne d'en-tête si présente
            boolean headerSkipped = false;

            while ((line = br.readLine()) != null) {
                // Ignorer la première ligne si elle contient des en-têtes
                if (!headerSkipped) {
                    headerSkipped = true; // Ne pas ignorer les lignes suivantes
                    continue; // Passe à la ligne suivante
                }

                // Séparer les données de la ligne
                String[] data = line.split(cvsSplitBy);

                // Vérifier que la ligne contient suffisamment de colonnes
                if (data.length >= 3) {
                    String city = data[0].trim(); // Assurez-vous que c'est le bon index pour la ville
                    String tempString = data[1].trim();

                    try {
                        double temperature = Double.parseDouble(tempString);

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
                        System.err.println("Invalid temperature format at line: " + line + " Error: " + e.getMessage());
                    }
                } else {
                    System.err.println("Invalid data line: " + line);
                }
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
