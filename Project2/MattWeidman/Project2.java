import java.util.*;
import java.io.*;
import java.lang.Math;


class Project2{
	public static void main(String args[]){


		System.out.println(read_and_find_shortest_distance("input.txt"));



	}

	public static int read_and_find_shortest_distance(String filename){
		List<Integer> oput= read_from_file(filename);
		//place values into list of type Point
		List<Point> point_list = Populate_point_list(oput);
		//sort in both X and Y
		// https://docs.oracle.com/javase/7/docs/api/java/util/Collections.html#sort(java.util.List,%20java.util.Comparator)
		// Implementation note: This implementation is a stable, adaptive, iterative mergesort that
		// requires far fewer than n lg(n) comparisons when the input array is partially sorted,
		// while offering the performance of a traditional mergesort when the input array is randomly ordered.
		// If the input array is nearly sorted, the implementation requires approximately n comparisons.
		// Temporary storage requirements vary from a small constant for nearly sorted input arrays to n/2 object references for
		// randomly ordered input arrays.
		Collections.sort(point_list, Point.X_comparator);
		List<Point> x_sorted = new ArrayList<Point>(point_list);
		Collections.sort(point_list, Point.Y_comparator);
		List<Point> y_sorted = new ArrayList<Point>(point_list);
		//call function to find shortest distance
		int hold = shortest_distance(x_sorted,y_sorted,x_sorted.size());

		//if only 1 point is in file
		if (hold == Integer.MAX_VALUE)
			return 0;
		else
			return hold;
	}


	public static List<Point> Populate_point_list(List<Integer> arr){
		List<Point> point_list= new ArrayList<Point>();

		Point hold = new Point();

		for( int i = 0; i < arr.size(); i += 2){
			hold = new Point(arr.get(i),arr.get(i+1));
			point_list.add(hold);
		}
		return point_list;
	}

	public static List<Integer> read_from_file(String filename){

		List<Integer> list = new ArrayList<Integer>();
		BufferedReader reader = null;

		// read input file
		// convert each string to tokens
		// then to integers
		// place in list
		try{
			reader = new BufferedReader(new FileReader(filename));
			String text = null;
			StringTokenizer st = null;
			while( (text = reader.readLine()) != null){
				st = new StringTokenizer(text);
				while(st.hasMoreTokens()){
					list.add(Integer.parseInt(st.nextToken()));
				}

			}

		}catch(FileNotFoundException e){
			System.out.println("file not found");
		}catch(IOException e){
			System.out.println("IO error");
		}
		return list;
	}

	// squared distance between points as per guidelines
	public static int distance_square(Point a, Point b){
		if (a.x == b.x && a.y == b.y )
			return Integer.MAX_VALUE;
		return (int)java.lang.Math.pow(a.x - b.x,2) + (int)java.lang.Math.pow(a.y - b.y,2);
	}

	//brute force method for small # of points
	public static int base_case(List<Point> x_sort){
		int smallest_distance = Integer.MAX_VALUE;
		int hold = 0;
		for(int i = 0; i < x_sort.size(); i++)
			for(int j = i; j < x_sort.size(); j++){
				hold = distance_square(x_sort.get(i), x_sort.get(j));
				if( hold < smallest_distance)
					smallest_distance = hold;
				}
		//System.out.println(smallest_distance);
		return smallest_distance;


	}

	public static int shortest_distance_strip(List<Point> strip){
		int sz = strip.size();
		int min_dist = Integer.MAX_VALUE;
		int hold = 0;
		for(int i = 0; i < sz; i++)
			for(int j = i+1; j < sz; j++){
				hold = distance_square(strip.get(i), strip.get(j));
				if( min_dist > hold)
					min_dist = hold;
			}
		return min_dist;
	}

	public static int shortest_distance(List<Point> x_sort, List<Point> y_sort, int n){
		//base case
		if (n <= 4){
			return base_case(x_sort);
		}
		//get midpt of sorted values
		int mid = x_sort.size()/2;
		Point midpoint = x_sort.get(mid-1);

		//need to split the given arrays into left and right half
		List<Point>y_left = new ArrayList<Point>(mid+1);
		List<Point>y_right= new ArrayList<Point>(n - (mid-1));

		// iteratively build the arrays
		// by comparing them against our midpoint
		// go through x_sort to build the halves
		for( int i = 0; i < n; i++){
			if (y_sort.get(i).x <= midpoint.x)
				y_left.add(y_sort.get(i));
			else
				y_right.add(y_sort.get(i));
		}

		int x_left_length = mid+1;
		int x_right_length = x_sort.size();

		//splitup the points into two halves
		// left side includes the midpoint and all points to the left of midpoint
		// right side is everything else
		List<Point> x_left = x_sort.subList(0,x_left_length);
		List<Point> x_right = x_sort.subList(x_left_length,x_right_length);


		//Point.showPointList("x_left size() = " + x_left.size(),x_left);
		//Point.showPointList("x_right",x_right);
		//recurse on the halves to get the smallest distances
		int left_smallest = shortest_distance(x_left, y_left, y_left.size());
		int right_smallest = shortest_distance(x_right, y_right, n - y_left.size());

		//get the smalleset distance of the two halves
		int min_dist = Math.min(left_smallest,right_smallest);

		//now check this distance against points near the center
		List<Point> comp_list = new ArrayList<Point>(n);
		for(int i = 0; i < x_sort.size(); i++){
			if( Math.abs(x_sort.get(i).x - midpoint.x) < min_dist)
				comp_list.add(x_sort.get(i));
		}
		int strip_dist = shortest_distance_strip(comp_list);

		return Math.min(min_dist,strip_dist);
	}




}
