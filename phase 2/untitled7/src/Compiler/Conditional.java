package Compiler;

import java.util.ArrayList;
import java.util.List;

public class Conditional {
    String type;
    //String statement;
    List<MoolaVar> vars = new ArrayList<MoolaVar>();
    public Conditional( MoolaVar vars, String type){
        //this.statement = statement;
        this.type=type;
        this.vars.add(vars);
    }
    public String print(){
        String s="";
        s += "type: " + this.type + " ";
        //s += "statement: " + this.statement;
        for (int i=1 ; i< vars.size() ; i++){
            s += vars.get(i).print();
        }
        return s;
    }

}
