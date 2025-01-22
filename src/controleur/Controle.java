package controleur;

import outilspackage.AsyncResponse;
import outilspackage.ClientSocket;
import outilspackage.Connection;
import outilspackage.ServeurSocket;
import vue.Arene;
import vue.ChoixJoueur;
import vue.EntreeJeu;

public class Controle implements outilspackage.AsyncResponse {
	/**
	 * N° du port d'écoute du serveur
	 */
	private static final int PORT = 6666;
	/**
	 * frame EntreeJeu
	 */
	private EntreeJeu frmEntreeJeu;
	/**
	 * frame Arene
	 */
	private Arene frmArene;
	/**
	 * frame ChoixJoueur
	 */
	private ChoixJoueur frmChoixJoueur;
	/**
	 * type du jeu : client ou serveur
	 */
	private String typeJeu;
	/**
	 * Constructeur
	 */
	private Controle() {
		this.frmEntreeJeu = new EntreeJeu(this);
		this.frmEntreeJeu.setVisible(true);
	}

	/**
	 * Méthode de démarrage
	 * 
	 * @param args non utilisé
	 */
	public static void main(String[] args) {
		new Controle();
	}

	/**
	 * Demande provenant de la vue EntreeJeu
	 * 
	 * @param info information à traiter
	 */
	public void evenementEntreeJeu(String info) {
		if (info.equals("serveur")) {
			this.typeJeu = "serveur";
			new ServeurSocket(this, 6666);
			this.frmEntreeJeu.dispose();
			this.frmArene = new Arene();
			this.frmArene.setVisible(true);
		} else {
			this.typeJeu = "client";
			new ClientSocket(this, info, 6666);
		}
	}

	@Override
	public void reception(Connection connection, String ordre, Object info) {
		switch(ordre) {
		case "connexion" :
			if(this.typeJeu.equals("client")) {
				this.frmEntreeJeu.dispose();
				this.frmArene = new Arene();
				this.frmChoixJoueur = new ChoixJoueur();
				this.frmChoixJoueur.setVisible(true);
			}
			break;
		case "reception" :
			break;
		case "deconnexion" :
			break;
		}

	}

}
