package com.ebmacs.helpapp.Models;

import android.graphics.Bitmap;

import java.util.Comparator;

public class Farmacino2 {

    public String name = null;
    public String time = null;
    public String expire = null;
    public String apres = null;
    public String description = null;
    public String eanCode = null;
    public String pricipal = null;
    public Bitmap foto = null;
    public String indicationDays = null;
    public String indicatHours = null;
    public String indicationHours = "";
    public String fabricante = null;
    public String medicamanto = null;
    public String tempoDe = null;
    public String type = null;
    public int quantToTake = 0;

    public String getMadicinId() {
        return madicinId;
    }

    public void setMadicinId(String madicinId) {
        this.madicinId = madicinId;
    }

    public String madicinId=null;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String userId;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String imageUrl = null;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }


    public String getIndicationDays() {
        return indicationDays;
    }

    public void setIndicationDays(String indicationDays) {
        this.indicationDays = indicationDays;
    }

    public String getIndicationHours() {
        return indicationHours;
    }

    public void setIndicationHours(String indicationHours) {
        this.indicationHours = indicationHours;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }


    public String getApres() {
        return apres;
    }

    public void setApres(String apres) {
        this.apres = apres;
    }


    public String getPricipal() {
        return pricipal;
    }

    public void setPricipal(String pricipal) {
        this.pricipal = pricipal;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }


    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getMedicamanto() {
        return medicamanto;
    }

    public void setMedicamanto(String medicamanto) {
        this.medicamanto = medicamanto;
    }

    public String getTempoDe() {
        return tempoDe;
    }

    public void setTempoDe(String tempoDe) {
        this.tempoDe = tempoDe;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantToTake() {
        return quantToTake;
    }

    public void setQuantToTake(int quantToTake) {
        this.quantToTake = quantToTake;
    }


    public static final Comparator<Farmacino2> ascending_comparater = new Comparator<Farmacino2>() {
        @Override
        public int compare(Farmacino2 farmacino2, Farmacino2 t1) {
            return farmacino2.getName().compareTo(t1.getName());
        }
    };
    public static final Comparator<Farmacino2> descending_comparater = new Comparator<Farmacino2>() {
        @Override
        public int compare(Farmacino2 farmacino2, Farmacino2 t1) {
            return t1.getName().compareTo(farmacino2.getName());
        }
    };


}
