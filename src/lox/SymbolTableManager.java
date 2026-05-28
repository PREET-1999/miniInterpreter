package src.lox;

public class SymbolTableManager {
    SymbolTable currentSymbolTable;//head

    SymbolTableManager(){
        currentSymbolTable = null;
    }

    void appendNewSymbolTable(){
        SymbolTable symTab = new SymbolTable();
        symTab.next = currentSymbolTable;
        currentSymbolTable = symTab;
    }
    void removeSymbolTable(){
        currentSymbolTable = currentSymbolTable.next;
    }

    Object getSymbol(String key){
        SymbolTable searchingInTable = currentSymbolTable;
        while(searchingInTable!=null){
            if(searchingInTable.containsSymbol(key)){
                return searchingInTable.getSymTabEntry(key);
            }
            searchingInTable = searchingInTable.next;
        }
        return null;
    }
    void addSymbol(String key, Object value){
        currentSymbolTable.putSymTabEntry(key, value);
    }

    boolean containsSymbol(String key){
        return (getSymbol(key) != null);
    }

}
