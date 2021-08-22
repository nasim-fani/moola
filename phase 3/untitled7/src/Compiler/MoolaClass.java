package Compiler;

import java.util.ArrayList;
import java.util.List;

public class MoolaClass {
    String name;
    String parent =null;
    Boolean isEntry = false;
    List<Method1> methods = new ArrayList<Method1>();
    List<Field> fields = new ArrayList<Field>();
    List<Conditional> conditionals = new ArrayList<Conditional>();

    public MoolaClass(String name,String parent,Field fields,Method1 methods,Conditional conditionals ){
        this.name = name;
        this.parent = parent;
        this.fields.add(fields);
        this.methods.add(methods);
        this.conditionals.add(conditionals);
    }
    public String globalprint(){
        return "name: "+name+", parent: "+parent;
    }
    public String print(){
        String s="";
        for (int i=1 ; i< methods.size() ; i++){
            s += methods.get(i).print();
        }
        for (int i=1 ; i< fields.size() ; i++){
            s += fields.get(i).print();
        }
        s+="---\n";

        for (int i=1 ; i< methods.size() ; i++){
            s += methods.get(i).tablePrint();
            s+="\n---\n";
        }
       // if(conditionals.size()>1)s+= "Conditional Blocks:\n";
        for (int i=1 ; i< conditionals.size() ; i++){
            s += conditionals.get(i).print();
            s+="\n---\n";
        }
        return s+"\n-------------------------\n";
    }
}
