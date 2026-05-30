🎮 Ultimate Tic Tac Toe - Game Engine with AI
A feature-rich Tic-Tac-Toe game built in Java with intelligent AI opponent, game state persistence, and advanced game management capabilities. Perfect for learning object-oriented programming, game theory (minimax algorithm), and file I/O operations.

✨ Features
Core Gameplay

✅ 2-Player Mode - Play against another human player
✅ AI Opponent - Unbeatable AI using Minimax algorithm
✅ Undo Moves - Press 'U' to undo your last move
✅ Score Tracking - Persistent score history across multiple games
✅ Win Detection - Automatic detection of wins, losses, and draws

*Advanced Features*

🔄 Game State Persistence - Save and load game boards
📝 Move History - Track and display last move played
💾 File Management - Save scores, moves, and board states
🎯 Customizable Players - Enter custom player names
🤖 Configurable First Player - Choose who starts first

Technical Features

🧠 Minimax Algorithm - Optimal AI decision making
🗂️ File I/O - Persistent storage using text files
📊 Score Management - Automatic score calculation and tracking
🔧 Clean Architecture - Well-organized static methods for maintainability

Game Rules:

Players take turns placing X or O on the board
Win by getting 3 marks in a row (horizontal, vertical, or diagonal)
If all 9 squares are filled with no winner, it's a draw
Undo Feature: Type 'U' instead of a position to undo your last move


🧠 Technical Implementation
Key Algorithms
Minimax Algorithm 🤖
The AI uses the Minimax algorithm to make optimal moves:

Evaluates all possible future game states
Maximizes AI's winning chances
Minimizes player's winning chances
Scores: Win = +1, Loss = -1, Draw = 0

Why Minimax?

Ensures AI never loses
Makes optimal strategic moves
Perfect for small game trees (9 positions)

Win Detection 🎯
Checks three types of winning conditions:

Rows - Three marks in same row
Columns - Three marks in same column
Diagonals - Main diagonal or anti-diagonal


This project demonstrates:

✅ OOP Concepts - Class structure, static methods, encapsulation
✅ Game Theory - Minimax algorithm, optimal game strategy
✅ File I/O - Reading/writing persistent data
✅ Algorithm Design - Recursive game tree evaluation
✅ User Interface - Menu systems, input validation
✅ Data Structures - Arrays, Stacks, file handling
✅ Exception Handling - Try-catch for robust error handling
