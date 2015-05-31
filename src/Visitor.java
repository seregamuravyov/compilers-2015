import javafx.util.Pair;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.*;

/**
 * Created by sergey on 02.05.15.
 */
public class Visitor extends GrammarBaseVisitor<VisitTreeNode> {

    private boolean optimize;

    private int labelCounter;
    private int tmpVarCounter;

    //variable processing
    private VariableStorage vst = new VariableStorage();
    private Map<String, Integer> datatypeSize = new HashMap<String, Integer>();

    private Pair<String, String> variables;

    private Map<String, FunctionStorage> funcStorage = new HashMap<>();

    //private Map<String, Pair<String, Map<String, String>>> functions = new HashMap<>();

    private List<String> globAssign = new ArrayList<>();
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

    public VisitTreeNode visitProgramm(@NotNull GrammarParser.ProgrammContext ctx) {
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
            code.addAll(visitStructDefinition(sdc).getCode());
            code.add("\n\n");
        }


        for (GrammarParser.GlobalVariableDeclarationContext gvdc: ctx.globalVariableDeclaration()){
            //пофиксить
            //code.addAll(visitGlobalVariableDeclaration(gvdc).getValue());
            globAssign.addAll(visitGlobalVariableDeclaration(gvdc).getCode());
        }

        code.add("section .text");
        code.add("global main\n");

        code.add("glob_assign:");
        code.add("push ebp\nmov ebp, esp");

        code.addAll(globAssign);
        code.add("mov esp, ebp\npop ebp");
        code.add("ret\n\n");

        vst.setStructStorage(strStorage);
        for (GrammarParser.FunctionDefinitionContext fdc : ctx.functionDefinition()) {
            code.addAll(visitFunctionDefinition(fdc).getCode());
            code.add("\n\n");
        }

        code.add("section .data");
        code.add("int_format dd \"%d\", 10, 0");
        code.add("str_format dd \"%s\", 10, 0");
        code.addAll(dataSection);
        code.add("\nsection .bss");
        code.addAll(bssSection);

        return new VisitTreeNode(null, code, null);
    }

    public VisitTreeNode visitFunctionDefinition(@NotNull GrammarParser.FunctionDefinitionContext ctx) {
        List<String> code = new ArrayList<String>();
        List<String> assignArg = new ArrayList<>();

        String returnType = visitFunctionReturnType(ctx.functionReturnType()).getType();
        String functionName = ctx.IDENTIFIER().getText();

        if (!functionName.equals("main")) {
            vst.enterBlock(functionName);
            code.add(functionName + ":");
            code.add("push ebp\nmov ebp, esp");

            FunctionStorage fs = new FunctionStorage();
            int totalArgStackSize = 0;
            if (ctx.variableList() != null){
                for (int i = 0; i < ctx.variableList().typeSpecifier().size(); i++) {
                    String argType = visitTypeSpecifier(ctx.variableList().typeSpecifier(i)).getType();
                    String argName = ctx.variableList().IDENTIFIER(i).getText();

                    if (!(argType.equals("int") || argType.equals("bool") || argType.equals("string"))){
                        fs.addArgument(argName, argType, 4);
                        try {
                            vst.addFuncArgument(argName, fs.getArgType(argName), fs.getArgAdress(argName));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        fs.addArgument(argName, argType, 4);
                        try {
                            vst.addFuncArgument(argName, fs.getArgType(argName), fs.getArgAdress(argName));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    totalArgStackSize += 4;
                }
            }
            fs.setReturnType(returnType);
            funcStorage.put(functionName, fs);

            VisitTreeNode body = visitFunctionBody(ctx.functionBody());
            if (!returnType.equals(body.getType())) {
                throw new IllegalArgumentException("Function type and return type does not match");
            }
            code.add("sub esp, " + String.valueOf(vst.getLocalVarStackSize() + 4));
            code.addAll(assignArg);
            code.addAll(body.getCode());

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
            VisitTreeNode body = visitFunctionBody(ctx.functionBody());
            code.add("sub esp, " + String.valueOf(vst.getLocalVarStackSize() + 4));

            code.add("pusha");
            code.add("call glob_assign");
            code.add("popa");

            code.addAll(body.getCode());
            code.addAll(vst.garbageCollector());
            vst.exitBlock();
            code.add("mov esp, ebp\npop ebp");
            code.add("ret");
        }
        return new VisitTreeNode(returnType, code, null);
    }

    public VisitTreeNode visitStructDefinition(GrammarParser.StructDefinitionContext ctx) {
        List<String> code = new ArrayList<String>();
        String structType = ctx.IDENTIFIER().getText();
        str = new StructStorage();
        VisitTreeNode structBody = visitStructBody(ctx.structBody());


        code.add("struc " + structType);
        code.addAll(structBody.getCode());
        code.add(".size:");
        code.add("endstruc");

        return new VisitTreeNode(structType, code, null);
    }

    public VisitTreeNode visitVariableList(GrammarParser.VariableListContext ctx) {
        throw new UnsupportedOperationException();
    }

    public VisitTreeNode visitFunctionReturnType(GrammarParser.FunctionReturnTypeContext ctx) {
        if (ctx.VOID() != null)
            return new VisitTreeNode(ctx.VOID().toString(), null, null);
        else
            return visitTypeSpecifier(ctx.typeSpecifier());
    }

    public VisitTreeNode visitTypeSpecifier(GrammarParser.TypeSpecifierContext ctx) {
        if (ctx.primitiveTypeSpecifier() != null)
            return visitPrimitiveTypeSpecifier(ctx.primitiveTypeSpecifier());
        else
            return visitStructType(ctx.structType());
    }

    public VisitTreeNode visitStructType(GrammarParser.StructTypeContext ctx) {
        String structType = ctx.IDENTIFIER().getText();
        if (datatypeSize.containsKey(structType)){
            return new VisitTreeNode(structType, null, null);
        }
        throw new UnsupportedOperationException("Unknown type " + structType);
    }

    public VisitTreeNode visitPrimitiveTypeSpecifier(GrammarParser.PrimitiveTypeSpecifierContext ctx) {
        if (ctx.STRING() != null) {
            return new VisitTreeNode(ctx.STRING().getText(), null, null);
        } else if (ctx.BOOL() != null) {
            return new VisitTreeNode(ctx.BOOL().getText(), null, null);
        } else if (ctx.INT() != null) {
            return new VisitTreeNode(ctx.INT().getText(), null, null);
        }
        throw new UnsupportedOperationException("Unsupported type");
    }


    public VisitTreeNode visitFunctionBody(GrammarParser.FunctionBodyContext ctx) {
        List<String> code = new LinkedList<String>();
        for (GrammarParser.StatementContext sc : ctx.statement()) {
            code.addAll(visitStatement(sc).getCode());
        }

        String returnType = "void";

        if(ctx.expression() != null) {
            VisitTreeNode returnStatement = visitExpression(ctx.expression());
            returnType = returnStatement.getType();
            code.addAll(returnStatement.getCode());
            if (returnStatement.getType().equals("int") || returnStatement.getType().equals("bool"))
                code.add("pop eax");
            else {

                if (returnStatement.getType().equals("string")) {
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
                    code.add("push " + getDatatypeSize(returnStatement.getType()));
                    code.add("call malloc");
                    code.add("add esp, 4");

                    code.add("pop ebx");
                    code.add("push " + getDatatypeSize(returnStatement.getType()));
                    code.add("push ebx");
                    code.add("push eax");
                    code.add("call memcpy");
                    code.add("add esp, 12");
                    code.add("push eax");
                    code.add("pop eax");
                }
            }
        }

        return new VisitTreeNode(returnType, code, null);
    }

    @Override
    public VisitTreeNode visitStructFieldDeclaration(GrammarParser.StructFieldDeclarationContext ctx) {
        List<String> code = new LinkedList<String>();
        String name = ctx.IDENTIFIER().getText();
        String type = visitTypeSpecifier(ctx.typeSpecifier()).getType();

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
        return new VisitTreeNode(type, code, null);
    }

    public VisitTreeNode visitStructBody(GrammarParser.StructBodyContext ctx) {
        List<String> code = new LinkedList<String>();
        for (GrammarParser.StructFieldDeclarationContext sc : ctx.structFieldDeclaration()) {
            code.addAll(visitStructFieldDeclaration(sc).getCode());
        }
        String structType = (((GrammarParser.StructDefinitionContext) ctx.getParent())).IDENTIFIER().getText();
        strStorage.put(structType, str);
        datatypeSize.put(structType, str.getStructSize());
        return new VisitTreeNode(structType, code, null);
    }

    @Override
    public VisitTreeNode visitStatement(GrammarParser.StatementContext ctx) {
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
        throw new UnsupportedOperationException("Unknown statement");
    }

    @Override
    public VisitTreeNode visitConditionStatement(GrammarParser.ConditionStatementContext ctx) {
        List<String> code = new LinkedList<String>();
        labelCounter += 2;
        int localLabelCounter = labelCounter + 2;

        VisitTreeNode expr = visitExpression(ctx.expression());
        if (expr.getType().equals("bool")){
            code.addAll(expr.getCode());
            code.add("pop eax");
            if (ctx.ELSE() == null){ //if() {};
                code.add("cmp eax, 1");

                localLabelCounter++;
                code.add("jne L" + localLabelCounter);
                code.addAll(visitCompoundStatement(ctx.compoundStatement(0)).getCode());
                code.add("L" + localLabelCounter + ":");
                labelCounter += 4;

            } else { // if() {} else {}
                code.add("cmp eax, 1");
                localLabelCounter++;
                code.add("jne L" + localLabelCounter);
                code.addAll(visitCompoundStatement(ctx.compoundStatement(0)).getCode());
                code.add("jmp L" + (localLabelCounter + 1));
                code.add("L" + localLabelCounter + ":");
                code.addAll(visitCompoundStatement(ctx.compoundStatement(1)).getCode());
                code.add("L" + (localLabelCounter + 1)  + ":");
                labelCounter += 4;

            }
        } else {
            throw new IllegalArgumentException("Value of the condition must be boolean");
        }


        return new VisitTreeNode("condition", code, null);
    }

    @Override
    public VisitTreeNode visitCompoundStatement(GrammarParser.CompoundStatementContext ctx) {
        List<String> code = new LinkedList<String>();

        for (int i = 0; i < ctx.statement().size(); i++) {
            code.addAll(visitStatement(ctx.statement(i)).getCode());
        }
        return new VisitTreeNode("compound", code, null);
    }

    @Override
    public VisitTreeNode visitWhileStatement(GrammarParser.WhileStatementContext ctx) {
        List<String> code = new LinkedList<String>();
        labelCounter += 2;
        int localLabelCounter = labelCounter + 2;

        VisitTreeNode expr = visitExpression(ctx.expression());
        if (expr.getType().equals("bool")){
            code.addAll(expr.getCode());
            code.add("pop eax");
            code.add("cmp eax, 1");
            localLabelCounter++;
            code.add("jne L" + localLabelCounter);
            code.add("L" + (localLabelCounter + 1) + ":");
            code.addAll(visitCompoundStatement(ctx.compoundStatement()).getCode());
            code.addAll(visitExpression(ctx.expression()).getCode());
            code.add("pop eax");
            code.add("cmp eax, 1");
            code.add("je L" + (localLabelCounter + 1));
            code.add("L" + localLabelCounter + ":");
            labelCounter += 4;

        } else {
            throw new IllegalArgumentException("Value inside the \"while\" must be boolean");
        }
        return new VisitTreeNode("while", code, null);
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
    public VisitTreeNode visitFunctionCall(GrammarParser.FunctionCallContext ctx) {
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
                return new VisitTreeNode("void", code, null);
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

                return new VisitTreeNode("void", code, null);
            }

        } else {
            if (ctx.WRITE() != null){
                VisitTreeNode arg = visitExpression(ctx.expression());
                code.addAll(arg.getCode());
                format = getFormat(arg.getType());
                code.add("push " + format);
                code.add("call printf");
                code.add("add esp, 8");
                return new VisitTreeNode("void", code, null);
            } else {
                if ((ctx.IDENTIFIER() != null) && (funcStorage.containsKey(ctx.IDENTIFIER().getText()))){
                    String funcName = ctx.IDENTIFIER().getText();
                    if (ctx.expressionList() != null)
                        for (int i = ctx.expressionList().expression().size()-1; i >= 0; i--){
                            VisitTreeNode exprRes = visitExpression(ctx.expressionList().expression(i));
                            if (exprRes.getType().equals(funcStorage.get(funcName).getArgType(i)))
                                code.addAll(exprRes.getCode());
                            else
                                throw new IllegalArgumentException("Type of some arguments doen's match the defenition");
                        }
                   code.add("call " + ctx.IDENTIFIER().getText());
                   return new VisitTreeNode(funcStorage.get(funcName).getReturnType(), code, null);
                }
            }
        }
        throw new UnsupportedOperationException("Unknown function " + ctx.getText());
    }

    @Override
    public VisitTreeNode visitStructCall(GrammarParser.StructCallContext ctx) {
        List<String> code = new ArrayList<String>();
        String name = ctx.IDENTIFIER(0).getText();
        String type = "", field = "";
        String register = "";
        String  adress = "";
        if(vst.containsVariable(name)){
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
                }
                field = ctx.IDENTIFIER(i).getText();
                adress += " + " + type + "."  + field;
            }

            try {
                if (!vst.isAllocated(name) && ctx.getText().contains(".") &&
                        !vst.getVariableAddress(name).startsWith("[ebp +")){
                    throw new UnsupportedOperationException("Can't get field of uninitialized structure " + name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (structToVarAss) {
                code.add("push " + register);
                code.add(adress);
                isStructCall = true;
            } else {
                if (assignmentStructFl){
                    code.add(register + adress);
                    assignmentStructFl = false;
                }
                else {
                    code.add("push dword [" + register + adress + "]");
                }
            }
            try {
                return new VisitTreeNode(strStorage.get(type).getFieldTypeStr(field), code, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new UnsupportedOperationException("Unsupported structure or structure field calling " + ctx.getText());

    }

    @Override
    public VisitTreeNode visitExpressionList(GrammarParser.ExpressionListContext ctx) {
        List<String> code = new ArrayList<String>();
        return new VisitTreeNode(null, code, null);
    }

    @Override
    public VisitTreeNode visitExpression(GrammarParser.ExpressionContext ctx) {
        labelCounter += 2;
        int localLabelCounter = labelCounter + 2;
        VisitTreeNode andExpr = visitAndExpr(ctx.andExpr(0));
        if (ctx.andExpr().size() > 1) {
            for (int i = 1; i < ctx.andExpr().size(); ++i) {
                VisitTreeNode nextAndExpr = visitAndExpr(ctx.andExpr(i));
                if (andExpr.getType().equals("bool") && nextAndExpr.getType().equals("bool")) {
                    andExpr.getCode().addAll(nextAndExpr.getCode());

                    localLabelCounter++;
                    andExpr.getCode().add("pop ebx");
                    andExpr.getCode().add("pop edx");
                    andExpr.getCode().add("cmp edx, 1");
                    andExpr.getCode().add("je L" + localLabelCounter);
                    andExpr.getCode().add("cmp ebx, 1");
                    andExpr.getCode().add("jne L" + (localLabelCounter - 1));
                    andExpr.getCode().add("L" + (localLabelCounter) + ":");
                    andExpr.getCode().add("mov eax, 1");
                    andExpr.getCode().add("jmp L" + (localLabelCounter + 1));
                    andExpr.getCode().add("L" + (localLabelCounter - 1) + ":");
                    andExpr.getCode().add("mov eax, 0");
                    andExpr.getCode().add("L" + (localLabelCounter + 1) + ":");
                    labelCounter += 4;
                    andExpr.getCode().add("push eax");
                    andExpr = new VisitTreeNode("bool", andExpr.getCode(), null);
                } else {
                    throw new IllegalArgumentException("Both values must be boolean");
                }
            }
        } else {

        }
        return andExpr;
    }

    @Override
    public VisitTreeNode visitAndExpr(GrammarParser.AndExprContext ctx) {
        labelCounter += 2;
        int localLabelCounter = labelCounter + 2;
        VisitTreeNode equalityExpr = visitEqualityExpr(ctx.equalityExpr(0));
        if (ctx.equalityExpr().size() > 1)
            for (int i = 1; i < ctx.equalityExpr().size(); ++i) {
                VisitTreeNode nextAndExpr = visitEqualityExpr(ctx.equalityExpr(i));
                if (equalityExpr.getType().equals("bool") && nextAndExpr.getType().equals("bool")) {
                    equalityExpr.getCode().addAll(nextAndExpr.getCode());
                    localLabelCounter++;
                    equalityExpr.getCode().add("pop ebx");
                    equalityExpr.getCode().add("pop edx");

                    equalityExpr.getCode().add("cmp edx, 1");
                    equalityExpr.getCode().add("jne L" + localLabelCounter);
                    equalityExpr.getCode().add("cmp ebx, 1");
                    equalityExpr.getCode().add("jne L" + localLabelCounter);
                    equalityExpr.getCode().add("mov eax, 1");
                    equalityExpr.getCode().add("jmp L" + (localLabelCounter + 1));

                    equalityExpr.getCode().add("L" + (localLabelCounter) + ":");
                    equalityExpr.getCode().add("mov eax, 0");

                    equalityExpr.getCode().add("L" + (localLabelCounter + 1) + ":");
                    equalityExpr.getCode().add("push eax");
                    equalityExpr = new VisitTreeNode("bool", equalityExpr.getCode(), null);
                    localLabelCounter += 4;

                } else {
                    throw new IllegalArgumentException("Both values must be boolean");
                }
            }
        else {

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
    public VisitTreeNode visitEqualityExpr(GrammarParser.EqualityExprContext ctx) {
        VisitTreeNode relationExpr = visitRelationExpr(ctx.relationExpr(0));
        labelCounter += 2;
        int localLabelCounter = labelCounter + 2;
        String operator = "";
        if (ctx.relationExpr().size() > 1)
            for (int i = 1; i < ctx.relationExpr().size(); ++i) {
                VisitTreeNode nextRelationExpr = visitRelationExpr(ctx.relationExpr(i));
                if ((relationExpr.getType().equals(nextRelationExpr.getType())) &&
                        (relationExpr.getType().equals("int") || relationExpr.getType().equals("bool"))) {
                    operator = getEqualityOperator(ctx.getChild(2 * i - 1).getText());
                    relationExpr.getCode().addAll(nextRelationExpr.getCode());
                    localLabelCounter++;
                    relationExpr.getCode().add("pop ebx");
                    relationExpr.getCode().add("pop edx");

                    relationExpr.getCode().add("cmp edx, ebx");
                    relationExpr.getCode().add(operator + " L" + localLabelCounter);
                    relationExpr.getCode().add("mov eax, 0");
                    relationExpr.getCode().add("jmp L" + (localLabelCounter + 1));

                    relationExpr.getCode().add("L" + (localLabelCounter) + ":");
                    relationExpr.getCode().add("mov eax, 1");

                    relationExpr.getCode().add("L" + (localLabelCounter + 1) + ":");
                    relationExpr.getCode().add("push eax");
                    relationExpr = new VisitTreeNode("bool", relationExpr.getCode(), null);
                    labelCounter += 4;

                } else {
                    if (relationExpr.getType().equals("string") && nextRelationExpr.getType().equals("string")) {
                        operator = getEqualityOperator(ctx.getChild(2 * i - 1).getText());
                        localLabelCounter++;
                        relationExpr.getCode().addAll(nextRelationExpr.getCode());
                        relationExpr.getCode().add("call strcmp");
                        relationExpr.getCode().add("add esp, 8");
                        relationExpr.getCode().add("mov ebx, eax");
                        relationExpr.getCode().add("cmp ebx, 0");
                        relationExpr.getCode().add(operator + "L" + localLabelCounter);
                        relationExpr.getCode().add("mov eax, 0");
                        relationExpr.getCode().add("jmp " + (localLabelCounter + 1));

                        relationExpr.getCode().add("L" + (localLabelCounter) + ":");
                        relationExpr.getCode().add("mov eax, 1");

                        relationExpr.getCode().add("L" + (localLabelCounter + 1) + ":");
                        relationExpr.getCode().add("push eax");
                        relationExpr = new VisitTreeNode("bool", relationExpr.getCode(), null);
                        labelCounter += 4;
                    } else
                        throw new IllegalArgumentException("Both values must be the same type");
                }
            }
        else {

        }
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
    public VisitTreeNode visitRelationExpr(GrammarParser.RelationExprContext ctx) {
        VisitTreeNode additiveExpr = visitAdditiveExpr(ctx.additiveExpr(0));
        labelCounter += 2;
        int localLabelCounter = labelCounter + 2;
        if (ctx.additiveExpr().size() > 1)
            for (int i = 1; i < ctx.additiveExpr().size(); ++i) {
                VisitTreeNode nextAdditiveExpr = visitAdditiveExpr(ctx.additiveExpr(i));
                if ((additiveExpr.getType().equals(nextAdditiveExpr.getType())) &&
                        (additiveExpr.getType().equals("int") || nextAdditiveExpr.getType().equals("bool"))) {
                    String operator = getRelationOperator(ctx.getChild(2 * i - 1).getText());
                    additiveExpr.getCode().addAll(nextAdditiveExpr.getCode());
                    localLabelCounter++;
                    additiveExpr.getCode().add("pop ebx");
                    additiveExpr.getCode().add("pop edx");

                    additiveExpr.getCode().add("cmp edx, ebx");
                    additiveExpr.getCode().add(operator + " L" + localLabelCounter);
                    additiveExpr.getCode().add("mov eax, 0");
                    additiveExpr.getCode().add("jmp L" + (localLabelCounter + 1));

                    additiveExpr.getCode().add("L" + (localLabelCounter) + ":");
                    additiveExpr.getCode().add("mov eax, 1");

                    additiveExpr.getCode().add("L" + (localLabelCounter + 1) + ":");
                    additiveExpr.getCode().add("push eax");
                    labelCounter += 4;
                    additiveExpr = new VisitTreeNode("bool", additiveExpr.getCode(), null);
                } else {
                    throw new IllegalArgumentException("Both values must be integer or boolean");
                }
            }
        else {

        }
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
    public VisitTreeNode visitAdditiveExpr(GrammarParser.AdditiveExprContext ctx) {
        VisitTreeNode multyplicationExpr = visitMultyplicationExpr(ctx.multyplicationExpr(0));
        String operator = "";
        if (ctx.multyplicationExpr().size() > 1) {
            for (int i = 1; i < ctx.multyplicationExpr().size(); i++) {
                VisitTreeNode nextMultyplicationExpr = visitMultyplicationExpr(ctx.multyplicationExpr(i));
                operator = getAddOperator(ctx.getChild(2 * i - 1).getText());
                if(multyplicationExpr.getType().equals("int") && nextMultyplicationExpr.getType().equals("int")){
                    multyplicationExpr.getCode().addAll(nextMultyplicationExpr.getCode());
                    multyplicationExpr.getCode().add("pop ebx");
                    multyplicationExpr.getCode().add("pop edx");
                    multyplicationExpr.getCode().add(operator + " edx, ebx");
                    multyplicationExpr.getCode().add("mov eax, edx");
                    multyplicationExpr.getCode().add("push eax");
                    multyplicationExpr = new VisitTreeNode("int", multyplicationExpr.getCode(), null);
                } else {
                  if (multyplicationExpr.getType().equals("string") && nextMultyplicationExpr.getType().equals("string") && operator.equals("add"))  {
                      multyplicationExpr.getCode().addAll(nextMultyplicationExpr.getCode());

                      multyplicationExpr.getCode().add("push 256");
                      multyplicationExpr.getCode().add("call malloc");
                      multyplicationExpr.getCode().add("add esp, 4");

                      multyplicationExpr.getCode().add("pop ebx");
                      multyplicationExpr.getCode().add("pop edx");

                      multyplicationExpr.getCode().add("push edx");
                      multyplicationExpr.getCode().add("push eax");
                      multyplicationExpr.getCode().add("call strcpy");
                      multyplicationExpr.getCode().add("add esp, 8");

                      multyplicationExpr.getCode().add("push ebx");
                      multyplicationExpr.getCode().add("push eax");
                      multyplicationExpr.getCode().add("call strcat");
                      multyplicationExpr.getCode().add("add esp, 8");

                      multyplicationExpr.getCode().add("push eax");
                      multyplicationExpr = new VisitTreeNode("string", multyplicationExpr.getCode(), null);
                  } else {
                      throw new IllegalArgumentException("Both values must be integer or strings for concatetaion");
                  }
                }

            }
        } else {

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
    public VisitTreeNode visitMultyplicationExpr(GrammarParser.MultyplicationExprContext ctx) {
        VisitTreeNode atomicExpr = visitAtomicExpr(ctx.atomicExpr(0));
        if (ctx.atomicExpr().size() > 1) {
            for (int i = 1; i < ctx.atomicExpr().size(); i++) {
                VisitTreeNode nextAtomicExpr = visitAtomicExpr(ctx.atomicExpr(i));
                if(atomicExpr.getType().equals("int") && nextAtomicExpr.getType().equals("int")){
                    atomicExpr.getCode().addAll(nextAtomicExpr.getCode());
                    atomicExpr.getCode().add("pop ebx");
                    atomicExpr.getCode().add("pop eax");

                    String op = ctx.getChild(2 * i - 1).getText();
                    List<String> lst = getMulOperExpression(op);
                    atomicExpr.getCode().addAll(lst);

                    atomicExpr.getCode().add("push eax");
                    atomicExpr = new VisitTreeNode("int", atomicExpr.getCode(), null);
                } else {
                    throw new IllegalArgumentException("Both values must be integer");
                }
            }
        } else {

        }
        return atomicExpr;
    }

    @Override
    public VisitTreeNode visitAtomicExpr(GrammarParser.AtomicExprContext ctx) {
        List<String> code = new ArrayList<String>();
        if (ctx.primitiveExpr() != null) {
            VisitTreeNode exprRes = visitPrimitiveExpr(ctx.primitiveExpr());
            code.addAll(exprRes.getCode());
            return new VisitTreeNode(exprRes.getType(), code, null);
        } else {
            if (ctx.functionCall() != null && funcStorage.containsKey(ctx.functionCall().IDENTIFIER().getText())
                    && !funcStorage.get(ctx.functionCall().IDENTIFIER().getText()).getReturnType().equals("void")){
                code.addAll(visitFunctionCall(ctx.functionCall()).getCode());
                code.add("push eax");
                return new VisitTreeNode(visitFunctionCall(ctx.functionCall()).getType(), code, null);
            } else {
                if (ctx.IDENTIFIER() != null &&  vst.containsVariable(ctx.IDENTIFIER().getText())) {
                    try {
                        String var = ctx.IDENTIFIER().getText();
                        //String type = vst.getVariableType(var);

                        if (vst.getVariableType(var).equals("string") && vst.isGlobal(var))
                            code.add("push " + vst.getVariableAddress(var).substring(1, vst.getVariableAddress(var).length() - 1));
                        else
                            code.add("push dword " + vst.getVariableAddress(var));

                        return new VisitTreeNode(vst.getVariableType(var), code, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (ctx.structCall() != null) {
                        VisitTreeNode structRes = visitStructCall(ctx.structCall());
                        code.addAll(structRes.getCode());
                        return new VisitTreeNode(structRes.getType(), code, null);
                    } else {
                        if (ctx.expression() != null){
                            VisitTreeNode exprRes = visitExpression(ctx.expression());
                            code.addAll(exprRes.getCode());
                            return new VisitTreeNode(exprRes.getType(), code, null);
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
    public VisitTreeNode visitPrimitiveExpr(GrammarParser.PrimitiveExprContext ctx) {
        List<String> code = new ArrayList<String>();
        if (ctx.BOOL_LITERAL() != null){
            code.add("push " + getBooleanValue(ctx.BOOL_LITERAL().getText()));
            return new VisitTreeNode("bool", code, null);
        } else {
            if (ctx.INTEGER_LITERAL() != null) {
                code.add("push " + ctx.INTEGER_LITERAL().getText());
                return new VisitTreeNode("int", code, null);
            } else {
                if (ctx.STRING_LITERAL() != null) {
                    tmpVarCounter++;
                    String var = ("tmp" + tmpVarCounter);
                    try {
                        dataSection.add(var + ": dd " + ctx.STRING_LITERAL().getText() + ", 0");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    code.add("push " + var);
                    return new VisitTreeNode("string", code, null);
                }
            }
        }
        throw new IllegalArgumentException("Not supported value!");
    }

    @Override
    public VisitTreeNode visitGlobalVariableDeclaration(GrammarParser.GlobalVariableDeclarationContext ctx) {
        List<String> code = new ArrayList<String>();

        String type = visitTypeSpecifier(ctx.typeSpecifier()).getType();
        if (datatypeSize.containsKey(type)) {
            for (int i = 0; i < ctx.globalAssignment().size(); i++) {
                VisitTreeNode globalRes = visitGlobalAssignment(ctx.globalAssignment(i));
                String name = globalRes.getType();
                code.addAll(globalRes.getCode());
            }
            return new VisitTreeNode(type, code, null);
        } else {
            throw new IllegalArgumentException("Not supported datatype:" + type);
        }
    }

    @Override
    public VisitTreeNode visitGlobalAssignment(GrammarParser.GlobalAssignmentContext ctx) {
        List<String> code = new ArrayList<String>();
        String type = visitTypeSpecifier(((GrammarParser.GlobalVariableDeclarationContext)ctx.getParent()).typeSpecifier()).getType();
        String name = ctx.IDENTIFIER().getText();

        if (!vst.containsVariable(name)){
            try {
                vst.addGlobalVariable(name, type, 4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Redefenition: " + name);
        }

        if (ctx.primitiveExpr() != null){
            if (type.equals("int")){
                dataSection.add(name + ": dd " + ctx.primitiveExpr().INTEGER_LITERAL().getText());
            } else {
                if (type.equals("string")) {
                    dataSection.add(name + ": dd " + ctx.primitiveExpr().STRING_LITERAL().getText() + ", 0");
                } else {
                    if (type.equals("bool")) {
                        dataSection.add(name + ": dd " + getBooleanValue(ctx.primitiveExpr().BOOL_LITERAL().getText()));
                    } else {
                        throw new IllegalArgumentException("Assignment of variable " + name + " is not allowed in current scope");
                    }
                }
            }
        } else {
            if (ctx.expressionList() != null) {
                if (datatypeSize.containsKey(type) &&
                        !(type.equals("string") || type.equals("int") || type.equals("bool"))) {

                    try {
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
                            VisitTreeNode exprLstRes = visitExpression(ctx.expressionList().expression(i));
                            try {
                                type = vst.getVariableType(name);
                                if (exprLstRes.getType().equals(strStorage.get(type).getFieldType(i))){
                                    code.addAll(exprLstRes.getCode());
                                    code.add("pop eax");

                                    if (exprLstRes.getType().equals("int") || exprLstRes.getType().equals("bool")){
                                        code.add("mov ecx, " + vst.getVariableAddress(name));
                                        code.add("mov [ecx + " +  type + "." + strStorage.get(type).getFieldName(i) + "], eax");
                                    } else {

                                        if (exprLstRes.getType().equals("string")) {
                                            code.add("push eax");
                                            code.add("push 256");
                                            code.add("call malloc");
                                            code.add("add esp, 4");
                                            code.add("mov edx, eax");
                                            code.add("pop eax");

                                            code.add("push eax");
                                            code.add("push edx");
                                            code.add("call strcpy");
                                            code.add("add esp, 8");

                                            code.add("mov ecx, " + vst.getVariableAddress(name));
                                            code.add("mov [ecx +" +  type + "." + strStorage.get(type).getFieldName(i) + "], edx");

                                            code.add("pusha");
                                            code.add("push eax");
                                            code.add("push edx");
                                            code.add("call strcpy");
                                            code.add("add esp, 8");
                                            code.add("popa");

                                        } else {
                                            List<String> assignCode = new ArrayList<>();
                                            code.addAll(assignStructuresByFields(exprLstRes.getType(), "mov edx, [eax ", vst.getVariableAddress(name),
                                                    "mov [ecx + " +  type + "." + strStorage.get(type).getFieldName(i), assignCode, false));
                                        }

                                    }
                                }
                                else
                                    throw new IllegalArgumentException("Type of some arguments doen't match the defenition " + ctx.getText());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isStructCall = false;

                    if (datatypeSize.containsKey(type)){
                        bssSection.add(name + ": resb " + type + ".size");
                    } else{
                        throw new IllegalArgumentException("Wrong type: " + type);
                    }

                } else{
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
            }
        }
        return new VisitTreeNode(name, code, null);
    }

    public List<String> assignStructuresByFields(String type, String sourse, String destAdress, String dest, List<String> code, Boolean assignFlag){
        StructStorage structStr = strStorage.get(type);
        for (Pair<String, String> j : structStr.getFieldsLst()) {
            if (j.getValue().equals("int") || j.getValue().equals("bool")){
                code.add("mov ecx, " + destAdress);
                code.add(sourse + " + " + type  + "." + j.getKey() + "]");
                try {
                    code.add(dest + " + " + type  + "." + j.getKey() + "], edx");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (j.getValue().equals("string")){

                    if (!assignFlag) {
                        code.add("push eax");
                        code.add("push 256");
                        code.add("call malloc");
                        code.add("add esp, 4");
                        code.add("mov ebx, eax");
                        code.add("pop eax");


                        code.add("mov ecx, " + destAdress);
                        code.add(sourse + " + " + type + "." + j.getKey() + "]");
                        try {
                            code.add(dest + " + " + type + "." + j.getKey() + "], ebx");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        code.add("mov ecx, " + destAdress);
                        code.add(sourse + " + " + type + "." + j.getKey() + "]");
                        code.add("mov ebx, " + dest.substring(3) + " + " + type + "." + j.getKey() + "]");
                    }

                    code.add("pusha");
                    code.add("push edx");
                    code.add("push ebx");
                    code.add("call strcpy");
                    code.add("add esp, 8");
                    code.add("popa");
                } else {
                    List<String> assignCode = new ArrayList<>();
                    code.addAll(assignStructuresByFields(j.getValue(), sourse + " + " + type  + "." + j.getKey(),
                            destAdress, dest + " + " + type  + "." + j.getKey(), assignCode, assignFlag));
                }
            }
        }
        return code;
    }

    @Override
    public VisitTreeNode visitLocalAssignment(GrammarParser.LocalAssignmentContext ctx) {
        String type = "";

        boolean strAssign = false;

        List<String> code = new ArrayList<String>();

        int firstBrace = 0;

        if (ctx.structCall() != null) {
            assignmentStructFl = true;
            VisitTreeNode structExpr = visitStructCall(ctx.structCall());

            String addr = structExpr.getCode().get(structExpr.getCode().size() - 1);
            VisitTreeNode expr = visitExpression(ctx.expression());

            if (structExpr.getType().equals(expr.getType())) {
                code.addAll(expr.getCode());
                code.add("pop eax");

                if (expr.getType().equals("int") || expr.getType().equals("bool")){
                    code.add(structExpr.getCode().get(0));
                    code.add("mov [" + addr + "], eax");
                } else {
                    if (expr.getType().equals("string")){
                        code.add(structExpr.getCode().get(0));
                        code.add("pusha");
                        code.add("push eax");
                        code.add("push dword [" + addr + "]");
                        code.add("call strcpy");
                        code.add("add esp, 8");
                        code.add("popa");

                    } else {
                        List<String> strCall = new ArrayList<>();
                        String localAdr = structExpr.getCode().get(0).substring(9, structExpr.getCode().get(0).length());

                        code.addAll(assignStructuresByFields(expr.getType(), "mov edx, [eax ", localAdr, "mov [" + addr, strCall, true));
                    }
                }
                isStructCall = false;

            } else {
                throw new IllegalArgumentException("Type mismatch: " + structExpr.getType() +
                        " and " + expr.getType());
            }
        } else {
            String name = ctx.IDENTIFIER().getText();
            if (ctx.typeSpecifier() != null)
                type = visitTypeSpecifier(ctx.typeSpecifier()).getType();

            if (ctx.typeSpecifier() != null) {
                firstBrace = 2;
                if (!datatypeSize.containsKey(type)){
                    throw new IllegalArgumentException("Not supported type: " + type);
                }

                if (vst.containsVariable(name)) {
                    throw new IllegalArgumentException("Redefenition of variable:" + name);
                } else {
                    try {
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

            VisitTreeNode exprRes = null;
            if (ctx.expression() != null)
                exprRes = visitExpression(ctx.expression());

            if (ctx.getChild(firstBrace).getText().equals("=") && exprRes.getType().equals(type)){

                if (datatypeSize.containsKey(type) && !(type.equals("int") || type.equals("bool"))){

                    if (type.equals("string"))
                        strAssign = true;
                    else
                    {
                        try {
                            if (!vst.isAllocated(name)) {
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //vst.setAllocated(name);
                    }
                }

                if (type.equals("int") ||  type.equals("bool")) {
                    code.addAll(exprRes.getCode());
                    code.add("pop eax");
                    try {
                        code.add("mov " + vst.getVariableAddress(name) + ", eax");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {

                    if (type.equals("string")) {
                        code.addAll(exprRes.getCode());
                        code.add("pop eax");
                        try {
                            if(strAssign && !vst.isAllocated(name)) {
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
                        } catch (Exception e) {
                            e.printStackTrace();
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
                        vst.setAllocated(name);

                    } else {
                        try {
                            //if (ctx.getText().contains(".")){
                                structToVarAss = true;
                                exprRes = visitExpression(ctx.expression());
                                structToVarAss = false;

                                String localAddr = "";

                                if (ctx.getText().contains(".")) {
                                    localAddr = exprRes.getCode().get(exprRes.getCode().size() - 1);
                                    code.addAll(exprRes.getCode().subList(0, 2));
                                } else {
                                    code.addAll(exprRes.getCode());
                                }

                                code.add("pop eax");

                                List<String> casualAssignment = new ArrayList<>();

                                code.addAll(assignStructuresByFields(type, "mov edx, [eax " + localAddr, vst.getVariableAddress(name),
                                        "mov [ecx ", casualAssignment, vst.isAllocated(name)));
                                isStructCall = false;
                                vst.setAllocated(name);
//                            } else {
//                                code.addAll(exprRes.getValue());
//                                code.add("pop eax");
//
//                                code.add("pusha");
//                                code.add("push " + getDatatypeSize(type));
//                                code.add("push eax");
//                                code.add("push dword " + vst.getVariableAddress(name));
//                                code.add("call memcpy");
//                                code.add("add esp, 12");
//                                code.add("popa");
//                            }
                            //code.add("mov ecx, " + vst.getVariableAddress(name));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                if (ctx.getChild(firstBrace).getText().equals("(") && ctx.getChild(firstBrace + 2).getText().equals(")")){

                    try {
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
                            VisitTreeNode exprLstRes = visitExpression(ctx.expressionList().expression(i));
                            try {
                                type = vst.getVariableType(name);
                                if (exprLstRes.getType().equals(strStorage.get(type).getFieldType(i))){
                                    code.addAll(exprLstRes.getCode());
                                    code.add("pop eax");

                                    if (exprLstRes.getType().equals("int") || exprLstRes.getType().equals("bool")){
                                        code.add("mov ecx, " + vst.getVariableAddress(name));
                                        code.add("mov [ecx + " +  type + "." + strStorage.get(type).getFieldName(i) + "], eax");
                                    } else {

                                        if (exprLstRes.getType().equals("string")) {
                                            code.add("push eax");
                                            code.add("push 256");
                                            code.add("call malloc");
                                            code.add("add esp, 4");
                                            code.add("mov edx, eax");
                                            code.add("pop eax");

                                            code.add("push eax");
                                            code.add("push edx");
                                            code.add("call strcpy");
                                            code.add("add esp, 8");

                                            code.add("mov ecx, " + vst.getVariableAddress(name));
                                            code.add("mov [ecx +" +  type + "." + strStorage.get(type).getFieldName(i) + "], edx");

                                            code.add("pusha");
                                            code.add("push eax");
                                            code.add("push edx");
                                            code.add("call strcpy");
                                            code.add("add esp, 8");
                                            code.add("popa");

                                        } else {
                                            List<String> assignCode = new ArrayList<>();
                                            code.addAll(assignStructuresByFields(exprLstRes.getType(), "mov edx, [eax ", vst.getVariableAddress(name),
                                                    "mov [ecx + " +  type + "." + strStorage.get(type).getFieldName(i), assignCode, false));
                                        }

                                    }
                                }
                                else
                                    throw new IllegalArgumentException("Type of some arguments doen't match the defenition " + ctx.getText());
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

        return new VisitTreeNode(type, code, null);
    }

    @Override
    public VisitTreeNode visitLocalVariableDeclaration(GrammarParser.LocalVariableDeclarationContext ctx) {
        String type = visitTypeSpecifier(ctx.typeSpecifier()).getType();
        List<String> code = new ArrayList<String>();

        if (datatypeSize.containsKey(type)) {
            int size = datatypeSize.get(type);
            for (int i = 0; i < ctx.IDENTIFIER().size(); i++) {
                String name = ctx.IDENTIFIER(i).getText();
                if (!vst.containsVariable(name)){
                    try {
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
        return new VisitTreeNode(type, code, null);
    }
}
