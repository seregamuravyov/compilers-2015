// Generated from Grammar.g4 by ANTLR 4.5
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GrammarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GrammarParser#programm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgramm(GrammarParser.ProgrammContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#functionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDefinition(GrammarParser.FunctionDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#structDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructDefinition(GrammarParser.StructDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#variableList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableList(GrammarParser.VariableListContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#functionReturnType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReturnType(GrammarParser.FunctionReturnTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#typeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSpecifier(GrammarParser.TypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#structType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructType(GrammarParser.StructTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#primitiveTypeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveTypeSpecifier(GrammarParser.PrimitiveTypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#functionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionBody(GrammarParser.FunctionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#structBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructBody(GrammarParser.StructBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#structFieldDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructFieldDeclaration(GrammarParser.StructFieldDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(GrammarParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#unionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionStatement(GrammarParser.UnionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#conditionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionStatement(GrammarParser.ConditionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStatement(GrammarParser.CompoundStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(GrammarParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(GrammarParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#structCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructCall(GrammarParser.StructCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(GrammarParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(GrammarParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#andExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(GrammarParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#equalityExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpr(GrammarParser.EqualityExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#relationExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationExpr(GrammarParser.RelationExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#additiveExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpr(GrammarParser.AdditiveExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#multyplicationExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultyplicationExpr(GrammarParser.MultyplicationExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#atomicExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomicExpr(GrammarParser.AtomicExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#primitiveExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveExpr(GrammarParser.PrimitiveExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#globalVariableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalVariableDeclaration(GrammarParser.GlobalVariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#globalAssignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalAssignment(GrammarParser.GlobalAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#localAssignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalAssignment(GrammarParser.LocalAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalVariableDeclaration(GrammarParser.LocalVariableDeclarationContext ctx);
}