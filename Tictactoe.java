import java.io.*;
import java.util.*;

public class Tictactoe {
    static Scanner input = new Scanner(System.in);
    static char[][] board = new char[3][3];
    static Stack<Integer> moveHistory = new Stack<>();
    static String playerX = "Player X";
    static String playerO = "Player O";
    static boolean vsAI = false;
    static boolean xTurn = true;
    static int scoreX = 0, scoreO = 0;
    static boolean exitProgram = false;

    // =====================================================
    //                 Main Menu
    //======================================================
    public static void main(String[] args) {
        while(!exitProgram){
            showMenu();
            String choice=input.nextLine();
            switch(choice) {
                case "1":
                    startGameWithSetup();
                    break;
                case "2":
                    System.out.println("\n-------SCORE HISTORY-------");
                    printScore();
                    break;
                case"3":
                    readLastMove();
                    break;
                case "4":
                    readLastSavedBoard();
                    break;
                case"5":
                    deleteSavedFiles();
                    break;
                case "6":
                    System.out.println("Exiting!!! Thanks for playing!");
                    exitProgram = true;
                    break;

                default:
                    System.out.println("Invalid choice! Try again buddy.");
            }
        }
    }

    //===============================- MENU DISPLAY -================================//
    static void showMenu() {
        System.out.println("\n------------------------------");
        System.out.println("           MAIN MENU          ");
        System.out.println("------------------------------");
        System.out.println("1. Start New Game");
        System.out.println("2. View Score File");
        System.out.println("3. View Last Move");
        System.out.println("4. View Last Saved Board");
        System.out.println("5. Delete Save Files");
        System.out.println("6. Exit");
        System.out.println("Choose an option: ");
    }

    //==========- START GAME SETUP -===========//
    static void startGameWithSetup() {
        System.out.println("\n===== Welcome to Ultimate Tic Tac Toe =====");
        displayBoardGuide();

        System.out.print("Enter Player X's name: ");
        playerX = input.nextLine();

        System.out.print("Play against AI? (yes/no): ");
        vsAI = input.nextLine().trim().equalsIgnoreCase("yes");

        if (!vsAI) {
            System.out.print("Enter Player O's name: ");
            playerO = input.nextLine();
        }

        System.out.print("Who should start first? (X/O): ");
        xTurn = input.nextLine().trim().equalsIgnoreCase("X");

        File file = new File("Score.txt");




        while (true) {
            try {
                FileWriter fw = new FileWriter("Score.txt",true);
                PrintWriter writer = new PrintWriter(fw);
                resetBoard();
                playGame();

                System.out.printf("Score: %s: %d | %s: %d\n", playerX, scoreX, playerO, scoreO);
                writer.println(playerX + scoreX + playerO + scoreO);
                writer.close();
                fw.close();

                printScore();
                System.out.print("Play again? (yes/no): ");

                if (!input.nextLine().trim().equalsIgnoreCase("yes")) {
                    break;
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            catch(IOException ex){

            }
        }


        System.out.println("Thanks for playing. Byeee!");
    }

    //========================- DISPLAY BOARD GUIDE -============================//

    static void displayBoardGuide() {
        System.out.println("\nBoard Position Guide:");
        System.out.println(" 0 | 1 | 2\n-----------\n 3 | 4 | 5\n-----------\n 6 | 7 | 8\n");
    }

    static void resetBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = (char)('0' + i * 3 + j);
        moveHistory.clear();
    }

    //================================- GAME LOOP -================================//
    static void playGame() {
        while (true) {
            displayBoard();
            if (xTurn || !vsAI) {
                playerMove();
            } else {
                aiMove();
            }

            if (checkWin()) {
                displayBoard();
                String winner = xTurn ? playerX : playerO;
                System.out.println("\n🎉 " + winner + " wins!");
                if (xTurn) scoreX++; else scoreO++;
                return;
            }

            if (isFull()) {
                displayBoard();
                System.out.println("\nIt's a draw!");
                return;
            }

            xTurn = !xTurn;
        }
    }
    //=========================- DISPLAY BOARD -=======================//
    static void displayBoard() {
        System.out.println("\nCurrent Board:");
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("|");
            for (int j = 0; j < 3; j++) {
                System.out.print(" " + board[i][j] + " |");
            }
            System.out.println("\n-------------");
        }
    }

    //=============================- PLAYER MOVE -===========================//
    static void playerMove() {
        String currentPlayer = xTurn ? playerX : playerO;
        char mark = xTurn ? 'X' : 'O';
        while (true) {
            System.out.print(currentPlayer + "'s turn (0-8 or U to undo): ");
            String inputStr = input.nextLine();

            if (inputStr.equalsIgnoreCase("U") && !moveHistory.isEmpty()) {
                int lastMove = moveHistory.pop();
                board[lastMove / 3][lastMove % 3] = (char)(lastMove + '0');
                xTurn = !xTurn;
                System.out.println("Move undone!");
                displayBoard();
                continue;
            }

            try {
                int pos = Integer.parseInt(inputStr);
                if (pos < 0 || pos > 8) {
                    System.out.println("⛔ Please choose a number between 0 and 8.");
                    continue;
                }

                if (board[pos / 3][pos % 3] == 'X' || board[pos / 3][pos % 3] == 'O') {
                    System.out.println("🚫 That spot's already taken! Try again.");
                    continue;
                }
                board[pos / 3][pos % 3] = mark;
                moveHistory.push(pos);

                saveLastMove(pos);
                saveBoardToFile();
                break;
            } catch (Exception e) {
                System.out.println("⚠️ Invalid input. Enter a number (0–8) or U to undo.");
            }
        }
    }

//=============================- AI MOVE -===============================//

    static void aiMove() {
        System.out.println("AI is thinking...");
        int bestScore = Integer.MIN_VALUE;
        int move = -1;
        for (int i = 0; i < 9; i++) {
            int row = i / 3, col = i % 3;
            if (board[row][col] != 'X' && board[row][col] != 'O') {
                char original = board[row][col];
                board[row][col] = 'O';
                int score = minimax(false);
                board[row][col] = original;
                if (score > bestScore) {
                    bestScore = score;
                    move = i;
                }
            }
        }
        board[move / 3][move % 3] = 'O';
        moveHistory.push(move);

        saveLastMove(move);
        saveBoardToFile();
    }

    //===============================- MINIMAX -=================================//
    static int minimax(boolean isMaximizing) {
        if (checkWinFor('O')) return 1;
        if (checkWinFor('X')) return -1;
        if (isFull()) return 0;

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < 9; i++) {
            int r = i / 3, c = i % 3;
            if (board[r][c] != 'X' && board[r][c] != 'O') {
                char original = board[r][c];
                board[r][c] = isMaximizing ? 'O' : 'X';
                int score = minimax(!isMaximizing);
                board[r][c] = original;
                bestScore = isMaximizing ? Math.max(score, bestScore) : Math.min(score, bestScore);
            }
        }
        return bestScore;
    }

    //================================- CHECKS -==================================//
    static boolean isFull() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] != 'X' && board[i][j] != 'O')
                    return false;
        return true;
    }

    static boolean checkWin() {
        return checkWinFor('X') || checkWinFor('O');
    }

    static boolean checkWinFor(char mark) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == mark && board[i][1] == mark && board[i][2] == mark) return true;
            if (board[0][i] == mark && board[1][i] == mark && board[2][i] == mark) return true;
        }
        if (board[0][0] == mark && board[1][1] == mark && board[2][2] == mark) return true;
        if (board[0][2] == mark && board[1][1] == mark && board[2][0] == mark) return true;
        return false;
    }

    //============================- PRINT SCORE FILE -==========================//
    static void printScore(){
        try {
            Scanner input = new Scanner(new File("Score.txt"));
            while (input.hasNextLine()) {
                String line = input.nextLine();
                System.out.println(line);
            }
        }
        catch (FileNotFoundException ex){

        }
    }


    //======================- SAVE LAST MOVE -===========================//
    static void saveLastMove(int pos){
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("LastMove.txt"));
            pw.println("Last Move:" + pos);
            pw.close();
        }
        catch (Exception e) {
        }
    }


    //======================- READ LAST MOVE -=====================//
    static void readLastMove() {
        try{
            Scanner input= new Scanner(new File("LastMove.txt"));
            while(input.hasNextLine()) {
                System.out.println(input.nextLine());
            }
        }
        catch(Exception e) {
        }
    }
    //==========================- SAVE BOARD -=============================//

    static void saveBoardToFile() {
        try {
            PrintWriter Writer = new PrintWriter(new FileWriter("Board.txt"));
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++)
                    Writer.println(board[i][j]+"  ");
            }
            Writer.close();
        }
        catch(Exception e) {
        }
    }
    //==========================- READ LAST SAVED BOARD -=============================//
    static void readLastSavedBoard(){
        System.out.println("\n======= Saved Board =======");
        try {
            Scanner input = new Scanner(new File("Board.txt"));
            while (input.hasNextLine()) {
                System.out.println(input.nextLine());
            }
        }
        catch(Exception e) {
        }

    }

    //============================- DELETE SAVE FILES -=================================//
    static void deleteSavedFiles(){
        File f1= new File("Score.txt");
        File f2= new File("LastMove.txt");
        File f3= new File("Board.txt");

        if(f1.exists())
            f1.delete();
        if(f2.exists())
            f2.delete();
        if(f3.exists())
            f3.delete();
        System.out.println("All Saved Files Deleted Successfully!!!!!");
    }
}





