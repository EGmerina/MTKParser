// Generated from /home/ekaterina/IT_Projects/MTKParser/src/main/resources/org/example/parser/WhileGrammar.g4 by ANTLR 4.13.2
package org.example.parser.gen.org.example.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link WhileGrammarParser}.
 */
public interface WhileGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link WhileGrammarParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(WhileGrammarParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileGrammarParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(WhileGrammarParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhileGrammarParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(WhileGrammarParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileGrammarParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(WhileGrammarParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhileGrammarParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(WhileGrammarParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileGrammarParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(WhileGrammarParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhileGrammarParser#statementList}.
	 * @param ctx the parse tree
	 */
	void enterStatementList(WhileGrammarParser.StatementListContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileGrammarParser#statementList}.
	 * @param ctx the parse tree
	 */
	void exitStatementList(WhileGrammarParser.StatementListContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhileGrammarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(WhileGrammarParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileGrammarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(WhileGrammarParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhileGrammarParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(WhileGrammarParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileGrammarParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(WhileGrammarParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhileGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(WhileGrammarParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(WhileGrammarParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhileGrammarParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(WhileGrammarParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileGrammarParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(WhileGrammarParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhileGrammarParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(WhileGrammarParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileGrammarParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(WhileGrammarParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhileGrammarParser#cmpOperator}.
	 * @param ctx the parse tree
	 */
	void enterCmpOperator(WhileGrammarParser.CmpOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileGrammarParser#cmpOperator}.
	 * @param ctx the parse tree
	 */
	void exitCmpOperator(WhileGrammarParser.CmpOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhileGrammarParser#addOperator}.
	 * @param ctx the parse tree
	 */
	void enterAddOperator(WhileGrammarParser.AddOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileGrammarParser#addOperator}.
	 * @param ctx the parse tree
	 */
	void exitAddOperator(WhileGrammarParser.AddOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhileGrammarParser#multOperator}.
	 * @param ctx the parse tree
	 */
	void enterMultOperator(WhileGrammarParser.MultOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhileGrammarParser#multOperator}.
	 * @param ctx the parse tree
	 */
	void exitMultOperator(WhileGrammarParser.MultOperatorContext ctx);
}