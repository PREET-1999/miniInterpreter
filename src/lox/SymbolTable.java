package src.lox;
import java.util.HashMap; // Import the HashMap class

//ek new symbolTable Banayengae which would be instantiated at the very start of program??
//fir uske insrance ko ghumana padega
//better static rakhdo usko (//...FOUND top level class declared as static would give compile time error)
public class SymbolTable {
        static HashMap<String, Object> symbolTable = new HashMap<>();

        static void putSymTabEntry(String key, Object value){
            symbolTable.put(key,value);
        }

        static Object getSymTabEntry(String key){
            return symbolTable.get(key);
        }

}
