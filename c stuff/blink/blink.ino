#define LEDPORT PORTD
#define LEDPORT_DIR DDRD
#define LEDPORT_DIR2 DDRD
#define LEDBIT 5
#define LEDBIT2 4

void setup()
{
  asm volatile
  (
    "sbi %[portdir], %[lbit] \n"
    "sbi %[portdir2], %[lbit2] \n"
    "ldi r19, 0x20 \n"
    "0:"
      "clr r16 \n" // 2 * 16000000 / 3 * 256
      "clr r17 \n"
      "ldi r18, 162 \n"
      
      "1:"
        "dec r16 \n"
        "brne 1b \n"
        "dec r17 \n"
        "brne 1b \n"
        "dec r18 \n"
        "brne 1b \n"
      
      "com r19 \n"
      "out %[port], r19 \n"
      "rjmp 0b \n"
      :
      : [portdir] "I" (_SFR_IO_ADDR(LEDPORT_DIR)),
        [portdir2] "I" (_SFR_IO_ADDR(LEDPORT_DIR2)),
        [port] "I" (_SFR_IO_ADDR(LEDPORT)),
        [lbit2] "I" (LEDBIT2),
        [lbit] "I" (LEDBIT)
  );
}

void loop(){}
