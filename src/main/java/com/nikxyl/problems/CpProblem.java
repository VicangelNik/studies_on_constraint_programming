package com.nikxyl.problems;

import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public abstract class CpProblem {

  protected Model model;
  protected static final int MAX_SOLUTIONS = 3;

  public abstract List<String[][]> solve();

  public abstract IntVar[] createModelAndVariables();

  public abstract void setConstraints(IntVar[] modelVars);

  public abstract List<String[][]> getSolutions();

  public abstract String[][] getMatrix(IntVar[] cells);

  public void printSolution(List<String[][]> solutions) {

    for (String[][] matrix : solutions) {
      for (String[] rows : matrix) {
        for (String cell : rows) {
          System.out.print(cell + " ");
        }
        System.out.println();
      }
      System.out.println();
    }
  }
}
