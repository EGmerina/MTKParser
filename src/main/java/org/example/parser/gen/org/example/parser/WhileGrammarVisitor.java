// Generated from /home/ekaterina/IT_Projects/MTKParser/src/main/resources/org/example/parser/WhileGrammar.g4 by ANTLR 4.13.2
package org.example.parser.gen.org.example.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link WhileGrammarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface WhileGrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link WhileGrammarParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(WhileGrammarParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link WhileGrammarParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(WhileGrammarParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link WhileGrammarParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(WhileGrammarParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link WhileGrammarParser#statementList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementList(WhileGrammarParser.StatementListContext ctx);
	/**
	 * Visit a parse tree produced by {@link WhileGrammarParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(WhileGrammarParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link WhileGrammarParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(WhileGrammarParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link WhileGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(WhileGrammarParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link WhileGrammarParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(WhileGrammarParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link WhileGrammarParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(WhileGrammarParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link WhileGrammarParser#cmpOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmpOperator(WhileGrammarParser.CmpOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link WhileGrammarParser#addOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddOperator(WhileGrammarParser.AddOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link WhileGrammarParser#multOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultOperator(WhileGrammarParser.MultOperatorContext ctx);
}