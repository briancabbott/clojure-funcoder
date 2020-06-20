// Read sorting control data-structure:
#include <stdio.h>
#include <stdlib.h>

void print_file_read_error_msg(char* filepath)
{
    printf("file read error message...\n");
}

long int get_file_size_from_fptr(FILE* fptr)
{
    long int fsize = -1;

    printf("entering get_file_size_from_fptr()\n");

    fseek(fptr, 0, SEEK_END);
    fsize = ftell(fptr);
    printf("\t filesize is: %li\n", fsize);

    rewind(fptr);
    printf("exiting\n");
    return fsize;
}

unsigned long get_file_size_from_path(char* filepath)
{
    FILE* fd;
    unsigned long fsize = -1;

    printf("entering get_file_size_from_path()\n");
    if ((fd = fopen(filepath, "r")) == NULL)
    {
        // Error reading file:
        fputs("Error occured reading file specified at location: ", stderr);
        exit(-1);
    }

    fsize = get_file_size_from_fptr(fd);
    fclose(filepath);

    printf("exiting\n");

    return fsize;
}

int** read()
{
    FILE* fd;
    char* filepath = "../clojure-datastructures-and-algorithms-sorting-control-data/data-list-01.dat";
    char err_buff[1024];
    long file_size;
    unsinged char* buf;

    printf("entering read()\n");

    if ((fd = fopen(filepath, "r")) == NULL)
    {
        printf("\tproblem reading file: \n");
        // print_file_read_error_msg(filepath);
        exit(-1);
    }

    file_size = get_file_size_from_fptr(fd);
    printf("\tFilesize: %li\n", file_size);


    buf = malloc(size_of file_size);
    

    printf("exiting\n");
    return NULL;
}


int main(int argc, char** argv) {
    read();
    return 0;
}
