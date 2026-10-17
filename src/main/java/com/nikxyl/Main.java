package com.nikxyl;

import com.nikxyl.problems.CpProblem;
import com.nikxyl.problems.impl.Queen8Problem;

public class Main {

  static void main() {

    CpProblem q = new Queen8Problem();
    q.printSolution(q.solve());
  }
}
