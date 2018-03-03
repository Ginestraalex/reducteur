package reducteur;

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
		setPreferredSize(new Dimension(400,100));
		this.setLayout(new GridLayout(1,2));
		JButton agrandire = new JButton("Agrandissement");
		JButton reduire = new JButton("Reduction");
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
		
		add(reduire);
		add(agrandire);
		this.pack();
		setVisible(true);
	}
	
	public void agrandissement() {
		String chemin = selectionFichier();
	    if(chemin != null) {
	    		int nbPixel = this.selectionNbPixels();
	    		if(nbPixel != 0) {
    				SeamCarving.augmentation(chemin, true, nbPixel);
    				JOptionPane.showMessageDialog(null, "Transformation de l'image terminé");
	    			
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
	    
	
	public String selectionOrientation() {
		String[] selection = {"Horizontale", "Verticale"};
		
		String retour = (String) JOptionPane.showInputDialog(null, "Choisissez l'orientation de la modification:", "Boite de dialogue",JOptionPane.QUESTION_MESSAGE, null, selection, selection[0]);
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
