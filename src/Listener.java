import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sergey on 20.05.15.
 */
public class Listener extends GrammarBaseListener {

    private Map<String, FunctionStorage> funcStorage = new HashMap<>();

    @Override
    public void enterProgramm(GrammarParser.ProgrammContext ctx) {
        super.enterProgramm(ctx);
    }

    @Override
    public void exitProgramm(GrammarParser.ProgrammContext ctx) {
        super.exitProgramm(ctx);
    }

    @Override
    public void enterFunctionDefinition(GrammarParser.FunctionDefinitionContext ctx) {
        super.enterFunctionDefinition(ctx);
    }

    @Override
    public void exitFunctionDefinition(GrammarParser.FunctionDefinitionContext ctx) {
        super.exitFunctionDefinition(ctx);
    }

    @Override
    public void enterStructDefinition(GrammarParser.StructDefinitionContext ctx) {
        super.enterStructDefinition(ctx);
    }

    @Override
    public void exitStructDefinition(GrammarParser.StructDefinitionContext ctx) {
        super.exitStructDefinition(ctx);
    }

    @Override
    public void enterVariableList(GrammarParser.VariableListContext ctx) {
        super.enterVariableList(ctx);
    }

    @Override
    public void exitVariableList(GrammarParser.VariableListContext ctx) {
        super.exitVariableList(ctx);
    }

    @Override
    public void enterFunctionReturnType(GrammarParser.FunctionReturnTypeContext ctx) {
        super.enterFunctionReturnType(ctx);
    }

    @Override
    public void exitFunctionReturnType(GrammarParser.FunctionReturnTypeContext ctx) {
        super.exitFunctionReturnType(ctx);
    }

    @Override
    public void enterTypeSpecifier(GrammarParser.TypeSpecifierContext ctx) {
        super.enterTypeSpecifier(ctx);
    }

    @Override
    public void exitTypeSpecifier(GrammarParser.TypeSpecifierContext ctx) {
        super.exitTypeSpecifier(ctx);
    }

    @Override
    public void enterStructType(GrammarParser.StructTypeContext ctx) {
        super.enterStructType(ctx);
    }

    @Override
    public void exitStructType(GrammarParser.StructTypeContext ctx) {
        super.exitStructType(ctx);
    }

    @Override
    public void enterPrimitiveTypeSpecifier(GrammarParser.PrimitiveTypeSpecifierContext ctx) {
        super.enterPrimitiveTypeSpecifier(ctx);
    }

    @Override
    public void exitPrimitiveTypeSpecifier(GrammarParser.PrimitiveTypeSpecifierContext ctx) {
        super.exitPrimitiveTypeSpecifier(ctx);
    }

    @Override
    public void enterFunctionBody(GrammarParser.FunctionBodyContext ctx) {
        super.enterFunctionBody(ctx);
    }

    @Override
    public void exitFunctionBody(GrammarParser.FunctionBodyContext ctx) {
        super.exitFunctionBody(ctx);
    }

    @Override
    public void enterStructBody(GrammarParser.StructBodyContext ctx) {
        super.enterStructBody(ctx);
    }

    @Override
    public void exitStructBody(GrammarParser.StructBodyContext ctx) {
        super.exitStructBody(ctx);
    }

    @Override
    public void enterStructFieldDeclaration(GrammarParser.StructFieldDeclarationContext ctx) {
        super.enterStructFieldDeclaration(ctx);
    }

    @Override
    public void exitStructFieldDeclaration(GrammarParser.StructFieldDeclarationContext ctx) {
        super.exitStructFieldDeclaration(ctx);
    }

    @Override
    public void enterStatement(GrammarParser.StatementContext ctx) {
        super.enterStatement(ctx);
    }

    @Override
    public void exitStatement(GrammarParser.StatementContext ctx) {
        super.exitStatement(ctx);
    }

    @Override
    public void enterUnionStatement(GrammarParser.UnionStatementContext ctx) {
        super.enterUnionStatement(ctx);
    }

    @Override
    public void exitUnionStatement(GrammarParser.UnionStatementContext ctx) {
        super.exitUnionStatement(ctx);
    }

    @Override
    public void enterConditionStatement(GrammarParser.ConditionStatementContext ctx) {
        super.enterConditionStatement(ctx);
    }

    @Override
    public void exitConditionStatement(GrammarParser.ConditionStatementContext ctx) {
        super.exitConditionStatement(ctx);
    }

    @Override
    public void enterCompoundStatement(GrammarParser.CompoundStatementContext ctx) {
        super.enterCompoundStatement(ctx);
    }

    @Override
    public void exitCompoundStatement(GrammarParser.CompoundStatementContext ctx) {
        super.exitCompoundStatement(ctx);
    }

    @Override
    public void enterWhileStatement(GrammarParser.WhileStatementContext ctx) {
        super.enterWhileStatement(ctx);
    }

    @Override
    public void exitWhileStatement(GrammarParser.WhileStatementContext ctx) {
        super.exitWhileStatement(ctx);
    }

    @Override
    public void enterFunctionCall(GrammarParser.FunctionCallContext ctx) {
        super.enterFunctionCall(ctx);
    }

    @Override
    public void exitFunctionCall(GrammarParser.FunctionCallContext ctx) {
        super.exitFunctionCall(ctx);
    }

    @Override
    public void enterStructCall(GrammarParser.StructCallContext ctx) {
        super.enterStructCall(ctx);
    }

    @Override
    public void exitStructCall(GrammarParser.StructCallContext ctx) {
        super.exitStructCall(ctx);
    }

    @Override
    public void enterExpressionList(GrammarParser.ExpressionListContext ctx) {
        super.enterExpressionList(ctx);
    }

    @Override
    public void exitExpressionList(GrammarParser.ExpressionListContext ctx) {
        super.exitExpressionList(ctx);
    }

    @Override
    public void enterExpression(GrammarParser.ExpressionContext ctx) {
        super.enterExpression(ctx);
    }

    @Override
    public void exitExpression(GrammarParser.ExpressionContext ctx) {
        super.exitExpression(ctx);
    }

    @Override
    public void enterAndExpr(GrammarParser.AndExprContext ctx) {
        super.enterAndExpr(ctx);
    }

    @Override
    public void exitAndExpr(GrammarParser.AndExprContext ctx) {
        super.exitAndExpr(ctx);
    }

    @Override
    public void enterEqualityExpr(GrammarParser.EqualityExprContext ctx) {
        super.enterEqualityExpr(ctx);
    }

    @Override
    public void exitEqualityExpr(GrammarParser.EqualityExprContext ctx) {
        super.exitEqualityExpr(ctx);
    }

    @Override
    public void enterRelationExpr(GrammarParser.RelationExprContext ctx) {
        super.enterRelationExpr(ctx);
    }

    @Override
    public void exitRelationExpr(GrammarParser.RelationExprContext ctx) {
        super.exitRelationExpr(ctx);
    }

    @Override
    public void enterAdditiveExpr(GrammarParser.AdditiveExprContext ctx) {
        super.enterAdditiveExpr(ctx);
    }

    @Override
    public void exitAdditiveExpr(GrammarParser.AdditiveExprContext ctx) {
        super.exitAdditiveExpr(ctx);
    }

    @Override
    public void enterMultyplicationExpr(GrammarParser.MultyplicationExprContext ctx) {
        super.enterMultyplicationExpr(ctx);
    }

    @Override
    public void exitMultyplicationExpr(GrammarParser.MultyplicationExprContext ctx) {
        super.exitMultyplicationExpr(ctx);
    }

    @Override
    public void enterAtomicExpr(GrammarParser.AtomicExprContext ctx) {
        super.enterAtomicExpr(ctx);
    }

    @Override
    public void exitAtomicExpr(GrammarParser.AtomicExprContext ctx) {
        super.exitAtomicExpr(ctx);
    }

    @Override
    public void enterPrimitiveExpr(GrammarParser.PrimitiveExprContext ctx) {
        super.enterPrimitiveExpr(ctx);
    }

    @Override
    public void exitPrimitiveExpr(GrammarParser.PrimitiveExprContext ctx) {
        super.exitPrimitiveExpr(ctx);
    }

    @Override
    public void enterIndex(GrammarParser.IndexContext ctx) {
        super.enterIndex(ctx);
    }

    @Override
    public void exitIndex(GrammarParser.IndexContext ctx) {
        super.exitIndex(ctx);
    }

    @Override
    public void enterGlobalVariableDeclaration(GrammarParser.GlobalVariableDeclarationContext ctx) {
        super.enterGlobalVariableDeclaration(ctx);
    }

    @Override
    public void exitGlobalVariableDeclaration(GrammarParser.GlobalVariableDeclarationContext ctx) {
        super.exitGlobalVariableDeclaration(ctx);
    }

    @Override
    public void enterGlobalAssignment(GrammarParser.GlobalAssignmentContext ctx) {
        super.enterGlobalAssignment(ctx);
    }

    @Override
    public void exitGlobalAssignment(GrammarParser.GlobalAssignmentContext ctx) {
        super.exitGlobalAssignment(ctx);
    }

    @Override
    public void enterLocalAssignment(GrammarParser.LocalAssignmentContext ctx) {
        super.enterLocalAssignment(ctx);
    }

    @Override
    public void exitLocalAssignment(GrammarParser.LocalAssignmentContext ctx) {
        super.exitLocalAssignment(ctx);
    }

    @Override
    public void enterLocalVariableDeclaration(GrammarParser.LocalVariableDeclarationContext ctx) {
        super.enterLocalVariableDeclaration(ctx);
    }

    @Override
    public void exitLocalVariableDeclaration(GrammarParser.LocalVariableDeclarationContext ctx) {
        super.exitLocalVariableDeclaration(ctx);
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        super.enterEveryRule(ctx);
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        super.exitEveryRule(ctx);
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        super.visitTerminal(node);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        super.visitErrorNode(node);
    }
}
