package src.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import src.lox.Scanner;

public class Lox {
  /* error reporting */
  static boolean error = false;
  static boolean runTimeError = false;

  static void run(String source) {
    // lets scan and generate tokens
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();

    // For now, just print the tokens.
    for (Token token : tokens) {
      System.out.println(token);
    }

    Parser parser = new Parser(tokens);
    Expr expression = parser.parse();

    if (error)
      return;
    // System.out.println(new ASTPrinter().print(expression));

  }

  static void runInputFile(String fileName) throws IOException {// due to error: unreported exception IOException; must
                                                                // be caught or declared to be thrown

    // check if file is present
    Path file = Paths.get(fileName);

    // byte is 8bit signed 2's complemnemnt int
    byte[] bytes = Files.readAllBytes(file);

    // convert this byte array to String using specific CharacterSet;
    run(new String(bytes, Charset.defaultCharset()));
    if (error) {
      System.exit(65);
    }
  }

  static void reportError(int line, String message) {
    System.err.println("[Error] " + " " + message);
    error = true;
  }

  static void error(Token token, String message) {
    System.out.println("in error token is"+token);
    if (token.type == TokenType.EOF) {
      reportError(token.line, message);
    } else {
      reportError(token.line, message);
    }
  }

  private static void startREPL() throws IOException { // due to error: unreported exception IOException; must be caught
                                                       // or declared to be thrown

    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    while (true) {
      System.out.print("[lox]>>  ");
      String line = reader.readLine();
      if (line == null)
        break; // on pressing ctrl-D , readLine returns NULL (EOF signal is sent to progeam)
      run(line);
      error = false; // for next >> , reset error state
    }
  }

  public static void main(String[] args) throws IOException {
    // Expr expression = new Expr.Binary(
    //     new Expr.Binary(new Expr.Literal(1), new Token(TokenType.PLUS, "+", null, 1), new Expr.Literal(2)),
    //     new Token(TokenType.STAR, "*", null, 1),
    //     new Expr.Binary(new Expr.Literal(4), new Token(TokenType.MINUS, "-", null, 1), new Expr.Literal(3)));

    // System.out.println(new ASTPrinter().print(expression));

    if (args.length > 1) {
    System.out.println("Usage: jlox [script]");
    System.exit(64);
    } else if (args.length == 1) {
    //run the file supplied
    runInputFile(args[0]);
    } else {
    //REPL
    startREPL();
    }
  }
}