package parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import Log.Log;
import codeGenerator.CodeGenerator;
import errorHandler.ErrorHandler;
import scanner.lexicalAnalyzer;
import scanner.token.Token;

public class Parser {
    private List<Rule> rules;
    private Stack<Integer> parsStack;
    private ParseTable parseTable;
    private lexicalAnalyzer lexicalAnalyzer;
    private CodeGenerator cg;
    public ParserFacade pf = new ParserFacade();

    public Parser() {
        setParsStack(new Stack<Integer>());
//        parsStack = new Stack<Integer>();
        getParsStack().push(0);
        try {
            setParseTable(new ParseTable(Files.readAllLines(Paths.get("src/main/resources/parseTable")).get(0)));
//            parseTable = new ParseTable(Files.readAllLines(Paths.get("src/main/resources/parseTable")).get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setRules(new ArrayList<Rule>());
//        rules = new ArrayList<Rule>();
        try {
            for (String stringRule : Files.readAllLines(Paths.get("src/main/resources/Rules"))) {
                getRules().add(new Rule(stringRule));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setCg(new CodeGenerator());
//        cg = new CodeGenerator();
    }

    public void startParse(java.util.Scanner sc) {
        setLexicalAnalyzer(new lexicalAnalyzer(sc));
//        lexicalAnalyzer = new lexicalAnalyzer(sc);
        Token lookAhead = getLexicalAnalyzer().getNextToken();
        boolean finish = false;
        Action currentAction;
        while (!finish) {
            try {
                Log.print(/*"lookahead : "+*/ lookAhead.toString() + "\t" + getParsStack().peek());
//                Log.print("state : "+ parsStack.peek());
                currentAction = getParseTable().getActionTable(getParsStack().peek(), lookAhead);
                Log.print(currentAction.toString());
                //Log.print("");
                ResponseObject facadeResponse = pf.switchAction(currentAction, parsStack, lookAhead, lexicalAnalyzer,
                                                                rules, parseTable, cg);
                parsStack = facadeResponse.parseStack;
                lookAhead = facadeResponse.lookAhead;
                lexicalAnalyzer = facadeResponse.lexicalAnalyzer;
                rules = facadeResponse.rules;
                parseTable = facadeResponse.parseTable;
                cg = facadeResponse.cg;
                finish = facadeResponse.finish;
                Log.print("");
            } catch (Exception ignored) {
                ignored.printStackTrace();
//                boolean find = false;
//                for (NonTerminal t : NonTerminal.values()) {
//                    if (parseTable.getGotoTable(parsStack.peek(), t) != -1) {
//                        find = true;
//                        parsStack.push(parseTable.getGotoTable(parsStack.peek(), t));
//                        StringBuilder tokenFollow = new StringBuilder();
//                        tokenFollow.append(String.format("|(?<%s>%s)", t.name(), t.pattern));
//                        Matcher matcher = Pattern.compile(tokenFollow.substring(1)).matcher(lookAhead.toString());
//                        while (!matcher.find()) {
//                            lookAhead = lexicalAnalyzer.getNextToken();
//                        }
//                    }
//                }
//                if (!find)
//                    parsStack.pop();
            }
        }
        if (!ErrorHandler.hasError) {
            getCg().printMemory();
        }
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Stack<Integer> getParsStack() {
        return parsStack;
    }

    public ParseTable getParseTable() {
        return parseTable;
    }

    public scanner.lexicalAnalyzer getLexicalAnalyzer() {
        return lexicalAnalyzer;
    }

    public CodeGenerator getCg() {
        return cg;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public void setParsStack(Stack<Integer> parsStack) {
        this.parsStack = parsStack;
    }

    public void setParseTable(ParseTable parseTable) {
        this.parseTable = parseTable;
    }

    public void setLexicalAnalyzer(scanner.lexicalAnalyzer lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
    }

    public void setCg(CodeGenerator cg) {
        this.cg = cg;
    }
}
