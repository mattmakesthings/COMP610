import java.io.*;
import java.util.*;

public class Project1{

  public static void main(String[] args){

    String filename = "input3.txt";
    String line = null;
    int n = 0; // size of men/women
    FileReader fileReader = null;
    BufferedReader bufferedReader = null;

    Person[] men = null;
    Person[] women = null;


    try {
      //read from file
      fileReader = new FileReader(filename);

      bufferedReader = new BufferedReader(fileReader);

      //retrieve n
      line = bufferedReader.readLine();
      if(line.length() == 1 && line.matches("[0-9]+") == true){
        n = Integer.parseInt(line);
      }
    }catch(FileNotFoundException ex){
      System.out.println("Unable to open file '" + filename + "'");

    }catch(IOException ex){
      System.out.println("Error reading file '" + filename + "'");
    }

      //get mens preferences
      //first declare list of men and women
      men = Person.initPeopleArray(n,"men");
      women = Person.initPeopleArray(n,"women");

      // populate preference list of men and women
      for ( int i = 0; i < n; i++){
        men[i].PopulateFromBufferedReader(n,bufferedReader,false);
        //System.out.print("\nMen\t[" + i + "]:");
        //men[i].printContents();
      }

      for ( int i = 0; i < n; i++){
        women[i].PopulateFromBufferedReader(n,bufferedReader,false);
        //System.out.print("\nWomen\t[" + i + "]:");
        //women[i].printContents();
    }

    // start looking for stable matches
    // Gale-Shapley
    // first run with men proposing
    // then with women proposing
    // use these results to narrow down possible matchings


    // stores the permutations of stable matchings that the algorithm finds
    while(Person.allEngaged(men) == false){
      for(int i = 0; i < n; i++){
          if(!men[i].isEngaged()){
              men[i].askNext(men,women);
          }

      }
    }

    ArrayList<Integer> men_first = Person.matchPermutation(men);

    Person.clearGroup(men);
    Person.clearGroup(women);

    while(Person.allEngaged(women) == false){
      for(int i = 0; i < n; i++){
          if(!women[i].isEngaged()){
              women[i].askNext(women,men);
          }

      }
    }


    ArrayList<Integer> women_first = Person.matchPermutation(women);

    //System.out.println("men propose   --> " + men_first);
    //System.out.println("women propose --> " + women_first);

    // use these permutations to narrow down stable matching search area

    ArrayList<ArrayList<Integer>> search_area = Person.generatePermutations(men_first,women_first,men,women);

    // load these matchings into out Person variables
    // test for stability
    // return count of stable matchings

    int stable_count = 0;
    for(int i = 0; i < search_area.size();i++){
        Person.loadPermutation(search_area.get(i), men, women);

        //test for stability
        if(Person.stabilityTest(men,women))
            stable_count++;
        else
            //System.out.println("unstable matching @ search area["+i+"]");
        Person.clearGroup(men);
        Person.clearGroup(women);
        }

    System.out.println(stable_count);
    }
}
