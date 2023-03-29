package graph;

import static compilationunit.CompilationUnitOperations.getClassDeclaration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.github.javaparser.JavaToken;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
public class MyGraphBuilder {
    private final List<String> forbiddenPatternList;

    public MyGraphBuilder(final String... forbiddenPatternList) {
        this.forbiddenPatternList = Arrays.asList(forbiddenPatternList);
    }

    public Map<String, Set<String>> buildFullyQualified(final List<CompilationUnit> compilationUnitList) {
        final Map<String, Set<String>> classNameToImportsMap = new HashMap<>();
        getFilteredCompilationUnits(compilationUnitList).forEach(
            compilationUnit -> buildFullyQualifiedClassNameDependencyMap(classNameToImportsMap, compilationUnit, compilationUnitList));
        return classNameToImportsMap;
    }

    public Map<String, Set<String>> makeStuff(final List<CompilationUnit> compilationUnitList) {
        HashMap<String, Set<String>> packageNameToClassNameMap = new HashMap<>();
        for ( final CompilationUnit compilationUnit : compilationUnitList ) {
            final String className = getClassDeclaration(compilationUnit)
                .map(ClassOrInterfaceDeclaration::getName)
                .map(SimpleName::asString)
                .orElse("");

            final String packageName = compilationUnit
                .getPackageDeclaration()
                .map(PackageDeclaration::getName)
                .map(Name::asString)
                .orElse("");

            if (packageNameToClassNameMap.containsKey(packageName)) {
                packageNameToClassNameMap
                    .get(packageName)
                    .add(className);
            } else {
                final Set<String> list = new HashSet<>();
                list.add(className);
                packageNameToClassNameMap.put(packageName, list);
            }
        }

        return packageNameToClassNameMap;
    }

    public Map<String, Set<String>> buildNLayer(final List<CompilationUnit> compilationUnitList, int n) {
        final Map<String, Set<String>> classNameToImportsMap = new HashMap<>();
        getFilteredCompilationUnits(compilationUnitList).forEach(
            compilationUnit -> buildNLayerDependencyMap(classNameToImportsMap, compilationUnit, n));
        return classNameToImportsMap;
    }

    private Stream<CompilationUnit> getFilteredCompilationUnits(final List<CompilationUnit> compilationUnitList) {
        return compilationUnitList
            .stream()
            .filter(compilationUnit -> getClassDeclaration(compilationUnit).isPresent())
            .filter(this::filterForbiddenClassNamePatterns);
    }

    private Set<String> buildNLayerDependencyMap(final Map<String, Set<String>> classNameToImportsMap,
                                                 final CompilationUnit compilationUnit, final int n) {
        return classNameToImportsMap.putIfAbsent(buildNModule(compilationUnit, n), buildModuleDependencies(compilationUnit));
    }

    private Set<String> buildFullyQualifiedClassNameDependencyMap(final Map<String, Set<String>> classNameToImportsMap,
                                                                  final CompilationUnit compilationUnit,
                                                                  final List<CompilationUnit> compilationUnitList) {
        return classNameToImportsMap.putIfAbsent(buildModule(compilationUnit),
                                                 buildModuleDependencies(compilationUnit, makeStuff(compilationUnitList)));
    }

    private Set<String> buildModuleDependencies(final CompilationUnit compilationUnit) {
        return compilationUnit
            .getImports()
            .stream()
            .map(importDeclaration -> importDeclaration
                .getName()
                .asString())
            .collect(
                Collectors.toSet());
    }

    private Set<String> buildModuleDependencies(final CompilationUnit compilationUnit, final Map<String, Set<String>> map) {
        final String className = getClassDeclaration(compilationUnit)
            .map(ClassOrInterfaceDeclaration::getName)
            .map(SimpleName::asString)
            .orElse("");

        final String packageName = compilationUnit
            .getPackageDeclaration()
            .map(PackageDeclaration::getName)
            .map(Name::asString)
            .orElse("");
        final Set<String> stringList = map.get(packageName);

        final Set<String> dependencySet = compilationUnit
            .getImports()
            .stream()
            .map(importDeclaration -> importDeclaration
                .getName()
                .asString())
            .collect(
                Collectors.toSet());

        final Iterator<JavaToken> iterator = compilationUnit
            .getTokenRange()
            .get()
            .iterator();

        while ( iterator.hasNext() ) {
            final String token = iterator
                .next()
                .getText();
            if (Objects.nonNull(stringList) && !token.equals(className) && stringList.contains(token)) {
                dependencySet.add(String.format("%s.%s", packageName, token));
            }
        }

        return dependencySet;
    }

    private String buildModule(final CompilationUnit compilationUnit) {
        return String.format("%s.%s", compilationUnit
                                 .getPackageDeclaration()
                                 .map(NodeWithName::getNameAsString)
                                 .orElse(""),
                             getClassDeclaration(compilationUnit)
                                 .map(ClassOrInterfaceDeclaration::getName)
                                 .map(SimpleName::asString)
                                 .orElse(""));
    }

    private String buildNModule(final CompilationUnit compilationUnit, final int n) {
        final String packageName = compilationUnit
            .getPackageDeclaration()
            .map(NodeWithName::getNameAsString)
            .orElse("");

        final List<String> packagePath = Arrays.asList(packageName.split("\\."));

        final StringBuilder modulName = new StringBuilder();
        for ( int i = 0; i < n; i++ ) {
            if (i < packagePath.size()) {
                modulName.append(packagePath.get(i));
                modulName.append(".");
            }
        }

        modulName.delete(modulName.length() - 1, modulName.length());
        return modulName.toString();
    }

    private Boolean filterForbiddenClassNamePatterns(final CompilationUnit compilationUnit) {
        return getClassDeclaration(compilationUnit)
            .map(TypeDeclaration::getName)
            .map(SimpleName::asString)
            .map(name -> forbiddenPatternList
                .stream()
                .noneMatch(name::contains))
            .orElse(false);
    }

    public static class Node {
        private String fullyQualifiedName;

        private List<Node> children;

        public Node(final String fullyQualifiedName) {
            this.fullyQualifiedName = fullyQualifiedName;
            children = new ArrayList<>();
        }

        public List<Node> searchNode(final String fullyQualifiedName) {
            return searchNodeInternal(fullyQualifiedName, new ArrayList<>());
        }

        public List<Node> searchNodeInternal(final String fullyQualifiedName, final List<String> alreadyVisited) {
            if (alreadyVisited.contains(fullyQualifiedName)) {
                System.out.println("cycle detected");
                return Collections.emptyList();
            }

            alreadyVisited.add(this.fullyQualifiedName);

            if (this.fullyQualifiedName.equals(fullyQualifiedName)) {
                return Collections.singletonList(this);
            }

            if (children.isEmpty()) {
                return Collections.emptyList();
            }

            return children
                .stream()
                .map(node -> node.searchNodeInternal(fullyQualifiedName, alreadyVisited))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        }
    }

}
