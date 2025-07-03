// 测试SCPI功能
function scpi_excution(param1)
	if scpi_test == 1 then
		print(" =SCPI= ")
		/*
		rtn1 = scpi_push("CONFigure:LOCK ON")
		
		rtn3 = scpi_push("READ:LED STATe?")
		rtn4 = scpi_push("CONFigure:SWITCH2 RF2")
		rtn5 = scpi_push("READ:SWITCH1:STATE?")
		*/
		scpi_push("CONFigure:SERVO CLOSE")
		rtn = scpi_push("READ:SERVO:STATE?")
		print(rtn)
		scpi_push("CONFigure:SERVO OPEN")
		rtn = scpi_push("READ:SERVO:STATE?")
		print(rtn)
	end
end

// SCPI指令分发
function scpi_push(str_scpi) 

	idxc = string.find(str_scpi,"CONF")
	idxr = string.find(str_scpi,"READ")
	idxt = string.find(str_scpi,"TRIG")
	idxbase = string.find(str_scpi,"*")
	// 判断是否配置指令
	if rtnIsExist(idxc) then 
		return conf_push(str_scpi)
	end
	
	// 判断是否读取指令
	if rtnIsExist(idxr) then
		return read_push(str_scpi)
	end
	

	// 判断是否触发指令
	if rtnIsExist(idxt) then
		return trig_push(str_scpi)
	end
	
	// 判断是否为基础指令
	if rtnIsExist(idxbase) then 
		return base_push(str_scpi)
    end
	
end

function base_push(str_scpi) 
	idxidn = string.find(str_scpi,"*IDN?")
	if rtnIsExist(idxidn) then 
		return "GTS PINPROBEG1 20240328 V0.0.1"
	end
end

// 判断配置的具体动作，分发CONF指令
function conf_push(str_scpi) 
	idxlock = string.find(str_scpi,"LOCK")
	idxled = string.find(str_scpi,"LED")
	idxsw = string.find(str_scpi,"SWITCH")
	idxcyl = string.find(str_scpi,"CYL")
	idxservo = string.find(str_scpi,"SERV")
	idxeth = string.find(str_scpi,"ETH")
	idxtest = string.find(str_scpi,"TEST")
	idxmode = string.find(str_scpi,"MODE")

	// 分发配置动作
	
	// 配置LOCK
	if rtnIsExist(idxlock) then 
		rtn = scpi_lock_excution(str_scpi)
		return rtn
    end
	
	// 配置LED
	if rtnIsExist(idxled) then 
		rtn = scpi_led_excution(str_scpi)
		return rtn
    end
	
	// 配置SWITCH
	if rtnIsExist(idxsw) then 
		rtn = scpi_sw_excution(str_scpi)
		return rtn
    end
	
	// 配置CYL
	if rtnIsExist(idxcyl) then 
		rtn = scpi_cyl_excution(str_scpi)
		return rtn
    end
	
	// 配置SERVO
	if rtnIsExist(idxservo) then 
		rtn = scpi_servo_excution(str_scpi)
		return rtn
	end
	
	// 配置ETH
	if rtnIsExist(idxeth) then 
		rtn = scpi_eth_excution(str_scpi)
		return rtn
	end
	
	// 配置TEST
	if rtnIsExist(idxtest) then 
		rtn = scpi_test_excution(str_scpi)
		return rtn
	end
	
	// 配置MODE
	if rtnIsExist(idxmode) then 
		rtn = scpi_mode_excution(str_scpi)
		return rtn
	end

	
	return "SCPI ERROR"
end
// 判断读取的具体状态，分发READ指令
function read_push(str_scpi) 
	idxlock = string.find(str_scpi,"LOCK")
	idxled = string.find(str_scpi,"LED")
	idxsw = string.find(str_scpi,"SWITCH")
	idxcyl = string.find(str_scpi,"CYL")
	idxservo = string.find(str_scpi,"SERV")
	idsys = string.find(str_scpi,"SYSTEM")
	idtrig = string.find(str_scpi,"TRIG")
	idxmode = string.find(str_scpi,"MODE")
	
	// 分发读取动作
	
	// 读取LOCK
	if rtnIsExist(idxlock) then 
		rtn = scpi_lock_excution(str_scpi)
		return rtn
    end
	
	// 读取LED
	if rtnIsExist(idxled) then 
		rtn = scpi_led_excution(str_scpi)
		return rtn
    end
	
	// 读取射频开关
	if rtnIsExist(idxsw) then 
		rtn = scpi_sw_excution(str_scpi)
		return rtn
    end
	
	// 读取CYL
	if rtnIsExist(idxcyl) then 
		rtn = scpi_cyl_excution(str_scpi)
		return rtn
    end
	
	// 读取SERVO
	if rtnIsExist(idxservo) then 
		rtn = scpi_servo_excution(str_scpi)
		return rtn
    end
	
	// 读取SYSTEM状态
	if rtnIsExist(idsys) then 
		rtn = scpi_sys_excution(str_scpi)
		return rtn
    end
	
	// 读取MODE
	if rtnIsExist(idxmode) then 
		rtn = scpi_mode_excution(str_scpi)
		return rtn
	end
	
end
// 判断读取的具体动作，分发TRIG指令
function trig_push(str_scpi) 
	print("TRIG PUSH")
	return 1
end
// 执行LOCK的动作和查询指令，并返回执行/查询结果
function scpi_lock_excution(str_scpi) 
	// 判断LOCK动作
	rtn_on = string.find(str_scpi,"LOCKED")
	rtn_off = string.find(str_scpi,"UNLOCK")
	rtn_state = string.find(str_scpi,"STAT")
	
	// 执行LOCK动作
	if rtnIsExist(rtn_on) then
		lock_flag = 1
		manualMode = 1
		return "LOCKED"
    end
	
	
	if rtnIsExist(rtn_off) then
		lock_flag = 0
		gSMSInitFlag = 0
		manualMode = 0
		mc.setdo(2,0)
		// 手动模式开启，进行初始化
		rtn_off = 0
		motion_home()
		
		
		return "UNLOCK"
	end
	
	// 回复LOCK状态
	if rtnIsExist(rtn_state) then 
		if lock_flag ==1 then 
			return "LOCKED"
        end
		if lock_flag ==1 then 
			return "UNLOCK"
        end
    end
	return "SCPI ERROR"
end

// 执行LED的动作和查询指令，并返回执行/查询结果
function scpi_led_excution(str_scpi) 

	// 判断LED颜色及状态
	rtn_red = string.find(str_scpi,"RED")
	rtn_green = string.find(str_scpi,"GREEN")
	rtn_yellow = string.find(str_scpi,"YELLOW")
	rtn_off = string.find(str_scpi,"OFF")
	rtn_state = string.find(str_scpi,"STAT")

	// 执行LED颜色切换
	if rtnIsExist(rtn_off) then
		rtn = led_encode(0)
		return rtn
    end	

	if rtnIsExist(rtn_red) then
		rtn = led_encode(1)
		return rtn
    end
	
	if rtnIsExist(rtn_green) then
		rtn = led_encode(2)
		return rtn
    end
	
	if rtnIsExist(rtn_yellow) then
		rtn = led_encode(3)
		return rtn
    end
	
	// 回复LED状态
	if rtnIsExist(rtn_state) then 
		rtn = led_decode()
		return rtn;
    end
	
	return "SCPI ERROR"
end

// 执行SWITCH的动作和查询指令，并返回执行/查询结果
function scpi_sw_excution(str_scpi) 
	if manualMode == 0 then 
		rtn_sw1 = string.find(str_scpi,"SWITCH1")
		rtn_sw2 = string.find(str_scpi,"SWITCH2")
		rtn_state = string.find(str_scpi,"STATE")
		rtn_rf1 = string.find(str_scpi,"RF1")
		rtn_rf2 = string.find(str_scpi,"RF2")
		rtn_rf3 = string.find(str_scpi,"RF3")
		rtn_rf4 = string.find(str_scpi,"RF4")
		rtn_rf5 = string.find(str_scpi,"RF5")
		rtn_rf6 = string.find(str_scpi,"RF6")
		rtn_rf7 = string.find(str_scpi,"RF7")
		rtn_rf8 = string.find(str_scpi,"RF8")
    elseif manualMode == 1 then
		return "UNLOCK"
	end


	if  rtnIsExist(rtn_sw1) then 
		// 配置指令
		if rtnIsExist(rtn_rf1) then 
			switch1_encode(1)
			return "RF1"
		elseif rtnIsExist(rtn_rf2) then 
			switch1_encode(2)
			return "RF2"
		elseif rtnIsExist(rtn_rf3) then 
			switch1_encode(3)
			return "RF3"
		elseif rtnIsExist(rtn_rf4) then 
			switch1_encode(4)
			return "RF4"
		elseif rtnIsExist(rtn_rf5) then 
			switch1_encode(5)
			return "RF5"
		elseif rtnIsExist(rtn_rf6) then 
			switch1_encode(6)
			return "RF6"
		elseif rtnIsExist(rtn_rf7) then 
			switch1_encode(7)
			return "RF7"
		elseif rtnIsExist(rtn_rf8) then 
			switch1_encode(8)
			return "RF8"
        end
		// 查询指令
		if rtnisExist(rtn_state) then 
			rtn = switch1_decode()
			return string.format("RF%X",rtn)
        end
    end
	
	if  rtnIsExist(rtn_sw2) then 
		if rtnIsExist(rtn_rf1) then 
			switch2_encode(1)
			return "RF1"
		elseif rtnIsExist(rtn_rf2) then 
			switch2_encode(2)
			return "RF2"
		elseif rtnIsExist(rtn_rf3) then 
			switch2_encode(3)
			return "RF3"
		elseif rtnIsExist(rtn_rf4) then 
			switch2_encode(4)
			return "RF4"
		elseif rtnIsExist(rtn_rf5) then 
			switch2_encode(5)
			return "RF5"
		elseif rtnIsExist(rtn_rf6) then 
			switch2_encode(6)
			return "RF6"
		elseif rtnIsExist(rtn_rf7) then 
			switch2_encode(7)
			return "RF7"
		elseif rtnIsExist(rtn_rf8) then 
			switch2_encode(8)
			return "RF8"
        end
		
		// 查询指令
		if rtnisExist(rtn_state) then 
			rtn = switch2_decode()
			return string.format("RF%X",rtn)
        end
    end
	return "SCPI ERROR"
end


// 执行SWITCH的动作和查询指令，并返回执行/查询结果
function scpi_cyl_excution(str_scpi) 
	rtn_up = string.find(str_scpi,"UP")
	rtn_down = string.find(str_scpi,"DOWN")
	rtn_state = string.find(str_scpi,"STATE")

	// 气缸上升
	if rtnisExist(rtn_up) then
		rtn = cyl1_encode(2)
		count = 0
		countMax = 100
		print(count)
		while count < countMax do 
			state = cyl1_decode()
			if state == "UP" then
				print("UP")
				return "UP"
			end
			count = count + 1
			print("CYL_UP:", count)
			delay(100)
        end
		return "CYL ERROR"
	end
	
	// 气缸下降
	if rtnisExist(rtn_down) then
		rtn = cyl1_encode(1)
		count = 0
		countMax = 100
		while count < countMax do 
			state = cyl1_decode()
			if state == "DOWN" then
				print("DOWN")
				return "DOWN"
			end
			count = count + 1
			print("CYL_DOWN:", count)
			delay(100)
        end
		return "CYL ERROR"
	end
	
	// 查询气缸状态
	if rtnisExist(rtn_state) then 
		rtn = cyl1_decode
		return rtn
    end
	return "SCPI ERROR"
end

// 执行SERVO的动作和查询指令，并返回执行/查询结果
function scpi_servo_excution(str_scpi) 

	rtn_close = string.find(str_scpi, "CLOSE")
	rtn_open = string.find(str_scpi, "OPEN")
	rtn_state = string.find(str_scpi, "STATE")
	
	stop_sig = mc.getdi(14)
	cyl_state = cyl1_decode()
	if cyl_state == "DOWN" then 
		gTestStep = 0
		gSMStep = 0
		return "CYL ERROR"
    end

	// 电机关门
	if rtnisExist(rtn_close) then
		if  stop_sig == 0  then  // 机械臂限高
			gTestStep = 0
			gSMStep = 0
			return "CYL ERROR"
		end
		rtn = motion_close()
		return "CLOSE"
	end
	
	// 电机开门
	if rtnisExist(rtn_open) then
		if  stop_sig == 0  then  // 机械臂限高
			gTestStep = 0
			gSMStep = 4
			return "CYL ERROR"
		end
		rtn = motion_open()
		return "OPEN"
	end
	
	// 查询电机状态
	if rtnisExist(rtn_state) then
		rtn = motion_decode()
		return rtn
	end
	return "SCPI ERROR"
end

// 自动状态执行操作，输入CONF:TEST START/STOP
function scpi_test_excution(str_scpi) 
	rtn_check = string.find(str_scpi, "CHECK")
	rtn_start = string.find(str_scpi, "START")
	rtn_stop = string.find(str_scpi, "STOP")
	rtn_DutOK = string.find(str_scpi, "OK")
	rtn_DutNG = string.find(str_scpi, "NG")
	rtn_DutError = string.find(str_scpi, "ERROR")

	if rtnisExist(rtn_check) then 
		gTestStep = 1		// Check Mode Start
		return "CHECKED"
    end
	
	if rtnisExist(rtn_start) then 
		gTestStep = 2		// 自动状态跳转，启动连续动作
		cyl_state = cyl1_decode()
		if cyl_state == "DOWN" then 
			return "TESTING"
        end
    end
	if rtnisExist(rtn_stop) then 
		gTestStep = 3		// 自动状态
		motion_state = motion_decode()
		if motion_state == "OPEN OK" then 
			return "TESTED"
        end
    end
	
	// 输出DUT状态
	if rtnisExist(rtn_DutOK) then
		DUT_Result = 1  // 测试OK 输出10ms脉冲
		return "DUT OK"
	end
	
	if rtnisExist(rtn_DutNG) then
		DUT_Result = 0  // 测试NG 输出10ms脉冲
		return "DUT NG"
	end
	
	if rtnisExist(rtn_DutError) then
		DUT_Result = 2 // 测试ERROR 输出10ms脉冲
		return "DUT ERROR"
	end

	print("gTestStep",gTestStep)
end

function scpi_mode_excution(str_scpi) 
	rtn_at = string.find(str_scpi, " AT")
	rtn_cal = string.find(str_scpi, " CAL")
	rtn_pi = string.find(str_scpi, " PI")
	rtn_pos = string.find(str_scpi, " POS")
	rtn_state = string.find(str_scpi, "STATE")
	
	// 输出AT状态
	if rtnisExist(rtn_at) then
		return "AT"
	end
	
	// 输出CAL状态
	if rtnisExist(rtn_cal) then
		return "CAL"
	end
	
	// 输出PI状态
	if rtnisExist(rtn_pi) then
		return "PI"
	end
	
	// 追加位置
	if rtnisExist(rtn_pos) then
		rtn_pos = string.find(str_scpi,"POS")
		if rtnisExist(rtn_pos) then
		end
	end
	
	
	print("gModeState",gModeState)
	// 输出查询状态
	if rtnisExist(rtn_state) then
		if  gModeState == 0 then 
			return "AT"
        end
		
		if  gModeState == 1 then 
			return "CAL"
        end
		
		if  gModeState == 2 then 
			return "PI"
        end
	end
	
end

// 系统状态查询SCPI
function scpi_sys_excution(str_scpi) 
	print("SYSTEM",gSMStState[gSMStep+1],gSMStep)
	// 如果状态机处于初始化流程，查询过程中向外抛出锁定状态
	if gsmStep == 0 then 
		return gSMStState[gSMStep+1]
    end
	
	if gsmStep == 1 then 
		return gSMStState[gSMStep+1]
    end
	
	if gsmStep == 2 then 
		return gSMStState[gSMStep+1]
    end
	
	if gsmStep == 3 then 
		return gSMStState[gSMStep+1]
    end
	if gsmStep == 4 then 
		return gSMStState[gSMStep]
    end
	// 如果状态机解锁完毕，可正常抛出状态[IDLE, READY, RUNNING, TESTING]
end

// 修改IP
function scpi_eth_excution(str_scpi) 
	if manualMode == 0 then 
	rtn_ip = string.find(str_scpi, "IP")
    elseif manualMode == 1 then
		return "UNLOCK"
	end


	if rtnisExist(rtn_ip) then 
		str_len = string.len(str_scpi) 
		ip = string.sub(str_scpi,13,len)
		print(ip)
		return eth_modify(ip)
    end
end
