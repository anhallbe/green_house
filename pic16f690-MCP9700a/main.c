/*
         ___________  ___________           
        |           \/           |        
  +5V---|Vdd     16F690       Vss|---GND  
LED  ---|RA5        RA0/AN0/(PGD)|-<-
     ---|RA4            RA1/(PGC)|---      
	 ---|RA3/!MCLR/(Vpp)  RA2/INT|---     
     ---|RC5/CCP              RC0|->-    
     ---|RC4                  RC1|->-   
	 -<-|RC3                  RC2|->-TEMP 
        |RC6                  RB4|       
        |RC7               RB5/Rx|       
        |RB7/Tx               RB6|        
        |________________________|     
                                          
*/
 
#include "16F690.h"
#pragma config |= 0x00D4 
 
#pragma bit	DATA  @ PORTC.5//p4
#pragma bit LED  @ PORTA.5// led
#pragma bit RX  @ PORTB.5// raspberry
#pragma bit TX  @ PORTB.7// raspberry
#pragma bit AN6  @ PORTC.2// tempsensor
#define MAX_STRING 10
 
bit compare( char * input_string, const char * candidate_string );

void delay( char );
void blink(char t);

void initserial( void );
void FlushRecieverBuffer(void);
void putchar( char );
char getchar( void );
void delay10( char );
void printf(const char *string, uns8 variable); 

void string_in( char * );
void bitbangstring_in(char *);
void bitbanginitserial( void );
void bitbangputchar( char );
char bitbanggetchar( void );
void bitbangprintf(const char *string, uns8 variable); 
unsigned long get_temp(void);
void init(void);

void main( void){
	init();
	unsigned long temp;
	char input[10];
	char a = 0;
	while(1){
		//delay10(100);
		//blink(15);
		FlushRecieverBuffer(); 
		string_in(&input[0]);
		//bitbangstring_in(&input[0]);
		if (compare(&input[0], "TEMP")){
			temp = get_temp();
			//bitbangprintf("VALUE%u\n", temp);
			printf("VALUE%u\n", temp);
		}
		else if(compare(&input[0], "CHECK")){
			delay10(100);
			bitbangprintf("ONLINE%c\n", a);
			printf("ONLINE%c\n", a);
		}
		else{
			delay10(100);
			bitbangprintf("NOMESS%c\n", a);
			printf("NOMESS%c\n", a);
		}
	}
}

unsigned long get_temp(void){
	unsigned long val;
	GO=1;          /* start AD                               */
	while(GO);     /* wait for done                          */
	//val = ADRESH;
	//val = ((unsigned)ADRESH << 8) | ADRESL;
	val = 256 * ADRESH; /* only using 8 MSB of ADRES (=ADRESH)    */
	val += ADRESL;
	//val = (val - 500) / 10;
	//val = val/10;
	//val = val-50;
	return val;
}

void init(void){
	TRISA = 0;
	TRISB = 0;
	TRISC = 0;
	
	TRISB.5 = 1; // rx
	TRISB.7 = 0; // tx
	TRISC.2 = 1; // data
	TRISA.5 = 0; // real led


	ANSEL.6 = 1;
	ADCON1 = 0b0.001.0000; /* AD conversion clock 'fosc/8' */
	/* 
	 0.x.xxxx.x.x  ADRESH:ADRESL is 10 bit left justified
	 x.0.xxxx.x.x  Vref is Vdd
	 x.x.0000.x.x  Channel 00 (AN0)
	 x.x.xxxx.0.x  Go/!Done start later
	 x.x.xxxx.x.1  Enable AD-converter
	*/
	ADCON0 = 0b1.0.0110.0.1; //channel 6 0110
	
	delay10(100);
	bitbanginitserial();
	delay10(100);
	initserial();
	delay10(100);
}

bit compare( char * input_string, const char * candidate_string ){
   /* compares input with the candidate string */
   char i, c, d;
   for(i=0; ; i++){
       c = input_string[i];
       d = candidate_string[i];
       if(d != c ) return 0;       /* no match    */
         if( d == '\0' ) return 1; /* exact match */
     }
}

void blink(char t){
	LED=0;
	delay10(t);
	LED=1;
	delay10(t);
}

void delay( char millisec){
    OPTION = 2;  /* prescaler divide by 8        */
    do  {
        TMR0 = 0;
        while ( TMR0 < 125)   /* 125 * 8 = 1000  */
            ;
    } while ( -- millisec > 0);
}

void initserial( void ){
   /* One start bit, one stop bit, 8 data bit, no parity. 4800 Baud. */

   TXEN = 1;      /* transmit enable                   */
   SYNC = 0;      /* asynchronous operation            */
   TX9  = 0;      /* 8 bit transmission                */
   SPEN = 1;

   BRGH  = 0;     /* settings for 4800 Baud            */
   BRG16 = 1;     /* @ 4 MHz-clock frequency           */
   SPBRG = 51;

   CREN = 1;      /* Continuous receive                */
   RX9  = 0;      /* 8 bit reception                   */
   ANSELH.3 = 0;  /* RB5 not AD-input but serial_in    */
}

void FlushRecieverBuffer(void){  
   char trash;
   trash = RCREG;  /* the two char's that locked the reciever  */
   trash = RCREG;  /* are read and ignored                     */
   CREN = 0;       /* the unlock procedure ...                 */
   CREN = 1;
}

char getchar( void )  /* recieves one char */{
   char d_in;
   while ( RCIF == 0 ) ;  /* wait for char */
   d_in = RCREG;
   return d_in;
}

void putchar( char d_out )  /* sends one char */{
   while (!TXIF) ;   /* wait until previus character transmitted */
   TXREG = d_out;
}

void delay10( char n){
    char i;
    OPTION = 7;
    do  {
        i = TMR0 + 39; /* 256 microsec * 39 = 10 ms */
        while ( i != TMR0)
            ;
    } while ( --n > 0);
}

// void string_in(char * string ){
	// /*tar in en del av strängen från gpsen, begränsad för att minnet inte räcker till*/
	// char charCount, c;
	// for( charCount = 0; ; charCount++ ){ 
		// c = getchar();        /* input 1 character         */
		// if(charCount==(50)||c=='\r'){
			// string[charCount] = '\0'; /* add "end of string" */
			// return; 
           // }
		// else{
			// string[charCount] = c; /* store the character       */
			//putchar(c);       /* don't echo the character  */
			//lcd_putchar(c);
         // }
	// }
// }

void string_in( char * string ) 
{
   char charCount, c;
   for( charCount = 0; ; charCount++ )
       {
         c = getchar( );     /* input 1 character             */
         string[charCount] = c;   /* store the character           */
         if( (charCount == (MAX_STRING-1))||(c=='\r' )) /* end of input   */
           {
             string[charCount] = '\0'; /* add "end of string"      */
             return;
           }
       }
}

void bitbangstring_in( char * string ) 
{
   char charCount, c;
   for( charCount = 0; ; charCount++ )
       {
         c = bitbanggetchar( );     /* input 1 character             */
         string[charCount] = c;   /* store the character           */
         if( (charCount == (MAX_STRING-1))||(c=='\r' )) /* end of input   */
           {
             string[charCount] = '\0'; /* add "end of string"      */
             return;
           }
       }
}

void bitbanginitserial( void )  /* initialise PIC16F690 serialcom port */
{
   ANSEL.0 = 0; /* No AD on RA0             */
   ANSEL.1 = 0; /* No AD on RA1             */
   PORTA.0 = 1; /* marking line             */
   TRISA.0 = 0; /* output to PK2 UART-tool  */
   TRISA.1 = 1; /* input from PK2 UART-tool */
   return;      
}

void bitbangputchar( char ch )  /* sends one char */
{
  char bitCount, ti;
  PORTA.0 = 0; /* set startbit */
  for ( bitCount = 10; bitCount > 0 ; bitCount-- )
   {
     /* delay one bit 104 usec at 4 MHz       */
     /* 5+18*5-1+1+9=104 without optimization */ 
     ti = 18; do ; while( --ti > 0); nop(); 
     Carry = 1;     /* stopbit                    */
     ch = rr( ch ); /* Rotate Right through Carry */
     PORTA.0 = Carry;
   }
  return;
}

char bitbanggetchar( void )  /* recieves one char, blocking */
{
   /* One start bit, one stop bit, 8 data bit, no parity = 10 bit. */
   /* Baudrate: 9600 baud => 104.167 usec. per bit.                */
   char d_in, bitCount, ti;
   while( PORTA.1 == 1 ) /* wait for startbit */ ;
      /* delay 1,5 bit 156 usec at 4 MHz         */
      /* 5+28*5-1+1+2+9=156 without optimization */
      ti = 28; do ; while( --ti > 0); nop(); nop2();
   for( bitCount = 8; bitCount > 0 ; bitCount--)
       {
        Carry = PORTA.1;
        d_in = rr( d_in);  /* rotate carry */
         /* delay one bit 104 usec at 4 MHz       */
         /* 5+18*5-1+1+9=104 without optimization */ 
         ti = 18; do ; while( --ti > 0); nop(); 
        }
   return d_in;
}

void bitbangprintf(const char *string, uns8 variable){
/* Bitbanging för felsökning*/
  char i, k, m, a, b;
  for(i = 0 ; ; i++)
   {
     k = string[i];
     if( k == '\0') break;   // at end of string
     if( k == '%')           // insert variable in string
      { 
        i++;
        k = string[i];
        switch(k)
         {
           case 'd':         // %d  signed 8bit
             if( variable.7 ==1) putchar('-');
             else bitbangputchar(' ');
             if( variable > 128) variable = -variable;  // no break!
           case 'u':         // %u unsigned 8bit
             a = variable/100;
             bitbangputchar('0'+a); // print 100's
             b = variable%100; 
             a = b/10;
             bitbangputchar('0'+a); // print 10's
             a = b%10;         
             bitbangputchar('0'+a); // print 1's 
             break;
           case 'b':         // %b BINARY 8bit
             for( m = 0 ; m < 8 ; m++ )
              {
                if (variable.7 == 1) bitbangputchar('1');
                else bitbangputchar('0');
                variable = rl(variable);
               }
              break;
           case 'c':         // %c  'char'
             bitbangputchar(variable); 
             break;
           case '%':
             bitbangputchar('%');
             break;
           default:          // not implemented 
             bitbangputchar('!');   
         }   
      }
      else bitbangputchar(k); 
   }
}

void printf(const char *string, uns8 variable)
{
  char i, k, m, a, b;
  for(i = 0 ; ; i++)
   {
     k = string[i];
     if( k == '\0') break;   // at end of string
     if( k == '%')           // insert variable in string
      { 
        i++;
        k = string[i];
        switch(k)
         {
           case 'd':         // %d  signed 8bit
             if( variable.7 ==1) putchar('-');
             else putchar(' ');
             if( variable > 128) variable = -variable;  // no break!
           case 'u':         // %u unsigned 8bit
             a = variable/100;
             putchar('0'+a); // print 100's
             b = variable%100; 
             a = b/10;
             putchar('0'+a); // print 10's
             a = b%10;         
             putchar('0'+a); // print 1's 
             break;
           case 'b':         // %b BINARY 8bit
             for( m = 0 ; m < 8 ; m++ )
              {
                if (variable.7 == 1) putchar('1');
                else putchar('0');
                variable = rl(variable);
               }
              break;
           case 'c':         // %c  'char'
             putchar(variable); 
             break;
           case '%':
             putchar('%');
             break;
           default:          // not implemented 
             putchar('!');   
         }   
      }
      else putchar(k); 
   }
}


