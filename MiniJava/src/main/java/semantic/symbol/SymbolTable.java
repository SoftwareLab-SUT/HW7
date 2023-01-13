package semantic.symbol;

import codeGenerator.Address;
import codeGenerator.Memory;
import codeGenerator.TypeAddress;
import codeGenerator.varType;
import errorHandler.ErrorHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private Map<String, Klass> klasses;
    private Map<String, Address> keyWords;
    private Memory mem;
    private SymbolType lastType;

    public SymbolTable(Memory memory) {
        mem = memory;
        klasses = new HashMap<>();
        keyWords = new HashMap<>();
        getKeyWords().put("true", new Address(1, varType.Bool, TypeAddress.Imidiate));
        getKeyWords().put("false", new Address(0, varType.Bool, TypeAddress.Imidiate));
    }

    public void setLastType(SymbolType type) {
        lastType = type;
    }

    public void addClass(String className) {
        if (getKlasses().containsKey(className)) {
            ErrorHandler.printError("This class already defined");
        }
        getKlasses().put(className, new Klass());
    }

    public void addField(String fieldName, String className) {
        getMem().addToLastDataAddress(getMem().getDataSize());
        getKlasses().get(className).Fields.put(fieldName, new Symbol(getLastType(), getMem().getDateAddress()));
    }

    public void addMethod(String className, String methodName, int address) {
        if (getKlasses().get(className).Methodes.containsKey(methodName)) {
            ErrorHandler.printError("This method already defined");
        }
        getKlasses().get(className).Methodes.put(methodName, new Method(address, getLastType()));
    }

    public void addMethodParameter(String className, String methodName, String parameterName) {
        getKlasses().get(className).Methodes.get(methodName).addParameter(parameterName);
    }

    public void addMethodLocalVariable(String className, String methodName, String localVariableName) {
//        try {
        if (getKlasses().get(className).Methodes.get(methodName).localVariable.containsKey(localVariableName)) {
            ErrorHandler.printError("This variable already defined");
        }
        getMem().addToLastDataAddress(getMem().getDataSize());
        getKlasses().get(className).Methodes.get(methodName).localVariable.put(localVariableName, new Symbol(getLastType(), getMem().getDateAddress()));
//        }catch (NullPointerException e){
//            e.printStackTrace();
//        }
    }

    public void setSuperClass(String superClass, String className) {
        getKlasses().get(className).superClass = getKlasses().get(superClass);
    }

    public Address get(String keywordName) {
        return getKeyWords().get(keywordName);
    }

    public Symbol get(String fieldName, String className) {
//        try {
        return getKlasses().get(className).getField(fieldName);
//        }catch (NullPointerException n)
//        {
//            n.printStackTrace();
//            return null;
//        }
    }

    public Symbol get(String className, String methodName, String variable) {
        Symbol res = getKlasses().get(className).Methodes.get(methodName).getVariable(variable);
        if (res == null) {
            res = get(variable, className);
        }
        return res;
    }

    public Symbol getNextParam(String className, String methodName) {
        return getKlasses().get(className).Methodes.get(methodName).getNextParameter();
    }

    public void startCall(String className, String methodName) {
//        try {
        getKlasses().get(className).Methodes.get(methodName).reset();
//        }catch (NullPointerException n)
//        {
//            n.printStackTrace();
//        }
    }

    public int getMethodCallerAddress(String className, String methodName) {
        return getKlasses().get(className).Methodes.get(methodName).callerAddress;
    }

    public int getMethodReturnAddress(String className, String methodName) {
        return getKlasses().get(className).Methodes.get(methodName).returnAddress;
    }

    public SymbolType getMethodReturnType(String className, String methodName) {
//        try {
        return getKlasses().get(className).Methodes.get(methodName).returnType;
//        }catch (NullPointerException ed){
//            ed.printStackTrace();
//            return null;
//        }

    }

    public int getMethodAddress(String className, String methodName) {
        return getKlasses().get(className).Methodes.get(methodName).codeAddress;
    }


    class Klass {
        public Map<String, Symbol> Fields;
        public Map<String, Method> Methodes;
        public Klass superClass;

        public Klass() {
            Fields = new HashMap<>();
            Methodes = new HashMap<>();
        }

        public Symbol getField(String fieldName) {
            if (Fields.containsKey(fieldName)) {
                return Fields.get(fieldName);
            }
            return superClass.getField(fieldName);

        }

    }

    class Method {
        public int codeAddress;
        public Map<String, Symbol> parameters;
        public Map<String, Symbol> localVariable;
        private List<String> orderdParameters;
        public int callerAddress;
        public int returnAddress;
        public SymbolType returnType;
        private int index;

        public Method(int codeAddress, SymbolType returnType) {
            this.codeAddress = codeAddress;
            this.returnType = returnType;
            this.orderdParameters = new ArrayList<>();
            getMem().addToLastDataAddress(getMem().getDataSize());
            this.returnAddress = getMem().getDateAddress();
            getMem().addToLastDataAddress(getMem().getDataSize());
            this.callerAddress = getMem().getDateAddress();
            this.parameters = new HashMap<>();
            this.localVariable = new HashMap<>();
        }

        public Symbol getVariable(String variableName) {
            if (parameters.containsKey(variableName)) {
                return parameters.get(variableName);
            }
            if (localVariable.containsKey(variableName)) {
                return localVariable.get(variableName);
            }
            return null;
        }

        public void addParameter(String parameterName) {
            getMem().addToLastDataAddress(getMem().getDataSize());
            parameters.put(parameterName, new Symbol(getLastType(), getMem().getDateAddress()));
            getOrderdParameters().add(parameterName);
        }

        private void reset() {
            setIndex(0);
        }

        private Symbol getNextParameter() {
            return parameters.get(getOrderdParameters().get(index++));
        }

        public List<String> getOrderdParameters() {
            return orderdParameters;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public Map<String, Klass> getKlasses() {
        return klasses;
    }

    public Map<String, Address> getKeyWords() {
        return keyWords;
    }

    public Memory getMem() {
        return mem;
    }

    public SymbolType getLastType() {
        return lastType;
    }
}

//class Symbol{
//    public SymbolType type;
//    public int address;
//    public Symbol(SymbolType type , int address)
//    {
//        this.type = type;
//        this.address = address;
//    }
//}
//enum SymbolType{
//    Int,
//    Bool
//}