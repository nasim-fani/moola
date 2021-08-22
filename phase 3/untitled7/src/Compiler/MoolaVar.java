package Compiler;

public class MoolaVar {
    String name;

    public MoolaVar(String name){
        this.name = name;
    }
    public String print(){
        return "Var->name: "+name+"\n";
    }
}
