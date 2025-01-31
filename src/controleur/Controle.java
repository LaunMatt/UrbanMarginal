package controleur;

import javax.swing.JLabel;
import javax.swing.JPanel;

import modelepackage.Jeu;
import modelepackage.JeuClient;
import modelepackage.JeuServeur;
import outilspackage.AsyncResponse;
import outilspackage.ClientSocket;
import outilspackage.Connection;
import outilspackage.ServeurSocket;
import vue.Arene;
import vue.ChoixJoueur;
import vue.EntreeJeu;

/**
 * Contrôleur et point d'entrée de l'applicaton 
 * @author MattLaun
 *
 */
public class Controle implements AsyncResponse, Global {

	/**
	 * frame EntreeJeu
	 */
	private EntreeJeu frmEntreeJeu ;
	/**
	 * frame Arene
	 */
	private Arene frmArene;
	/**
	 * frame ChoixJoueur
	 */
	private ChoixJoueur frmChoixJoueur;
	/**
	 * instance du jeu (JeuServeur ou JeuClient)
	 */
	private Jeu leJeu;

	/**
	 * Méthode de démarrage
	 * @param args non utilisé
	 */
	public static void main(String[] args) {
		new Controle();
	}
	
	/**
	 * Constructeur
	 */
	private Controle() {
		this.frmEntreeJeu = new EntreeJeu(this) ;
		this.frmEntreeJeu.setVisible(true);
	}
	
	/**
	 * Demande provenant de la vue EntreeJeu
	 * @param info information à traiter
	 */
	public void evenementEntreeJeu(String info) {
		if(info.equals(SERVEUR)) {
			new ServeurSocket(this, PORT);
			this.leJeu = new JeuServeur(this);
			this.frmEntreeJeu.dispose();
			this.frmArene = new Arene(this, SERVEUR);
			((JeuServeur)this.leJeu).constructionMurs();
			this.frmArene.setVisible(true);
		} else {
			new ClientSocket(this, info, PORT);
		}
	}
	
	/**
	 * Demande provenant de JeuServeur
	 * @param ordre ordre à exécuter
	 * @param info information à traiter
	 */
	public void evenementJeuServeur(String ordre, Object info) {
		switch(ordre) {
		case AJOUTMUR :
			frmArene.ajoutMurs(info);
			break;
		case AJOUTPANELMURS :
			this.leJeu.envoi((Connection)info, this.frmArene.getJpnMurs());
			break;
		case AJOUTJLABELJEU :
			this.frmArene.ajoutJLabelJeu((JLabel)info);
			break;
		case MODIFPANELJEU :
			this.leJeu.envoi((Connection)info, this.frmArene.getJpnJeu());
			break;
		case AJOUTPHRASE :
			this.frmArene.ajoutTchat((String)info);
			((JeuServeur)this.leJeu).envoi(this.frmArene.getTxtChat());
			break;
		}
	}
	
	/**
	 * Demande provenant de JeuClient
	 * @param ordre ordre à exécuter
	 * @param info information à traiter
	 */
	public void evenementJeuClient(String ordre, Object info) {
		switch(ordre) {
		case AJOUTPANELMURS :
			this.frmArene.setJpnMurs((JPanel)info);
			break;
		case MODIFPANELJEU :
			this.frmArene.setJpnJeu((JPanel)info);
			break;
		case MODIFTCHAT :
			this.frmArene.setTxtChat((String)info);
			break;
		}
	}
	
	/**
	 * Informations provenant de la vue ChoixJoueur
	 * @param pseudo le pseudo du joueur
	 * @param numPerso le numéro du personnage choisi par le joueur
	 */
	public void evenementChoixJoueur(String pseudo, int numPerso) {
		this.frmChoixJoueur.dispose();
		this.frmArene.setVisible(true);
		((JeuClient)this.leJeu).envoi(controleur.Global.PSEUDO+controleur.Global.STRINGSEPARE+pseudo+controleur.Global.STRINGSEPARE+numPerso);
	}
	
	/**
	 * Information provenant de la vue Arene
	 * @param info information à transférer
	 */
	public void evenementArene(Object info) {
		if(info instanceof String) {
			((JeuClient)this.leJeu).envoi(TCHAT+STRINGSEPARE+info);
		}else if (info instanceof Integer) {
			((JeuClient)this.leJeu).envoi(ACTION+STRINGSEPARE+info);
		}
	}

	/**
	 * Envoi d'informations vers l'ordinateur distant
	 * @param connection objet de connexion pour l'envoi vers l'ordinateur distant
	 * @param info information à envoyer
	 */
	public void envoi(Connection connection, Object info) {
		connection.envoi(info);
	}
	
	/**
	 * Réception des informations envoyées par les différentes classes
	 */
	@Override
	public void reception(Connection connection, String ordre, Object info) {
		switch(ordre) {
		case controleur.Global.CONNEXION :
			if(!(this.leJeu instanceof JeuServeur)) {
				this.leJeu = new JeuClient(this);
				this.leJeu.connexion(connection);
				this.frmEntreeJeu.dispose();
				this.frmArene = new Arene(this, CLIENT);
				this.frmChoixJoueur = new ChoixJoueur(this);
				this.frmChoixJoueur.setVisible(true);
			} else {
				this.leJeu.connexion(connection);
			}
			break;
		case controleur.Global.RECEPTION :
			this.leJeu.reception(connection, info);
			break;
		case controleur.Global.DECONNEXION :
			break;
		}
		
	}

}
