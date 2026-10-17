package com.nikxyl.problems;

import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public abstract class CpProblem {

  protected Model model;
  protected static final int MAX_SOLUTIONS = 1;

  public abstract List<Integer[][]> solve();

  public abstract IntVar[][] createModelAndVariables();

  public abstract void setConstraints(IntVar[][] modelVars);

  public abstract List<Integer[][]> getSolutions();

  public abstract Integer[][] getMatrix(IntVar[] cells);

  public void printSolution(List<Integer[][]> solutions) {

    for (Integer[][] matrix : solutions) {
      for (Integer[] rows : matrix) {
        for (Integer cell : rows) {
          System.out.print(cell + " ");
        }
        System.out.println();
      }
      System.out.println();
    }
  }
}
