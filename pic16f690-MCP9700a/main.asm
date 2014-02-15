
; CC5X Version 3.4H, Copyright (c) B Knudsen Data
; C compiler for the PICmicro family
; ************  22. Apr 2013  18:50  *************

	processor  16F690
	radix  DEC

	__config 0xD4

INDF        EQU   0x00
TMR0        EQU   0x01
PCL         EQU   0x02
FSR         EQU   0x04
PORTA       EQU   0x05
TRISA       EQU   0x85
TRISB       EQU   0x86
PCLATH      EQU   0x0A
Carry       EQU   0
Zero_       EQU   2
RP0         EQU   5
RP1         EQU   6
IRP         EQU   7
OPTION_REG  EQU   0x81
TXREG       EQU   0x19
RCREG       EQU   0x1A
ADRESH      EQU   0x1E
ADCON0      EQU   0x1F
TRISC       EQU   0x87
SPBRG       EQU   0x99
ADRESL      EQU   0x9E
ADCON1      EQU   0x9F
ANSEL       EQU   0x11E
ANSELH      EQU   0x11F
TXIF        EQU   4
RCIF        EQU   5
CREN        EQU   4
RX9         EQU   6
SPEN        EQU   7
GO          EQU   1
BRGH        EQU   2
SYNC        EQU   4
TXEN        EQU   5
TX9         EQU   6
BRG16       EQU   3
LED         EQU   5
temp        EQU   0x20
a           EQU   0x2C
val         EQU   0x2D
input_string EQU   0x2D
candidate_string EQU   0x2E
i           EQU   0x2F
c           EQU   0x30
d           EQU   0x31
t           EQU   0x7F
millisec    EQU   0x7F
trash       EQU   0x2D
d_in        EQU   0x30
d_out       EQU   0x34
n           EQU   0x2D
i_2         EQU   0x2E
string      EQU   0x2D
charCount   EQU   0x2E
c_2         EQU   0x2F
string_2    EQU   0x7F
charCount_2 EQU   0x7F
c_3         EQU   0x7F
ch          EQU   0x34
bitCount    EQU   0x35
ti          EQU   0x36
d_in_2      EQU   0x7F
bitCount_2  EQU   0x7F
ti_2        EQU   0x7F
string_3    EQU   0x2D
variable    EQU   0x2E
i_3         EQU   0x2F
k           EQU   0x30
m           EQU   0x31
a_2         EQU   0x32
b           EQU   0x33
C2cnt       EQU   0x34
C3tmp       EQU   0x35
C4rem       EQU   0x36
C5cnt       EQU   0x34
C6tmp       EQU   0x35
C7cnt       EQU   0x34
C8tmp       EQU   0x35
C9rem       EQU   0x36
C10cnt      EQU   0x34
C11tmp      EQU   0x35
string_4    EQU   0x2D
variable_2  EQU   0x2E
i_4         EQU   0x2F
k_2         EQU   0x30
m_2         EQU   0x31
a_3         EQU   0x32
b_2         EQU   0x33
C12cnt      EQU   0x34
C13tmp      EQU   0x35
C14rem      EQU   0x36
C15cnt      EQU   0x34
C16tmp      EQU   0x35
C17cnt      EQU   0x34
C18tmp      EQU   0x35
C19rem      EQU   0x36
C20cnt      EQU   0x34
C21tmp      EQU   0x35
ci          EQU   0x34

	GOTO main

  ; FILE main.c
			;/*
			;         ___________  ___________           
			;        |           \/           |        
			;  +5V---|Vdd     16F690       Vss|---GND  
			;LED  ---|RA5        RA0/AN0/(PGD)|-<-
			;     ---|RA4            RA1/(PGC)|---      
			;	 ---|RA3/!MCLR/(Vpp)  RA2/INT|---     
			;     ---|RC5/CCP              RC0|->-    
			;     ---|RC4                  RC1|->-   
			;	 -<-|RC3                  RC2|->-TEMP 
			;        |RC6                  RB4|       
			;        |RC7               RB5/Rx|       
			;        |RB7/Tx               RB6|        
			;        |________________________|     
			;                                          
			;*/
			; 
			;#include "16F690.h"
			;#pragma config |= 0x00D4 
			; 
			;#pragma bit	DATA  @ PORTC.5//p4
			;#pragma bit LED  @ PORTA.5// led
			;#pragma bit RX  @ PORTB.5// raspberry
			;#pragma bit TX  @ PORTB.7// raspberry
			;#pragma bit AN6  @ PORTC.2// tempsensor
			;#define MAX_STRING 10
			; 
			;bit compare( char * input_string, const char * candidate_string );
			;
			;void delay( char );
			;void blink(char t);
			;
			;void initserial( void );
			;void FlushRecieverBuffer(void);
			;void putchar( char );
			;char getchar( void );
			;void delay10( char );
			;void printf(const char *string, uns8 variable); 
			;
			;void string_in( char * );
			;void bitbangstring_in(char *);
			;void bitbanginitserial( void );
			;void bitbangputchar( char );
			;char bitbanggetchar( void );
			;void bitbangprintf(const char *string, uns8 variable); 
			;unsigned long get_temp(void);
			;void init(void);
			;
			;void main( void){
_const1
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF ci
	MOVLW 40
	SUBWF ci,W
	BTFSC 0x03,Carry
	RETLW 0
	BCF   0x03,RP0
	BCF   0x03,RP1
	CLRF  PCLATH
	MOVF  ci,W
	ADDWF PCL,1
	RETLW 84
	RETLW 69
	RETLW 77
	RETLW 80
	RETLW 0
	RETLW 86
	RETLW 65
	RETLW 76
	RETLW 85
	RETLW 69
	RETLW 37
	RETLW 117
	RETLW 10
	RETLW 0
	RETLW 67
	RETLW 72
	RETLW 69
	RETLW 67
	RETLW 75
	RETLW 0
	RETLW 79
	RETLW 78
	RETLW 76
	RETLW 73
	RETLW 78
	RETLW 69
	RETLW 37
	RETLW 99
	RETLW 10
	RETLW 0
	RETLW 78
	RETLW 79
	RETLW 77
	RETLW 69
	RETLW 83
	RETLW 83
	RETLW 37
	RETLW 99
	RETLW 10
	RETLW 0
main
			;	init();
	CALL  init
			;	unsigned long temp;
			;	char input[10];
			;	char a = 0;
	BCF   0x03,RP0
	BCF   0x03,RP1
	CLRF  a
			;	while(1){
			;		//delay10(100);
			;		//blink(15);
			;		FlushRecieverBuffer(); 
m001	CALL  FlushRecieverBuffer
			;		string_in(&input[0]);
	MOVLW 34
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF string
	CALL  string_in
			;		//bitbangstring_in(&input[0]);
			;		if (compare(&input[0], "TEMP")){
	MOVLW 34
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF input_string
	CLRF  candidate_string
	CALL  compare
	BTFSS 0x03,Carry
	GOTO  m002
			;			temp = get_temp();
	CALL  get_temp
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  val,W
	MOVWF temp
	MOVF  val+1,W
	MOVWF temp+1
			;			//bitbangprintf("VALUE%u\n", temp);
			;			printf("VALUE%u\n", temp);
	MOVLW 5
	MOVWF string_4
	MOVF  temp,W
	CALL  printf
			;		}
			;		else if(compare(&input[0], "CHECK")){
	GOTO  m001
m002	MOVLW 34
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF input_string
	MOVLW 14
	MOVWF candidate_string
	CALL  compare
	BTFSS 0x03,Carry
	GOTO  m003
			;			delay10(100);
	MOVLW 100
	CALL  delay10
			;			bitbangprintf("ONLINE%c\n", a);
	MOVLW 20
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF string_3
	MOVF  a,W
	CALL  bitbangprintf
			;			printf("ONLINE%c\n", a);
	MOVLW 20
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF string_4
	MOVF  a,W
	CALL  printf
			;		}
			;		else{
	GOTO  m001
			;			delay10(100);
m003	MOVLW 100
	CALL  delay10
			;			bitbangprintf("NOMESS%c\n", a);
	MOVLW 30
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF string_3
	MOVF  a,W
	CALL  bitbangprintf
			;			printf("NOMESS%c\n", a);
	MOVLW 30
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF string_4
	MOVF  a,W
	CALL  printf
			;		}
			;	}
	GOTO  m001
			;}
			;
			;unsigned long get_temp(void){
get_temp
			;	unsigned long val;
			;	GO=1;          /* start AD                               */
	BCF   0x03,RP0
	BCF   0x03,RP1
	BSF   0x1F,GO
			;	while(GO);     /* wait for done                          */
m004	BCF   0x03,RP0
	BCF   0x03,RP1
	BTFSC 0x1F,GO
	GOTO  m004
			;	//val = ADRESH;
			;	//val = ((unsigned)ADRESH << 8) | ADRESL;
			;	val = 256 * ADRESH; /* only using 8 MSB of ADRES (=ADRESH)    */
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  ADRESH,W
	MOVWF val+1
	CLRF  val
			;	val += ADRESL;
	BSF   0x03,RP0
	MOVF  ADRESL,W
	BCF   0x03,RP0
	ADDWF val,1
	BTFSC 0x03,Carry
	INCF  val+1,1
			;	//val = (val - 500) / 10;
			;	//val = val/10;
			;	//val = val-50;
			;	return val;
	MOVF  val,W
	RETURN
			;}
			;
			;void init(void){
init
			;	TRISA = 0;
	BSF   0x03,RP0
	BCF   0x03,RP1
	CLRF  TRISA
			;	TRISB = 0;
	CLRF  TRISB
			;	TRISC = 0;
	CLRF  TRISC
			;	
			;	TRISB.5 = 1; // rx
	BSF   TRISB,5
			;	TRISB.7 = 0; // tx
	BCF   TRISB,7
			;	TRISC.2 = 1; // data
	BSF   TRISC,2
			;	TRISA.5 = 0; // real led
	BCF   TRISA,5
			;
			;
			;	ANSEL.6 = 1;
	BCF   0x03,RP0
	BSF   0x03,RP1
	BSF   ANSEL,6
			;	ADCON1 = 0b0.001.0000; /* AD conversion clock 'fosc/8' */
	MOVLW 16
	BSF   0x03,RP0
	BCF   0x03,RP1
	MOVWF ADCON1
			;	/* 
			;	 0.x.xxxx.x.x  ADRESH:ADRESL is 10 bit left justified
			;	 x.0.xxxx.x.x  Vref is Vdd
			;	 x.x.0000.x.x  Channel 00 (AN0)
			;	 x.x.xxxx.0.x  Go/!Done start later
			;	 x.x.xxxx.x.1  Enable AD-converter
			;	*/
			;	ADCON0 = 0b1.0.0110.0.1; //channel 6 0110
	MOVLW 153
	BCF   0x03,RP0
	MOVWF ADCON0
			;	
			;	delay10(100);
	MOVLW 100
	CALL  delay10
			;	bitbanginitserial();
	CALL  bitbanginitserial
			;	delay10(100);
	MOVLW 100
	CALL  delay10
			;	initserial();
	CALL  initserial
			;	delay10(100);
	MOVLW 100
	GOTO  delay10
			;}
			;
			;bit compare( char * input_string, const char * candidate_string ){
compare
			;   /* compares input with the candidate string */
			;   char i, c, d;
			;   for(i=0; ; i++){
	BCF   0x03,RP0
	BCF   0x03,RP1
	CLRF  i
			;       c = input_string[i];
m005	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  i,W
	ADDWF input_string,W
	MOVWF FSR
	BCF   0x03,IRP
	MOVF  INDF,W
	MOVWF c
			;       d = candidate_string[i];
	MOVF  i,W
	ADDWF candidate_string,W
	CALL  _const1
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF d
			;       if(d != c ) return 0;       /* no match    */
	MOVF  d,W
	XORWF c,W
	BTFSC 0x03,Zero_
	GOTO  m006
	BCF   0x03,Carry
	RETURN
			;         if( d == '\0' ) return 1; /* exact match */
m006	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  d,1
	BTFSS 0x03,Zero_
	GOTO  m007
	BSF   0x03,Carry
	RETURN
			;     }
m007	BCF   0x03,RP0
	BCF   0x03,RP1
	INCF  i,1
	GOTO  m005
			;}
			;
			;void blink(char t){
blink
	MOVWF t
			;	LED=0;
	BCF   0x03,RP0
	BCF   0x03,RP1
	BCF   0x05,LED
			;	delay10(t);
	MOVF  t,W
	CALL  delay10
			;	LED=1;
	BCF   0x03,RP0
	BCF   0x03,RP1
	BSF   0x05,LED
			;	delay10(t);
	MOVF  t,W
	GOTO  delay10
			;}
			;
			;void delay( char millisec){
delay
	MOVWF millisec
			;    OPTION = 2;  /* prescaler divide by 8        */
	MOVLW 2
	BSF   0x03,RP0
	BCF   0x03,RP1
	MOVWF OPTION_REG
			;    do  {
			;        TMR0 = 0;
m008	BCF   0x03,RP0
	BCF   0x03,RP1
	CLRF  TMR0
			;        while ( TMR0 < 125)   /* 125 * 8 = 1000  */
m009	MOVLW 125
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF TMR0,W
	BTFSS 0x03,Carry
			;            ;
	GOTO  m009
			;    } while ( -- millisec > 0);
	DECFSZ millisec,1
	GOTO  m008
			;}
	RETURN
			;
			;void initserial( void ){
initserial
			;   /* One start bit, one stop bit, 8 data bit, no parity. 4800 Baud. */
			;
			;   TXEN = 1;      /* transmit enable                   */
	BSF   0x03,RP0
	BCF   0x03,RP1
	BSF   0x98,TXEN
			;   SYNC = 0;      /* asynchronous operation            */
	BCF   0x98,SYNC
			;   TX9  = 0;      /* 8 bit transmission                */
	BCF   0x98,TX9
			;   SPEN = 1;
	BCF   0x03,RP0
	BSF   0x18,SPEN
			;
			;   BRGH  = 0;     /* settings for 4800 Baud            */
	BSF   0x03,RP0
	BCF   0x98,BRGH
			;   BRG16 = 1;     /* @ 4 MHz-clock frequency           */
	BSF   0x9B,BRG16
			;   SPBRG = 51;
	MOVLW 51
	MOVWF SPBRG
			;
			;   CREN = 1;      /* Continuous receive                */
	BCF   0x03,RP0
	BSF   0x18,CREN
			;   RX9  = 0;      /* 8 bit reception                   */
	BCF   0x18,RX9
			;   ANSELH.3 = 0;  /* RB5 not AD-input but serial_in    */
	BSF   0x03,RP1
	BCF   ANSELH,3
			;}
	RETURN
			;
			;void FlushRecieverBuffer(void){  
FlushRecieverBuffer
			;   char trash;
			;   trash = RCREG;  /* the two char's that locked the reciever  */
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  RCREG,W
	MOVWF trash
			;   trash = RCREG;  /* are read and ignored                     */
	MOVF  RCREG,W
	MOVWF trash
			;   CREN = 0;       /* the unlock procedure ...                 */
	BCF   0x18,CREN
			;   CREN = 1;
	BSF   0x18,CREN
			;}
	RETURN
			;
			;char getchar( void )  /* recieves one char */{
getchar
			;   char d_in;
			;   while ( RCIF == 0 ) ;  /* wait for char */
m010	BCF   0x03,RP0
	BCF   0x03,RP1
	BTFSS 0x0C,RCIF
	GOTO  m010
			;   d_in = RCREG;
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  RCREG,W
	MOVWF d_in
			;   return d_in;
	MOVF  d_in,W
	RETURN
			;}
			;
			;void putchar( char d_out )  /* sends one char */{
putchar
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF d_out
			;   while (!TXIF) ;   /* wait until previus character transmitted */
m011	BCF   0x03,RP0
	BCF   0x03,RP1
	BTFSS 0x0C,TXIF
	GOTO  m011
			;   TXREG = d_out;
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  d_out,W
	MOVWF TXREG
			;}
	RETURN
			;
			;void delay10( char n){
delay10
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF n
			;    char i;
			;    OPTION = 7;
	MOVLW 7
	BSF   0x03,RP0
	MOVWF OPTION_REG
			;    do  {
			;        i = TMR0 + 39; /* 256 microsec * 39 = 10 ms */
m012	MOVLW 39
	BCF   0x03,RP0
	BCF   0x03,RP1
	ADDWF TMR0,W
	MOVWF i_2
			;        while ( i != TMR0)
m013	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  i_2,W
	XORWF TMR0,W
	BTFSS 0x03,Zero_
			;            ;
	GOTO  m013
			;    } while ( --n > 0);
	BCF   0x03,RP0
	BCF   0x03,RP1
	DECFSZ n,1
	GOTO  m012
			;}
	RETURN
			;
			;// void string_in(char * string ){
			;	// /*tar in en del av strängen från gpsen, begränsad för att minnet inte räcker till*/
			;	// char charCount, c;
			;	// for( charCount = 0; ; charCount++ ){ 
			;		// c = getchar();        /* input 1 character         */
			;		// if(charCount==(50)||c=='\r'){
			;			// string[charCount] = '\0'; /* add "end of string" */
			;			// return; 
			;           // }
			;		// else{
			;			// string[charCount] = c; /* store the character       */
			;			//putchar(c);       /* don't echo the character  */
			;			//lcd_putchar(c);
			;         // }
			;	// }
			;// }
			;
			;void string_in( char * string ) 
			;{
string_in
			;   char charCount, c;
			;   for( charCount = 0; ; charCount++ )
	BCF   0x03,RP0
	BCF   0x03,RP1
	CLRF  charCount
			;       {
			;         c = getchar( );     /* input 1 character             */
m014	CALL  getchar
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF c_2
			;         string[charCount] = c;   /* store the character           */
	MOVF  charCount,W
	ADDWF string,W
	MOVWF FSR
	BCF   0x03,IRP
	MOVF  c_2,W
	MOVWF INDF
			;         if( (charCount == (MAX_STRING-1))||(c=='\r' )) /* end of input   */
	MOVF  charCount,W
	XORLW 9
	BTFSC 0x03,Zero_
	GOTO  m015
	MOVF  c_2,W
	XORLW 13
	BTFSS 0x03,Zero_
	GOTO  m016
			;           {
			;             string[charCount] = '\0'; /* add "end of string"      */
m015	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  charCount,W
	ADDWF string,W
	MOVWF FSR
	BCF   0x03,IRP
	CLRF  INDF
			;             return;
	RETURN
			;           }
			;       }
m016	BCF   0x03,RP0
	BCF   0x03,RP1
	INCF  charCount,1
	GOTO  m014
			;}
			;
			;void bitbangstring_in( char * string ) 
			;{
bitbangstring_in
			;   char charCount, c;
			;   for( charCount = 0; ; charCount++ )
	CLRF  charCount_2
			;       {
			;         c = bitbanggetchar( );     /* input 1 character             */
m017	CALL  bitbanggetchar
	MOVWF c_3
			;         string[charCount] = c;   /* store the character           */
	MOVF  charCount_2,W
	ADDWF string_2,W
	MOVWF FSR
	BCF   0x03,IRP
	MOVF  c_3,W
	MOVWF INDF
			;         if( (charCount == (MAX_STRING-1))||(c=='\r' )) /* end of input   */
	MOVF  charCount_2,W
	XORLW 9
	BTFSC 0x03,Zero_
	GOTO  m018
	MOVF  c_3,W
	XORLW 13
	BTFSS 0x03,Zero_
	GOTO  m019
			;           {
			;             string[charCount] = '\0'; /* add "end of string"      */
m018	MOVF  charCount_2,W
	ADDWF string_2,W
	MOVWF FSR
	BCF   0x03,IRP
	CLRF  INDF
			;             return;
	RETURN
			;           }
			;       }
m019	INCF  charCount_2,1
	GOTO  m017
			;}
			;
			;void bitbanginitserial( void )  /* initialise PIC16F690 serialcom port */
			;{
bitbanginitserial
			;   ANSEL.0 = 0; /* No AD on RA0             */
	BCF   0x03,RP0
	BSF   0x03,RP1
	BCF   ANSEL,0
			;   ANSEL.1 = 0; /* No AD on RA1             */
	BCF   ANSEL,1
			;   PORTA.0 = 1; /* marking line             */
	BCF   0x03,RP1
	BSF   PORTA,0
			;   TRISA.0 = 0; /* output to PK2 UART-tool  */
	BSF   0x03,RP0
	BCF   TRISA,0
			;   TRISA.1 = 1; /* input from PK2 UART-tool */
	BSF   TRISA,1
			;   return;      
	RETURN
			;}
			;
			;void bitbangputchar( char ch )  /* sends one char */
			;{
bitbangputchar
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF ch
			;  char bitCount, ti;
			;  PORTA.0 = 0; /* set startbit */
	BCF   PORTA,0
			;  for ( bitCount = 10; bitCount > 0 ; bitCount-- )
	MOVLW 10
	MOVWF bitCount
m020	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  bitCount,1
	BTFSC 0x03,Zero_
	GOTO  m022
			;   {
			;     /* delay one bit 104 usec at 4 MHz       */
			;     /* 5+18*5-1+1+9=104 without optimization */ 
			;     ti = 18; do ; while( --ti > 0); nop(); 
	MOVLW 18
	MOVWF ti
m021	BCF   0x03,RP0
	BCF   0x03,RP1
	DECFSZ ti,1
	GOTO  m021
	NOP  
			;     Carry = 1;     /* stopbit                    */
	BSF   0x03,Carry
			;     ch = rr( ch ); /* Rotate Right through Carry */
	RRF   ch,1
			;     PORTA.0 = Carry;
	BTFSS 0x03,Carry
	BCF   PORTA,0
	BTFSC 0x03,Carry
	BSF   PORTA,0
			;   }
	DECF  bitCount,1
	GOTO  m020
			;  return;
m022	RETURN
			;}
			;
			;char bitbanggetchar( void )  /* recieves one char, blocking */
			;{
bitbanggetchar
			;   /* One start bit, one stop bit, 8 data bit, no parity = 10 bit. */
			;   /* Baudrate: 9600 baud => 104.167 usec. per bit.                */
			;   char d_in, bitCount, ti;
			;   while( PORTA.1 == 1 ) /* wait for startbit */ ;
m023	BCF   0x03,RP0
	BCF   0x03,RP1
	BTFSC PORTA,1
	GOTO  m023
			;      /* delay 1,5 bit 156 usec at 4 MHz         */
			;      /* 5+28*5-1+1+2+9=156 without optimization */
			;      ti = 28; do ; while( --ti > 0); nop(); nop2();
	MOVLW 28
	MOVWF ti_2
m024	DECFSZ ti_2,1
	GOTO  m024
	NOP  
	GOTO  m025
			;   for( bitCount = 8; bitCount > 0 ; bitCount--)
m025	MOVLW 8
	MOVWF bitCount_2
m026	MOVF  bitCount_2,1
	BTFSC 0x03,Zero_
	GOTO  m028
			;       {
			;        Carry = PORTA.1;
	BCF   0x03,Carry
	BCF   0x03,RP0
	BCF   0x03,RP1
	BTFSC PORTA,1
	BSF   0x03,Carry
			;        d_in = rr( d_in);  /* rotate carry */
	RRF   d_in_2,1
			;         /* delay one bit 104 usec at 4 MHz       */
			;         /* 5+18*5-1+1+9=104 without optimization */ 
			;         ti = 18; do ; while( --ti > 0); nop(); 
	MOVLW 18
	MOVWF ti_2
m027	DECFSZ ti_2,1
	GOTO  m027
	NOP  
			;        }
	DECF  bitCount_2,1
	GOTO  m026
			;   return d_in;
m028	MOVF  d_in_2,W
	RETURN
			;}
			;
			;void bitbangprintf(const char *string, uns8 variable){
bitbangprintf
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF variable
			;/* Bitbanging för felsökning*/
			;  char i, k, m, a, b;
			;  for(i = 0 ; ; i++)
	CLRF  i_3
			;   {
			;     k = string[i];
m029	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  i_3,W
	ADDWF string_3,W
	CALL  _const1
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF k
			;     if( k == '\0') break;   // at end of string
	MOVF  k,1
	BTFSC 0x03,Zero_
	GOTO  m051
			;     if( k == '%')           // insert variable in string
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  k,W
	XORLW 37
	BTFSS 0x03,Zero_
	GOTO  m049
			;      { 
			;        i++;
	INCF  i_3,1
			;        k = string[i];
	MOVF  i_3,W
	ADDWF string_3,W
	CALL  _const1
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF k
			;        switch(k)
	MOVF  k,W
	XORLW 100
	BTFSC 0x03,Zero_
	GOTO  m030
	XORLW 17
	BTFSC 0x03,Zero_
	GOTO  m033
	XORLW 23
	BTFSC 0x03,Zero_
	GOTO  m042
	XORLW 1
	BTFSC 0x03,Zero_
	GOTO  m046
	XORLW 70
	BTFSC 0x03,Zero_
	GOTO  m047
	GOTO  m048
			;         {
			;           case 'd':         // %d  signed 8bit
			;             if( variable.7 ==1) putchar('-');
m030	BCF   0x03,RP0
	BCF   0x03,RP1
	BTFSS variable,7
	GOTO  m031
	MOVLW 45
	CALL  putchar
			;             else bitbangputchar(' ');
	GOTO  m032
m031	MOVLW 32
	CALL  bitbangputchar
			;             if( variable > 128) variable = -variable;  // no break!
m032	MOVLW 129
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF variable,W
	BTFSS 0x03,Carry
	GOTO  m033
	COMF  variable,1
	INCF  variable,1
			;           case 'u':         // %u unsigned 8bit
			;             a = variable/100;
m033	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  variable,W
	MOVWF C3tmp
	CLRF  C4rem
	MOVLW 8
	MOVWF C2cnt
m034	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   C3tmp,1
	RLF   C4rem,1
	MOVLW 100
	SUBWF C4rem,W
	BTFSS 0x03,Carry
	GOTO  m035
	MOVLW 100
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF C4rem,1
	BSF   0x03,Carry
m035	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   a_2,1
	DECFSZ C2cnt,1
	GOTO  m034
			;             bitbangputchar('0'+a); // print 100's
	MOVLW 48
	ADDWF a_2,W
	CALL  bitbangputchar
			;             b = variable%100; 
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  variable,W
	MOVWF C6tmp
	CLRF  b
	MOVLW 8
	MOVWF C5cnt
m036	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   C6tmp,1
	RLF   b,1
	MOVLW 100
	SUBWF b,W
	BTFSS 0x03,Carry
	GOTO  m037
	MOVLW 100
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF b,1
m037	BCF   0x03,RP0
	BCF   0x03,RP1
	DECFSZ C5cnt,1
	GOTO  m036
			;             a = b/10;
	MOVF  b,W
	MOVWF C8tmp
	CLRF  C9rem
	MOVLW 8
	MOVWF C7cnt
m038	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   C8tmp,1
	RLF   C9rem,1
	MOVLW 10
	SUBWF C9rem,W
	BTFSS 0x03,Carry
	GOTO  m039
	MOVLW 10
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF C9rem,1
	BSF   0x03,Carry
m039	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   a_2,1
	DECFSZ C7cnt,1
	GOTO  m038
			;             bitbangputchar('0'+a); // print 10's
	MOVLW 48
	ADDWF a_2,W
	CALL  bitbangputchar
			;             a = b%10;         
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  b,W
	MOVWF C11tmp
	CLRF  a_2
	MOVLW 8
	MOVWF C10cnt
m040	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   C11tmp,1
	RLF   a_2,1
	MOVLW 10
	SUBWF a_2,W
	BTFSS 0x03,Carry
	GOTO  m041
	MOVLW 10
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF a_2,1
m041	BCF   0x03,RP0
	BCF   0x03,RP1
	DECFSZ C10cnt,1
	GOTO  m040
			;             bitbangputchar('0'+a); // print 1's 
	MOVLW 48
	ADDWF a_2,W
	CALL  bitbangputchar
			;             break;
	GOTO  m050
			;           case 'b':         // %b BINARY 8bit
			;             for( m = 0 ; m < 8 ; m++ )
m042	BCF   0x03,RP0
	BCF   0x03,RP1
	CLRF  m
m043	MOVLW 8
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF m,W
	BTFSC 0x03,Carry
	GOTO  m050
			;              {
			;                if (variable.7 == 1) bitbangputchar('1');
	BTFSS variable,7
	GOTO  m044
	MOVLW 49
	CALL  bitbangputchar
			;                else bitbangputchar('0');
	GOTO  m045
m044	MOVLW 48
	CALL  bitbangputchar
			;                variable = rl(variable);
m045	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   variable,1
			;               }
	INCF  m,1
	GOTO  m043
			;              break;
			;           case 'c':         // %c  'char'
			;             bitbangputchar(variable); 
m046	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  variable,W
	CALL  bitbangputchar
			;             break;
	GOTO  m050
			;           case '%':
			;             bitbangputchar('%');
m047	MOVLW 37
	CALL  bitbangputchar
			;             break;
	GOTO  m050
			;           default:          // not implemented 
			;             bitbangputchar('!');   
m048	MOVLW 33
	CALL  bitbangputchar
			;         }   
			;      }
			;      else bitbangputchar(k); 
	GOTO  m050
m049	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  k,W
	CALL  bitbangputchar
			;   }
m050	BCF   0x03,RP0
	BCF   0x03,RP1
	INCF  i_3,1
	GOTO  m029
			;}
m051	RETURN
			;
			;void printf(const char *string, uns8 variable)
			;{
printf
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF variable_2
			;  char i, k, m, a, b;
			;  for(i = 0 ; ; i++)
	CLRF  i_4
			;   {
			;     k = string[i];
m052	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  i_4,W
	ADDWF string_4,W
	CALL  _const1
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF k_2
			;     if( k == '\0') break;   // at end of string
	MOVF  k_2,1
	BTFSC 0x03,Zero_
	GOTO  m074
			;     if( k == '%')           // insert variable in string
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  k_2,W
	XORLW 37
	BTFSS 0x03,Zero_
	GOTO  m072
			;      { 
			;        i++;
	INCF  i_4,1
			;        k = string[i];
	MOVF  i_4,W
	ADDWF string_4,W
	CALL  _const1
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVWF k_2
			;        switch(k)
	MOVF  k_2,W
	XORLW 100
	BTFSC 0x03,Zero_
	GOTO  m053
	XORLW 17
	BTFSC 0x03,Zero_
	GOTO  m056
	XORLW 23
	BTFSC 0x03,Zero_
	GOTO  m065
	XORLW 1
	BTFSC 0x03,Zero_
	GOTO  m069
	XORLW 70
	BTFSC 0x03,Zero_
	GOTO  m070
	GOTO  m071
			;         {
			;           case 'd':         // %d  signed 8bit
			;             if( variable.7 ==1) putchar('-');
m053	BCF   0x03,RP0
	BCF   0x03,RP1
	BTFSS variable_2,7
	GOTO  m054
	MOVLW 45
	CALL  putchar
			;             else putchar(' ');
	GOTO  m055
m054	MOVLW 32
	CALL  putchar
			;             if( variable > 128) variable = -variable;  // no break!
m055	MOVLW 129
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF variable_2,W
	BTFSS 0x03,Carry
	GOTO  m056
	COMF  variable_2,1
	INCF  variable_2,1
			;           case 'u':         // %u unsigned 8bit
			;             a = variable/100;
m056	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  variable_2,W
	MOVWF C13tmp
	CLRF  C14rem
	MOVLW 8
	MOVWF C12cnt
m057	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   C13tmp,1
	RLF   C14rem,1
	MOVLW 100
	SUBWF C14rem,W
	BTFSS 0x03,Carry
	GOTO  m058
	MOVLW 100
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF C14rem,1
	BSF   0x03,Carry
m058	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   a_3,1
	DECFSZ C12cnt,1
	GOTO  m057
			;             putchar('0'+a); // print 100's
	MOVLW 48
	ADDWF a_3,W
	CALL  putchar
			;             b = variable%100; 
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  variable_2,W
	MOVWF C16tmp
	CLRF  b_2
	MOVLW 8
	MOVWF C15cnt
m059	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   C16tmp,1
	RLF   b_2,1
	MOVLW 100
	SUBWF b_2,W
	BTFSS 0x03,Carry
	GOTO  m060
	MOVLW 100
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF b_2,1
m060	BCF   0x03,RP0
	BCF   0x03,RP1
	DECFSZ C15cnt,1
	GOTO  m059
			;             a = b/10;
	MOVF  b_2,W
	MOVWF C18tmp
	CLRF  C19rem
	MOVLW 8
	MOVWF C17cnt
m061	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   C18tmp,1
	RLF   C19rem,1
	MOVLW 10
	SUBWF C19rem,W
	BTFSS 0x03,Carry
	GOTO  m062
	MOVLW 10
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF C19rem,1
	BSF   0x03,Carry
m062	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   a_3,1
	DECFSZ C17cnt,1
	GOTO  m061
			;             putchar('0'+a); // print 10's
	MOVLW 48
	ADDWF a_3,W
	CALL  putchar
			;             a = b%10;         
	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  b_2,W
	MOVWF C21tmp
	CLRF  a_3
	MOVLW 8
	MOVWF C20cnt
m063	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   C21tmp,1
	RLF   a_3,1
	MOVLW 10
	SUBWF a_3,W
	BTFSS 0x03,Carry
	GOTO  m064
	MOVLW 10
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF a_3,1
m064	BCF   0x03,RP0
	BCF   0x03,RP1
	DECFSZ C20cnt,1
	GOTO  m063
			;             putchar('0'+a); // print 1's 
	MOVLW 48
	ADDWF a_3,W
	CALL  putchar
			;             break;
	GOTO  m073
			;           case 'b':         // %b BINARY 8bit
			;             for( m = 0 ; m < 8 ; m++ )
m065	BCF   0x03,RP0
	BCF   0x03,RP1
	CLRF  m_2
m066	MOVLW 8
	BCF   0x03,RP0
	BCF   0x03,RP1
	SUBWF m_2,W
	BTFSC 0x03,Carry
	GOTO  m073
			;              {
			;                if (variable.7 == 1) putchar('1');
	BTFSS variable_2,7
	GOTO  m067
	MOVLW 49
	CALL  putchar
			;                else putchar('0');
	GOTO  m068
m067	MOVLW 48
	CALL  putchar
			;                variable = rl(variable);
m068	BCF   0x03,RP0
	BCF   0x03,RP1
	RLF   variable_2,1
			;               }
	INCF  m_2,1
	GOTO  m066
			;              break;
			;           case 'c':         // %c  'char'
			;             putchar(variable); 
m069	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  variable_2,W
	CALL  putchar
			;             break;
	GOTO  m073
			;           case '%':
			;             putchar('%');
m070	MOVLW 37
	CALL  putchar
			;             break;
	GOTO  m073
			;           default:          // not implemented 
			;             putchar('!');   
m071	MOVLW 33
	CALL  putchar
			;         }   
			;      }
			;      else putchar(k); 
	GOTO  m073
m072	BCF   0x03,RP0
	BCF   0x03,RP1
	MOVF  k_2,W
	CALL  putchar
			;   }
m073	BCF   0x03,RP0
	BCF   0x03,RP1
	INCF  i_4,1
	GOTO  m052
			;}
m074	RETURN

	END


; *** KEY INFO ***

; 0x00A9 P0   34 word(s)  1 % : compare
; 0x00D6 P0   17 word(s)  0 % : delay
; 0x00CB P0   11 word(s)  0 % : blink
; 0x00E7 P0   18 word(s)  0 % : initserial
; 0x00F9 P0    9 word(s)  0 % : FlushRecieverBuffer
; 0x010C P0   12 word(s)  0 % : putchar
; 0x0102 P0   10 word(s)  0 % : getchar
; 0x0118 P0   22 word(s)  1 % : delay10
; 0x027A P0  207 word(s) 10 % : printf
; 0x012E P0   33 word(s)  1 % : string_in
; 0x014F P0   25 word(s)  1 % : bitbangstring_in
; 0x0168 P0   10 word(s)  0 % : bitbanginitserial
; 0x0172 P0   27 word(s)  1 % : bitbangputchar
; 0x018D P0   30 word(s)  1 % : bitbanggetchar
; 0x01AB P0  207 word(s) 10 % : bitbangprintf
; 0x007A P0   20 word(s)  0 % : get_temp
; 0x008E P0   27 word(s)  1 % : init
; 0x0035 P0   69 word(s)  3 % : main
; 0x0001 P0   52 word(s)  2 % : _const1

; RAM usage: 23 bytes (23 local), 233 bytes free
; Maximum call level: 2
;  Codepage 0 has  841 word(s) :  41 %
;  Codepage 1 has    0 word(s) :   0 %
; Total of 841 code words (20 %)
