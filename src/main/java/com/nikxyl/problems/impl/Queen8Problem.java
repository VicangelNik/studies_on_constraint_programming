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

  public List<Integer[][]> solve() {
    IntVar[][] modelVars = createModelAndVariables();
    setConstraints(modelVars);

    return getSolutions();
  }

  public IntVar[][] createModelAndVariables() {
    model = new Model("Queen8Problem");

    return model.intVarMatrix("queens", 8, 8, 1, 8);
  }

  public void setConstraints(IntVar[][] modelVars) {
    allRowsAreDifferent(modelVars);

    allColumnsAreDifferent(modelVars);

    // all diagonals are different downwards left -> right
//    int i = 0;
//    while (i < 7) { // skip the last one as it is not a diagonal - just one value
//      IntVar[] varsD = new IntVar[8 - i];
//      int step = 0;
//      for (int j = i; j < 8; j++) {
//        varsD[step] = modelVars[j][step];
//        step++;
//      }
//      model.allDifferent(varsD).post();
//      i++;
//    }

    // all diagonals are different downwards right -> left
//    int l = 0;
//    while (l < 7) {
//      IntVar[] varsD = new IntVar[8 - l];
//      int step = 0;
//      for (int k = 7 - l; k >= 0; k--) {
//        varsD[step] = modelVars[step][k];
//        step++;
//      }
//      model.allDifferent(varsD).post();
//      l++;
//    }

//    7 2 1 4 3 5 8 6
//    1 4 8 6 2 7 3 5
//    3 6 5 8 4 1 2 7
//    5 8 3 1 7 6 4 2
//    4 7 6 2 8 3 5 1
//    6 3 4 7 5 2 1 8
//    2 5 7 3 1 8 6 4
//    8 1 2 5 6 4 7 3

    // all diagonals are different right top -> right bottom without the big diagonal (previously done)
    // 17, 26, 35, 44, 53, 62, 71
    // 27, 36, 45, 54, 63, 72
    // 37, 46, 55, 64, 73

//    l = 1;
//    while (l < 6) {
//      IntVar[] varsD = new IntVar[8 - l];
//      int step = l;
//      int t1 = 0;
//      for (int k = 7; k >= l; k--) {
//        varsD[t1] = modelVars[step][k];
//        step++;
//        t1++;
//      }
//      model.allDifferent(varsD).post();
//      l++;
//    }
  }

  private void allRowsAreDifferent(IntVar[][] modelVars) {
    List<TuplePos> positionsRowList = new ArrayList<>();
    List<TuplePos> positionsDiagonalList = new ArrayList<>();
    // all rows are different
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        positionsRowList.add(new TuplePos(i, j));
        for (int k = i, l = j; k < 8 && l < 8; k++, l++) {
          positionsDiagonalList.add(new TuplePos(k, l));
        }
        applyAllDifferent(modelVars, positionsDiagonalList);
      }

      applyAllDifferent(modelVars, positionsRowList);
    }
  }

  private void applyAllDifferent(IntVar[][] modelVars, List<TuplePos> positionsDiagonalList) {
    IntVar[] varsD = new IntVar[positionsDiagonalList.size()];
    for (int t = 0; t < positionsDiagonalList.size(); t++) {
      TuplePos tp = positionsDiagonalList.get(t);
      varsD[t] = modelVars[tp.r][tp.c];
    }
    model.allDifferent(varsD).post();
    positionsDiagonalList.clear();
  }

  private void allColumnsAreDifferent(IntVar[][] modelVars) {
    IntVar[] vars = new IntVar[8];
    int t = 0;
    for (int j = 0; j < 8; j++) {
      for (int i = 0; i < 8; i++) {
        vars[t] = modelVars[i][j];
        t++;
      }
      model.allDifferent(vars).post();
      vars = new IntVar[8];
      t = 0;
    }
  }

  public List<Integer[][]> getSolutions() {
    List<Integer[][]> matrix = new ArrayList<>();
    int solutionCount = 0;
    while (solutionCount++ < MAX_SOLUTIONS && model.getSolver().solve()) {
      IntVar[] cells = model.retrieveIntVars(true);
      matrix.add(getMatrix(cells));
      model.getSolver().reset();
    }
    return matrix;
  }

  public Integer[][] getMatrix(IntVar[] cells) {
    Integer[][] matrix = new Integer[8][8];
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        matrix[i][j] = cells[i * 8 + j].getValue();
      }
    }
    return matrix;
  }

  private record TuplePos(int r, int c) {}
}
