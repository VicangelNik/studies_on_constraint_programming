package com.nikxyl.problems.impl;

import java.util.ArrayList;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import com.nikxyl.problems.CpProblem;

/**
 * This class solves the 8-queens problem using constraint programming.
 * 8 queens in 8x8 chessboard cannot attack each other.
 * Meaning that no two queens can be in the same row, column, or diagonal.
 */
public class Queen8Problem extends CpProblem {

  private final int nQueens = 8;

  public List<String[][]> solve() {
    IntVar[] modelVars = createModelAndVariables();
    setConstraints(modelVars);

    return getSolutions();
  }

  public IntVar[] createModelAndVariables() {
    model = new Model("Queen8Problem");

    return model.intVarArray("Q", nQueens, 1, nQueens, false);
  }

  public void setConstraints(IntVar[] modelVars) {
    // Rule 1: No two queens can be in the same column
    model.allDifferent(modelVars).post();

    for (int i = 0; i < nQueens; i++) {
      for (int j = i + 1; j < nQueens; j++) {
        // Diagonal 1: Column_i # Column_j + (j-i) (Downward-sloping: ↘)
        model.arithm(modelVars[i], "!=", modelVars[j], "+", j - i).post();

        // Diagonal 2: Column_i # Column_j - (j-i)  (Upward-sloping: ↗)
        model.arithm(modelVars[i], "!=", modelVars[j], "-", j - i).post();
      }
    }
  }

  public List<String[][]> getSolutions() {
    List<String[][]> matrix = new ArrayList<>();
    int solutionCount = 0;
    while (solutionCount++ < MAX_SOLUTIONS) {
      if (model.getSolver().solve()) {
        IntVar[] cells = model.retrieveIntVars(false);
        matrix.add(getMatrix(cells));
        model.getSolver().reset();
      } else {
        System.out.printf("No solution found on try {%s}.%n", solutionCount);
      }
    }
    return matrix;
  }

  public String[][] getMatrix(IntVar[] cells) {
    String[][] matrix = new String[nQueens][nQueens];

    for (int i = 0; i < nQueens; i++) {
      IntVar c = cells[i];
      for (int j = 1; j <= nQueens; j++) {
        if (j == c.getValue()) {
          matrix[i][j - 1] = c.getName();
        } else {
          matrix[i][j - 1] = ".   ";
        }
      }
    }
    return matrix;
  }
}
