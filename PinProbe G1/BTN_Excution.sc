
// 验证压紧气缸和电机的功能
function btn_excution(param1)
	if btn_test == 1 then 
		print(" =CYL= ")
		cyl1_encode(1)
		delay(1000)
		cyl1_encode(0)
		delay(1000)
		cyl1_encode(1)
		delay(1000)
		cyl1_encode(0)
		delay(1000)
		cyl2_encode(1)
		delay(1000)
		cyl2_encode(0)
	end
end


// 两个按钮同时按压检测
function allBtnIsPress(times) 
	btnLState = mc.getdi(4)	// 获取左按钮状态，获取右按钮状态
	btnRState = mc.getdi(5)
	count = 0
	countMax = times*10
	btn_num = btnLState + btnRState
	TestMachine_AutoCheck = check_auto()
	// 必须手动开启才能按按钮
	if btn_num == 0  and TestMachine_AutoCheck == 0 then 
		while count < countMax and count > - countMax do 
				btnLState = mc.getdi(4)
				btnRState = mc.getdi(5)
				btn_num = btnLState + btnRState
			if btnLState + btnRState == 0 then 
				count = count + 1
				delay(100)
				if count == countMax then 
					print("Pressed")
					count = 0
					return "Pressed"
                end
			else
				count = count -1
				delay(100)
				if count == - countMax then 
					print("Released")
					count = 0
					return "Released"
                end
			end
			print("COUNTA:",count)
        end
    end
end

// 单独按钮按压检测
function anyBtnIsPress(times) 
	btnLState = mc.getdi(4)
	btnRState = mc.getdi(5)
	count = 0
	countMax = times*10
	btn_num = btnLState + btnRState
	TestMachine_AutoCheck = check_auto()
	// 必须手动开启才能按按钮
	if btn_num == 1 and TestMachine_AutoCheck == 0 then 
		while count < countMax and count > - countMax do 
			btnLState = mc.getdi(4)
			btnRState = mc.getdi(5)
			btn_num = btnLState + btnRState
			if btnLState == 0 or btnRState == 0 then 
				count = count + 1
				delay(100)
				if count == countMax then 
					print("Pressed")
					count = 0
					return "Pressed"
                end
			else
				count = count -1
				delay(100)
				if count == - countMax then 
					print("Released")
					count =  0
					return "Released"
                end
			end
			print("COUNTS:",count, btnLState, btnRState)
        end
    end
end

// 急停
function btn_emerged()
	btn_stop = mc.getdi(1)
	if btn_stop == 1 then 
		eth_write(0,"EMERGENCY")
		led_Redflash()
		gSMStep = 5
		gTestStep = 0
		mc.movstop(axis)
		mc..RunStop()
		return 1
    end
end

// 光栅
function btn_laser()
	btn_laser = mc.getdi(6)
	if btn_laser == 1 then 
		gSMStep = 5
		gtestStep = 0
		rtn = mc.axissts(axis,0)
		if rtn ~=0 then 
			mc.movstop(axis)
        end
    end
end

// 手动自动切换
function check_auto(param1) 
	auto_io = mc.getdi(15)
	if auto_io == 0 then
		return 1
	else
		return 0
	end
end

function check_emerage(param1)
	stop_sig = mc.getdi(14)
	if stop_sig == 0 then
		print("E_STOP")
		print("STOP_SIG:",stop_sig)
		mc.movstop(axis)
	end
end
<<<<<<< HEAD
=======
function check_emerage(param1)
	stop_sig = mc.getdi(14)
	if stop_sig == 0 then
		mc.movstop(axis)
	end
end
>>>>>>> 0fbc4e4 (fix Zero Lock)
