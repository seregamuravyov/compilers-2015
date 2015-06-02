// Generated from Grammar.g4 by ANTLR 4.5
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link GrammarParser}.
 */
public interface GrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link GrammarParser#programm}.
	 * @param ctx the parse tree
	 */
	void enterProgramm(GrammarParser.ProgrammContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#programm}.
	 * @param ctx the parse tree
	 */
	void exitProgramm(GrammarParser.ProgrammContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDefinition(GrammarParser.FunctionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDefinition(GrammarParser.FunctionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#structDefinition}.
	 * @param ctx the parse tree
	 */
	void enterStructDefinition(GrammarParser.StructDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#structDefinition}.
	 * @param ctx the parse tree
	 */
	void exitStructDefinition(GrammarParser.StructDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#variableList}.
	 * @param ctx the parse tree
	 */
	void enterVariableList(GrammarParser.VariableListContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#variableList}.
	 * @param ctx the parse tree
	 */
	void exitVariableList(GrammarParser.VariableListContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#functionReturnType}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReturnType(GrammarParser.FunctionReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#functionReturnType}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReturnType(GrammarParser.FunctionReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeSpecifier(GrammarParser.TypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeSpecifier(GrammarParser.TypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#structType}.
	 * @param ctx the parse tree
	 */
	void enterStructType(GrammarParser.StructTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#structType}.
	 * @param ctx the parse tree
	 */
	void exitStructType(GrammarParser.StructTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#primitiveTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveTypeSpecifier(GrammarParser.PrimitiveTypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#primitiveTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveTypeSpecifier(GrammarParser.PrimitiveTypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void enterFunctionBody(GrammarParser.FunctionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void exitFunctionBody(GrammarParser.FunctionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#structBody}.
	 * @param ctx the parse tree
	 */
	void enterStructBody(GrammarParser.StructBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#structBody}.
	 * @param ctx the parse tree
	 */
	void exitStructBody(GrammarParser.StructBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#structFieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterStructFieldDeclaration(GrammarParser.StructFieldDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#structFieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitStructFieldDeclaration(GrammarParser.StructFieldDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(GrammarParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(GrammarParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#unionStatement}.
	 * @param ctx the parse tree
	 */
	void enterUnionStatement(GrammarParser.UnionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#unionStatement}.
	 * @param ctx the parse tree
	 */
	void exitUnionStatement(GrammarParser.UnionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#conditionStatement}.
	 * @param ctx the parse tree
	 */
	void enterConditionStatement(GrammarParser.ConditionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#conditionStatement}.
	 * @param ctx the parse tree
	 */
	void exitConditionStatement(GrammarParser.ConditionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(GrammarParser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(GrammarParser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(GrammarParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(GrammarParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(GrammarParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(GrammarParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#structCall}.
	 * @param ctx the parse tree
	 */
	void enterStructCall(GrammarParser.StructCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#structCall}.
	 * @param ctx the parse tree
	 */
	void exitStructCall(GrammarParser.StructCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(GrammarParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(GrammarParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(GrammarParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(GrammarParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(GrammarParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(GrammarParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#equalityExpr}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpr(GrammarParser.EqualityExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#equalityExpr}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpr(GrammarParser.EqualityExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#relationExpr}.
	 * @param ctx the parse tree
	 */
	void enterRelationExpr(GrammarParser.RelationExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#relationExpr}.
	 * @param ctx the parse tree
	 */
	void exitRelationExpr(GrammarParser.RelationExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#additiveExpr}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpr(GrammarParser.AdditiveExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#additiveExpr}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpr(GrammarParser.AdditiveExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#multyplicationExpr}.
	 * @param ctx the parse tree
	 */
	void enterMultyplicationExpr(GrammarParser.MultyplicationExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#multyplicationExpr}.
	 * @param ctx the parse tree
	 */
	void exitMultyplicationExpr(GrammarParser.MultyplicationExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#atomicExpr}.
	 * @param ctx the parse tree
	 */
	void enterAtomicExpr(GrammarParser.AtomicExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#atomicExpr}.
	 * @param ctx the parse tree
	 */
	void exitAtomicExpr(GrammarParser.AtomicExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#primitiveExpr}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveExpr(GrammarParser.PrimitiveExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#primitiveExpr}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveExpr(GrammarParser.PrimitiveExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#globalVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterGlobalVariableDeclaration(GrammarParser.GlobalVariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#globalVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitGlobalVariableDeclaration(GrammarParser.GlobalVariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#globalAssignment}.
	 * @param ctx the parse tree
	 */
	void enterGlobalAssignment(GrammarParser.GlobalAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#globalAssignment}.
	 * @param ctx the parse tree
	 */
	void exitGlobalAssignment(GrammarParser.GlobalAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#localAssignment}.
	 * @param ctx the parse tree
	 */
	void enterLocalAssignment(GrammarParser.LocalAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#localAssignment}.
	 * @param ctx the parse tree
	 */
	void exitLocalAssignment(GrammarParser.LocalAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclaration(GrammarParser.LocalVariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclaration(GrammarParser.LocalVariableDeclarationContext ctx);
}