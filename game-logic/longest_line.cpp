#include <iostream>

class determine_length{

  public:

    int board[8][8]{
      {0, 20, 0, 20, 0, 20, 0, 20},  //0 is an empty spot
      {20, 0, 20, 0, 20, 0, 20, 0},  //10 is a computer piece
      {0, 20, 0, 20, 0, 20, 0, 20},  //11 is a computer crowned piece
      {0,  0, 0,  0, 0,  0, 0,  0},  //20 is a player piece
      {0,  0, 0,  0, 0,  0, 0,  0},  //21 is a player crowned piece
      {10, 0, 10, 0, 10, 0, 10, 0},
      {0, 10, 0, 10, 0, 10, 0, 10},
      {10, 0, 10, 0, 10, 0, 10, 0}
    };

  int longest_line(int row, int column){
    board[row][column];

    if ((row >= 0 && row <= 7) && (column >= 0 && column <= 7)) {

      if (board[row][column] == 10){
        int line_count_1R = 0;
        int line_count_1L = 0;
        int line_count_2R = 0;
        int line_count_2L = 0;
        int line_count_1 = 0;
        int line_count_2 = 0;

        //down-right
        for (int i = 0; i < 9; i++){
          if (row + i >= 0 && row + i <= 7 && column + i >= 0 && column + i <= 7 && board[row + i][column + i] == 10 || board[row + i][column + i] == 11){
            line_count_1R++;
          } else {
            break;
          }
        }

        //up-left
        for (int i = 0; i < 9; i++){

          if (row - i >= 0 && row - i <= 7 && column - i >= 0 && column - i <= 7 && board[row - i][column - i] == 10 || board[row + i][column + i] == 11){
            line_count_1L++;
          } else {
            if (line_count_1R >= 1){
              line_count_1L--;
            }
            break;
          }
        }

        //down-left
        for (int i = 0; i < 9; i++){

          if (row + i >= 0 && row + i <= 7 && column - i >= 0 && column - i <= 7 && board[row + i][column - i] == 10 || board[row + i][column + i] == 11){
            line_count_2L++;
          } else {
            break;
          }
        }

        //up-right
        for (int i = 0; i < 9; i++){

          if (row - i >= 0 && row - i <= 7 && column + i >= 0 && column + i <= 7 && board[row - i][column + i] == 10 || board[row + i][column + i] == 11){
            line_count_2R++;
          } else{
            if (line_count_2L >= 1){
              line_count_2R--;
            }
            break;
          }
        }

        line_count_1 = line_count_1L + line_count_1R;
        line_count_2 = line_count_2L + line_count_2R;

        if (line_count_1 > line_count_2){
          return line_count_1;
        } else if (line_count_2 > line_count_1){
          return line_count_2;
        } else{
          return line_count_1;
        }
      } else {
        return 0;
      }
    } else {
      return 0;
    }
  }

};
