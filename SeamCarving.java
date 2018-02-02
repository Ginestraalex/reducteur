package reducteur;

import java.io.*;
import java.util.*;


public class SeamCarving
{

	public SeamCarving(){
		
	}
	
	
	public static int[][] readpgm(String fn)
	 {		
        try {
            InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
            BufferedReader d = new BufferedReader(new InputStreamReader(f));
            String magic = d.readLine();
            String line = d.readLine();
		   while (line.startsWith("#")) {
			  line = d.readLine();
		   }
		   Scanner s = new Scanner(line);
		   int width = s.nextInt();
		   int height = s.nextInt();		   
		   line = d.readLine();
		   s = new Scanner(line);
		   int maxVal = s.nextInt();
		   int[][] im = new int[height][width];
		   s = new Scanner(d);
		   int count = 0;
		   while (count < height*width) {
			  im[count / width][count % width] = s.nextInt();
			  count++;
		   }
		   return im;
        }
        catch(Throwable t) {
            t.printStackTrace(System.err) ;
            return null;
        }
        }

	
   public static void writepgm(int[][] image, String filename){
	   try {
		   Writer writer = new BufferedWriter(new FileWriter(filename));
		   
		   writer.write("P2\n");
		   writer.write(""+image[0].length);
		   writer.write(" "+image.length);
		   writer.write("\n255");
		   for(int i = 0 ; i < image.length ; i++) {
			   writer.write("\n");
			   for(int j = 0 ; j < image[0].length ; j++) {
				   writer.write(image[i][j]+" ");
			   }
		   }
		   writer.close();
		   }
		   catch (IOException e)
		   {
		   e.printStackTrace();
		   }
   }
   
   
   
   
   public static int[][] interest(int[][] image){
	   int[][] res = new int[image.length][image[0].length];
	   int moyenne;
	   
	   for(int i = 0 ; i < res.length ; i ++) {
		   for(int j = 0 ; j < res[0].length ; j++) {
			   if(j == 0) {
				   moyenne = image[i][1];
			   }
			   else if(j == res[0].length-1) {
				   moyenne = image[i][j-1];
			   }
			   else {
				   moyenne = (image[i][j-1] + image[i][j+1])/2;
			   }
			   res[i][j] = Math.abs(moyenne - image[i][j]);
			   System.out.print(res[i][j]+" ");
		   }
		   System.out.println("");
	   }
	   
	   return res;
   }
   
   
   public static Graph tograph(int[][] itr) { 
	   int hauteur = itr.length;
	   int largeur = itr[0].length;
	   Graph graph = new Graph(largeur*hauteur+2); //+2 expliqué par départ + arriver
	   
	   for(int i = 0 ; i < largeur ; i++)
	   {
		   graph.addEdge(new Edge(0, i+1, 0)); //from the top to the first floor
	   }
	   
	   for(int i = 0 ; i < hauteur-1 ; i++) {
		   for(int j = 0 ; j < largeur ; j++) {
			   	if(j == 0){
			   		graph.addEdge(new Edge(i*largeur+1, (i+1)*largeur+1, itr[i][j]));
			   		graph.addEdge(new Edge(i*largeur+1, (i+1)*largeur+2, itr[i][j]));
			   	}
			   	else if(j == largeur-1) {
			   		graph.addEdge(new Edge(i*largeur+1+j, (i+1)*largeur+j, itr[i][j]));
			   		graph.addEdge(new Edge(i*largeur+1+j, (i+1)*largeur+1+j, itr[i][j]));
			   	}
			   	else{
			   		graph.addEdge(new Edge(i*largeur+1+j, (i+1)*largeur+j, itr[i][j]));
			   		graph.addEdge(new Edge(i*largeur+1+j, (i+1)*largeur+1+j, itr[i][j]));
			   		graph.addEdge(new Edge(i*largeur+1+j, (i+1)*largeur+2+j, itr[i][j]));
			   	}
		   }
	   }
	   
	   for(int i = 0 ; i < largeur ; i++) {
		   graph.addEdge(new Edge(largeur*(hauteur-1)+i+1, graph.vertices()-1, itr[hauteur-1][i]));
	   }
	   return graph;
   }
   
   
   public static ArrayList<Edge> Dijkstra1(Graph g, int s, int t) {
	   ArrayList<Edge> chemin= new ArrayList<Edge>();
	   Heap hp = new Heap(g.vertices());
	   boolean[] estVisite = new boolean[g.vertices()];
	   Edge[] parcours = new Edge[g.vertices()];
	   int ex = 0;
	   int parcouru = t;
	   // initialisation du tableau des sommets visités
	   for(int i = 0 ; i < g.vertices() ; i++) {
		   estVisite[i] = false;
	   }
	   hp.decreaseKey(0 , 0);
	   while( !( hp.isEmpty() ) ){
		   ex = hp.pop();
		   estVisite[ex] = true;
		   for(Edge ed : g.next(ex)) {
			   if( !( estVisite[ed.to] ) ) {
				   if( (hp.priority(ed.from) + ed.cost) < hp.priority(ed.to) ) {
					   hp.decreaseKey(ed.to , ed.cost + hp.priority(ed.from) );
					   parcours[ed.to] = ed;
				   }
			   }
		   }
	   }
	   while( s != parcouru) {
		  chemin.add(parcours[parcouru]);
		  parcouru = parcours[parcouru].from;
	   }
	   return chemin;
   }
   
   public static ArrayList<Integer> Dijkstra(Graph g, int s, int t) {
	   ArrayList<Integer> chemin= new ArrayList<Integer>();
	   Heap hp = new Heap(g.vertices());
	   boolean[] estVisite = new boolean[g.vertices()];
	   int[] parcours = new int[g.vertices()];
	   int ex = 0;
	   int parcouru = t;
	   // initialisation du tableau des sommets visités
	   for(int i = 0 ; i < g.vertices() ; i++) {
		   estVisite[i] = false;
	   }
	   hp.decreaseKey(0 , 0);
	   while( !( hp.isEmpty() ) ){
		   ex = hp.pop();

		   estVisite[ex] = true;
		   for(Edge ed : g.next(ex)) {
			   if( !( estVisite[ed.to] ) ) {
				   if( (hp.priority(ed.from) + ed.cost) < hp.priority(ed.to) ) {
					   hp.decreaseKey(ed.to , ed.cost + hp.priority(ed.from) );
					   parcours[ed.to] = ed.from;
				   }
			   }
		   }
	   }
	   while( s != parcouru) {
		  chemin.add(parcours[parcouru]);
		  parcouru = parcours[parcouru];
	   }
	   return chemin;
   }
   
   
   public void reducePict(String nameFile) {
	   int[][] im = this.readpgm(nameFile);
	   for(int i = 0 ; i < im.length ; i++) {
		   for(int j = 0 ; i < im[0].length ; j++) {
			   System.out.println(im[i][j]+"  ");
		   }
		   System.out.println("");
	   }
	   Graph graph = this.tograph(this.interest(im));
		ArrayList<Integer> array = this.Dijkstra(graph, 0, graph.vertices()-1);
		int[][] newIm = new int[im.length][im[0].length-1];
		
		System.out.println("");

		int k;
		int l = array.size()-1;
		for(int i = 0 ; i < im.length ; i++) {
			for(int j = 0 ; j < im[0].length ; j++) {
				k = 0;
				if(l > 0 && i*im[0].length+j == array.get(l-1)-1) {
					l--;
					k++;
				}
				else {
					newIm[i][k] = im[i][j];
					System.out.print(newIm[i][k]+" ");
					k++;
				}
			}
			System.out.println("");
		}
		this.writepgm(newIm, "monFichier.pgm");
   }
 
   
   public static void main(String... args){
	   SeamCarving sc = new SeamCarving();
	   sc.reducePict("./image/test.pgm");
	   
	   
	   
	   
	   
	   
		/*int[][] im = new int[3][4];
		for(int i = 0 ; i < im.length ; i++) {
			for (int j = 0 ; j < im[0].length ; j++) {
				im[i][j] = i+2*j;
				System.out.print(im[i][j]+" ");
			}
			System.out.print("\n");
		}
		System.out.print("\n");

		Graph graph = sc.tograph(sc.interest(im));
		ArrayList<Integer> array = sc.Dijkstra(graph, 0, graph.vertices()-1);
		int[][] newIm = new int[im.length][im[0].length-1];
		
		
		int k;
		int l = array.size()-1;
		for(int i = 0 ; i < im.length ; i++) {
			for(int j = 0 ; j < im[0].length ; j++) {
				k = 0;
				if(l > 0 && i*im[0].length+j == array.get(l-1)-1) {
					l--;
					k++;
				}
				else {
					newIm[i][k] = im[i][j];
					System.out.print(newIm[i][k]);
					k++;
				}
			}
			System.out.println("");
		}
		sc.writepgm(newIm, "monFichier.pgm");*/

   }
}



