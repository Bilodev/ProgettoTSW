package model;

public class OrdineDVD {
    private int ordineId;
    private int dvdId;
    private int quantita;
    private float prezzoUnitario;   // prezzo congelato al momento dell'acquisto

    public OrdineDVD() {}

    public OrdineDVD(int ordineId, int dvdId, int quantita, float prezzoUnitario) {
        this.ordineId       = ordineId;
        this.dvdId          = dvdId;
        this.quantita       = quantita;
        this.prezzoUnitario = prezzoUnitario;
    }

    public int getOrdineId()                        { return ordineId; }
    public void setOrdineId(int ordineId)           { this.ordineId = ordineId; }

    public int getDvdId()                           { return dvdId; }
    public void setDvdId(int dvdId)                 { this.dvdId = dvdId; }

    public int getQuantita()                        { return quantita; }
    public void setQuantita(int quantita)           { this.quantita = quantita; }

    public float getPrezzoUnitario()                { return prezzoUnitario; }
    public void setPrezzoUnitario(float p)          { this.prezzoUnitario = p; }

    // Totale della riga
    public float getTotaleRiga()                    { return prezzoUnitario * quantita; }
}