import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class User {
    static int counter = 1;
    String name;
    double income;
    int creditScore;

    public User(String name, double income, int creditScore) {
        this.name = name;
        this.income = income;
        this.creditScore = creditScore;
    }

    public void display() {
        System.out.println("Name: " + name);
        System.out.println("Income: ₹" + income);
        System.out.println("Credit Score: " + creditScore);
    }

    public void saveToFile() {
        try {
            FileWriter writer = new FileWriter("users.csv", true);

            File file = new File("users.csv");

            if (file.length() == 0) {
                writer.write("S.No,Name,Income,CreditScore\n");
            }

            writer.write(counter + "," + name + "," + income + "," + creditScore + "\n");
            counter++;

            writer.close();
            System.out.println("✅ User saved to file!");

        }
        catch (IOException e) {
            System.out.println("❌ Error saving user.");
        }
    }

    public static void readFromFile() {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.FileReader("users.csv"));

            String line;
            
            System.out.println("\n📂 Saved Users:");
            System.out.println("----------------------");

            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                System.out.println(
                "S.No: " + data[0] +
                ", Name: " + data[1] +
                ", Income: ₹" + data[2] +
                ", Credit Score: " + data[3]
                );
            }

            reader.close();

        } 
        catch (Exception e) {
            System.out.println("❌ No saved users found.");
        }
    }
}