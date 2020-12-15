/****************************************************************************************
* WS2811 �ʵ���������
*
* ���÷�����
*	�޸ĺ궨�壺 #define nWs 1	// �ж��ٿ�WS2811����
*	WS_Init();	// IO��ʼ��
*	WsDat[0] = 0x808080;//�Դ渳ֵ
*	WS_SetAll();  // ��������
*	ColorToColor(unsigned long color0, unsigned long color1);// ��ɫ�����㷨
*
* ���ߣ���ϣ������У�飩
* ���ڣ�2015��6��24��
****************************************************************************************/
#include "sys.h"
#include <stm32f10x.h>
#include "WS2811.h"
#include "delay.h"
extern unsigned long newcolor;	
extern u8 flag_kg;
/* �Դ� */
unsigned long WsDat[nWs]={0X222222,0X222222,0X222222,0X222222,0X888888,0X888888,0X888888,0X888888};


/**************************************************************************************
* IO��ʼ������ֲʱ���޸ģ�
**************************************************************************************/
void WS_Init()
{
	GPIO_InitTypeDef  GPIO_InitStructure;	
	
	//�˿�ʱ�ӣ�ʹ��
	RCC_APB2PeriphClockCmd( RCC_APB2Periph_GPIOC, ENABLE );	 

	// �˿�����
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_6;				// PIN
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP; 		// �������
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		// IO���ٶ�Ϊ50MHz
	GPIO_Init(GPIOC, &GPIO_InitStructure);					// �����趨������ʼ�� 
	
}

/**************************
* �ڲ���ʱ
***************************/
void delay2us()
{
	unsigned char i;
	for(i=0; i<12; i++);
}
void delay05us()
{
	unsigned char i;
	for(i=0; i<1; i++);
}

/***************************
* ����һ����
****************************/
void TX0()  	{PCout(6) = 1; delay05us();PCout(6) = 0; delay2us(); } // ����0
void TX1()  	{ PCout(6) = 1; delay2us(); PCout(6) = 0; delay05us(); } // ����1
void WS_Reset()
	{ 
	
  PCout(6) = 0; 
  delay_us(60); 
	PCout(6)= 1;
  PCout(6) = 0; 
	}

/**************************************************************************************
* ����һ�ֽ�
**************************************************************************************/
void WS_Set1(unsigned long dat)
{
	unsigned char i;
	
	for(i=0; i<24; i++)
	{
		if(0x800000 == (dat & 0x800000) )	TX1();
		else								TX0();
		dat<<=1;							//����һλ
	}
}

/**************************************************************************************
* ���������ֽ�
**************************************************************************************/
void WS_SetAll(unsigned long *p)
{
	unsigned char j;
	
	for(j=0; j<nWs; j++)
	{
		WS_Set1(p[j]);  // j / 0
	}
	WS_Reset();
}


void change(unsigned long color,float i)
{
	unsigned char j;
	for(j=0; j<nWs; j++)
	{
		WsDat[j]=(unsigned long)color*i; 
	}
	
}






/********************************************
* �����ֵ
********************************************/
unsigned char abs0(int num)
{
	if(num>0) return num;
	
	num = -num;
	return (unsigned char) num;
}



/***********************************************************************************
* ��ɫ�����㷨
* ��� <= 2
************************************************************************************/
u32 ColorToColor(unsigned long color0, unsigned long color1,u8 time)
{
	unsigned char Red0, Green0, Blue0;  // ��ʼ��ԭɫ
	unsigned char Red1, Green1, Blue1;  // �����ԭɫ
	int			  RedMinus, GreenMinus, BlueMinus;	// ��ɫ�color1 - color0��
	unsigned char NStep; 							// ��Ҫ����
	float		  RedStep, GreenStep, BlueStep;		// ��ɫ����ֵ
	unsigned long color;							// ���ɫ
	unsigned char i,j;
	
	// �� �� �� ��ԭɫ�ֽ�
	Red0   = color0>>8;
	Green0 = color0>>16;
	Blue0  = color0;
	
	Red1   = color1>>8;
	Green1 = color1>>16;
	Blue1  = color1;
	
	// ������Ҫ���ٲ���ȡ��ֵ�����ֵ��
	RedMinus   = Red1 - Red0; 
	GreenMinus = Green1 - Green0; 
	BlueMinus  = Blue1 - Blue0;
	
	NStep = ( abs0(RedMinus) > abs0(GreenMinus) ) ? abs0(RedMinus):abs0(GreenMinus);
	NStep = ( NStep > abs0(BlueMinus) ) ? NStep:abs0(BlueMinus);
	
	// �������ɫ����ֵ
	RedStep   = (float)RedMinus   / NStep;
	GreenStep = (float)GreenMinus / NStep;
	BlueStep  = (float)BlueMinus  / NStep;
	
	// ���俪ʼ
	for(i=0; i<NStep; i++)
	{
		Red1   = Red0   + (int)(RedStep   * i);
		Green1 = Green0 + (int)(GreenStep * i);
		Blue1  = Blue0  + (int)(BlueStep  * i);
		
		color  = Green1<<16 | Red1<<8 | Blue1; 	// �ϳ�  �̺���
		for(j=0; j<nWs; j++)
	{
		WsDat[j]=(unsigned long)color; 
	}
	 
	  WS_SetAll(WsDat);
	  
		delay_ms(time);						// �����ٶ�
	}
	// �������
	
	return color;
}
void brightness_change(int color0,float multiple)
{
	unsigned char Red0, Green0, Blue0;  // ��ʼ��ԭɫ
	unsigned char Red1, Green1, Blue1;  // �仯��ԭɫ
	unsigned long color1;							// ���ɫ
	unsigned char j;
	Red0   = color0>>8;
	Green0 = color0>>16;
	Blue0  = color0;
	
	Red1=(int)Red0*multiple;
	Green1=(int)Green0*multiple;
	Blue1=(int)Blue0*multiple;
	
	color1  = Green1<<16 | Red1<<8 | Blue1; 	// �ϳ�  �̺���
	for(j=0; j<nWs; j++)
	{
		WsDat[j]=(unsigned long)color1; 
	}
	 
	  WS_SetAll(WsDat);
}

void mode_breathe(void)
{
	u8 i;
	if(flag_kg==1)
	{
	for(i=15;i<100;i++)
	{
	  brightness_change(newcolor,i/100.00);
		delay_ms(20);	
	}
	delay_ms(3000);	
		for(i=100;i>15;i--)
	{
	  brightness_change(newcolor,i/100.00);
		delay_ms(10);	
	}
	delay_ms(5000);	
	}
	else if(flag_kg==2)
	{
		brightness_change(Black,1);
	}
}

void mode_docidaci(void)
{
	if(flag_kg==1)
	{
	 brightness_change(Red,0.3);
	delay_ms(500);	
	 brightness_change(Green,0.3);
	delay_ms(500);	
	 brightness_change(White,1);
	delay_ms(500);	
	 brightness_change(Blue,0.3);
	delay_ms(500);	
		}
	else if(flag_kg==2)
	{
		brightness_change(Black,1);
	}
}

void mode_manshn(void)
{
	if(flag_kg==1)
	{
	ColorToColor(Red,Green,10);
	ColorToColor(Green,Blue,10);
	ColorToColor(Blue,Red,10);
  }
	else if(flag_kg==2)
	{
		brightness_change(Black,1);
	}
}





