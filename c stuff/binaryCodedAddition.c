#include <stdio.h>

int main()
{
    //int aIn[] = {0,0,0,0,1,2,3,4};
    //int bIn[] = {0,0,0,0,5,6,7,8};

    char aIn[8+1];
    char bIn[8+1];

    printf("enter two positive 8 digit integers:\n");
    gets(aIn);
    gets(bIn);

    unsigned long a = 0x00000000;
    unsigned long b = 0x00000000;

    int i;
    for(i=0;i < 8;i++)
        a = (a << 4) | (*(aIn+i) - 48);

    for(i=0;i < 8;i++)
        b = (b << 4) | (*(bIn+i) - 48);

    //long a = 0x00001234;
    //long b = 0x00005678;


    unsigned long t1 = 0x06666666 + a;
    unsigned long t2 = b + t1;
    unsigned long t3 = t1 ^ b;
    unsigned long t4 = t2 ^ t3;
    unsigned long t5 = ~t4 & 0x11111110;
    unsigned long t6 = (t5 >> 2) | (t5 >> 3);
    unsigned long ans = t2 - t6;

    printf("a + b = %x",ans);
    return 0;
}
