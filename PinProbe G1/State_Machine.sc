gSMSInitFlag = 1		// 初始化标志位
gSMStep = 0			// 状态机状态变量
gSMOldStep = 0
gSMStState = {"IDLE","READY","RUNNING","TESTING","LOCK","EMERGENCY"}

gTestStep = 0
gCalStep = 0
gPiStep = 0

gTestState = {"TESTING", "TESTED"}
gModeState = 0
gModeOldState = 0
gModeStateList = {"AT", "CAL", "PI"}

TestMachine_AutoCheck = 0 // 默认为手动状态

function SmInit() 	// 状态机初始化
		// 如果未回零，则进行回零操作

		// 气缸抬起
		rtn1 = scpi_cyl_excution("CONF:CYL1 UP")
		if rtn1 == "UP" then
		// 气缸抬起完毕后，电机进行复位, 完成退出
			motion_home()
		end	
		// 电机退出
		
		// 门开完之后，状态变成IDLE
		if rtn2 == "OPEN" then
			gSMStep = 0
			// LED 灯熄灭
			scpi_led_excution("CONF:LED OFF")
			eth_write(0,gSMStState[1])
		end
		gSMSInitFlag = 1
end

function SmMain() // 状态机主函数
	// 更新状态
	gSMOldStep = gSMStep
	
	if gSMStep == 0 then SmStep0() 
		elseif gSMStep == 1 then SmStep1() 
		elseif gSMStep == 2 then SmStep2() 
		elseif gSMStep == 3 then SmStep3() 
		elseif gSMStep == 4 then SmStep4() 
	end
	
	// 对外发送状态
	if gSMOldStep ~= gSMStep then
		showState()
	end
end

function SmStep0() // 状态0处理：Idle
	delay(10)
	rtn = allBtnIsPress(0.2) 										// 短按一秒
	if rtn == "Pressed" or gTestStep == 1 or gCalStep == 2 then									
		gSMStep = 1												// 跳转到下一个状态
		scpi_led_excution("CONF:LED YELLOW")		// 灯变成黄色，状态变成Ready
	end
	// print("STEP0")
end

function SmStep1()		// 状态1处理：Reading
    delay(10)
	rtn = allBtnIsPress(0.2) 											// 长按一秒
	if rtn == "Pressed" or gTestStep == 2 or gCalStep == 2 then
		gSMStep = 2
		// 通知自动测试已经启动
		ext_StartOK(500)
	end
	// print("STEP1")
end

function SmStep2() 	// 状态2处理：Running
// 开始关门
	rtn1 = scpi_servo_excution("CONF:SERVO CLOSE") 
	if rtn1 == "CLOSE" then 
		// 关门完成后，气缸开始下压
		rtn2 = scpi_cyl_excution("CONF:CYL1 DOWN") 
		// 如果气缸下压超时，报警并退出
		if rtn2 == "CYL ERROR" then 
			print("========CYL ERROR============")
			ErrorExit() 
        end
    end

	if rtn1 == "CLOSE" and rtn2 == "DOWN" then
		gSMStep = 3		// 跳转到下一个状态
		gTestStep = 0
		gCalStep = 0
	end
	// print("STEP2")
end

function SmStep3() 	// 状态3处理:Testing
	delay(10)
	scpi_led_excution("CONF:LED GREEN")
    gSMStep = 4	// 跳转到下一个状态
end

function SmStep4() 	// 状态4处理:开门
	rtn = allBtnIsPress(0.3)  // 短按0.3s
	if rtn == "Pressed" or gTestStep == 3 or gCalStep == 3 then
		rtn1 = scpi_cyl_excution("CONF:CYL1 UP") 		// 气缸上升
		if rtn1 == "UP" then
			rtn2 = scpi_servo_excution("CONF:SERVO OPEN") // 开始开门
			if rtn2 == "OPEN" then
				gSMStep = 0
				gTestStep = 0
				gCalStep = 0
				scpi_led_excution("CONF:LED OFF")	
				eth_write(0,"TESTED")		// 测试完成，抛出测试完成指令
				// 自动发OK或者NG
				ext_DutTestResult(500)
			end
		end
	end
	// print("STEP4")
end

// 卡死自动退料
function ErrorExit() 
	rtn1 = scpi_cyl_excution("CONF:CYL1 UP") 		// 气缸上升
	if rtn1 == "UP" then
		rtn2 = scpi_servo_excution("CONF:SERVO OPEN") // 开始开门
		if rtn2 == "OPEN" then
			gSMStep = 0
			gTestStep = 0
			gCalStep = 0
			scpi_led_excution("CONF:LED OFF")	
			eth_write(0,"ERROREXIT")		// 未压合好，退出抛料
			// 自动发OK或者NG
			DUT_Result = 2
			ext_DutTestResult(500)
		end
	end
end

function showState() 
	if gSMStep == 0 then
		eth_write(0,gSMStState[1])
		print("STATE:",gSMStState[1])
	elseif gSMStep == 1 then
		eth_write(0,gSMStState[2])
		print("STATE:",gSMStState[2])
	elseif gSMStep == 2 then
		eth_write(0,gSMStState[3])
		print("STATE:",gSMStState[3])
	elseif gSMStep == 3 then
		eth_write(0,gSMStState[4])
		print("STATE:",gSMStState[4])
	elseif gSMStep == 5 then
		eth_write(0,gSMStState[6])
		print("STATE:",gSMStState[6])
	end
end