package src.lox;

public class SymbolTableManager {
    SymbolTable currentSymbolTable;// head

    SymbolTableManager() {
        currentSymbolTable = null;
    }

    void appendNewSymbolTable() {
        // System.out.println("appending new SymTab");
        SymbolTable symTab = new SymbolTable();
        symTab.next = currentSymbolTable;
        currentSymbolTable = symTab;
    }

    void removeSymbolTable() {
        // System.out.println("removing  SymTab");
        currentSymbolTable = currentSymbolTable.next;
    }

    Object getSymbol(String key) {
        SymbolTable searchingInTable = currentSymbolTable;
        while (searchingInTable != null) {
            if (searchingInTable.containsSymbol(key)) {
                return searchingInTable.getSymTabEntry(key);
            }
            searchingInTable = searchingInTable.next;
        }
        return null;
    }

    void addSymbol(String key, Object value) {
        // incorrect if the key is in some other symTab, so commenting
        // currentSymbolTable.putSymTabEntry(key, value);
        if (containsSymbol(key)) {
            SymbolTable searchingInTable = currentSymbolTable;
            while (searchingInTable != null) {
                if (searchingInTable.containsSymbol(key)) {
                    searchingInTable.putSymTabEntry(key, value);
                    break;
                }
                searchingInTable = searchingInTable.next;
            }
        }

        currentSymbolTable.putSymTabEntry(key, value);


    }

    boolean containsSymbol(String key) {
        return (getSymbol(key) != null);
    }
}
