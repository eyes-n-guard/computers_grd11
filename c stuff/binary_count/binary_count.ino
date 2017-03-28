#define LEDPORT PORTD
#define LEDBIT_1 2
#define LEDBIT_2 3
#define LEDBIT_3 4
#define LEDBIT_4 5
#define LEDPORT_DIR DDRD

void setup()
{
  asm volatile
  (
    "sbi %[portdir], %[lbit1] \n"
    "sbi %[portdir], %[lbit2] \n"
    "sbi %[portdir], %[lbit3] \n"
    "sbi %[portdir], %[lbit4] \n"
    
    "clr r19 \n"
    
    "0:"
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
       
       "inc r19 \n"
       "out %[port], r19 \n"
       "rjmp 0b \n"
    
    :
    : [portdir] "I" (_SFR_IO_ADDR(LEDPORT_DIR)),
      [port] "I" (_SFR_IO_ADDR(LEDPORT)),
      [lbit1] "I" (LEDBIT_1),
      [lbit2] "I" (LEDBIT_2),
      [lbit3] "I" (LEDBIT_3),
      [lbit4] "I" (LEDBIT_4)
  );
}

void loop(){}
