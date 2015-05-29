import javafx.util.Pair;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.*;

/**
 * Created by sergey on 02.05.15.
 */
public class Visitor extends GrammarBaseVisitor<Pair<String, List<String>>> {

    private boolean optimize;

    private int labelCounter;
    private int tmpVarCounter;

    //variable processing
    private VariableStorage vst = new VariableStorage();
    private Map<String, Integer> datatypeSize = new HashMap<String, Integer>();

    private Pair<String, String> variables;

    private Map<String, FunctionStorage> funcStorage = new HashMap<>();

    private Map<String, Pair<String, Map<String, String>>> functions = new HashMap<>();

    private List<String> bssSection = new LinkedList<>();
    private List<String> dataSection = new LinkedList<>();

    private Map<String, StructStorage> strStorage = new HashMap<>();
    private StructStorage str;

    private boolean assignmentStructFl;
    private boolean assignmentStringFl;
    private boolean structToVarAss = false;

    private boolean isStructCall = false;

    private void setOptimize(){
        optimize = true;
    }

    private int getDatatypeSize(String s){
        return datatypeSize.get(s);
    }

    public void setFunctionStorage(Map<String, FunctionStorage> st){
        funcStorage = st;
    }

    public Pair<String, List<String>> visitProgramm(@NotNull GrammarParser.ProgrammContext ctx) {
        labelCounter = 0;
        tmpVarCounter = 0;

        List<String> code = new LinkedList<String>();
        datatypeSize.put("int", 4);
        datatypeSize.put("string", 4);
        datatypeSize.put("bool", 4);

        code.add("extern printf");
        code.add("extern scanf\n");
        code.add("extern strcmp");
        code.add("extern strcat");
        code.add("extern strcpy\n\n");

        code.add("extern malloc");
        code.add("extern memcpy");
        code.add("extern free\n\n");

        for (GrammarParser.StructDefinitionContext sdc: ctx.structDefinition()){
            code.addAll(visitStructDefinition(sdc).getValue());
            code.add("\n\n");
        }

        for (GrammarParser.GlobalVariableDeclarationContext gvdc: ctx.globalVariableDeclaration()){
            //пофиксить
            //code.addAll(visitGlobalVariableDeclaration(gvdc).getValue());
            visitGlobalVariableDeclaration(gvdc);
        }

        code.add("section .text");
        code.add("global main\n");

        for (GrammarParser.FunctionDefinitionContext fdc : ctx.functionDefinition()) {
            code.addAll(visitFunctionDefinition(fdc).getValue());
            code.add("\n\n");
        }

        code.add("section .data");
        code.add("int_format dd \"%d\", 10, 0");
        code.add("str_format dd \"%s\", 10, 0");
        code.addAll(dataSection);
        code.add("\nsection .bss");
        code.addAll(bssSection);

        return new Pair<String, List<String>>(null, code);
    }

    public Pair<String, List<String>> visitFunctionDefinition(@NotNull GrammarParser.FunctionDefinitionContext ctx) {
        List<String> code = new ArrayList<String>();
        List<String> assignArg = new ArrayList<>();

        String returnType = visitFunctionReturnType(ctx.functionReturnType()).getKey();
        String functionName = ctx.IDENTIFIER().getText();

        if (!functionName.equals("main")) {
            vst.enterBlock(functionName);
            code.add(functionName + ":");
            code.add("push ebp\nmov ebp, esp");

            FunctionStorage fs = new FunctionStorage();
            int totalArgStackSize = 0;
            if (ctx.variableList() != null){
                for (int i = 0; i < ctx.variableList().typeSpecifier().size(); i++) {
                    String argType = visitTypeSpecifier(ctx.variableList().typeSpecifier(i)).getKey();
                    String argName = ctx.variableList().IDENTIFIER(i).getText();

                    if (!(argType.equals("int") || argType.equals("bool") || argType.equals("string"))){
                        //fs.addArgument(argName, argType, getDatatypeSize(argType));
                        //заменить на
                        fs.addArgument(argName, argType, 4);

                        //убрать выделение структкр в bss секции
//                        bssSection.add(vst.getStructName(argName) + ": resb " + argType + ".size");
//                        try {
//                            assignArg.add("mov edx, " + fs.getArgAdress(argName) );
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        assignArg.add("mov [" + vst.getStructName(argName) + "], edx" );
//                        ///

                        try {
                            //vst.addFuncArgument(argName, fs.getArgType(argName), "[" + vst.getStructName(argName) + "]");
                            //заменить на
                            vst.addFuncArgument(argName, fs.getArgType(argName), fs.getArgAdress(argName));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        //fs.addArgument(argName, argType, getDatatypeSize(argType));
                        //заменить на
                        fs.addArgument(argName, argType, 4);
                        try {
                            vst.addFuncArgument(argName, fs.getArgType(argName), fs.getArgAdress(argName));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    //totalArgStackSize += getDatatypeSize(visitTypeSpecifier(ctx.variableList().typeSpecifier(i)).getKey());
                    //заменить на
                    totalArgStackSize += 4;
                }
            }
            fs.setReturnType(returnType);
            funcStorage.put(functionName, fs);

            Pair<String, List<String>> body = visitFunctionBody(ctx.functionBody());
            if (!returnType.equals(body.getKey())) {
                throw new IllegalArgumentException("Function type and return type does not match");
            }
            code.add("sub esp, " + String.valueOf(vst.getLocalVarStackSize() + 4));
            code.addAll(assignArg);
            code.addAll(body.getValue());

            code.addAll(vst.garbageCollector());

            vst.exitBlock();
            code.add("mov esp, ebp\npop ebp");
            if (totalArgStackSize != 0)
                code.add("ret " + totalArgStackSize);
            else
                code.add("ret");
        } else {
            code.add(functionName + ":");
            vst.enterBlock(functionName);
            code.add("push ebp\nmov ebp, esp");
            Pair<String, List<String>> body = visitFunctionBody(ctx.functionBody());
            code.add("sub esp, " + String.valueOf(vst.getLocalVarStackSize() + 4));
            code.addAll(body.getValue());
            //code.addAll(vst.garbageCollector());
            vst.exitBlock();
            code.add("mov esp, ebp\npop ebp");
            code.add("ret");
        }
        return new Pair<String, List<String>>(returnType, code);
    }

    public Pair<String, List<String>> visitStructDefinition(GrammarParser.StructDefinitionContext ctx) {
        List<String> code = new ArrayList<String>();
        String structType = ctx.IDENTIFIER().getText();
        str = new StructStorage();
        Pair<String, List<String>> structBody = visitStructBody(ctx.structBody());


        code.add("struc " + structType);
        code.addAll(structBody.getValue());
        code.add(".size:");
        code.add("endstruc");

        //add size of structure

        //returnType = (((GrammarParser.StructDefinitionContext) ctx.structBody().getParent())).IDENTIFIER().getText();
        return new Pair<String, List<String>>(structType, code);
    }

    public Pair<String, List<String>> visitVariableList(GrammarParser.VariableListContext ctx) {
        throw new UnsupportedOperationException();
    }

    public Pair<String, List<String>> visitFunctionReturnType(GrammarParser.FunctionReturnTypeContext ctx) {
        if (ctx.VOID() != null)
            return new Pair<String, List<String>>(ctx.VOID().toString(), null);
        else
            return visitTypeSpecifier(ctx.typeSpecifier());
    }

    public Pair<String, List<String>> visitTypeSpecifier(GrammarParser.TypeSpecifierContext ctx) {
        if (ctx.primitiveTypeSpecifier() != null)
            return visitPrimitiveTypeSpecifier(ctx.primitiveTypeSpecifier());
        else
            return visitStructType(ctx.structType());
    }

    public Pair<String, List<String>> visitStructType(GrammarParser.StructTypeContext ctx) {
        String structType = ctx.IDENTIFIER().getText();
        if (datatypeSize.containsKey(structType)){
            return new Pair<String, List<String>>(structType, null);
        }
        throw new UnsupportedOperationException("Unknown type " + structType);
    }

    public Pair<String, List<String>> visitPrimitiveTypeSpecifier(GrammarParser.PrimitiveTypeSpecifierContext ctx) {
        if (ctx.STRING() != null) {
            return new Pair<String, List<String>>(ctx.STRING().getText(), null);
        } else if (ctx.BOOL() != null) {
            return new Pair<String, List<String>>(ctx.BOOL().getText(), null);
        } else if (ctx.INT() != null) {
            return new Pair<String, List<String>>(ctx.INT().getText(), null);
        }
        throw new UnsupportedOperationException("Unsupported type");
    }


    public Pair<String, List<String>> visitFunctionBody(GrammarParser.FunctionBodyContext ctx) {
        List<String> code = new LinkedList<String>();
        for (GrammarParser.StatementContext sc : ctx.statement()) {
            code.addAll(visitStatement(sc).getValue());
        }

        String returnType = "void";

        if(ctx.expression() != null) {
            Pair<String, List<String>> returnStatement = visitExpression(ctx.expression());
            returnType = returnStatement.getKey();
            code.addAll(returnStatement.getValue());
            if (returnStatement.getKey().equals("int") || returnStatement.getKey().equals("bool"))
                code.add("pop eax");
            else {

                if (returnStatement.getKey().equals("string")) {
                    code.add("push 256");
                    code.add("call malloc");
                    code.add("add esp, 4");

                    code.add("pop ebx");
                    code.add("push ebx");
                    code.add("push eax");
                    code.add("call strcpy");
                    code.add("add esp, 8");
                    code.add("push eax");
                    code.add("pop eax");
                } else {
                    code.add("push " + getDatatypeSize(returnStatement.getKey()));
                    code.add("call malloc");
                    code.add("add esp, 4");

                    code.add("pop ebx");
                    code.add("push " + getDatatypeSize(returnStatement.getKey()));
                    code.add("push ebx");
                    code.add("push eax");
                    code.add("call memcpy");
                    code.add("add esp, 12");
                    code.add("push eax");
                    code.add("pop eax");
                }
            }
        }

        return new Pair<String, List<String>>(returnType, code);
    }

    @Override
    public Pair<String, List<String>> visitStructFieldDeclaration(GrammarParser.StructFieldDeclarationContext ctx) {
        List<String> code = new LinkedList<String>();
        String name = ctx.IDENTIFIER().getText();
        String type = visitTypeSpecifier(ctx.typeSpecifier()).getKey();

        if (datatypeSize.containsKey(type)){
            int size = datatypeSize.get(type);
            str.addField(name, type, size);
            if (type.equals("int") || type.equals("string") || type.equals("bool"))
                code.add("." + name + " RESD 1");
            else
                code.add("." + name + " RESB " + type + ".size");
        } else {
            throw new IllegalArgumentException("Unsupported datatype: " + type);
        }
        return new Pair<>(type, code);
    }

    public Pair<String, List<String>> visitStructBody(GrammarParser.StructBodyContext ctx) {
        List<String> code = new LinkedList<String>();
        for (GrammarParser.StructFieldDeclarationContext sc : ctx.structFieldDeclaration()) {
            code.addAll(visitStructFieldDeclaration(sc).getValue());
        }
        String structType = (((GrammarParser.StructDefinitionContext) ctx.getParent())).IDENTIFIER().getText();
        strStorage.put(structType, str);
        //datatypeSize.put(structType, 4);
        //заменить на
        datatypeSize.put(structType, str.getStructSize());
        return new Pair<String, List<String>>(structType, code);
    }

    @Override
    public Pair<String, List<String>> visitStatement(GrammarParser.StatementContext ctx) {
        if (ctx.localVariableDeclaration() != null)
            return visitLocalVariableDeclaration(ctx.localVariableDeclaration());
        else if(ctx.conditionStatement() != null)
            return visitConditionStatement(ctx.conditionStatement());
        else if(ctx.compoundStatement() != null)
            return visitCompoundStatement(ctx.compoundStatement());
        else if(ctx.localAssignment() != null)
            return visitLocalAssignment(ctx.localAssignment());
        else if(ctx.functionCall() != null)
            return visitFunctionCall(ctx.functionCall());
        else if(ctx.whileStatement() != null)
            return visitWhileStatement(ctx.whileStatement());
//        else if(ctx.unionStatement() != null) {
//            return visitUnionStatement(ctx.unionStatement());
//        }
        throw new UnsupportedOperationException("Unknown statement");
    }

    @Override
    public Pair<String, List<String>> visitUnionStatement(GrammarParser.UnionStatementContext ctx) {
        return null;
    }

    @Override
    public Pair<String, List<String>> visitConditionStatement(GrammarParser.ConditionStatementContext ctx) {
        List<String> code = new LinkedList<String>();
        labelCounter += 2;
        int localLabelCounter = labelCounter + 2;

        Pair<String, List<String>> expr = visitExpression(ctx.expression());
        if (expr.getKey().equals("bool")){
            code.addAll(expr.getValue());
            code.add("pop eax");
            if (ctx.ELSE() == null){ //if() {};
                code.add("cmp eax, 1");

                localLabelCounter++;
                code.add("jne L" + localLabelCounter);
                code.addAll(visitCompoundStatement(ctx.compoundStatement(0)).getValue());
                code.add("L" + localLabelCounter + ":");
                labelCounter += 4;

            } else { // if() {} else {}
                code.add("cmp eax, 1");
                localLabelCounter++;
                code.add("jne L" + localLabelCounter);
                code.addAll(visitCompoundStatement(ctx.compoundStatement(0)).getValue());
                code.add("jmp L" + (localLabelCounter + 1));
                code.add("L" + localLabelCounter + ":");
                code.addAll(visitCompoundStatement(ctx.compoundStatement(1)).getValue());
                code.add("L" + (localLabelCounter + 1)  + ":");
                labelCounter += 4;

            }
        } else {
            throw new IllegalArgumentException("Value of the condition must be boolean");
        }


        return new Pair<>("condition", code);
    }

    @Override
    public Pair<String, List<String>> visitCompoundStatement(GrammarParser.CompoundStatementContext ctx) {
        List<String> code = new LinkedList<String>();

        for (int i = 0; i < ctx.statement().size(); i++) {
            code.addAll(visitStatement(ctx.statement(i)).getValue());
        }
        return new Pair<>("compound", code);
    }

    @Override
    public Pair<String, List<String>> visitWhileStatement(GrammarParser.WhileStatementContext ctx) {
        List<String> code = new LinkedList<String>();
        labelCounter += 2;
        int localLabelCounter = labelCounter + 2;

        Pair<String, List<String>> expr = visitExpression(ctx.expression());
        if (expr.getKey().equals("bool")){
            code.addAll(expr.getValue());
            code.add("pop eax");
            code.add("cmp eax, 1");
            localLabelCounter++;
            code.add("jne L" + localLabelCounter);
            code.add("L" + (localLabelCounter + 1) + ":");
            code.addAll(visitCompoundStatement(ctx.compoundStatement()).getValue());
            code.addAll(visitExpression(ctx.expression()).getValue());
            code.add("pop eax");
            code.add("cmp eax, 1");
            code.add("je L" + (localLabelCounter + 1));
            code.add("L" + localLabelCounter + ":");
            labelCounter += 4;

        } else {
            throw new IllegalArgumentException("Value inside the \"while\" must be boolean");
        }
        return new Pair<>("while", code);
    }

    private String getFormat(String format) {
        switch (format){
            case "int":
                return "int_format";
            case "bool":
                return "int_format";
            case "string":
                return "str_format";
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public Pair<String, List<String>> visitFunctionCall(GrammarParser.FunctionCallContext ctx) {
        List<String> code = new LinkedList<String>();
        List<String> args = new ArrayList<String>();
        String format = "";

        if (ctx.READ() != null){
            String name = ctx.IDENTIFIER().getText();
            args.add(name);

            String type = "";
            String adress = "";
            boolean funcArg = false;
            FunctionStorage f = new FunctionStorage();
            for (FunctionStorage i: funcStorage.values()){
                funcArg = i.containsArg(name);
                if (funcArg){
                    f = i;
                    break;
                }
            }
            if (vst.containsVariable(name)){
                try {
                    adress = vst.getVariableAddress(name);
                    //code.add("lea ebx, " + vst.getVariableAddress(name));
                    type = vst.getVariableType(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (funcArg) {
                    try {
                        //code.add("lea ebx, " + f.getArgAdress(name));
                        adress = f.getArgAdress(name);
                        type = f.getArgType(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            format = getFormat(type);
            if (type.equals("int")) {
                code.add("pusha");
                code.add("lea ebx, " + adress);
                code.add("push ebx");
                code.add("push " + format);
                code.add("call scanf");
                code.add("add esp, 8");
                code.add("popa");
                return new Pair<>("void", code);
            }
            if (type.equals("string")){
                code.add("pusha");


                if (adress.startsWith("[ebp")){
                    code.add("push 256");
                    code.add("call malloc");
                    code.add("add esp, 4");
                    code.add("mov " + adress + ", eax");
                    code.add("push dword " + adress);
                }
                else {
                    code.add("push " + adress.substring(1, adress.length() - 1));
                }
                code.add("push str_format");
                code.add("call scanf");
                code.add("add esp, 8");
                code.add("popa");

                return new Pair<>("void", code);
            }

        } else {
            if (ctx.WRITE() != null){
                Pair<String, List<String>> arg = visitExpression(ctx.expression());
                code.addAll(arg.getValue());
                format = getFormat(arg.getKey());
                code.add("push " + format);
                code.add("call printf");
                code.add("add esp, 8");
                //code.add("popa");
                return new Pair<>("void", code);
            } else {
                if ((ctx.IDENTIFIER() != null) && (funcStorage.containsKey(ctx.IDENTIFIER().getText()))){
                    String funcName = ctx.IDENTIFIER().getText();
                    if (ctx.expressionList() != null)
                        for (int i = ctx.expressionList().expression().size()-1; i >= 0; i--){
                            Pair<String, List<String>> exprRes = visitExpression(ctx.expressionList().expression(i));
                            if (exprRes.getKey().equals(funcStorage.get(funcName).getArgType(i)))
                                code.addAll(exprRes.getValue());
                            else
                                throw new IllegalArgumentException("Type of some arguments doen's match the defenition");
                        }
                   code.add("call " + ctx.IDENTIFIER().getText());
                   return new Pair<>(funcStorage.get(funcName).getReturnType(), code);
                }
            }
        }
        throw new UnsupportedOperationException("Unknown function " + ctx.getText());
    }

    @Override
    public Pair<String, List<String>> visitStructCall(GrammarParser.StructCallContext ctx) {
        List<String> code = new ArrayList<String>();
        String name = ctx.IDENTIFIER(0).getText();
        String type = "", field = "";
        String register = "";
        String  adress = "";
        if(vst.containsVariable(name)){
            //adress += vst.getStructName(name);

            try {
                code.add("mov ecx, " + vst.getVariableAddress(name));
            } catch (Exception e) {
                e.printStackTrace();
            }
            register = "ecx";

            for(int i = 1; i < ctx.IDENTIFIER().size(); i++) {
                if (i == 1) {
                    try {
                        type = vst.getVariableType(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        type = strStorage.get(type).getFieldTypeStr(field);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //type = structureBodyStorage.get(type).get(field);
                }
                field = ctx.IDENTIFIER(i).getText();
                adress += " + " + type + "."  + field;
            }

            if (structToVarAss) {
                code.add("push " + register);
                code.add(adress);
                isStructCall = true;
                //structToVarAss = false;
            } else {
                if (assignmentStructFl){
                    code.add(register + adress);
                    //code.add("mov dword [" + adress + "], eax");
                    assignmentStructFl = false;
                }
                else {
                    code.add("push dword [" + register + adress + "]");
                }
            }

//            if (adress.length() > 0)
//                isStructCall = true;

            try {
                return new Pair<String, List<String>>(strStorage.get(type).getFieldTypeStr(field), code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new UnsupportedOperationException("Unsupported structure or structure field calling " + ctx.getText());

    }

    @Override
    public Pair<String, List<String>> visitExpressionList(GrammarParser.ExpressionListContext ctx) {
        List<String> code = new ArrayList<String>();

//        String funcName = ((GrammarParser.FunctionCallContext)(ctx.getParent())).IDENTIFIER().getText();
//        for (int i = ctx.expression().size()-1; i >= 0; i--){
//            if (visitExpression(ctx.expression(i)).getKey() == funcStorage.get(funcName).getArgType(i))
//                code.addAll(visitExpression(ctx.expression(i)).getValue());
//            else
//                throw new IllegalArgumentException("Type of some arguments doen's match the defenition");
//        }
        return new Pair<String, List<String>>(null, code);
    }

    @Override
    public Pair<String, List<String>> visitExpression(GrammarParser.ExpressionContext ctx) {
        labelCounter += 2;
        int localLabelCounter = labelCounter + 2;
        Pair<String, List<String>> andExpr = visitAndExpr(ctx.andExpr(0));
        if (ctx.andExpr().size() > 1) {
            for (int i = 1; i < ctx.andExpr().size(); ++i) {
                Pair<String, List<String>> nextAndExpr = visitAndExpr(ctx.andExpr(i));
                if (andExpr.getKey().equals("bool") && nextAndExpr.getKey().equals("bool")) {
                    andExpr.getValue().addAll(nextAndExpr.getValue());

                    localLabelCounter++;
                    andExpr.getValue().add("pop ebx");
                    andExpr.getValue().add("pop edx");
                    andExpr.getValue().add("cmp edx, 1");
                    andExpr.getValue().add("je L" + localLabelCounter);
                    andExpr.getValue().add("cmp ebx, 1");
                    andExpr.getValue().add("jne L" + (localLabelCounter - 1));
                    andExpr.getValue().add("L" + (localLabelCounter) + ":");
                    andExpr.getValue().add("mov eax, 1");
                    andExpr.getValue().add("jmp L" + (localLabelCounter + 1));
                    andExpr.getValue().add("L" + (localLabelCounter - 1) + ":");
                    andExpr.getValue().add("mov eax, 0");
                    andExpr.getValue().add("L" + (localLabelCounter + 1) + ":");
                    labelCounter += 4;
                    andExpr.getValue().add("push eax");
                    andExpr = new Pair<>("bool", andExpr.getValue());
                } else {
                    throw new IllegalArgumentException("Both values must be boolean");
                }
            }
        } else {
//            andExpr.getValue().add("pop eax");
//            andExpr.getValue().add("push eax");
        }
        return andExpr;
    }

    @Override
    public Pair<String, List<String>> visitAndExpr(GrammarParser.AndExprContext ctx) {
        labelCounter += 2;
        int localLabelCounter = labelCounter + 2;
        Pair<String, List<String>> equalityExpr = visitEqualityExpr(ctx.equalityExpr(0));
        if (ctx.equalityExpr().size() > 1)
            for (int i = 1; i < ctx.equalityExpr().size(); ++i) {
                Pair<String, List<String>> nextAndExpr = visitEqualityExpr(ctx.equalityExpr(i));
                if (equalityExpr.getKey().equals("bool") && nextAndExpr.getKey().equals("bool")) {
                    equalityExpr.getValue().addAll(nextAndExpr.getValue());
                    localLabelCounter++;
                    equalityExpr.getValue().add("pop ebx");
                    equalityExpr.getValue().add("pop edx");

                    equalityExpr.getValue().add("cmp edx, 1");
                    equalityExpr.getValue().add("jne L" + localLabelCounter);
                    equalityExpr.getValue().add("cmp ebx, 1");
                    equalityExpr.getValue().add("jne L" + localLabelCounter);
                    equalityExpr.getValue().add("mov eax, 1");
                    equalityExpr.getValue().add("jmp L" + (localLabelCounter + 1));

                    equalityExpr.getValue().add("L" + (localLabelCounter) + ":");
                    equalityExpr.getValue().add("mov eax, 0");

                    equalityExpr.getValue().add("L" + (localLabelCounter + 1) + ":");
                    equalityExpr.getValue().add("push eax");
                    equalityExpr = new Pair<>("bool", equalityExpr.getValue());
                    localLabelCounter += 4;

                } else {
                    throw new IllegalArgumentException("Both values must be boolean");
                }
            }
        else {
//            equalityExpr.getValue().add("pop eax");
//            equalityExpr.getValue().add("push eax");
        }
        return equalityExpr;
    }

    private String getEqualityOperator(String op){
        switch (op) {
            case "==":
                return "je ";
            case "!=":
                return "jne ";
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public Pair<String, List<String>> visitEqualityExpr(GrammarParser.EqualityExprContext ctx) {
        Pair<String, List<String>> relationExpr = visitRelationExpr(ctx.relationExpr(0));
        labelCounter += 2;
        int localLabelCounter = labelCounter + 2;
        String operator = "";
        if (ctx.relationExpr().size() > 1)
            for (int i = 1; i < ctx.relationExpr().size(); ++i) {
                Pair<String, List<String>> nextRelationExpr = visitRelationExpr(ctx.relationExpr(i));
                if ((relationExpr.getKey().equals(nextRelationExpr.getKey())) &&
                        (relationExpr.getKey().equals("int") || relationExpr.getKey().equals("bool"))) {
                    operator = getEqualityOperator(ctx.getChild(2 * i - 1).getText());
                    relationExpr.getValue().addAll(nextRelationExpr.getValue());
                    localLabelCounter++;
                    relationExpr.getValue().add("pop ebx");
                    relationExpr.getValue().add("pop edx");

                    relationExpr.getValue().add("cmp edx, ebx");
                    relationExpr.getValue().add(operator + " L" + localLabelCounter);
                    relationExpr.getValue().add("mov eax, 0");
                    relationExpr.getValue().add("jmp L" + (localLabelCounter + 1));

                    relationExpr.getValue().add("L" + (localLabelCounter) + ":");
                    relationExpr.getValue().add("mov eax, 1");

                    relationExpr.getValue().add("L" + (localLabelCounter + 1) + ":");
                    relationExpr.getValue().add("push eax");
                    relationExpr = new Pair<>("bool", relationExpr.getValue());
                    labelCounter += 4;

                } else {
                    if (relationExpr.getKey().equals("string") && nextRelationExpr.getKey().equals("string")) {
                        operator = getEqualityOperator(ctx.getChild(2 * i - 1).getText());
                        localLabelCounter++;
                        relationExpr.getValue().addAll(nextRelationExpr.getValue());
                        relationExpr.getValue().add("call strcmp");
                        relationExpr.getValue().add("add esp, 8");
                        relationExpr.getValue().add("mov ebx, eax");
                        relationExpr.getValue().add("cmp ebx, 0");
                        relationExpr.getValue().add(operator + "L" + localLabelCounter);
                        relationExpr.getValue().add("mov eax, 0");
                        relationExpr.getValue().add("jmp " + (localLabelCounter + 1));

                        relationExpr.getValue().add("L" + (localLabelCounter) + ":");
                        relationExpr.getValue().add("mov eax, 1");

                        relationExpr.getValue().add("L" + (localLabelCounter + 1) + ":");
                        relationExpr.getValue().add("push eax");
                        relationExpr = new Pair<>("bool", relationExpr.getValue());
                        labelCounter += 4;
                    } else
                        throw new IllegalArgumentException("Both values must be the same type");
                }
            }
        else {
//            relationExpr.getValue().add("pop eax");
//            relationExpr.getValue().add("push eax");
        }
        //relationExpr = new Pair<>("bool", relationExpr.getValue());
        return relationExpr;
    }

    private String getRelationOperator(String op){
        switch (op) {
            case ">=":
                return "jae ";
            case ">":
                return "ja ";
            case "<=":
                return "jbe ";
            case "<":
                return "jb ";
            default:
                throw new IllegalArgumentException();
        }
    }


    @Override
    public Pair<String, List<String>> visitRelationExpr(GrammarParser.RelationExprContext ctx) {
        Pair<String, List<String>> additiveExpr = visitAdditiveExpr(ctx.additiveExpr(0));
        labelCounter += 2;
        int localLabelCounter = labelCounter + 2;
        if (ctx.additiveExpr().size() > 1)
            for (int i = 1; i < ctx.additiveExpr().size(); ++i) {
                Pair<String, List<String>> nextAdditiveExpr = visitAdditiveExpr(ctx.additiveExpr(i));
                if ((additiveExpr.getKey().equals(nextAdditiveExpr.getKey())) &&
                        (additiveExpr.getKey().equals("int") || nextAdditiveExpr.getKey().equals("bool"))) {
                    String operator = getRelationOperator(ctx.getChild(2 * i - 1).getText());
                    additiveExpr.getValue().addAll(nextAdditiveExpr.getValue());
                    localLabelCounter++;
                    additiveExpr.getValue().add("pop ebx");
                    additiveExpr.getValue().add("pop edx");

                    additiveExpr.getValue().add("cmp edx, ebx");
                    additiveExpr.getValue().add(operator + " L" + localLabelCounter);
                    additiveExpr.getValue().add("mov eax, 0");
                    additiveExpr.getValue().add("jmp L" + (localLabelCounter + 1));

                    additiveExpr.getValue().add("L" + (localLabelCounter) + ":");
                    additiveExpr.getValue().add("mov eax, 1");

                    additiveExpr.getValue().add("L" + (localLabelCounter + 1) + ":");
                    additiveExpr.getValue().add("push eax");
                    labelCounter += 4;
                    additiveExpr = new Pair<>("bool", additiveExpr.getValue());
                } else {
                    throw new IllegalArgumentException("Both values must be integer or boolean");
                }
            }
        else {
//            additiveExpr.getValue().add("pop eax");
//            additiveExpr.getValue().add("push eax");
        }
        //additiveExpr = new Pair<>("")
        return additiveExpr;
    }

    private String getAddOperator(String op){
        switch (op) {
            case "+":
                return "add";
            case "-":
                return "sub";
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public Pair<String, List<String>> visitAdditiveExpr(GrammarParser.AdditiveExprContext ctx) {
        Pair<String, List<String>> multyplicationExpr = visitMultyplicationExpr(ctx.multyplicationExpr(0));
        String operator = "";
        if (ctx.multyplicationExpr().size() > 1) {
            for (int i = 1; i < ctx.multyplicationExpr().size(); i++) {
                Pair<String, List<String>> nextMultyplicationExpr = visitMultyplicationExpr(ctx.multyplicationExpr(i));
                operator = getAddOperator(ctx.getChild(2 * i - 1).getText());
                if(multyplicationExpr.getKey().equals("int") && nextMultyplicationExpr.getKey().equals("int")){
                    multyplicationExpr.getValue().addAll(nextMultyplicationExpr.getValue());
                    multyplicationExpr.getValue().add("pop ebx");
                    multyplicationExpr.getValue().add("pop edx");
                    multyplicationExpr.getValue().add(operator + " edx, ebx");
                    multyplicationExpr.getValue().add("mov eax, edx");
                    multyplicationExpr.getValue().add("push eax");
                    multyplicationExpr = new Pair<>("int", multyplicationExpr.getValue());
                } else {
                  if (multyplicationExpr.getKey().equals("string") && nextMultyplicationExpr.getKey().equals("string") && operator.equals("add"))  {
                      multyplicationExpr.getValue().addAll(nextMultyplicationExpr.getValue());

                      multyplicationExpr.getValue().add("push 256");
                      multyplicationExpr.getValue().add("call malloc");
                      multyplicationExpr.getValue().add("add esp, 4");

                      multyplicationExpr.getValue().add("pop ebx");
                      multyplicationExpr.getValue().add("pop edx");

                      multyplicationExpr.getValue().add("push edx");
                      multyplicationExpr.getValue().add("push eax");
                      multyplicationExpr.getValue().add("call strcpy");
                      multyplicationExpr.getValue().add("add esp, 8");

                      multyplicationExpr.getValue().add("push ebx");
                      multyplicationExpr.getValue().add("push eax");
                      multyplicationExpr.getValue().add("call strcat");
                      multyplicationExpr.getValue().add("add esp, 8");

                      multyplicationExpr.getValue().add("push eax");
                      multyplicationExpr = new Pair<>("string", multyplicationExpr.getValue());
                  } else {
                      throw new IllegalArgumentException("Both values must be integer or strings for concatetaion");
                  }
                }

            }
        } else {
//            multyplicationExpr.getValue().add("pop eax");
//            multyplicationExpr.getValue().add("push eax");
        }
        return multyplicationExpr;
    }

    private List<String> getMulOperExpression(String op) {
        List<String> lst = new ArrayList<>();
        if (op.equals("*")) {
            lst.add("imul ebx");
        } else if (op.equals("/")) {
            lst.add("mov edx, 0");
            lst.add("idiv ebx");
        } else if (op.equals("%")){
            lst.add("mov edx, 0");
            lst.add("idiv ebx");
            lst.add("mov eax, edx");
        } else {
            throw new IllegalArgumentException();
        }
        return lst;
    }

    @Override
    public Pair<String, List<String>> visitMultyplicationExpr(GrammarParser.MultyplicationExprContext ctx) {
        Pair<String, List<String>> atomicExpr = visitAtomicExpr(ctx.atomicExpr(0));
        if (ctx.atomicExpr().size() > 1) {
            for (int i = 1; i < ctx.atomicExpr().size(); i++) {
                Pair<String, List<String>> nextAtomicExpr = visitAtomicExpr(ctx.atomicExpr(i));
                if(atomicExpr.getKey().equals("int") && nextAtomicExpr.getKey().equals("int")){
                    atomicExpr.getValue().addAll(nextAtomicExpr.getValue());
                    atomicExpr.getValue().add("pop ebx");
                    atomicExpr.getValue().add("pop eax");

                    String op = ctx.getChild(2 * i - 1).getText();
                    List<String> lst = getMulOperExpression(op);
                    atomicExpr.getValue().addAll(lst);

                    atomicExpr.getValue().add("push eax");
                    atomicExpr = new Pair<>("int", atomicExpr.getValue());
                } else {
                    throw new IllegalArgumentException("Both values must be integer");
                }
            }
        } else {
//            atomicExpr.getValue().add("pop eax");
//            atomicExpr.getValue().add("push eax");
        }
        return atomicExpr;
    }

    @Override
    public Pair<String, List<String>> visitAtomicExpr(GrammarParser.AtomicExprContext ctx) {
        List<String> code = new ArrayList<String>();
        if (ctx.primitiveExpr() != null) {
            Pair<String, List<String>> exprRes = visitPrimitiveExpr(ctx.primitiveExpr());
            code.addAll(exprRes.getValue());
            return new Pair<>(exprRes.getKey(), code);
        } else {
            if (ctx.functionCall() != null && funcStorage.containsKey(ctx.functionCall().IDENTIFIER().getText())
                    && !funcStorage.get(ctx.functionCall().IDENTIFIER().getText()).getReturnType().equals("void")){
                code.addAll(visitFunctionCall(ctx.functionCall()).getValue());
                code.add("push eax");
                return new Pair<>(visitFunctionCall(ctx.functionCall()).getKey(), code);
            } else {
                //Boolean c = vst.containsVariable(ctx.IDENTIFIER().getText());

                if (ctx.IDENTIFIER() != null &&  vst.containsVariable(ctx.IDENTIFIER().getText())) {
                    try {
                        String var = ctx.IDENTIFIER().getText();
                        String type = vst.getVariableType(var);
                        //if (!(type.equals("int") || type.equals("bool") || type.equals("string")))

                        if (vst.getVariableType(var).equals("string") && vst.isGlobal(var))
                            code.add("push " + vst.getVariableAddress(var).substring(1, vst.getVariableAddress(var).length() - 1));
                        else
                            code.add("push dword " + vst.getVariableAddress(var));

//                        if (vst.getVariableAddress(var).startsWith("[ebp"))
//                            code.add("push dword " + vst.getVariableAddress(var));
//                        else
//                            code.add("push " + vst.getVariableAddress(var).substring(1, vst.getVariableAddress(var).length() - 1));
                        return new Pair<>(vst.getVariableType(var), code);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (ctx.structCall() != null) {
                        Pair<String, List<String>> structRes = visitStructCall(ctx.structCall());
                        code.addAll(structRes.getValue());
                        return new Pair<>(structRes.getKey(), code);
                    } else {
                        if (ctx.expression() != null){
                            Pair<String, List<String>> exprRes = visitExpression(ctx.expression());
                            code.addAll(exprRes.getValue());
                            return new Pair<>(exprRes.getKey(), code);
                        }
                    }

                }
            }
        }
        throw new IllegalArgumentException("Not supported type!" + ctx.getText());
    }

    private String getBooleanValue(String s){
        switch (s) {
            case "true":
                return "1";
            case "false":
                return "0";
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public Pair<String, List<String>> visitPrimitiveExpr(GrammarParser.PrimitiveExprContext ctx) {
        List<String> code = new ArrayList<String>();
        if (ctx.BOOL_LITERAL() != null){
            code.add("push " + getBooleanValue(ctx.BOOL_LITERAL().getText()));
            return new Pair<>("bool", code);
        } else {
            if (ctx.INTEGER_LITERAL() != null) {
                code.add("push " + ctx.INTEGER_LITERAL().getText());
                return new Pair<>("int", code);
            } else {
                if (ctx.STRING_LITERAL() != null) {
                    tmpVarCounter++;
                    String var = ("tmp" + tmpVarCounter);
                    try {
                        dataSection.add(var + ": dd " + ctx.STRING_LITERAL().getText() + ", 0");
                        //vst.addGlobalVariable(var, "string", 4);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    code.add("push " + var);
                    return new Pair<>("string", code);
                }
            }
        }
        throw new IllegalArgumentException("Not supported value!");
    }

    @Override
    public Pair<String, List<String>> visitIndex(GrammarParser.IndexContext ctx) {
        return super.visitIndex(ctx);
    }

    @Override
    public Pair<String, List<String>> visitGlobalVariableDeclaration(GrammarParser.GlobalVariableDeclarationContext ctx) {
        List<String> code = new ArrayList<String>();

        String type = visitTypeSpecifier(ctx.typeSpecifier()).getKey();
        if (datatypeSize.containsKey(type)) {
            for (int i = 0; i < ctx.globalAssignment().size(); i++) {
                Pair<String, List<String>> globalRes = visitGlobalAssignment(ctx.globalAssignment(i));
                String name = globalRes.getKey();
                if (!vst.containsVariable(name)){
                    try {
                        vst.addGlobalVariable(name, type, 4);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    throw new IllegalArgumentException("Redefenition: " + name);
                }
                code.addAll(globalRes.getValue());
            }
            return new Pair<>(type, code);
        } else {
            throw new IllegalArgumentException("Not supported datatype:" + type);
        }
    }

    @Override
    public Pair<String, List<String>> visitGlobalAssignment(GrammarParser.GlobalAssignmentContext ctx) {
        List<String> code = new ArrayList<String>();
        String type = visitTypeSpecifier(((GrammarParser.GlobalVariableDeclarationContext)ctx.getParent()).typeSpecifier()).getKey();
        String name = ctx.IDENTIFIER().getText();
        if (ctx.primitiveExpr() != null){
            if (type.equals("int")){
                dataSection.add(name + ": dd " + ctx.primitiveExpr().INTEGER_LITERAL().getText());
            } else {
                if (type.equals("string")) {
                    dataSection.add(name + ": dd " + ctx.primitiveExpr().STRING_LITERAL().getText() + ", 0");
                } else {
                    if (type.equals("bool")) {
                        String val = ctx.primitiveExpr().BOOL_LITERAL().getText();
                        dataSection.add(name + ": dd " + getBooleanValue(ctx.primitiveExpr().BOOL_LITERAL().getText()));
                    } else {
                        throw new IllegalArgumentException("Assignment of variable " + name + " is not allowed in current scope");
                    }
                }
            }
        } else {
            if (type.equals("int")){
                bssSection.add(name + ": resd 1");
            } else {
                if (type.equals("string")) {
                    bssSection.add(name + ": resd 1");
                } else {
                    if (type.equals("bool")) {
                        bssSection.add(name + ": resd 1");
                    } else {
                        if (datatypeSize.containsKey(type)){
                            bssSection.add(name + ": resb " + type + ".size");
                        } else{
                            throw new IllegalArgumentException("Wrong type: " + type);
                        }
                    }
                }
            }
        }
        return new Pair<>(name, code);
    }

    public List<String> assignStructuresByFields(String type, String sourse, String destAdress, String dest, List<String> code){
        StructStorage structStr = strStorage.get(type);
        for (Pair<String, String> j : structStr.getFieldsLst()) {
            if (j.getValue().equals("int") || j.getValue().equals("string") || j.getValue().equals("bool")){
                code.add("mov ecx, " + destAdress);
                code.add(sourse + " + " + type  + "." + j.getKey() + "]");
                try {
                    code.add(dest + " + " + type  + "." + j.getKey() + "], edx");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                List<String> assignCode = new ArrayList<>();
                code.addAll(assignStructuresByFields(j.getValue(), sourse + " + " + type  + "." + j.getKey(),
                        destAdress, dest + " + " + type  + "." + j.getKey(), assignCode));
            }
        }
        return code;
    }

    @Override
    public Pair<String, List<String>> visitLocalAssignment(GrammarParser.LocalAssignmentContext ctx) {
        String type = "";

        boolean strAssign = false;

        List<String> code = new ArrayList<String>();

        int firstBrace = 0;

        if (ctx.structCall() != null) {
            assignmentStructFl = true;
            Pair<String, List<String>> structExpr = visitStructCall(ctx.structCall());

            String addr = structExpr.getValue().get(structExpr.getValue().size() - 1);
            Pair<String, List<String>> expr = visitExpression(ctx.expression());

            if (structExpr.getKey().equals(expr.getKey())) {
                code.addAll(expr.getValue());
                code.add("pop eax");

                if (expr.getKey().equals("int") || expr.getKey().equals("bool")){
                    code.add(structExpr.getValue().get(0));
                    code.add("mov [" + addr + "], eax");
                } else {
                    if (expr.getKey().equals("string")){
                        code.add(structExpr.getValue().get(0));

                        //code.add("pusha");
                        //code.add("push dword [" + addr + "]");
                        //code.add("call free");
                        //code.add("add esp, 4");
                        //code.add("popa");

                        code.add("pusha");
                        code.add("push 256");
                        code.add("call malloc");
                        code.add("add esp, 4");
                        code.add("mov [" + addr + "], eax");
                        code.add("popa");

                        code.add("pusha");
                        code.add("push eax");
                        code.add("push dword [" + addr + "]");
                        code.add("call strcpy");
                        code.add("add esp, 8");
                        code.add("popa");

                    } else {
                        List<String> strCall = new ArrayList<>();
                        String localAdr = structExpr.getValue().get(0).substring(9, structExpr.getValue().get(0).length());

                        code.addAll(assignStructuresByFields(expr.getKey(), "mov edx, [eax ", localAdr, "mov [" + addr, strCall));
                    }
                    //code.add(structExpr.getValue().get(0));
                }
                isStructCall = false;

            } else {
                throw new IllegalArgumentException("Type mismatch: " + structExpr.getKey() +
                        " and " + expr.getKey());
            }
        } else {
            String name = ctx.IDENTIFIER().getText();
            if (ctx.typeSpecifier() != null)
                type = visitTypeSpecifier(ctx.typeSpecifier()).getKey();

            if (ctx.typeSpecifier() != null) {
                firstBrace = 2;
                if (!datatypeSize.containsKey(type)){
                    throw new IllegalArgumentException("Not supported type: " + type);
                }

                if (vst.containsVariable(name)) {
                    throw new IllegalArgumentException("Redefenition of variable:" + name);
                } else {
                    try {
                        //////
//                        if (type.equals("int") || type.equals("bool") || type.equals("string"))
//                            vst.addLocalVariable(name, type, datatypeSize.get(type));
//                        else {
//                            vst.addGlobalVariable(name, type, datatypeSize.get(type));
//                        }
                        ////// заменить на
                        vst.addLocalVariable(name, type, 4);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                firstBrace = 1;
                if (vst.containsVariable(name)) {
                    try {
                        type = vst.getVariableType(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    throw new IllegalArgumentException("There is not such variable: " + name + " in current scope");
                }
            }

            //убрать нах
//            if (!(type.equals("int") || type.equals("bool") || type.equals("string"))) {
//                //name = vst.getStructName(name);
//                bssSection.add(vst.getStructName(name) + ": resb " + type + ".size");
//            }
            //////

            Pair<String, List<String>> exprRes = null;
            if (ctx.expression() != null)
                exprRes = visitExpression(ctx.expression());

            if (ctx.getChild(firstBrace).getText().equals("=") && exprRes.getKey().equals(type)){

                if (datatypeSize.containsKey(type) && !(type.equals("int") || type.equals("bool"))){


                    if (type.equals("string"))
                        strAssign = true;
                    else
                    {
                        code.add("pusha");
                        code.add("push " + getDatatypeSize(type));
                        code.add("call malloc");
                        code.add("add esp, 4");
                        try {
                            code.add("mov " + vst.getVariableAddress(name) + ", eax");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        code.add("popa");
                    }

                    //code.add("popa");
                    //if (type.equals("string"))

//                    else {
//                        try {
//                            code.add("mov ecx, " + vst.getVariableAddress(name));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        code.add("mov [ecx], eax");
//                    }
                }

//                code.addAll(exprRes.getValue());
//                code.add("pop eax");

                if (type.equals("int") ||  type.equals("bool")) {
                    code.addAll(exprRes.getValue());
                    code.add("pop eax");
                    try {
                        code.add("mov " + vst.getVariableAddress(name) + ", eax");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {

                    code.addAll(exprRes.getValue());
                    code.add("pop eax");

                    if (type.equals("string")) {

                        if(strAssign) {
                            code.add("pusha");
                            code.add("push 256");
                            code.add("call malloc");
                            code.add("add esp, 4");
                            try {
                                code.add("mov " + vst.getVariableAddress(name) + ", eax");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            code.add("popa");
                            strAssign = false;
                        }

                        code.add("pusha");
                        code.add("push eax");
                        try {

                            if (vst.getVariableType(name).equals("string") && vst.isGlobal(name))
                                code.add("push " + vst.getVariableAddress(name).substring(1, vst.getVariableAddress(name).length() - 1));
                            else
                                code.add("push dword " + vst.getVariableAddress(name));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        code.add("call strcpy");
                        code.add("add esp, 8");
                        code.add("popa");
                    } else {
                        try {
                            if (ctx.getText().contains(".")){

                                structToVarAss = true;
                                exprRes = visitExpression(ctx.expression());
                                structToVarAss = false;

                                String localAddr = exprRes.getValue().get(exprRes.getValue().size()- 1);

                                code.addAll(exprRes.getValue().subList(0, 2));
                                code.add("pop eax");

                                List<String> casualAssignment = new ArrayList<>();

                                code.addAll(assignStructuresByFields(type, "mov edx, [eax " + localAddr, vst.getVariableAddress(name),
                                        "mov [ecx " , casualAssignment));
                                isStructCall = false;
                            } else {
                                code.addAll(exprRes.getValue());
                                code.add("pop eax");

                                code.add("pusha");
                                code.add("push " + getDatatypeSize(type));
                                code.add("push eax");
                                code.add("push dword " + vst.getVariableAddress(name));
                                code.add("call memcpy");
                                code.add("add esp, 12");
                                code.add("popa");
                            }
                            code.add("mov ecx, " + vst.getVariableAddress(name));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //code.add("mov [ecx], eax");
                        //isStructCall = false;

                    }
                }
            } else {
                if (ctx.getChild(firstBrace).getText().equals("(") && ctx.getChild(firstBrace + 2).getText().equals(")")){

                    try {
                        //раскомментить
                        if(!vst.isAllocated(name)) {
                            code.add("pusha");
                            code.add("push " + getDatatypeSize(vst.getVariableType(name)));
                            code.add("call malloc");
                            code.add("add esp, 4");

                            code.add("mov " + vst.getVariableAddress(name) + ", eax");
                            code.add("popa");
                            vst.setAllocated(name);
                        }

                        for (int i = ctx.expressionList().expression().size()-1; i >= 0; i--){
                            Pair<String, List<String>> exprLstRes = visitExpression(ctx.expressionList().expression(i));
                            try {
                                type = vst.getVariableType(name);
                                if (exprLstRes.getKey().equals(strStorage.get(type).getFieldType(i))){
                                    code.addAll(exprLstRes.getValue());
                                    code.add("pop eax");

                                    if (exprLstRes.getKey().equals("int") || exprLstRes.getKey().equals("string") || exprLstRes.getKey().equals("bool")){
                                        code.add("mov ecx, " + vst.getVariableAddress(name));
                                        code.add("mov [ecx + " +  type + "." + strStorage.get(type).getFieldName(i) + "], eax");
                                    } else {
                                        //code.add("mov ecx, " + vst.getVariableAddress(name));
                                        //code.add("mov edx, [eax]");
                                        //code.add("mov [ecx + " +  type + "." + strStorage.get(type).getFieldName(i) + "], edx");

                                        //реализовать
                                        //if (exprLstRes.getKey() != int | string | boolean)


                                        List<String> assignCode = new ArrayList<>();

                                        code.addAll(assignStructuresByFields(exprLstRes.getKey(), "mov edx, [eax ", vst.getVariableAddress(name),
                                                "mov [ecx + " +  type + "." + strStorage.get(type).getFieldName(i), assignCode));

//                                        StructStorage structStr = strStorage.get(exprLstRes.getKey());
//                                        for (Pair<String, String> j : structStr.getFieldsLst()) {
//                                            if (j.getValue().equals("int") || j.getValue().equals("string") || j.getValue().equals("bool")){
//                                                //String addr = vst.getVariableAddress(name).substring(0, vst.getVariableAddress(name).length() - 1);
//                                                //addr += structStr.getStructType() + "";
//                                                //code.add("mov ecx, ");
//                                                code.add("mov ecx, " + vst.getVariableAddress(name));
//                                                code.add("mov edx, [eax + " + exprLstRes.getKey()  + "." + j.getKey() + "]");
//                                                try {
//                                                code.add("mov [ecx + " +  type + "." + strStorage.get(type).getFieldName(i) + " + "+
//                                                        exprLstRes.getKey()  + "." + j.getKey() + "], edx");
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//                                            } else {
//
//                                            }
//                                        }

                                        ///////////////////////////////////////

                                        // {
                                        // mov ecx, [struct.adress]
                                        // mov [ecx + type.field]
                                        //
                                        // }

//                                        code.add("pusha");
//                                        code.add("push " + getDatatypeSize(type));
//                                        code.add("push eax");
//
//
//                                        code.add("push dword [ecx + " +  type + "." + strStorage.get(type).getFieldName(i) + "]");
//                                        code.add("call memcpy");
//                                        code.add("add esp, 12");
//                                        code.add("popa");
                                    }
                                    //
                                    //code.add("mov ecx, " + vst.getVariableAddress(name));
                                    //code.add("mov [ecx + " +  type + "." + strStorage.get(type).getFieldName(i) + "], eax");
                                    //

                                    //убрать и заменить на вышестоящее
                                    //code.add("mov [" + vst.getStructName(name) + " + " + type + "." +
                                    //        strStorage.get(type).getFieldName(i) + "], eax");
                                    ///
                                }
                                else
                                    throw new IllegalArgumentException("Type of some arguments doen's match the defenition");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isStructCall = false;

                } else {
                    throw new IllegalArgumentException("Type mismatch " + ctx.getText() + " " + type);
                }
            }
        }

        return new Pair<>(type, code);
    }

    @Override
    public Pair<String, List<String>> visitLocalVariableDeclaration(GrammarParser.LocalVariableDeclarationContext ctx) {
        String type = visitTypeSpecifier(ctx.typeSpecifier()).getKey();
        List<String> code = new ArrayList<String>();

        if (datatypeSize.containsKey(type)) {
            int size = datatypeSize.get(type);
            for (int i = 0; i < ctx.IDENTIFIER().size(); i++) {
                String name = ctx.IDENTIFIER(i).getText();
                if (!vst.containsVariable(name)){
                    try {
                        ///////////////
//                        if (type.equals("int") || type.equals("bool") || type.equals("string"))
//                            vst.addLocalVariable(name, type, 4);
//                        else {
//                            vst.addGlobalVariable(name, type, 4);
//                        }
                        ////////////// заменить
                        vst.addLocalVariable(name, type, 4);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    throw new IllegalArgumentException("Redefenition: " + name);
                }
            }
        } else {
            throw new IllegalArgumentException("Not supported datatype:" + type);
        }
        return new Pair<>(type, code);
    }
}
