#ifndef __WS2811_H
#define __WS2811_H	 
#include "sys.h"


#define White       0xFFFFFF  // 白色
#define Black       0x000000  // 黑色
#define Red         0x00ee00  // 红色
#define Green       0xee0000  // 绿色
#define Blue        0x0000ff  // 蓝色
#define Yollow      0xff00ff  // 蓝色

#define nWs 8		// 有多少颗WS2811级联

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






