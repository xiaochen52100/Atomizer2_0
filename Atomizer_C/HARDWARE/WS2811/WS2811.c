/****************************************************************************************
* WS2811 彩灯驱动函数
*
* 调用方法：
*	修改宏定义： #define nWs 1	// 有多少颗WS2811级联
*	WS_Init();	// IO初始化
*	WsDat[0] = 0x808080;//显存赋值
*	WS_SetAll();  // 发送数据
*	ColorToColor(unsigned long color0, unsigned long color1);// 颜色渐变算法
*
* 作者：星希望（已校验）
* 日期：2015年6月24日
****************************************************************************************/
#include "sys.h"
#include <stm32f10x.h>
#include "WS2811.h"
#include "delay.h"
extern unsigned long newcolor;	
extern u8 flag_kg;
/* 显存 */
unsigned long WsDat[nWs]={0X222222,0X222222,0X222222,0X222222,0X888888,0X888888,0X888888,0X888888};


/**************************************************************************************
* IO初始化（移植时请修改）
**************************************************************************************/
void WS_Init()
{
	GPIO_InitTypeDef  GPIO_InitStructure;	
	
	//端口时钟，使能
	RCC_APB2PeriphClockCmd( RCC_APB2Periph_GPIOC, ENABLE );	 

	// 端口配置
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_6;				// PIN
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP; 		// 推挽输出
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		// IO口速度为50MHz
	GPIO_Init(GPIOC, &GPIO_InitStructure);					// 根据设定参数初始化 
	
}

/**************************
* 内部延时
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
* 发送一比特
****************************/
void TX0()  	{PCout(6) = 1; delay05us();PCout(6) = 0; delay2us(); } // 发送0
void TX1()  	{ PCout(6) = 1; delay2us(); PCout(6) = 0; delay05us(); } // 发送1
void WS_Reset()
	{ 
	
  PCout(6) = 0; 
  delay_us(60); 
	PCout(6)= 1;
  PCout(6) = 0; 
	}

/**************************************************************************************
* 发送一字节
**************************************************************************************/
void WS_Set1(unsigned long dat)
{
	unsigned char i;
	
	for(i=0; i<24; i++)
	{
		if(0x800000 == (dat & 0x800000) )	TX1();
		else								TX0();
		dat<<=1;							//左移一位
	}
}

/**************************************************************************************
* 发送所有字节
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
* 求绝对值
********************************************/
unsigned char abs0(int num)
{
	if(num>0) return num;
	
	num = -num;
	return (unsigned char) num;
}



/***********************************************************************************
* 颜色渐变算法
* 误差 <= 2
************************************************************************************/
u32 ColorToColor(unsigned long color0, unsigned long color1,u8 time)
{
	unsigned char Red0, Green0, Blue0;  // 起始三原色
	unsigned char Red1, Green1, Blue1;  // 结果三原色
	int			  RedMinus, GreenMinus, BlueMinus;	// 颜色差（color1 - color0）
	unsigned char NStep; 							// 需要几步
	float		  RedStep, GreenStep, BlueStep;		// 各色步进值
	unsigned long color;							// 结果色
	unsigned char i,j;
	
	// 绿 红 蓝 三原色分解
	Red0   = color0>>8;
	Green0 = color0>>16;
	Blue0  = color0;
	
	Red1   = color1>>8;
	Green1 = color1>>16;
	Blue1  = color1;
	
	// 计算需要多少步（取差值的最大值）
	RedMinus   = Red1 - Red0; 
	GreenMinus = Green1 - Green0; 
	BlueMinus  = Blue1 - Blue0;
	
	NStep = ( abs0(RedMinus) > abs0(GreenMinus) ) ? abs0(RedMinus):abs0(GreenMinus);
	NStep = ( NStep > abs0(BlueMinus) ) ? NStep:abs0(BlueMinus);
	
	// 计算出各色步进值
	RedStep   = (float)RedMinus   / NStep;
	GreenStep = (float)GreenMinus / NStep;
	BlueStep  = (float)BlueMinus  / NStep;
	
	// 渐变开始
	for(i=0; i<NStep; i++)
	{
		Red1   = Red0   + (int)(RedStep   * i);
		Green1 = Green0 + (int)(GreenStep * i);
		Blue1  = Blue0  + (int)(BlueStep  * i);
		
		color  = Green1<<16 | Red1<<8 | Blue1; 	// 合成  绿红蓝
		for(j=0; j<nWs; j++)
	{
		WsDat[j]=(unsigned long)color; 
	}
	 
	  WS_SetAll(WsDat);
	  
		delay_ms(time);						// 渐变速度
	}
	// 渐变结束
	
	return color;
}
void brightness_change(int color0,float multiple)
{
	unsigned char Red0, Green0, Blue0;  // 起始三原色
	unsigned char Red1, Green1, Blue1;  // 变化三原色
	unsigned long color1;							// 结果色
	unsigned char j;
	Red0   = color0>>8;
	Green0 = color0>>16;
	Blue0  = color0;
	
	Red1=(int)Red0*multiple;
	Green1=(int)Green0*multiple;
	Blue1=(int)Blue0*multiple;
	
	color1  = Green1<<16 | Red1<<8 | Blue1; 	// 合成  绿红蓝
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





