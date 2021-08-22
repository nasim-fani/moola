package Compiler;

public class Field {
    String name;
    String type;
    String access_modifier;

    public Field(String name,String type,String access_modifier){
        this.name = name;
        this.type = type;
        this.access_modifier = access_modifier;
    }

    public String print(){
        return "field -> name: "+name+", type: "+type+", access_modifier: "+access_modifier+"\n";
    }
}
