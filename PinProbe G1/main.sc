Sysopt(1, 1, -1)

gInitFlag = 0
gNetInitedFlag = 0
manualMode = 1


lock_flag= 0

axis = 1
axis_guide = 20 // 导程
axis_equivalent = 36000 // 单圈脉冲数

position_mode1 = 198.26 // 新被测件位置

// 配置单轴运行的关门位置
axis_aim_pos = 198.26
// 配置单轴运行的开门位置
axis_zero_pos = -60

// 单轴运行的速度，加减度
axis_aim_vel = 200
axis_aim_acc = 400
axis_aim_dec = 400

// Debug所用标志位
scpi_test = 0
led_test = 0
cyl_test = 0
rf_test = 0
motion_test = 0


function task1_main()
	print(" =TASK 2 START= ")
	local netIdx = 0	// 0~2:服务器，3~5：客服端
	local gProtoType = 1			// 1:TCP,2:UDP
	// 初始化net
	eth_connect(netIdx, gProtoType)
	print("=========================ginitFlag",ginitFlag)
	// 发送及接收
    while gInitFlag > 0 do
		if net.NetSts(netIdx) > 0 then
			delay(1)
			// 读取数据
			read_data = eth_read(netIdx) 
			if rtnIsExist(read_data) then 
				// 将读取到的数据压入SCPI处理函数
				delay(1)
				scpi_recv = scpi_push(read_data)
            end
			
			// 读取处理结果并且返回数据
			if rtnIsExist(scpi_recv) then 
				delay(1)
				eth_write(netIdx,scpi_recv)
            end
		else
			eth_disconnect(netIdx, gProtoType)
		end 
		
    end
end


// 传感器中断查询
function task2_main()
	print(" =TASK 2 START= ")
	while 1 > 0 do
		if gSMSInitFlag == 0 then 
			SmInit()
		end
		delay(1)
		// 外部操作区域
		
		btn_emerged()

		ext_StartTest(30)   	// 开始测试
		ext_StartReset(50) 	// 开始复位
		ext_StopTest(50)	// 停止测试
		
		
		// 模式选择
		gModeOldState = gModeState

		ext_modeSelect(100) 
		// 对外模式
		if gModeOldState ~= gModeState then
			eth_write(0,gModeStateList[gModeState+1])
		end
	end
end

// 状态机线程
function task3_main()
	while 1 > 0 do
		if gSMSInitFlag > 0 then
			if manualMode == 0 then //切换至手动模式
				SmMain()
			else
				delay(1)	// 切换至自动模式
			end
		else
			delay(1)
		end
		// 急停触发状态
		btn_emerged()
		// 检查是否在零位
		check_zero()
		// 检查急停是否触发
		check_emerage()
	end
end

// 初始化函数
function task4_main()
	print(" =TASK 4 INIT= ")
	delay(1000)
	rtn = mc.axisop(axis,3) //延时3s，清除伺服异常
	delay(1000)
	// 如果未使能，则伺服使能
	servo_on = mc.axissts(axis,4)
	if servo_on ~= 1 then 
		mc.axisop(axis,2) 
    end
	gSMSInitFlag = 0
	manualMode = 0
end





