package Compiler;

import java.util.ArrayList;
import java.util.List;

public class Method1 {
    String name;
    String return_type;
    String access_modifier;
    List<MoolaVar> vars = new ArrayList<MoolaVar>();
    List<MoolaVar> inputs = new ArrayList<MoolaVar>();

    public Method1(String name,String return_type,String access_modifier,MoolaVar vars, MoolaVar inputs){
        this.name = name;
        this.return_type = return_type;
        this.access_modifier = access_modifier;
        this.vars.add(vars);
        this.inputs.add((inputs));

    }
    public String print(){
        String inputs = "";
        if(this.inputs.size()>1) inputs+=" inputs: ";
        for (int i =1; i<this.inputs.size(); i++){
            inputs += this.inputs.get(i).name+", ";
        }
        return "method -> name: "+name+","+inputs+" return_type: "+return_type+", access_modifier: "+access_modifier+"\n";
    }
    public String tablePrint(){
        String s= "";
        //if(vars.size()>1 )
            s+="Method \""+name+ "\" Scope: \n";
        for (int i=1 ; i< vars.size() ; i++){
            s += vars.get(i).print();
        }
        if(this.inputs.size()>1) s+="inputs: ";
        for (int i =1; i<this.inputs.size(); i++){
            s += this.inputs.get(i).name+", ";
        }
        return s;
    }
}
