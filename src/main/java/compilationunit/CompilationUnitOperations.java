package compilationunit;

import java.util.Optional;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class CompilationUnitOperations {

    public static Optional<ClassOrInterfaceDeclaration> getClassDeclaration(final CompilationUnit compilationUnit) {
        return compilationUnit
            .getChildNodes()
            .stream()
            .filter(ClassOrInterfaceDeclaration.class::isInstance)
            .map(ClassOrInterfaceDeclaration.class::cast)
            .findFirst();
    }
}
