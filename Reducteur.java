package reducteur;

import java.awt.Component;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import reducteur.Graph;
import reducteur.SeamCarving;

public class Reducteur {
	
	public Reducteur() {
		
	}
	
	public static void main(String... args) {
		SeamCarving sc = new SeamCarving();
		if(args.length == 0) {
		    Component parent = null;
			JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "PGM Images", "pgm");
		    chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(parent);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    		/*String[] selection = {"1","2","3","4","5","6","7","8","9"};
			    	String retour = (String) JOptionPane.showInputDialog(null, "Choisissez un indice", "Boite de dialogue",JOptionPane.QUESTION_MESSAGE, null, selection, selection[0]);
			    	if(retour != null) {
			    		sc.reducePict(chooser.getSelectedFile().getPath(), true, Integer.parseInt(retour));
					JOptionPane.showMessageDialog(null, "Transformation de l'image terminé");
			    	}*/
		    		Graph g = sc.tograph(sc.readpgm(chooser.getSelectedFile().getPath(), true));
		    		g.writeFile("graphAvant");;
		    		sc.twopath(g, 0, g.vertices()-1);
		    		g.writeFile("graphApres");
		    		
		    }
		}
		else if(args.length == 1){
			sc.reducePict(args[0], false, 1);
			JOptionPane.showMessageDialog(null, "Transformation de l'image terminé");
		}
		else if(args.length == 2) {
			sc.reducePict(args[0], false, Integer.parseInt(args[1]));
			JOptionPane.showMessageDialog(null, "Transformation de l'image terminé");
		}
		else {
			JOptionPane.showMessageDialog(null, "vous devez faire:\n java -jar nomProgramme.jar nomfichier nbDePixelASupp");
		}
	}
}
