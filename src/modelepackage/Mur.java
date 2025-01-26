package modelepackage;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controleur.Global;

/**
 * Gestion des murs
 *
 */
public class Mur extends Objet implements Global {

	/**
	 * Constructeur : crée un mur (position aléatoire, image)
	 */
	public Mur() {
		// calcul position aléatoire du mur
		posX = (int) Math.round(Math.random() * (controleur.Global.LARGEURARENE - controleur.Global.LARGEURMUR)) ;
		posY = (int) Math.round(Math.random() * (controleur.Global.HAUTEURARENE - controleur.Global.HAUTEURMUR)) ;
		// création du jLabel pour ce mur
		jLabel = new JLabel();
		// caractéristiques du mur (position, image)
		jLabel.setBounds(posX, posY, controleur.Global.LARGEURMUR, controleur.Global.HAUTEURMUR);
		URL resource = getClass().getClassLoader().getResource(controleur.Global.MUR);
		jLabel.setIcon(new ImageIcon(resource));

	}
	
}
