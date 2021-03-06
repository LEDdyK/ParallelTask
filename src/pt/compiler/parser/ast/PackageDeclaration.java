/*
 * Copyright (C) 2007 J�lio Vilmar Gesser.
 * 
 * This file is part of Java 1.5 parser and Abstract Syntax Tree.
 *
 * Java 1.5 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.5 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.5 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 17/10/2007
 */
package pt.compiler.parser.ast;

import pt.compiler.parser.ast.expr.AnnotationExpr;
import pt.compiler.parser.ast.expr.NameExpr;
import pt.compiler.parser.ast.visitor.GenericVisitor;
import pt.compiler.parser.ast.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class PackageDeclaration extends Node {

    private final List<AnnotationExpr> annotations;

    private final NameExpr name;

    public PackageDeclaration(int line, int column, List<AnnotationExpr> annotations, NameExpr name) {
        super(line, column);
        this.annotations = annotations;
        this.name = name;
    }

    public List<AnnotationExpr> getAnnotations() {
        return annotations;
    }

    public NameExpr getName() {
        return name;
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

}
