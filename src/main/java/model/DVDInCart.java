package model;

public class DVDInCart extends DVD{
	private int quantitaSelezionata;
	
	  public DVDInCart(int id, String nome, int durata, String regista, float prezzo, boolean inCatalogo, int quantita, int quantitaSelezionata) {
	        super(id, nome, durata, regista, prezzo, inCatalogo, quantita);
	        this.quantitaSelezionata = quantitaSelezionata;
	    }
	
	public void setQuantitaSelezionata(int q) {
		quantitaSelezionata = q;
	}
	
	public int getQuantitaSelezionata() {
		return quantitaSelezionata;
	}
}