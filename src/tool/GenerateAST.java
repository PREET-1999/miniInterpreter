package src.tool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class Field {
    String name;
    String type;

    Field(String name, String type) {
        this.name = name;
        this.type = type;
    }
}

class ClassDef {
    String name;
    Field[] fields;

    ClassDef(String name, Field[] fields) {
        this.name = name;
        this.fields = fields;
    }
}

public class GenerateAST {

    public static void main(String[] args) throws IOException {
        String outputDir = args.length > 0 ? args[0] : ".";

        List<ClassDef> exprTypes = List.of(
                new ClassDef("Binary", new Field[] {
                        new Field("left", "Expr"),
                        new Field("operator", "Token"),
                        new Field("right", "Expr")
                }),
                new ClassDef("Grouping", new Field[] {
                        new Field("expression", "Expr")
                }),
                new ClassDef("Literal", new Field[] {
                        // new Field("value", "Token") // lets see why OBject would be better
                        new Field("value", "Object") // OBject as I needed to create as new Literal(123);

                }),
                new ClassDef("Unary", new Field[] {
                        new Field("operator", "Token"),
                        new Field("right", "Expr")
                }));

        generateAST(outputDir, "Expr", exprTypes);

        //Now for stmt
        List<ClassDef> stmtTypes = List.of(
                new ClassDef("Expression", new Field[] {
                        new Field("expression", "Expr")
                }),
                new ClassDef("Print", new Field[] {
                        new Field("expression", "Expr")
                }),
                new ClassDef("VarDecl", new Field[] {
                        new Field("expression", "Expr"),
                        new Field("varId", "Token"),

                })
               );
                generateAST(outputDir, "Stmt", stmtTypes);

        //Now for Decl
        


    }

    static void defineVisitor(StringBuilder content, String baseName, List<ClassDef> types) {
        content.append("    interface Visitor<R> {\n");

        for (ClassDef type : types) {
            content.append("        R taskOn")
                    .append(type.name)
                    .append(baseName)
                    .append("(")
                    .append(type.name)
                    .append(" ")
                    .append(baseName.toLowerCase())
                    .append(");\n");
        }

        content.append("    }\n\n");
    }

    static void generateAST(String outputDir, String baseName, List<ClassDef> types)
            throws IOException {

        StringBuilder content = new StringBuilder();
        content.append("package ")
                .append(outputDir.replace(System.getProperty("file.separator"), "."))
                .append(";\n\n");
        content.append("abstract class ").append(baseName).append(" {\n");

        // Visitor
        defineVisitor(content, baseName, types);

        content.append("    abstract <R> R accept(Visitor<R> visitor);\n\n");

        // Generate subclasses
        for (ClassDef type : types) {
            defineType(content, baseName, type);
        }

        content.append("}\n");

        Path path = Path.of(outputDir, baseName + ".java");
        Files.writeString(path, content.toString());
    }

    static void defineType(StringBuilder content, String baseName, ClassDef classDef) {
        content.append("    static class ")
                .append(classDef.name)
                .append(" extends ")
                .append(baseName)
                .append(" {\n");

        // Fields
        for (Field field : classDef.fields) {
            content.append("        final ")
                    .append(field.type)
                    .append(" ")
                    .append(field.name)
                    .append(";\n");
        }

        //// visit/taskOn for my naming convention
        content.append("\n");
        content.append("\n        @Override\n")
                .append("        public <R> R accept(Visitor<R> visitor) {\n")
                .append("            return visitor.taskOn")
                .append(classDef.name)
                .append(baseName)
                .append("(this);\n")
                .append("        }\n");

        // Constructor
        content.append("        ")
                .append(classDef.name)
                .append("(");

        for (int i = 0; i < classDef.fields.length; i++) {
            Field f = classDef.fields[i];
            content.append(f.type).append(" ").append(f.name);
            if (i < classDef.fields.length - 1) {
                content.append(", ");
            }
        }

        content.append(") {\n");

        // Assignments
        for (Field field : classDef.fields) {
            content.append("            this.")
                    .append(field.name)
                    .append(" = ")
                    .append(field.name)
                    .append(";\n");
        }

        content.append("        }\n");

        content.append("    }\n");
    }
}