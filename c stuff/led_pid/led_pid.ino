#define LEDPORT PORTD
#define LEDBIT_1 4
#define LEDBIT_2 5
#define LEDPORT_DIR DDRD
#define TIMER_REG TCCR0A
#define PWM1 OCR0A
#define PWM2 OCR0B
#define COMPARE_OUTPUT_MODE1 COM0A1
#define COMPARE_OUTPUT_MODE2 COM0B1
#define PWMTYPE1 WGM00
#define PWMTYPE2 WGM01

void setup()
{
  asm volatile
  (
    "sbi %[portdir], %[lbit1] \n"
    "sbi %[portdir], %[lbit2] \n"
    
    :
    : [portdir] "I" (_SFR_IO_ADDR(LEDPORT_DIR)),
      [port] "I" (_SFR_IO_ADDR(LEDPORT)),
      [lbit1] "I" (LEDBIT_1),
      [lbit2] "I" (LEDBIT_2),
      [leds] "M" ((1 << COMPARE_OUTPUT_MODE1) | (1 << COMPARE_OUTPUT_MODE2) | (1 << PWMTYPE1) | (1 << PWMTYPE2)),
      "Z" (_SFR_MEM_ADDR(TIMER_REG))
  );
}

void loop(){}
