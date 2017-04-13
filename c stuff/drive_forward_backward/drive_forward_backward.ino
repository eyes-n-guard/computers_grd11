#define DRIVEPORT PORTD
#define DRIVEPORT_DIR DDRD
#define LEFT_F 2
#define LEFT_B 3
#define RIGHT_F 4
#define RIGHT_B 5
#define ENABLE_L 6
#define ENABLE_R 7


void setup()
{
  asm volatile
  (
    //"sbi %[portdir], %[leftF] \n" //12 bytes
    //"sbi %[portdir], %[leftB] \n"
    //"sbi %[portdir], %[rightF] \n"
    //"sbi %[portdir], %[rightB] \n"
    //"sbi %[portdir], %[enableL] \n"
    //"sbi %[portdir], %[enableR] \n"
    
    "ldi r19, 0xff \n"
    "out %[portdir], r19 \n" //4 bytes --clearly superior--
    
    "ldi r19, 0xb4 \n"
    "ldi r20, 0x6c \n"
    
    "0:"
      "out %[port], r19 \n"
      "eor r19, r20 \n"
      
      "clr r16 \n"
      "clr r17 \n"
      "ldi r18, 81 \n"
      
      "1:"
        "dec r16 \n"
        "brne 1b \n"
        "dec r17 \n"
        "brne 1b \n"
        "dec r18 \n"
        "brne 1b \n"
        
      "rjmp 0b \n"
      
    :
    :[portdir] "I" (_SFR_IO_ADDR(DRIVEPORT_DIR)),
     [port] "I" (_SFR_IO_ADDR(DRIVEPORT)),
     [leftF] "I" (LEFT_F),
     [leftB] "I" (LEFT_B),
     [rightF] "I" (RIGHT_F),
     [rightB] "I" (RIGHT_B),
     [enableL] "I" (ENABLE_L),
     [enableR] "I" (ENABLE_R)
  );

}

void loop() {}
