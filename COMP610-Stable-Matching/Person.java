import java.io.*;
import java.util.*;

public class Person{
  //private class to store the persons preferred partner
  // and whether or not they had been proposed to yet
  String groupName;

  //match indicates the matches index in the preference list
  int match;
  int name;
  boolean engaged;

  public class Preferences{
    int name;
    boolean proposedTo;


    Preferences(){
      name = -1;
      proposedTo = false;
    }
  }

    Preferences[] p_list; //preference list
    Person(){
      this.p_list = new Preferences[0];
      match = -1;
      name = -1;
      groupName = "";
      engaged = false;
    }
    Person(int position,int size,String g_name){
      this.p_list = new Preferences[size];
      match = -1;
      groupName = g_name;
      engaged = false;
      name = position+1;


    }


  public void PopulateFromBufferedReader(int size, BufferedReader bufferedReader, boolean show){
    try {
    String line = null;
    /*for (int i = 0; (line = bufferedReader.readLine()) !=null && i < size; i++ )*/
    if((line = bufferedReader.readLine()) !=null){
      StringTokenizer st = new StringTokenizer(line);

      //parse string for preferences
      for(int i = 0; st.hasMoreTokens()== true && i < size; i++){
          this.p_list[i].name = Integer.parseInt(st.nextToken());
        }
      }
      if( show == true)
        System.out.println(line);

    }catch(FileNotFoundException ex){
      System.out.println("Unable to open file -- Person.PopulateFromBufferedReader" );
    }catch(IOException ex){
      System.out.println("Error reading file -- Person.PopulateFromBufferedReader");
    }
  }

  //funtions that act on array of Person objects

  //creates an nxn array of People objects
  public static Person[] initPeopleArray(int size,String groupName){
    Person[] group = new Person[size];
    for( int i = 0; i < size; i++){
      group[i] = new Person(i,size, groupName);
      for( int j = 0; j < size; j++){
        group[i].p_list[j] = group[i].new Preferences();
      }
    }
    return group;
  }

// clears out all temporary data, such as match and whether they are engaged or not
// from Person object
 public void resetMatchData(){
     this.match = -1;
     this.engaged = false;
     for(int i = 0; i < p_list.length; i++)
        p_list[i].proposedTo = false;
  }

//top level matching function
 public void askNext(Person[] our_group,Person[] other_group){
   int match_index = this.setNextMatch();
   int match_name = this.getMatch();
   int match_home_index = getMatchIndex(match_name, other_group);
   boolean match_engaged = other_group[match_home_index].isEngaged();

   if( !match_engaged ){
      this.setEngagement(this.name, match_index, match_name, our_group, other_group, match_engaged);
      this.p_list[match_index].proposedTo = true;
      }
   else{
       if( other_group[match_index].isHigherMatch(this.name) ){
           this.setEngagement(this.name, match_index, match_name, our_group, other_group, match_engaged);
           this.p_list[match_index].proposedTo = true;
       }
       else{
           this.p_list[match_index].proposedTo = true;
       }
   }
 }

// performs the engagement process
 private void setEngagement(int this_name, int other_index, int match_name,Person[] our_group,Person[] other_group, boolean other_engaged){
   int match_home_index = getMatchIndex(match_name, other_group);

   //if match is engaged we must first reverse the engagment
   // we mark the match's betrothed to be not engaged
   if( other_engaged ){
      int current_name = other_group[match_home_index].getMatch();
      int current_index = getMatchIndex(current_name,our_group);
      our_group[current_index].engaged = false;
   }

  // now we engage the two objects
  // we set their engaged variables
  // and set their match variables to the p_list element which contains
  // their match's name

  //first with the match object
   other_group[match_home_index].engaged = true;
   for( int i = 0; i < other_group[match_home_index].p_list.length; i++){
       if( this_name == other_group[match_home_index].p_list[i].name ){
           other_group[match_home_index].match = i;
           break;
       }
   }

   //continue with remaining object
   this.engaged = true;
   this.match = other_index;
   this.p_list[other_index].proposedTo = true;
 }

//checks if all in group are engaged
  public static boolean allEngaged(Person[] people){
    for(int i = 0; i < people.length ; i++){
      if (people[i].isEngaged()== false){
          //System.out.println("allEngaged: false @ " + people[i].getGroupName() + "[" + i + "]");
          return false;
      }

    }
    return true;
  }


// used to compare place in preference list
   public boolean isHigherMatch(int comp){
    int comp_index = 0;
    for(int c = 0; c < this.p_list.length; c++){
      if(this.p_list[c].name == comp)
        comp_index = c;
    }
    if (comp_index < match)
        return true;
    else
        return false;
  }

  public static ArrayList<Integer> matchPermutation(Person[] group){
    ArrayList<Integer> ret = new ArrayList<Integer>(group.length);
    for(int i = 0; i < group.length ; i++){
      ret.add(group[i].name-1, group[i].p_list[group[i].match].name );
    }
    return ret;
  }

  //function to load permutation
  //into our Person objects
  public static void loadPermutation(ArrayList<Integer> permutation, Person[] group, Person[] other_group){
      //hold the name of group[i].match
      int other_group_index = 0;
      for(int i = 0; i < group.length ; i++){
          group[i].match = permutation.get(i);
          group[i].engaged = true;
          other_group_index = group[i].p_list[group[i].match].name;
          other_group[other_group_index-1].match = other_group[other_group_index-1].getPositionFromName(group[i].name);
          other_group[other_group_index-1].engaged = true;

          //System.out.println("+++ " + i + " +++");
          //System.out.println(permutation);
          //group[i].printContents();
          //other_group[i].printContents();

      }

  }

// use the two given permutations to generate all permutations between them
    public static ArrayList<ArrayList<Integer>> generatePermutations(ArrayList<Integer> m_first,ArrayList<Integer> w_first, Person[] men, Person[] women){
      ArrayList<Integer> ul = new ArrayList<Integer>(w_first.size());
      ArrayList<Integer> ll = new ArrayList<Integer>(m_first.size());


      //ul will swap the index and elements of w_first
      //so the matching will appear as though it came from the pov
      //of the other group
     for( int i = 0; i < w_first.size() ;i++){
         ul.add(0);
         ll.add(0);
     }

     for( int i = 0; i < m_first.size();i++){
         ll.set(i, men[i].getPositionFromName(m_first.get(i)));
     }

     //convert w_first to man's point of view wrt to matches
     //convert the names to p_list positions
     //System.out.println(ll);
      for(int i = 0; i < w_first.size() ; i++){
          ul.set(w_first.get(i)-1, men[w_first.get(i)-1].getPreference(i+1));

      }

      //System.out.println(ul);

      //total number of permutations
      int num_perm = 1;

      for(int i = 0; i < ul.size(); i++){
          num_perm *= ul.get(i) - ll.get(i)+1;
      }
      //System.out.println("total number of permutations: " + num_perm);

      ArrayList<ArrayList<Integer>> permutations = new ArrayList<ArrayList<Integer>>(num_perm);

      permutations.add(ll);
      for(int i = 0; i<num_perm-1 ;i++){
          //System.out.println(permutations.get(0));
          permutations.add(inc(permutations.get(i), ll.size()-1, ll, ul));
      }


    return permutations;
    }

    //helper function for generatePermutations
    //creates each individual permutation
    //to be called inside for loop in generatePermutations
    public static ArrayList<Integer> inc(ArrayList<Integer> arr_orig, int index, ArrayList<Integer> ll, ArrayList<Integer> ul){
        ArrayList<Integer> arr = new ArrayList<Integer>(arr_orig);

        //System.out.println(arr + "\t" + ll + "\t" + ul + "\t" + index);

        if (arr.equals(ul)){
            return arr;
        }


        if(arr.get(index) == ul.get(index)){
            //System.out.println(arr.get(index) + " " + ul.get(index));
            arr.set(index,ll.get(index));
            ArrayList<Integer> ret = inc(arr,index-1,ll,ul);
            return ret;

        }
        else
            arr.set(index, arr.get(index) + 1);
            return arr;



    }

    //once the two groups have their preference list and match variables loaded from the permutation array
    //we test the stability of that matching
    public static boolean stabilityTest(Person[] group, Person[] other_group){

        for(int i = 0; i < group.length; i++){
            for(int j = i+1; j < group.length; j++){
                if(group[i].p_list[group[i].match].name == group[j].p_list[group[j].match].name)
                    return false;
            }
        }


        int other_name = 0;
        for(int i = 0; i < group.length ;i++){
            //group[i].printContents();
            if( group[i].match > 0)
              // for loop starts from 1st better choice on preference list
              // which is we subtract 1 from the match number
                for(int j = group[i].match-1; j >= 0; j--){
                    other_name = group[i].p_list[j].name;
                    //other_group[other_name-1].printContents();
                    if(other_group[other_name-1].isHigherMatch(other_group[other_name-1].getPositionFromName(group[i].name))){
                        return false;
                    }

                }

        }

        return true;

    }
      //helper functions
       public void printContents(){
           System.out.println("groupName: " + this.groupName + "\nname: " + this.name + "\nmatch: " + this.match +  "\nengaged: " + this.engaged + "\np_list: ");
         for ( int i = 0; i < p_list.length; i++){
           System.out.println("\t" + p_list[i].name +" proposed: " + p_list[i].proposedTo );
         }
       }

       public String getGroupName(){
         return groupName;
       }

       public boolean isEngaged(){
         return engaged;
       }



       public int getMatch(){
         return p_list[match].name;
       }

       public int setNextMatch(){

          // explicitly set match to zero for first calling of function
          if( match == -1){
              match = 0;
              return match;
          } else if( match < p_list.length -1 && engaged != true){
              return ++match;
          } else if(engaged == true){
              return match;
          }
          return -1;
       }

       //meant to be used at the beginning of algorithm execution
       //so we can assume the person being matched too isn't engaged and hasn't been proposed to
       public int setMatch(int i){
           int ret = 0;
           if( i > this.p_list.length || i < 0 )
                ret = -1;
           this.match = i;
           this.engaged = false;
           return ret;
       }

       public int getMatchIndex(int name, Person[] group){
           for (int i = 0 ; i < group.length; i++){
               if(group[i].name == name )
                return i;
           }
           return -1;
       }

       public int getPositionFromName(int name){
           for(int i = 0; i < p_list.length ; i++){
               if(p_list[i].name == name)
                    return i;
           }
           return -1;
       }

       public int getPreference(int name){
           for( int i = 0; i<p_list.length ;i++){
               if( name == p_list[i].name)
                return i;
           }
           return -1;
       }

       public static void clearGroup(Person[] group){
           for(int i = 0; i < group.length ;i++)
                group[i].resetMatchData();
       }





}
