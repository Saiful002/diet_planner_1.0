import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class dietPlanner {
    
private Map<String, Integer> mealIndexMap;
private double[][] adjacencyMatrix;
public double[] doubleArray = new double[30];

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

    // Calculate calories per day based on gender
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


 private static int[] findEdge(double[][] adjacencyMatrix, int[] visitedCount, int maxVisitCount,double dailyCalories,double loseWeightInCalories,double doubleArray[]) {
        int numNodes = adjacencyMatrix.length;
        Queue<Integer> queue = new ArrayDeque<>();

        for (int i = 0; i < numNodes; i++) {
            if (visitedCount[i] < maxVisitCount) {
                queue.offer(i);
            }
        }
        double [] array=new double[30];
        for(int i=0;i<30;i++){
            array[i]=doubleArray[i];
        }
        int k=0;
        while (!queue.isEmpty()) {
            int currentNode = queue.poll();
            
            for (int neighbor = 0; neighbor < numNodes; neighbor++) {
                if (adjacencyMatrix[currentNode][neighbor] > 0 && adjacencyMatrix[currentNode][neighbor] <=array[k]&& adjacencyMatrix[currentNode][neighbor] <=Math.ceil(dailyCalories)) {
                    double difference=array[k]-adjacencyMatrix[currentNode][neighbor];
                    array[k+1]+=difference;
                    System.out.println(array[k]+ " "+ adjacencyMatrix[currentNode][neighbor]);
                    k++;
                    visitedCount[currentNode]++;
                    visitedCount[neighbor]++;
                    return new int[]{currentNode, neighbor};
                }
            }
        }

        return null;
    }

public void printGraph(int schedule, double dailyCalories,double loseWeightInCalories,double doubleArray[]) {
    // for (int i = 0; i < adjacencyMatrix.length; i++) {
    //     for (int j = 0; j < adjacencyMatrix[i].length; j++) {
    //         System.out.printf(adjacencyMatrix[i][j]+" ");
    //     }
    //     System.out.println();
    // }
    int numNodes = adjacencyMatrix.length;
int[] visitedCount = new int[numNodes];
int maxVisitCount = 1;
    for (int i = 0; i < schedule; i++) {
        int[] edge = findEdge(adjacencyMatrix, visitedCount, maxVisitCount,dailyCalories,loseWeightInCalories,doubleArray);
        if (edge != null) {
            System.out.printf("Edge with weight less than 1500: Nodes %d and %d and %d\n", edge[0], edge[1],adjacencyMatrix[edge[0]][edge[1]]);
        } else {
            System.out.println("No more edges with weight less than 1500.");
            break;
        }
    }
}


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int schedule;
        do {
            System.out.print("Enter your desired schedule (minimum 15 days and maximum 30 days): ");
            schedule = scanner.nextInt();

            if (schedule < 15 || schedule>30) {
                System.out.println("Please enter a desired schedule for at least 15 days and at most 30 days.");
            }
        } while (schedule < 15 || schedule>30);


System.out.print("Enter your current weight in kg: ");
        int currentWeight = scanner.nextInt();


        double loseWeight;
        do {
            System.out.print("Enter the amount of weight you want to lose in "+schedule+ " days :");
            loseWeight = scanner.nextDouble();

            if (schedule <20 && loseWeight>1.5 || schedule<30 && loseWeight>2.5) {
                System.out.println("You should not lose you weight more the 2.5 Kg in a month and more than 1.5 Kg in 15 days. It will be harmful for your body condition!!");
            }
        } while (schedule <20 && loseWeight>1.5 || schedule<30 && loseWeight>2.5);


        System.out.print("Enter your height in Inches: ");
        double height = scanner.nextDouble();

        System.out.print("Enter your age: ");
        int age = scanner.nextInt();

        System.out.print("Enter your gender: ");
        String gender = scanner.next();

double loseWeightInCalories=(loseWeight*7700)/schedule;
double[] doubleArray = new double[30];

        // Set all elements to 5.0
        for (int i = 0; i < 30; i++) {
            doubleArray[i] = Math.ceil(loseWeightInCalories);
        }
        
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
dietPlanner d= new dietPlanner(mealItems);
d.printGraph(schedule,calculateDailyCalories(currentWeight, height, age, gender),loseWeightInCalories,doubleArray);
// double dailyCalories = ;
//  System.out.println("Your daily calories are : "+dailyCalories);
    }
}
