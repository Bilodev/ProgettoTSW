package model;

import java.sql.Timestamp;

public class Ordine {
    private int id;
    private int utenteId;
    private String seqId; // si riferisce alla specifica dell'ordine
    private Timestamp dataOrdine;

    public Ordine() {}

    public Ordine(int id, String seqId, int utenteId, Timestamp dataOrdine) {
        this.id          = id;
        this.seqId       = seqId;
        this.utenteId    = utenteId;
        this.dataOrdine  = dataOrdine;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public String getSeqId()                    { return seqId; }
    public void setSeqId(String seqId)          { this.seqId = seqId; }

    public int getUtenteId()                    { return utenteId; }
    public void setUtenteId(int utenteId)       { this.utenteId = utenteId; }

    public Timestamp getDataOrdine()            { return dataOrdine; }
    public void setDataOrdine(Timestamp t)      { this.dataOrdine = t; }
}