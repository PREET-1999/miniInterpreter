public class test {
public static void main(String[] args) {
    Book acd = new ACD();
    ReadBookTitle readVisitor = new ReadBookTitle();
    HighlightBook high = new HighlightBook();
    acd.accept(readVisitor);
    acd.accept(high);
}
}

abstract class Book {
    abstract void accept(BookVisitor bookVisitor);
}

class ACD extends Book {
    public void accept(BookVisitor bookVisitor) {
        bookVisitor.visitACD(this);
    }
}

class CI extends Book {
public void accept(BookVisitor bookVisitor) {
        bookVisitor.visitCI(this);
    }
}

interface BookVisitor {

    public void visitACD(ACD acd);

    public void visitCI(CI ci);

}

class ReadBookTitle implements BookVisitor {
    public void visitACD(ACD acd) {
        System.out.println("Title ACD");
    }

    public void visitCI(CI ci) {
        System.out.println("Title CI");

    }
}
class HighlightBook implements BookVisitor {
    public void visitACD(ACD acd) {
        System.out.println("Highlight ACD");
    }

    public void visitCI(CI ci) {
        System.out.println("Title CI");

    }
}