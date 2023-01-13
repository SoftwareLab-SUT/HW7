package parser;

import codeGenerator.CodeGenerator;
import scanner.lexicalAnalyzer;
import scanner.token.Token;

import java.util.List;
import java.util.Stack;

public class ResponseObject {
    public Stack<Integer> parseStack;
    public Token lookAhead;
    public scanner.lexicalAnalyzer lexicalAnalyzer;
    public List<Rule> rules;
    public ParseTable parseTable;
    public CodeGenerator cg;
    public boolean finish;

    public ResponseObject(Stack<Integer> parseStack, Token lookAhead, scanner.lexicalAnalyzer lexicalAnalyzer,
                          List<Rule> rules, ParseTable parseTable, CodeGenerator cg, boolean finish) {
        this.parseStack = parseStack;
        this.lookAhead = lookAhead;
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.rules = rules;
        this.parseTable = parseTable;
        this.cg = cg;
        this.finish = finish;
    }
}
