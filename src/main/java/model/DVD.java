package model;

public class DVD {
    private int id;
    private String nome;
    private int durata;
    private String regista;
    private boolean inCatalogo;
    
    public DVD() {
    }

    public DVD(int id, String nome, int durata, String regista, boolean inCatalogo) {
        this.id = id;
        this.nome = nome;
        this.durata = durata;
        this.regista = regista;
        this.inCatalogo = inCatalogo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public String getRegista() {
        return regista;
    }

    public void setRegista(String regista) {
        this.regista = regista;
    }
    
    public boolean isInCatalogo() {
    	return inCatalogo;
    }
    public void setInCatalogo(boolean b) {
    	inCatalogo = b;
    }
}
