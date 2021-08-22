package Compiler;

import gen.MoolaListener;
import gen.MoolaParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ProgramPrinter implements MoolaListener {

    Hashtable global;
    MoolaClass c1;
    Method1 m1;
    Boolean entry = false;
    Conditional con;
    Boolean conditional=false;
    List<MoolaClass> moolaClassList = new ArrayList<MoolaClass>();
    ArrayList<String> news = new ArrayList<>();


    public ProgramPrinter(Hashtable global){
        this.global = global;
    }

    @Override
    public void enterProgram(MoolaParser.ProgramContext ctx) {
        /*System.out.println("program start{");*/
    }

    @Override
    public void exitProgram(MoolaParser.ProgramContext ctx) {

        //System.out.println(moolaClassList);

        for (MoolaClass c :
                moolaClassList) {
           // if(c.parent== null) System.out.println(c.name);
            List<String> parents = new ArrayList<>();
            if(c.parent!= null)parents.add(c.name);
            int flag = 1;
            while (c.parent!= null && flag == 1){
                parents.add(c.parent);
                //System.out.println(parents);
                for (int i = 0; i < parents.size()-1; i++) {
                    for (int j = i+1; j < parents.size(); j++) {
                        if(parents.get(i).equals(parents.get(j))){
                            System.out.print("Error410 : Invalid inheritance ");
                            for (String parr :
                                    parents) {
                                System.out.print(parr + " -> ");
                            }
                            System.out.println("");
                            flag =0;
                        }
                    }
                }
                int flagg= 1;
                for (MoolaClass par :
                        moolaClassList) {
                    if (par.name.equals(c.parent)&& flagg==1){
                        c = par;
                        flagg = 0;
                    }
                }
            }

            //System.out.println("------"+parents);
        }

        for (int i = 0; i < news.size(); i+=2) {
            int flaag = 0;
            for (MoolaClass c :
                    moolaClassList) {
                if (c.name.equals(news.get(i))) {
                    flaag = 1;
                }
            }
            if(flaag==0){
                System.out.println("Error105 : in line "+news.get(i+1)+", cannot find class "+news.get(i));
            }
        }
        System.out.println("Global Scope:");
        System.out.println(global);
    }

    @Override
    public void enterClassDeclaration(MoolaParser.ClassDeclarationContext ctx) {
        /*if(!ctx.methodDeclaration().get(0).methodName.getText().equals("main"))
        System.out.print("\tclass: "+ctx.className.getText());
        if(ctx.classParent!=null) System.out.print("/ class parent: "+ ctx.classParent.getText());
        System.out.println("{");*/

            if (ctx.classParent != null)
                c1 = new MoolaClass(ctx.className.getText(), ctx.classParent.getText(), new Field("", "", ""), new Method1("", "", "", new MoolaVar(""),new MoolaVar("")),new Conditional(new MoolaVar(""),""));
            else
                c1 = new MoolaClass(ctx.className.getText(), null, new Field("", "", ""), new Method1("", "", "", new MoolaVar(""),new MoolaVar("")),new Conditional(new MoolaVar(""),""));

            //if (!ctx.methodDeclaration().get(0).methodName.getText().equals("main")) ;
        if (ctx.classParent != null) {
            if (ctx.classParent.getText().equals("String")) {
                System.out.println("*Error430 : in line " + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine() + 7 + ", class" + ctx.className.getText() + "cannot inherits from class String");
            }
        }
        if(entry){
            c1.isEntry = true;
            c1.name = ctx.className.getText();
            if(global.containsKey("MainClass_"+c1.name)){
                System.out.println("Error101 : in line "+ctx.start.getLine()+":"+ctx.start.getCharPositionInLine()+7+", class "+c1.name+" has been defined already\n");
                c1.name = c1.name+"_"+ctx.start.getLine()+"_"+ctx.start.getCharPositionInLine()+7;
                global.put("MainClass_"+c1.name, c1.globalprint());
            }
            else
            global.put("MainClass_"+c1.name, c1.globalprint());
        }
        else {
            if(global.containsKey("Class_"+c1.name)){
                c1.name = c1.name+"_"+ctx.start.getLine()+"_"+ctx.start.getCharPositionInLine()+7;
                System.out.println("Error101 : in line "+ctx.start.getLine()+":"+ctx.start.getCharPositionInLine()+7+", class "+c1.name+" has been defined already\n");
                global.put("Class_"+c1.name, c1.globalprint());
            }
            else
            global.put("Class_"+c1.name, c1.globalprint());
        }
        moolaClassList.add(c1);
        entry = false;

    }

    @Override
    public void exitClassDeclaration(MoolaParser.ClassDeclarationContext ctx) {
        //System.out.println("\t}");
        if(c1.isEntry)
        System.out.println("Mainclass \""+c1.name+"\" Scope:");
        else
            System.out.println("class \""+c1.name+"\" Scope:");
        System.out.println(c1.print());
    }

    @Override
    public void enterEntryClassDeclaration(MoolaParser.EntryClassDeclarationContext ctx) {
        //System.out.print("\tmain class: "+ctx.classDeclaration().className.getText());
        //MainClass c1 = new MainClass(ctx.classDeclaration().className.getText());

        //global.put("Mainclass_"+ctx.classDeclaration().className.getText(),c1.printer());
        /*c1 = new MoolaClass("Mainclass_"+ctx.classDeclaration().className.getText(),null,new Field("","",""),new Method1("","","",new MoolaVar("")));
        global.put("MainClass_"+ctx.classDeclaration().className.getText(),c1.globalprint());
        c1.isEntry = true;*/
        entry = true;

    }

    @Override
    public void exitEntryClassDeclaration(MoolaParser.EntryClassDeclarationContext ctx) {

    }

    @Override
    public void enterFieldDeclaration(MoolaParser.FieldDeclarationContext ctx) {
        //System.out.println("\t\tfield: "+ ctx.fieldName.getText() + "/ type="+ctx.fieldType.getText() + "/ access modifier="+ctx.fieldAccessModifier.getText());
        Field f1;
        List<TerminalNode> ids = ctx.ID();
        for (TerminalNode id : ids) {
            if (ctx.fieldAccessModifier != null)
                f1 = new Field(id.toString(), ctx.fieldType.getText(), ctx.fieldAccessModifier.getText());
            else
                f1 = new Field(id.toString(), ctx.fieldType.getText(), "private");

            for (Field f :
                    c1.fields) {
                if (f.name.equals(f1.name)){
                    System.out.println("Error104 : in line "+ctx.start.getLine()+":"+ctx.start.getCharPositionInLine()+", field "+f1.name+" has been defined already\n");
                    f1.name=f1.name+"_"+ctx.start.getLine()+"_"+ctx.start.getCharPositionInLine();
                }
            }
            c1.fields.add(f1);
        }

        //global.put("Field_"+ctx.fieldName.getText(),f1);



    }

    @Override
    public void exitFieldDeclaration(MoolaParser.FieldDeclarationContext ctx) {

    }

    @Override
    public void enterAccess_modifier(MoolaParser.Access_modifierContext ctx) {

    }

    @Override
    public void exitAccess_modifier(MoolaParser.Access_modifierContext ctx) {

    }

    @Override
    public void enterMethodDeclaration(MoolaParser.MethodDeclarationContext ctx) {
        /*System.out.print("\t\tclass method: "+ctx.methodName.getText()+"/ return type="+ ctx.moolaType().get(0).getText());
        if(ctx.methodAccessModifier!=null) System.out.print("/ access modifier="+ctx.access_modifier().getText());
        else System.out.print("/ access modifier=public");
        System.out.println("{");*/

        ArrayList<MoolaVar> params = new ArrayList<>();
        if (ctx.param1 != null) {
            List<MoolaParser.MoolaTypeContext> inputs = ctx.moolaType();
            List<TerminalNode> names = ctx.ID();
            for (int i = 0; i < inputs.size() - 1; i++) { //param2 s
                MoolaVar var = new MoolaVar(names.get(i + 1).getSymbol().getText() + " : " +inputs.get(i).st.getText());
                params.add(var);
            }
        }

        if(ctx.methodAccessModifier!=null)
            m1 = new Method1(ctx.methodName.getText(),ctx.t.getText(),ctx.access_modifier().getText(),new MoolaVar(""),new MoolaVar(""));
        else
            m1 = new Method1(ctx.methodName.getText(),ctx.t.getText(),"public",new MoolaVar(""),new MoolaVar(""));
        //global.put("Method_"+ctx.methodName.getText(),m1);
        m1.inputs.addAll(params);
        for (Method1 m:
            c1.methods ) {
            if(m.name.equals(m1.name)){
                System.out.println("Error102 : in line "+ctx.start.getLine()+":"+ctx.start.getCharPositionInLine()+", method "+m1.name+" has been defined already\n");
                m1.name = m1.name+"_"+ctx.start.getLine()+"_"+ctx.start.getCharPositionInLine();
            }
        }
        c1.methods.add(m1);
        if(ctx.methodName.getText().equals("main")){
            if(!ctx.t.getText().equals("void")){
                System.out.println("*Error420 : in line"+ctx.start.getLine()+":"+ctx.start.getCharPositionInLine()+", return type of main method must be void");
            }
        }

    }

    @Override
    public void exitMethodDeclaration(MoolaParser.MethodDeclarationContext ctx) {
        //System.out.println("\t\t}");
        //System.out.println();
        String a = ctx.s.s1.getText();
        if(!m1.return_type.equals("void")){
            if(!a.contains("return")){
                System.out.println("*Error440: in method "+m1.name +" return type required");
            }
        }
    }

    @Override
    public void enterClosedStatement(MoolaParser.ClosedStatementContext ctx) {
        String type = "";
        if (ctx.getParent() instanceof MoolaParser.ClosedConditionalContext) {
            MoolaParser.ClosedConditionalContext parent = (MoolaParser.ClosedConditionalContext) ctx.getParent();
            if (ctx == parent.ifStat) type ="if";
            else if (ctx == parent.elifStat) type = "elif";
            else type = "else";
        } else if (ctx.getParent() instanceof MoolaParser.OpenConditionalContext) {
            MoolaParser.OpenConditionalContext parent = (MoolaParser.OpenConditionalContext) ctx.getParent();
            if (ctx == parent.secondIfStat) type = "if";
            else if (parent.closedStatement().subList(1, parent.closedStatement().size()).contains(ctx)) type = "elif";
            else if (ctx == parent.thirdIfStat) type = "if";
        }
        if(type!="") {
            con = new Conditional(new MoolaVar(""), type);
            c1.conditionals.add(con);
            conditional = true;
        }
    }

    @Override
    public void exitClosedStatement(MoolaParser.ClosedStatementContext ctx) {
        conditional = false;
    }

    @Override
    public void enterClosedConditional(MoolaParser.ClosedConditionalContext ctx) {

    }

    @Override
    public void exitClosedConditional(MoolaParser.ClosedConditionalContext ctx) {

    }

    @Override
    public void enterOpenConditional(MoolaParser.OpenConditionalContext ctx) {

    }

    @Override
    public void exitOpenConditional(MoolaParser.OpenConditionalContext ctx) {

    }

    @Override
    public void enterOpenStatement(MoolaParser.OpenStatementContext ctx) {
        if (ctx.getParent() instanceof MoolaParser.OpenConditionalContext) {
            MoolaParser.OpenConditionalContext parent = (MoolaParser.OpenConditionalContext) ctx.getParent();
            if (ctx == parent.elseStmt) {
                con = new Conditional(new MoolaVar(""), "else");
                c1.conditionals.add(con);
                conditional = true;
            }
        }
    }

    @Override
    public void exitOpenStatement(MoolaParser.OpenStatementContext ctx) {
        conditional = false;

    }

    @Override
    public void enterStatement(MoolaParser.StatementContext ctx) {
        String type ="";
        if (ctx.getParent() instanceof MoolaParser.OpenConditionalContext) {
            MoolaParser.OpenConditionalContext parent = (MoolaParser.OpenConditionalContext) ctx.getParent();
            if (ctx == parent.ifStat)
                type = "if";
            else if (ctx == parent.lastElifStmt)
                type = "elif";
        }
        if(type!="") {
            con = new Conditional(new MoolaVar(""), type);
            c1.conditionals.add(con);
            conditional = true;
        }
    }

    @Override
    public void exitStatement(MoolaParser.StatementContext ctx) {
        conditional = false;
    }

    @Override
    public void enterStatementVarDef(MoolaParser.StatementVarDefContext ctx) {
        //System.out.println("\t\t\tvar: "+ctx.ID().get(0));
        //System.out.println(ctx.parent.getParent().getText());
        MoolaVar v1 = new MoolaVar(ctx.ID().get(0).getText());
        if(conditional) {
            for (MoolaVar v :
                    con.vars) {
                if (v.name.equals(v1.name)){
                    System.out.println("Error103 : in line "+ctx.start.getLine()+":"+ctx.start.getCharPositionInLine()+", var "+v1.name+" has been defined already\n");
                    v1.name=v1.name+"_"+ctx.start.getLine()+"_"+ctx.start.getCharPositionInLine();
                }
            }
            con.vars.add(v1);
        }
        else {
            for (MoolaVar v :
                    m1.vars) {
                if (v.name.equals(v1.name)){
                    System.out.println("Error103 : in line "+ctx.start.getLine()+":"+ctx.start.getCharPositionInLine()+", var "+v1.name+" has been defined already\n");
                    v1.name=v1.name+"_"+ctx.start.getLine()+"_"+ctx.start.getCharPositionInLine();
                }
            }
            m1.vars.add(v1);
        }

        String a =ctx.expression().get(0).getText();
        if(a.contains("new")){
            if(a.contains("(")){
                String[] parts = a.split("new");
                String[] part2 = parts[1].split("\\(");
                news.add(part2[0]);
                news.add(ctx.start.getLine()+":"+ctx.start.getCharPositionInLine());
            }
        }
    }

    @Override
    public void exitStatementVarDef(MoolaParser.StatementVarDefContext ctx) {

    }

    @Override
    public void enterStatementBlock(MoolaParser.StatementBlockContext ctx) {
        ParserRuleContext parent = ctx.getParent().getParent();
        if (! (parent instanceof MoolaParser.StatementClosedLoopContext || parent instanceof MoolaParser.ClosedConditionalContext ||  parent instanceof MoolaParser.OpenConditionalContext
        )) {
            con = new Conditional(new MoolaVar(""), "block");
            c1.conditionals.add(con);
            conditional = true;
        }
    }

    @Override
    public void exitStatementBlock(MoolaParser.StatementBlockContext ctx) {
        conditional = false;
    }

    @Override
    public void enterStatementContinue(MoolaParser.StatementContinueContext ctx) {

    }

    @Override
    public void exitStatementContinue(MoolaParser.StatementContinueContext ctx) {

    }

    @Override
    public void enterStatementBreak(MoolaParser.StatementBreakContext ctx) {

    }

    @Override
    public void exitStatementBreak(MoolaParser.StatementBreakContext ctx) {

    }

    @Override
    public void enterStatementReturn(MoolaParser.StatementReturnContext ctx) {
//        System.out.println("!!!!!!!!!!!!!!!!");
//        System.out.println(ctx.getText());

    }

    @Override
    public void exitStatementReturn(MoolaParser.StatementReturnContext ctx) {

    }

    @Override
    public void enterStatementClosedLoop(MoolaParser.StatementClosedLoopContext ctx) {
        con = new Conditional(new MoolaVar(""), "while");
        c1.conditionals.add(con);
        conditional = true;
    }

    @Override
    public void exitStatementClosedLoop(MoolaParser.StatementClosedLoopContext ctx) {
        conditional = false;
    }

    @Override
    public void enterStatementOpenLoop(MoolaParser.StatementOpenLoopContext ctx) {
        con = new Conditional(new MoolaVar(""), "while");
        c1.conditionals.add(con);
        conditional = true;
    }

    @Override
    public void exitStatementOpenLoop(MoolaParser.StatementOpenLoopContext ctx) {
        conditional = false;
    }

    @Override
    public void enterStatementWrite(MoolaParser.StatementWriteContext ctx) {

    }

    @Override
    public void exitStatementWrite(MoolaParser.StatementWriteContext ctx) {

    }

    @Override
    public void enterStatementAssignment(MoolaParser.StatementAssignmentContext ctx) {

        String left = ctx.left.getText();
        int fflag =1;
        for (MoolaVar v :
                m1.vars) {
            if(v.name.equals(left)) fflag = 0;
        }
        if(fflag==1){
            System.out.println("Error106 : in line "+ctx.start.getLine()+":"+ctx.start.getCharPositionInLine()+", cannot find variable "+left);
        }
    }

    @Override
    public void exitStatementAssignment(MoolaParser.StatementAssignmentContext ctx) {

    }

    @Override
    public void enterStatementInc(MoolaParser.StatementIncContext ctx) {

    }

    @Override
    public void exitStatementInc(MoolaParser.StatementIncContext ctx) {

    }

    @Override
    public void enterStatementDec(MoolaParser.StatementDecContext ctx) {

    }

    @Override
    public void exitStatementDec(MoolaParser.StatementDecContext ctx) {

    }

    @Override
    public void enterExpression(MoolaParser.ExpressionContext ctx) {

    }

    @Override
    public void exitExpression(MoolaParser.ExpressionContext ctx) {

    }

    @Override
    public void enterExpressionOr(MoolaParser.ExpressionOrContext ctx) {

    }

    @Override
    public void exitExpressionOr(MoolaParser.ExpressionOrContext ctx) {

    }

    @Override
    public void enterExpressionOrTemp(MoolaParser.ExpressionOrTempContext ctx) {

    }

    @Override
    public void exitExpressionOrTemp(MoolaParser.ExpressionOrTempContext ctx) {

    }

    @Override
    public void enterExpressionAnd(MoolaParser.ExpressionAndContext ctx) {

    }

    @Override
    public void exitExpressionAnd(MoolaParser.ExpressionAndContext ctx) {

    }

    @Override
    public void enterExpressionAndTemp(MoolaParser.ExpressionAndTempContext ctx) {

    }

    @Override
    public void exitExpressionAndTemp(MoolaParser.ExpressionAndTempContext ctx) {

    }

    @Override
    public void enterExpressionEq(MoolaParser.ExpressionEqContext ctx) {

    }

    @Override
    public void exitExpressionEq(MoolaParser.ExpressionEqContext ctx) {

    }

    @Override
    public void enterExpressionEqTemp(MoolaParser.ExpressionEqTempContext ctx) {

    }

    @Override
    public void exitExpressionEqTemp(MoolaParser.ExpressionEqTempContext ctx) {

    }

    @Override
    public void enterExpressionCmp(MoolaParser.ExpressionCmpContext ctx) {

    }

    @Override
    public void exitExpressionCmp(MoolaParser.ExpressionCmpContext ctx) {

    }

    @Override
    public void enterExpressionCmpTemp(MoolaParser.ExpressionCmpTempContext ctx) {

    }

    @Override
    public void exitExpressionCmpTemp(MoolaParser.ExpressionCmpTempContext ctx) {

    }

    @Override
    public void enterExpressionAdd(MoolaParser.ExpressionAddContext ctx) {

    }

    @Override
    public void exitExpressionAdd(MoolaParser.ExpressionAddContext ctx) {

    }

    @Override
    public void enterExpressionAddTemp(MoolaParser.ExpressionAddTempContext ctx) {

    }

    @Override
    public void exitExpressionAddTemp(MoolaParser.ExpressionAddTempContext ctx) {

    }

    @Override
    public void enterExpressionMultMod(MoolaParser.ExpressionMultModContext ctx) {

    }

    @Override
    public void exitExpressionMultMod(MoolaParser.ExpressionMultModContext ctx) {

    }

    @Override
    public void enterExpressionMultModTemp(MoolaParser.ExpressionMultModTempContext ctx) {

    }

    @Override
    public void exitExpressionMultModTemp(MoolaParser.ExpressionMultModTempContext ctx) {

    }

    @Override
    public void enterExpressionUnary(MoolaParser.ExpressionUnaryContext ctx) {

    }

    @Override
    public void exitExpressionUnary(MoolaParser.ExpressionUnaryContext ctx) {

    }

    @Override
    public void enterExpressionMethods(MoolaParser.ExpressionMethodsContext ctx) {

    }

    @Override
    public void exitExpressionMethods(MoolaParser.ExpressionMethodsContext ctx) {

    }

    @Override
    public void enterExpressionMethodsTemp(MoolaParser.ExpressionMethodsTempContext ctx) {

    }

    @Override
    public void exitExpressionMethodsTemp(MoolaParser.ExpressionMethodsTempContext ctx) {

    }

    @Override
    public void enterExpressionOther(MoolaParser.ExpressionOtherContext ctx) {

    }

    @Override
    public void exitExpressionOther(MoolaParser.ExpressionOtherContext ctx) {

    }

    @Override
    public void enterMoolaType(MoolaParser.MoolaTypeContext ctx) {

    }

    @Override
    public void exitMoolaType(MoolaParser.MoolaTypeContext ctx) {

    }

    @Override
    public void enterSingleType(MoolaParser.SingleTypeContext ctx) {

    }

    @Override
    public void exitSingleType(MoolaParser.SingleTypeContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
