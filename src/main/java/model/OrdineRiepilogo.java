package model;

import java.sql.Timestamp;

public class OrdineRiepilogo {
    private int ordineId; // <--- Nuova proprietà per la PK numerica di ORDINE
    private String seqId;
    private Timestamp dataOrdine;
    private float totale;
    private String nome;
    private String cognome;
    private String indirizzo;
    private String citta;
    private String cap;
    private String paese;
    private String dvdAcquistati;
    private String username;
    private String email;
    private int numOrdini;
    private float totaleSpeso;
    private int utenteID;

    public OrdineRiepilogo() {}

    public OrdineRiepilogo(int ordineId, String seqId, Timestamp dataOrdine, float totale,
                           String nome, String cognome, String indirizzo,
                           String citta, String cap, String paese,
                           String dvdAcquistati, String username, String email, int utenteID) {
        this.ordineId      = ordineId;
        this.utenteID      = utenteID;
        this.seqId         = seqId;
        this.dataOrdine    = dataOrdine;
        this.totale        = totale;
        this.nome          = nome;
        this.cognome       = cognome;
        this.indirizzo     = indirizzo;
        this.citta         = citta;
        this.cap           = cap;
        this.paese         = paese;
        this.dvdAcquistati = dvdAcquistati;
        this.username      = username;
        this.email         = email;
    }

    // Getter e Setter per la nuova proprietà ordineId
    public int getOrdineId() { return ordineId; }
    public void setOrdineId(int ordineId) { this.ordineId = ordineId; }

    public String getSeqId()                    { return seqId; }
    public void setSeqId(String seqId)          { this.seqId = seqId; }

    public Timestamp getDataOrdine()            { return dataOrdine; }
    public void setDataOrdine(Timestamp d)      { this.dataOrdine = d; }

    public float getTotale()                    { return totale; }
    public void setTotale(float totale)         { this.totale = totale; }

    public String getNome()                     { return nome; }
    public void setNome(String nome)            { this.nome = nome; }

    public String getCognome()                  { return cognome; }
    public void setCognome(String cognome)      { this.cognome = cognome; }

    public String getIndirizzo()                    { return indirizzo; }
    public void setIndirizzo(String indirizzo)      { this.indirizzo = indirizzo; }

    public String getCitta()                { return citta; }
    public void setCitta(String citta)      { this.citta = citta; }

    public String getCap()              { return cap; }
    public void setCap(String cap)      { this.cap = cap; }

    public String getPaese()                { return paese; }
    public void setPaese(String paese)      { this.paese = paese; }

    public String getDvdAcquistati()                    { return dvdAcquistati; }
    public void setDvdAcquistati(String dvdAcquistati)  { this.dvdAcquistati = dvdAcquistati; }

    public String getUsername()                 { return username; }
    public void setUsername(String username)    { this.username = username; }

    public String getEmail()                { return email; }
    public void setEmail(String email)      { this.email = email; }

    public int getNumOrdini()                   { return numOrdini; }
    public void setNumOrdini(int numOrdini)     { this.numOrdini = numOrdini; }

    public float getTotaleSpeso()                   { return totaleSpeso; }
    public void setTotaleSpeso(float totaleSpeso)   { this.totaleSpeso = totaleSpeso; }
    
    public int getUtenteID() { return utenteID; }
    public void setUtenteID(int utenteID) { this.utenteID = utenteID; }
}