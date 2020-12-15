#include "sys.h"
#include "delay.h"
#include "usart.h"
#include "usart2.h"
#include "led.h"
#include "dht11.h"
#include "ds18b20.h"
#include "usart3.h"
#include "WS2811.h"
#include "adc.h"
#include "data.h"
#include "24cxx.h"
#include "stmflash.h"
#include "string.h"
/************************************************
 ALIENTEK战舰STM32开发板实验1
 跑马灯实验 
 技术支持：www.openedv.com
 淘宝店铺：http://eboard.taobao.com 
 关注微信公众平台微信号："正点原子"，免费获取STM32资料。
 广州市星翼电子科技有限公司  
 作者：正点原子 @ALIENTEK
************************************************/

const u8 TEXT_Buffer[]={" "};
#define SIZE sizeof(TEXT_Buffer)		//数组长度
u8 flag_kg=0;
extern char desired;
unsigned long newcolor=Green;
struct Data data;
u8 uploadDateBuf[256];
u8 desiredDateBuf[256];
void TIM3_Int_Init(u16 arr,u16 psc)
{
    TIM_TimeBaseInitTypeDef  TIM_TimeBaseStructure;
	NVIC_InitTypeDef NVIC_InitStructure;

	RCC_APB1PeriphClockCmd(RCC_APB1Periph_TIM3, ENABLE); //时钟使能
	
	//定时器TIM3初始化
	TIM_TimeBaseStructure.TIM_Period = arr; //设置在下一个更新事件装入活动的自动重装载寄存器周期的值	
	TIM_TimeBaseStructure.TIM_Prescaler =psc; //设置用来作为TIMx时钟频率除数的预分频值
	TIM_TimeBaseStructure.TIM_ClockDivision = TIM_CKD_DIV1; //设置时钟分割:TDTS = Tck_tim
	TIM_TimeBaseStructure.TIM_CounterMode = TIM_CounterMode_Up;  //TIM向上计数模式
	TIM_TimeBaseInit(TIM3, &TIM_TimeBaseStructure); //根据指定的参数初始化TIMx的时间基数单位
 
	TIM_ITConfig(TIM3,TIM_IT_Update,ENABLE ); //使能指定的TIM3中断,允许更新中断

	//中断优先级NVIC设置
	NVIC_InitStructure.NVIC_IRQChannel = TIM3_IRQn;  //TIM3中断
	NVIC_InitStructure.NVIC_IRQChannelPreemptionPriority = 0;  //先占优先级0级
	NVIC_InitStructure.NVIC_IRQChannelSubPriority = 3;  //从优先级3级
	NVIC_InitStructure.NVIC_IRQChannelCmd = ENABLE; //IRQ通道被使能
	NVIC_Init(&NVIC_InitStructure);  //初始化NVIC寄存器


	TIM_Cmd(TIM3, ENABLE);  //使能TIMx					 
}
void TIM3_IRQHandler(void)   //TIM3中断
{
	float temp;
	u16 adcx;
	int i;
	u8 sendBuf[16];
	int ret;
	int static count=0;
	if (TIM_GetITStatus(TIM3, TIM_IT_Update) != RESET)  //检查TIM3更新中断发生与否
		{
		TIM_ClearITPendingBit(TIM3, TIM_IT_Update  );  //清除TIMx更新中断标志 
		printf("desired:%d\r\n",desired);
		LED0=!LED0;
		DHT11_Read_Data_2(&data.hum1);
		//printf("hum1:%d,\n",data.hum1);
		data.tem1=DS18B20_Get_Temp();
		//printf("tem1:%d,\n",data.tem1);
		adcx=Get_Adc_Average(ADC_Channel_1,10);
		temp=(float)adcx*(3.3/4096);
		adcx=temp;
		temp*=100;
		data.leval=(temp-25)/200.00*100;
		if(data.leval>=100) data.leval=100;
		//data.leval=(u8)temp;
		//printf("adcx:%d,\n",data.leval);
		memcpy(sendBuf,&data,sizeof(struct Data));
		for(i=0;i<sizeof(struct Data);i++)
		{
			USART_SendData(USART2, sendBuf[i]);
			while(USART_GetFlagStatus(USART2,USART_FLAG_TC)!=SET);//等待发送结束
		}
		
		
		count++;
		if(count>=10&&desired==2)
		{
			ret=sprintf(desiredDateBuf,"02{\"id\":\"123\",\"version\": \"1.0\",\"params\": [\"lock\"]}\n");
			for(i=0;i<ret;i++)
			{
				USART_SendData(USART3, desiredDateBuf[i]);
				while(USART_GetFlagStatus(USART3,USART_FLAG_TC)!=SET);//等待发送结束
			}
			printf("send %s \r\n",desiredDateBuf);
			count=0;
			//printf(desiredDateBuf);
		}
		if(count>=60&&desired!=2)
		{
			ret=sprintf(uploadDateBuf,"01{\"id\":\"123\",\"version\": \"1.0\",\"params\": {\"level\":{\"value\":%d},\"lock\":{\"value\":%d}}}\n",data.leval,data.lock);
			for(i=0;i<ret;i++)
			{
				USART_SendData(USART3, uploadDateBuf[i]);
				while(USART_GetFlagStatus(USART3,USART_FLAG_TC)!=SET);//等待发送结束
			}
//			for(i=0;i<ret;i++)
//			{
//				USART_SendData(USART1, uploadDateBuf[i]);
//				while(USART_GetFlagStatus(USART1,USART_FLAG_TC)!=SET);//等待发送结束
//			}
		//printf(uploadDateBuf);
			count=0;
		}
		}
}
int main(void)
{	
	u16 writeTemp[1]={0};
	u8 hum_tem=0;
	u8 i=0;
	u8 ret;
	u16 tem1;
	u16 humidity1=0;
	u8 read24cxx=0;
	u8 datatemp[2];
	NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2); //设置NVIC中断分组2:2位抢占优先级，2位响应优先级
	uart_init(115200);	 //串口初始化为115200
	uart_init2(115200);	 //串口初始化为115200
	usart3_init(115200);
	delay_init();	    //延时函数初始化	 
	WS_Init();
	change(Green,1);			 //初始颜色
	WS_SetAll(WsDat);
	LED_Init();		  	//初始化与LED连接的硬件接口
	Adc_Init();		  		//ADC初始化
	DHT11_Init();
	DS18B20_Init();
//	while(DHT11_Init());
//	while(DS18B20_Init());
	memset(&data,0,sizeof(struct Data));
	data.head=0xFE;
	data.tail=0xCF;
//	LED0=0;
	
//	STMFLASH_Write(FLASH_SAVE_ADDR,(u16*)writeTemp,1);
//	delay_ms(1000);
	STMFLASH_Read(FLASH_SAVE_ADDR,(u16*)datatemp,1);
	printf("datatemp[0]:%d  datatemp[1]:%d\n",datatemp[0],datatemp[1]);
	if(datatemp[0]==0&&datatemp[1]==0)
	{
		data.lock=0;
		LED1=1;
		//printf("unlock\n");
	}
	else if(datatemp[0]==1&&datatemp[1]==0)
	{
		data.lock=1;
		LED1=0;
		//printf("lock\n");
	}
	delay_ms(1000);
	delay_ms(1000);
	TIM3_Int_Init(9999,7199);
//	ret=sprintf(desiredDateBuf,"02{\"id\":\"123\",\"version\": \"1.0\",\"params\": [\"lock\"]}");
//	printf(desiredDateBuf);
	while(1)
	{
		delay_ms(1000);
	}
}



