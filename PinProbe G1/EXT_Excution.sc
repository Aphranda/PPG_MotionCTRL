 function ext_excution(param1) 

end

// 被测件状态标志
DUT_Result = 0

// 开始测试完成
function ext_StartOK(times) 
	mc.setdo(1,1)
	delay(times)
	mc.setdo(1,0)
	print("======START OK======")
end

// 复位完成
function ext_ResetOK(times) 
	mc.setdo(2,1)
	delay(times)
	mc.setdo(2,0)
	print("======RESET OK======")
end

// 测试异常
function ext_TestError(times) 
	mc.setdo(3,1)
	delay(times)
	mc.setdo(3,0)
	print("======TEST ERROR======")
end

// DUT测试良好
function ext_DutTestOK(times) 
	mc.setdo(4,1)
	delay(times)
	mc.setdo(4,0)
	print("======DUT OK======")
end

// DUT测试不良
function ext_DutTestNG(times) 
	mc.setdo(5,1)
	delay(times)
	mc.setdo(5,0)
	print("======DUT NG======")
end

// 发送DUT状态
function ext_DutTestResult(times) 
	if DUT_Result == 0 then 
		ext_DutTestNG(times)
    end
	
	if DUT_Result == 1 then 
		ext_DutTestOK(times)
    end
	
	if DUT_Result == 2 then 
		ext_TestError(times)
    end
end

// 启动测试功能
function ext_StartTest(times)
	btnStartTest1 = mc.getdi(9)
	delay(times)
	btnStartTest2 = mc.getdi(9)
	btnStartTestNum = btnStartTest1 + btnStartTest2
	TestMachine_AutoCheck = check_auto()
	if btnStartTestNum == 0 and TestMachine_AutoCheck == 1then 
		gTestStep = 2 // 外部IO触发，进入自动测试状态
		print("==============EXT START===============")
    end
end

// 启动复位功能
function ext_StartReset(times) 
	btnStartReset1 = mc.getdi(10)
	delay(times)
	btnStartReset2 = mc.getdi(10)
	
	btnStartResetNum = btnStartReset1 + btnStartReset2
	if btnStartResetNum == 0 then
		motion_home()
		print("==============EXT HOME===============")
	end
end

// 停止测试功能
function ext_StopTest(times) 
	btnStopTest1 = mc.getdi(11)
	delay(times)
	btnStopTest2 = mc.getdi(11)
	
	btnStopTestNum = btnStopTest1 + btnStopTest2
	if btnStopTestNum == 0 then 
		gTestStep = 3 // 外部IO触发，退出测试状态
		print("==============EXT STOP===============")
    end
end

// 模式选择功能
function ext_modeSelect(times) 
	mode1 = mc.getdi(12)
	delay(times)
	mode2 = mc.getdi(13)
	
	// 校准模式
	if mode1 == 1 and mode2 == 0 then 
		gModeState = 1
    end
	
	// 点检模式
	if mode1 == 0 and mode2 == 1 then
		gmodeState = 2	
	end
	
	// 常规模式
	if mode1 == 1 and mode2 == 1 then 
		gModeState = 0
    end
end

