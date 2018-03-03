
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.filechooser.FileNameExtensionFilter;

import reducteur.Graph;
import reducteur.SeamCarving;

public class Reducteur extends JFrame{
	
	public Reducteur() {
		setPreferredSize(new Dimension(500,100));
		this.setLayout(new GridLayout(1,3));
		JButton agrandire = new JButton("Agrandissement");
		JButton reduire = new JButton("Réduction");
		JButton zone = new JButton("Reduction Zone Ciblé");
		agrandire.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				agrandissement();
			}
		});
		
		reduire.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reduction();
			}
		});
		
		zone.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reductionZone();
			}
			
		});
		
		add(reduire);
		add(agrandire);
		add(zone);
		this.pack();
		setVisible(true);
	}
	
	public void agrandissement() {
		String chemin = selectionFichier();
	    if(chemin != null) {
	    		int nbPixel = this.selectionNbPixels();
	    		if(nbPixel != 0) {
	    			String orientation = selectionOrientation();
	    			if(orientation != null && orientation.equals("Verticale")) {
	    				SeamCarving.augmentationVertical(chemin, true, nbPixel);
	    				JOptionPane.showMessageDialog(null, "Transformation de l'image terminé");
	    			}
	    			else if(orientation != null) {
	    				SeamCarving.augmentationHorizontal(chemin, true, nbPixel);
	    				JOptionPane.showMessageDialog(null, "Transformation de l'image terminé");
	    			}
    			}
	    }
	}
	
	public void reduction() {
		String chemin = selectionFichier();
	    if(chemin != null) {
	    		int nbPixel = this.selectionNbPixels();
	    		if(nbPixel != 0) {
	    			String orientation = selectionOrientation();
	    			if(orientation != null && orientation.equals("Verticale")) {
	    				SeamCarving.reducePict(chemin, true, nbPixel);
	    				JOptionPane.showMessageDialog(null, "Transformation de l'image terminé");
	    			}
	    			else if(orientation != null) {
	    				SeamCarving.reducePictHorizontal(chemin, true, nbPixel);
	    				JOptionPane.showMessageDialog(null, "Transformation de l'image terminé");
	    			}
    			}
	    }
	}
	    
	
	public void reductionZone() {
		String chemin = selectionFichier();
	    if(chemin != null) {
	    		int nbPixel = selectionNbPixels();
	    		if(nbPixel != 0) {
		    		String coordx = JOptionPane.showInputDialog(null,"Veuillez entrez la coordonnée x1 qui sera le haut/gauche le zone de suppression", "Informations Requises", JOptionPane.QUESTION_MESSAGE);
		    		if(coordx != null) {
			    		String coordy = JOptionPane.showInputDialog(null,"Veuillez entrez la coordonnée x2 qui sera le bas/droite le zone de suppression", "Informations Requises", JOptionPane.QUESTION_MESSAGE);
			    		if(coordy != null) {
			    			String orientation = selectionOrientation();
			    			if(orientation != null && orientation.equals("Verticale")) {
			    				SeamCarving.supprimerZoneVertical(chemin, true, nbPixel, Integer.parseInt(coordx), Integer.parseInt(coordy));
			    			}
			    			else if(orientation != null) {
			    				SeamCarving.supprimerZoneHorizontal(chemin, true, nbPixel, Integer.parseInt(coordx), Integer.parseInt(coordy));
			    			}
			    		}
		    		}
	    		}
	    }
	}
	
	
	public void dessinerZoneImportante() {
		String chemin = selectionFichier();
	    if(chemin != null) {
	    		int nbPixel = selectionNbPixels();
	    		if(nbPixel != 0) {
		    		String coordx1 = JOptionPane.showInputDialog(null,"Veuillez entrez la coordonnée x1 qui sera le x du coin haut gauche le zone de suppression", "Informations Requises", JOptionPane.QUESTION_MESSAGE);
		    		if(coordx1 != null) {
			    		String coordy1 = JOptionPane.showInputDialog(null,"Veuillez entrez la coordonnée y1 qui sera le y du coin haut gauche le zone de suppression", "Informations Requises", JOptionPane.QUESTION_MESSAGE);
			    		if(coordy1 != null) {
				    		String coordx2 = JOptionPane.showInputDialog(null,"Veuillez entrez la coordonnée x2 qui sera le x du coin bas droit le zone de suppression", "Informations Requises", JOptionPane.QUESTION_MESSAGE);
		    				if(coordx2 != null) {
		    					String coordy2 = JOptionPane.showInputDialog(null,"Veuillez entrez la coordonnée x2 qui sera le x du coin bas droit le zone de suppression", "Informations Requises", JOptionPane.QUESTION_MESSAGE);
		    					if(coordy2 != null) {
					    			SeamCarving.dessinerZoneImportante(chemin, true, nbPixel, Integer.parseInt(coordx1), Integer.parseInt(coordx2), Integer.parseInt(coordy1), Integer.parseInt(coordy2));
		    					}
		    				}
			    		}
		    		}
	    		}
	    }
	}
	
	public String selectionOrientation() {
		String[] selection = {"Horizontale", "Verticale"};
		
		String retour = (String) JOptionPane.showInputDialog(null, "Choisissez l'orientation de la modification:", "Boite de dialogue",JOptionPane.QUESTION_MESSAGE, null, selection, selection[0]);
		return retour;
	}
	
	public String selectionZone() {
		String[] selection = {"A conserver", "A supprimer"};
		String retour = (String) JOptionPane.showInputDialog(null, "Choisissez la zone de l'action:", "Boite de dialogue",JOptionPane.QUESTION_MESSAGE, null, selection, selection[0]);
		return retour;
	}
	
	
	public String selectionFichier() {
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("PGM Images", "pgm");
	    chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getPath();
		}
		return null;
	}
	
	public int selectionNbPixels() {
		String[] selection = {"1","2","3","4","5","6","7","8","9", "10",
				"11","12","13","14","15","16","17","18","19", "20",
				"21","22","23","24","25","26","27","28","29", "30",
				"31","32","33","34","35","36","37","38","39", "40",
				"41","42","43","44","45","46","47","48","49", "50"};
		
		String retour = (String) JOptionPane.showInputDialog(null, "Choisissez un indice de modification\n(nombre de Pixel)", "Boite de dialogue",JOptionPane.QUESTION_MESSAGE, null, selection, selection[0]);
		if(retour != null) {
			return Integer.parseInt(retour);
		}
		return 0;
	}
	    
	public static void main(String... args) {
		
		if(args.length == 0) {
			Reducteur red = new Reducteur();
		}
		else if(args.length == 1){
			SeamCarving.reducePict(args[0], false, 1);
			JOptionPane.showMessageDialog(null, "Transformation de l'image terminé");
		}
		else if(args.length == 2) {
			SeamCarving.reducePict(args[0], false, Integer.parseInt(args[1]));
			JOptionPane.showMessageDialog(null, "Transformation de l'image terminé");
		}
		else {
			JOptionPane.showMessageDialog(null, "vous devez faire:\n java -jar nomProgramme.jar cheminAbsolu/nomfichier nbDePixelASupp");
		}
	}
}
