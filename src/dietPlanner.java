import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class dietPlanner {
    
private Map<String, Integer> mealIndexMap;
private double[][] adjacencyMatrix;

public dietPlanner(String[] mealItems) {
    
    this.mealIndexMap = new HashMap<>();
    initializeMealIndexMap(mealItems);
    this.adjacencyMatrix = new double[mealItems.length / 2][mealItems.length / 2];
    buildGraph(mealItems);
}

private void initializeMealIndexMap(String[] mealItems) {
    for (int i = 0; i < mealItems.length; i += 2) {
        mealIndexMap.put(mealItems[i], i / 2);
    }
}

private int getCalories(String meal) {
    String[] tokens = meal.split(": ");
    return Integer.parseInt(tokens[1]);
}

private void buildGraph(String[] mealItems) {
    for (int i = 0; i < mealItems.length; i += 2) {
        for (int j = 0; j < mealItems.length; j += 2) {
            if (i != j) {
                int node1Index = mealIndexMap.get(mealItems[i]);
                int node2Index = mealIndexMap.get(mealItems[j]);
                int edgeWeight = getCalories(mealItems[i + 1]) + getCalories(mealItems[j + 1]);
                adjacencyMatrix[node1Index][node2Index] = edgeWeight;
                adjacencyMatrix[node2Index][node1Index] = edgeWeight;
            }
        }
    }
}

public static double calculateDailyCalories(double currentWeight, double height, int age, String gender) {
    double caloriesPerDay;
    if (gender.equalsIgnoreCase("Male")) {
        caloriesPerDay = 66 + (6.3 *(currentWeight* 2.20462)) + (12.9*height)-(6.8* age);
    } else if (gender.equalsIgnoreCase("Female")) {
        caloriesPerDay = 655 + (4.3 *(currentWeight* 2.20462)) + (4.7*height)-(4.7* age);
    } else {
        throw new IllegalArgumentException("Invalid gender. Please provide 'Male' or 'Female'.");
    }

    return caloriesPerDay;
}


 private static int[] findEdge(double[][] adjacencyMatrix, int[] visitedCount, int maxVisitCount,double dailyCalories,double[] lose,double loseWeightInCalories) {
        int numNodes = adjacencyMatrix.length;
        Queue<Integer> queue = new ArrayDeque<>();

        for (int i = 0; i < numNodes; i++) {
            if (visitedCount[i] < maxVisitCount) {
                queue.offer(i);
            }
        }
        while (!queue.isEmpty()) {
            int currentNode = queue.poll();
            
            for (int neighbor = 0; neighbor < numNodes; neighbor++) {
                if (adjacencyMatrix[currentNode][neighbor] > 0 && adjacencyMatrix[currentNode][neighbor] <= Math.ceil(lose[0])&& adjacencyMatrix[currentNode][neighbor] <=Math.ceil(dailyCalories)) {
                    double difference=lose[0]-adjacencyMatrix[currentNode][neighbor];
                    lose[1] = difference + loseWeightInCalories;
                    visitedCount[currentNode]++;
                    visitedCount[neighbor]++;
                    return new int[]{currentNode, neighbor};
                }
            }
        }

        return null;
    }

public void printGraph(int schedule, double dailyCalories,double loseWeightInCalories, String[] mealNames) {
    // for (int i = 0; i < adjacencyMatrix.length; i++) {
    //     for (int j = 0; j < adjacencyMatrix[i].length; j++) {
    //         System.out.printf(adjacencyMatrix[i][j]+" ");
    //     }
    //     System.out.println();
    // }
    int numNodes = adjacencyMatrix.length;
int[] visitedCount = new int[numNodes];
int maxVisitCount = 1;
        double[] lose = new double[2];
        lose[0] = loseWeightInCalories;
        int l=1;
    for (int i = 0; i < schedule; i++) {

        int[] edge = findEdge(adjacencyMatrix, visitedCount, maxVisitCount,dailyCalories,lose, loseWeightInCalories);
        if (edge != null) {

System.out.println("Day " + l++ + " Meal =>");
System.out.println("  First Meal: " + mealNames[edge[0]]);
System.out.println("  Second Meal: " + mealNames[edge[1]]);
System.out.println("  Total Calories: " + adjacencyMatrix[edge[0]][edge[1]]);
System.out.println("***********************************");
            lose[0] = lose[1];
        } else {
            System.out.println("No more edges with satisfy your condition....Please add more meal items");
            break;
        }
    }
}


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Print a welcome message
        System.out.println("*******************************");
        System.out.println("      Welcome to Diet Planner   ");
        System.out.println("*******************************");

        System.out.println("\nLet's get started! Please fill in the following information.");
        
        int schedule;
        do {
            System.out.print("\n\nEnter your desired schedule (minimum 15 days and maximum 30 days): ");
            schedule = scanner.nextInt();

            if (schedule < 15 || schedule > 30) {
                System.out.println("Please enter a desired schedule for at least 15 days and at most 30 days.");
            }
        } while (schedule < 15 || schedule > 30);

        // Get the current weight
        System.out.print("\nEnter your current weight in kg: ");
        int currentWeight = scanner.nextInt();

        // Get the amount of weight to lose
        double loseWeight;
        do {
            System.out.print("\nEnter the amount of weight you want to lose in " + schedule + " days: ");
            loseWeight = scanner.nextDouble();

            if ((schedule < 20 && loseWeight > 1.5) || (schedule < 30 && loseWeight > 2.5)) {
                System.out.println("WARNING: You should not lose more than 2.5 kg in a month or more than 1.5 kg in 15 days. It can be harmful to your body.");
            }
        } while ((schedule < 20 && loseWeight > 1.5) || (schedule < 30 && loseWeight > 2.5));

        // Get the height
        System.out.print("\nEnter your height in inches: ");
        double height = scanner.nextDouble();

        // Get the age
        System.out.print("\nEnter your age: ");
        int age = scanner.nextInt();

        // Get the gender
        System.out.print("\nEnter your gender (Male/Female): ");
        String gender = scanner.next();

        // Close the scanner
        scanner.close();

        // Display a summary
        System.out.println("\n*******************************");
        System.out.println("        User Information       ");
        System.out.println("*******************************");
        System.out.println("Schedule: " + schedule + " days");
        System.out.println("Current Weight: " + currentWeight + " kg");
        System.out.println("Weight to Lose: " + loseWeight + " kg");
        System.out.println("Height: " + height + " inches");
        System.out.println("Age: " + age + " years");
        System.out.println("Gender: " + gender);


double BMR=calculateDailyCalories(currentWeight, height, age, gender);

        System.out.println("\nYour BMR is: "+BMR+" Calories/day");

        System.out.println("\n***********************************");
System.out.println("*    Diet Planner - Meal Recommendation    *");
System.out.println("***********************************\n");
double loseWeightInCalories=(loseWeight*7700)/schedule;

        
        String[] mealItems = {
        "Biryani", "Calories: 700",
        "Fish Curry with Rice", "Calories: 500",
        "Khichuri", "Calories: 400",
        "Chicken Bhuna", "Calories: 600",
        "Bhat with Hilsha Fish", "Calories: 450",
        "Beef Korma", "Calories: 550",
        "Shorshe Ilish", "Calories: 350",
        "Dal with Chapati", "Calories: 300",
        "Cauliflower Bhaji", "Calories: 150",
        "Chingri Bhapa", "Calories: 400",
        "Beef Bhuna", "Calories: 300",
        "Aloo Posto", "Calories: 300",
        "Machher Jhol", "Calories: 350",
        "Luchi with Alur Dom", "Calories: 450", 
        "Mishti Doi", "Calories: 200",
        "Kacchi Biryani", "Calories: 800",
        "Morog Polao", "Calories: 600",
        "Bhapa Doi", "Calories: 300",
        "Shutki Bhorta", "Calories: 200",
        "Patishapta", "Calories: 250",
        "Chitol Macher Muitha", "Calories: 400",
        "Kala Bhuna", "Calories: 350",
        "Begun Bhaja", "Calories: 200",
        "Chapati with Chicken Curry", "Calories: 450",
        "Labra (Mixed Vegetable)", "Calories: 160",
        "Bhapa Ilish", "Calories: 350",
        "Fried Rice", "Calories: 250",
        "Kheer", "Calories: 300",
        "Roti (Chapati)", "Calories: 80",
        "Dal (Lentil Soup)", "Calories: 150",
};

String[] mealNames = {
     "Biryani",
        "Fish Curry with Rice",
        "Khichuri",
        "Chicken Bhuna",
        "Bhat with Hilsha Fish",
        "Beef Korma",
        "Shorshe Ilish",
        "Dal with Chapati",
        "Cauliflower Bhaji",
        "Chingri Bhapa",
        "Beef Bhuna",
        "Aloo Posto",
        "Machher Jhol",
        "Luchi with Alur Dom", 
        "Mishti Doi",
        "Kacchi Biryani",
        "Morog Polao",
        "Bhapa Doi",
        "Shutki Bhorta",
        "Patishapta",
        "Chitol Macher Muitha",
        "Kala Bhuna",
        "Begun Bhaja",
        "Chapati with Chicken Curry",
        "Labra (Mixed Vegetable0",
        "Bhapa Ilish",
        "Fried Rice",
        "Kheer",
        "Roti (Chapati)",
        "Dal (Lentil Soup)"  
};
dietPlanner d= new dietPlanner(mealItems);

d.printGraph(schedule,BMR,loseWeightInCalories, mealNames);
    }
}