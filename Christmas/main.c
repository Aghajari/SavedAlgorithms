#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

#ifdef _WIN32
#include <windows.h>
#else
#include <unistd.h>
#endif

//#define BLACK 0
#define RED 1
#define GREEN 2
#define YELLOW 3
//#define BLUE 4
//#define PURPLE 5
//#define CYAN 6
#define WHITE 7

#ifdef _WIN32
#define SNOW "."
#else
#define SNOW "•"
#endif

#define printMyChar(c) printf("\033[%s3%dm%s\033[0m", c.snow == 1 ? "1;" : "", c.color, c.value)

typedef struct {
    char *value;
    int color, snow;
} MyChar;

void drawText(int i, int j, char *text, int color, int len, MyChar array[][len]) {
    char *t = text;
    for (int k = 0; k < strlen(t); k++) {
        char *value = malloc(2);
        value[0] = t[k];
        value[1] = '\0';
        array[i][j + k] = (MyChar) {value, color};
    }
}

int rnd(int bound) {
    return (int) (rand() % bound); // NOLINT(cert-msc50-cpp)
}

void thread_sleep(int ms) {
#ifdef _WIN32
    Sleep(ms);
#else
    usleep(ms * 1000);
#endif
}

void clearConsole() {
#ifdef _WIN32
    system("cls");
#else
    system("clear");
#endif
}

int main() {
    srand(time(0)); // NOLINT(cert-msc51-cpp)

    int top = 3, left = 20, tree_height = 10;
    int width = tree_height * 2 + left * 2, height = 20;
    int round = 1;

    MyChar array[height][width];
    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            array[i][j] = (MyChar) {" ", 0, 0};
        }
    }

    drawText(top + tree_height, left + tree_height - 1, "mWm", YELLOW, width, array);
    drawText(top + tree_height + 1, left + tree_height - 1, "mWm", YELLOW, width, array);
    drawText(top + tree_height + 2, left + tree_height - 7, "MERRY CHRISTMAS", RED, width, array);

    MyChar tree = (MyChar) {"*", GREEN};

#pragma clang diagnostic push
#pragma ide diagnostic ignored "EndlessLoop"
    while (1) {
        round = !round;

        for (int j = round ? 1 : 0; j < width; j += 2) {
            if (rnd(8) == 2) {
                array[0][j] = (MyChar) {SNOW, WHITE, 1};
            }
        }

        for (int i = 0; i < tree_height; ++i) {
            for (int j = 1; j < tree_height * 2; ++j) {
                if (j > tree_height - i - 1 && j < tree_height + i + 1) {
                    if (j != tree_height - i && j != tree_height + i && rnd(4) == 2) {
                        array[top + i][left + j] = (MyChar) {"o", rnd(7) + 1};
                    } else {
                        array[top + i][left + j] = tree;
                    }
                }
            }
        }

        array[top + tree_height + 3][left + tree_height - 3] = (MyChar) {"2", rnd(7) + 1};
        array[top + tree_height + 3][left + tree_height - 1] = (MyChar) {"0", rnd(7) + 1};
        array[top + tree_height + 3][left + tree_height + 1] = (MyChar) {"2", rnd(7) + 1};
        array[top + tree_height + 3][left + tree_height + 3] = (MyChar) {"2", rnd(7) + 1};

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (array[i][j].snow == 3) {
                    array[i][j].snow = 0;
                    if (strcmp(array[i + 1][j].value, " ") == 0) {
                        array[i + 1][j].snow = 1;
                        array[i + 1][j].value = SNOW;
                        array[i + 1][j].color = WHITE;
                    } else {
                        array[i + 1][j].snow = 3;
                    }
                }
                if (array[i][j].snow == 2) {
                    if (strcmp(array[i + 1][j].value, " ") == 0) {
                        MyChar tmp = array[i][j];
                        tmp.snow = 1;
                        array[i][j] = array[i + 1][j];
                        array[i + 1][j] = tmp;
                    } else {
                        array[i][j].snow = 0;
                        array[i][j].value = " ";
                        array[i + 1][j].snow = 3;
                    }
                }
                if (array[i][j].snow == 1) {
                    if (i == height - 1 /*|| (i >= top - 1 && j >= left - 4 && j < left + tree_height * 2 + 4)*/) {
                        array[i][j].snow = 0;
                        array[i][j].value = " ";
                    } else {
                        array[i][j].snow = 2;
                    }
                }
                printMyChar(array[i][j]);
            }
            printf("\n");
        }

        thread_sleep(200);
        //return 0;
        clearConsole();
    }
#pragma clang diagnostic pop
}

