package reducteur;

import java.util.ArrayList;

public class Reducteur {
	
	public Reducteur() {
		
	}
	
	public static void main(String... args) {
		if(args.length != 1) {
			System.out.println("Vous devez donnez le nom d'un fichier en paramètre");
		}
		else{
			SeamCarving sc = new SeamCarving();
			int[][] tabImage = sc.readpgm(args[0]);
			int[][] itr = sc.interest(tabImage);
			Graph gr = sc.tograph(itr);
			ArrayList<Edge>parcourt = sc.Dijkstra(gr, 0, gr.vertices()-1);
		}
	}
}
