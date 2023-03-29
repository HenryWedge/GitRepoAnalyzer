package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.tools.ant.DirectoryScanner;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class ASTParser {

    public List<CompilationUnit> getCompilationUnits(final String sourceCodeInputPath, final String libInputPath) {
        // Get the java files of given root.
        final String[] patterns = {"**/*.java"};
        final List<String> sourceList = Arrays
            .asList(filesScannedInDirectory(sourceCodeInputPath, patterns));

        setupSymbolSolver(sourceCodeInputPath, libInputPath);

        // Parse all files.
        return sourceList
            .stream()
            .map(source -> {
                try {
                    return StaticJavaParser.parse(new File(source));
                } catch ( final FileNotFoundException | ParseProblemException | StackOverflowError e ) {
                    //e.printStackTrace();
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private void setupSymbolSolver(final String sourceCodeInput, final String libDir) {

        final List<TypeSolver> parseTypeSolvers = new ArrayList<>();
        parseTypeSolvers.add(new JavaParserTypeSolver(sourceCodeInput));

        // Add the ReflectionTypeSolver for resolve internal connections of java
        // in-build-functions.
        parseTypeSolvers.add(new ReflectionTypeSolver());

        // Add external Library files.
        if (libDir != null && !libDir.equals("")) {
            addLibSourcesToParseTypeSources(libDir, parseTypeSolvers);
        }

        // Setup java symbol solver
        final TypeSolver typeSolver = new CombinedTypeSolver(parseTypeSolvers);
        final JavaSymbolSolver symSolv = new JavaSymbolSolver(typeSolver);

        StaticJavaParser
            .getConfiguration()
            .setSymbolResolver(symSolv)
            .setLanguageLevel(LanguageLevel.JAVA_17);
    }

    private void addLibSourcesToParseTypeSources(final String libDir, final List<TypeSolver> parseTypeSolvers) {
        final List<String> libSources = Arrays
            .asList(filesScannedInDirectory(libDir, new String[]{"**/*.jar"}));

        libSources
            // Create and collect JarType Solvers form given libraries and map to the
            // corresponding library name
            .forEach((lib -> {
                try {
                    parseTypeSolvers.add(new JarTypeSolver(lib));
                } catch ( IOException ignore ) {
                    // don't perform an action when jar cannot be resolved
                }
            }));
    }

    private static String[] filesScannedInDirectory(String baseDir, final String[] wildcardPattern) {
        baseDir = new File(baseDir).getAbsolutePath();
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(wildcardPattern);
        scanner.setBasedir(baseDir);
        scanner.setExcludes(new String[]{});
        scanner.scan();
        final String[] returnValues = scanner.getIncludedFiles();
        for ( int i = 0; i < returnValues.length; i++ ) {
            returnValues[i] = baseDir + File.separator + returnValues[i];
        }
        return returnValues;
    }
}
