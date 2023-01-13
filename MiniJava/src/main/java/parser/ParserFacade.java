package parser;

import Log.Log;
import codeGenerator.CodeGenerator;
import scanner.lexicalAnalyzer;
import scanner.token.Token;

import java.util.List;
import java.util.Stack;


public class ParserFacade {
    public ResponseObject switchAction (Action currentAction, Stack<Integer> parseStack,
                                  Token lookAhead, lexicalAnalyzer lexicalAnalyzer,
                                  List<Rule> rules, ParseTable parseTable,
                                  CodeGenerator cg
                                  ) {
        boolean finish = false;
        switch (currentAction.getAction()) {
            case shift:
                parseStack.push(currentAction.number);
                lookAhead = lexicalAnalyzer.getNextToken();

                break;
            case reduce:
                Rule rule = rules.get(currentAction.number);
                for (int i = 0; i < rule.RHS.size(); i++) {
                    parseStack.pop();
                }

                Log.print(/*"state : " +*/ parseStack.peek() + "\t" + rule.LHS);
//                        Log.print("LHS : "+rule.LHS);
                parseStack.push(parseTable.getGotoTable(parseStack.peek(), rule.LHS));
                Log.print(/*"new State : " + */parseStack.peek() + "");
//                        Log.print("");
                try {
                    cg.semanticFunction(rule.semanticAction, lookAhead);
                } catch (Exception e) {
                    Log.print("Code Genetator Error");
                }
                break;
            case accept:
                finish = true;
                break;
        }
        ResponseObject response = new ResponseObject(parseStack, lookAhead, lexicalAnalyzer, rules, parseTable, cg, finish);
        return response;
    }
}
