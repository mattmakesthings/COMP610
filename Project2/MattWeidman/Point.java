import java.util.Comparator;
import java.util.List;

public class Point{
	public int x;
	public int y;

	public Point(){
		x = 0;
		y = 0;
	}


	public Point(int x0, int y0){
		x = x0;
		y = y0;
	}

	public void show(){
		System.out.println(this.x + ", " + this.y);
	}

	public static void showPointList(List<Point> lst){
		for(int i = 0; i < lst.size(); i++)
			lst.get(i).show();
		System.out.println();
	}

	public static void showPointList(String message, List<Point> lst){
		System.out.println(message);
		for(int i = 0; i < lst.size(); i++)
			lst.get(i).show();
		System.out.println();
	}

	//Comparator
	public static Comparator<Point> X_comparator = new Comparator<Point>(){

			public int compare(Point a, Point b){
				return a.x - b.x;
			}
	};

	public static Comparator<Point> Y_comparator = new Comparator<Point>(){

			public int compare(Point a, Point b){
				return a.y - b.y;
			}
	};


}
