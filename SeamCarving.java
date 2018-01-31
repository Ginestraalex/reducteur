package reducteur;

import java.io.*;
import java.util.*;


public class SeamCarving
{

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

   private static void writepgm(int[][] image, String filename){
	   try {
		   DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(filename))));
		   
		   for(int i = 0 ; i < image.length ; i++) {
			   for(int j = 0 ; j < image[0].length ; j++) {
				   writer.writeInt(image[i][j]);
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
   
   public Graph tograph(int[][] itr) {
	   Graph res = new Graph(itr[0].length*itr.length+2);
	   
	   return res;
	   
   }
 
   
   public static void main(String... args){
		int[][] im = new int[3][4];
		for(int i = 0 ; i < im.length ; i++) {
			for (int j = 0 ; j < im[0].length ; j++) {
				im[i][j] = i+2*j;
				System.out.print(im[i][j]+" ");
			}
			System.out.print("\n");
		}
		
		SeamCarving.writepgm(im, "monFichier");
		SeamCarving.interest(im);
		
   }
}



