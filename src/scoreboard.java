//Assignment name: Scoreboard
//Vidun Jayakody
//October 3rd, 2020
//Generates a random score and updates a text file as the high score increases

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.lang.String;

public class scoreboard {

    public static void main(String[] args) {
       
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> score = new ArrayList<String>();
        ArrayList<String> rank = new ArrayList<String>();
        ArrayList<String> unsortedScore = new ArrayList<String>();
        ArrayList<String> unsortedNames = new ArrayList<String>();
        Scanner in = new Scanner(System.in);
        String title = "";

        File file = new File("gameData.txt");
        boolean exists = file.exists();

        // If the file doesn't exist
        if (!exists){
            for (int i = 0; i<=100; i++) {
                name.add("***");
                score.add("0");
                rank.add(Integer.toString(i)+1);
            }

            // Asks user for game title
            System.out.println("Enter Game Title: ");
            title = in.nextLine();

            /*Catches errors and makes file if it doesnt already exist. Then it plays the game (generate new value) 
            and prints the new updated file. */
            try {
                FileWriter fw = new FileWriter("gameData.txt");
                writeFile("gameData.txt", title, rank, name, score);

                int newScore;
                newScore = playGame(exists);

                String newName;
                newName = getName();
                
                score.add(0,Integer.toString(newScore));
                name.add(0,(newName));
                writeFile("gameData.txt", title, rank, name, score);
                topEleven(title, name, score);
            }

            catch (IOException e){} // End first try catch

        } 
        /* If the file already exists, it reads the existing file, stores the names and score
        values in tempScoreName, and those values are then added to main 'name'and 'score' arraylists. */
        else{ 
            try {
            String [][] tempScoreName = new String[2][100];
            int newScore;
            String newName;

            tempScoreName = readFile("gameData.txt", title, rank, name, score);

            for (int j = 0; j < 100; j ++){
                name.set(j,tempScoreName[0][j]);
                score.set(j,tempScoreName[1][j]);
            }

            //adds the newly generated score value to the front of the 'score' arraylist.
            newScore = playGame(exists);
            score.add(0,Integer.toString(newScore));
            
            //adds the newly generated name value to the front of the 'name' arraylist.
            newName = getName();
            name.add(0,newName);
    
            //adds all the scores to a temporary arraylist called unsortedScore
            for(String item: score) {
                unsortedScore.add(item);
            }

            //adds all the scores to a temporary arraylist called unsortedNames
            for(String item2: name) {
                unsortedNames.add(item2);
            }

            //The original arraylist 'score' is sorted and reversed to get the highest on top
            sort(score);
            Collections.reverse(score);

            /* This essentially matches the names to the score. It checks where the value i in the (now) sorted 
            score array is in the unsorted (initial input) array. This gives the index for the sort order from teh 
            initial input. Then this since the names list, will have the same size, I use that index (i) at the unsortedNames
            arraylist and set it as the new value for my main arraylist at index i. 
            This was done to mock a "treemap" key and value feature. */
            for (int i = 0; i < score.size(); i++){
                for (int j = 0; j < score.size(); j++){
                    if (score.get(i).equals(unsortedScore.get(j)) && !score.get(i).equals("0")){
                        name.set(i, unsortedNames.get(j));
                    }
                }
            }

            // Prints top 11 scores to terminal
            try {
                FileReader fr = new FileReader("gameData.txt");
                BufferedReader br = new BufferedReader(fr);
                String topEleven="";
                System.out.println("\t\t    " + title);
                System.out.println("\t\t    HIGH SCORES");
                System.out.println("\tRANK\t  NAME\t\tSCORE");

                for (int i = 0; i<9; i++) {
                    topEleven = "\t" + (i+1) + "         " + name.get(i) + "         " + score.get(i);
                    System.out.println(topEleven);
                }
                for (int i = 9; i<11; i++) {
                    topEleven = "\t" + (i+1) + "        " + name.get(i) + "         " + score.get(i);
                    System.out.println(topEleven);
                }
                br.close();
            }

            catch (Exception e) {}
        }

        /* If a NullPointException occurs (can't read file to sort into array), it means the
        file is corrupt, and program needs to be restarted. */
        catch (NullPointerException err) {
            System.out.println("\nFATAL ERROR: 'gameData.txt' file is corrupt! Please delete this from the source directory and try again.");
            System.out.println("NOTE: ALL HIGH SCORES WILL BE ERASED.\n");
        }
        

            }

            writeFile("gameData.txt", title, rank, name, score);
            in.close();
            
        }

    //Method that wirtes the file (deletes the old one) and creates an updated version.
    public static void writeFile(String filename, String title, ArrayList<String> rank, ArrayList<String> name, ArrayList<String> score){
        try {
            
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            new File("gameData.txt").delete();
            FileWriter fw = new FileWriter("gameData.txt");
            PrintWriter pw = new PrintWriter(fw);
            String line;
            
            // Initializes document
            pw.println("\t\t    " + title);
            pw.println("\t\t    HIGH SCORES");
            pw.println("\tRANK\t  NAME\t\tSCORE");
            
            // Printing the line with the values from name and score (from prev. file)
            for (int i = 0; i<9; i++) {
                line = "\t" + (i+1) + "         " + name.get(i) + "         " + score.get(i);
                pw.println(line);
            }
            for (int i = 9; i<99; i++) {
                line = "\t" + (i+1) + "        " + name.get(i) + "         " + score.get(i);
                pw.println(line);
            }
            line = "\t" + "100" + "       " + name.get(name.size()-1) + "         " + score.get(score.size()-1);
            pw.println(line);

            pw.close(); 
            br.close();
        }
        catch (Exception e) {}
    }

    //reads old file. Adds the neccesarry piece of information (ex. rank, name and score) into 
    //the appropriate array
    public static String[][] readFile(String filename, String title, ArrayList<String> rank, ArrayList<String> name, ArrayList<String> score) {
        
        try{

            BufferedReader br = new BufferedReader(new FileReader(filename)); 

            String line;
            br.readLine();
            br.readLine();
            br.readLine();

            while ((line = br.readLine()) != null) {
                    int i = line.indexOf('\t');
                        String rankTemp = line.substring(i+1, i+3);
                        rankTemp = rankTemp.replaceAll(" ","");
                        rank.add(rankTemp);

                        String nameTemp = (line.substring(11,14));
                        name.add(nameTemp);
        
                        String scoreTemp = (line.substring(14+9,line.length()));
                        score.add(scoreTemp);    
            }

            String[][] nameScore = new String[100][100];
           
            for (int j = 0; j < 100; j++){
                nameScore[0][j] = name.get(j);
                nameScore[1][j] = score.get(j);
            }
            br.close();
            return nameScore;
        }
        
        catch (Exception e) {return null;}
    }

    //Sort method 
    public static ArrayList<String> sort(ArrayList<String> score) {
                score.sort(Comparator.comparing(Integer::valueOf));
                return score;
    }

    //Runs game (generates random value) returns this value
    public static Integer playGame(Boolean exists){ 
            Random rand = new Random();   
            System.out.println("Press Enter to generate your score...");
                try
                    {
                        System.in.read();
                    }  
                catch(Exception e){} 

            int userScore = rand.nextInt(1000000000);
            System.out.println("Your score is " + userScore + "!\n");
            return userScore;

        }

    //Prompts user to enter valid name and returns this name
    public static String getName() {

    String processedName, finalName;
    Scanner in = new Scanner(System.in);

        try {
        System.out.println("Congratulations! You scored in the top 100!");
        System.out.println("Enter initials (MAX 3): ");

        while (!in.hasNext("[A-Za-z]+")) {
            System.out.println("Special characters not allowed!");
            in.next();
        }

        processedName = in.next();

        if (processedName.length() == 1)
            processedName = processedName + "--";

        else if(processedName.length() == 2)
            processedName = processedName + "-";

        in.close();

        System.out.println("Thank you for playing!");
        finalName = (processedName.substring(0, 3)).toUpperCase();
        return finalName;

    }

    catch (Exception e) {return null;}
    }

    public static void topEleven(String title, ArrayList<String> name, ArrayList<String> score) {
        try {
            FileReader fr = new FileReader("gameData.txt");
            BufferedReader br = new BufferedReader(fr);
            String topEleven="";
            System.out.println("\t\t    " + title);
            System.out.println("\t\t    HIGH SCORES");
            System.out.println("\tRANK\t  NAME\t\tSCORE");

            for (int i = 0; i<9; i++) {
                topEleven = "\t" + (i+1) + "         " + name.get(i) + "         " + score.get(i);
                System.out.println(topEleven);
            }
            for (int i = 9; i<11; i++) {
                topEleven = "\t" + (i+1) + "        " + name.get(i) + "         " + score.get(i);
                System.out.println(topEleven);
            }
            br.close();
        }

        catch (Exception e) {}
    }

}
