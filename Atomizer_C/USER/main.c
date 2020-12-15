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
 ALIENTEKս��STM32������ʵ��1
 �����ʵ�� 
 ����֧�֣�www.openedv.com
 �Ա����̣�http://eboard.taobao.com 
 ��ע΢�Ź���ƽ̨΢�źţ�"����ԭ��"����ѻ�ȡSTM32���ϡ�
 ������������ӿƼ����޹�˾  
 ���ߣ�����ԭ�� @ALIENTEK
************************************************/

const u8 TEXT_Buffer[]={" "};
#define SIZE sizeof(TEXT_Buffer)		//���鳤��
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

	RCC_APB1PeriphClockCmd(RCC_APB1Periph_TIM3, ENABLE); //ʱ��ʹ��
	
	//��ʱ��TIM3��ʼ��
	TIM_TimeBaseStructure.TIM_Period = arr; //��������һ�������¼�װ�����Զ���װ�ؼĴ������ڵ�ֵ	
	TIM_TimeBaseStructure.TIM_Prescaler =psc; //����������ΪTIMxʱ��Ƶ�ʳ�����Ԥ��Ƶֵ
	TIM_TimeBaseStructure.TIM_ClockDivision = TIM_CKD_DIV1; //����ʱ�ӷָ�:TDTS = Tck_tim
	TIM_TimeBaseStructure.TIM_CounterMode = TIM_CounterMode_Up;  //TIM���ϼ���ģʽ
	TIM_TimeBaseInit(TIM3, &TIM_TimeBaseStructure); //����ָ���Ĳ�����ʼ��TIMx��ʱ�������λ
 
	TIM_ITConfig(TIM3,TIM_IT_Update,ENABLE ); //ʹ��ָ����TIM3�ж�,��������ж�

	//�ж����ȼ�NVIC����
	NVIC_InitStructure.NVIC_IRQChannel = TIM3_IRQn;  //TIM3�ж�
	NVIC_InitStructure.NVIC_IRQChannelPreemptionPriority = 0;  //��ռ���ȼ�0��
	NVIC_InitStructure.NVIC_IRQChannelSubPriority = 3;  //�����ȼ�3��
	NVIC_InitStructure.NVIC_IRQChannelCmd = ENABLE; //IRQͨ����ʹ��
	NVIC_Init(&NVIC_InitStructure);  //��ʼ��NVIC�Ĵ���


	TIM_Cmd(TIM3, ENABLE);  //ʹ��TIMx					 
}
void TIM3_IRQHandler(void)   //TIM3�ж�
{
	float temp;
	u16 adcx;
	int i;
	u8 sendBuf[16];
	int ret;
	int static count=0;
	if (TIM_GetITStatus(TIM3, TIM_IT_Update) != RESET)  //���TIM3�����жϷ������
		{
		TIM_ClearITPendingBit(TIM3, TIM_IT_Update  );  //���TIMx�����жϱ�־ 
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
			while(USART_GetFlagStatus(USART2,USART_FLAG_TC)!=SET);//�ȴ����ͽ���
		}
		
		
		count++;
		if(count>=10&&desired==2)
		{
			ret=sprintf(desiredDateBuf,"02{\"id\":\"123\",\"version\": \"1.0\",\"params\": [\"lock\"]}\n");
			for(i=0;i<ret;i++)
			{
				USART_SendData(USART3, desiredDateBuf[i]);
				while(USART_GetFlagStatus(USART3,USART_FLAG_TC)!=SET);//�ȴ����ͽ���
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
				while(USART_GetFlagStatus(USART3,USART_FLAG_TC)!=SET);//�ȴ����ͽ���
			}
//			for(i=0;i<ret;i++)
//			{
//				USART_SendData(USART1, uploadDateBuf[i]);
//				while(USART_GetFlagStatus(USART1,USART_FLAG_TC)!=SET);//�ȴ����ͽ���
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
	NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2); //����NVIC�жϷ���2:2λ��ռ���ȼ���2λ��Ӧ���ȼ�
	uart_init(115200);	 //���ڳ�ʼ��Ϊ115200
	uart_init2(115200);	 //���ڳ�ʼ��Ϊ115200
	usart3_init(115200);
	delay_init();	    //��ʱ������ʼ��	 
	WS_Init();
	change(Green,1);			 //��ʼ��ɫ
	WS_SetAll(WsDat);
	LED_Init();		  	//��ʼ����LED���ӵ�Ӳ���ӿ�
	Adc_Init();		  		//ADC��ʼ��
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



