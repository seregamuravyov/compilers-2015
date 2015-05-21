import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sergey on 20.05.15.
 */
public class FunctionVisitor extends GrammarBaseVisitor<String> {

    private Map<String, FunctionStorage> funcStorage = new HashMap<>();
    private Map<String, Integer> datatypeSize = new HashMap<String, Integer>();

    public void setFunctionStorage(Map<String, FunctionStorage> st){
        funcStorage = st;
    }
    public Map<String, FunctionStorage> getFuncStorage(){
        return funcStorage;
    }
    @Override
    public String visitProgramm(GrammarParser.ProgrammContext ctx) {
        datatypeSize.put("int", 4);
        datatypeSize.put("string", 4);
        datatypeSize.put("bool", 4);

        for (int i = 0; i < ctx.structDefinition().size(); i++) {
            visitStructDefinition(ctx.structDefinition(i));
        }

        for (int i = 0; i < ctx.functionDefinition().size(); i++) {
            visitFunctionDefinition(ctx.functionDefinition(i));
        }
        return "Done";
    }

    @Override
    public String visitFunctionDefinition(GrammarParser.FunctionDefinitionContext ctx) {
        List<String> code = new ArrayList<String>();

        String returnType = visitFunctionReturnType(ctx.functionReturnType());
        String functionName = ctx.IDENTIFIER().getText();
        FunctionStorage fs = new FunctionStorage();
        if (ctx.variableList() != null){
            for (int i = 0; i < ctx.variableList().typeSpecifier().size(); i++) {
                String argType = visitTypeSpecifier(ctx.variableList().typeSpecifier(i));
                String argName = ctx.variableList().IDENTIFIER(i).getText();
                fs.addArgument(argName, argType, 4);
            }
        }
        fs.setReturnType(returnType);
        funcStorage.put(functionName, fs);


        return super.visitFunctionDefinition(ctx);
    }

    @Override
    public String visitStructDefinition(GrammarParser.StructDefinitionContext ctx) {
        String structType = ctx.IDENTIFIER().getText();
        datatypeSize.put(structType, 4);
        return structType;
    }

    @Override
    public String visitVariableList(GrammarParser.VariableListContext ctx) {
        return super.visitVariableList(ctx);
    }

    @Override
    public String visitFunctionReturnType(GrammarParser.FunctionReturnTypeContext ctx) {
        return super.visitFunctionReturnType(ctx);
    }

    @Override
    public String visitTypeSpecifier(GrammarParser.TypeSpecifierContext ctx) {
        if (ctx.primitiveTypeSpecifier() != null)
            return visitPrimitiveTypeSpecifier(ctx.primitiveTypeSpecifier());
        else
            return visitStructType(ctx.structType());
    }

    @Override
    public String visitStructType(GrammarParser.StructTypeContext ctx) {
        String structType = ctx.IDENTIFIER().getText();
        if (datatypeSize.containsKey(structType)){
            return structType;
        }
        throw new UnsupportedOperationException("Unknown type " + structType);
    }

    @Override
    public String visitPrimitiveTypeSpecifier(GrammarParser.PrimitiveTypeSpecifierContext ctx) {
        if (ctx.STRING() != null) {
            return ctx.STRING().getText();
        } else if (ctx.BOOL() != null) {
            return ctx.BOOL().getText();
        } else if (ctx.INT() != null) {
            return ctx.INT().getText();
        }
        throw new UnsupportedOperationException("Unsupported type!");
    }

    @Override
    public String visitFunctionBody(GrammarParser.FunctionBodyContext ctx) {
        return super.visitFunctionBody(ctx);
    }

    @Override
    public String visitStructBody(GrammarParser.StructBodyContext ctx) {
        return super.visitStructBody(ctx);
    }

    @Override
    public String visitStructFieldDeclaration(GrammarParser.StructFieldDeclarationContext ctx) {
        return super.visitStructFieldDeclaration(ctx);
    }

    @Override
    public String visitStatement(GrammarParser.StatementContext ctx) {
        return super.visitStatement(ctx);
    }

    @Override
    public String visitUnionStatement(GrammarParser.UnionStatementContext ctx) {
        return super.visitUnionStatement(ctx);
    }

    @Override
    public String visitConditionStatement(GrammarParser.ConditionStatementContext ctx) {
        return super.visitConditionStatement(ctx);
    }

    @Override
    public String visitCompoundStatement(GrammarParser.CompoundStatementContext ctx) {
        return super.visitCompoundStatement(ctx);
    }

    @Override
    public String visitWhileStatement(GrammarParser.WhileStatementContext ctx) {
        return super.visitWhileStatement(ctx);
    }

    @Override
    public String visitFunctionCall(GrammarParser.FunctionCallContext ctx) {
        return super.visitFunctionCall(ctx);
    }

    @Override
    public String visitStructCall(GrammarParser.StructCallContext ctx) {
        return super.visitStructCall(ctx);
    }

    @Override
    public String visitExpressionList(GrammarParser.ExpressionListContext ctx) {
        return super.visitExpressionList(ctx);
    }

    @Override
    public String visitExpression(GrammarParser.ExpressionContext ctx) {
        return super.visitExpression(ctx);
    }

    @Override
    public String visitAndExpr(GrammarParser.AndExprContext ctx) {
        return super.visitAndExpr(ctx);
    }

    @Override
    public String visitEqualityExpr(GrammarParser.EqualityExprContext ctx) {
        return super.visitEqualityExpr(ctx);
    }

    @Override
    public String visitRelationExpr(GrammarParser.RelationExprContext ctx) {
        return super.visitRelationExpr(ctx);
    }

    @Override
    public String visitAdditiveExpr(GrammarParser.AdditiveExprContext ctx) {
        return super.visitAdditiveExpr(ctx);
    }

    @Override
    public String visitMultyplicationExpr(GrammarParser.MultyplicationExprContext ctx) {
        return super.visitMultyplicationExpr(ctx);
    }

    @Override
    public String visitAtomicExpr(GrammarParser.AtomicExprContext ctx) {
        return super.visitAtomicExpr(ctx);
    }

    @Override
    public String visitPrimitiveExpr(GrammarParser.PrimitiveExprContext ctx) {
        return super.visitPrimitiveExpr(ctx);
    }

    @Override
    public String visitIndex(GrammarParser.IndexContext ctx) {
        return super.visitIndex(ctx);
    }

    @Override
    public String visitGlobalVariableDeclaration(GrammarParser.GlobalVariableDeclarationContext ctx) {
        return super.visitGlobalVariableDeclaration(ctx);
    }

    @Override
    public String visitGlobalAssignment(GrammarParser.GlobalAssignmentContext ctx) {
        return super.visitGlobalAssignment(ctx);
    }

    @Override
    public String visitLocalAssignment(GrammarParser.LocalAssignmentContext ctx) {
        return super.visitLocalAssignment(ctx);
    }

    @Override
    public String visitLocalVariableDeclaration(GrammarParser.LocalVariableDeclarationContext ctx) {
        return super.visitLocalVariableDeclaration(ctx);
    }
}
