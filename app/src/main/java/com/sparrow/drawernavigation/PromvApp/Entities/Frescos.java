package com.sparrow.drawernavigation.PromvApp.Entities;

public class Frescos {
    String id,name;
    public Frescos(int conatdor, String producto_fresco) {
    }

    public Frescos(String id, String name){
        this.id = id;
        this.name = name;
    }
    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String setId(){
        return this.id;
    }
    public String setNombre(){
        return this.name;
    }

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
