package reducteur;

import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;


public class SeamCarving
{

	public SeamCarving(){
		
	}
	
	
	public static int[][] readpgm(String fn, boolean absolutePath)
	 {		
        try {
            InputStream f;
        	if(absolutePath) {
        		f = new FileInputStream(fn);
        	}
        	else {
        		f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);

        	}
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

	/*
	 * fonction qui creer un fichier
	 * image a partir du tableau image 
	 * qui passe en parametre
	 */
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
   
   
   
   
   public static int[][] interestHorizontal(int[][] image){
	   int[][] res = new int[image.length][image[0].length];
	   int moyenne;
	   
	   for(int i = 0 ; i < res[0].length ; i ++) {
		   for(int j = 0 ; j < res.length ; j++) {
			   if(j == 0) {
				   moyenne = image[1][i];
			   }
			   else if(j == res.length-1) {
				   moyenne = image[j-1][i];
			   }
			   else {
				   moyenne = (image[j-1][i] + image[j+1][i])/2;
			   }
			   res[j][i] = Math.abs(moyenne - image[j][i]);
		   }
	   }
	   
	   return res;
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
		   }
	   }
	   
	   return res;
   }
   
  
   /*
    * fonction qui transforme un tableau itr
    * contenant les valeurs d'importance d'une image
    * en un graphe orienté pour supprimer des lignes
    * de manière verticale
    */
   public static Graph tograph(int[][] itr) { 
	   int hauteur = itr.length;
	   int largeur = itr[0].length;
	   
	   Graph graph = new Graph(largeur*(hauteur*2-2)+2); //hauteur * 2 pour la subdivision et -2 pour le sommet de depart et de fin
	   
	   for(int i = 0 ; i < largeur ; i++)
	   {
		   graph.addEdge(new Edge(0, i+1, 0)); //from the top to the first floor
	   }
	   /* premiere ligne	*/
	   for(int i = 0 ; i < largeur ; i++) {
		   int sommet = i+1;
		   if(i == 0){
		   		graph.addEdge(new Edge(1, largeur+1, itr[0][i]));
		   		graph.addEdge(new Edge(1, largeur+2, itr[0][i]));
		   	}
		   	else if(i == largeur-1) {
		   		graph.addEdge(new Edge(sommet, largeur+sommet, itr[0][i]));
		   		graph.addEdge(new Edge(sommet, largeur+sommet-1, itr[0][i]));
		   	}
		   	else{
		   		graph.addEdge(new Edge(sommet, largeur+i, itr[0][i]));
		   		graph.addEdge(new Edge(sommet, largeur+sommet, itr[0][i]));
		   		graph.addEdge(new Edge(sommet, largeur+sommet+1, itr[0][i]));
		   	}
	   }
	   /* lignes a doubler */
	   for(int i = 1 ; i < hauteur-1 ; i++) {
		   for(int j = 0 ; j < largeur ; j++) {
			   int sommet = (2*i*largeur)+1+j;
			   /* cote gauche du graphe */
			   	if(j == 0){ 
			   		/* premiere ligne egal a 0 */
			   		graph.addEdge(new Edge(sommet-largeur, sommet, 0));
			   		/* seconde ligne avec les valeurs de itr*/
			   		graph.addEdge(new Edge(sommet, sommet+largeur, itr[i][j]));
			   		graph.addEdge(new Edge(sommet, sommet+largeur+1, itr[i][j]));
			   	}
			   	/* partie droite du graphe */
			   	else if(j == largeur-1) {
			   		/* premiere ligne egale a 0 */
			   		graph.addEdge(new Edge(sommet-largeur, sommet, 0));
			   		/* seconde ligne contenant les valeurs de itr */
			   		graph.addEdge(new Edge(sommet, sommet+largeur, itr[i][j]));
			   		graph.addEdge(new Edge(sommet, sommet+largeur-1, itr[i][j]));
			   	}
			   	/* partie centrale du graphe */
			   	else{
			   		/* premiere ligne egale a 0 */
			   		graph.addEdge(new Edge(sommet-largeur, sommet, 0));
			   		/* seconde ligne contenant les valeurs de itr */
			   		graph.addEdge(new Edge(sommet, sommet+largeur, itr[i][j]));
			   		graph.addEdge(new Edge(sommet, sommet+largeur+1, itr[i][j]));
			   		graph.addEdge(new Edge(sommet, sommet+largeur-1, itr[i][j]));
			   	}
		   }
	   }
	   for(int i = 0 ; i < largeur ; i++) {
		   graph.addEdge(new Edge(largeur*(hauteur*2-3)+i+1, graph.vertices()-1, itr[hauteur-1][i]));
	   }
	   return graph;
   }
   
   
   
   /*
    * fonction qui transforme un tableau itr
    * contenant les valeurs d'importance d'une image
    * en un graphe orienté pour supprimer des lignes
    * de manière horizontale
    */
   public static Graph tographHorizontal(int[][] itr) { 
	   int hauteur = itr.length;
	   int largeur = itr[0].length;
	   Graph graph = new Graph(largeur*hauteur+2); //+2 expliqué par sommet départ + sommet arriver
	   
	   for(int i = 0 ; i < hauteur ; i++)
	   {
		   graph.addEdge(new Edge(0, i+1, 0)); //from the top to the first floor
	   }
	   
	   for(int i = 0 ; i < largeur-1 ; i++) {
		   for(int j = 0 ; j < hauteur ; j++) {
			   	if(j == 0){
			   		graph.addEdge(new Edge(i*hauteur+1, (i+1)*hauteur+1, itr[j][i]));
			   		graph.addEdge(new Edge(i*hauteur+1, (i+1)*hauteur+2, itr[j][i]));
			   	}
			   	else if(j == hauteur-1) {
			   		graph.addEdge(new Edge(i*hauteur+1+j, (i+1)*hauteur+j, itr[j][i]));
			   		graph.addEdge(new Edge(i*hauteur+1+j, (i+1)*hauteur+1+j, itr[j][i]));
			   	}
			   	else{
			   		graph.addEdge(new Edge(i*hauteur+1+j, (i+1)*hauteur+j, itr[j][i]));
			   		graph.addEdge(new Edge(i*hauteur+1+j, (i+1)*hauteur+1+j, itr[j][i]));
			   		graph.addEdge(new Edge(i*hauteur+1+j, (i+1)*hauteur+2+j, itr[j][i]));
			   	}
		   }
	   }
	   
	   for(int i = 0 ; i < hauteur ; i++) {
		   graph.addEdge(new Edge(hauteur*(largeur-1)+i+1, graph.vertices()-1, itr[i][largeur-1]));
	   }
	   return graph;
   }
   
   
   public ArrayList<Edge> twopath(Graph g, int s, int t){
	   int edgeTemp;
	   ArrayList<Edge> listeRes = Dijkstra1(g, s, t);
	   Iterable<Edge> listeEdgeGraph = g.edges();
	   for(Edge e : listeEdgeGraph) {
		   if(listeRes.contains(e)) {
			   System.out.println("from: "+e.from+" to: "+e.to);
			   edgeTemp = e.to;
			   e.to = e.from;
			   e.from = edgeTemp;
		   }
	   }
	   ArrayList<Edge> liste2 = Dijkstra1(g, s, t);
	   for(Edge e : listeEdgeGraph) {
		   if(liste2.contains(e)) {
			   System.out.println("from: "+e.from+" to: "+e.to);
			   edgeTemp = e.to;
			   e.to = e.from;
			   e.from = edgeTemp;
		   }
	   }
	   for(Edge e : liste2) {
		   if(!listeRes.contains(e)) {
			   listeRes.add(e);
		   }
	   }
	   return listeRes;
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
   
   
   public void reducePict(String nameFile, boolean isAbsolutePath, int nbFois) {
	   int[][] im = this.readpgm(nameFile, isAbsolutePath);
	   if(nbFois >= im[0].length) {
		   JOptionPane.showMessageDialog(null,
				    "You are asking impossible things (too much times for the picture's size).",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
	   }
	   else {
		   int[][] newIm = null;
		   for(int r = 0 ; r < nbFois ; r++) {
			   Graph graph = this.tograph(this.interest(im));
			   ArrayList<Integer> array = this.Dijkstra(graph, 0, graph.vertices()-1);
			   newIm = new int[im.length][im[0].length-1];
				
		
				int k;
				int l = array.size()-1;
				for(int i = 0 ; i < im.length ; i++) {
					k = 0;
					for(int j = 0 ; j < im[0].length ; j++) {
						if(l > 0 && i*im[0].length+j == array.get(l-1)-1) {
							l--;
						}
						else {
							newIm[i][k] = im[i][j];
							k++;
						}
					}
				}
				im = newIm;
		   	}
			writepgm(newIm, "monFichier.pgm");
	   }
   }
 
   public void reducePictHorizontal(String nameFile, boolean isAbsolutePath, int nbFois) {
	   int[][] im = this.readpgm(nameFile, isAbsolutePath);
	   if(nbFois >= im.length) {
		   JOptionPane.showMessageDialog(null,
				    "You are asking impossible things (too much times for the picture's size).",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
	   }
	   else {
		   int[][] newIm = null;
		   for(int r = 0 ; r < nbFois ; r++) {
			   Graph graph = this.tographHorizontal(this.interestHorizontal(im));
			   ArrayList<Integer> array = this.Dijkstra(graph, 0, graph.vertices()-1);
			   newIm = new int[im.length-1][im[0].length];
				
		
				int k;
				int l = array.size()-1;
				for(int i = 0 ; i < im[0].length ; i++) {
					k = 0;
					for(int j = 0 ; j < im.length ; j++) {
						if(l > 0 && i*im.length+j == array.get(l-1)-1) {
							l--;
						}
						else {
							newIm[k][i] = im[j][i];
							k++;
						}
					}
				}
				im = newIm;
		   	}
			writepgm(newIm, "monFichier.pgm");
	   }
   }
   
   
   
   public static void augmentation(String nameFile, boolean isAbsolutePath,int augmentation) {
   	int[][] img = readpgm(nameFile,isAbsolutePath);
   	
   	if (augmentation < 0) {
   		System.err.println("L'augmentation doit être positive sinon sélectionner réduction");
   		System.exit(1);
   	}
   	
   	augmentation(img, augmentation);
   }
   /*
   public static void augmentation2(int[][] img, int augmentation) {
   		int hauteur = img.length;
	    int largeur = img[0].length;
	    	    
	    int source = largeur * hauteur;
	    int arrive = source + 1;	    	   	    	   	    
	    int[][] passage = new int[hauteur][largeur];
	    
	    for (int i = 0; i < hauteur; i ++) {
		    	for(int j = 0 ; j < largeur ; j ++) {
		    		passage[i][j] = 0;
		    	}
	    }
	    
	    int[][] itr = interest(img);
	    Graph g = tograph(itr); 
	    ArrayList<Integer> chemin = new ArrayList<Integer>();
	    
	    for (int i = 0; i < augmentation; i ++) {
	    	
		    	chemin = Dijkstra(g,source, arrive);	
		    	
		    	int dernier = chemin.get(0);
		    	Edge e = g.edge(dernier, arrive);
		    	
		    	e.cost = Integer.MAX_VALUE;
		    	
		    	for (int j = 0; j < chemin.size(); j ++) {
		    		int sommet = chemin.get(j);
		    		int y = sommet / largeur;
		    		int x = sommet % largeur;
		    		
		    		passage[y][x] ++;
		    	}
	    }

	    int p3 = 0;
	    int largeur2 = largeur + augmentation;
	    int[][] img2 = new int[hauteur][largeur2];    
	    
	    for (int p1 = 0; p1 < hauteur ; p1 ++) {
	    		for(int p2 = 0 ; p2 < largeur ; p2++) {
	    				int copies = passage[p1][p2] + 1;	    
	    	
	    				for (int c = 0; c < copies; c ++) {
	    					int y2 = p3 / largeur2;
	    					int x2 = p3 % largeur2;
		    	
	    					img2[y2][x2] = img[p1][p2];		    	
	    					p3 ++;
	    				}	    	
	    		}
	    }
	    
	    writepgm(img2, "Augmentationfichier.pgm");
   }
   */
   
   public static void augmentation(int[][] im, int augmentation) {
	   if(augmentation >= im.length) {
		   JOptionPane.showMessageDialog(null,
				    "You are asking impossible things (too much times for the picture's size).",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
	   }
	   else {
		   int[][] newIm = null;
		   for(int r = 0 ; r < augmentation ; r++) {
			   
			   	int[][] itr = interestHorizontal(im);
			    Graph g = tograph(itr); 
			    ArrayList<Integer> array = Dijkstra(g, 0, g.vertices()-1);
			    newIm = new int[im.length+1][im[0].length];
				
		
				int k;
				int l = array.size()-1;
				for(int i = 0 ; i < im[0].length ; i++) {
					k = 0;
					for(int j = 0 ; j < im.length ; j++) {
						if(l > 0 && i*im.length+j == array.get(l-1)-1) {
							l--;
							newIm[k][i] = im[j][i];
						}
						else {
							newIm[k][i] = im[j][i];
							k++;
						}
					}
				}
				im = newIm;
		   	}
			writepgm(newIm, "monFichierAugmente.pgm");
	   }
   }
   
   // Fonction qui definit une zone importante
   public static void dessinerZoneImportante(int[][] image, String name) {
	   int[][] tabS = image ;
	   /* Marquage de la zone importante */
	   if(tabS.length > 40) {
		   for (int x = 0 ;  x < tabS.length ; x++) {
			   for (int y = 0 ; y < tabS[0].length ; y++) {
				   if( x > (int)(tabS.length/4) && x < (tabS.length - (int)(tabS.length/4)) ) {
					   tabS[x][y] = 255 ;
				   }
			   }
		   }
	   }
	   // On affiche le resultat
	   writepgm(tabS,"image_issue_selection_"+name);
   }
   
   // Fonction qui definit une zone importante
   public static void dessinerZoneImportanteVerticale(int[][] image, String name) {
	   int[][] tabS = image ;
	   /* Marquage de la zone importante */
	   if(tabS[0].length > 40) {
		   for (int x = 0 ;  x < tabS.length ; x++) {
			   for (int y = 0 ; y < tabS[0].length ; y++) {
				   if( x > (int)(tabS[0].length/4) && x < (tabS[0].length - (int)(tabS[0].length/4)) ) {
					   tabS[x][y] = 255 ;
				   }
			   }
		   }
	   }
	   // On affiche le resultat
	   writepgm(tabS,"image_issue_selection_verticale_"+name);
   }
   
   
   /*public static void main(String... args){
	   SeamCarving sc = new SeamCarving();
	   sc.reducePict("./image/test.pgm", false);
	   
		int[][] im = new int[3][4];
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
		sc.writepgm(newIm, "monFichier.pgm");

   }*/
}



