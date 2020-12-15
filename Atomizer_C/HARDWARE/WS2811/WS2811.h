#ifndef __WS2811_H
#define __WS2811_H	 
#include "sys.h"


#define White       0xFFFFFF  // ��ɫ
#define Black       0x000000  // ��ɫ
#define Red         0x00ee00  // ��ɫ
#define Green       0xee0000  // ��ɫ
#define Blue        0x0000ff  // ��ɫ
#define Yollow      0xff00ff  // ��ɫ

#define nWs 8		// �ж��ٿ�WS2811����

extern unsigned long WsDat[];
void change(unsigned long color,float i);
void brightness_change(int color0,float multiple);
extern void WS_Init(void);
void WS_SetAll(unsigned long *p);
extern u32 ColorToColor(unsigned long color0, unsigned long color1,u8 time);
void mode_breathe(void);
void mode_docidaci(void);		
void mode_manshn(void);
#endif






