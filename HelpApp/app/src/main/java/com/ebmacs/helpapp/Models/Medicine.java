package com.ebmacs.helpapp.Models;

public class Medicine
{
    private String doses;

    private String videoUrl;

    private String principioAtivo;

    private String apresentacao;

    private String nome;

    private String id;

    private String identifierType;

    private String laboratorio;

    private String classeTerapeutica;

    private String logoUrl;

    public String getDoses ()
    {
        return doses;
    }

    public void setDoses (String doses)
    {
        this.doses = doses;
    }

    public String getVideoUrl ()
    {
        return videoUrl;
    }

    public void setVideoUrl (String videoUrl)
    {
        this.videoUrl = videoUrl;
    }

    public String getPrincipioAtivo ()
    {
        return principioAtivo;
    }

    public void setPrincipioAtivo (String principioAtivo)
    {
        this.principioAtivo = principioAtivo;
    }

    public String getApresentacao ()
    {
        return apresentacao;
    }

    public void setApresentacao (String apresentacao)
    {
        this.apresentacao = apresentacao;
    }

    public String getNome ()
    {
        return nome;
    }

    public void setNome (String nome)
    {
        this.nome = nome;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getIdentifierType ()
    {
        return identifierType;
    }

    public void setIdentifierType (String identifierType)
    {
        this.identifierType = identifierType;
    }

    public String getLaboratorio ()
    {
        return laboratorio;
    }

    public void setLaboratorio (String laboratorio)
    {
        this.laboratorio = laboratorio;
    }

    public String getClasseTerapeutica ()
    {
        return classeTerapeutica;
    }

    public void setClasseTerapeutica (String classeTerapeutica)
    {
        this.classeTerapeutica = classeTerapeutica;
    }

    public String getLogoUrl ()
    {
        return logoUrl;
    }

    public void setLogoUrl (String logoUrl)
    {
        this.logoUrl = logoUrl;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [doses = "+doses+", videoUrl = "+videoUrl+", principioAtivo = "+principioAtivo+", apresentacao = "+apresentacao+", nome = "+nome+", id = "+id+", identifierType = "+identifierType+", laboratorio = "+laboratorio+", classeTerapeutica = "+classeTerapeutica+", logoUrl = "+logoUrl+"]";
    }
}
