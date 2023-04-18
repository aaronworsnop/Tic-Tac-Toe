import java.util.Scanner;

public class TicTacToe {

  static int[][] board = new int[3][3];
  static boolean gameEnd = false;
  static Scanner input = new Scanner(System.in);
  static boolean userTurn = true;

  public static void main(String[] args) {
    // Setting up the game
    startGame();

    while (gameEnd == false) {
      // AI's turn
      System.out.println("\nAi's move...\n");
      int aiMove = miniMaxMove();
      updateBoard(aiMove, -1);
      printBoard();
      checkGameEnd();

      if (gameEnd) {
        break;
      }

      // Retrieving user's move
      System.out.println("\nYour move...\n");
      int move = -9;
      while (move == -9) {
        move = requestMove();
      }
      updateBoard(move, 1);
      printBoard();
      checkGameEnd();
    }
  }

  public static void startGame() {
    System.out.println("\n\n\n\n\n\n\n\n\n\n=== TIC TAC TOE ===\n");

    // Initialising board
    clearBoard();

    // Initial tutorial for the player
    System.out.println("Type in the the position you want to place your token and press \"ENTER\"");
    System.out.println("\n1 2 3\n4 5 6\n7 8 9\n");
  }

  public static void clearBoard() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        board[i][j] = 0;
      }
    }
  }

  public static void printBoard() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (board[i][j] == 0) {
          // Blank
          System.out.print("_ ");
        } else if (board[i][j] == 1) {
          // Player token
          System.out.print("O ");
        } else if (board[i][j] == -1) {
          // AI token
          System.out.print("X ");
        }
      }
      System.out.println("");
    }
    System.out.println("");
  }

  public static int requestMove() {
    int userMove = 0;
    while (userMove < 1 || userMove > 9) {
      // Retrieving user's move and ensuring validity
      if (input.hasNextInt()) {
        userMove = input.nextInt();
      } else {
        input.next();
        continue;
      }
    }

    // Checking availability
    if (checkBoard(userMove)) {
      System.out.println("");
      return userMove;
    }

    // Not available
    return -9;
  }

  public static boolean checkBoard(int userMove) {
    if (userMove < 4) {
      if (board[0][userMove - 1] == 0) {
        return true;
      }
    } else if (userMove > 6) {
      if (board[2][userMove - 7] == 0) {
        return true;
      }
    } else {
      if (board[1][userMove - 4] == 0) {
        return true;
      }
    }
    return false;
  }

  public static void updateBoard(int gameMove, int player) {
    if (gameMove < 4) {
      // FIRST row
      board[0][gameMove - 1] = player;
      System.out.print("");
    } else if (gameMove > 6) {
      // THIRD row
      board[2][gameMove - 7] = player;
      System.out.print("");
    } else {
      // SECOND row
      board[1][gameMove - 4] = player;
      System.out.print("");
    }
  }

  public static int howGameEnd() {
    // Checking for a win (1 for player, -1 for AI)
    for (int i = 0; i < 3; i++) {
      // Horizontal possibilities
      if ((board[i][0] == board[i][1] && board[i][1] == board[i][2]) && board[i][0] != 0) {
        return board[i][0];
      }
    }

    for (int i = 0; i < 3; i++) {
      // Vertical possibilities
      if ((board[0][i] == board[1][i] && board[1][i] == board[2][i]) && board[0][i] != 0) {
        return board[0][i];
      }
    }

    for (int i = 0; i < 2; i++) {
      // Diagonal possibilities
      if (((board[0][0] == board[1][1] && board[1][1] == board[2][2])
              || (board[0][2] == board[1][1] && board[1][1] == board[2][0]))
          && board[1][1] != 0) {
        return board[1][1];
      }
    }

    // Checking if the board is full (0)
    boolean full = true;

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (board[i][j] == 0) {
          full = false;
          break;
        }
      }
    }

    if (full) {
      return 0;
    }

    // Game has not ended, continue to play (9)
    return 9;
  }

  public static void checkGameEnd() {
    if (howGameEnd() == 0) {
      System.out.println("\nIt's a tie!\n");
      gameEnd = true;
    } else if (howGameEnd() == 1) {
      // This should not be possible
      System.out.println("\nYou win!\n");
      gameEnd = true;
    } else if (howGameEnd() == -1) {
      System.out.println("\nThe AI wins!\n");
      gameEnd = true;
    }
  }

  // A win at depth 1 is worth 20 | A win at depth 9 is worth 11;
  // A draw at depth 1 is worth 10 | A draw at depth 9 is worth 1;
  // A loss at depth 1 is worth -10 | A loss at depth 9 is worth -1;

  // This scoring system allows for the AI to always make THE best move, rather than A best move.

  public static int miniMaxMove() {
    // Using the MiniMax algorithm to find the best move to play (AI's move)
    int bestScore = 30;
    int bestMove = 0;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        // Checking for available position
        if (board[i][j] == 0) {
          board[i][j] = -1;
          int score = miniMax(true, 30, -20, 0);
          board[i][j] = 0;

          // New best move
          if (score < bestScore) {
            bestScore = score;
            bestMove = i * 3 + j + 1;
          }
        }
      }
    }
    return bestMove;
  }

  public static int miniMax(boolean isMaximising, int alpha, int beta, int depth) {
    // Minimax algorithm
    int result = howGameEnd();
    if (result == 1) {
      // Lower depth is a higher win
      return 20 - depth;
    } else if (result == 0) {
      // Lower depth is a higher tie
      return depth;
    } else if (result == -1) {
      // Lower depth is a Higher loss
      return -10 + depth;
    }

    if (isMaximising) {
      // High scores
      int bestScore = -20;
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          // Checking for available position
          if (board[i][j] == 0) {
            board[i][j] = 1;
            // Recursive call
            int score = miniMax(false, alpha, beta, depth + 1);
            board[i][j] = 0;

            // Pruning

            // New best move
            bestScore = Math.max(bestScore, score);
          }
        }
      }
      return bestScore;
    } else {
      // Low scores
      int bestScore = 30;
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          // Checking for available position
          if (board[i][j] == 0) {
            board[i][j] = -1;
            // Recursive call
            int score = miniMax(true, alpha, beta, depth + 1);
            board[i][j] = 0;

            // Pruning

            // New best move
            bestScore = Math.min(bestScore, score);
          }
        }
      }
      return bestScore;
    }
  }
}
